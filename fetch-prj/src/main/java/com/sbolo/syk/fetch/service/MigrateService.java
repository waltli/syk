package com.sbolo.syk.fetch.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.ui.RequestResult;
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

import okhttp3.Response;

@Service
public class MigrateService {
	private static final Logger log = LoggerFactory.getLogger(MigrateService.class);
	
	@Resource
	private MovieInfoMapper movieInfoMapper;
	
	@Resource
	private ResourceInfoMapper resourceInfoMapper;
	
	@Resource
	private MovieDictMapper movieDictMapper;
	
	@Resource
	private MovieFetchRecordMapper movieFetchRecordMapper;
	
	@Resource
	private MovieFetchRecordService movieFetchRecordService;
	
	public void migrate() throws Exception {
		log.info("======================开始数据迁移=========================");
		List<MovieFetchRecordEntity> noMigrated = movieFetchRecordService.getNoMigrated();
		log.info("======================待迁移数：{}=========================", noMigrated.size());
		MigrateVO todo = this.todo(noMigrated);
		
		String url = ConfigUtil.getPropertyValue("migrate.view.url");
		
		HttpResult<RequestResult> httpResult = HttpUtils.httpPost(url, todo, new HttpSendCallback<RequestResult>() {

			@Override
			public RequestResult<String> onResponse(Response response) throws Exception {
				if(!response.isSuccessful()) {
					return RequestResult.error("code: "+ response.code()+ " message: "+response.message());
				}
				String string = response.body().string();
				return JSON.parseObject(string, RequestResult.class);
			}
		});
		
		RequestResult result = httpResult.getValue();
		if(!result.getStatus()) {
			throw new Exception("数据迁移失败！cause:"+result.getError());
		}
		
		List<String> prnList = new ArrayList<>();
		for(MovieFetchRecordEntity entity : noMigrated) {
			prnList.add(entity.getPrn());
		}
		this.done(prnList);
		log.info("======================已迁移数：{}=========================", prnList.size());
	}
	
	private MigrateVO todo(List<MovieFetchRecordEntity> noMigrated) {

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
	
	private void done(List<String> prnList) {
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
