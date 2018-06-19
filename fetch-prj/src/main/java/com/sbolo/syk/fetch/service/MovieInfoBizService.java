package com.sbolo.syk.fetch.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.TriggerEnum;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.fetch.dao.HotStatisticsEntityMapper;
import com.sbolo.syk.fetch.dao.LabelMappingEntityMapper;
import com.sbolo.syk.fetch.dao.LocationMappingEntityMapper;
import com.sbolo.syk.fetch.dao.MovieInfoEntityMapper;
import com.sbolo.syk.fetch.dao.MovieResourceEntityMapper;
import com.sbolo.syk.fetch.dao.ResourceInfoEntityMapper;
import com.sbolo.syk.fetch.po.HotStatisticsEntity;
import com.sbolo.syk.fetch.po.LabelMappingEntity;
import com.sbolo.syk.fetch.po.LocationMappingEntity;
import com.sbolo.syk.fetch.po.MovieInfoEntity;
import com.sbolo.syk.fetch.po.MovieResourceEntity;
import com.sbolo.syk.fetch.po.ResourceInfoEntity;

@Service
public class MovieInfoBizService {
	private static final Logger log = LoggerFactory.getLogger(MovieInfoBizService.class);
	
	@Resource
	private MovieInfoEntityMapper movieInfoEntityMapper;
	
	@Resource
	private MovieResourceEntityMapper movieResourceEntityMapper;
	
	@Resource
	private LabelMappingEntityMapper labelMappingEntityMapper;
	
	@Resource
	private LocationMappingEntityMapper locationMappingEntityMapper;
	
	@Resource
	private ResourceInfoEntityMapper resourceInfoEntityMapper;
	
	@Resource
	private HotStatisticsEntityMapper hotStatisticsEntityMapper;
	
	@Resource
	private ResourceInfoBizService resourceInfoBizService;
	
	public static void main(String[] args) {
		MovieInfoBizService f = new MovieInfoBizService();
		f.getCurrMonthTop();
	}
	
	public void modiMovieInfo(MovieInfoEntity modiInfo){
		movieInfoEntityMapper.updateByMovieIdSelective(modiInfo);
	}
	
	public Map<String, List<HotStatisticsEntity>> getCurrMonthTop(){
		Map<String, List<HotStatisticsEntity>> tops = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		Calendar calendar = Calendar.getInstance();
		
		//获取本月当前日
		Date timeEnd = calendar.getTime();
		String end = sdf.format(timeEnd);
		
		//获取本月第一天
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0); 
		calendar.set(Calendar.MINUTE,0); 
		calendar.set(Calendar.SECOND,0);
		Date timeStart = calendar.getTime();
		String start = sdf.format(timeStart);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limitNum", 10);
		params.put("timeStart", timeStart);
		params.put("timeEnd", timeEnd);
		
		List<HotStatisticsEntity> entities = hotStatisticsEntityMapper.selectHotByTime(params);
		
		if(entities.size() != 0){
			tops = new HashMap<String, List<HotStatisticsEntity>>();
			String topKey = start+"-"+end;
			tops.put(topKey, entities);
		}
		return tops;
		
	}
	
	public Map<String, List<HotStatisticsEntity>> getLastMonthTop(){
		Map<String, List<HotStatisticsEntity>> tops = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM曰dd日");
		
		Calendar calendar = Calendar.getInstance();
		//获取上月最后一天
		calendar.set(Calendar.DAY_OF_MONTH, 1); 
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23); 
		calendar.set(Calendar.MINUTE,59); 
		calendar.set(Calendar.SECOND,59); 
		Date timeEnd = calendar.getTime();
		String end = sdf.format(timeEnd);
		
		//获取上月第一天
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0); 
		calendar.set(Calendar.MINUTE,0); 
		calendar.set(Calendar.SECOND,0);
		Date timeStart = calendar.getTime();
		String start = sdf.format(timeStart);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limitNum", 10);
		params.put("timeStart", timeStart);
		params.put("timeEnd", timeEnd);
		
		List<HotStatisticsEntity> entities = hotStatisticsEntityMapper.selectHotByTime(params);
		if(entities.size() != 0){
			tops = new HashMap<String, List<HotStatisticsEntity>>();
			String topKey = start+"-"+end;
			tops.put(topKey, entities);
		}
		return tops;
	}
	
	public MovieResourceEntity getAroundByMovieId(String movieId){
		return movieResourceEntityMapper.selectByMovieId(movieId);
	}
	
	public MovieResourceEntity getAroundByPureNameAndLimitReleaseTime(String pureName, String yearStr) throws ParseException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pureName", pureName);
		params.put("year", DateUtil.str2Date(yearStr));
		return movieResourceEntityMapper.selectByPureNameAndLimitReleaseTime(params);
	}
	
	public MovieInfoEntity getMovieInfoByPureNameAndPrecision(String pureName, List<String> precisions) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pureName", pureName);
		if(precisions != null && precisions.size() > 0){
			params.put("precision", precisions.get(0));
		}
		return movieInfoEntityMapper.selectByPureNameAndPrecision(params);
	}
	
	public MovieInfoEntity getMovieInfoByDoubanId(String doubanId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("doubanId", doubanId);
		return movieInfoEntityMapper.selectByDoubanId(doubanId);
	}
	
	public MovieInfoEntity getMovieInfoByPureNameAndReleaseTime(String pureName, Date releaseTime){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pureName", pureName);
		params.put("releaseTime", releaseTime);
		return movieInfoEntityMapper.selectByPureNameAndReleaseTime(params);
	}
	
	public MovieInfoEntity getMovieInfoByPureName(String pureName){
		return movieInfoEntityMapper.selectByPureName(pureName);
	}
	
	@Transactional
	public void batchAdd(List<MovieInfoEntity> adMovies, List<MovieInfoEntity> upMovies, List<ResourceInfoEntity> adResources, List<ResourceInfoEntity> upResources, List<LabelMappingEntity> labels, List<LocationMappingEntity> locations){
		if(adMovies.size() != 0){
			movieInfoEntityMapper.batchInsert(adMovies);
		}
		if(upMovies.size() != 0){
			movieInfoEntityMapper.batchUpdateByMovieIdSelective(upMovies);
		}
		if(labels.size() != 0){
			labelMappingEntityMapper.batchInsert(labels);
		}
		
		if(locations.size() != 0){
			locationMappingEntityMapper.batchInsert(locations);
		}
		
		if(adResources.size() != 0){
			resourceInfoEntityMapper.batchInsert(adResources);
		}
		if(upResources.size() != 0){
			resourceInfoEntityMapper.batchUpdateByResourceIdSelective(upResources);
		}
	}
	
	@Transactional
	public void manualAddAround(MovieInfoEntity movie, List<ResourceInfoEntity> resources, List<LabelMappingEntity> labels, List<LocationMappingEntity> locations){
		movieInfoEntityMapper.insertSelective(movie);
		if(labels.size() != 0){
			labelMappingEntityMapper.batchInsert(labels);
		}
		
		if(locations.size() != 0){
			locationMappingEntityMapper.batchInsert(locations);
		}
		
		if(resources.size() != 0){
			resourceInfoEntityMapper.batchInsert(resources);
		}
	}
	
	@Transactional
	public void manualAddResource(MovieInfoEntity toUpMovie, List<ResourceInfoEntity> resources){
		if(toUpMovie != null){
			movieInfoEntityMapper.updateByMovieIdSelective(toUpMovie);
		}
		if(resources.size() != 0){
			resourceInfoEntityMapper.batchInsert(resources);
		}
	}
	
	public void updateStatus(String movieId, int movieStatus){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("movieId", movieId);
		params.put("movieStatus", movieStatus);
		movieInfoEntityMapper.signStatusByMovieId(params);
	}
	
	
	public MovieInfoEntity getMovieInfoByMovieId(String movieId){
		return movieInfoEntityMapper.selectByMovieId(movieId);
	}
	
	@Transactional
	public void modifyCountClick(String movieId){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.error("",e);
		}
		movieInfoEntityMapper.updateCountClick(movieId);
		HotStatisticsEntity hot = getHot(movieId, TriggerEnum.click.getCode());
		hotStatisticsEntityMapper.insertSelective(hot);
	}
	
	@Transactional
	public void modifyCountDownload(String movieId){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.error("",e);
		}
		movieInfoEntityMapper.updateCountDownload(movieId);
		HotStatisticsEntity hot = getHot(movieId, TriggerEnum.download.getCode());
		hotStatisticsEntityMapper.insertSelective(hot);
	}
	
	private HotStatisticsEntity getHot(String movieId, Integer trigger){
		MovieInfoEntity movie = movieInfoEntityMapper.selectByMovieId(movieId);
		HotStatisticsEntity hot = new HotStatisticsEntity();
		hot.setHotId(StringUtil.getId(CommonConstants.hot_s));
		hot.setDoubanScore(movie.getDoubanScore());
		hot.setImdbScore(movie.getImdbScore());
		hot.setMovieId(movieId);
		hot.setPureName(movie.getPureName());
		hot.setReleaseTime(movie.getReleaseTime());
		hot.setTriggerType(trigger);
		hot.setCreateTime(new Date());
		return hot;
	}
	
	public RequestResult<MovieInfoEntity> getMovieList(int pageNum, int pageSize, String keyword){
		int startIndex = RequestResult.countOffset(pageNum, pageSize);
        int limitNum = pageSize;
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(keyword)){
        	params.put("keyword", keyword);
        }
        params.put("startIndex", startIndex);
        params.put("limitNum", limitNum);
        
        List<MovieInfoEntity> list = movieInfoEntityMapper.selectListByCondition(params);
        MovieInfoEntity.parse(list);
        int count = movieInfoEntityMapper.selectListByConditionCount(params);
        return new RequestResult<>(list, count, pageNum, pageSize);
	}
	
	public RequestResult<MovieResourceEntity> getAroundList(int pageNum, int pageSize, String label, String keyword){
		int startIndex = RequestResult.countOffset(pageNum, pageSize);
        int limitNum = pageSize;
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(keyword)){
        	params.put("keyword", keyword);
        }
        params.put("startIndex", startIndex);
        params.put("limitNum", limitNum);
        List<MovieResourceEntity> list = null;
        int count = 0;
		if(StringUtils.isBlank(label)){
			list = movieResourceEntityMapper.selectListByCondition(params);
			count = movieResourceEntityMapper.selectListByConditionCount(params);
		}else {
			params.put("label", label);
			list = movieResourceEntityMapper.selectListByConditionWithLabel(params);
			count = movieResourceEntityMapper.selectListByConditionWithLabelCount(params);
		}
		MovieResourceEntity.parse(list);
		return new RequestResult<>(list, count, pageNum, pageSize);
	}
}
