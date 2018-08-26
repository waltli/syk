package com.sbolo.syk.fetch.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.mapper.ResourceInfoMapper;

@Service
public class ResourceInfoService {
	private static final Logger log = LoggerFactory.getLogger(ResourceInfoService.class);
	@Resource
	private ResourceInfoMapper resourceInfoMapper;
	
	public ResourceInfoEntity getOptimalResource(String moviePrn) {
		ResourceInfoEntity dbOptimalResource = resourceInfoMapper.selectOptimalResource(moviePrn);
		return dbOptimalResource;
	}
	
	public void updateStatusByMovieId(String moviePrn, int resourceStatus){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("moviePrn", moviePrn);
		params.put("resourceStatus", resourceStatus);
		resourceInfoMapper.signStatusByMoviePrn(params);
	}
	
}
