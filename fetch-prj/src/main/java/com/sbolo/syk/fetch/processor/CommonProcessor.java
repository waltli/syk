package com.sbolo.syk.fetch.processor;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.fetch.po.LabelMappingEntity;
import com.sbolo.syk.fetch.po.LocationMappingEntity;
import com.sbolo.syk.fetch.po.MovieInfoEntity;
import com.sbolo.syk.fetch.po.ResourceInfoEntity;
import com.sbolo.syk.fetch.service.MovieInfoBizService;
import com.sbolo.syk.fetch.service.ResourceInfoBizService;
import com.sbolo.syk.fetch.vo.ConcludeVO;
import com.sbolo.syk.fetch.vo.GatherVO;
import com.sbolo.syk.fetch.vo.LinkInfoVO;
import com.sbolo.syk.fetch.vo.PureNameAndSeasonVO;

public class CommonProcessor {
	private static final Logger log = LoggerFactory.getLogger(CommonProcessor.class);
	private ConcurrentMap<String, String> cacheDistinct = new ConcurrentHashMap<String, String>();
	private ConcurrentMap<String, Integer> fileNameDistinct = new ConcurrentHashMap<String, Integer>(); 
	private ConcurrentMap<String, String> photoUrlMapping = new ConcurrentHashMap<String, String>(); 
	
	private String iconDir = ConfigUtil.getPropertyValue("iconDir");
	private String posterDir = ConfigUtil.getPropertyValue("posterDir");
	private String photoDir = ConfigUtil.getPropertyValue("photoDir");
	private String torrentDir = ConfigUtil.getPropertyValue("torrentDir");
	
	private String startUrl;
	
	public String getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}
	
	@Resource
	private ThreadPoolTaskExecutor threadPool;
	
	@Resource
	private ResourceInfoBizService resourceInfoBizService;
	
	@Resource
	private MovieInfoBizService movieInfoBizService;
	
    public PureNameAndSeasonVO getPureNameAndSeason(String pureName, String fullName) throws MovieInfoFetchException{
    	
    	if(pureName == null && fullName == null){
    		throw new MovieInfoFetchException("pureName and fullName all are null!");
    	}

    	if(fullName == null){
    		fullName = "";
    	}
    	
    	if(pureName == null){
    		Matcher m = Pattern.compile(RegexConstant.pure_name).matcher(fullName);
    		if(!m.find()){
    			throw new MovieInfoFetchException("Failure to fetch pureName from \""+fullName+"\", so jump out of the page!");
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
			throw new MovieInfoFetchException("The pureName is null after delete types. from \""+name+"\"");
		}
    	
    	String desc = fullName;
    	if(!fullName.contains(pureName)){
    		desc = pureName+fullName;
    	}
    	
		Integer season = null;
		String cnSeason = null;
		for(int i=0; i<RegexConstant.list_season.size(); i++){
			Matcher m2 = RegexConstant.list_season.get(i).matcher(StringUtil.trimAll(desc));
			if(m2.find()){
				//防止名称和季数中间没有任何间隔 例：越狱第五季
				int seasonIdx = pureName.indexOf(m2.group());
				if(seasonIdx != -1){
					pureName = pureName.substring(0, seasonIdx).trim();
				}
				season = Utils.chineseNumber2Int(m2.group(1));
				cnSeason = "第"+Utils.int2ChineseNumber(m2.group(1))+"季";
				pureName = pureName +" "+cnSeason;
				break;
			}
		}
		return new PureNameAndSeasonVO(pureName, season, cnSeason);
    }
	
    
    
    
    protected void destroy(){
    	cacheDistinct.clear();
    }
    
    protected List<String> getPrecisionsByInfo(List<TextNode> textNodes) {
		String directDesc = "";
		String castDesc = "";
		for(TextNode textNode : textNodes) {
			String text = textNode.text();
			if(Pattern.compile(RegexConstant.DYtitle).matcher(text).find() && StringUtils.isBlank(directDesc)){
				directDesc = text;
			}else if(Pattern.compile(RegexConstant.YYtitle+"|"+RegexConstant.ZYtitle).matcher(text).find() && StringUtils.isBlank(castDesc)){
				castDesc = text;
			}
			if(StringUtils.isNotBlank(directDesc) && StringUtils.isNotBlank(castDesc)){
				break;
			}
		}
		List<String> precisions = getPrecisions(directDesc, castDesc);
		return precisions;
	}
    
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
    			name = name.split(" ")[0].replace("・", "·");
    		}
    		precisions.add(name);
    		if(precisions.size() >= count){
				break;
			}
    	}
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
	
	protected MovieInfoEntity fetchMovieFromDouban(final String pureName, final List<String> precisions) throws MovieInfoFetchException {
		String doubanUrl = this.getDoubanUrl(pureName, precisions);
		MovieInfoEntity movieInfo = fetchMovieFromDouban(doubanUrl);
		return movieInfo;
	}
	
	protected ConcludeVO filterWithCache(String pureName, List<String> precisions, List<LinkInfoVO> links, List<String> printScreens, String comeFromUrl, String doubanUrl) throws MovieInfoFetchException{
		MovieInfoEntity fetchMovie = null;
		
		if(StringUtils.isBlank(doubanUrl)){
			doubanUrl = getDoubanUrl(pureName, precisions);
		}
		
		fetchMovie = fetchMovieFromDouban(doubanUrl);
		
		//影片信息过滤，避免出现一个网站两个相同影片，fetchPureName+fetchReleaseTimeStr做为key来过滤
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
		List<LabelMappingEntity> labels = fetchLabels(fetchMovie.getLabels(), fetchMovie.getMovieId(), pureName, fetchMovie.getReleaseTime());
		List<LocationMappingEntity> locations = fetchLocations(fetchMovie.getLocations(), fetchMovie.getMovieId(), pureName, fetchMovie.getReleaseTime());
		return new ConcludeVO(fetchMovie, resources, labels, locations);
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
	
	/**
	 * 与自身进行过滤
	 * @param category 类型 tv/movie
	 * @param fetchResources 获取到的所有resource
	 * @return
	 */
	private List<ResourceInfoEntity> filterResourceInSelf(int category, List<ResourceInfoEntity> fetchResources){
		//过滤完成后剩下的资源
		Map<String, ResourceInfoEntity> filterMap = new HashMap<String, ResourceInfoEntity>();
		
		for(ResourceInfoEntity fetchResource:fetchResources){
			String filterKey = fetchResource.getDownloadLink();
			if(category == MovieCategoryEnum.movie.getCode()){
				ResourceInfoEntity filterResource = filterMap.get(filterKey);
				if(filterResource != null){
					continue;
				}
			}else {
				if(fetchResource.getEpisodeEnd() == null){
	    			log.info("url:{}, 电影在豆瓣中被标记为电视剧，但是资源[{}]却未获得第几集，跳过该资源！",fetchResource.getComeFromUrl(), fetchResource.getPureName());
					continue;
	    		}
				//根据Map的key来过滤
				filterKey = fetchResource.getEpisodeEnd()+"";
				if(fetchResource.getEpisodeStart() != null){
					filterKey = fetchResource.getEpisodeStart() + "-" + fetchResource.getEpisodeEnd();
				}
				ResourceInfoEntity filterResource = filterMap.get(filterKey);
				if(filterResource != null){
					int action = resourceCompare(category, fetchResource, filterResource);
					if(action == CommonConstants.abandon){
						continue;
					}
				}
			}
			filterMap.put(filterKey, fetchResource);
		}
		return new ArrayList<ResourceInfoEntity>(filterMap.values());
	}
	
	/**
	 * 与数据库中的resource对比进行过滤
	 * @param category 类型 tv/movie
	 * @param fetchResources 获取到的所有resource
	 * @return
	 */
	private List<ResourceInfoEntity> filterResourceInDB(int category, String moviePrn, List<ResourceInfoEntity> fetchResources){
		//过滤完成后剩下的资源
		List<ResourceInfoEntity> filterList = new ArrayList<>();
		
		//获取到数据库中该影片质量最好的resource
		ResourceInfoEntity dbOptimalResource = new ResourceInfoEntity();
		
		for(ResourceInfoEntity fetchResource : fetchResources){
			int action = resourceCompare(category, fetchResource, dbOptimalResource);
			if(action == CommonConstants.abandon){
				continue;
			}
			
			if(action == CommonConstants.insert){
				fetchResource.setCreateTime(new Date());
				//设置movieId
			}else if(action == CommonConstants.update){
				fetchResource.setUpdateTime(new Date());
				//设置resourceId
			}
			//设置action在后续插入数据库时使用
			fetchResource.setAction(action);
			filterList.add(fetchResource);
		}
		
		return filterList;
	}
	
	/**
	 * 下载视频截图并设置
	 * 
	 * @param fetchResource 获取到的resource
	 * @throws AnalystException
	 */
	private void setPrintScreens(ResourceInfoEntity optimalResource) throws AnalystException{
		List<String> busPhotosList = optimalResource.getBusPhotosList();
		if(busPhotosList == null){
			return;
		}
		List<String> photosList = new ArrayList<>();
		for(String photoStr:busPhotosList){
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
				log.error(optimalResource.getComeFromUrl(),e);
			}
		}
		if(photosList.size() > 0){
			String photos = JSON.toJSONString(photosList);
			optimalResource.setPhotos(photos);
		}
	}
	
	/**
	 * 分析下载链接并填充信息，如果是种子文件则下载到服务器，再设置。
	 * 
	 * @param fetchResource
	 * @throws AnalystException
	 */
	private void analyzeDownloadLink(ResourceInfoEntity fetchResource) throws AnalystException{
		String link = fetchResource.getDownloadLink();
		if(StringUtils.isBlank(link)){
			throw new AnalystException("The download link of \"["+fetchResource.getPureName()+"]\" is null");
		}
		
		if(Pattern.compile(RegexConstant.baiduNet).matcher(link).find()){
			return;
		}
		
		if(Pattern.compile(RegexConstant.torrent).matcher(link).find()){
			try {
				StringBuffer fileName = new StringBuffer(fetchResource.getPureName().replaceAll(" ", "."));
				if(fetchResource.getEpisodeStart() != null){
					fileName.append(".第").append(fetchResource.getEpisodeStart())
							.append("-").append(fetchResource.getEpisodeEnd()).append("集");
				}else if(fetchResource.getEpisodeEnd() != null){
					fileName.append(".第").append(fetchResource.getEpisodeEnd()).append("集");
				}
				if(StringUtils.isNotBlank(fetchResource.getQuality())){
					fileName.append(".").append(fetchResource.getQuality());
				}
				if(StringUtils.isNotBlank(fetchResource.getResolution())){
					fileName.append(".").append(fetchResource.getResolution());
				}
				String subtitleNotice = "无字幕";
				if(StringUtils.isNotBlank(fetchResource.getSubtitle())){
					subtitleNotice = fetchResource.getSubtitle();
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
				fetchResource.setDownloadLink(reloadName);
			} catch (Exception e) {
				throw new AnalystException(e);
			}
		}
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
	
	private List<LabelMappingEntity> fetchLabels(String labelsStr, String movieId, String pureName, Date releaseTime){
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
	
	private List<LocationMappingEntity> fetchLocations(String locationsStr, String movieId, String pureName, Date releaseTime){
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
	 * 将新获得的resource与缓存中的最佳resource进行比较
	 * @param category 类别 tv/movie
	 * @param fetchResource 最新获取到resource
	 * @param optimalResource 最佳的resource
	 * @return
	 */
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
				//如果是新的一集
				return CommonConstants.insert;
			}else if(fetchEpisodeEnd.intValue() == optimalEpisodeEnd.intValue()){
				//如果集数相同
				if(StringUtils.isNotBlank(fetchSubtitle) && StringUtils.isBlank(optimalSubtitle)){
					//如果新的有子标题，而最佳的没有子标题
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
	
	
}
