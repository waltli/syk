package com.sbolo.syk.admin.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import okhttp3.Response;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.constants.TriggerEnum;
import com.sbolo.syk.common.exception.MovieInfoFetchException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.callback.HttpSendCallbackPure;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.admin.entity.MovieHotStatEntity;
import com.sbolo.syk.admin.entity.MovieInfoEntity;
import com.sbolo.syk.admin.entity.ResourceInfoEntity;
import com.sbolo.syk.admin.mapper.MovieHotStatMapper;
import com.sbolo.syk.admin.mapper.MovieInfoMapper;
import com.sbolo.syk.admin.po.HotStatisticsEntity;
import com.sbolo.syk.admin.vo.MovieInfoVO;
import com.sbolo.syk.admin.vo.ResourceInfoVO;

@Service
public class MovieInfoService {
	
	private static final Logger log = LoggerFactory.getLogger(MovieInfoService.class);
	
	@Resource
	private MovieInfoMapper movieInfoMapper;
	
	@Resource
	private MovieHotStatMapper movieHotStatMapper;
	
	public RequestResult<MovieInfoVO> getAroundList(int pageNum, int pageSize, String label, String keyword){
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(keyword)){
        	params.put("keyword", keyword);
        }
        List<MovieInfoEntity> list = null;
		if(StringUtils.isBlank(label)){
			PageHelper.startPage(pageNum, pageSize, "t.resource_write_time DESC");
			list = movieInfoMapper.selectByAssociation(params);
		}else {
			params.put("label", label);
			PageHelper.startPage(pageNum, pageSize, "t.resource_write_time DESC");
			list = movieInfoMapper.selectByAssociationWithLabel(params);
		}
		PageInfo<MovieInfoEntity> pageInfo = new PageInfo<>(list);
		List<MovieInfoVO> movieVOList = new ArrayList<>();
		for(MovieInfoEntity movieEntity : list) {
			ResourceInfoEntity optimalResourceEntity = movieEntity.getOptimalResource();
			movieEntity.setOptimalResource(null);
			MovieInfoVO movieVO = VOUtils.po2vo(movieEntity, MovieInfoVO.class);
			if(optimalResourceEntity != null) {
				ResourceInfoVO optimalResourceVO = VOUtils.po2vo(optimalResourceEntity, ResourceInfoVO.class);
				movieVO.setOptimalResource(optimalResourceVO);
			}
			movieVOList.add(movieVO);
		}
		MovieInfoVO.parse(movieVOList);
		return new RequestResult<>(movieVOList, pageInfo.getTotal(), pageNum, pageSize);
	}
	
	
	public List<MovieInfoEntity> fetchFromDouban(final String query) throws Exception{
		
		final List<MovieInfoEntity> fetchMovies = new ArrayList<MovieInfoEntity>();
		
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
							
							MovieInfoEntity movie = new MovieInfoEntity();
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
}
