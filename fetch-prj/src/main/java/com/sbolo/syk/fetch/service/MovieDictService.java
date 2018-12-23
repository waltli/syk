package com.sbolo.syk.fetch.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieDictEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.fetch.entity.MovieDictEntity;
import com.sbolo.syk.fetch.entity.MovieFetchRecordEntity;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.mapper.MovieDictMapper;
import com.sbolo.syk.fetch.mapper.MovieFetchRecordMapper;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.vo.MovieDictVO;
import com.sbolo.syk.fetch.vo.MovieInfoVO;

@Service
public class MovieDictService {
	
	private static final Logger log = LoggerFactory.getLogger(MovieDictService.class);
	
	@Resource
	private MovieDictMapper movieDictMapper;
	
	@Resource
	private MovieFetchRecordMapper movieFetchRecordMapper;
	
	@Resource
	private MovieInfoMapper movieInfoMapper;
	
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
	
	public String getTagRoot() {
		return movieDictMapper.selectByCode(MovieDictEnum.TAG.getCode());
	}
	
	public List<String> getTags() {
		List<String> tagEntities = movieDictMapper.selectByParentCode(MovieDictEnum.TAG.getCode());
		return tagEntities;
	}
	
	public List<MovieDictEntity> getAll(){
		List<MovieDictEntity> selectAll = movieDictMapper.selectAll();
		return selectAll;
	}
	
	public List<MovieDictVO> distDict(List<MovieInfoVO> fetchMovies, Date thisTime) throws Exception{
		if(fetchMovies == null || fetchMovies.size() == 0) {
			return null;
		}
		
		//放在Set里面去重
		Set<String> setLabels = new HashSet<>();
		Set<String> setLocations = new HashSet<>();
		Set<String> setTags = new HashSet<>();
		for(MovieInfoVO fetchMovie : fetchMovies) {
			String labels = fetchMovie.getLabels();
			String locations = fetchMovie.getLocations();
			String tag = fetchMovie.getTag();
			
			if(StringUtils.isNotBlank(labels)) {
				List<String> oneLabelList = Arrays.asList(labels.split(RegexConstant.slashSep));
				for(String oneLabel : oneLabelList) {
					//添加到Set里面去重
					setLabels.add(oneLabel);
				}
			}
			
			if(StringUtils.isNotBlank(locations)) {
				List<String> oneLocationList = Arrays.asList(locations.split(RegexConstant.slashSep));
				for(String oneLocation : oneLocationList) {
					//添加到Set里面去重
					setLocations.add(oneLocation);
				}
			}
			
			if(StringUtils.isNotBlank(tag)) {
				setTags.add(tag);
			}
		}
		
		if(setLabels.size() == 0 && setLocations.size() == 0 && setTags.size() == 0) {
			return null;
		}
		
		List<MovieDictVO> fetchDicts = new ArrayList<>();
		List<String> codes = MovieDictEnum.getCodes();
		List<MovieDictEntity> all = movieDictMapper.selectByRootCodes(codes);
		if(all != null && all.size() > 0) {
			//放在MAP中在后面进行过滤是否存在
			Map<String, Integer> dbMap = new HashMap<>();
			for(MovieDictEntity dictEntity : all) {
				String val = dictEntity.getVal();
				String parentCode = dictEntity.getParentCode();
				String key = this.getDictKey(parentCode, val);
				dbMap.put(key, 1);
			}
			
			if(setLabels.size() > 0) {
				List<MovieDictVO> labelDicts = this.buildDict(dbMap, setLabels, MovieDictEnum.LABEL.getCode(), MovieDictEnum.LABEL.getDesc(), CommonConstants.label_s, thisTime);
				fetchDicts.addAll(labelDicts);
			}
			
			if(setLocations.size() > 0) {
				List<MovieDictVO> locationDicts = this.buildDict(dbMap, setLocations, MovieDictEnum.LOCATION.getCode(), MovieDictEnum.LOCATION.getDesc(), CommonConstants.location_s, thisTime);
				fetchDicts.addAll(locationDicts);
			}
			
			if(setTags.size() > 0) {
				List<MovieDictVO> tagDicts = this.buildDict(dbMap, setTags, MovieDictEnum.TAG.getCode(), MovieDictEnum.TAG.getDesc(), CommonConstants.tag_s, thisTime);
				fetchDicts.addAll(tagDicts);
			}
			
		}else {
			MovieDictVO labelRoot = MovieDictVO.buildRoot(MovieDictEnum.LABEL.getCode(), MovieDictEnum.LABEL.getDesc(), thisTime);
			MovieDictVO locationRoot = MovieDictVO.buildRoot(MovieDictEnum.LOCATION.getCode(), MovieDictEnum.LOCATION.getDesc(), thisTime);
			MovieDictVO tagRoot = MovieDictVO.buildRoot(MovieDictEnum.TAG.getCode(), MovieDictEnum.TAG.getDesc(), thisTime);
			fetchDicts.add(labelRoot);
			fetchDicts.add(locationRoot);
			fetchDicts.add(tagRoot);
			
			for(String fetchLabel : setLabels) {
				MovieDictVO vo = MovieDictVO.build(StringUtil.getId(CommonConstants.label_s), MovieDictEnum.LABEL.getCode(), MovieDictEnum.LABEL.getCode(), fetchLabel, 2, thisTime);
				fetchDicts.add(vo);
			}
			
			for(String fetchLocation : setLocations) {
				MovieDictVO vo = MovieDictVO.build(StringUtil.getId(CommonConstants.location_s), MovieDictEnum.LOCATION.getCode(), MovieDictEnum.LOCATION.getCode(), fetchLocation, 2, thisTime);
				fetchDicts.add(vo);
			}
			
			for(String fetchTag : setTags) {
				MovieDictVO vo = MovieDictVO.build(StringUtil.getId(CommonConstants.tag_s), MovieDictEnum.TAG.getCode(), MovieDictEnum.TAG.getCode(), fetchTag, 2, thisTime);
				fetchDicts.add(vo);
			}
		}
		return fetchDicts;
		
	}
	
	private String getDictKey(String parentCode, String val) {
		return parentCode + "-" + val;
	}
	
	private List<MovieDictVO> buildDict(Map<String, Integer> dbMap, Set<String> vals, String parentCode, String parentVal, String sign, Date thisTime) {
		String rootKey = this.getDictKey(CommonConstants.DICT_TOP, parentVal);
		List<MovieDictVO> dicts = new ArrayList<>();
		if(dbMap.get(rootKey) == null) {
			MovieDictVO root = MovieDictVO.buildRoot(parentCode, parentVal, thisTime);
			dicts.add(root);
		}
		
		for(String val : vals) {
			String key = this.getDictKey(parentCode, val);
			if(dbMap.get(key) != null) {
				continue;
			}
			MovieDictVO vo = MovieDictVO.build(StringUtil.getId(sign), parentCode, parentCode, val, 2, thisTime);
			dicts.add(vo);
		}
		
		return dicts;
	}
	
	@Transactional
	public void junitInsert(List<MovieDictEntity> dictAll) {
		movieDictMapper.insertList(dictAll);
	}
	
	@Transactional
	public void junitUP(List<MovieInfoEntity> toupList) {
		movieInfoMapper.updateListByPrn(toupList);
	}
}
