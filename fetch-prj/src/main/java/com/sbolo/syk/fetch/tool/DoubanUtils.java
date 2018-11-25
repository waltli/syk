package com.sbolo.syk.fetch.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.constants.MovieTagEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.http.callback.HttpSendCallbackPure;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.fetch.spider.exception.SpiderException;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.PureNameAndSeasonVO;

import okhttp3.Response;

public class DoubanUtils {
	
	private static final Logger log = LoggerFactory.getLogger(DoubanUtils.class);
	
	/**
	 * 到豆瓣搜索页以pureName为关键字进行搜索后，再根据releaseTime进行筛选，
	 * 最后得出准确的pureName和releaseTimeStr以及doubanDetailUrl
	 * @param pureName
	 * @param releaseTimeStr
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public static String getDoubanUrl(final PureNameAndSeasonVO pureNameAndSeason, final List<String> precisions) throws Exception{
		HttpResult<String> result = HttpUtils.httpGet("https://api.douban.com/v2/movie/search?q="+Utils.encode(pureNameAndSeason.getPureName(), "UTF-8"), 
				new HttpSendCallback<String>() {
			@Override
			public String onResponse(Response response) throws Exception{
				if(!response.isSuccessful()){
					String responseStr = response.body().string();
					throw new SpiderException("在豆瓣中搜索影片["+pureNameAndSeason.getPureName()+"]失败，返回响应码："+response.code()+", response: "+responseStr);
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
					
					String pureName = pureNameAndSeason.getPureName();
					String noSeasonName = pureNameAndSeason.getNoSeasonName(); //因为豆瓣会存在少数电视剧资源在名字上没有加上第一季，故此做
					String name_s1 = pureName+" 第一季"; //有些网站美剧第一季通常不会把“第一季”写上，所以增加这个搜寻条件
					
					if(realPureName.equals(pureName) || originalName.equals(pureName) || 
								realPureName.equals(name_s1) || originalName.equals(name_s1)) {
						if(precisions != null && precisions.size() > 0 && Utils.containsOne(names, precisions)) {
							precision = 2;
							doubanDetailUrl = subject.getString("alt");
							break;
						}
					}else if((realPureName.equals(noSeasonName) || originalName.equals(noSeasonName)) && 
							precision < 1) {
						if(precisions != null && precisions.size() > 0 && Utils.containsOne(names, precisions)) {
							precision = 1;
							doubanDetailUrl = subject.getString("alt");
						}
					}
				}
			    if(precision == -1){
			    	throw new SpiderException("It's doesn't dovetailed with the result of douban search for ["+pureNameAndSeason.getPureName()+"]");
			    }
				return doubanDetailUrl;
				
			}
			
		});
		try {
			return result.getValue();
		} catch (Exception e) {
			if(e instanceof SpiderException){
				throw (SpiderException) e;
			}
			throw e;
		}
	}
	
	public static void main(String[] args) {
		String content = "剧情/科幻/惊悚";
		content = content.replaceAll("(?<=[^\\s])/(?=[^\\s])", " | ");
		System.out.println(content);
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
	 * @throws Exception 
     */
	public static MovieInfoVO fetchMovieFromDouban(String url, Date thisTime) throws Exception{
		HttpResult<MovieInfoVO> result = HttpUtils.httpGet(url, new HttpSendCallback<MovieInfoVO>() {

			@Override
			public MovieInfoVO onResponse(Response response)
					throws Exception {
				if(!response.isSuccessful()){
					throw new SpiderException("Get movie detail failed from douban, code:"+response.code()+", url:"+url);
				}
				String finalUrl = response.request().url().toString();
				if(!finalUrl.equals(url)){
					throw new SpiderException("Requested url: "+url+" that was redirected to "+finalUrl);
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
				String iconOutUrl = doc.select("#mainpic > a > img").first().attr("src");
				String iconName = iconOutUrl.substring(iconOutUrl.lastIndexOf("/")+1);
				if(!iconName.equals(CommonConstants.movie_default_icon) && !iconName.equals(CommonConstants.tv_default_icon)) {
					newMovie.setIconOutUrl(iconOutUrl);
				}
				
				Elements photoElements = doc.select("#related-pic li img");
				if(photoElements != null && photoElements.size() > 0) {
					List<String> thumPhotoOutUrlList = photoElements.eachAttr("src");
					List<String> photoOutUrlList = new ArrayList<>();
					for(String photoOutUrl : thumPhotoOutUrlList) {
						Matcher matcher = Pattern.compile("(?<=/photo/)(sqxs)").matcher(photoOutUrl);
						if(matcher.find()) {
							photoOutUrl = matcher.replaceAll("l");
						}else {
							log.warn("豆瓣电影的photo缩略图中没有发现sqxs, photoOutUrl: {}", photoOutUrl);
						}
						photoOutUrlList.add(photoOutUrl);
					}
					if(photoOutUrlList.size() > 0) {
						newMovie.setPhotoOutUrlList(photoOutUrlList);
					}
				}
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
					}else if("上映日期".equals(title)) {
						releaseTimeStr = Utils.getTimeStr(content);
					}else if("首播".equals(title)) {
						releaseTimeStr = Utils.getTimeStr(content);
						if(category == MovieCategoryEnum.tv.getCode()) {
							int tag = getTag(category, content);
							newMovie.setTag(tag);
						}
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
				String year = null;
				Element yearElement = doc.select("#content > h1 > span.year").first();
				if (yearElement != null) {
					year = doc.select("#content > h1 > span.year").first().text();
					year = Utils.getTimeStr(year);
					if(StringUtils.isBlank(releaseTimeStr)){
						releaseTimeStr = year;
					}
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
			if(e instanceof SpiderException){
				throw (SpiderException) e;
			}
			throw e;
		}
	}
	
	private static int getTag(int category, String text) {
		Integer tagCode = null;
		if(category == MovieCategoryEnum.tv.getCode()) {
			Matcher m2 = Pattern.compile(RegexConstant.TAG_LOCATION).matcher(text);
	    	String tagLocation = null;
	    	if(m2.find()){
	    		tagLocation = m2.group();
	    	}
	    	tagCode = MovieTagEnum.getCodeByLocation(tagLocation);
		}
		return tagCode;
	}
	
	public static List<MovieInfoVO> fetchListFromDouban(final String query) throws Exception{
		
		final List<MovieInfoVO> fetchMovies = new ArrayList<MovieInfoVO>();
		
		HttpUtils.httpGet("https://api.douban.com/v2/movie/search?q="+Utils.encode(query, "UTF-8"), 
				new HttpSendCallbackPure() {

					@Override
					public void onResponse(Response response)
							throws Exception {
						if(!response.isSuccessful()){
							String responseStr = response.body().string();
							throw new SpiderException("在豆瓣中搜索影片["+query+"]失败，返回响应码："+response.code()+", response: "+responseStr);
						}
						
						String contentJson = response.body().string();
						JSONObject content = JSON.parseObject(contentJson);
						JSONArray subjects = content.getJSONArray("subjects");
						for(int i=0; i<subjects.size(); i++){
							JSONObject subject = subjects.getJSONObject(i);
							JSONObject images = subject.getJSONObject("images");
							String icon = images.getString("small");
							String realPureName = subject.getString("title");
							String originalName = subject.getString("original_title");
							String year = subject.getString("year");
							String doubanId = subject.getString("id");
							JSONArray directors = subject.getJSONArray("directors");
							Map<String, Integer> castMap = new HashMap<String, Integer>();
							StringBuffer castbf = new StringBuffer();
							for(int j=0; j<directors.size(); j++){
								JSONObject director = directors.getJSONObject(j);
								String name = director.getString("name");
								if(Pattern.compile("^"+RegexConstant.chinese).matcher(name).find()){
					    			name = name.split(" ")[0];
					    		}
								if(castMap.get(name) != null){
									continue;
								}
								castbf.append(" / ").append(name);
								castMap.put(name, 1);
							}
							
							JSONArray casts = subject.getJSONArray("casts");
							for(int j=0; j<casts.size(); j++){
								JSONObject cast = casts.getJSONObject(j);
								String name = cast.getString("name");
								if(Pattern.compile("^"+RegexConstant.chinese).matcher(name).find()){
					    			name = name.split(" ")[0];
					    		}
								if(castMap.get(name) != null){
									continue;
								}
								castbf.append(" / ").append(name);
								castMap.put(name, 1);
							}
							if(castbf.length() > 0){
								castbf = castbf.replace(0, 3, "");
							}
							
							JSONArray genres = subject.getJSONArray("genres");
							String labelStr = "";
							for(int j=0; j<genres.size(); j++){
								labelStr += (" / "+genres.getString(j));
							}
							if(StringUtils.isNotBlank(labelStr)){
								labelStr = labelStr.substring(3);
							}
							
							MovieInfoVO movie = new MovieInfoVO();
							movie.setIconOutUrl(icon);
							movie.setPureName(realPureName);
							movie.setAnotherName(originalName);
							movie.setYear(year);
							movie.setCasts(castbf.toString());
							movie.setLabels(labelStr);
							movie.setDoubanId(doubanId);
							fetchMovies.add(movie);
						}
					}
		});
		
		return fetchMovies;
	}
	
	public static MovieInfoVO fetchMovieFromDouban(PureNameAndSeasonVO pureNameAndSeason, List<String> precisions, Date thisTime) throws Exception {
		String doubanUrl = getDoubanUrl(pureNameAndSeason, precisions);
		MovieInfoVO movieInfo = fetchMovieFromDouban(doubanUrl, thisTime);
		return movieInfo;
	}
	
	/**
	 * 从豆瓣PosterPage页面中爬取Poster图片list
	 * 
	 * @param posterPageUrl
	 * @param iconName 过滤掉icon的图片
	 * @return
	 */
	public static List<String> getPosterUrlList(String posterPageUrl, String iconOutUrl) {
		if(StringUtils.isBlank(posterPageUrl)) {
			return null;
		}
		String iconName = StringUtils.isNotBlank(iconOutUrl) ? iconOutUrl.substring(iconOutUrl.lastIndexOf("/")+1) : "";
		List<String> posterUrlList = new ArrayList<>();
		try {
			HttpUtils.httpGet(posterPageUrl, new HttpSendCallbackPure() {
				
				@Override
				public void onResponse(Response response) throws Exception {
					if(!response.isSuccessful()){
						throw new SpiderException("Get movie poster failed from douban, code:"+response.code()+", url:"+posterPageUrl);
					}
					Document doc = Jsoup.parse(new String(response.body().bytes(), "utf-8"));
					Elements lis = doc.select("#content > div > div.article > ul > li[data-id]");
					int lisSize = lis.size();
					if(lisSize >= 4) {
						lisSize = 4;
					}
					
					
					for(int i=0; i < lisSize; i++){
						Element li = lis.get(i);
						Element img = li.select(".cover > a > img").first();
						if(img == null){
							continue;
						}
						String imgUrl = img.attr("src").trim();
						if(StringUtils.isBlank(imgUrl)){
							continue;
						}
						//此处打开的是预览图，预览图较小，需要高清图，但高清图在下一层链接里，因知道高清图和预览图只有一个字段的区别，顾直接替换
						Matcher matcher = Pattern.compile("(?<=/photo/)(m)").matcher(imgUrl);
						if(matcher.find()) {
							imgUrl = matcher.replaceAll("l");
						}else {
							log.warn("豆瓣电影的poster缩略图中没有发现m, posterUrl: {}", imgUrl);
						}
						String posterName = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
						if(posterName.equals(iconName)){
							continue;
						}
						posterUrlList.add(imgUrl);
					}
				}
			});
		} catch (Exception e) {
			log.error("Request posterPageUrl failed. url: {}", posterPageUrl, e);
		}
		return posterUrlList;
			
	}
}
