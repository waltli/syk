package com.sbolo.syk.admin.tools;

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
import com.sbolo.syk.admin.vo.MovieInfoVO;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.exception.MovieInfoFetchException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.http.callback.HttpSendCallbackPure;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.Utils;

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
	 * @throws MovieInfoFetchException 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public static String getDoubanUrl(final String pureName, final List<String> precisions) throws MovieInfoFetchException{
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
	public static MovieInfoVO fetchMovieFromDouban(String url, Date thisTime) throws MovieInfoFetchException{
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
				
				Elements photoElements = doc.select("#related-pic li img");
				if(photoElements != null && photoElements.size() > 0) {
					List<String> thumPhotoUrlList = photoElements.eachAttr("src");
					List<String> photoUrlList = new ArrayList<>();
					for(String photoUrl : thumPhotoUrlList) {
						Matcher matcher = Pattern.compile("(?<=/photo/)(sqxs)").matcher(photoUrl);
						if(matcher.find()) {
							photoUrl = matcher.replaceAll("l");
						}else {
							log.warn("豆瓣电影的photo缩略图中没有发现sqxs, photoUrl: {}", photoUrl);
						}
						photoUrlList.add(photoUrl);
					}
					if(photoUrlList.size() > 0) {
						newMovie.setPhotoUrlList(photoUrlList);
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
	
	public static List<MovieInfoVO> fetchListFromDouban(final String query) throws Exception{
		
		final List<MovieInfoVO> fetchMovies = new ArrayList<MovieInfoVO>();
		
		HttpUtils.httpGet("https://api.douban.com/v2/movie/search?q="+Utils.encode(query, "UTF-8"), 
				new HttpSendCallbackPure() {

					@Override
					public void onResponse(Response response)
							throws Exception {
						if(!response.isSuccessful()){
							String responseStr = response.body().string();
							throw new MovieInfoFetchException("在豆瓣中搜索影片["+query+"]失败，返回响应码："+response.code()+", response: "+responseStr);
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
							movie.setIconUri(icon);
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
	
	public static MovieInfoVO fetchMovieFromDouban(String pureName, List<String> precisions, Date thisTime) throws MovieInfoFetchException {
		String doubanUrl = getDoubanUrl(pureName, precisions);
		MovieInfoVO movieInfo = fetchMovieFromDouban(doubanUrl, thisTime);
		return movieInfo;
	}
}