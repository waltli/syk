package com.sbolo.syk.fetch.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
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
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.tools.BucketUtils;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.MovieLabelEntity;
import com.sbolo.syk.fetch.entity.MovieLocationEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.mapper.MovieLabelMapper;
import com.sbolo.syk.fetch.mapper.MovieLocationMapper;
import com.sbolo.syk.fetch.mapper.ResourceInfoMapper;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.MovieLabelVO;
import com.sbolo.syk.fetch.vo.MovieLocationVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

@Service
public class MovieInfoService {
	
	private static final Logger log = LoggerFactory.getLogger(MovieInfoService.class);
	
	@Resource
	private MovieInfoMapper movieInfoMapper;
	
	@Resource
	private MovieLabelMapper movieLabelMapper;
	
	@Resource MovieLocationMapper movieLocationMapper;
	
	@Resource
	private ResourceInfoService resourceInfoService;
	
	@Resource
	private ResourceInfoMapper resourceInfoMapper;
	
	public MovieInfoEntity getOneByPureNameAndYear(String pureName, Date year) {
		MovieInfoEntity movie = movieInfoMapper.selectOneByPureNameAndYear(pureName, year);
		return movie;
	}
	
	public RequestResult<MovieInfoVO> getAroundList(int pageNum, int pageSize, String label, String keyword) throws InstantiationException, IllegalAccessException, InvocationTargetException{
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
	
	@Transactional
	public void signDeleteable(String moviePrn){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("moviePrn", moviePrn);
		params.put("movieStatus", MovieStatusEnum.deletable.getCode());
		movieInfoMapper.signStatusByPrn(params);
		
		resourceInfoService.updateStatusByMoviePrn(moviePrn, MovieStatusEnum.deletable.getCode());
	}
	
	@Transactional
	public void signAvailable(String moviePrn){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("moviePrn", moviePrn);
		params.put("movieStatus", MovieStatusEnum.available.getCode());
		movieInfoMapper.signStatusByPrn(params);
		
		resourceInfoService.updateStatusByMoviePrn(moviePrn, MovieStatusEnum.available.getCode());
	}
	
	public MovieInfoEntity getMovieInfoByDoubanId(String doubanId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("doubanId", doubanId);
		return movieInfoMapper.selectByDoubanId(doubanId);
	}
	
	public MovieInfoEntity getMovieInfoByPureNameAndReleaseTime(String pureName, Date releaseTime){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pureName", pureName);
		params.put("releaseTime", releaseTime);
		return movieInfoMapper.selectByPureNameAndReleaseTime(params);
	}
	
	@Transactional
	public void manualAddAround(MovieInfoVO movie, List<ResourceInfoVO> resources) throws Exception {
		String moviePrn = StringUtil.getId(CommonConstants.movie_s);
		Date now = new Date();
		movie.setPrn(moviePrn);
		movie.setCreateTime(now);
		movie.setResourceWriteTime(now);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String releaseTimeStr = movie.getReleaseTimeStr();
		Date releaseTime = sdf.parse(releaseTimeStr);
		movie.setReleaseTime(releaseTime);
		movie.setReleaseTimeFormat(CommonConstants.getTimeFormat(releaseTimeStr));
		movie.setCountClick(0);
		movie.setCountComment(0);
		movie.setCountDownload(0);
		movie.setSt(MovieStatusEnum.available.getCode());
		
		List<MovieLabelEntity> labelList = new ArrayList<MovieLabelEntity>();
		String labels = movie.getLabels();
		String[] labelsSplit = labels.split(RegexConstant.slashSep);
		for(int i=0; i<labelsSplit.length; i++){
			String label = labelsSplit[i];
			MovieLabelEntity labelEntity = new MovieLabelEntity();
			labelEntity.setLabelName(label);
			String labelPrn = StringUtil.getId(CommonConstants.label_s);
			labelEntity.setPrn(labelPrn);
			labelEntity.setMoviePrn(moviePrn);
			labelEntity.setPureName(movie.getPureName());
			labelEntity.setReleaseTime(movie.getReleaseTime());
			labelEntity.setCreateTime(now);
			labelList.add(labelEntity);
		}
		
		
		List<MovieLocationEntity> locationList = new ArrayList<MovieLocationEntity>();
		String locations = movie.getLocations();
		String[] locationsSplit = locations.split(RegexConstant.slashSep);
		for(int i=0; i<locationsSplit.length; i++){
			String location = locationsSplit[i];
			//地区只要中文，如果实在没有也没有办法！
			Matcher m = Pattern.compile(RegexConstant.chinese).matcher(location);
			if(!m.find()){
				continue;
			}
			location = m.group();
			MovieLocationEntity locationEntity = new MovieLocationEntity();
			locationEntity.setLocationName(location);
			String locationPrn = StringUtil.getId(CommonConstants.location_s);
			locationEntity.setPrn(locationPrn);
			locationEntity.setMoviePrn(moviePrn);
			locationEntity.setPureName(movie.getPureName());
			locationEntity.setReleaseTime(movie.getReleaseTime());
			locationEntity.setCreateTime(now);
			locationList.add(locationEntity);
		}
		
		//上传ICON图片
		if(StringUtils.isNotBlank(movie.getIconSubDir())) {
			String iconUri = FetchUtils.uploadIconGetUriFromDir(movie.getIconSubDir());
			movie.setIconUri(iconUri);
		}
		
		//上传poster图片
		if(StringUtils.isNotBlank(movie.getPosterSubDirStr())) {
			String posterUriJson = FetchUtils.uploadPosterAndGetUriJsonFromDirs(movie.getPosterSubDirStr());
			movie.setPosterUriJson(posterUriJson);
		}
		
		//上传photo图片
		if(StringUtils.isNotBlank(movie.getPhotoSubDirStr())) {
			String photoUriJson = FetchUtils.uploadPhotoAndGetUriJsonFromDirs(movie.getPhotoSubDirStr());
			movie.setPhotoUriJson(photoUriJson);
		}
		
		Integer optimalIdx = 0;
		Integer maxDefinition = -1;
		Integer maxEpisodeStart = -1;
		Integer maxEpisodeEnd = -1;
		for(int i=0; i<resources.size(); i++){
			ResourceInfoVO resource = resources.get(i);
			String resourcePrn = StringUtil.getId(CommonConstants.resource_s);
			resource.setPrn(resourcePrn);
			resource.setMoviePrn(moviePrn);
			resource.setPureName(movie.getPureName());
			resource.setSt(MovieStatusEnum.available.getCode());
			resource.setReleaseTime(releaseTime);
			resource.setSpeed(5);
			resource.setSeason(movie.getPresentSeason());
			resource.setCreateTime(now);
			
			if(movie.getCategory() == MovieCategoryEnum.tv.getCode()){
				if(resource.getEpisodeStart() != null && resource.getEpisodeStart() > maxEpisodeStart){
					maxEpisodeStart = resource.getEpisodeStart();
				}
				if(resource.getEpisodeEnd() != null && resource.getEpisodeEnd() > maxEpisodeEnd){
					maxEpisodeEnd = resource.getEpisodeEnd();
				}
			}
			
			//上传torrent文件
			String downloadLinkTemp = resource.getDownloadLinkTemp();
			String downloadLink = null;
			if(Pattern.compile(RegexConstant.torrent).matcher(downloadLinkTemp).find()) {
				String torrentName = FetchUtils.getTorrentName(resource);
				downloadLink = FetchUtils.uploadTorrentGetUriFromDir(downloadLinkTemp, torrentName);
			}else {
				downloadLink = downloadLinkTemp;
			}
			resource.setDownloadLink(downloadLink);
			
			//上传shot图片
			if(StringUtils.isNotBlank(resource.getShotSubDirStr())) {
				String shotUriJson = FetchUtils.uploadShotAndGetUriJsonFromDirs(resource.getShotSubDirStr());
				resource.setShotUriJson(shotUriJson);
			}
			
			//资源清晰度得分
			Integer definitionScore = FetchUtils.translateDefinitionIntoScore(resource.getQuality(), resource.getResolution());
			resource.setDefinition(definitionScore);
			if(definitionScore > maxDefinition){
				maxDefinition = definitionScore;
				optimalIdx = i;
			}
		}
		
		//设置最佳资源信息
		ResourceInfoVO optimalResource = resources.get(optimalIdx);
		
		movie.setOptimalResourcePrn(optimalResource.getPrn());
		movie.setResourceWriteTime(optimalResource.getCreateTime());
		
		MovieInfoEntity movieEntity = VOUtils.po2vo(movie, MovieInfoEntity.class);
		movieInfoMapper.insert(movieEntity);
		if(labelList.size() != 0){
			movieLabelMapper.insertList(labelList);
		}
		
		if(locationList.size() != 0){
			movieLocationMapper.insertList(locationList);
		}
		
		if(resources.size() != 0){
			List<ResourceInfoEntity> resourceEntities = VOUtils.po2vo(resources, ResourceInfoEntity.class);
			resourceInfoMapper.insertList(resourceEntities);
		}
	}
	
	public MovieInfoEntity getMovieInfoByPureName(String pureName){
		return movieInfoMapper.selectByPureName(pureName);
	}
	
	public MovieInfoVO getMovieInfoByPrn(String moviePrn) throws Exception{
		MovieInfoEntity movie = movieInfoMapper.selectByPrn(moviePrn);
		if(movie == null){
			throw new Exception("未查询到prn为"+moviePrn+"的影片信息！");
		}
		MovieInfoVO movieVO = VOUtils.po2vo(movie, MovieInfoVO.class);
		movieVO.parse();
		return movieVO;
	}
	
	@Transactional
	public void modiMovieInfoManual(MovieInfoVO modiMovie) throws Exception{
		MovieInfoEntity dbMovie = movieInfoMapper.selectByPrn(modiMovie.getPrn());
		if(dbMovie == null){
			throw new Exception("该影片信息不存在，修改失败！");
		}
		List<MovieLabelEntity> dbLabels = movieLabelMapper.selectListByMoviePrn(dbMovie.getPrn());
		List<MovieLocationEntity> dbLocations = movieLocationMapper.selectListByMoviePrn(dbMovie.getPrn());
		MovieInfoVO changeMovie = FetchUtils.movieChangeOption(dbMovie, modiMovie, dbLabels, dbLocations, new Date());
		
		if(changeMovie == null){
			return;
		}
		changeMovie.setPrn(dbMovie.getPrn());
		
		//上传ICON图片
		if(StringUtils.isNotBlank(changeMovie.getIconSubDir()) && !StringUtil.isHttp(changeMovie.getIconSubDir())) {
			String iconUri = FetchUtils.uploadIconGetUriFromDir(changeMovie.getIconSubDir());
			changeMovie.setIconUri(iconUri);
			BucketUtils.delete(dbMovie.getIconUri());
		}
		
		
		//上传poster图片
		if(StringUtils.isNotBlank(changeMovie.getPosterSubDirStr())) {
			String uriJson = FetchUtils.compareUploadGetJsonAndDelOld(dbMovie.getPosterUriJson(), changeMovie.getPosterSubDirStr(), CommonConstants.poster_v);
			changeMovie.setPosterUriJson(uriJson);
		}
		
		//上传photo图片
		if(StringUtils.isNotBlank(changeMovie.getPhotoSubDirStr())) {
			String uriJson = FetchUtils.compareUploadGetJsonAndDelOld(dbMovie.getPhotoUriJson(), changeMovie.getPhotoSubDirStr(), CommonConstants.photo_v);
			changeMovie.setPhotoUriJson(uriJson);
		}
		
		
		List<MovieLabelVO> labelList = changeMovie.getLabelList();
		List<MovieLocationVO> locationList = changeMovie.getLocationList();
		
		List<MovieLabelEntity> labelEntities = VOUtils.po2vo(labelList, MovieLabelEntity.class);
		List<MovieLocationEntity> locationEntities = VOUtils.po2vo(locationList, MovieLocationEntity.class);
		MovieInfoEntity movieInfoEntity = VOUtils.po2vo(changeMovie, MovieInfoEntity.class);
		if(labelEntities != null && labelEntities.size() > 0) {
			movieLabelMapper.insertList(labelEntities);
		}
		if(locationEntities != null && locationEntities.size() > 0) {
			movieLocationMapper.insertList(locationEntities);
		}
		if(movieInfoEntity != null) {
			movieInfoMapper.updateByPrn(movieInfoEntity);
		}
	}
	
	public void updateByPrn(MovieInfoEntity entity) {
		movieInfoMapper.updateByPrn(entity);
	}
	
	public MovieInfoEntity getMovieInfoByPureNameAndPrecision(String pureName, List<String> precisions) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pureName", pureName);
		if(precisions != null && precisions.size() > 0){
			params.put("precision", precisions.get(0));
		}
		return movieInfoMapper.selectByPureNameAndPrecision(params);
	}
	
	public MovieInfoEntity freshOptimalResource(int category, ResourceInfoVO newOptimalResourceVO, ResourceInfoEntity nowOptimalResourceVO) throws Exception{
		if(category == MovieCategoryEnum.movie.getCode()){
			if(newOptimalResourceVO.getDefinition() == null){
				return null;
			}
			int newDefinition = newOptimalResourceVO.getDefinition().intValue();
			int oldDefinition = nowOptimalResourceVO.getDefinition().intValue();
			
			if(newDefinition <= oldDefinition){
				return null;
			}
			
			ResourceInfoEntity optimalResource = getNowOptimalResource(nowOptimalResourceVO.getMoviePrn());
			
			int optimalDefinition = optimalResource.getDefinition().intValue();
			if(newDefinition <= optimalDefinition){
				return null;
			}
		}else {
			if(newOptimalResourceVO.getEpisodeEnd() == null){
				return null;
			}
			int newEpisodeEnd = newOptimalResourceVO.getEpisodeEnd().intValue();
			int oldEpisodeEnd = nowOptimalResourceVO.getEpisodeEnd().intValue();
			
			if(newEpisodeEnd <= oldEpisodeEnd){
				return null;
			}
			ResourceInfoEntity optimalResource = getNowOptimalResource(nowOptimalResourceVO.getMoviePrn());
			
			int optimalEpisodeEnd = optimalResource.getEpisodeEnd();
			
			if(newEpisodeEnd <= optimalEpisodeEnd){
				return null;
			}
		}
		
		MovieInfoEntity toUpMovie = new MovieInfoEntity();
		toUpMovie.setOptimalResourcePrn(newOptimalResourceVO.getPrn());
		toUpMovie.setPrn(nowOptimalResourceVO.getMoviePrn());
		toUpMovie.setUpdateTime(new Date());
		return toUpMovie;
		
	}
	
	private ResourceInfoEntity getNowOptimalResource(String moviePrn) throws Exception{
		MovieInfoEntity movieAround = movieInfoMapper.selectAssociationByMoviePrn(moviePrn);
		if(movieAround == null){
			throw new Exception("当前resource找不到关联的movie！");
		}
		ResourceInfoEntity optimalResource = movieAround.getOptimalResource();
		return optimalResource;
	}
	
}
