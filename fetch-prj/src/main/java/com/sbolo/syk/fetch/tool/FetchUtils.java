package com.sbolo.syk.fetch.tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.MovieDictEnum;
import com.sbolo.syk.common.constants.MovieQualityEnum;
import com.sbolo.syk.common.constants.MovieResolutionConstant;
import com.sbolo.syk.common.constants.MovieStatusEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.tools.BucketUtils;
import com.sbolo.syk.common.tools.ConfigUtils;
import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.GrapicmagickUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.fetch.entity.MovieDictEntity;
import com.sbolo.syk.fetch.entity.MovieFetchRecordEntity;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.enums.OperateTypeEnum;
import com.sbolo.syk.fetch.enums.RelyDataEnum;
import com.sbolo.syk.fetch.exception.ResourceException;
import com.sbolo.syk.fetch.vo.MovieDictVO;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

public class FetchUtils {
	private static final Logger log = LoggerFactory.getLogger(FetchUtils.class);
	
	public static List<MovieFetchRecordEntity> buildFetchRecordList(
			List<MovieInfoEntity> addMovies, List<MovieInfoEntity> updateMovies,
			List<ResourceInfoEntity> addResourceInfos, List<ResourceInfoEntity> updateResourceInfos, 
			List<MovieDictEntity> addDicts) {
		
		List<MovieFetchRecordEntity> fetchRecordList = new ArrayList<>();
		
		if(addMovies != null && addMovies.size() > 0) {
			for(MovieInfoEntity addMovie : addMovies) {
				MovieFetchRecordEntity fetchRecord = buildFetchRecord(addMovie.getPrn(), OperateTypeEnum.insert.getCode(), RelyDataEnum.moive.getCode(), null);
				fetchRecordList.add(fetchRecord);
			}
		}
		
		if(updateMovies != null && updateMovies.size() > 0) {
			for(MovieInfoEntity updateMovie : updateMovies) {
				String jsonString = JSON.toJSONString(updateMovie);
				MovieFetchRecordEntity fetchRecord = buildFetchRecord(updateMovie.getPrn(), OperateTypeEnum.update.getCode(), RelyDataEnum.moive.getCode(), jsonString);
				fetchRecordList.add(fetchRecord);
			}
		}
		
		if(addResourceInfos != null && addResourceInfos.size() > 0) {
			for(ResourceInfoEntity addResource : addResourceInfos) {
				MovieFetchRecordEntity fetchRecord = buildFetchRecord(addResource.getPrn(), OperateTypeEnum.insert.getCode(), RelyDataEnum.resource.getCode(), null);
				fetchRecordList.add(fetchRecord);
			}
		}
		
		if(updateResourceInfos != null && updateResourceInfos.size() > 0) {
			for(ResourceInfoEntity updateResource : updateResourceInfos) {
				String jsonString = JSON.toJSONString(updateResource);
				MovieFetchRecordEntity fetchRecord = buildFetchRecord(updateResource.getPrn(), OperateTypeEnum.update.getCode(), RelyDataEnum.resource.getCode(), jsonString);
				fetchRecordList.add(fetchRecord);
			}
		}
		
		if(addDicts != null && addDicts.size() > 0) {
			for(MovieDictEntity addDict : addDicts) {
				MovieFetchRecordEntity fetchRecord = buildFetchRecord(addDict.getCode(), OperateTypeEnum.insert.getCode(), RelyDataEnum.dict.getCode(), null);
				fetchRecordList.add(fetchRecord);
			}
		}
		
		
		
		return fetchRecordList;
	}
	
	private static MovieFetchRecordEntity buildFetchRecord(String dataPrn, Integer operateType, String relyData, String dataJson) {
		MovieFetchRecordEntity fetchRecordEntity = new MovieFetchRecordEntity();
		fetchRecordEntity.setCreateTime(new Date());
		fetchRecordEntity.setDataPrn(dataPrn);
		fetchRecordEntity.setDataJson(dataJson);
		fetchRecordEntity.setHasMigrated(false);
		fetchRecordEntity.setOperateType(operateType);
		fetchRecordEntity.setPrn(StringUtil.getId(null));
		fetchRecordEntity.setRelyData(relyData);
		fetchRecordEntity.setSt(MovieStatusEnum.available.getCode());
		return fetchRecordEntity;
	}
	
	public static String uploadIconGetUri(byte[] bytes, String suffix) throws Exception {
		int fixWidth = CommonConstants.icon_width;
		int fixHeight = CommonConstants.icon_height;
		byte[] imageBytes = GrapicmagickUtils.descale(bytes, fixWidth, fixHeight);
		String root = ConfigUtils.getPropertyValue("bucket.formal");
		String typeDir = FileUtils.getTypeDir("icon");
		String dateDir = FileUtils.getTypeDir("date");
		String targetDir = root + typeDir + dateDir;
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		BucketUtils.upload(imageBytes, targetDir, fileName, suffix);
		return ConfigUtils.getPropertyValue("bucket.formal") + typeDir + dateDir + "/" + fileName + "." +suffix;
	}
	
	public static String uploadIconGetUri(String iconUrl) throws Exception {
		byte[] bytes = HttpUtils.getBytes(iconUrl);
		String suffix = iconUrl.substring(iconUrl.lastIndexOf(".")+1);
		return uploadIconGetUri(bytes, suffix);
	}
	
	public static String uploadIconGetUri(File file) throws Exception {
		byte[] bytes = FileUtils.getBytes(file);
		String path = file.getPath();
		String suffix = path.substring(path.lastIndexOf(".")+1);
		return uploadIconGetUri(bytes, suffix);
	}
	
	public static String uploadIconGetUriFromDir(String iconSubDir) throws Exception {
		String root = ConfigUtils.getPropertyValue("fs.temp.dir");
		File iconFile = new File(root+iconSubDir);
		return uploadIconGetUri(iconFile);
	}
	
	public static String uploadPosterGetUri(byte[] bytes, String suffix) throws Exception {
		int fixWidth = CommonConstants.poster_width;
		int fixHeight = CommonConstants.poster_height;
		byte[] imageBytes = GrapicmagickUtils.descale(bytes, fixWidth, fixHeight);
		String root = ConfigUtils.getPropertyValue("bucket.formal");
		String typeDir = FileUtils.getTypeDir("poster");
		String dateDir = FileUtils.getTypeDir("date");
		String targetDir = root + typeDir + dateDir;
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		BucketUtils.upload(imageBytes, targetDir, fileName, suffix);
		return ConfigUtils.getPropertyValue("bucket.formal") + typeDir + dateDir + "/" + fileName + "." +suffix;
	}
	
	public static String uploadPosterGetUri(String posterUrl) throws Exception {
		byte[] bytes = HttpUtils.getBytes(posterUrl);
		String suffix = posterUrl.substring(posterUrl.lastIndexOf(".")+1);
		return uploadPosterGetUri(bytes, suffix);
	}
	
	public static String uploadPosterGetUri(File file) throws Exception {
		byte[] bytes = FileUtils.getBytes(file);
		String path = file.getPath();
		String suffix = path.substring(path.lastIndexOf(".")+1);
		return uploadPosterGetUri(bytes, suffix);
	}
	
	public static String uploadPosterGetUriFromDir(String posterSubDir) throws Exception {
		String root = ConfigUtils.getPropertyValue("fs.temp.dir");
		File posterFile = new File(root+posterSubDir);
		return uploadPosterGetUri(posterFile);
	}
	
	public static String uploadPosterAndGetUriJsonFromDirs(String posterSubDirStr) throws Exception {
		String[] posterSubDirArr = posterSubDirStr.split(",");
		List<String> posterUriList = new ArrayList<>();
		for(String posterSubDir : posterSubDirArr) {
			String posterUri = uploadPosterGetUriFromDir(posterSubDir);
			posterUriList.add(posterUri);
		}
		if(posterUriList.size() > 0) {
			String posterUriJson = JSON.toJSONString(posterUriList);
			return posterUriJson;
		}
		return null;
	}
	
	public static String uploadPhotoGetUri(byte[] bytes, String suffix) throws Exception {
		int fixWidth = CommonConstants.photo_width;
		int fixHeight = CommonConstants.photo_height;
		byte[] imageBytes = GrapicmagickUtils.descale(bytes, fixWidth, fixHeight);
		String root = ConfigUtils.getPropertyValue("bucket.formal");
		String typeDir = FileUtils.getTypeDir("photo");
		String dateDir = FileUtils.getTypeDir("date");
		String targetDir = root + typeDir + dateDir;
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		BucketUtils.upload(imageBytes, targetDir, fileName, suffix);
		return ConfigUtils.getPropertyValue("bucket.formal") + typeDir + dateDir + "/" + fileName + "." +suffix;
	}
	
	public static String uploadPhotoGetUri(String photoUrl) throws Exception {
		byte[] bytes = HttpUtils.getBytes(photoUrl);
		String suffix = photoUrl.substring(photoUrl.lastIndexOf(".")+1);
		return uploadPhotoGetUri(bytes, suffix);
	}
	
	public static String uploadPhotoGetUri(File file) throws Exception {
		byte[] bytes = FileUtils.getBytes(file);
		String path = file.getPath();
		String suffix = path.substring(path.lastIndexOf(".")+1);
		return uploadPhotoGetUri(bytes, suffix);
	}
	
	public static String uploadPhotoGetUriFromDir(String photoSubDir) throws Exception {
		String root = ConfigUtils.getPropertyValue("fs.temp.dir");
		File photoFile = new File(root+photoSubDir);
		return uploadPhotoGetUri(photoFile);
	}
	
	public static String uploadPhotoAndGetUriJsonFromDirs(String photoSubDirStr) throws Exception {
		String[] photoSubDirArr = photoSubDirStr.split(",");
		List<String> photoUriList = new ArrayList<>();
		for(String photoSubDir : photoSubDirArr) {
			String photoUri = uploadPhotoGetUriFromDir(photoSubDir);
			photoUriList.add(photoUri);
		}
		if(photoUriList.size() > 0) {
			String photoUriJson = JSON.toJSONString(photoUriList);
			return photoUriJson;
		}
		return null;
	}
	
	public static String uploadShotGetUri(byte[] bytes, String suffix) throws Exception {
		InputStream markStream = null;
		try {
			int fixWidth = CommonConstants.shot_width;
			int fixHeight = CommonConstants.shot_height;
			byte[] imageBytes = GrapicmagickUtils.descale(bytes, fixWidth, fixHeight);
			markStream = FetchUtils.class.getResourceAsStream("/img/mark.png");
			if(markStream != null) {
				byte[] markBytes = IOUtils.toByteArray(markStream);
				imageBytes = GrapicmagickUtils.watermark(imageBytes, markBytes);
			}
			String root = ConfigUtils.getPropertyValue("bucket.formal");
			String typeDir = FileUtils.getTypeDir("shot");
			String dateDir = FileUtils.getTypeDir("date");
			String targetDir = root + typeDir + dateDir;
			String fileName = StringUtil.getId(CommonConstants.pic_s);
			BucketUtils.upload(imageBytes, targetDir, fileName, suffix);
			return ConfigUtils.getPropertyValue("bucket.formal") + typeDir + dateDir + "/" + fileName + "." +suffix;
		} finally {
			if(markStream != null) {
				markStream.close();
				markStream = null;
			}
		}
		
	}
	
	public static String uploadShotGetUri(String shotUrl) throws Exception {
		byte[] bytes = HttpUtils.getBytes(shotUrl);
		String suffix = shotUrl.substring(shotUrl.lastIndexOf(".")+1);
		return uploadShotGetUri(bytes, suffix);
	}
	
	public static String uploadShotGetUri(File file) throws Exception {
		byte[] bytes = FileUtils.getBytes(file);
		String path = file.getPath();
		String suffix = path.substring(path.lastIndexOf(".")+1);
		return uploadShotGetUri(bytes, suffix);
	}
	
	public static String uploadShotGetUriFromDir(String shotSubDir) throws Exception {
		String root = ConfigUtils.getPropertyValue("fs.temp.dir");
		File shotFile = new File(root+shotSubDir);
		return uploadShotGetUri(shotFile);
	}
	
	public static String uploadShotAndGetUriJsonFromDirs(String shotSubDirStr) throws Exception {
		String[] shotSubDirArr = shotSubDirStr.split(",");
		List<String> shotUriList = new ArrayList<>();
		for(String shotSubDir : shotSubDirArr) {
			String shotUri = uploadShotGetUriFromDir(shotSubDir);
			shotUriList.add(shotUri);
		}
		if(shotUriList.size() > 0) {
			String shotUriJson = JSON.toJSONString(shotUriList);
			return shotUriJson;
		}
		return null;
	}
	
	public static String uploadTorrentGetUri(byte[] bytes, String torrentName, String suffix) throws Exception {
		String root = ConfigUtils.getPropertyValue("bucket.formal");
		String typeDir = FileUtils.getTypeDir("torrent");
		String dateDir = FileUtils.getTypeDir("date");
		String targetDir = root + typeDir + dateDir;
		BucketUtils.upload(bytes, targetDir, torrentName, suffix);
		return ConfigUtils.getPropertyValue("bucket.formal") + typeDir + dateDir + "/" + torrentName + "." +suffix;
	}
	
	public static String uploadTorrentGetUri(String torrentUrl, String torrentName) throws Exception {
		byte[] bytes = HttpUtils.getBytes(torrentUrl);
		String suffix = torrentUrl.substring(torrentUrl.lastIndexOf(".")+1);
		return uploadTorrentGetUri(bytes, torrentName, suffix);
	}
	
	public static String uploadTorrentGetUri(File file, String torrentName) throws Exception {
		byte[] bytes = FileUtils.getBytes(file);
		String path = file.getPath();
		String suffix = path.substring(path.lastIndexOf(".")+1);
		return uploadTorrentGetUri(bytes, torrentName, suffix);
	}
	
	public static String uploadTorrentGetUriFromDir(String torrentSubDir, String torrentName) throws Exception {
		String root = ConfigUtils.getPropertyValue("fs.temp.dir");
		File torrentFile = new File(root+torrentSubDir);
		return uploadTorrentGetUri(torrentFile, torrentName);
	}
	
	/**
	 * 重新组建种子文件名
	 * @param fetchResource
	 * @return
	 */
	public static String getTorrentName(ResourceInfoVO fetchResource) {
		StringBuffer fileName = new StringBuffer(fetchResource.getPureName().replaceAll(" ", "."));
		if(fetchResource.getEpisodeStart() != null){
			fileName.append(".第").append(fetchResource.getEpisodeStart())
			.append("-").append(fetchResource.getEpisodeEnd()).append("集");
		}else if(fetchResource.getEpisodeEnd() != null){
			fileName.append(".第").append(fetchResource.getEpisodeEnd()).append("集");
		}
		if(StringUtils.isNotBlank(fetchResource.getQuality())){
			fileName.append(".").append(fetchResource.getQuality());
		}
		if(StringUtils.isNotBlank(fetchResource.getResolution())){
			fileName.append(".").append(fetchResource.getResolution());
		}
		String subtitleNotice = "";
		if(StringUtils.isNotBlank(fetchResource.getSubtitle())){
			subtitleNotice = fetchResource.getSubtitle();
		}
		fileName.append(".").append(subtitleNotice).append(CommonConstants.local_sign);
		return fileName.toString();
	}
	
	/**
     * 计算资源resource的得分情况
     * 方便进行资源的好坏的比较
     * @param quality
     * @param resolution
     * @return
     */
    public static int translateDefinitionIntoScore(String quality, String resolution){
		
		if(StringUtils.isBlank(quality) && StringUtils.isBlank(resolution)){
			return 0;
		}
		if(StringUtils.isBlank(quality)){
			return MovieResolutionConstant.getPureResolutionScoreByKey(resolution);
		}
		
		if(StringUtils.isBlank(resolution)){
			return MovieQualityEnum.getScoreByName(quality);
		}
		
		BigDecimal rat = new BigDecimal(0.69);
		BigDecimal qualityScore = new BigDecimal(MovieQualityEnum.getScoreByName(quality));
		BigDecimal resolutionScore = new BigDecimal(MovieResolutionConstant.getResolutionScoreByKey(resolution));
		BigDecimal definition = qualityScore.add(resolutionScore).multiply(rat).setScale(0,BigDecimal.ROUND_HALF_UP);
		
		return definition.intValue();
	}
	
	/**
     * 当原先resource被替换后，删除原resource留下的文件
     * @param resource
     */
    public static void deleteResourceTempFile(ResourceInfoVO resource){
    	String formalDir = ConfigUtils.getPropertyValue("fs.formal.dir");
		String shotJson = resource.getShotUriJson();
		
		if(StringUtils.isNotBlank(shotJson)){
			List<String> shotUriList = JSON.parseArray(shotJson, String.class);
			for(String shotUri:shotUriList){
				String shotPath = formalDir+"/"+shotUri;
				FileUtils.deleteFile(shotPath);
			}
		}
		
		String downloadLink = resource.getDownloadLink();
		if(StringUtil.isLocalLink(downloadLink)){
			String torrentPath = formalDir+"/"+downloadLink;
			FileUtils.deleteFile(torrentPath);
		}
	}
    
    /**
     * 删除movie相关的文件
     * 因电影图片可以不用更新，故该方法暂时未使用
     * @param resource
     */
    public static void deleteMovieTempFile(MovieInfoVO movie){
    	String formalDir = ConfigUtils.getPropertyValue("fs.formal.dir");
		String iconUri = movie.getIconUri();
		if(StringUtils.isNotBlank(iconUri)) {
			String iconPath = formalDir+"/"+iconUri;
			FileUtils.deleteFile(iconPath);
		}
		
		String posterUriJson = movie.getPosterUriJson();
		if(StringUtils.isNotBlank(posterUriJson)) {
			List<String> posterUriList = JSON.parseArray(posterUriJson, String.class);
			for(String posterUri : posterUriList) {
				String posterPath = formalDir + "/" + posterUri;
				FileUtils.deleteFile(posterPath);
			}
		}
		
		String photoUriJson = movie.getPhotoUriJson();
		if(StringUtils.isNotBlank(photoUriJson)) {
			List<String> photoUriList = JSON.parseArray(photoUriJson, String.class);
			for(String photoUri : photoUriList) {
				String photoPath = formalDir + "/" + photoUri;
				FileUtils.deleteFile(photoPath);
			}
		}
	}
    
    /**
	 * 将数据库中的电影信息，和新爬到的电影信息进行对比，看是否有更新项
	 * 如果有则赋值到新的movieInfo对象中并返回
	 * @param dbMovie
	 * @param fetchMovie
	 * @return
	 */
	public static MovieInfoVO movieChangeOption(MovieInfoEntity dbMovie, MovieInfoVO fetchMovie, Date thisTime){
    	MovieInfoVO changeOption = new MovieInfoVO();
    	boolean hasChange = false;
    	
    	if(StringUtils.isNotBlank(dbMovie.getLabels()) && 
			StringUtils.isNotBlank(fetchMovie.getLabels()) &&
			!dbMovie.getLabels().equals(fetchMovie.getLabels())) {

			changeOption.setLabels(fetchMovie.getLabels());
			hasChange = true;
    	}
    	
    	
    	if(StringUtils.isNotBlank(dbMovie.getLocations()) && 
			StringUtils.isNotBlank(fetchMovie.getLocations()) &&
			!dbMovie.getLocations().equals(fetchMovie.getLocations())) {

			changeOption.setLocations(fetchMovie.getLocations());
			hasChange = true;
    	}
    	
    	if(StringUtils.isBlank(dbMovie.getIconUri()) && StringUtils.isNotBlank(fetchMovie.getIconOutUrl())){
    		changeOption.setIconOutUrl(fetchMovie.getIconOutUrl());
    		hasChange = true;
    	}else if(StringUtils.isNotBlank(fetchMovie.getIconSubDir()) && !StringUtil.isHttp(fetchMovie.getIconSubDir())) {
    		changeOption.setIconSubDir(fetchMovie.getIconSubDir());
    		hasChange = true;
    	}
    	
    	if(StringUtils.isBlank(dbMovie.getPosterUriJson()) && CollectionUtils.isNotEmpty(fetchMovie.getPosterOutUrlList())) {
    		//到此方法时posterOutUrlList属性还未赋值，故暂无逻辑
    	}else if(StringUtils.isNotBlank(fetchMovie.getPosterSubDirStr())) {
    		String[] posterSubDirArr = fetchMovie.getPosterSubDirStr().split(",");
    		for(String str : posterSubDirArr) {
    			if(!StringUtil.isHttp(str)) {
    				changeOption.setPosterSubDirStr(fetchMovie.getPosterSubDirStr());
    				hasChange = true;
    				break;
    			}
    		}
    	}
    	
    	if(StringUtils.isBlank(dbMovie.getPhotoUriJson()) && CollectionUtils.isNotEmpty(fetchMovie.getPhotoOutUrlList())) {
    		//到此方法时photoOutUrlList属性还未赋值，故暂无逻辑
    	}else if(StringUtils.isNotBlank(fetchMovie.getPhotoSubDirStr())) {
    		String[] photoSubDirArr = fetchMovie.getPhotoSubDirStr().split(",");
    		for(String str : photoSubDirArr) {
    			if(!StringUtil.isHttp(str)) {
    				changeOption.setPhotoSubDirStr(fetchMovie.getPhotoSubDirStr());
    				hasChange = true;
    				break;
    			}
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getPureName())){
    		if(StringUtils.isBlank(dbMovie.getPureName()) || !dbMovie.getPureName().equals(fetchMovie.getPureName())){
    			changeOption.setPureName(fetchMovie.getPureName());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getAnotherName())){
    		if(StringUtils.isBlank(dbMovie.getAnotherName()) || !dbMovie.getAnotherName().equals(fetchMovie.getAnotherName())){
    			changeOption.setAnotherName(fetchMovie.getAnotherName());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getDirectors())){
    		if(StringUtils.isBlank(dbMovie.getDirectors()) || !dbMovie.getDirectors().equals(fetchMovie.getDirectors())){
    			changeOption.setDirectors(fetchMovie.getDirectors());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getCasts())){
    		if(StringUtils.isBlank(dbMovie.getCasts()) || !dbMovie.getCasts().equals(fetchMovie.getCasts())){
    			changeOption.setCasts(fetchMovie.getCasts());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getWriters())){
    		if(StringUtils.isBlank(dbMovie.getWriters()) || !dbMovie.getWriters().equals(fetchMovie.getWriters())){
    			changeOption.setWriters(fetchMovie.getWriters());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getLanguages())){
    		if(StringUtils.isBlank(dbMovie.getLanguages()) || !dbMovie.getLanguages().equals(fetchMovie.getLanguages())){
    			changeOption.setLanguages(fetchMovie.getLanguages());
    			hasChange = true;
    		}
    	}

    	if(fetchMovie.getReleaseTime() != null){
        	if(dbMovie.getReleaseTime() == null || !dbMovie.getReleaseTime().equals(fetchMovie.getReleaseTime())){
        		changeOption.setReleaseTime(fetchMovie.getReleaseTime());
        		changeOption.setReleaseTimeFormat(fetchMovie.getReleaseTimeFormat());
        		changeOption.setReleaseTimeStr(fetchMovie.getReleaseTimeStr());
    			hasChange = true;
        	}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getYear())){
    		if(StringUtils.isBlank(dbMovie.getYear()) || !dbMovie.getYear().equals(fetchMovie.getYear())){
    			changeOption.setYear(fetchMovie.getYear());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getDuration())){
    		if(StringUtils.isBlank(dbMovie.getDuration()) || !dbMovie.getDuration().equals(fetchMovie.getDuration())){
    			changeOption.setDuration(fetchMovie.getDuration());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getSummary())){
    		if(StringUtils.isBlank(dbMovie.getSummary()) || !dbMovie.getSummary().equals(fetchMovie.getSummary())){
    			changeOption.setSummary(fetchMovie.getSummary());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getDoubanId())){
    		if(StringUtils.isBlank(dbMovie.getDoubanId()) || !dbMovie.getDoubanId().equals(fetchMovie.getDoubanId())){
    			changeOption.setDoubanId(fetchMovie.getDoubanId());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getImdbId())){
    		if(StringUtils.isBlank(dbMovie.getImdbId()) || !dbMovie.getImdbId().equals(fetchMovie.getImdbId())){
    			changeOption.setImdbId(fetchMovie.getImdbId());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchMovie.getPresentSeason() != null){
    		if(dbMovie.getPresentSeason() == null || dbMovie.getPresentSeason().intValue() != fetchMovie.getPresentSeason().intValue()){
    			changeOption.setPresentSeason(fetchMovie.getPresentSeason());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchMovie.getTotalEpisode() != null){
    		if(dbMovie.getTotalEpisode() == null || dbMovie.getTotalEpisode().intValue() != fetchMovie.getTotalEpisode().intValue()){
    			changeOption.setTotalEpisode(fetchMovie.getTotalEpisode());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchMovie.getDoubanScore() != null){
    		if(dbMovie.getDoubanScore() == null || dbMovie.getDoubanScore().doubleValue() != fetchMovie.getDoubanScore().doubleValue()){
    			changeOption.setDoubanScore(fetchMovie.getDoubanScore());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchMovie.getImdbScore() != null){
    		if(dbMovie.getImdbScore() == null || dbMovie.getImdbScore().doubleValue() != fetchMovie.getImdbScore().doubleValue()){
    			changeOption.setImdbScore(fetchMovie.getImdbScore());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchMovie.getAttentionRate() != null){
    		if(dbMovie.getAttentionRate() == null || dbMovie.getAttentionRate().intValue() != fetchMovie.getAttentionRate().intValue()){
    			changeOption.setAttentionRate(fetchMovie.getAttentionRate());
    			hasChange = true;
    		}
    	}
    	
    	if(hasChange){
    		return changeOption;
    	}
    	return null;
    }
	
	public static ResourceInfoVO resourceChangeOption(ResourceInfoEntity dbResource, ResourceInfoVO fetchResource){
    	ResourceInfoVO changeOption = new ResourceInfoVO();
    	boolean hasChange = false;
    	
    	if(StringUtils.isNotBlank(fetchResource.getDownloadLinkTemp())){
    		changeOption.setDownloadLinkTemp(fetchResource.getDownloadLinkTemp());
			hasChange = true;
    	}
    	
    	if(StringUtils.isNotBlank(fetchResource.getSize())){
    		if(StringUtils.isBlank(dbResource.getSize()) || !dbResource.getSize().equals(fetchResource.getSize())){
    			changeOption.setSize(fetchResource.getSize());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchResource.getFormat())){
    		if(StringUtils.isBlank(dbResource.getFormat()) || !dbResource.getFormat().equals(fetchResource.getFormat())){
    			changeOption.setFormat(fetchResource.getFormat());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchResource.getQuality())){
    		if(StringUtils.isBlank(dbResource.getQuality()) || !dbResource.getQuality().equals(fetchResource.getQuality())){
    			changeOption.setQuality(fetchResource.getQuality());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchResource.getResolution())){
    		if(StringUtils.isBlank(dbResource.getResolution()) || !dbResource.getResolution().equals(fetchResource.getResolution())){
    			changeOption.setResolution(fetchResource.getResolution());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchResource.getEpisodeStart() != null){
    		if(dbResource.getEpisodeStart() == null || dbResource.getEpisodeStart().intValue() != fetchResource.getEpisodeStart().intValue()){
    			changeOption.setEpisodeStart(fetchResource.getEpisodeStart());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchResource.getEpisodeEnd() != null){
    		if(dbResource.getEpisodeEnd() == null || dbResource.getEpisodeEnd().intValue() != fetchResource.getEpisodeEnd().intValue()){
    			changeOption.setEpisodeEnd(fetchResource.getEpisodeEnd());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchResource.getSubtitle())){
    		if(StringUtils.isBlank(dbResource.getSubtitle()) || !dbResource.getSubtitle().equals(fetchResource.getSubtitle())){
    			changeOption.setSubtitle(fetchResource.getSubtitle());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(fetchResource.getComeFromUrl())){
    		if(StringUtils.isBlank(dbResource.getComeFromUrl()) || !dbResource.getComeFromUrl().equals(fetchResource.getComeFromUrl())){
    			changeOption.setComeFromUrl(fetchResource.getComeFromUrl());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isBlank(fetchResource.getShotUriJson()) && CollectionUtils.isNotEmpty(fetchResource.getShotOutUrlList())) {
    		//到此方法时shotOutUrlList属性还未赋值，故暂无逻辑
    	}else if(StringUtils.isNotBlank(fetchResource.getShotSubDirStr())) {
    		String[] shotSubDirArr = fetchResource.getShotSubDirStr().split(",");
    		for(String str : shotSubDirArr) {
    			if(!StringUtil.isHttp(str)) {
    				changeOption.setShotSubDirStr(fetchResource.getShotSubDirStr());
    				hasChange = true;
    				break;
    			}
    		}
    	}
    	
    	if(hasChange){
    		return changeOption;
    	}
    	return null;
    	
    }
	
	public static ResourceInfoVO buildResouceInfoFromName(String downloadLinkName, Integer category, Integer season, Integer totalEpisode) throws ResourceException{
		ResourceInfoVO resourceVO = new ResourceInfoVO();
		
		Matcher m2 = Pattern.compile(RegexConstant.quality).matcher(downloadLinkName);
    	String finalMatch = null;
    	while(m2.find()){
    		finalMatch = m2.group();
    	}
    	if(StringUtils.isNotBlank(finalMatch)){
    		MovieQualityEnum movieQualityEnum = MovieQualityEnum.getEnumByName(StringUtil.replaceBlank2(finalMatch).toUpperCase());
    		resourceVO.setQuality(movieQualityEnum.getQuality());
    	}
		
		//从下载链接名字中获取片源分辨率
		m2 = Pattern.compile(RegexConstant.resolution).matcher(downloadLinkName);
		if(m2.find()){
			resourceVO.setResolution(m2.group());
		}
		
		//计算清晰度得分
		Integer definitionScore = FetchUtils.translateDefinitionIntoScore(resourceVO.getQuality(), resourceVO.getResolution());
		resourceVO.setDefinition(definitionScore);
		
		//从下载链接名字中获取字幕信息
		m2 = Pattern.compile(RegexConstant.subtitle).matcher(downloadLinkName);
		if(m2.find()){
			resourceVO.setSubtitle(m2.group());
		}else {
			m2 = Pattern.compile(RegexConstant.subtitle_m_encn).matcher(downloadLinkName);
			if(m2.find()){
				resourceVO.setSubtitle("中英双字");
			}else {
				m2 = Pattern.compile(RegexConstant.subtitle_m_cn).matcher(downloadLinkName);
				if(m2.find()){
					resourceVO.setSubtitle("中文字幕");
				}
			}
		}
		
		//若此处未获取到，则在处理链接的时候再次获取
		m2 = Pattern.compile(RegexConstant.format).matcher(downloadLinkName);
		if(m2.find()){
			resourceVO.setFormat(m2.group());
		}
		
		//若此处未获取到，则在处理链接的时候再次获取
		m2 = Pattern.compile(RegexConstant.size).matcher(downloadLinkName);
		if(m2.find()){
			resourceVO.setSize(m2.group());
		}
		
		//避免标题处没有写明第几季的情况，在资源name处再次获取
		if(category == MovieCategoryEnum.tv.getCode() && season == null){
			for(int i=0; i<RegexConstant.list_season.size(); i++){
				m2 = RegexConstant.list_season.get(i).matcher(StringUtil.trimAll(downloadLinkName));
				if(m2.find()){
					season = Utils.chineseNumber2Int(m2.group(1));
					break;
				}
			}
		}
		resourceVO.setSeason(season);
		
		//如果是连续剧则获取最新集数
		if(category == MovieCategoryEnum.tv.getCode()){
			List<Pattern> list_episode = RegexConstant.list_episode3;
			boolean hasEpisode = false;
			for(int i=0; i<list_episode.size(); i++){
	    		m2 = list_episode.get(i).matcher(downloadLinkName);
	    		if(m2.find()){
	    			String m2Grop1 = m2.group(1);
	    			if(m2Grop1 == null){
	    				m2Grop1 = "";
	    			}
					Matcher m3 = Pattern.compile(RegexConstant.cn_number).matcher(m2Grop1);
					if(m3.find()){
						//将中文数字转换为阿拉伯数字
						resourceVO.setEpisodeEnd(Utils.chineseNumber2Int(m3.group()));
					}else if(m2.group().equals("全集")){
						resourceVO.setEpisodeStart(1);
						resourceVO.setEpisodeEnd(totalEpisode);
					}else if(m2Grop1.indexOf("-") != -1){
						//如过集数的样式为40-50
						String[] episodeArr = m2.group(1).split("-");
						Integer startEpisode = Integer.valueOf(episodeArr[0]);
						Integer endEpisode = Integer.valueOf(episodeArr[1]);
						if(endEpisode.intValue()>startEpisode.intValue()){
							resourceVO.setEpisodeStart(startEpisode);
						}
						resourceVO.setEpisodeEnd(endEpisode);
					}else {
						resourceVO.setEpisodeEnd(Integer.valueOf(m2Grop1));
					}
					hasEpisode = true;
					break;
	    		}
	    	}
			
			if(!hasEpisode) {
				throw new ResourceException("剧集未获取到集数信息。downloadLinkName: "+downloadLinkName);
			}
			
		}
		return resourceVO;
	}
	
	public static String compareUploadGetJsonAndDelOld(String dbUriJosn, String newSubDirStr, int fileType) throws Exception {
		if(StringUtils.isBlank(dbUriJosn) && StringUtils.isBlank(newSubDirStr)) {
			throw new BusinessException("老版圖片和新版圖片皆為空！");
		}
		
		List<String> dbUriList = JSON.parseArray(dbUriJosn, String.class);
		String[] newSubDirArr = newSubDirStr.split(",");
		List<String> newUriList = new ArrayList<>();
		for(int i=0; i<newSubDirArr.length; i++) {
			String newSubDir = newSubDirArr[i];
			if(StringUtil.isHttp(newSubDir)) {
				newUriList.add(dbUriList.get(i));
				continue;
			}
			if(StringUtils.isNotBlank(newSubDir)) {
				String uri = null;
				if(fileType == CommonConstants.poster_v) {
					uri = FetchUtils.uploadPosterGetUriFromDir(newSubDir);
				}else if(fileType == CommonConstants.photo_v) {
					uri = FetchUtils.uploadPhotoGetUriFromDir(newSubDir);
				}else if(fileType == CommonConstants.shot_v) {
					uri = FetchUtils.uploadShotGetUriFromDir(newSubDir);
				}else {
					throw new BusinessException("文件類型錯誤");
				}
				newUriList.add(uri);
			}
			//如果沒有超出原List的長度，則刪除原來的。
			if(dbUriList != null && dbUriList.size() > i) {
				BucketUtils.delete(dbUriList.get(i));
			}
			
		}
		if(newUriList.size() > 0) {
			return JSON.toJSONString(newUriList);
		}
		return null;
	}
	
	public static String saveTempIcon(byte[] bytes, String suffix) throws IOException {
		String root = ConfigUtils.getPropertyValue("fs.temp.dir");
		String typeDir = FileUtils.getTypeDir("icon");
		String targetDir = root+typeDir;
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		FileUtils.saveFile(bytes, targetDir, fileName, suffix);
		return typeDir+"/"+fileName+"."+suffix;
	}
	
	public static String saveTempIcon(String url) throws Exception {
		byte[] bytes = HttpUtils.getBytes(url);
		String suffix = url.substring(url.lastIndexOf(".")+1);
		return saveTempIcon(bytes, suffix);
	}
	
	public static String saveTempPoster(byte[] bytes, String suffix) throws IOException {
		String root = ConfigUtils.getPropertyValue("fs.temp.dir");
		String typeDir = FileUtils.getTypeDir("poster");
		String targetDir = root+typeDir;
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		FileUtils.saveFile(bytes, targetDir, fileName, suffix);
		return typeDir+"/"+fileName+"."+suffix;
	}
	
	public static String saveTempPoster(String url) throws Exception {
		byte[] bytes = HttpUtils.getBytes(url);
		String suffix = url.substring(url.lastIndexOf(".")+1);
		return saveTempPoster(bytes, suffix);
	}
	
	public static String saveTempPhoto(byte[] bytes, String suffix) throws IOException {
		String root = ConfigUtils.getPropertyValue("fs.temp.dir");
		String typeDir = FileUtils.getTypeDir("photo");
		String targetDir = root+typeDir;
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		FileUtils.saveFile(bytes, targetDir, fileName, suffix);
		return typeDir+"/"+fileName+"."+suffix;
	}
	
	public static String saveTempPhoto(String url) throws Exception {
		byte[] bytes = HttpUtils.getBytes(url);
		String suffix = url.substring(url.lastIndexOf(".")+1);
		return saveTempPhoto(bytes, suffix);
	}
	
	public static String saveTempShot(byte[] bytes, String suffix) throws IOException {
		String root = ConfigUtils.getPropertyValue("fs.temp.dir");
		String typeDir = FileUtils.getTypeDir("shot");
		String targetDir = root+typeDir;
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		FileUtils.saveFile(bytes, targetDir, fileName, suffix);
		return typeDir+"/"+fileName+"."+suffix;
	}
	
	public static String saveTempShot(String url) throws Exception {
		byte[] bytes = HttpUtils.getBytes(url);
		String suffix = url.substring(url.lastIndexOf(".")+1);
		return saveTempShot(bytes, suffix);
	}
	
	public static String saveTempTorrent(byte[] bytes, String suffix) throws IOException {
		String root = ConfigUtils.getPropertyValue("fs.temp.dir");
		String typeDir = FileUtils.getTypeDir("torrent");
		String targetDir = root+typeDir;
		String fileName = StringUtil.getId(CommonConstants.file_s);
		FileUtils.saveFile(bytes, targetDir, fileName, suffix);
		return typeDir+"/"+fileName+"."+suffix;
	}
	
	public static String saveTempTorrent(String url) throws Exception {
		byte[] bytes = HttpUtils.getBytes(url);
		String suffix = url.substring(url.lastIndexOf(".")+1);
		return saveTempTorrent(bytes, suffix);
	}
}
