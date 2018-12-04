package com.sbolo.syk.fetch.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sbolo.syk.common.vo.MigrateVO;
import com.sbolo.syk.fetch.entity.MovieDictEntity;
import com.sbolo.syk.fetch.entity.MovieFetchRecordEntity;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.enums.OperateTypeEnum;
import com.sbolo.syk.fetch.enums.RelyDataEnum;
import com.sbolo.syk.fetch.mapper.MovieDictMapper;
import com.sbolo.syk.fetch.mapper.MovieFetchRecordMapper;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.mapper.ResourceInfoMapper;

@Service
public class MigrateService {
	
	@Resource
	private MovieInfoMapper movieInfoMapper;
	
	@Resource
	private ResourceInfoMapper resourceInfoMapper;
	
	@Resource
	private MovieDictMapper movieDictMapper;
	
	@Resource
	private MovieFetchRecordMapper movieFetchRecordMapper;
	
	public MigrateVO todo(List<MovieFetchRecordEntity> noMigrated) {

		List<String> addMoviePrns = new ArrayList<>();
		List<String> addResourcePrns = new ArrayList<>();
		List<String> addDictPrns = new ArrayList<>();
		List<JSONObject> movieUpdateJsonArray = new ArrayList<>();
		List<JSONObject> resourceUpdateJsonArray = new ArrayList<>();
		if(noMigrated != null && noMigrated.size() > 0) {
			for(MovieFetchRecordEntity record : noMigrated) {
				if(OperateTypeEnum.insert.getCode() == record.getOperateType()) {
					if(RelyDataEnum.moive.getCode().equals(record.getRelyData())) {
						addMoviePrns.add(record.getDataPrn());
					}else if(RelyDataEnum.resource.getCode().equals(record.getRelyData())) {
						addResourcePrns.add(record.getDataPrn());
					}else if(RelyDataEnum.dict.getCode().equals(record.getRelyData())) {
						addDictPrns.add(record.getDataPrn());
					}
				} else if(OperateTypeEnum.update.getCode() == record.getOperateType()) {
					JSONObject parseObject = JSON.parseObject(record.getDataJson());
					if(RelyDataEnum.moive.getCode().equals(record.getRelyData())) {
						movieUpdateJsonArray.add(parseObject);
					}else if(RelyDataEnum.resource.getCode().equals(record.getRelyData())) {
						resourceUpdateJsonArray.add(parseObject);
					}
				}
			}
		}
		
		MigrateVO migrate = new MigrateVO();
		
		if(addMoviePrns.size() > 0) {
			List<MovieInfoEntity> movieEntities = movieInfoMapper.selectByPrnList(addMoviePrns);
			if(movieEntities != null && movieEntities.size() > 0) {
				String addMovies = JSON.toJSONString(movieEntities);
				migrate.setAddMovies(addMovies);
			}
		}
		if(addResourcePrns.size() > 0) {
			List<ResourceInfoEntity> resourceEntities = resourceInfoMapper.selectByPrnList(addResourcePrns);
			if(resourceEntities != null && resourceEntities.size() > 0) {
				String addResources = JSON.toJSONString(resourceEntities);
				migrate.setAddResources(addResources);
			}
		}
		if(addDictPrns.size() > 0) {
			List<MovieDictEntity> dictEntities = movieDictMapper.selectByPrnList(addDictPrns);
			if(dictEntities != null && dictEntities.size() > 0) {
				String addDicts = JSON.toJSONString(dictEntities);
				migrate.setAddDicts(addDicts);
			}
		}
		if(movieUpdateJsonArray.size() > 0) {
			String updateMovies = JSON.toJSONString(movieUpdateJsonArray);
			migrate.setUpdateMovies(updateMovies);
		}
		if(resourceUpdateJsonArray.size() > 0) {
			String updateResources = JSON.toJSONString(resourceUpdateJsonArray);
			migrate.setUpdateResources(updateResources);
		}
		return migrate;
	}
	
	@Transactional
	public void done(List<String> prnList) {
		List<MovieFetchRecordEntity> recordList = new ArrayList<>();
		for(String prn : prnList) {
			MovieFetchRecordEntity mfre = new MovieFetchRecordEntity();
			mfre.setPrn(prn);
			mfre.setHasMigrated(true);
		}
		
		if(recordList.size() > 0) {
			movieFetchRecordMapper.updateListByPrn(recordList);
		}
	}
}
