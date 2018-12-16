package com.sbolo.syk.fetch.distinct;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.MovieDictEnum;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.vo.LinkAnalyzeResultVO;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.service.MovieDictService;
import com.sbolo.syk.fetch.service.MovieInfoService;
import com.sbolo.syk.fetch.service.ResourceInfoService;
import com.sbolo.syk.fetch.spider.Distinct;
import com.sbolo.syk.fetch.spider.exception.AnalystException;
import com.sbolo.syk.fetch.spider.exception.DistinctException;
import com.sbolo.syk.fetch.spider.exception.SpiderException;
import com.sbolo.syk.fetch.tool.DoubanUtils;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.tool.LinkAnalyst;
import com.sbolo.syk.fetch.vo.MovieDictVO;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

@Component
public class MyDistinct implements Distinct {
	
	private static final Logger log = LoggerFactory.getLogger(MyDistinct.class);
	
	private ConcurrentMap<String, String> cacheDist;
	
	@Resource
	private MovieInfoService movieInfoService;
	
	@Resource
	private ResourceInfoService resourceInfoService;
	
	@Resource
	private MovieDictService movieDictService;
	
	@Override
	public void process(Map<String, Object> fields) throws Exception {
		Date thisTime = new Date();
		
		//获取fetchMovies
		List<MovieInfoVO> fetchMovies = new ArrayList<>();
		Collection<Object> values = fields.values();
		Iterator<Object> it = values.iterator() ;
		it.forEachRemaining(obj -> fetchMovies.add((MovieInfoVO) obj));
		
		//初步在缓存和DB中过滤movie
		List<MovieInfoVO> stepMovies = distMovie(fetchMovies, thisTime);
		
		//根据resource深入过滤movie和resource
		List<MovieInfoVO> finalMovies = distResource(stepMovies, thisTime);
		
		//整理备用资源信息
		this.assist(finalMovies, thisTime);
		
		//从movie中分离出resource
		List<ResourceInfoVO> finalResources = splitResources(finalMovies);
		
		//从movie中分离出dict
		List<MovieDictVO> finalDicts = distDict(finalMovies, thisTime);
		
		fields.clear();
		fields.put("finalMovies", finalMovies);
		fields.put("finalResources", finalResources);
		fields.put("finalDicts", finalDicts);
	}
	
	@Override
	public void after() {
		if(cacheDist != null) {
    		cacheDist.clear();
    		cacheDist = null;
    	}
	}

	@Override
	public void before() {
		cacheDist = new ConcurrentHashMap<String, String>();
	}
	
	private void assist(List<MovieInfoVO> finalMovies, Date thisTime) throws DistinctException {
		if(finalMovies == null || finalMovies.size() == 0) {
			throw new DistinctException("assist, finalMovies为空！");
		}
		
		for(MovieInfoVO finalMovie : finalMovies) {
			List<ResourceInfoVO> oneResource = finalMovie.getResourceList();
			
			//精选过后获取posterUrlList
			List<String> posterOutUrlList = DoubanUtils.getPosterUrlList(finalMovie.getPosterPageUrl(), finalMovie.getIconOutUrl());
			finalMovie.setPosterOutUrlList(posterOutUrlList);
			
			//既然走到这里，证明缓存中的resource比数据库存在更佳的resource
			ResourceInfoVO fetchOptimalResource = this.getOptimalResource(finalMovie.getCategory(), oneResource);
			
			//为finalMovie设置optimalResource的相关信息
			this.setOptimalResource(finalMovie, fetchOptimalResource);
			
			//从下载连接中分析资源信息
			this.analyzeDownloadLink(oneResource, thisTime);
		}
	}
	
	/**
	 * 批量分析下载链接并填充信息，如果是种子文件则下载到服务器，再设置。
	 * 
	 * @param fetchResource
	 * @throws Exception 
	 * @throws AnalystException
	 */
	private void analyzeDownloadLink(List<ResourceInfoVO> fetchResources, Date thisTime){
		for(ResourceInfoVO fetchResource : fetchResources) {
			String link = fetchResource.getDownloadLinkTemp();
			try {
				if(StringUtils.isBlank(link)){
					log.info("The download link of \"["+fetchResource.getPureName()+"]\" is null");
					continue;
				}

				LinkAnalyzeResultVO analyzeResult = null;
				//根據需要分析下載鏈接
				if(StringUtils.isBlank(fetchResource.getSize()) || StringUtils.isBlank(fetchResource.getFormat())){
					analyzeResult = LinkAnalyst.analyseDownloadLink(link, fetchResource.getThunderDecoding());
					if(analyzeResult != null) {
						if(StringUtils.isBlank(fetchResource.getSize())) {
							fetchResource.setSize(analyzeResult.getMovieSize());
						}
						if(StringUtils.isBlank(fetchResource.getFormat())) {
							fetchResource.setFormat(analyzeResult.getMovieFormat());
						}
						if(analyzeResult.getTorrentBytes() != null) {
							fetchResource.setTorrentBytes(analyzeResult.getTorrentBytes());
							fetchResource.setDownloadLinkTemp(analyzeResult.getDownloadLink());
							link = analyzeResult.getDownloadLink();
						}
					}
				}
				
			} catch (Throwable e) {
				//假若失败，则resource还是存的原始的地址，所以不用删除
				log.error("解析下载链接失败！movie: ["+ fetchResource.getPureName() +"] url: "+ link, e);
			}
			
		}
	}
	
	/**
	 * 为finalMovie设置最佳resource的信息
	 * @param finalMovie
	 * @param optimalResource
	 */
	private void setOptimalResource(MovieInfoVO finalMovie, ResourceInfoVO optimalResource) {
		//既然走到这里，且optimal为空，那么表明resource中只有修改，没有新增
		//仅仅只是为了将影片展示排序靠前
		if(optimalResource == null) {
			finalMovie.setResourceWriteTime(new Date());
			return;
		}
		finalMovie.setOptimalResourcePrn(optimalResource.getPrn());
		finalMovie.setResourceWriteTime(optimalResource.getCreateTime());
	}
	
	/**
	 * 在一批resource中获取最佳resource
	 * @param category
	 * @param resources
	 * @return
	 */
	private ResourceInfoVO getOptimalResource(int category, List<ResourceInfoVO> resources){
		ResourceInfoVO optimalResource = null;
		for(ResourceInfoVO resource:resources){
			
			/*因为前面的逻辑决定了，电影不会有修改，只有剧集才会有修改，
			而剧集只有在fetch到新一集的时候才会新增，故，如果不是新增，则不必变更optimal*/
			if(resource.getAction() != CommonConstants.insert) {
				continue;
			}
			
			if(optimalResource == null){
				optimalResource = resource;
				continue;
			}
			
			if(category == MovieCategoryEnum.movie.getCode()){
				if(resource.getDefinition().intValue() > optimalResource.getDefinition().intValue()){
					optimalResource = resource;
				}
			}else {
				if(resource.getEpisodeEnd().intValue() > optimalResource.getEpisodeEnd().intValue()){
					optimalResource = resource;
				}
			}
		}
		return optimalResource;
	}
	
	private List<ResourceInfoVO> splitResources(List<MovieInfoVO> finalMovies) throws DistinctException {
		if(finalMovies == null || finalMovies.size() == 0) {
			throw new DistinctException("splitResources, finalMovies为空！");
		}
		
		List<ResourceInfoVO> finalResources = new ArrayList<>();
		
		for(MovieInfoVO finalMovie : finalMovies) {
			List<ResourceInfoVO> oneResources = finalMovie.getResourceList();
			finalResources.addAll(oneResources);
			finalMovie.setResourceList(null);
		}
		
		return finalResources;
		
	}
	
	private List<MovieDictVO> distDict(List<MovieInfoVO> fetchMovies, Date thisTime) throws DistinctException{
		if(fetchMovies == null || fetchMovies.size() == 0) {
			throw new DistinctException("distDict, fetchMovies为空！");
		}
		
		Set<String> setLabels = new HashSet<>();
		Set<String> setLocations = new HashSet<>();
		for(MovieInfoVO fetchMovie : fetchMovies) {
			String labels = fetchMovie.getLabels();
			String locations = fetchMovie.getLocations();
			
			if(StringUtils.isNotBlank(labels)) {
				List<String> oneLabelList = Arrays.asList(labels.split(RegexConstant.slashSep));
				for(String oneLabel : oneLabelList) {
					//添加到Set里面去重
					setLabels.add(oneLabel);
				}
			}
			
			if(StringUtils.isNotBlank(locations)) {
				List<String> oneLocationList = Arrays.asList(locations.split(RegexConstant.slashSep));
				for(String oneLocation : oneLocationList) {
					//添加到Set里面去重
					setLocations.add(oneLocation);
				}
			}
		}
		
		String dbLabelRoot = movieDictService.getLabelRoot();
		String dbLocationRoot = movieDictService.getLocationRoot();
		List<String> dbLabelList = movieDictService.getLabels();
		List<String> dbLocationList = movieDictService.getLocations();
		
		List<MovieDictVO> fetchDicts = new ArrayList<>();
		
		if(StringUtils.isBlank(dbLabelRoot)) {
			MovieDictVO labelRoot = new MovieDictVO(MovieDictEnum.LABEL.getCode(), MovieDictEnum.ROOT.getCode(), MovieDictEnum.LABEL.getDesc(), MovieStatusEnum.available.getCode(), 1, thisTime);
			fetchDicts.add(labelRoot);
		}
		if(StringUtils.isBlank(dbLocationRoot)) {
			MovieDictVO locationRoot = new MovieDictVO(MovieDictEnum.LOCATION.getCode(), MovieDictEnum.ROOT.getCode(), MovieDictEnum.LOCATION.getDesc(), MovieStatusEnum.available.getCode(), 1, thisTime);
			fetchDicts.add(locationRoot);
		}
		
		if(dbLabelList != null && dbLabelList.size() > 0) {
			for(String fetchLabel : setLabels) {
				boolean exist = false;
				for(String dbLabel : dbLabelList) {
					if(dbLabel.equals(fetchLabel)) {
						exist = true;
						break;
					}
				}
				if(!exist) {
					MovieDictVO vo = new MovieDictVO(StringUtil.getId(CommonConstants.label_s), MovieDictEnum.LABEL.getCode(), fetchLabel, MovieStatusEnum.available.getCode(), 2, thisTime);
					fetchDicts.add(vo);
				}
			}
		}else {
			for(String fetchLabel : setLabels) {
				MovieDictVO vo = new MovieDictVO(StringUtil.getId(CommonConstants.label_s), MovieDictEnum.LABEL.getCode(), fetchLabel, MovieStatusEnum.available.getCode(), 2, thisTime);
				fetchDicts.add(vo);
			}
		}
		
		if(dbLocationList != null && dbLocationList.size() > 0) {
			for(String fetchLocation : setLocations) {
				boolean exist = false;
				for(String dbLocation : dbLocationList) {
					if(dbLocation.equals(fetchLocation)) {
						exist = true;
						break;
					}
				}
				if(!exist) {
					MovieDictVO vo = new MovieDictVO(StringUtil.getId(CommonConstants.location_s), MovieDictEnum.LOCATION.getCode(), fetchLocation, MovieStatusEnum.available.getCode(), 2, thisTime);
					fetchDicts.add(vo);
				}
			}
		}else {
			for(String fetchLocation : setLocations) {
				MovieDictVO vo = new MovieDictVO(StringUtil.getId(CommonConstants.location_s), MovieDictEnum.LOCATION.getCode(), fetchLocation, MovieStatusEnum.available.getCode(), 2, thisTime);
				fetchDicts.add(vo);
			}
		}
		
		return fetchDicts;
		
	}
	
	/**
	 * 筛选resources，并根据resources的筛选结果进一步筛选movies
	 * 先将movie为insert的resource都设置为insert
	 * 再将movie为update的resource单独设置insert or update
	 * 筛选项：
	 * 1、对resource进行筛选
	 * 2、筛选掉没有resource的movie
     * 3、标识出资源的insert or update
     * 4、竞选出optimalResource
	 * @param fetchMovies
	 * @param thisTime
	 * @return
	 * @throws DistinctException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private List<MovieInfoVO> distResource(List<MovieInfoVO> fetchMovies, Date thisTime) throws DistinctException, InstantiationException, IllegalAccessException, InvocationTargetException {
		if(fetchMovies == null || fetchMovies.size() == 0) {
			throw new DistinctException("distResource, fetchMovies为空！");
		}
		
		List<MovieInfoVO> finalMovies = new ArrayList<>();

		//过滤掉没有resource的movie，将movie为insert的标识为最终movie
		//为update的，留后处理
		Map<String, MovieInfoVO> toupMovies = new HashMap<>();
		for(MovieInfoVO fetchMovie : fetchMovies) {
			List<ResourceInfoVO> oneResources = fetchMovie.getResourceList();
			if(oneResources == null || oneResources.size() == 0) {
				log.info("未爬取到该影片的资源，跳过! movie: [{}] url: {}", fetchMovie.getPureName(), fetchMovie.getComeFromUrl());
				continue;
			}
			
			//根据下载链接或集数进行过滤，此处只会获取不会设置action，根据action过滤掉abandon
			List<ResourceInfoVO> filter1 = filterResourceInCache(fetchMovie.getCategory(), oneResources);
			if(filter1 == null || filter1.size() == 0) {
				log.info("resource过滤后该影片的资源数为0! movie: [{}] url: {}", fetchMovie.getPureName(), fetchMovie.getComeFromUrl());
				continue;
			}
			
			if(fetchMovie.getAction() == CommonConstants.insert) {
				//如果movie是insert，那么resource肯定全部是insert，且该资源在此处则告一段落
				this.setInsertResource(filter1, fetchMovie.getPrn(), thisTime);
				fetchMovie.setResourceList(filter1);
				finalMovies.add(fetchMovie);
			}else if(fetchMovie.getAction() == CommonConstants.update) {
				//如果movie是update的，那么resource暂不设置action，留着后面来处理
				toupMovies.put(fetchMovie.getPrn(), fetchMovie);
			}
		}
		
		
		//对需要update的movie和DB中最佳的资源进行比对，看是否需要写入resource
		if(toupMovies.size() == 0) {
			return finalMovies;
		}
		//获取DB中的最佳resource
		List<String> toupMoviePrns = new ArrayList<>(toupMovies.keySet());
		List<ResourceInfoEntity> dbOptimalResources = resourceInfoService.getOptimalResources(toupMoviePrns);
		if(dbOptimalResources == null || dbOptimalResources.size() == 0) {
			String toupMoviePrnStr = StringUtils.join(toupMoviePrns.toArray());
			log.warn("严重问题：前面根据查询出的movie标识为update，但现在根据已标识为update的moviePrn却查询不出结果。请检查！影片prns: "+toupMoviePrnStr);
			return finalMovies;
		}
		
		//与DB中的optimalResource进行比较，看是否fetch到的更加optimal
		for(ResourceInfoEntity dbOptimalResource : dbOptimalResources) {
			String optimalResourceMoviePrn = dbOptimalResource.getMoviePrn();
			MovieInfoVO toup = toupMovies.get(optimalResourceMoviePrn);
			if(toup == null) {
				log.warn("严重问题：前面根据查询的movie标识为udpate，但现在根据updateMovie的prn却获取不到对象。请检查！ 影片prn：{}", optimalResourceMoviePrn);
				continue;
			}
			//同DB中的optimalResource比对，并竞选出此时的真正optimal
			//对movie为update的resource进行判断，看resource为insert or update
			List<ResourceInfoVO> filters = this.filterResourceInDB(toup.getCategory(), toup.getPrn(), toup.getResourceList(), dbOptimalResource, thisTime);
			toup.setResourceList(filters);
			finalMovies.add(toup);
		}
		return finalMovies;
	}
		
		
	/**
	 * 与数据库中的resource对比进行过滤
	 * @param category 类型 tv/movie
	 * @param fetchResources 获取到的所有resource
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private List<ResourceInfoVO> filterResourceInDB(int category, String moviePrn, 
			List<ResourceInfoVO> fetchResources, 
			ResourceInfoEntity dbOptimalResource, Date thisTime) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		//过滤完成后剩下的资源
		List<ResourceInfoVO> filterList = new ArrayList<>();
		
		//将数据库查询出的数据转换为VO
		ResourceInfoVO dbOptimalResourceVO = VOUtils.po2vo(dbOptimalResource, ResourceInfoVO.class);
		
		for(ResourceInfoVO fetchResource : fetchResources){
			if(Pattern.compile(RegexConstant.baiduNet).matcher(fetchResource.getDownloadLinkTemp()).find()) {
				fetchResource.setAction(CommonConstants.abandon);
				continue;
			}
			
			int action = resourceCompare(category, fetchResource, dbOptimalResourceVO);
			if(action == CommonConstants.abandon){
				fetchResource.setAction(action);
				continue;
			}
			
			if(action == CommonConstants.insert){
				fetchResource.setAction(action);
				fetchResource.setCreateTime(thisTime);
				fetchResource.setMoviePrn(moviePrn);
			}else if(action == CommonConstants.update){
				fetchResource = FetchUtils.resourceChangeOption(dbOptimalResource, fetchResource);
				fetchResource.setPureName(dbOptimalResource.getPureName());
				fetchResource.setAction(CommonConstants.update);
				fetchResource.setUpdateTime(thisTime);
				fetchResource.setPrn(dbOptimalResource.getPrn());
			}
			//设置action在后续插入数据库时使用
			filterList.add(fetchResource);
		}
		
		return filterList;
	}
	
	/**
	 * 标识为插入的resource
	 * @param fetchResources
	 * @param moviePrn
	 * @param thisTime
	 */
	private void setInsertResource(List<ResourceInfoVO> fetchResources, String moviePrn, Date thisTime) {
		for(ResourceInfoVO fetchResource : fetchResources) {
			if(Pattern.compile(RegexConstant.baiduNet).matcher(fetchResource.getDownloadLinkTemp()).find()) {
				fetchResource.setAction(CommonConstants.abandon);
				continue;
			}
			fetchResource.setAction(CommonConstants.insert);
			fetchResource.setCreateTime(thisTime);
			fetchResource.setMoviePrn(moviePrn);
		}
	}
	
	/**
	 * 根据下载链接或集数来过滤
	 * 通过map的key来进行过滤
	 * 标识资源为insert or update
	 * 
	 * @param category 类型 tv/movie
	 * @param fetchResources 获取到的所有resource
	 * @return
	 */
	private List<ResourceInfoVO> filterResourceInCache(int category, List<ResourceInfoVO> fetchResources){
		//过滤完成后剩下的资源
		Map<String, ResourceInfoVO> filterMap = new HashMap<String, ResourceInfoVO>();
		
		for(ResourceInfoVO fetchResource:fetchResources){
			//是否是可支持的下载链接
			if(!this.isCanableLink(fetchResource.getDownloadLinkTemp())) {
				continue;
			}
			
			String filterKey = fetchResource.getDownloadLinkTemp();
			if(category == MovieCategoryEnum.movie.getCode()){
				ResourceInfoVO filterResource = filterMap.get(filterKey);
				if(filterResource != null){
					continue;
				}
			}else {
				if(fetchResource.getEpisodeEnd() == null){
	    			log.warn("url:{}, 电影在豆瓣中被标记为电视剧，但是资源[{}]却未获得第几集，跳过该资源！",fetchResource.getComeFromUrl(), fetchResource.getPureName());
					continue;
	    		}
				//根据Map的key来过滤
				filterKey = fetchResource.getEpisodeEnd()+"";
				if(fetchResource.getEpisodeStart() != null){
					filterKey = fetchResource.getEpisodeStart() + "-" + fetchResource.getEpisodeEnd();
				}
				ResourceInfoVO filterResource = filterMap.get(filterKey);
				if(filterResource != null){
					//获取action，过滤掉abandon
					int action = resourceCompare(category, fetchResource, filterResource);
					if(action == CommonConstants.abandon){
						continue;
					}
				}
			}
			filterMap.put(filterKey, fetchResource);
		}
		return new ArrayList<ResourceInfoVO>(filterMap.values());
	}
	
	/**
	 * 将新获得的resource与缓存中的最佳resource进行比较
	 * @param category 类别 tv/movie
	 * @param fetchResource 最新获取到resource
	 * @param optimalResource 最佳的resource
	 * @return
	 */
	private int resourceCompare(int category, ResourceInfoVO fetchResource, ResourceInfoVO optimalResource){
		
		Integer fetchEpisodeEnd = fetchResource.getEpisodeEnd();
		String fetchSubtitle = fetchResource.getSubtitle();
		Integer fetchDefinition = fetchResource.getDefinition();
		
		Integer optimalEpisodeEnd = optimalResource.getEpisodeEnd();
		String optimalSubtitle = optimalResource.getSubtitle();
		Integer optimalDefinition = optimalResource.getDefinition();
		
		if(category == MovieCategoryEnum.movie.getCode()){
			if(fetchDefinition.intValue() > optimalDefinition.intValue()){
				return CommonConstants.insert;
			}else {
				return CommonConstants.abandon;
			}
		}else {
			
			if(fetchEpisodeEnd.intValue() > optimalEpisodeEnd.intValue()){
				//如果是新的一集
				return CommonConstants.insert;
			}else if(fetchEpisodeEnd.intValue() == optimalEpisodeEnd.intValue()){
				//如果集数相同
				if(StringUtils.isNotBlank(fetchSubtitle) && StringUtils.isBlank(optimalSubtitle)){
					//如果新的有字幕，而最佳的没有字幕
					return CommonConstants.update;
				}else if((StringUtils.isNotBlank(fetchSubtitle) && StringUtils.isNotBlank(optimalSubtitle)) ||
						(StringUtils.isBlank(fetchSubtitle) && StringUtils.isBlank(optimalSubtitle))){
					//如果都没有字幕或者都有字幕
					if(fetchDefinition.intValue() > optimalDefinition.intValue()){
						//若果新的清晰度更高
						return CommonConstants.update;
					}else {
						return CommonConstants.abandon;
					}
				}else {
					return CommonConstants.abandon;
				}
			}else {
				return CommonConstants.abandon;
			}
		}
		
	}
	
	/**
	 * 是否是支持的下载链接
	 * @param fetchResource
	 * @return
	 */
	private boolean isCanableLink(String downloadLink) {
		if(StringUtils.isBlank(downloadLink)) {
			return false;
		}
		
		if(Pattern.compile(RegexConstant.ed2k).matcher(downloadLink).find() || 
				Pattern.compile(RegexConstant.thunder).matcher(downloadLink).find() || 
				Pattern.compile(RegexConstant.torrent).matcher(downloadLink).find() || 
				Pattern.compile(RegexConstant.magnet).matcher(downloadLink).find()){
			return true;
		}
		return false;
	}
	
	/**
	 * 对fetchMovies进行初步的筛选以及标识insert or update
	 * 过滤项：
	 * 1、相同的pureName和releaseTime
	 * 2、是否全为insert
	 * 3、甄选inser or update 并获取update项
	 * @param fetchMovies
	 * @param thisTime
	 * @return
	 * @throws DistinctException
	 */
	private List<MovieInfoVO> distMovie(List<MovieInfoVO> fetchMovies, Date thisTime) throws DistinctException {
		if(fetchMovies == null || fetchMovies.size() == 0) {
			throw new DistinctException("distMovie,fetch到的movies为空！");
		}
		
		//1、过滤相同的pureName和releaseTime
		List<MovieInfoVO> filter1 = new ArrayList<>();
		List<String> pureNames = new ArrayList<>();
		for(MovieInfoVO vo : fetchMovies) {
			String comeFromUrl = vo.getComeFromUrl();
			if(isRepeatedMovieInCache(vo, comeFromUrl)) {
				continue;
			}
			pureNames.add(vo.getPureName());
			filter1.add(vo);
		}
		
		//2、是否全为insert
		List<MovieInfoEntity> dbMovies = movieInfoService.getByPureNames(pureNames);
		//如果DB中查询为空，则直接全部插入
		if(dbMovies == null || dbMovies.size() == 0) {
			for(MovieInfoVO dist : filter1) {
				//给予标识，标识插入
				dist.setAction(CommonConstants.insert);
			}
			return filter1; 
		}
		
		//3、标识inser or update 并获取update项
		List<MovieInfoVO> movieFilter2 = new ArrayList<>();
		for(MovieInfoVO fetchMovie : filter1) {
			boolean isExist = false;
			String fetchPureName = fetchMovie.getPureName();
			Date fetchReleaseTime = fetchMovie.getReleaseTime();
			for(MovieInfoEntity dbMovie : dbMovies) {
				String dbPureName = dbMovie.getPureName();
				Date dbReleaseTime = dbMovie.getReleaseTime();
				
				//根据pureName和releaseTime进行比对
				if(fetchPureName.equals(dbPureName) && 
						fetchReleaseTime.getTime() <= dbReleaseTime.getTime()) {
					isExist = true;
					//获取需要修改的项
					MovieInfoVO changeOption = FetchUtils.movieChangeOption(dbMovie, fetchMovie, thisTime);
					if(changeOption == null){
						//如果没有改变，则new一个新的，用作放置optimalResourcePrn
						changeOption = new MovieInfoVO();
					}
					//给予标识，标识修改
					changeOption.setAction(CommonConstants.update);
					changeOption.setPrn(dbMovie.getPrn());
					changeOption.setUpdateTime(thisTime);
					changeOption.setResourceList(fetchMovie.getResourceList());
					//后面会用到category，所以没办法，必须设置
					changeOption.setCategory(dbMovie.getCategory());
					changeOption.setComeFromUrl(fetchMovie.getComeFromUrl());
					movieFilter2.add(changeOption);
					break;
				}
			}
			
			//db遍历结束后还是不存在则表示是新的
			if(!isExist) {
				fetchMovie.setAction(CommonConstants.insert);
				movieFilter2.add(fetchMovie);
			}
		}
		
		return movieFilter2;
	}
	
	/**
	 * 判断该影片是否在缓存中出现过，
	 * 避免出现一个网站两个相同影片，fetchPureName+fetchReleaseTimeStr做为key来过滤
	 * @param fetchMovie
	 * @param comeFromUrl
	 * @throws SpiderException
	 */
	private boolean isRepeatedMovieInCache(MovieInfoVO fetchMovie, String comeFromUrl) {
		String fetchPureName = fetchMovie.getPureName();
		String fetchReleaseTimeStr = fetchMovie.getReleaseTimeStr();
		String repeatedUrl = cacheDist.get(fetchPureName+fetchReleaseTimeStr);
		if(repeatedUrl != null){
			log.info("warning!!! ["+fetchMovie.getPureName()+"] has appeared in this web. between "+ repeatedUrl +" and " + comeFromUrl);
			return true;
		}
		cacheDist.put(fetchPureName+fetchReleaseTimeStr, comeFromUrl);
		return false;
	}
	
	

}
