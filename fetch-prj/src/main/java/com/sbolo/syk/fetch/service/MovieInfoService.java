package com.sbolo.syk.fetch.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import okhttp3.Response;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;

@Service
public class MovieInfoService {
	
	private static final Logger log = LoggerFactory.getLogger(MovieInfoService.class);
	
	@Resource
	private MovieInfoMapper movieInfoMapper;
	
	public MovieInfoEntity getOneByPureNameAndYear(String pureName, Date year) {
		MovieInfoEntity movie = movieInfoMapper.selectOneByPureNameAndYear(pureName, year);
		return movie;
	}
}
