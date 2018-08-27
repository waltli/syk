package com.sbolo.syk.fetch.tool;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieQualityEnum;
import com.sbolo.syk.common.constants.MovieResolutionConstant;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.tools.BucketUtils;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.FileUtils;
import com.sbolo.syk.common.tools.GrapicmagickUtils;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.common.vo.LinkAnalyzeResultVO;
import com.sbolo.syk.fetch.entity.MovieInfoEntity;
import com.sbolo.syk.fetch.entity.MovieLabelEntity;
import com.sbolo.syk.fetch.entity.MovieLocationEntity;
import com.sbolo.syk.fetch.entity.ResourceInfoEntity;
import com.sbolo.syk.fetch.pipeline.MyPipeline;
import com.sbolo.syk.fetch.vo.MovieInfoVO;
import com.sbolo.syk.fetch.vo.MovieLabelVO;
import com.sbolo.syk.fetch.vo.MovieLocationVO;
import com.sbolo.syk.fetch.vo.ResourceInfoVO;

public class FetchUtils {
	private static final Logger log = LoggerFactory.getLogger(FetchUtils.class);
	
	
	public static String uploadIcon(byte[] bytes, String suffix) throws Exception {
		int fixWidth = CommonConstants.icon_width;
		int fixHeight = CommonConstants.icon_height;
		byte[] imageBytes = GrapicmagickUtils.descale(bytes, fixWidth, fixHeight);
		String targetDir = ConfigUtil.getPropertyValue("bucket.formal.icon");
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		String uri = BucketUtils.upload2Subdir(imageBytes, targetDir, fileName, suffix);
		return uri;
	}
	
	public static String uploadIcon(String iconUrl) throws Exception {
		byte[] bytes = HttpUtils.getBytes(iconUrl);
		String suffix = iconUrl.substring(iconUrl.lastIndexOf(".")+1);
		return uploadIcon(bytes, suffix);
	}
	
	public static String uploadIcon(File file) throws Exception {
		byte[] bytes = FileUtils.getBytes(file);
		String path = file.getPath();
		String suffix = path.substring(path.lastIndexOf(".")+1);
		return uploadIcon(bytes, suffix);
	}
	
	public static String uploadIconFromUri(String iconTempUri) throws Exception {
		String iconTempDir = ConfigUtil.getPropertyValue("icon.temp.dir");
		File iconFile = new File(iconTempDir+iconTempUri);
		return uploadIcon(iconFile);
	}
	
	public static String uploadPoster(byte[] bytes, String suffix) throws Exception {
		int fixWidth = CommonConstants.poster_width;
		int fixHeight = CommonConstants.poster_height;
		byte[] imageBytes = GrapicmagickUtils.descale(bytes, fixWidth, fixHeight);
		String targetDir = ConfigUtil.getPropertyValue("bucket.formal.poster");
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		String uri = BucketUtils.upload2Subdir(imageBytes, targetDir, fileName, suffix);
		return uri;
	}
	
	public static String uploadPoster(String posterUrl) throws Exception {
		byte[] bytes = HttpUtils.getBytes(posterUrl);
		String suffix = posterUrl.substring(posterUrl.lastIndexOf(".")+1);
		return uploadPoster(bytes, suffix);
	}
	
	public static String uploadPoster(File file) throws Exception {
		byte[] bytes = FileUtils.getBytes(file);
		String path = file.getPath();
		String suffix = path.substring(path.lastIndexOf(".")+1);
		return uploadPoster(bytes, suffix);
	}
	
	public static String uploadPosterFromUri(String posterTempUri) throws Exception {
		String posterTempDir = ConfigUtil.getPropertyValue("poster.temp.dir");
		File posterFile = new File(posterTempDir+posterTempUri);
		return uploadPoster(posterFile);
	}
	
	public static String uploadPosterAndGetUriJsonFromTempUris(String posterTempUriStr) throws Exception {
		String[] posterTempUriArr = posterTempUriStr.split(",");
		List<String> posterUriList = new ArrayList<>();
		for(String posterTempUri : posterTempUriArr) {
			String posterUri = uploadPosterFromUri(posterTempUri);
			posterUriList.add(posterUri);
		}
		if(posterUriList.size() > 0) {
			String posterUriJson = JSON.toJSONString(posterUriList);
			return posterUriJson;
		}
		return null;
	}
	
	public static String uploadPhoto(byte[] bytes, String suffix) throws Exception {
		int fixWidth = CommonConstants.photo_width;
		int fixHeight = CommonConstants.photo_height;
		byte[] imageBytes = GrapicmagickUtils.descale(bytes, fixWidth, fixHeight);
		String targetDir = ConfigUtil.getPropertyValue("bucket.formal.photo");
		String fileName = StringUtil.getId(CommonConstants.pic_s);
		String uri = BucketUtils.upload2Subdir(imageBytes, targetDir, fileName, suffix);
		return uri;
	}
	
	public static String uploadPhoto(String photoUrl) throws Exception {
		byte[] bytes = HttpUtils.getBytes(photoUrl);
		String suffix = photoUrl.substring(photoUrl.lastIndexOf(".")+1);
		return uploadPhoto(bytes, suffix);
	}
	
	public static String uploadPhoto(File file) throws Exception {
		byte[] bytes = FileUtils.getBytes(file);
		String path = file.getPath();
		String suffix = path.substring(path.lastIndexOf(".")+1);
		return uploadPhoto(bytes, suffix);
	}
	
	public static String uploadPhotoFromUri(String photoTempUri) throws Exception {
		String photoTempDir = ConfigUtil.getPropertyValue("photo.temp.dir");
		File photoFile = new File(photoTempDir+photoTempUri);
		return uploadPhoto(photoFile);
	}
	
	public static String uploadPhotoAndGetUriJsonFromTempUris(String photoTempUriStr) throws Exception {
		String[] photoTempUriArr = photoTempUriStr.split(",");
		List<String> photoUriList = new ArrayList<>();
		for(String photoTempUri : photoTempUriArr) {
			String photoUri = uploadPhotoFromUri(photoTempUri);
			photoUriList.add(photoUri);
		}
		if(photoUriList.size() > 0) {
			String photoUriJson = JSON.toJSONString(photoUriList);
			return photoUriJson;
		}
		return null;
	}
	
	public static String uploadShot(byte[] bytes, String suffix) throws Exception {
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
			String targetDir = ConfigUtil.getPropertyValue("bucket.formal.shot");
			String fileName = StringUtil.getId(CommonConstants.pic_s);
			String uri = BucketUtils.upload2Subdir(imageBytes, targetDir, fileName, suffix);
			return uri;
		} finally {
			if(markStream != null) {
				markStream.close();
				markStream = null;
			}
		}
		
	}
	
	public static String uploadShot(String shotUrl) throws Exception {
		byte[] bytes = HttpUtils.getBytes(shotUrl);
		String suffix = shotUrl.substring(shotUrl.lastIndexOf(".")+1);
		return uploadShot(bytes, suffix);
	}
	
	public static String uploadShot(File file) throws Exception {
		byte[] bytes = FileUtils.getBytes(file);
		String path = file.getPath();
		String suffix = path.substring(path.lastIndexOf(".")+1);
		return uploadShot(bytes, suffix);
	}
	
	public static String uploadShotFromUri(String shotTempUri) throws Exception {
		String iconTempDir = ConfigUtil.getPropertyValue("shot.temp.dir");
		File shotFile = new File(iconTempDir+shotTempUri);
		return uploadShot(shotFile);
	}
	
	public static String uploadShotAndGetUriJsonFromTempUris(String shotTempUriStr) throws Exception {
		String[] shotTempUriArr = shotTempUriStr.split(",");
		List<String> shotUriList = new ArrayList<>();
		for(String shotTempUri : shotTempUriArr) {
			String shotUri = uploadShotFromUri(shotTempUri);
			shotUriList.add(shotUri);
		}
		if(shotUriList.size() > 0) {
			String shotUriJson = JSON.toJSONString(shotUriList);
			return shotUriJson;
		}
		return null;
	}
	
	public static String uploadTorrent(byte[] bytes, String torrentName, String suffix) throws Exception {
		String targetDir = ConfigUtil.getPropertyValue("bucket.formal.torrent");
		String uri = BucketUtils.upload2Subdir(bytes, targetDir, torrentName, suffix);
		return uri;
	}
	
	public static String uploadTorrent(String torrentUrl, String torrentName) throws Exception {
		byte[] bytes = HttpUtils.getBytes(torrentUrl);
		String suffix = torrentUrl.substring(torrentUrl.lastIndexOf(".")+1);
		return uploadTorrent(bytes, torrentName, suffix);
	}
	
	public static String uploadTorrent(File file, String torrentName) throws Exception {
		byte[] bytes = FileUtils.getBytes(file);
		String path = file.getPath();
		String suffix = path.substring(path.lastIndexOf(".")+1);
		return uploadTorrent(bytes, torrentName, suffix);
	}
	
	public static String uploadTorrentFromUri(String torrentTempUri, String torrentName) throws Exception {
		String torrentTempDir = ConfigUtil.getPropertyValue("torrent.temp.dir");
		File torrentFile = new File(torrentTempDir+torrentTempUri);
		return uploadTorrent(torrentFile, torrentName);
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
    public static void deleteResourceFile(ResourceInfoVO resource){
    	String formalDir = ConfigUtil.getPropertyValue("fs.formal.dir");
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
    public static void deleteMovieFile(MovieInfoVO movie){
    	String formalDir = ConfigUtil.getPropertyValue("fs.formal.dir");
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
	public static MovieInfoVO changeOption(MovieInfoEntity dbMovie, MovieInfoVO fetchMovie, List<MovieLabelEntity> dbLabels, List<MovieLocationEntity> dbLocations, Date thisTime){
    	MovieInfoVO changeOption = new MovieInfoVO();
    	boolean hasChange = false;
    	
    	if(StringUtils.isNotBlank(dbMovie.getLabels()) && 
    			StringUtils.isNotBlank(fetchMovie.getLabels()) &&
    			!dbMovie.getLabels().equals(fetchMovie.getLabels())) {
    		//从数据库中查询出的labels
    		String[] fetchLabelNames = fetchMovie.getLabels().split(",");
    		
    		List<MovieLabelVO> newLabels = new ArrayList<>();
    		
    		for(String fetchLabelName : fetchLabelNames) {
    			for(MovieLabelEntity dbLabel : dbLabels) {
    				if(dbLabel.getLabelName().equals(fetchLabelName)) {
    					//如果有相同的，证明之前已经有了，直接break，循环下一个被fetch到的
    					break;
    				}
    			}
    			MovieLabelVO newLabel = MovieLabelVO.buildLabel(fetchLabelName, dbMovie.getPrn(), dbMovie.getPureName(), dbMovie.getReleaseTime(), thisTime);
    			newLabels.add(newLabel);
    		}
    		if(newLabels.size() != 0) {
    			hasChange = true;
    			changeOption.setLabels(fetchMovie.getLabels());
    			changeOption.setLabelList(newLabels);
    		}
    	}
    	
    	
    	if(StringUtils.isNotBlank(dbMovie.getLocations()) && 
    			StringUtils.isNotBlank(fetchMovie.getLocations()) &&
    			!dbMovie.getLocations().equals(fetchMovie.getLocations())) {
    		//从数据库中查询出的labels 
    		String[] fetchLocationNames = fetchMovie.getLocations().split(",");
    		
    		List<MovieLocationVO> newLocations = new ArrayList<>();
    		
    		for(String fetchLocationStr : fetchLocationNames) {
    			//地区只要中文，如果实在没有也没有办法！
    			Matcher m = Pattern.compile(RegexConstant.chinese).matcher(fetchLocationStr);
    			if(!m.find()){
    				continue;
    			}
    			String fetchLocationName = m.group();
    			for(MovieLocationEntity dbLocation : dbLocations) {
    				if(dbLocation.getLocationName().equals(fetchLocationName)) {
    					//如果有相同的，证明之前已经有了，直接break，循环下一个被fetch到的
    					break;
    				}
    			}
    			MovieLocationVO newLocation = MovieLocationVO.buildLocation(fetchLocationName, dbMovie.getPrn(), dbMovie.getPureName(), dbMovie.getReleaseTime(), thisTime);
    			newLocations.add(newLocation);
    		}
    		if(newLocations.size() != 0) {
    			hasChange = true;
    			changeOption.setLocations(fetchMovie.getLocations());
    			changeOption.setLocationList(newLocations);
    		}
    	}
    	
    	
    	
    	
    	if(StringUtils.isBlank(dbMovie.getIconUri()) && StringUtils.isNotBlank(fetchMovie.getIconUri())){
    		changeOption.setIconUri(fetchMovie.getIconUri());
    		hasChange = true;
    	}
    	
    	if(StringUtils.isNotBlank(fetchMovie.getAnotherName())){
    		if(StringUtils.isBlank(dbMovie.getAnotherName()) || !dbMovie.getAnotherName().equals(fetchMovie.getAnotherName())){
    			changeOption.setAnotherName(fetchMovie.getAnotherName());
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
    		if(dbMovie.getDoubanScore() == null || dbMovie.getDoubanScore().intValue() != fetchMovie.getDoubanScore().intValue()){
    			changeOption.setDoubanScore(fetchMovie.getDoubanScore());
    			hasChange = true;
    		}
    	}
    	
    	if(fetchMovie.getImdbScore() != null){
    		if(dbMovie.getImdbScore() == null || dbMovie.getImdbScore().intValue() != fetchMovie.getImdbScore().intValue()){
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
}
