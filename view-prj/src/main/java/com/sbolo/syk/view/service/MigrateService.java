package com.sbolo.syk.view.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.vo.MigrateVO;
import com.sbolo.syk.view.entity.MovieDictEntity;
import com.sbolo.syk.view.entity.MovieInfoEntity;
import com.sbolo.syk.view.entity.ResourceInfoEntity;
import com.sbolo.syk.view.mapper.MovieDictMapper;
import com.sbolo.syk.view.mapper.MovieInfoMapper;
import com.sbolo.syk.view.mapper.ResourceInfoMapper;

@Service
public class MigrateService {
	
	@Autowired
	private MovieDictMapper movieDictMapper;
	
	@Autowired
	private MovieInfoMapper movieInfoMapper;
	
	@Autowired
	private ResourceInfoMapper resourceInfoMapper;
	
	@Transactional
	public void write(MigrateVO vo) {
		String addMovies = vo.getAddMovies();
		String addResources = vo.getAddResources();
		String addDicts = vo.getAddDicts();
		String updateMovies = vo.getUpdateMovies();
		String updateResources = vo.getUpdateResources();
		
		if(StringUtils.isNotBlank(addMovies)) {
			List<MovieInfoEntity> addMovieList = JSON.parseArray(addMovies, MovieInfoEntity.class);
			movieInfoMapper.insertList(addMovieList);
		}
		
		if(StringUtils.isNotBlank(addResources)) {
			List<ResourceInfoEntity> addResourceList = JSON.parseArray(addResources, ResourceInfoEntity.class);
			resourceInfoMapper.insertList(addResourceList);
		}
		
		if(StringUtils.isNotBlank(addDicts)) {
			List<MovieDictEntity> addDictList = JSON.parseArray(addDicts, MovieDictEntity.class);
			movieDictMapper.insertList(addDictList);
		}
		
		if(StringUtils.isNotBlank(updateMovies)) {
			List<MovieInfoEntity> updateMovieList = JSON.parseArray(updateMovies, MovieInfoEntity.class);
			movieInfoMapper.updateListByPrn(updateMovieList);
		}
		
		if(StringUtils.isNotBlank(updateResources)) {
			List<ResourceInfoEntity> updateResourceList = JSON.parseArray(updateResources, ResourceInfoEntity.class);
			resourceInfoMapper.updateListByPrn(updateResourceList);
		}
		
		
	}
}
