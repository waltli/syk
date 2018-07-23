package com.sbolo.syk.view.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sbolo.syk.view.entity.ResourceInfoEntity;
import com.sbolo.syk.view.mapper.ResourceInfoMapper;

@Service
public class ResourceInfoService {
	private static final Logger log = LoggerFactory.getLogger(ResourceInfoService.class);
	@Resource
	private ResourceInfoMapper resourceInfoMapper;
	
	public ResourceInfoEntity getOptimalResource(String moviePrn) {
		ResourceInfoEntity dbOptimalResource = resourceInfoMapper.selectOptimalResource(moviePrn);
		return dbOptimalResource;
	}
	
}
