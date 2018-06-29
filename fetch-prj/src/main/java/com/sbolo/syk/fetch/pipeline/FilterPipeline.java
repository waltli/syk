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
	
	
	@Override
	public void after() {
		fileNameDistinct.clear();
		photoUrlMapping.clear();
		cacheDistinct.clear();
	}
	
	

}
