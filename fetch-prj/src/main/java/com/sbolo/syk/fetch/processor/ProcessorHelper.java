package com.sbolo.syk.fetch.processor;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
import com.sbolo.syk.common.exception.AnalystException;
import com.sbolo.syk.common.exception.MovieInfoFetchException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.http.callback.HttpSendCallbackPure;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.UIDGenerator;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.vo.LinkAnalyzeResultVO;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.MovieLabelEntity;
import com.sbolo.syk.fetch.entity.MovieLocationEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.service.MovieInfoService;
import com.sbolo.syk.fetch.service.MovieLabelService;
import com.sbolo.syk.fetch.service.MovieLocationService;
import com.sbolo.syk.fetch.service.ResourceInfoService;
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
	private ConcurrentMap<String, String> cacheDistinct = new ConcurrentHashMap<String, String>();
	private ConcurrentMap<String, Integer> fileNameDistinct = new ConcurrentHashMap<String, Integer>(); 
	private ConcurrentMap<String, String> photoUrlMapping = new ConcurrentHashMap<String, String>(); 
	
	private String doubanDefaultIcon = "movie_default_large.png";
	
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
	private ResourceInfoService resourceInfoService;
	
	@Resource
	private MovieInfoService movieInfoService;
	
	@Resource
	private MovieLabelService movieLabelService;
	
	@Resource
	private MovieLocationService movieLocationService;
	
	
	protected ConcludeVO resolve(String pureName, List<String> precisions, List<LinkInfoVO> links, List<String> printScreens, String comeFromUrl, String doubanUrl) throws MovieInfoFetchException, AnalystException, ParseException {
		Date thisTime = new Date();
		
		//判断豆瓣URL是否为空，因为有些网站已经将豆瓣URL贴上了
		if(StringUtils.isBlank(doubanUrl)){
			doubanUrl = this.getDoubanUrl(pureName, precisions);
		}
		
		//从豆瓣中获取到准确的movie信息
		MovieInfoVO fetchMovie = this.fetchMovieFromDouban(doubanUrl, thisTime);
		
		//判断在这次扫描中是否有重复出现过
		boolean isRepeated = this.isRepeatedMovieInCache(fetchMovie, comeFromUrl);
		if(isRepeated) {
			return null;
		}
		
		//与数据库中的该条movie对比，是否有，是否有不同，最终获取需要写入的movie
		MovieInfoVO finalMovie = this.filterMovieInDb(fetchMovie, thisTime);
		
		//根据扫描的信息构建resource对象
		List<ResourceInfoVO> fetchResources = this.getResources(links, fetchMovie, printScreens, comeFromUrl, thisTime);
		
		//在缓存中过滤一批resource
		List<ResourceInfoVO> filter1 = this.filterResourceInCache(fetchMovie.getCategory(), fetchResources);
		
		if(filter1 == null || filter1.size() == 0) {
			log.info("No canable resource after fileter in cache! url: {}", comeFromUrl);
			return null;
		}
		
		//如果影片的action等于insert，那就表示是新电影，库中肯定没有资源
		List<ResourceInfoVO> filter2 = null;
		ResourceInfoEntity dbOptimalResource = null;
		if(finalMovie.getAction() == CommonConstants.insert) {
			filter2 = this.setAndGetInsertResource(fetchResources, finalMovie.getPrn(), thisTime);
		}else {
			//获取到数据库中该影片质量最好的resource
			dbOptimalResource = resourceInfoService.getOptimalResource(finalMovie.getPrn());
			
			if(dbOptimalResource == null) {
				filter2 = this.setAndGetInsertResource(fetchResources, finalMovie.getPrn(), thisTime);
			}else {
				//在数据库中过滤一批reousrce
				filter2 = this.filterResourceInDB(fetchMovie.getCategory(), finalMovie.getPrn(), filter1, dbOptimalResource, thisTime);
			}
		}
		
		if(filter2 == null || filter2.size() == 0) {
			log.info("No canable resource after filter in db! url: {}", comeFromUrl);
			return null;
		}
		
		//既然走到这里，证明缓存中的resource比数据库存在更佳的resource
		ResourceInfoVO fetchOptimalResource = this.getOptimalResource(fetchMovie.getCategory(), filter2);
		
		//为finalMovie设置optimalResource的相关信息
		this.setOptimalResource(finalMovie, fetchOptimalResource);
		//集中处理电影和资源的相关文件
		this.dealMovieAndResourceFiles(fetchMovie, filter2, fetchOptimalResource, dbOptimalResource);
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
			if(Pattern.compile(RegexConstant.baiduNet).matcher(fetchResource.getDownloadLink()).find()) {
				fetchResource.setAction(CommonConstants.abandon);
				continue;
			}
			fetchResource.setAction(CommonConstants.insert);
			fetchResource.setCreateTime(thisTime);
			fetchResource.setMoviePrn(moviePrn);
		}
		return fetchResources;
	}
	
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
    	fileNameDistinct.clear();
		photoUrlMapping.clear();
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
     * 爬取豆瓣对应电影的MovieInfo，并拼装MovieInfoVO和MovieLabelMappingEntity
     * 拼装属性涵盖：
     * moviePrn,pureName,doubanId,director,writers,cast,location,language,releaseTime,
     * duration,imdbId,doubanScore,attentionRate,summary,icon
     * 缺乏：无
     * @param url
     * @param fields
     * @return
     * @throws MovieInfoFetchException 
     */
	private MovieInfoVO fetchMovieFromDouban(String url, Date thisTime) throws MovieInfoFetchException{
		HttpResult<MovieInfoVO> result = HttpUtils.httpGet(url, new HttpSendCallback<MovieInfoVO>() {

			@Override
			public MovieInfoVO onResponse(Response response)
					throws Exception {
				if(!response.isSuccessful()){
					throw new MovieInfoFetchException("Get movie detail failed from douban, code:"+response.code()+", url:"+url);
				}
				String finalUrl = response.request().url().toString();
				if(!finalUrl.equals(url)){
					throw new MovieInfoFetchException("Requested url: "+url+" that was redirected to "+finalUrl);
				}
				MovieInfoVO newMovie = new MovieInfoVO();
				String moviePrn = StringUtil.getId(CommonConstants.movie_s);
				Integer category = MovieCategoryEnum.movie.getCode();
				newMovie.setPrn(moviePrn);
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
				//将<br>替换为特殊符号，再解析为document，而后再获取文字
				String movieInfoStr = Jsoup.parse(movieInfoElement.html().replace("<br>", CommonConstants.SEPARATOR)).text();
				String[] movieInfoArr = movieInfoStr.split(CommonConstants.SEPARATOR);
				for(String movieInfo : movieInfoArr){
					int separatorIdx = movieInfo.indexOf(":");
					String title = movieInfo.substring(0, separatorIdx).trim();
					String content = movieInfo.substring(separatorIdx+1).trim();
					content = content.replaceAll("(?<=[^\\s])/(?=[^\\s])", " / ");
					if("导演".equals(title)){
						newMovie.setDirectors(content);
					} else if("编剧".equals(title)){
						newMovie.setWriters(content);
					}else if("主演".equals(title)){
						newMovie.setCasts(content);
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
						newMovie.setLanguages(content);
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
				newMovie.setCreateTime(thisTime);
				newMovie.setSt(MovieStatusEnum.available.getCode());
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
	
	protected MovieInfoVO fetchMovieFromDouban(String pureName, List<String> precisions, Date thisTime) throws MovieInfoFetchException {
		String doubanUrl = this.getDoubanUrl(pureName, precisions);
		MovieInfoVO movieInfo = fetchMovieFromDouban(doubanUrl, thisTime);
		return movieInfo;
	}
	
	/**
	 * 判断该影片是否在缓存中出现过，
	 * 避免出现一个网站两个相同影片，fetchPureName+fetchReleaseTimeStr做为key来过滤
	 * @param fetchMovie
	 * @param comeFromUrl
	 * @throws MovieInfoFetchException
	 */
	private boolean isRepeatedMovieInCache(MovieInfoVO fetchMovie, String comeFromUrl) throws MovieInfoFetchException {
		String fetchPureName = fetchMovie.getPureName();
		String fetchReleaseTimeStr = fetchMovie.getReleaseTimeStr();
		String repeatedUrl = cacheDistinct.get(fetchPureName+fetchReleaseTimeStr);
		if(repeatedUrl != null){
			log.info("warning!!! ["+fetchMovie.getPureName()+"] has appeared in this web. between "+ repeatedUrl +" and " + comeFromUrl);
			return true;
		}
		cacheDistinct.put(fetchPureName+fetchReleaseTimeStr, comeFromUrl);
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
		MovieInfoVO changeOption = this.changeOption(dbMovie, fetchMovie, thisTime);
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
	 * 构建icon对象
	 * @param iconUrl
	 * @return
	 */
	private PicVO buildIconPic(String iconUrl) {
		if(StringUtils.isBlank(iconUrl)){
			return null;
		}
		String iconName = iconUrl.substring(iconUrl.lastIndexOf("/")+1);
		if(iconName.equals(doubanDefaultIcon)) {
			return null;
		}
		String suffix = iconName.substring(iconName.lastIndexOf(".")+1);
		String fileName = StringUtil.getId(CommonConstants.pic_s)+"."+suffix;
		return new PicVO(iconUrl, fileName, CommonConstants.icon_v);
	}
	
	/**
	 * 从豆瓣PosterPage页面中爬取Poster图片list
	 * 
	 * @param posterPageUrl
	 * @param iconName 过滤掉icon的图片
	 * @return
	 */
	private List<PicVO> buildPosterPicList(String posterPageUrl, String iconName) {
		if(StringUtils.isBlank(posterPageUrl)) {
			return null;
		}
		List<PicVO> picList = new ArrayList<>();
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
						if(posterName.equals(iconName)){
							continue;
						}
						String suffix = posterName.substring(posterName.lastIndexOf(".")+1);
						String fileName = StringUtil.getId(CommonConstants.pic_s)+"."+suffix;
						PicVO posterPic = new PicVO(imgUrl, fileName, CommonConstants.poster_v);
						picList.add(posterPic);
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
		return picList;
			
	}
	
	/**
	 * 收集icon和poster
	 * 将收集到的icon和poster下载到服务器
	 * 将uri设置到fetchMovie中
	 * 
	 * @param fetchMovie
	 * @param picList
	 */
	private void downloadAndSetMoviePic(MovieInfoVO fetchMovie){
		//获取icon
		PicVO icon = buildIconPic(fetchMovie.getIconUrl());
		//获取poster
		List<PicVO> posterList = buildPosterPicList(fetchMovie.getPosterPageUrl(), icon.getFileName());
		//下载icon到服务器并设置uri
		try {
			String iconUri = this.picDownAndFix(icon.getFetchUrl(), ConfigUtil.getPropertyValue("icon.dir"), CommonConstants.icon_width, CommonConstants.icon_height);
			fetchMovie.setIconUri(iconUri);
		}catch (Exception e) {
			log.error("download icon wrong, url: {}", icon.getFetchUrl(), e);
		}
		
		//下载poster到服务器并设置uri
		List<String> posterNames = new ArrayList<String>();
		for(PicVO poster:posterList){
			try {
				String posterUri = this.picDownAndFix(poster.getFetchUrl(), ConfigUtil.getPropertyValue("poster.dir"), CommonConstants.icon_width, CommonConstants.icon_height);
				posterNames.add(posterUri);
			} catch (Exception e) {
				log.error("download poster wrong, url: {}", poster.getFetchUrl(), e);
			}
		}
		
		if(posterNames.size() > 0){
			String posterUriJson = JSON.toJSONString(posterNames);
			fetchMovie.setPosterUriJson(posterUriJson);
		}
		
	}
	
	/**
	 * 下载图片并修正图片大小，返回uri
	 * @param picUrl
	 * @return
	 * @throws Exception
	 */
	private String picDownAndFix(String picUrl, String targetDir, int fixWidth, int fixHeight) throws Exception {
		String suffix = picUrl.substring(picUrl.lastIndexOf(".")+1);
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		byte[] bytes = HttpUtils.getBytes(picUrl);
		byte[] imageFix = FileUtils.imageFix(bytes, fixWidth, fixHeight, suffix);
		String subDir = DateUtil.date2Str(new Date(), "yyyyMM");
		String saveDir = targetDir+"/"+subDir;
		FileUtils.saveFile(imageFix, saveDir, fileName, suffix);
		String uri = saveDir.replace(ConfigUtil.getPropertyValue("fs.formal.dir"), "");
		return uri+"/"+fileName+"."+suffix;
	}
	
	/**
	 * 下载图片并修正图片大小以及添加水印，返回uri
	 * @param picUrl
	 * @return
	 * @throws Exception
	 */
	private String picDownAndFixMark(String picUrl, String targetDir, int fixWidth, int fixHeight) throws Exception {
		String suffix = picUrl.substring(picUrl.lastIndexOf(".")+1);
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		byte[] bytes = HttpUtils.getBytes(picUrl);
		byte[] imageFix = FileUtils.imageFix(bytes, fixWidth, fixHeight, suffix);
		byte[] imageMark = FileUtils.imageMark(imageFix, suffix);
		String subDir = DateUtil.date2Str(new Date(), "yyyyMM");
		String saveDir = targetDir+"/"+subDir;
		FileUtils.saveFile(imageMark, saveDir, fileName, suffix);
		String uri = saveDir.replace(ConfigUtil.getPropertyValue("fs.formal.dir"), "");
		return uri+"/"+fileName+"."+suffix;
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
	private List<ResourceInfoVO> getResources(List<LinkInfoVO> links, MovieInfoVO fetchMovie, List<String> printScreens, String comeFromUrl, Date thisTime){
		List<ResourceInfoVO> resources = new ArrayList<ResourceInfoVO>();
		String anotherName = fetchMovie.getAnotherName();
		for(LinkInfoVO link:links){
			if(!Pattern.compile(RegexConstant.resource_protocol).matcher(link.getDownloadLink()).find()){
				log.info("The link of \""+link.getDownloadLink()+"\", that doesn't matching protocol, Supported: ed2k,thunder,magnet,torrent or baiduNet");
				continue;
			}
			link.setName(stripEngName(link.getName(), anotherName));
			ResourceInfoVO fetchResource = buildResourceInfoFromLinkName(fetchMovie, link, comeFromUrl, thisTime);
			fetchResource.setPrintscreenUrlList(printScreens);
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
			if(!this.isCanableLink(fetchResource.getDownloadLink())) {
				continue;
			}
			
			String filterKey = fetchResource.getDownloadLink();
			if(category == MovieCategoryEnum.movie.getCode()){
				ResourceInfoVO filterResource = filterMap.get(filterKey);
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
			if(Pattern.compile(RegexConstant.baiduNet).matcher(fetchResource.getDownloadLink()).find()) {
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
				//暂时标记为abandon，因为resource修改主要是修改下载链接和截图，符合修改条件的也许有多个，到后面只拿一个最佳的进行修改即可。
				fetchResource.setAction(CommonConstants.waiting);
				fetchResource.setUpdateTime(thisTime);
				fetchResource.setPrn(dbOptimalResource.getPrn());
			}
			//设置action在后续插入数据库时使用
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
	private void downloadAndSetPrintScreens(ResourceInfoVO optimalResource) {
		List<String> printscreenUrlList = optimalResource.getPrintscreenUrlList();
		if(printscreenUrlList == null){
			return;
		}
		List<String> printscreenUriList = new ArrayList<>();
		for(String printscreenUrl : printscreenUrlList){
			try {
				String repeatedPrintscreenUri = photoUrlMapping.get(printscreenUrl);
				if(repeatedPrintscreenUri == null){
					String printscreenUri = this.picDownAndFixMark(printscreenUrl, ConfigUtil.getPropertyValue("printscreen.dir"), CommonConstants.photo_width, CommonConstants.photo_height);
					photoUrlMapping.put(printscreenUrl, printscreenUri);
					printscreenUriList.add(printscreenUri);
				}else {
					printscreenUriList.add(repeatedPrintscreenUri);
				}
			} catch (Exception e) {
				log.error(optimalResource.getComeFromUrl(),e);
			}
		}
		if(printscreenUriList.size() > 0){
			String printscreenUriJson = JSON.toJSONString(printscreenUriList);
			optimalResource.setPrintscreenUriJson(printscreenUriJson);
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
	 * 批量分析下载链接并填充信息，如果是种子文件则下载到服务器，再设置。
	 * 
	 * @param fetchResource
	 * @throws Exception 
	 * @throws AnalystException
	 */
	private void analyzeDownloadLink(List<ResourceInfoVO> fetchResources){
		for(ResourceInfoVO fetchResource : fetchResources) {
			String link = fetchResource.getDownloadLink();
			try {
				if(StringUtils.isBlank(link)){
					log.info("The download link of \"["+fetchResource.getPureName()+"]\" is null");
					continue;
				}
				
				LinkAnalyzeResultVO analyzeResult = null;
				//根據需要分析下載鏈接
				String property = System.getProperty("java.library.path");
				if(StringUtils.isBlank(fetchResource.getSize()) || StringUtils.isBlank(fetchResource.getFormat())){
					analyzeResult = LinkAnalyst.analyseDownloadLink(link, fetchResource.getThunderDecoding());
					if(analyzeResult != null) {
						if(StringUtils.isBlank(fetchResource.getSize())) {
							fetchResource.setSize(analyzeResult.getMovieSize());
						}
						if(StringUtils.isBlank(fetchResource.getFormat())) {
							fetchResource.setFormat(analyzeResult.getMovieFormat());
						}
					}
				}
				
				//如果是種子文件則需要下載到服務器
				if(Pattern.compile(RegexConstant.torrent).matcher(link).find()){
					String torrentName = getTorrentName(fetchResource);
					byte[] torrentBytes = null;
					if(analyzeResult != null) {
						torrentBytes = analyzeResult.getTorrentBytes();
					}
					if(torrentBytes == null) {
						torrentBytes = HttpUtils.getBytes(link);
					}
					String suffix = link.substring(link.lastIndexOf(".")+1);
					String torrentDir = ConfigUtil.getPropertyValue("torrent.dir");
					String subDir = DateUtil.date2Str(new Date(), "yyyyMM");
					String saveDir = torrentDir+"/"+subDir;
					FileUtils.saveFile(torrentBytes, saveDir, torrentName, suffix);
					String uri = saveDir.replace(ConfigUtil.getPropertyValue("fs.formal.dir"), "");
					fetchResource.setDownloadLink(uri + "/"+torrentName + "." +suffix);
				}
			} catch (Throwable e) {
				log.error("解析下载链接失败！ url: "+ link, e);
			}
			
		}
	}
	
	/**
	 * 重新组建种子文件名
	 * @param fetchResource
	 * @return
	 */
	private String getTorrentName(ResourceInfoVO fetchResource) {
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
		String subtitleNotice = "";
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
		
		fileNameDistinct.put(fileName.toString(), 1);
		return fileName.toString();
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
			MovieLabelVO label = this.buildLabel(labelName, moviePrn, pureName, releaseTime, thisTime);
			labels.add(label);
		}
		return labels;
	}
	
	/**
	 * 构建一个label
	 * @param labelName
	 * @param moviePrn
	 * @param pureName
	 * @param releaseTime
	 * @param now
	 * @return
	 */
	private MovieLabelVO buildLabel(String labelName, String moviePrn, String pureName, Date releaseTime, Date thisTime) {
		MovieLabelVO label = new MovieLabelVO();
		label.setLabelName(labelName);
		label.setPrn(UIDGenerator.getUID()+"");
		label.setMoviePrn(moviePrn);
		label.setPureName(pureName);
		label.setReleaseTime(releaseTime);
		label.setCreateTime(thisTime);
		return label;
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
			MovieLocationVO location = this.buildLocation(locationName, moviePrn, pureName, releaseTime, thisTime);
			locations.add(location);
		}
		return locations;
	}
	
	private MovieLocationVO buildLocation(String locationName, String moviePrn, String pureName, Date releaseTime, Date thisTime) {
		MovieLocationVO location = new MovieLocationVO();
		location.setLocationName(locationName);
		location.setPrn(UIDGenerator.getUID()+"");
		location.setMoviePrn(moviePrn);
		location.setPureName(pureName);
		location.setReleaseTime(releaseTime);
		location.setCreateTime(thisTime);
		return location;
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
    	
    	
    	ResourceInfoVO newResource = new ResourceInfoVO();
    	newResource.setMoviePrn(moviePrn);
    	newResource.setPureName(pureName);
    	newResource.setReleaseTime(releaseTime);
    	newResource.setComeFromUrl(comeFromUrl);
    	newResource.setDownloadLink(downloadLink);
    	
    	String resourcePrn = StringUtil.getId(CommonConstants.resource_s);
    	newResource.setPrn(resourcePrn);
    	newResource.setSpeed(5);
    	newResource.setSt(MovieStatusEnum.available.getCode());
    	
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
		
		//计算清晰度得分
		Integer definitionScore = this.translateDefinitionIntoScore(newResource.getQuality(), newResource.getResolution());
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
		
		newResource.setCreateTime(thisTime);
		return newResource;
    }
    
    /**
     * 计算资源resource的得分情况
     * 方便进行资源的好坏的比较
     * @param quality
     * @param resolution
     * @return
     */
    private int translateDefinitionIntoScore(String quality, String resolution){
		
		if(StringUtils.isBlank(quality) && StringUtils.isBlank(resolution)){
			return 0;
		}
		if(StringUtils.isBlank(quality)){
			return MovieResolutionConstant.getPureResolutionScoreByKey(resolution);
		}
		
		if(StringUtils.isBlank(resolution)){
			return MovieQualityEnum.getScoreByName(quality);
		}
		
		BigDecimal rat = new BigDecimal(0.69);
		BigDecimal qualityScore = new BigDecimal(MovieQualityEnum.getScoreByName(quality));
		BigDecimal resolutionScore = new BigDecimal(MovieResolutionConstant.getResolutionScoreByKey(resolution));
		BigDecimal definition = qualityScore.add(resolutionScore).multiply(rat).setScale(0,BigDecimal.ROUND_HALF_UP);
		
		return definition.intValue();
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
	 * 集中处理电影和资源的文件
	 * 下载链接最为重要，如果有下载链接，其他的文件就算不下载依然可通过
	 * 如果没有下载链接，那么其他的文件统统不做处理
	 * 删除被替换的文件
	 * 下载新文件到服务器，再讲uri填充到对象中
	 * @param fetchMovie
	 * @param filter2
	 * @param fetchOptimalResource
	 * @param dbOptimalResource
	 * @return
	 */
	private void dealMovieAndResourceFiles(MovieInfoVO fetchMovie, List<ResourceInfoVO> filter2, ResourceInfoVO fetchOptimalResource,  ResourceInfoEntity dbOptimalResource) {
		//分析下载链接并填充下载信息，并判断是否有有效的下载链接
		//最好放在第一步，因为如果没有有效的下载链接，就等于没有资源，也就不需要去下载其他的文件了。
		this.analyzeDownloadLink(filter2);
		try {
			
			//下载电影相关图片信息到服务器，并设置uri
			this.downloadAndSetMoviePic(fetchMovie);
			
			//如果最佳的resource的action为waiting，那么就表示这个fetchOptimalResource应该替换掉dbOptimalResource
			if(fetchOptimalResource.getAction() == CommonConstants.waiting) {
				fetchOptimalResource.setAction(CommonConstants.update);
				//因为修改，所以需要删除db中resource文件，待后面添加新的resource文件
				if(dbOptimalResource != null) {
					ResourceInfoVO resource = VOUtils.po2vo(dbOptimalResource, ResourceInfoVO.class);
					FetchUtils.deleteResourceFile(resource);
				}
			}
			
			//下载截图并给最佳的resource设置截图uri
			this.downloadAndSetPrintScreens(fetchOptimalResource);
		} catch (Exception e) {
			log.error("", e);
		}
	}
	
	
	/**
	 * 将数据库中的电影信息，和新爬到的电影信息进行对比，看是否有更新项
	 * 如果有则赋值到新的movieInfo对象中并返回
	 * @param dbMovie
	 * @param fetchMovie
	 * @return
	 */
	private MovieInfoVO changeOption(MovieInfoEntity dbMovie, MovieInfoVO fetchMovie, Date thisTime){
    	MovieInfoVO changeOption = new MovieInfoVO();
    	boolean hasChange = false;
    	
    	if(StringUtils.isNotBlank(dbMovie.getLabels()) && 
    			StringUtils.isNotBlank(fetchMovie.getLabels()) &&
    			!dbMovie.getLabels().equals(fetchMovie.getLabels())) {
    		//从数据库中查询出的labels
    		List<MovieLabelEntity> dbLabels = movieLabelService.getListByMoviePrn(dbMovie.getPrn());
    		String[] fetchLabelNames = fetchMovie.getLabels().split(",");
    		
    		List<MovieLabelVO> newLabels = new ArrayList<>();
    		
    		for(String fetchLabelName : fetchLabelNames) {
    			for(MovieLabelEntity dbLabel : dbLabels) {
    				if(dbLabel.getLabelName().equals(fetchLabelName)) {
    					//如果有相同的，证明之前已经有了，直接break，循环下一个被fetch到的
    					break;
    				}
    			}
    			MovieLabelVO newLabel = this.buildLabel(fetchLabelName, dbMovie.getPrn(), dbMovie.getPureName(), dbMovie.getReleaseTime(), thisTime);
    			newLabels.add(newLabel);
    		}
    		if(newLabels.size() != 0) {
    			hasChange = true;
    			changeOption.setLabels(fetchMovie.getLabels());
    			changeOption.setLabelList(newLabels);
    		}
    	}
    	
    	
    	if(StringUtils.isNotBlank(dbMovie.getLocations()) && 
    			StringUtils.isNotBlank(fetchMovie.getLocations()) &&
    			!dbMovie.getLocations().equals(fetchMovie.getLocations())) {
    		//从数据库中查询出的labels 
    		List<MovieLocationEntity> dbLocations = movieLocationService.getListByMoviePrn(dbMovie.getPrn());
    		String[] fetchLocationNames = fetchMovie.getLocations().split(",");
    		
    		List<MovieLocationVO> newLocations = new ArrayList<>();
    		
    		for(String fetchLocationStr : fetchLocationNames) {
    			//地区只要中文，如果实在没有也没有办法！
    			Matcher m = Pattern.compile(RegexConstant.chinese).matcher(fetchLocationStr);
    			if(!m.find()){
    				continue;
    			}
    			String fetchLocationName = m.group();
    			for(MovieLocationEntity dbLocation : dbLocations) {
    				if(dbLocation.getLocationName().equals(fetchLocationName)) {
    					//如果有相同的，证明之前已经有了，直接break，循环下一个被fetch到的
    					break;
    				}
    			}
    			MovieLocationVO newLocation = this.buildLocation(fetchLocationName, dbMovie.getPrn(), dbMovie.getPureName(), dbMovie.getReleaseTime(), thisTime);
    			newLocations.add(newLocation);
    		}
    		if(newLocations.size() != 0) {
    			hasChange = true;
    			changeOption.setLocations(fetchMovie.getLocations());
    			changeOption.setLocationList(newLocations);
    		}
    	}
    	
    	
    	
    	
    	if(StringUtils.isBlank(dbMovie.getIconUri()) && StringUtils.isNotBlank(fetchMovie.getIconUri())){
    		changeOption.setIconUri(fetchMovie.getIconUri());
    		hasChange = true;
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getAnotherName())){
    		if(StringUtils.isBlank(dbMovie.getAnotherName()) || !dbMovie.getAnotherName().equals(fetchMovie.getAnotherName())){
    			changeOption.setAnotherName(fetchMovie.getAnotherName());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getLanguages())){
    		if(StringUtils.isBlank(dbMovie.getLanguages()) || !dbMovie.getLanguages().equals(fetchMovie.getLanguages())){
    			changeOption.setLanguages(fetchMovie.getLanguages());
    			hasChange = true;
    		}
    	}

    	if(fetchMovie.getReleaseTime() != null){
        	if(dbMovie.getReleaseTime() == null || !dbMovie.getReleaseTime().equals(fetchMovie.getReleaseTime())){
        		changeOption.setReleaseTime(fetchMovie.getReleaseTime());
        		changeOption.setReleaseTimeFormat(fetchMovie.getReleaseTimeFormat());
        		changeOption.setReleaseTimeStr(fetchMovie.getReleaseTimeStr());
    			hasChange = true;
        	}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getYear())){
    		if(StringUtils.isBlank(dbMovie.getYear()) || !dbMovie.getYear().equals(fetchMovie.getYear())){
    			changeOption.setYear(fetchMovie.getYear());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getDuration())){
    		if(StringUtils.isBlank(dbMovie.getDuration()) || !dbMovie.getDuration().equals(fetchMovie.getDuration())){
    			changeOption.setDuration(fetchMovie.getDuration());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getSummary())){
    		if(StringUtils.isBlank(dbMovie.getSummary()) || !dbMovie.getSummary().equals(fetchMovie.getSummary())){
    			changeOption.setSummary(fetchMovie.getSummary());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getDoubanId())){
    		if(StringUtils.isBlank(dbMovie.getDoubanId()) || !dbMovie.getDoubanId().equals(fetchMovie.getDoubanId())){
    			changeOption.setDoubanId(fetchMovie.getDoubanId());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getImdbId())){
    		if(StringUtils.isBlank(dbMovie.getImdbId()) || !dbMovie.getImdbId().equals(fetchMovie.getImdbId())){
    			changeOption.setImdbId(fetchMovie.getImdbId());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchMovie.getPresentSeason() != null){
    		if(dbMovie.getPresentSeason() == null || dbMovie.getPresentSeason().intValue() != fetchMovie.getPresentSeason().intValue()){
    			changeOption.setPresentSeason(fetchMovie.getPresentSeason());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchMovie.getTotalEpisode() != null){
    		if(dbMovie.getTotalEpisode() == null || dbMovie.getTotalEpisode().intValue() != fetchMovie.getTotalEpisode().intValue()){
    			changeOption.setTotalEpisode(fetchMovie.getTotalEpisode());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchMovie.getDoubanScore() != null){
    		if(dbMovie.getDoubanScore() == null || dbMovie.getDoubanScore().intValue() != fetchMovie.getDoubanScore().intValue()){
    			changeOption.setDoubanScore(fetchMovie.getDoubanScore());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchMovie.getImdbScore() != null){
    		if(dbMovie.getImdbScore() == null || dbMovie.getImdbScore().intValue() != fetchMovie.getImdbScore().intValue()){
    			changeOption.setImdbScore(fetchMovie.getImdbScore());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchMovie.getAttentionRate() != null){
    		if(dbMovie.getAttentionRate() == null || dbMovie.getAttentionRate().intValue() != fetchMovie.getAttentionRate().intValue()){
    			changeOption.setAttentionRate(fetchMovie.getAttentionRate());
    			hasChange = true;
    		}
    	}
    	
    	if(hasChange){
    		return changeOption;
    	}
    	return null;
    }
	
	
}
