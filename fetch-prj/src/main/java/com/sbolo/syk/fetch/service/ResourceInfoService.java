package com.sbolo.syk.fetch.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.BucketUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.UIDGen;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.fetch.entity.MovieFetchRecordEntity;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.mapper.MovieFetchRecordMapper;
import com.sbolo.syk.fetch.mapper.MovieInfoMapper;
import com.sbolo.syk.fetch.mapper.ResourceInfoMapper;
import com.sbolo.syk.fetch.tool.FetchUtils;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

@Service
public class ResourceInfoService {
	private static final Logger log = LoggerFactory.getLogger(ResourceInfoService.class);
	@Resource
	private ResourceInfoMapper resourceInfoMapper;
	
	@Resource
	private MovieInfoMapper movieInfoMapper;
	
	@Resource
	private MovieInfoService movieInfoService;
	
	@Autowired
	private MovieFetchRecordMapper movieFetchRecordMapper;
	
	public List<ResourceInfoEntity> getOptimalResources(List<String> moviePrns) {
		List<ResourceInfoEntity> dbOptimalResources = resourceInfoMapper.selectOptimalResources(moviePrns);
		return dbOptimalResources;
	}
	
	public void updateStatusByMoviePrn(String moviePrn, int resourceStatus){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("moviePrn", moviePrn);
		params.put("resourceStatus", resourceStatus);
		resourceInfoMapper.signStatusByMoviePrn(params);
	}
	
	public void updateStatus(String resourcePrn, int resourceStatus){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourcePrn", resourcePrn);
		params.put("resourceStatus", resourceStatus);
		resourceInfoMapper.signStatusByPrn(params);
	}
	
	public List<ResourceInfoVO> getListByMoviePrnOrderNoStatus(String moviePrn, Integer category) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("moviePrn", moviePrn);
		Boolean istv = false;
		if(category == MovieCategoryEnum.tv.getCode()){
			istv = true;
		}
		params.put("istv", istv);
		
		List<ResourceInfoEntity> resourceList = resourceInfoMapper.selectByMoviePrnOrderNostatus(params);
		List<ResourceInfoVO> resourceVOList = VOUtils.po2vo(resourceList, ResourceInfoVO.class);
		return resourceVOList;
	}
	
	@Transactional
	public void signDeleteable(String moviePrn, String resourcePrn){
		this.updateStatus(resourcePrn, MovieStatusEnum.deletable.getCode());
		Map<String, Object> params = new HashMap<>();
		params.put("moviePrn", moviePrn);
		params.put("resourceStatus", MovieStatusEnum.available.getCode());
		List<ResourceInfoEntity> resources = resourceInfoMapper.selectAllByMoviePrnAndStatus(params);
		if(resources == null || resources.size() == 0){
			Map<String, Object> params2 = new HashMap<>();
			params2.put("moviePrn", moviePrn);
			params2.put("movieStatus", MovieStatusEnum.deletable.getCode());
			movieInfoMapper.signStatusByPrn(params2);
		}
	}
	
	@Transactional
	public void signAvailable(String moviePrn, String resourcePrn){
		this.updateStatus(resourcePrn, MovieStatusEnum.available.getCode());
		MovieInfoEntity movie = movieInfoMapper.selectByPrn(moviePrn);
		if(movie.getSt() == MovieStatusEnum.deletable.getCode()){
			Map<String, Object> params2 = new HashMap<>();
			params2.put("moviePrn", moviePrn);
			params2.put("movieStatus", MovieStatusEnum.available.getCode());
			movieInfoMapper.signStatusByPrn(params2);
		}
	}
	
	public MovieInfoVO setOptimalAndGet(MovieInfoEntity movieAround, List<ResourceInfoVO> resources) throws Exception {
		Integer optimalIdx = 0;
		Integer maxDefinition = -1;
		for(int i=0; i<resources.size(); i++){
			ResourceInfoVO resource = resources.get(i);
			int definitionScore = resource.getDefinition();
			if(definitionScore > maxDefinition){
				maxDefinition = definitionScore;
				optimalIdx = i;
			}
		}
		//设置最佳资源信息
		ResourceInfoEntity dbOptimalResource = movieAround.getOptimalResource();
		ResourceInfoVO newOptimalResourceVO = resources.get(optimalIdx);
		MovieInfoVO toUpMovie = movieInfoService.freshOptimalResource(movieAround.getCategory(), newOptimalResourceVO, dbOptimalResource);
		return toUpMovie;
	}
	
	public void addResourcesProcess(MovieInfoEntity movieAround, List<ResourceInfoVO> resources) throws Exception {
		String moviePrn = movieAround.getPrn();
		Date thisTime = new Date();
		Integer maxEpisodeStart = -1;
		Integer maxEpisodeEnd = -1;
		List<ResourceInfoVO> changeResources = new ArrayList<>();
		for(int i=0; i<resources.size(); i++){
			ResourceInfoVO resource = resources.get(i);
			String resourcePrn = UIDGen.getUID(CommonConstants.resource_s);
			resource.setPrn(resourcePrn);
			resource.setPureName(movieAround.getPureName());
			resource.setReleaseTime(movieAround.getReleaseTime());
			resource.setSpeed(5);
			resource.setMoviePrn(moviePrn);
			resource.setCreateTime(thisTime);
			resource.setSt(MovieStatusEnum.available.getCode());
			
			if(movieAround.getCategory() == MovieCategoryEnum.tv.getCode()){
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
			
			//上传shots图片
			String shotSubDirStr = resource.getShotSubDirStr();
			if(StringUtils.isNotBlank(shotSubDirStr)) {
				String shotUriJson = FetchUtils.uploadShotAndGetUriJsonFromDirs(shotSubDirStr);
				resource.setShotUriJson(shotUriJson);
			}
			
			//资源清晰度得分
			Integer definitionScore = FetchUtils.translateDefinitionIntoScore(resource.getQuality(), resource.getResolution());
			resource.setDefinition(definitionScore);
			changeResources.add(resource);
		}
	}
	
	@Transactional
	public void manualAddResources(MovieInfoVO toUpMovie, List<ResourceInfoVO> resources) throws Exception{
		MovieInfoEntity movieEntity = VOUtils.po2vo(toUpMovie, MovieInfoEntity.class);
		List<ResourceInfoEntity> resourceEntities = null;
		if(resources.size() != 0){
			resourceEntities = VOUtils.po2vo(resources, ResourceInfoEntity.class);
		}
		List<MovieInfoEntity> updateMovies = new ArrayList<>();
		updateMovies.add(movieEntity);
		List<MovieFetchRecordEntity> recordList = FetchUtils.buildFetchRecordList(null, updateMovies, resourceEntities, null, null);
		
		if(movieEntity != null){
			movieInfoMapper.updateByPrn(movieEntity);
		}
		if(resourceEntities != null && resourceEntities.size() > 0){
			resourceInfoMapper.insertList(resourceEntities);
		}
		if(recordList != null && recordList.size() > 0) {
			movieFetchRecordMapper.insertList(recordList);
		}
	}
	
	public ResourceInfoVO getResourceByPrn(String resourcePrn) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		ResourceInfoEntity resource = resourceInfoMapper.selectByPrn(resourcePrn);
		ResourceInfoVO resourceVO = VOUtils.po2vo(resource, ResourceInfoVO.class);
		resourceVO.parse();
		return resourceVO;
	}
	
	public ResourceInfoVO modiResourceProcess(ResourceInfoVO newResource, ResourceInfoEntity dbResource, Date thisTime) throws Exception {
		ResourceInfoVO changeResource = FetchUtils.resourceChangeOption(dbResource, newResource);
		
		if(changeResource == null) {
			return null;
		}
		String downloadLinkTemp = changeResource.getDownloadLinkTemp();
		if(StringUtils.isNotBlank(downloadLinkTemp)){
			String downloadLink = null;
			if(Pattern.compile(RegexConstant.torrent).matcher(downloadLinkTemp).find()){
				//上传torrent文件
				String torrentName = FetchUtils.getTorrentName(newResource);
				downloadLink = FetchUtils.uploadTorrentGetUriFromDir(downloadLinkTemp, torrentName);
				BucketUtils.delete(dbResource.getDownloadLink());
			}else {
				downloadLink = downloadLinkTemp;
			}
			changeResource.setDownloadLink(downloadLink);
		}
		
		//上传shots图片
		if(StringUtils.isNotBlank(changeResource.getShotSubDirStr())) {
			String uriJson = FetchUtils.compareUploadGetJsonAndDelOld(dbResource.getShotUriJson(), changeResource.getShotSubDirStr(), CommonConstants.shot_v);
			changeResource.setShotUriJson(uriJson);
		}
		if(StringUtils.isNotBlank(changeResource.getQuality()) && StringUtils.isNotBlank(changeResource.getResolution())) {
			int definitionScore = FetchUtils.translateDefinitionIntoScore(changeResource.getQuality(), changeResource.getResolution());
			changeResource.setDefinition(definitionScore);
		}
		
		changeResource.setPrn(dbResource.getPrn());
		changeResource.setUpdateTime(new Date());
		return changeResource;
	}
	
	public MovieInfoVO getToUpMovie(boolean isOptimal, ResourceInfoVO changeResource, String moviePrn, Date thisTime) {
		if(!isOptimal){
			return null;
		}
		MovieInfoVO toUpMovie = new MovieInfoVO();
		toUpMovie.setOptimalResourcePrn(changeResource.getPrn());
		toUpMovie.setResourceWriteTime(thisTime);
		toUpMovie.setPrn(moviePrn);
		toUpMovie.setUpdateTime(thisTime);
		return toUpMovie;
	}
	
	@Transactional
	public void modiResource(ResourceInfoVO newResource, MovieInfoVO toUpMovie) throws Exception{
		List<MovieInfoEntity> updateMovies = new ArrayList<>();
		if(toUpMovie != null) {
			MovieInfoEntity movieEntity = VOUtils.po2vo(toUpMovie, MovieInfoEntity.class);
			updateMovies.add(movieEntity);
			movieInfoMapper.updateByPrn(movieEntity);
		}
		List<ResourceInfoEntity> updateResourceInfos = new ArrayList<>();
		if(newResource != null) {
			ResourceInfoEntity modiResourceEntity = VOUtils.po2vo(newResource, ResourceInfoEntity.class);
			updateResourceInfos.add(modiResourceEntity);
			resourceInfoMapper.updateByPrn(modiResourceEntity);
		}
		List<MovieFetchRecordEntity> recordList = FetchUtils.buildFetchRecordList(null, updateMovies, null, updateResourceInfos, null);
		
		if(recordList != null && recordList.size() > 0) {
			movieFetchRecordMapper.insertList(recordList);
		}
		
	}
	
}
