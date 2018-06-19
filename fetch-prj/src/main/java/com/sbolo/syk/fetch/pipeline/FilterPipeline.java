package com.sbolo.syk.fetch.pipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import okhttp3.Response;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.MovieQualityEnum;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.exception.AnalystException;
import com.sbolo.syk.common.exception.MovieInfoFetchException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.http.callback.HttpSendCallbackPure;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.LinkAnalyst;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.common.vo.LinkAnalyzeResultVO;
import com.sbolo.syk.fetch.dao.MovieResourceEntityMapper;
import com.sbolo.syk.fetch.po.LabelMappingEntity;
import com.sbolo.syk.fetch.po.LocationMappingEntity;
import com.sbolo.syk.fetch.po.MovieInfoEntity;
import com.sbolo.syk.fetch.po.MovieResourceEntity;
import com.sbolo.syk.fetch.po.ResourceInfoEntity;
import com.sbolo.syk.fetch.service.MovieInfoBizService;
import com.sbolo.syk.fetch.service.ResourceInfoBizService;
import com.sbolo.syk.fetch.spider.Pipeline;
import com.sbolo.syk.fetch.vo.MovieFileUrl;
import com.sbolo.syk.fetch.vo.GatherVO;
import com.sbolo.syk.fetch.vo.ConcludeVO;
import com.sbolo.syk.fetch.vo.LinkInfoVO;

@Component
public class FilterPipeline implements Pipeline {
	
	private static final Logger log = LoggerFactory.getLogger(FilterPipeline.class);

	@Resource
	private MovieInfoBizService movieInfoBizService;
	
	@Resource
	private MovieResourceEntityMapper movieResourceEntityMapper;
	
	@Resource
	private ResourceInfoBizService resourceInfoBizService;
	
	@Resource
	private ThreadPoolTaskExecutor threadPool;
	
	
	private String iconDir = ConfigUtil.getPropertyValue("iconDir");
	private String posterDir = ConfigUtil.getPropertyValue("posterDir");
	private String photoDir = ConfigUtil.getPropertyValue("photoDir");
	private String torrentDir = ConfigUtil.getPropertyValue("torrentDir");

	private ConcurrentMap<String, Integer> fileNameDistinct = new ConcurrentHashMap<String, Integer>(); 
	
	private ConcurrentMap<String, String> photoUrlMapping = new ConcurrentHashMap<String, String>(); 
	
	private ConcurrentMap<String, String> cacheDistinct = new ConcurrentHashMap<String, String>();
	
	private String doubanDefaultIcon = "movie_default_large.png";
	
	@Override
	public void process(Map<String, Object> fields) throws Exception {
		final List<ConcludeVO> concludes = new Vector<ConcludeVO>();
		final CountDownLatch downLatch = new CountDownLatch(fields.size());
		log.info("本页实际获取电影数量："+fields.size());
		for (Map.Entry<String, Object> entry : fields.entrySet()) {
			Object o = entry.getValue();
			if(!(o instanceof GatherVO)){
				continue;
			}
			final GatherVO gather = (GatherVO) o;
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						ConcludeVO conclude = filterWithCache(gather);
						concludes.add(conclude);
					} catch (Exception e) {
						log.error(gather.getComeFromUrl(),e);
					} finally {
						downLatch.countDown();
					}
				}
			});
		}
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.error("downLatch.await();",e);
		}
		
		this.filterAndPutWithDB(concludes);
	}
	
	private ConcludeVO filterWithCache(GatherVO gather) throws MovieInfoFetchException{
		MovieInfoEntity fetchMovie = null;
		String pureName = gather.getPureName();
		List<String> precisions = gather.getPrecisions();
		List<LinkInfoVO> links = gather.getLinkInfos();
		String comeFromUrl = gather.getComeFromUrl();
		List<String> printScreens = gather.getPrintScreens();
		String doubanUrl = gather.getDoubaDetailUril();
		
		if(StringUtils.isBlank(doubanUrl)){
			doubanUrl = getDoubanUrl(pureName, precisions);
		}
		
		fetchMovie = fetchMovieFromDouban(doubanUrl);
		
		//影片信息过滤
		String fetchPureName = fetchMovie.getPureName();
		String fetchReleaseTimeStr = fetchMovie.getReleaseTimeStr();
		String repeated = cacheDistinct.get(fetchPureName+fetchReleaseTimeStr);
		if(repeated != null){
			throw new MovieInfoFetchException("warning!!! ["+fetchMovie.getPureName()+"] has appeared in this web. between "+ repeated +" and " + comeFromUrl);
		}
		cacheDistinct.put(fetchPureName+fetchReleaseTimeStr, comeFromUrl);
		
		List<ResourceInfoEntity> resources = getResources(links, fetchMovie, printScreens, comeFromUrl);
		if(fetchMovie.getCategory() == MovieCategoryEnum.tv.getCode()){
			resources = filterEpisodeResources(fetchMovie.getCategory(), resources);
		}
		if(resources == null || resources.size() == 0){
			throw new MovieInfoFetchException("["+fetchMovie.getPureName()+"] is new, but there is not resource, skip!");
		}
		List<LabelMappingEntity> labels = getLabels(fetchMovie.getLabels(), fetchMovie.getMovieId(), pureName, fetchMovie.getReleaseTime());
		List<LocationMappingEntity> locations = getLocations(fetchMovie.getLocations(), fetchMovie.getMovieId(), pureName, fetchMovie.getReleaseTime());
		return new ConcludeVO(fetchMovie, resources, labels, locations);
	}
	
	
	private void filterAndPutWithDB(List<ConcludeVO> concludes) throws ParseException{
		
		List<MovieInfoEntity> adMovies = new ArrayList<MovieInfoEntity>();
		List<MovieInfoEntity> mdMovies = new ArrayList<MovieInfoEntity>();
		List<ResourceInfoEntity> adResources = new ArrayList<ResourceInfoEntity>();
		List<ResourceInfoEntity> mdResources = new ArrayList<ResourceInfoEntity>();
		List<LabelMappingEntity> adLabels = new ArrayList<LabelMappingEntity>();
		List<LocationMappingEntity> adLocations = new ArrayList<LocationMappingEntity>();
		
		
		for(ConcludeVO conclude:concludes){
 			MovieInfoEntity fetchMovie = conclude.getFetchMovie();
			List<ResourceInfoEntity> resources = conclude.getResources();
			List<LabelMappingEntity> labels = conclude.getLabels();
			List<LocationMappingEntity> locations = conclude.getLocations();
			
			String fetchReleaseTimeStr = fetchMovie.getReleaseTimeStr();
			String yearStr = fetchReleaseTimeStr.substring(0,4);
			MovieResourceEntity localMovieOptimalResource = movieInfoBizService.getAroundByPureNameAndLimitReleaseTime(fetchMovie.getPureName(), yearStr);
			ResourceInfoEntity fetchOptimalResource = getOptimalResource(fetchMovie.getCategory(), resources);
			
			if(localMovieOptimalResource == null){
				setMovieFile(fetchMovie);
				setResourcesFile(resources);
				fetchMovie.setOptimalResourceId(fetchOptimalResource.getResourceId());
				fetchMovie.setResourceWriteTime(fetchOptimalResource.getCreateTime());
				adMovies.add(fetchMovie);
				adResources.addAll(resources);
				adLabels.addAll(labels);
				adLocations.addAll(locations);
				continue;
			}
			MovieInfoEntity localMovie = localMovieOptimalResource.getMovie();
			ResourceInfoEntity localOptimalResource = localMovieOptimalResource.getResource();
			
			MovieInfoEntity changeOption = getMovieChangeOption(fetchMovie, localMovie);
			Date now = new Date();
			boolean movieMd = false;
			if(changeOption != null){
				movieMd = true;
			}else {
				changeOption = new MovieInfoEntity();
			}
 			changeOption.setMovieId(localMovie.getMovieId());
			changeOption.setUpdateTime(now);
			for(ResourceInfoEntity fetchResource : resources){
				try {
					int action = resourceCompare(fetchMovie.getCategory(), fetchResource, localOptimalResource);
 					if(action == CommonConstants.insert){
						setResourceFile(fetchResource);
						if(StringUtils.isBlank(changeOption.getOptimalResourceId())){
							changeOption.setOptimalResourceId(fetchOptimalResource.getResourceId());
							changeOption.setResourceWriteTime(fetchOptimalResource.getCreateTime());
						}
						fetchResource.setMovieId(localMovie.getMovieId());
						adResources.add(fetchResource);
						movieMd = true;
					}else if(action == CommonConstants.update){
						deleteResourceFile(localOptimalResource);
						setResourceFile(fetchResource);
						fetchResource.setUpdateTime(now);
						fetchResource.setMovieId(localMovie.getMovieId());
						fetchResource.setResourceId(localMovie.getOptimalResourceId());
						mdResources.add(fetchResource);
					}
				} catch (Exception e) {
					log.error(fetchResource.getComeFromUrl(),e);
				}
				
			}
			
			if(movieMd){
				log.info("[{}] exist better info, so update movieInfo! ", fetchMovie.getPureName());
				mdMovies.add(changeOption);
			}else {
				log.info("[{}] have no change, so skip! ", fetchMovie.getPureName());
			}
		}
		
		movieInfoBizService.batchAdd(adMovies, mdMovies, adResources, mdResources, adLabels, adLocations);
		log.info("新增movieInfo条数："+adMovies.size());
		log.info("修改movieInfo条数："+mdMovies.size());
		log.info("labels条数："+adLabels.size());
		log.info("locations条数："+adLocations.size());
		log.info("新增resourceInfo条数："+adResources.size());
		log.info("修改resourceInfo条数："+mdResources.size());
		log.info("bachAdd completion");
	}
	
	private void deleteResourceFile(ResourceInfoEntity resource){
		String photos = resource.getPhotos();
		
		if(StringUtils.isNotBlank(photos)){
			List<String> photosList = JSON.parseArray(photos, String.class);
			for(String photoStr:photosList){
				String busPhotoStr = photoDir+"/"+photoStr;
				File photo = new File(busPhotoStr);
				if(photo.exists()){
					photo.delete();
				}
			}
		}
		
		String downloadLink = resource.getDownloadLink();
		if(StringUtil.isLocalLink(downloadLink)){
			String busLinkStr = torrentDir+"/"+downloadLink;
			File torrent = new File(busLinkStr);
			if(torrent.exists()){
				torrent.delete();
			}
		}
	}
	
	private ResourceInfoEntity getOptimalResource(int category, List<ResourceInfoEntity> resources){
		ResourceInfoEntity optimalResource = null;
		for(ResourceInfoEntity resource:resources){
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
	
	private int resourceCompare(int category, ResourceInfoEntity fetchResource, ResourceInfoEntity optimalResource){
		
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
				return CommonConstants.insert;
			}else if(fetchEpisodeEnd.intValue() == optimalEpisodeEnd.intValue()){
				if(StringUtils.isNotBlank(fetchSubtitle) && StringUtils.isBlank(optimalSubtitle)){
					return CommonConstants.insert;
				}else if((StringUtils.isNotBlank(fetchSubtitle) && StringUtils.isNotBlank(optimalSubtitle)) ||
						(StringUtils.isBlank(fetchSubtitle) && StringUtils.isBlank(optimalSubtitle))){
					if(fetchDefinition.intValue() > optimalDefinition.intValue()){
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

	private MovieInfoEntity getMovieChangeOption(MovieInfoEntity fetchMovie, MovieInfoEntity localMovie){
		MovieInfoEntity changeOption = localMovie.changeOption(fetchMovie);
		return changeOption;
	}
	
	private void setResourcesFile(final List<ResourceInfoEntity> fetchResources){
		final CountDownLatch downLatch = new CountDownLatch(fetchResources.size());
		for(final ResourceInfoEntity resource:fetchResources){
			threadPool.execute(new Runnable() {

				@Override
				public void run() {
					try {
						setResourceFile(resource);
					} catch (Exception e) {
						log.error(resource.getComeFromUrl(),e);
					} finally {
						downLatch.countDown();
					}
				}
			});
		}
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.error("downLatch.await();",e);
		}
	}
	
	private void setResourceFile(final ResourceInfoEntity fetchResource) throws AnalystException{
		analyzeDownloadLink(fetchResource);
		List<String> busPhotosList = fetchResource.getBusPhotosList();
		if(busPhotosList == null){
			return;
		}
		final List<String> photosList = new Vector<String>();
		final CountDownLatch downLatch = new CountDownLatch(busPhotosList.size());
		for(final String photoStr:busPhotosList){
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						String reloaded = photoUrlMapping.get(photoStr);
						if(reloaded == null){
							String suffix = photoStr.substring(photoStr.lastIndexOf(".")+1);
							String fileName = StringUtil.getId(CommonConstants.pic_s)+"."+suffix;
							HttpUtils.downloadPicResize(photoStr, photoDir, fileName, CommonConstants.photo_width, CommonConstants.photo_height);
							photoUrlMapping.put(photoStr, fileName);
							photosList.add(fileName);
						}else {
							photosList.add(reloaded);
						}
					} catch (Exception e) {
						log.error(fetchResource.getComeFromUrl(),e);
					} finally {
						downLatch.countDown();
					}
				}
			});
			
		}
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.error("downLatch.await();",e);
		}
		if(photosList.size() > 0){
			String photos = JSON.toJSONString(photosList);
			fetchResource.setPhotos(photos);
		}
	}
	
	
	private void setMovieFile(final MovieInfoEntity fetchMovie){
		List<MovieFileUrl> urls = new ArrayList<MovieFileUrl>();
		String iconUrl = fetchMovie.getIconUrl();
		String iconName = "";
		String posterPageUrl = fetchMovie.getPosterPageUrl();
		if(StringUtils.isNotBlank(iconUrl)){
			iconName = iconUrl.substring(iconUrl.lastIndexOf("/")+1);
			if(!iconName.equals(doubanDefaultIcon)){
				String suffix = iconName.substring(iconName.lastIndexOf(".")+1);
				String fileName = StringUtil.getId(CommonConstants.pic_s)+"."+suffix;
				MovieFileUrl iconFileUrl = new MovieFileUrl(iconUrl, fileName, CommonConstants.icon_v);
				urls.add(iconFileUrl);
			}
		}
		
		if(StringUtils.isNotBlank(posterPageUrl)){
			addPosterFile(posterPageUrl, urls, iconName);
		}
		final Vector<String> posterNames = new Vector<String>();
		final CountDownLatch downLatch = new CountDownLatch(urls.size());
		for(final MovieFileUrl fileUrl:urls){
			threadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						if(fileUrl.getFlag() == CommonConstants.icon_v){
							HttpUtils.downloadPicResize(fileUrl.getFetchUrl(), iconDir, fileUrl.getFileName(), CommonConstants.icon_width, CommonConstants.icon_height);
							fetchMovie.setIcon(fileUrl.getFileName());
						}else {
							HttpUtils.downloadPicResize(fileUrl.getFetchUrl(), posterDir, fileUrl.getFileName(), CommonConstants.photo_width, CommonConstants.photo_height);
							posterNames.add(fileUrl.getFileName());
						}
						
					} catch (Exception e) {
						log.error("download poster wrong, url: {}", fileUrl.getFetchUrl(), e);
					}finally {
						downLatch.countDown();
					}
				}
			});
		}
		
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			log.error("downLatch.await();downloadPoster",e);
		}
		if(posterNames.size() > 0){
			String poster = JSON.toJSONString(posterNames);
			fetchMovie.setPoster(poster);
		}
		
	}
	
	private void addPosterFile(final String posterPageUrl, final List<MovieFileUrl> urls, final String doubanIconName){
		if(StringUtils.isNotBlank(posterPageUrl)){
			try {
				HttpUtils.httpGet(posterPageUrl, new HttpSendCallbackPure() {
					
					@Override
					public void onResponse(Response response) throws Exception {
						if(!response.isSuccessful()){
							throw new MovieInfoFetchException("Get movie poster failed from douban, code:"+response.code()+", url:"+posterPageUrl);
						}
						Document doc = Jsoup.parse(new String(response.body().bytes(), "utf-8"));
						Elements lis = doc.select("#content > div > div.article > ul > li[data-id]");
						int count = 1;
						for(int i=lis.size()-1; i >= 0; i--){
							Element li = lis.get(i);
							Element img = li.select(".cover > a > img").first();
							if(img == null){
								continue;
							}
							//此处打开的是预览图，预览图较小，需要高清图，但高清图在下一层链接里，因知道高清图和预览图只有一个字段的区别，顾直接替换
							String imgUrl = img.attr("src");
							if(StringUtils.isBlank(imgUrl)){
								continue;
							}
							imgUrl = imgUrl.replaceAll("thumb", "photo");
							String posterName = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
							if(posterName.equals(doubanIconName)){
								continue;
							}
							String suffix = posterName.substring(posterName.lastIndexOf(".")+1);
							String fileName = StringUtil.getId(CommonConstants.pic_s)+"."+suffix;
							MovieFileUrl posterFile = new MovieFileUrl(imgUrl, fileName, CommonConstants.poster_v);
							
							urls.add(posterFile);
							if(count >= 4){
								break;
							}
							count++;
						}
					}
				});
			} catch (Exception e) {
				log.error("Request posterPageUrl failed. url: {}", posterPageUrl, e);
			}
			
		}
	}
	
	private void analyzeDownloadLink(ResourceInfoEntity fetchResourceInfo) throws AnalystException{
		String link = fetchResourceInfo.getDownloadLink();
		if(StringUtils.isBlank(link)){
			throw new AnalystException("The download link of \"["+fetchResourceInfo.getPureName()+"]\" is null");
		}
		
		if(Pattern.compile(RegexConstant.baiduNet).matcher(link).find()){
			return;
		}
		
		if(Pattern.compile(RegexConstant.torrent).matcher(link).find()){
			try {
				StringBuffer fileName = new StringBuffer(fetchResourceInfo.getPureName().replaceAll(" ", "."));
				if(fetchResourceInfo.getEpisodeStart() != null){
					fileName.append(".第").append(fetchResourceInfo.getEpisodeStart())
							.append("-").append(fetchResourceInfo.getEpisodeEnd()).append("集");
				}else if(fetchResourceInfo.getEpisodeEnd() != null){
					fileName.append(".第").append(fetchResourceInfo.getEpisodeEnd()).append("集");
				}
				if(StringUtils.isNotBlank(fetchResourceInfo.getQuality())){
					fileName.append(".").append(fetchResourceInfo.getQuality());
				}
				if(StringUtils.isNotBlank(fetchResourceInfo.getResolution())){
					fileName.append(".").append(fetchResourceInfo.getResolution());
				}
				String subtitleNotice = "无字幕";
				if(StringUtils.isNotBlank(fetchResourceInfo.getSubtitle())){
					subtitleNotice = fetchResourceInfo.getSubtitle();
				}
				fileName.append(".").append(subtitleNotice).append(CommonConstants.local_sign);
				
				//去除当前页面文件重名的可能性
				StringBuffer test = new StringBuffer(fileName);
				int count = 1;
				do{
					Integer in = fileNameDistinct.get(test.toString());
					if(in == null){
						fileName = test;
						break;
					}
					test = new StringBuffer(fileName);
					test.append("(").append(count).append(")");
					count++;
				} while(true);
				
				String fileNameStr = fileName.toString();
				fileNameDistinct.put(fileNameStr, 1);
				String reloadName = HttpUtils.dowloadFile(link, torrentDir, fileNameStr, "torrent");
				fetchResourceInfo.setDownloadLink(reloadName);
			} catch (Exception e) {
				throw new AnalystException(e);
			}
		}
		
		
		if(StringUtils.isBlank(fetchResourceInfo.getSize()) || StringUtils.isBlank(fetchResourceInfo.getFormat())){
			final String linkCopy = fetchResourceInfo.getDownloadLink();
			final String thunderDecodingCopy = fetchResourceInfo.getThunderDecoding();
			
			try {
				LinkAnalyzeResultVO linkAnalyzeResult = LinkAnalyst.analysis(linkCopy, thunderDecodingCopy);
				if(linkAnalyzeResult == null){
					return;
				}
				if(StringUtils.isBlank(fetchResourceInfo.getSize())){
					fetchResourceInfo.setSize(linkAnalyzeResult.getMovieSize());
				}
				if(StringUtils.isBlank(fetchResourceInfo.getFormat())) {
					fetchResourceInfo.setFormat(linkAnalyzeResult.getMovieFormat());
				}
			} catch (Exception e) {
				Throwable cause = e.getCause();
				if(cause instanceof AnalystException){
					throw (AnalystException) cause;
				}
				AnalystException ae = new AnalystException(e.getMessage()+" downloadLink: "+fetchResourceInfo.getDownloadLink()+" episod: "+fetchResourceInfo.getEpisodeEnd());
				if(cause != null){
					ae.setStackTrace(cause.getStackTrace());
				}else {
					ae.setStackTrace(e.getStackTrace());
				}
				throw ae;
			}
		}
	}
	
	
	private List<LocationMappingEntity> getLocations(String locationsStr, String movieId, String pureName, Date releaseTime){
		if(StringUtils.isBlank(locationsStr)){
			locationsStr = "中国大陆";
		}
		String[] locationsSplit = locationsStr.split(RegexConstant.slashSep);
		Date now = new Date();
		List<LocationMappingEntity> locations = new ArrayList<LocationMappingEntity>();
		for(int i=0; i<locationsSplit.length; i++){
			String locationStr = locationsSplit[i];
			//地区只要中文，如果实在没有也没有办法！
			Matcher m = Pattern.compile(RegexConstant.chinese).matcher(locationStr);
			if(!m.find()){
				continue;
			}
			locationsStr = m.group();
			LocationMappingEntity location = new LocationMappingEntity();
			location.setLocation(locationsStr);
			String mappingId = StringUtil.getId(CommonConstants.location_s);
			location.setLocationId(mappingId);
			location.setMovieId(movieId);
			location.setPureName(pureName);
			location.setReleaseTime(releaseTime);
			location.setCreateTime(now);
			locations.add(location);
		}
		return locations;
	}
	
	
	private List<LabelMappingEntity> getLabels(String labelsStr, String movieId, String pureName, Date releaseTime){
		if(StringUtils.isBlank(labelsStr)){
			labelsStr = "剧情";
		}
		String[] labelsSplit = labelsStr.split(RegexConstant.slashSep);
		Date now = new Date();
		List<LabelMappingEntity> labels = new ArrayList<LabelMappingEntity>();
		for(int i=0; i<labelsSplit.length; i++){
			String labelStr = labelsSplit[i];
			LabelMappingEntity label = new LabelMappingEntity();
			label.setLabel(labelStr);
			String mappingId = StringUtil.getId(CommonConstants.label_s);
			label.setLabelId(mappingId);
			label.setMovieId(movieId);
			label.setPureName(pureName);
			label.setReleaseTime(releaseTime);
			label.setCreateTime(now);
			labels.add(label);
		}
		return labels;
	}
	
	private List<ResourceInfoEntity> getResources(List<LinkInfoVO> links, MovieInfoEntity fetchMovie, List<String> printScreens, String comeFromUrl){
		List<ResourceInfoEntity> resources = new ArrayList<ResourceInfoEntity>();
		String anotherName = fetchMovie.getAnotherName();
		for(LinkInfoVO link:links){
			if(!Pattern.compile(RegexConstant.resource_protocol).matcher(link.getDownloadLink()).find()){
				log.info("The link of \""+link.getDownloadLink()+"\", that doesn't matching protocol, Supported: ed2k,thunder,magnet,torrent or baiduNet");
				continue;
			}
			link.setName(stripEngName(link.getName(), anotherName));
			ResourceInfoEntity fetchResource = buildResourceInfoFromLinkName(fetchMovie, link, comeFromUrl);
			fetchResource.setBusPhotosList(printScreens);
			resources.add(fetchResource);
		}
		return resources;
	}
	
	private List<ResourceInfoEntity> filterEpisodeResources(int category, List<ResourceInfoEntity> resources){
		
		Map<String, ResourceInfoEntity> filterEpisodeMap = new HashMap<String, ResourceInfoEntity>();
		
		for(ResourceInfoEntity fetchResource:resources){
			String filterKey = fetchResource.getDownloadLink();
			if(category == MovieCategoryEnum.movie.getCode()){
				ResourceInfoEntity filterResource = filterEpisodeMap.get(filterKey);
				if(filterResource != null){
					continue;
				}
			}else {
				if(fetchResource.getEpisodeEnd() == null){
	    			log.info("url:{}, 电影在豆瓣中被标记为电视剧，但是资源[{}]却未获得第几集，跳过该资源！",fetchResource.getComeFromUrl(), fetchResource.getPureName());
					continue;
	    		}
				filterKey = fetchResource.getEpisodeEnd()+"";
				if(fetchResource.getEpisodeStart() != null){
					filterKey = fetchResource.getEpisodeStart() + "-" + fetchResource.getEpisodeEnd();
				}
				ResourceInfoEntity filterResource = filterEpisodeMap.get(filterKey);
				if(filterResource != null){
					int action = resourceCompare(category, fetchResource, filterResource);
					if(action == CommonConstants.abandon){
						continue;
					}
				}
				filterEpisodeMap.put(filterKey, fetchResource);
			}
		}
		return new ArrayList<ResourceInfoEntity>(filterEpisodeMap.values());
	}
	
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
     * 根据下载链接的名字，拼装ResourceInfoEntity
     * 涵盖字段：
     * resourceId,movieId,season,speed,definition,quality,resolution,subtitle,format,size,episode,createTime
     * 缺乏：
     * listPhotos,downloadLink,comeFromUrl
     * 
     * @param downloadLinkName
     * @return
     */
    private ResourceInfoEntity buildResourceInfoFromLinkName(MovieInfoEntity fetchMovie, LinkInfoVO link, String comeFromUrl){
    	String pureName = fetchMovie.getPureName();
    	Date releaseTime = fetchMovie.getReleaseTime();
    	Integer season = fetchMovie.getPresentSeason();
    	Integer category = fetchMovie.getCategory();
    	Integer totalEpisode = fetchMovie.getTotalEpisode();
    	String movieId = fetchMovie.getMovieId();
    	
    	String downloadLinkName = link.getName();
    	String downloadLink = link.getDownloadLink();
    	String thunderDecoding = link.getLinkDecoding();
    	
    	
    	ResourceInfoEntity newResource = new ResourceInfoEntity();
    	newResource.setMovieId(movieId);
    	newResource.setPureName(pureName);
    	newResource.setReleaseTime(releaseTime);
    	newResource.setComeFromUrl(comeFromUrl);
    	newResource.setDownloadLink(downloadLink);
    	
    	String resourceId = StringUtil.getId(CommonConstants.resource_s);
    	newResource.setResourceId(resourceId);
    	newResource.setSpeed(5);
    	newResource.setResourceStatus(MovieStatusEnum.available.getCode());
    	
    	//该网址符合获取“下载链接网址”的正则才进行操作
    	Matcher m2 = Pattern.compile(RegexConstant.quality).matcher(downloadLinkName);
    	String finalMatch = null;
    	while(m2.find()){
    		finalMatch = m2.group();
    	}
    	if(StringUtils.isNotBlank(finalMatch)){
    		MovieQualityEnum movieQualityEnum = MovieQualityEnum.getEnumByName(StringUtil.replaceBlank2(finalMatch).toUpperCase());
    		newResource.setQuality(movieQualityEnum.getQuality());
    	}
		
		//从下载链接名字中获取片源分辨率
		m2 = Pattern.compile(RegexConstant.resolution).matcher(downloadLinkName);
		if(m2.find()){
			newResource.setResolution(m2.group());
		}
		
		Integer definitionScore = Utils.translateDefinitionIntoScore(newResource.getQuality(), newResource.getResolution());
		newResource.setDefinition(definitionScore);
		
		//从下载链接名字中获取字幕信息
		m2 = Pattern.compile(RegexConstant.subtitle).matcher(downloadLinkName);
		if(m2.find()){
			newResource.setSubtitle(m2.group());
		}else {
			m2 = Pattern.compile(RegexConstant.subtitle_m_encn).matcher(downloadLinkName);
			if(m2.find()){
				newResource.setSubtitle("中英双字");
			}else {
				m2 = Pattern.compile(RegexConstant.subtitle_m_cn).matcher(downloadLinkName);
				if(m2.find()){
					newResource.setSubtitle("中文字幕");
				}
			}
		}
		
		//若此处未获取到，则在处理链接的时候再次获取
		m2 = Pattern.compile(RegexConstant.format).matcher(downloadLinkName);
		if(m2.find()){
			newResource.setFormat(m2.group());
		}
		
		//若此处未获取到，则在处理链接的时候再次获取
		m2 = Pattern.compile(RegexConstant.size).matcher(downloadLinkName);
		if(m2.find()){
			newResource.setSize(m2.group());
		}
		
		//避免标题处没有写明第几季的情况，在资源name处再次获取
		if(category == MovieCategoryEnum.tv.getCode() && season == null){
			for(int i=0; i<RegexConstant.list_season.size(); i++){
				m2 = RegexConstant.list_season.get(i).matcher(StringUtil.trimAll(downloadLinkName));
				if(m2.find()){
					season = Utils.chineseNumber2Int(m2.group(1));
					break;
				}
			}
		}
		newResource.setSeason(season);
		
		//如果是连续剧则获取最新集数
		if(category == MovieCategoryEnum.tv.getCode()){
			List<Pattern> list_episode = RegexConstant.list_episode2;
			if(fetchMovie.getTotalEpisode() != null && fetchMovie.getTotalEpisode().intValue() > 100){
				list_episode = RegexConstant.list_episode3;
			}
			
			for(int i=0; i<list_episode.size(); i++){
	    		m2 = list_episode.get(i).matcher(downloadLinkName);
	    		if(m2.find()){
	    			String m2Grop1 = m2.group(1);
	    			if(m2Grop1 == null){
	    				m2Grop1 = "";
	    			}
					Matcher m3 = Pattern.compile(RegexConstant.cn_number).matcher(m2Grop1);
					if(m3.find()){
						//将中文数字转换为阿拉伯数字
						newResource.setEpisodeEnd(Utils.chineseNumber2Int(m3.group()));
					}else if(m2.group().equals("全集")){
						newResource.setEpisodeStart(1);
						newResource.setEpisodeEnd(totalEpisode);
					}else if(m2Grop1.indexOf("-") != -1){
						//如过集数的样式为40-50
						String[] episodeArr = m2.group(1).split("-");
						Integer startEpisode = Integer.valueOf(episodeArr[0]);
						Integer endEpisode = Integer.valueOf(episodeArr[1]);
						if(endEpisode.intValue()>startEpisode.intValue()){
							newResource.setEpisodeStart(startEpisode);
						}
						newResource.setEpisodeEnd(endEpisode);
					}else {
						newResource.setEpisodeEnd(Integer.valueOf(m2Grop1));
					}
					break;
	    		}
	    	}
		}
    	
    	if(Pattern.compile(RegexConstant.thunder).matcher(downloadLink).find()){
    		newResource.setThunderDecoding(thunderDecoding);
    	}
		
		newResource.setCreateTime(new Date());
		return newResource;
    }
	
	/**
	 * 到豆瓣搜索页以pureName为关键字进行搜索后，再根据releaseTime进行筛选，
	 * 最后得出准确的pureName和releaseTimeStr以及doubanDetailUrl
	 * @param pureName
	 * @param releaseTimeStr
	 * @return
	 * @throws Exception 
	 * @throws MovieInfoFetchException 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	private String getDoubanUrl(final String pureName, final List<String> precisions) throws MovieInfoFetchException{
		HttpResult<String> result = HttpUtils.httpGet("https://api.douban.com/v2/movie/search?q="+Utils.encode(pureName, "UTF-8"), 
				
				new HttpSendCallback<String>() {

			@Override
			public String onResponse(Response response) throws Exception{
				if(!response.isSuccessful()){
					String responseStr = response.body().string();
					throw new MovieInfoFetchException("在豆瓣中搜索影片["+pureName+"]失败，返回响应码："+response.code()+", response: "+responseStr);
				}
				
				String contentJson = response.body().string();
				JSONObject content = JSON.parseObject(contentJson);
				
				JSONArray subjects = content.getJSONArray("subjects");
				int precision = -1;
		    	String doubanDetailUrl = "";
				for(int i=0; i<subjects.size(); i++){
					JSONObject subject = subjects.getJSONObject(i);
					String realPureName = subject.getString("title");
					String originalName = subject.getString("original_title");
					
					List<String> names = new ArrayList<String>();
					JSONArray directors = subject.getJSONArray("directors");
					for(int j=0; j<directors.size(); j++){
						JSONObject director = directors.getJSONObject(j);
						String name = director.getString("name");
						if(Pattern.compile("^"+RegexConstant.chinese).matcher(name).find()){
			    			name = name.split(" ")[0];
			    		}
						names.add(name);
					}
					
					JSONArray casts = subject.getJSONArray("casts");
					for(int j=0; j<casts.size(); j++){
						JSONObject cast = casts.getJSONObject(j);
						String name = cast.getString("name");
						if(Pattern.compile("^"+RegexConstant.chinese).matcher(name).find()){
			    			name = name.split(" ")[0];
			    		}
						names.add(name);
					}
					
					String TVPureNameTOBE = pureName+" 第一季"; //有些网站美剧第一季通常不会把“第一季”写上，所以增加这个搜寻条件
					
					if(precisions != null && precisions.size() > 0){
						if((realPureName.equals(pureName) || originalName.equals(pureName) || 
								realPureName.equals(TVPureNameTOBE) || originalName.equals(TVPureNameTOBE))
				    			&& Utils.containsOne(names, precisions)){
							doubanDetailUrl = subject.getString("alt");
							precision = 2;
				    		break;
				    	}else if(Utils.containsOne(names, precisions)){
				    		if(precision <= 0){
				    			precision = 1;
				    			doubanDetailUrl = subject.getString("alt");
				    		}
				    	}
					}
					if(realPureName.equals(pureName) || originalName.equals(pureName) || 
								realPureName.equals(TVPureNameTOBE) || originalName.equals(TVPureNameTOBE)){
						if(precision == -1){
							precision = 0;
							doubanDetailUrl = subject.getString("alt");
						}
					}
				}
			    if(precision == -1){
			    	throw new MovieInfoFetchException("It's doesn't dovetailed with the result of douban search for ["+pureName+"]");
			    }
				return doubanDetailUrl;
				
			}
			
		});
		try {
			return result.getValue();
		} catch (Exception e) {
			if(e instanceof MovieInfoFetchException){
				throw (MovieInfoFetchException) e;
			}
			throw new MovieInfoFetchException(e);
		}
	}
	
	
	/**
     * 爬取豆瓣对应电影的MovieInfo，并拼装MovieInfoEntity和MovieLabelMappingEntity
     * 拼装属性涵盖：
     * movieId,pureName,doubanId,director,writers,cast,location,language,releaseTime,
     * duration,imdbId,doubanScore,attentionRate,summary,icon
     * 缺乏：无
     * @param url
     * @param fields
     * @return
     * @throws MovieInfoFetchException 
     */
	private MovieInfoEntity fetchMovieFromDouban(final String url) throws MovieInfoFetchException{
		HttpResult<MovieInfoEntity> result = HttpUtils.httpGet(url, new HttpSendCallback<MovieInfoEntity>() {

			@Override
			public MovieInfoEntity onResponse(Response response)
					throws Exception {
				if(!response.isSuccessful()){
					throw new MovieInfoFetchException("Get movie detail failed from douban, code:"+response.code()+", url:"+url);
				}
				String finalUrl = response.request().url().toString();
				if(!finalUrl.equals(url)){
					throw new MovieInfoFetchException("Requested url: "+url+" that was redirected to "+finalUrl);
				}
				MovieInfoEntity newMovie = new MovieInfoEntity();
				String movieId = StringUtil.getId(CommonConstants.movie_s);
				Integer category = MovieCategoryEnum.movie.getCode();
				newMovie.setMovieId(movieId);
				Document doc = Jsoup.parse(new String(response.body().bytes(), "utf-8"));
				String pureName = doc.select("head title").first().text().replace("(豆瓣)", "").trim();
				newMovie.setPureName(pureName);
				Element seasonElement = doc.select("#season option[selected=selected]").first();
				if(seasonElement != null){
					Integer season = Integer.valueOf(seasonElement.text());
					newMovie.setPresentSeason(season);
				}
				String releaseTimeStr = "";
				
				String anotherName = doc.select("#content > h1 > span[property]").first().text().replace(pureName, "").trim();
				
				String[] urlSplit = url.split("/");
				String doubanId = urlSplit[urlSplit.length-1];
				if(StringUtils.isBlank(doubanId)){
					doubanId = urlSplit[urlSplit.length-2];
				}
				newMovie.setDoubanId(doubanId);
				String posterPageUrl = doc.select("#mainpic > a").first().attr("href");
				newMovie.setPosterPageUrl(posterPageUrl);
				String iconUrl = doc.select("#mainpic > a > img").first().attr("src");
				newMovie.setIconUrl(iconUrl);
				Element movieInfoElement = doc.select("#info").first();
				List<TextNode> textNodes = movieInfoElement.textNodes();
				
				for(TextNode textNode : textNodes) {
					String text = textNode.text();
					int separatorIdx = text.indexOf(":");
					String title = text.substring(0, separatorIdx).trim();
					String content = text.substring(separatorIdx+1).trim();
					content = content.replaceAll("(?<=[^\\s])/(?=[^\\s])", " / ");
					if("导演".equals(title)){
						newMovie.setDirector(content);
					} else if("编剧".equals(title)){
						newMovie.setWriters(content);
					}else if("主演".equals(title)){
						newMovie.setCast(content);
					}else if("类型".equals(title)){
						newMovie.setLabels(content);
					}else if("季数".equals(title)){ //根据是否有集数标签设置是否为电视剧
						category = MovieCategoryEnum.tv.getCode();
					}else if("集数".equals(title)){
						try {
							newMovie.setTotalEpisode(Integer.valueOf(content));
						} catch (Exception e) {
							log.error("设置总集数出错，不影响总体进程！",e);
						}
						category = MovieCategoryEnum.tv.getCode();
					}else if("制片国家/地区".equals(title)){
						newMovie.setLocations(content);
					}else if("语言".equals(title)){
						newMovie.setLanguage(content);
					}else if("上映日期".equals(title) || "首播".equals(title)) {
						releaseTimeStr = Utils.getTimeStr(content);
					}else if("片长".equals(title) || "单集片长".equals(title)){
						newMovie.setDuration(content);
					}else if("又名".equals(title)){
						anotherName += (" / "+content);
						anotherName = Pattern.compile(RegexConstant.slash).matcher(anotherName.trim()).replaceAll("").trim();
					}else if("IMDb链接".equals(title)){
						newMovie.setImdbId(content);
					}
				
				}
				
				newMovie.setCategory(category);
				
				if(StringUtils.isNotBlank(anotherName)){
					newMovie.setAnotherName(anotherName);
				}
				String year = doc.select("#content > h1 > span.year").first().text();
				year = Utils.getTimeStr(year);
				if(StringUtils.isBlank(releaseTimeStr)){
					releaseTimeStr = year;
				}
				newMovie.setYear(year);
				newMovie.setReleaseTimeStr(releaseTimeStr);
				newMovie.setReleaseTime(DateUtil.str2Date(releaseTimeStr));
				newMovie.setReleaseTimeFormat(CommonConstants.getTimeFormat(releaseTimeStr));
				
				String doubanScoreStr = doc.select("#interest_sectl > div.rating_wrap.clearbox > div.rating_self.clearfix > strong").first().text();
				Double doubanScore = null;
				if(StringUtils.isNotBlank(doubanScoreStr)){
					doubanScore = Double.valueOf(doubanScoreStr);
				}
				newMovie.setDoubanScore(doubanScore);
				Elements attentionRateElements = doc.select("#interest_sectl > div.rating_wrap.clearbox > div.rating_self.clearfix > div > div.rating_sum > a > span");
				String attentionRateStr = null;
				if(attentionRateElements.size()>0){
					attentionRateStr = doc.select("#interest_sectl > div.rating_wrap.clearbox > div.rating_self.clearfix > div > div.rating_sum > a > span").first().text();
				}
				Integer attentionRate = null;
				if(StringUtils.isNotBlank(attentionRateStr)){
					attentionRate = Integer.valueOf(attentionRateStr);
				}
				newMovie.setAttentionRate(attentionRate);
				Elements summaryElements = doc.select("span.all.hidden");
				if(summaryElements.size() == 0){
					summaryElements = doc.select("#link-report > span");
				}
				if(summaryElements.size() != 0){
					String summary = summaryElements.first().html();
					summary = Pattern.compile("(^　+|<a.*?</a>)").matcher(summary).replaceAll("");
					newMovie.setSummary(summary);
				}
				Date date = new Date();
				newMovie.setCreateTime(date);
				newMovie.setMovieStatus(MovieStatusEnum.available.getCode());
				newMovie.setResourceWriteTime(date);
				newMovie.setCountClick(0);
				newMovie.setCountComment(0);
				newMovie.setCountDownload(0);
				return newMovie;
			}
		});
		try {
			return result.getValue();
		} catch (Exception e) {
			if(e instanceof MovieInfoFetchException){
				throw (MovieInfoFetchException) e;
			}
			throw new MovieInfoFetchException(e);
		}
	}

	@Override
	public void after() {
		fileNameDistinct.clear();
		photoUrlMapping.clear();
		cacheDistinct.clear();
	}
	
	

}
