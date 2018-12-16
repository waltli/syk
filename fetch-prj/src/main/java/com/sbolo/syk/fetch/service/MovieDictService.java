package com.sbolo.syk.fetch.service;

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
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.MovieDictEnum;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.BucketUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.fetch.entity.MovieDictEntity;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.mapper.MovieDictMapper;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.mapper.ResourceInfoMapper;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

@Service
public class MovieDictService {
	
	private static final Logger log = LoggerFactory.getLogger(MovieDictService.class);
	
	@Resource
	private MovieDictMapper movieDictMapper;
	
	public List<String> getLabels() {
		List<String> labelEntities = movieDictMapper.selectByParentCode(MovieDictEnum.LABEL.getCode());
		return labelEntities;
	}
	
	public List<String> getLocations() {
		List<String> locationEntities = movieDictMapper.selectByParentCode(MovieDictEnum.LOCATION.getCode());
		return locationEntities;
	}
	
	public String getLabelRoot() {
		return movieDictMapper.selectByCode(MovieDictEnum.LABEL.getCode());
	}
	
	public String getLocationRoot() {
		return movieDictMapper.selectByCode(MovieDictEnum.LOCATION.getCode());
	}
	
}
