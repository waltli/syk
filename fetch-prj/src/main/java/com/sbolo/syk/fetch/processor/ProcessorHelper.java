package com.sbolo.syk.fetch.processor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import okhttp3.Response;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.MovieQualityEnum;
import com.sbolo.syk.common.constants.MovieResolutionConstant;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.http.callback.HttpSendCallbackPure;
import com.sbolo.syk.common.tools.BucketUtils;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.UIDGenerator;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.vo.LinkAnalyzeResultVO;
import com.sbolo.syk.fetch.entity.MovieFileIndexEntity;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.MovieLabelEntity;
import com.sbolo.syk.fetch.entity.MovieLocationEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.mapper.MovieFileIndexMapper;
import com.sbolo.syk.fetch.service.MovieInfoService;
import com.sbolo.syk.fetch.service.MovieLabelService;
import com.sbolo.syk.fetch.service.MovieLocationService;
import com.sbolo.syk.fetch.service.ResourceInfoService;
import com.sbolo.syk.fetch.spider.exception.AnalystException;
import com.sbolo.syk.fetch.spider.exception.SpiderException;
import com.sbolo.syk.fetch.tool.DoubanUtils;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.tool.LinkAnalyst;
import com.sbolo.syk.fetch.vo.ConcludeVO;
import com.sbolo.syk.fetch.vo.LinkInfoVO;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.MovieLabelVO;
import com.sbolo.syk.fetch.vo.MovieLocationVO;
import com.sbolo.syk.fetch.vo.PicVO;
import com.sbolo.syk.fetch.vo.PureNameAndSeasonVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

@Component
public class ProcessorHelper {
	private static final Logger log = LoggerFactory.getLogger(ProcessorHelper.class);
	private ConcurrentMap<String, String> cacheDist;

	private String startUrl;
	
	public String getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}
	
	@Resource
	private ResourceInfoService resourceInfoService;
	
	@Resource
	private MovieInfoService movieInfoService;
	
	@Resource
	private MovieLabelService movieLabelService;
	
	@Resource
	private MovieLocationService movieLocationService;
	
	@Resource
	private MovieFileIndexMapper movieFileIndexMapper;
	
	
	protected ConcludeVO resolve(PureNameAndSeasonVO pureNameAndSeanson, List<String> precisions, List<LinkInfoVO> links, List<String> shots, String comeFromUrl, String doubanUrl) throws Exception {
		if(links == null || links.size() == 0) {
			log.info("No donload link! movie: [{}] url: {}", pureNameAndSeanson.getPureName(), comeFromUrl);
			return null;
		}
		
		Date thisTime = new Date();
		
		//判断豆瓣URL是否为空，因为有些网站已经将豆瓣URL贴上了
		if(StringUtils.isBlank(doubanUrl)){
			doubanUrl = DoubanUtils.getDoubanUrl(pureNameAndSeanson, precisions);
		}
		
		//从豆瓣中获取到准确的movie信息
		MovieInfoVO fetchMovie = DoubanUtils.fetchMovieFromDouban(doubanUrl, thisTime);
		
		//判断在这次扫描中是否有重复出现过
		boolean isRepeated = this.isRepeatedMovieInCache(fetchMovie, comeFromUrl);
		if(isRepeated) {
			return null;
		}
		
		//与数据库中的该条movie对比，是否有，是否有不同，最终获取需要写入的movie
		MovieInfoVO finalMovie = this.filterMovieInDb(fetchMovie, thisTime);
		
		//根据扫描的信息构建resource对象
		List<ResourceInfoVO> fetchResources = this.getResources(links, fetchMovie, shots, comeFromUrl, thisTime);
		
		//在缓存中过滤一批resource
		List<ResourceInfoVO> filter1 = this.filterResourceInCache(fetchMovie.getCategory(), fetchResources);
		
		if(filter1 == null || filter1.size() == 0) {
			log.info("No canable resource after fileter in cache! movie: [{}] url: {}", finalMovie.getPureName(), comeFromUrl);
			return null;
		}
		
		//如果影片的action等于insert，那就表示是新电影，库中肯定没有资源
		List<ResourceInfoVO> filter2 = null;
		ResourceInfoEntity dbOptimalResource = null;
		if(finalMovie.getAction() == CommonConstants.insert) {
			filter2 = this.setAndGetInsertResource(filter1, finalMovie.getPrn(), thisTime);
		}else {
			//获取到数据库中该影片质量最好的resource
			dbOptimalResource = resourceInfoService.getOptimalResource(finalMovie.getPrn());
			
			if(dbOptimalResource == null) {
				filter2 = this.setAndGetInsertResource(filter1, finalMovie.getPrn(), thisTime);
			}else {
				//在数据库中过滤一批reousrce
				filter2 = this.filterResourceInDB(fetchMovie.getCategory(), finalMovie.getPrn(), filter1, dbOptimalResource, thisTime);
			}
		}
		
		if(filter2 == null || filter2.size() == 0) {
			log.info("No canable resource after filter in db! movie: [{}] url: {}", finalMovie.getPureName(), comeFromUrl);
			return null;
		}
		
		//精选过后获取posterUrlList
		List<String> posterOutUrlList = DoubanUtils.getPosterUrlList(finalMovie.getPosterPageUrl(), finalMovie.getIconOutUrl());
		finalMovie.setPosterOutUrlList(posterOutUrlList);
		
		//既然走到这里，证明缓存中的resource比数据库存在更佳的resource
		ResourceInfoVO fetchOptimalResource = this.getOptimalResource(fetchMovie.getCategory(), filter2);
		
		//为finalMovie设置optimalResource的相关信息
		this.setOptimalResource(finalMovie, fetchOptimalResource);
		
		//从下载连接中分析资源信息
		this.analyzeDownloadLink(filter2, thisTime);
		
		return new ConcludeVO(finalMovie, filter2);
	}
	
	/**
	 * 标识为插入的resource
	 * @param fetchResources
	 * @param moviePrn
	 * @param thisTime
	 */
	private List<ResourceInfoVO> setAndGetInsertResource(List<ResourceInfoVO> fetchResources, String moviePrn, Date thisTime) {
		for(ResourceInfoVO fetchResource : fetchResources) {
			if(Pattern.compile(RegexConstant.baiduNet).matcher(fetchResource.getDownloadLinkTemp()).find()) {
				fetchResource.setAction(CommonConstants.abandon);
				continue;
			}
			fetchResource.setAction(CommonConstants.insert);
			fetchResource.setCreateTime(thisTime);
			fetchResource.setMoviePrn(moviePrn);
		}
		return fetchResources;
	}
	
    public PureNameAndSeasonVO getPureNameAndSeason(String pureName, String fullName) throws SpiderException{
    	
    	if(pureName == null && fullName == null){
    		throw new SpiderException("pureName and fullName all are null!");
    	}

    	if(fullName == null){
    		fullName = "";
    	}
    	
    	if(pureName == null){
    		Matcher m = Pattern.compile(RegexConstant.pure_name).matcher(fullName);
    		if(!m.find()){
    			throw new SpiderException("Failure to fetch pureName from \""+fullName+"\", so jump out of the page!");
    		}
    		pureName = m.group();
    	}else {
    		int pointIdx = pureName.indexOf(".");
    		if(pointIdx > -1){
    			pureName = pureName.substring(0, pointIdx).trim();
    			fullName += pureName.substring(pointIdx);
    		}
    		
    		int slashIdx = pureName.indexOf("/");
    		if(slashIdx > -1){
    			pureName = pureName.substring(0, slashIdx).trim();
    			fullName += pureName.substring(slashIdx);
    		}
    	}
    	
    	//filter pureName
    	String name = pureName;
    	pureName = Pattern.compile(RegexConstant.types).matcher(name).replaceAll("").trim();
		if(StringUtils.isBlank(pureName)){
			throw new SpiderException("The pureName is null after delete types. from \""+name+"\"");
		}
    	
    	String desc = fullName;
    	if(!fullName.contains(pureName)){
    		desc = pureName+fullName;
    	}
    	
		Integer season = null;
		String cnSeason = null;
		String noSeasonName = pureName;
		for(int i=0; i<RegexConstant.list_season.size(); i++){
			Matcher m2 = RegexConstant.list_season.get(i).matcher(StringUtil.trimAll(desc));
			if(m2.find()){
				//防止名称和季数中间没有任何间隔 例：越狱第五季
				int seasonIdx = pureName.indexOf(m2.group());
				if(seasonIdx != -1){
					noSeasonName = pureName.substring(0, seasonIdx).trim();
				}
				season = Utils.chineseNumber2Int(m2.group(1));
				cnSeason = "第"+Utils.int2ChineseNumber(m2.group(1))+"季";
				pureName = noSeasonName +" "+cnSeason;
				break;
			}
		}
		return new PureNameAndSeasonVO(pureName, noSeasonName, season, cnSeason);
    }
    
    protected void destroy(){
    	if(cacheDist != null) {
    		cacheDist.clear();
    		cacheDist = null;
    	}
    }
    
    protected void init() {
    	cacheDist = new ConcurrentHashMap<String, String>();
    }
    
    protected List<String> getPrecisionsByInfo(String infos, String separator, String filter) {
    	String[] filters = new String[] {filter};
    	return getPrecisionsByInfo(infos, separator, filters);
    }
    
    protected List<String> getPrecisionsByInfo(String infos, String separator) {
    	return getPrecisionsByInfo(infos, separator, new String[] {});
    }
    
    protected List<String> getPrecisionsByInfo(String infos, String separator, String[] filters) {
    	if(StringUtils.isBlank(infos)) {
    		return null;
    	}
    	
    	if(filters != null && filters.length > 0) {
    		for(String filter : filters) {
    			infos = infos.replace(filter, "");
    		}
    	}
		String[] texts = StringUtil.trimAll(infos.replaceAll(separator, CommonConstants.SEPARATOR)   //将separator更换为自定义的区别符
				.replaceAll("<.*?>", ""))   //去除所有的html标签
				.split(CommonConstants.SEPARATOR);
		String directDesc = "";
		String castDesc = "";
		for(String text : texts) {
			if(Pattern.compile(RegexConstant.DYtitle).matcher(text).find() && StringUtils.isBlank(directDesc)){
				directDesc = StringUtil.replaceHTML(text);
			}else if(Pattern.compile(RegexConstant.YYtitle+"|"+RegexConstant.ZYtitle).matcher(text).find() && StringUtils.isBlank(castDesc)){
				castDesc = StringUtil.replaceHTML(text);
			}
			if(StringUtils.isNotBlank(directDesc) && StringUtils.isNotBlank(castDesc)){
				break;
			}
		}
		List<String> precisions = getPrecisions(directDesc, castDesc);
		return precisions;
	}
    
//    protected List<String> getPrecisionsByInfo(List<TextNode> textNodes) {
//		String directDesc = "";
//		String castDesc = "";
//		for(TextNode textNode : textNodes) {
//			String text = textNode.text();
//			if(Pattern.compile(RegexConstant.DYtitle).matcher(text).find() && StringUtils.isBlank(directDesc)){
//				directDesc = text;
//			}else if(Pattern.compile(RegexConstant.YYtitle+"|"+RegexConstant.ZYtitle).matcher(text).find() && StringUtils.isBlank(castDesc)){
//				castDesc = text;
//			}
//			if(StringUtils.isNotBlank(directDesc) && StringUtils.isNotBlank(castDesc)){
//				break;
//			}
//		}
//		List<String> precisions = getPrecisions(directDesc, castDesc);
//		return precisions;
//	}
    
    protected List<String> getPrecisions(String directDesc, String castDesc){
    	List<String> precisions = new ArrayList<String>();
    	String[] directNamesArr = null;
    	String[] castNamesArr = null;
    	if(StringUtils.isNotBlank(directDesc)){
    		String noTitleDirect = Pattern.compile(RegexConstant.DYtitle).matcher(directDesc).replaceAll("");
    		String directNames = StringUtil.trim(noTitleDirect);
    		directNamesArr = directNames.split(RegexConstant.slashSep);
    	}
    	if(StringUtils.isNotBlank(castDesc)){
    		String noTitleCast = Pattern.compile(RegexConstant.YYtitle+"|"+RegexConstant.ZYtitle).matcher(castDesc).replaceAll("");
    		String castNames = StringUtil.trim(noTitleCast);
        	castNamesArr = castNames.split(RegexConstant.slashSep);
    	}
    	
    	if(directNamesArr == null && castNamesArr == null){
    		return precisions;
    	}else if(castNamesArr == null || (directNamesArr != null && directNamesArr.length >= 2)){
    		getPrecisions(precisions, directNamesArr, 2);
    	}else if(directNamesArr == null){
    		getPrecisions(precisions, castNamesArr, 2);
    	}else {
    		getPrecisions(precisions, directNamesArr, 1);
    		getPrecisions(precisions, castNamesArr, 1);
    	}
		return precisions;
    }
    
    private void getPrecisions(List<String> precisions, String[] namesArr, int count){
    	for(String name : namesArr){
    		if(Pattern.compile("^"+RegexConstant.chinese).matcher(name).find()){
    			name = name.replaceAll("[a-zA-Z]+・?[a-zA-Z]+", "");    //某些网站将中文名和英文名一并写上了，所以去掉英文名
    			name = name.split(" ")[0].replace("・", "·");  //豆瓣采用的是中文点
    		}else {
    			name = name.replaceAll("・|·", " ");  //如果是英文，豆瓣没有点，所以更换为空格
    		}
    		precisions.add(name);
    		if(precisions.size() >= count){
				break;
			}
    	}
    }
    
    
	
	/**
	 * 判断该影片是否在缓存中出现过，
	 * 避免出现一个网站两个相同影片，fetchPureName+fetchReleaseTimeStr做为key来过滤
	 * @param fetchMovie
	 * @param comeFromUrl
	 * @throws SpiderException
	 */
	private boolean isRepeatedMovieInCache(MovieInfoVO fetchMovie, String comeFromUrl) throws SpiderException {
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
	
	/**
	 * 与数据库中的该条movie对比，是否有，是否有不同，最终获取需要写入的movie
	 * 如果无更新项则new一个空的，待后面更新optimalResourcePrn
	 * @param fetchMovie
	 * @return
	 * @throws ParseException 
	 */
	private MovieInfoVO filterMovieInDb(MovieInfoVO fetchMovie, Date thisTime) throws ParseException {
		//根据条件从数据库中查询出相应的movie
		String pureName = fetchMovie.getPureName();
		Date releaseTime = fetchMovie.getReleaseTime();
		String yearStr = fetchMovie.getReleaseTimeStr().substring(0,4);
		MovieInfoEntity dbMovie = movieInfoService.getOneByPureNameAndYear(pureName, DateUtil.str2Date(yearStr, "yyyy"));
		
		//如果数据库中查询为空，则直接全部插入
		if(dbMovie == null) {
			//给予标识，标识插入
			fetchMovie.setAction(CommonConstants.insert);
			List<MovieLabelVO> labelList = this.buildLabels(fetchMovie.getLabels(), fetchMovie.getPrn(), pureName, releaseTime, thisTime);
			List<MovieLocationVO> locationList = this.buildLocations(fetchMovie.getLocations(), fetchMovie.getPrn(), pureName, releaseTime, thisTime);
			fetchMovie.setLabelList(labelList);
			fetchMovie.setLocationList(locationList);
			return fetchMovie;
		}
		//获取有改变的属性项
		List<MovieLabelEntity> dbLabels = movieLabelService.getListByMoviePrn(dbMovie.getPrn());
		List<MovieLocationEntity> dbLocations = movieLocationService.getListByMoviePrn(dbMovie.getPrn());
		MovieInfoVO changeOption = FetchUtils.movieChangeOption(dbMovie, fetchMovie, dbLabels, dbLocations, thisTime);
		if(changeOption == null){
			//如果没有改变，则new一个新的，用作放置optimalResourcePrn
			changeOption = new MovieInfoVO();
		}
		//给予标识，标识修改
		changeOption.setAction(CommonConstants.update);
		changeOption.setPrn(dbMovie.getPrn());
		changeOption.setUpdateTime(thisTime);
		return changeOption;
		
	}
	
	/**
	 * 根据爬取到的链接集合等信息
	 * 构建出ResourceInfo
	 * @param links 下载链接集合
	 * @param fetchMovie 电影信息
	 * @param printScreens 电影截图
	 * @param comeFromUrl 爬取的网站
	 * @return
	 */
	private List<ResourceInfoVO> getResources(List<LinkInfoVO> links, MovieInfoVO fetchMovie, List<String> shots, String comeFromUrl, Date thisTime){
		List<ResourceInfoVO> resources = new ArrayList<ResourceInfoVO>();
		String anotherName = fetchMovie.getAnotherName();
		for(LinkInfoVO link:links){
			if(!Pattern.compile(RegexConstant.resource_protocol).matcher(link.getDownloadLink()).find()){
				log.info("The link of \""+link.getDownloadLink()+"\", that doesn't matching protocol, Supported: ed2k,thunder,magnet,torrent or baiduNet");
				continue;
			}
			link.setName(stripEngName(link.getName(), anotherName));
			ResourceInfoVO fetchResource = buildResourceInfoFromLinkName(fetchMovie, link, comeFromUrl, thisTime);
			fetchResource.setShotOutUrlList(shots);
			resources.add(fetchResource);
		}
		return resources;
	}
	
	/**
	 * 在cache中进行过滤
	 * 通过map的key来进行过滤
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
	 * 与数据库中的resource对比进行过滤
	 * @param category 类型 tv/movie
	 * @param fetchResources 获取到的所有resource
	 * @return
	 */
	private List<ResourceInfoVO> filterResourceInDB(int category, String moviePrn, 
			List<ResourceInfoVO> fetchResources, 
			ResourceInfoEntity dbOptimalResource, Date thisTime){
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
	 * 去除英文名字，因为英文名字会干扰....
	 * @param downloadLinkName
	 * @param anotherName
	 * @return
	 */
	private String stripEngName(String downloadLinkName, String anotherName){
    	if(StringUtils.isBlank(anotherName)){
    		return downloadLinkName;
    	}
    	String engNames = Pattern.compile(RegexConstant.not_word_blank).matcher(anotherName).replaceAll("");
    	String[] engNamesSplit = engNames.split(" ");
    	for(String word : engNamesSplit){
    		if(word.length() <= 1){
    			continue;
    		}
    		downloadLinkName = Pattern.compile("(?i)"+word).matcher(downloadLinkName).replaceAll("");
		}
    	return downloadLinkName;
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
			if(optimalResource == null){
				optimalResource = resource;
				continue;
			}
			
			if(category == MovieCategoryEnum.movie.getCode()){
				if(resource.getDefinition().intValue() > optimalResource.getDefinition().intValue()){
					optimalResource = resource;
				}
			}else {
				log.warn("========测试：resource.getEpisodeEnd(): {}, optimalResource.getEpisodeEnd(): {}========", resource.getEpisodeEnd(), optimalResource.getEpisodeEnd());
				if(resource.getEpisodeEnd().intValue() > optimalResource.getEpisodeEnd().intValue()){
					optimalResource = resource;
				}
			}
		}
		return optimalResource;
	}
	
	/**
	 * 为finalMovie设置最佳resource的信息
	 * @param finalMovie
	 * @param optimalResource
	 */
	private void setOptimalResource(MovieInfoVO finalMovie, ResourceInfoVO optimalResource) {
		finalMovie.setOptimalResourcePrn(optimalResource.getPrn());
		finalMovie.setResourceWriteTime(optimalResource.getCreateTime());
	}
	
	
	/**
	 * 构建label信息
	 * 
	 * @param labelsStr
	 * @param moviePrn
	 * @param pureName
	 * @param releaseTime
	 * @return
	 */
	private List<MovieLabelVO> buildLabels(String labelsStr, String moviePrn, String pureName, Date releaseTime, Date thisTime){
		if(StringUtils.isBlank(labelsStr)){
			labelsStr = "剧情";
		}
		String[] labelsSplit = labelsStr.split(RegexConstant.slashSep);
		List<MovieLabelVO> labels = new ArrayList<MovieLabelVO>();
		for(int i=0; i<labelsSplit.length; i++){
			String labelName = labelsSplit[i];
			MovieLabelVO label = MovieLabelVO.buildLabel(labelName, moviePrn, pureName, releaseTime, thisTime);
			labels.add(label);
		}
		return labels;
	}
	
	/**
	 * 构建location对象
	 * 
	 * @param locationsStr
	 * @param moviePrn
	 * @param pureName
	 * @param releaseTime
	 * @return
	 */
	private List<MovieLocationVO> buildLocations(String locationsStr, String moviePrn, String pureName, Date releaseTime, Date thisTime){
		if(StringUtils.isBlank(locationsStr)){
			locationsStr = "中国大陆";
		}
		String[] locationsSplit = locationsStr.split(RegexConstant.slashSep);
		List<MovieLocationVO> locations = new ArrayList<MovieLocationVO>();
		for(int i=0; i<locationsSplit.length; i++){
			String locationStr = locationsSplit[i];
			//地区只要中文，如果实在没有也没有办法！
			Matcher m = Pattern.compile(RegexConstant.chinese).matcher(locationStr);
			if(!m.find()){
				continue;
			}
			String locationName = m.group();
			MovieLocationVO location = MovieLocationVO.buildLocation(locationName, moviePrn, pureName, releaseTime, thisTime);
			locations.add(location);
		}
		return locations;
	}
	
	/**
     * 根据下载链接的名字，拼装ResourceInfoVO
     * 涵盖字段：
     * resourcePrn,moviePrn,season,speed,definition,quality,resolution,subtitle,format,size,episode,createTime
     * 缺乏：
     * listPhotos,downloadLink,comeFromUrl
     * 
     * @param downloadLinkName
     * @return
     */
    private ResourceInfoVO buildResourceInfoFromLinkName(MovieInfoVO fetchMovie, LinkInfoVO link, String comeFromUrl, Date thisTime){
    	String pureName = fetchMovie.getPureName();
    	Date releaseTime = fetchMovie.getReleaseTime();
    	Integer season = fetchMovie.getPresentSeason();
    	Integer category = fetchMovie.getCategory();
    	Integer totalEpisode = fetchMovie.getTotalEpisode();
    	String moviePrn = fetchMovie.getPrn();
    	
    	String downloadLinkName = link.getName();
    	String downloadLink = link.getDownloadLink();
    	String thunderDecoding = link.getLinkDecoding();
    	
    	//从name中获取resource的相关信息
    	ResourceInfoVO newResource = FetchUtils.buildResouceInfoFromName(downloadLinkName, category, season, totalEpisode);
    	
    	newResource.setMoviePrn(moviePrn);
    	newResource.setPureName(pureName);
    	newResource.setReleaseTime(releaseTime);
    	newResource.setComeFromUrl(comeFromUrl);
    	newResource.setDownloadLinkTemp(downloadLink);
    	
    	String resourcePrn = StringUtil.getId(CommonConstants.resource_s);
    	newResource.setPrn(resourcePrn);
    	newResource.setSpeed(5);
    	newResource.setSt(MovieStatusEnum.available.getCode());
    	
    	
    	if(Pattern.compile(RegexConstant.thunder).matcher(downloadLink).find()){
    		newResource.setThunderDecoding(thunderDecoding);
    	}
		
		newResource.setCreateTime(thisTime);
		return newResource;
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
	
}
