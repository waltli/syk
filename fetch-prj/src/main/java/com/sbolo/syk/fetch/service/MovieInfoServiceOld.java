package com.sbolo.syk.fetch.service;

import java.util.ArrayList;
import java.util.List;
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

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.fetch.dao.MovieInfoEntityMapper;
import com.sbolo.syk.fetch.dao.ResourceInfoEntityMapper;
import com.sbolo.syk.fetch.po.MovieInfoEntity;
import com.sbolo.syk.fetch.po.ResourceInfoEntity;

@Component
public class MovieInfoServiceOld {
	
	private static final Logger log = LoggerFactory.getLogger(MovieInfoServiceOld.class);
	
	private ThreadPoolTaskExecutor threadPool;
	
	private MovieInfoEntityMapper movieInfoEntityMapper;
	
	private ResourceInfoEntityMapper resourceInfoEntityMapper;
	
	public void updateMovie(){
		List<MovieInfoEntity> movies = movieInfoEntityMapper.selectAllNoCondition();
//		List<ResourceInfoEntity> resources = resourceInfoEntityMapper.selectAllNoCondition();
		List<MovieInfoEntity> toupMovies = new ArrayList<MovieInfoEntity>();
		
		for(int i=0; i<movies.size(); i++){
			
			MovieInfoEntity movie = movies.get(i);
			String movieId = movie.getMovieId();
			int category = movie.getCategory();
			List<ResourceInfoEntity> resources = resourceInfoEntityMapper.selectAllByMovieId(movieId);
			int optimalDefinition = -1;
			int optimalEpisodeEnd = -1;
			ResourceInfoEntity optimalResource = null;
			
			for(ResourceInfoEntity resource:resources){
				if(category == MovieCategoryEnum.movie.getCode()){
					if(resource.getDefinition().intValue() > optimalDefinition){
						optimalDefinition = resource.getDefinition().intValue();
						optimalResource = resource;
					}
				}else {
					if(resource.getEpisodeEnd().intValue() > optimalEpisodeEnd){
						optimalEpisodeEnd = resource.getEpisodeEnd().intValue();
						optimalResource = resource;
					}
				}
			}
			MovieInfoEntity toupMovie = new MovieInfoEntity();
			toupMovie.setMovieId(movieId);
			toupMovie.setOptimalResourceId(optimalResource.getResourceId());
			toupMovie.setResourceWriteTime(optimalResource.getCreateTime());
			
			toupMovies.add(toupMovie);
		}
		
		movieInfoEntityMapper.batchUpdateOptimalByMovieId(toupMovies);
		
	}
}
