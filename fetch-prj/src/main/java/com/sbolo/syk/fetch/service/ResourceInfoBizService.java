package com.sbolo.syk.fetch.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.fetch.dao.ResourceInfoEntityMapper;
import com.sbolo.syk.fetch.po.MovieInfoEntity;
import com.sbolo.syk.fetch.po.ResourceInfoEntity;

@Service
public class ResourceInfoBizService {
	private static final Logger log = LoggerFactory.getLogger(ResourceInfoBizService.class);
	@Resource
	private ResourceInfoEntityMapper resourceInfoEntityMapper;
	
	public ResourceInfoEntity getHighestResource(String movieId, Integer category, Integer episode, boolean hasSubtitle){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("movieId", movieId);
		if(category == MovieCategoryEnum.tv.getCode()){
			param.put("episode", episode);
		}
		param.put("hasSubtitle", hasSubtitle);
		ResourceInfoEntity resourceInfoEntities = resourceInfoEntityMapper.selectHighestResource(param);
		return resourceInfoEntities;
	}
	
	public void batchAdd(List<ResourceInfoEntity> ries){
		resourceInfoEntityMapper.batchInsert(ries);
	}
	
	public List<ResourceInfoEntity> getListByMovieIdOrder(String movieId, Integer category){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("movieId", movieId);
		Boolean istv = false;
		if(category == MovieCategoryEnum.tv.getCode()){
			istv = true;
		}
		param.put("istv", istv);
		
		return resourceInfoEntityMapper.selectByMovieIdOrder(param);
	}
	
	public List<ResourceInfoEntity> getListByMovieIdOrderNoStatus(String movieId, Integer category){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("movieId", movieId);
		Boolean istv = false;
		if(category == MovieCategoryEnum.tv.getCode()){
			istv = true;
		}
		param.put("istv", istv);
		
		return resourceInfoEntityMapper.selectByMovieIdOrderNostatus(param);
	}
	
	public void updateStatus(String resourceId, int resourceStatus){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourceId", resourceId);
		params.put("resourceStatus", resourceStatus);
		resourceInfoEntityMapper.signStatusByResourceId(params);
	}
	
	public void updateStatusByMovieId(String movieId, int resourceStatus){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("movieId", movieId);
		params.put("resourceStatus", resourceStatus);
		resourceInfoEntityMapper.signStatusByMovieId(params);
	}
	
	public List<ResourceInfoEntity> getByMovieIdStatus(String movieId, int resourceStatus){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("movieId", movieId);
		params.put("resourceStatus", resourceStatus);
		return resourceInfoEntityMapper.selectAllByMovieIdAndStatus(params);
	}
	
	public ResourceInfoEntity getResourceByResourceId(String resourceId){
		ResourceInfoEntity resource = resourceInfoEntityMapper.selectByResourceId(resourceId);
		return resource;
	}
	
	public void modiResourceByResourceId(ResourceInfoEntity resource){
		resourceInfoEntityMapper.updateByResourceIdSelective(resource);
	}
}
