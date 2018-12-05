package com.sbolo.syk.fetch.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.common.vo.MigrateVO;
import com.sbolo.syk.fetch.entity.MovieFetchRecordEntity;
import com.sbolo.syk.fetch.mapper.MovieFetchRecordMapper;

import okhttp3.Response;

@Service
public class MovieFetchRecordService {
	
	private static final Logger log = LoggerFactory.getLogger(MovieFetchRecordService.class);
	
	@Resource
	private MovieFetchRecordMapper movieFetchRecordMapper;
	
	@Resource
	private MigrateService migrateService;
	
	public List<MovieFetchRecordEntity> getNoMigrated() {
		List<MovieFetchRecordEntity> noMigrated = movieFetchRecordMapper.selectByHasMigrated(false);
		return noMigrated;
	}
	
	
	
}
