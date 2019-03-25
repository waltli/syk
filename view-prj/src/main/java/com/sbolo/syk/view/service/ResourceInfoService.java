package com.sbolo.syk.view.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sbolo.syk.common.enums.MovieCategoryEnum;
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
	
	public List<ResourceInfoEntity> getListByMoviePrnOrder(String moviePrn, Integer category){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("moviePrn", moviePrn);
		Boolean istv = false;
		if(category == MovieCategoryEnum.tv.getCode()){
			istv = true;
		}
		param.put("istv", istv);
		
		return resourceInfoMapper.selectByMoviePrnOrder(param);
	}
	
}
