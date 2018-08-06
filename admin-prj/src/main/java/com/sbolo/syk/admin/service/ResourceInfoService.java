package com.sbolo.syk.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.admin.entity.ResourceInfoEntity;
import com.sbolo.syk.admin.mapper.ResourceInfoMapper;

@Service
public class ResourceInfoService {
	private static final Logger log = LoggerFactory.getLogger(ResourceInfoService.class);
	
	@Resource
	private ResourceInfoMapper resourceInfoMapper;
	
}
