package com.sbolo.syk.view.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.TriggerEnum;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.view.entity.MovieHotStatEntity;
import com.sbolo.syk.view.entity.MovieInfoEntity;
import com.sbolo.syk.view.entity.ResourceInfoEntity;
import com.sbolo.syk.view.mapper.MovieHotStatMapper;
import com.sbolo.syk.view.mapper.MovieInfoMapper;
import com.sbolo.syk.view.vo.MovieInfoVO;
import com.sbolo.syk.view.vo.ResourceInfoVO;

@Service
public class MovieInfoService {
	
	private static final Logger log = LoggerFactory.getLogger(MovieInfoService.class);
	
	@Resource
	private MovieInfoMapper movieInfoMapper;
	
	@Resource
	private MovieHotStatMapper movieHotStatMapper;
	
	public RequestResult<MovieInfoVO> getAroundList(int pageNum, int pageSize, String label, String keyword){
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(keyword)){
        	params.put("keyword", keyword);
        }
        List<MovieInfoEntity> list = null;
		if(StringUtils.isBlank(label)){
			PageHelper.startPage(pageNum, pageSize, "t.resource_write_time DESC");
			list = movieInfoMapper.selectByAssociation(params);
		}else {
			params.put("label", label);
			PageHelper.startPage(pageNum, pageSize, "t.resource_write_time DESC");
			list = movieInfoMapper.selectByAssociationWithLabel(params);
		}
		PageInfo<MovieInfoEntity> pageInfo = new PageInfo<>(list);
		List<MovieInfoVO> movieVOList = new ArrayList<>();
		for(MovieInfoEntity movieEntity : list) {
			ResourceInfoEntity optimalResourceEntity = movieEntity.getOptimalResource();
			movieEntity.setOptimalResource(null);
			MovieInfoVO movieVO = VOUtils.po2vo(movieEntity, MovieInfoVO.class);
			if(optimalResourceEntity != null) {
				ResourceInfoVO optimalResourceVO = VOUtils.po2vo(optimalResourceEntity, ResourceInfoVO.class);
				movieVO.setOptimalResource(optimalResourceVO);
			}
			movieVOList.add(movieVO);
		}
		MovieInfoVO.parse(movieVOList);
		return new RequestResult<>(movieVOList, pageInfo.getTotal(), pageNum, pageSize);
	}
	
	public Map<String, List<MovieHotStatEntity>> getCurrMonthTop(){
		Map<String, List<MovieHotStatEntity>> tops = null;
//		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		Calendar calendar = Calendar.getInstance();
		
		//获取当前日
//		Date timeCurr = calendar.getTime();
//		String curr = sdf.format(timeCurr);
		
		//获取本月当前日的前一天  为了不将实时的点击结果表示出来，故此做
		calendar.add(Calendar.DATE,-1);
		Date timeEnd = calendar.getTime();
//		String end = sdf.format(timeEnd);
		
		//获取本月第一天
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0); 
		calendar.set(Calendar.MINUTE,0); 
		calendar.set(Calendar.SECOND,0);
		Date timeStart = calendar.getTime();
//		String start = sdf.format(timeStart);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limitNum", 10);
		params.put("timeStart", timeStart);
		params.put("timeEnd", timeEnd);
		
		List<MovieHotStatEntity> entities = movieHotStatMapper.selectHotByTime(params);
		
		if(entities.size() != 0){
			tops = new HashMap<String, List<MovieHotStatEntity>>();
			String topKey = "本月";
			tops.put(topKey, entities);
		}
		return tops;
		
	}
	
	public Map<String, List<MovieHotStatEntity>> getLastMonthTop(){
		Map<String, List<MovieHotStatEntity>> tops = null;
//		SimpleDateFormat sdf = new SimpleDateFormat("MM曰dd日");
		
		Calendar calendar = Calendar.getInstance();
		//获取上月最后一天
		calendar.set(Calendar.DAY_OF_MONTH, 1); 
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23); 
		calendar.set(Calendar.MINUTE,59); 
		calendar.set(Calendar.SECOND,59); 
		Date timeEnd = calendar.getTime();
//		String end = sdf.format(timeEnd);
		
		//获取上月第一天
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0); 
		calendar.set(Calendar.MINUTE,0); 
		calendar.set(Calendar.SECOND,0);
		Date timeStart = calendar.getTime();
//		String start = sdf.format(timeStart);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limitNum", 10);
		params.put("timeStart", timeStart);
		params.put("timeEnd", timeEnd);
		
		List<MovieHotStatEntity> entities = movieHotStatMapper.selectHotByTime(params);
		if(entities.size() != 0){
			tops = new HashMap<String, List<MovieHotStatEntity>>();
			String topKey = "上月";
			tops.put(topKey, entities);
		}
		return tops;
	}
	
	@Transactional
	public void modifyCountClick(String prn){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.error("",e);
		}
		movieInfoMapper.updateCountClick(prn);
		MovieHotStatEntity hotStat = buildHotStat(prn, TriggerEnum.click.getCode());
		movieHotStatMapper.insertSelective(hotStat);
	}
	
	public MovieInfoEntity getMovieByPrn(String prn){
		return movieInfoMapper.selectByPrn(prn);
	}
	
	@Transactional
	public void modifyCountDownload(String moviePrn){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.error("",e);
		}
		movieInfoMapper.updateCountDownload(moviePrn);
		MovieHotStatEntity hotStat = buildHotStat(moviePrn, TriggerEnum.download.getCode());
		movieHotStatMapper.insertSelective(hotStat);
	}
	
	private MovieHotStatEntity buildHotStat(String prn, Integer trigger){
		MovieInfoEntity movie = movieInfoMapper.selectByPrn(prn);
		MovieHotStatEntity hot = new MovieHotStatEntity();
		hot.setPrn(StringUtil.getId(CommonConstants.hot_s));
		hot.setMoviePrn(prn);
		hot.setDoubanScore(movie.getDoubanScore());
		hot.setImdbScore(movie.getImdbScore());
		hot.setPureName(movie.getPureName());
		hot.setReleaseTime(movie.getReleaseTime());
		hot.setTriggerType(trigger);
		hot.setCreateTime(new Date());
		return hot;
	}
}
