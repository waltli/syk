package com.sbolo.syk.fetch.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sbolo.syk.fetch.entity.MovieFetchRecordEntity;
import com.sbolo.syk.fetch.mapper.MovieFetchRecordMapper;

@Service
public class MovieFetchRecordService {
	
	private static final Logger log = LoggerFactory.getLogger(MovieFetchRecordService.class);
	
	@Resource
	private MovieFetchRecordMapper movieFetchRecordMapper;
	
	public List<MovieFetchRecordEntity> getNoMigrated() {
		List<MovieFetchRecordEntity> noMigrated = movieFetchRecordMapper.selectByHasMigrated(false);
		return noMigrated;
	}
	
}
