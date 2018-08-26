package com.sbolo.syk.admin.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import okhttp3.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.taskdefs.Input;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.constants.TriggerEnum;
import com.sbolo.syk.common.exception.MovieInfoFetchException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.callback.HttpSendCallbackPure;
import com.sbolo.syk.common.tools.BucketUtils;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.admin.entity.MovieHotStatEntity;
import com.sbolo.syk.admin.entity.MovieInfoEntity;
import com.sbolo.syk.admin.entity.MovieLabelEntity;
import com.sbolo.syk.admin.entity.MovieLocationEntity;
import com.sbolo.syk.admin.entity.ResourceInfoEntity;
import com.sbolo.syk.admin.mapper.MovieHotStatMapper;
import com.sbolo.syk.admin.mapper.MovieInfoMapper;
import com.sbolo.syk.admin.po.HotStatisticsEntity;
import com.sbolo.syk.admin.vo.MovieInfoVO;
import com.sbolo.syk.admin.vo.ResourceInfoVO;

@Service
public class MovieInfoService {
	
	private static final Logger log = LoggerFactory.getLogger(MovieInfoService.class);
	
	@Resource
	private MovieInfoMapper movieInfoMapper;
	
	@Resource
	private MovieHotStatMapper movieHotStatMapper;
	
	@Resource
	private ResourceInfoService resourceInfoService;
	
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
	
	@Transactional
	public void signDeleteable(String moviePrn){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prn", moviePrn);
		params.put("movieStatus", MovieStatusEnum.deletable.getCode());
		movieInfoMapper.signStatusByPrn(params);
		
		resourceInfoService.updateStatusByMovieId(moviePrn, MovieStatusEnum.deletable.getCode());
	}
	
	@Transactional
	public void signAvailable(String moviePrn){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prn", moviePrn);
		params.put("movieStatus", MovieStatusEnum.available.getCode());
		movieInfoMapper.signStatusByPrn(params);
		
		resourceInfoService.updateStatusByMovieId(moviePrn, MovieStatusEnum.available.getCode());
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
	
	public void manualAddAround(MovieInfoVO movie, List<ResourceInfoVO> resources, String oldRootPath) {
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
		String iconUri = SykUtils.uploadIcon4Uri(movie.getIconTempUri());
		
		//上传poster图片
		String posterTempUriStr = movie.getPosterTempUriStr();
		String[] posterTempUriArr = posterTempUriStr.split(",");
		String posterUriStr = SykUtils.uploadPoster4Uri(Arrays.asList(posterTempUriArr));
		movie.setPosterUriJson(posterUriStr);
		
		//上传photo图片
		String photoTempUriStr = movie.getPhotoTempUriStr();
		String[] photoTempUriArr = photoTempUriStr.split(",");
		String photoUriStr = SykUtils.uploadPhoto4Uri(Arrays.asList(photoTempUriArr));
		movie.setPhotoUriJson(photoUriStr);
		
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
			if(StringUtils.isNotBlank(downloadLinkTemp)){
				String reloadLink = null;
				if(Pattern.compile(RegexConstant.torrent).matcher(downloadLinkTemp).find()){
					String torrentTempDir = ConfigUtil.getPropertyValue("torrent.temp.dir");
					String suffix = downloadLinkTemp.substring(downloadLinkTemp.lastIndexOf(".")+1);
					String newName = resourceService.buildTorrentName(resource)+"."+suffix;
					try {
						String reloadBt = Utils.copyFile(oldRootPath, tempLinkStr, newRootPath, newName);
						reloadLink = reloadBt;
					} catch (IOException e) {
						log.error("",e);
					}
				}else {
					reloadLink = tempLinkStr;
				}
				if(StringUtils.isNotBlank(reloadLink)){
					resource.setDownloadLink(reloadLink);
				}
			}
			
			//上传shot图片
			String shotTempUriStr = resource.getShotTempUriStr();
			String[] shotTempUriArr = shotTempUriStr.split(",");
			String shotUriStr = SykUtils.uploadPoster4Uri(Arrays.asList(shotTempUriArr));
			resource.setShotUriJson(shotUriStr);
			
			//资源清晰度得分
			Integer definitionScore = Utils.translateDefinitionIntoScore(resource.getQuality(), resource.getResolution());
			resource.setDefinition(definitionScore);
			if(definitionScore > maxDefinition){
				maxDefinition = definitionScore;
				optimalIdx = i;
			}
		}
		
		//设置最佳资源信息
		ResourceInfoEntity optimalResource = resources.get(optimalIdx);
		
		movie.setOptimalResourceId(optimalResource.getResourceId());
		movie.setResourceWriteTime(optimalResource.getCreateTime());
		
		movieInfoBizService.manualAddAround(movie, resources, labelList, locationList);
	}
}
