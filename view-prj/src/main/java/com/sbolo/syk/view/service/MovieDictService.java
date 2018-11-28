package com.sbolo.syk.view.service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sbolo.syk.common.constants.MovieDictEnum;
import com.sbolo.syk.view.mapper.MovieDictMapper;

@Service
public class MovieDictService {
	
	private static final Logger log = LoggerFactory.getLogger(MovieDictService.class);
	
	@Resource
	private MovieDictMapper movieDictMapper;
	
	public List<String> getLabels() {
		List<String> labelEntities = movieDictMapper.selectByParentPrn(MovieDictEnum.LABEL.getCode());
		return labelEntities;
	}
	
	public List<String> getLocations() {
		List<String> locationEntities = movieDictMapper.selectByParentPrn(MovieDictEnum.LOCATION.getCode());
		return locationEntities;
	}
	
}
