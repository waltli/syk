package com.sbolo.syk.fetch.po;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieCategoryEnum;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.StringUtil;


public class MovieResourceEntity {
	private String movieId;

    private String icon;   	//icon的名字
    
    private String busIcon;  //带有uri的icon
    
    private String iconUrl; //待下载的ICON链接
    
    private String poster;  //poster名字被json转换成的字符串集
    
    private String busPoster; //比上面多了uri
    
    private List<String> busPosterList;  //带uri的Poster的list
    
    private String posterPageUrl;  //待去下载poster的网址

    private String pureName;
    
    private String pureNameShow;
    
    private String anotherName;
    
    private String anotherNameSplit[];
    
    private String labels;
    
    private String[] labelsSplit;

    private String director;

    private String writers;

    private String cast;

    private String locations;
    
    private String[] locationSplit;

    private String language;
    
    private String[] languageSplit;

    private Date releaseTime;
    
    private String releaseTimeStr;
    
    private String year;
    
    private String releaseTimeFormat;

    private String duration;

    private String doubanId;

    private String imdbId;

    private Double doubanScore;
    
    private String doubanScoreCalc;

    private Double imdbScore;

    private Integer attentionRate;
    
    private Integer category;
    
    private Integer presentSeason;
    
    private Integer totalEpisode;
    
    private Integer movieStatus;

    private Date createTime;
    
    private String createTimeStr;

    private Date updateTime;
    
    private String updateTimeStr;
    
    private Date resourceWriteTime;
    
    private String resourceWriteTimeStr;

    private String summary;
    
    private String summaryShow;
    
    private String optimalResourceId;
    
    private Integer countClick;
    
    private Integer countComment;
    
    private Integer countDownload;
    
    private String resourceId;

    private String size;

    private String format;

    private Integer definition;
    
    private String definitionCalc;

    private String quality;

    private String resolution;

    private Integer speed;

    private String downloadLink;  //本地link纯名字
    
    private String busDownloadLink;  //link带有URI
    
    private String thunderDecoding;
    
    private Boolean isLocalLink;
    
    private String linkType;

    private Integer season;

    private Integer episodeStart;
    
    private Integer episodeEnd;

    private String subtitle;

    private String photos;  //本地photo名字字符串
    
    private String busPhotos;  //本地photo带Uri字符串
    
    private List<String> busPhotosList;  //本地photo带UriList
    
    private String comeFromUrl;
    
    private Integer resourceStatus;

	public Integer getMovieStatus() {
		return movieStatus;
	}


	public void setMovieStatus(Integer movieStatus) {
		this.movieStatus = movieStatus;
	}


	public Integer getResourceStatus() {
		return resourceStatus;
	}


	public void setResourceStatus(Integer resourceStatus) {
		this.resourceStatus = resourceStatus;
	}


	public String getMovieId() {
		return movieId;
	}


	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}


	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	public String getBusIcon() {
		return busIcon;
	}


	public void setBusIcon(String busIcon) {
		this.busIcon = busIcon;
	}


	public String getIconUrl() {
		return iconUrl;
	}


	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}


	public String getPoster() {
		return poster;
	}


	public void setPoster(String poster) {
		this.poster = poster;
	}


	public String getBusPoster() {
		return busPoster;
	}


	public void setBusPoster(String busPoster) {
		this.busPoster = busPoster;
	}


	public List<String> getBusPosterList() {
		return busPosterList;
	}


	public void setBusPosterList(List<String> busPosterList) {
		this.busPosterList = busPosterList;
	}


	public String getPosterPageUrl() {
		return posterPageUrl;
	}


	public void setPosterPageUrl(String posterPageUrl) {
		this.posterPageUrl = posterPageUrl;
	}


	public String getPureName() {
		return pureName;
	}


	public void setPureName(String pureName) {
		this.pureName = pureName;
	}


	public String getPureNameShow() {
		return pureNameShow;
	}


	public void setPureNameShow(String pureNameShow) {
		this.pureNameShow = pureNameShow;
	}


	public String getAnotherName() {
		return anotherName;
	}


	public void setAnotherName(String anotherName) {
		this.anotherName = anotherName;
	}


	public String[] getAnotherNameSplit() {
		return anotherNameSplit;
	}


	public void setAnotherNameSplit(String[] anotherNameSplit) {
		this.anotherNameSplit = anotherNameSplit;
	}


	public String getLabels() {
		return labels;
	}


	public void setLabels(String labels) {
		this.labels = labels;
	}


	public String[] getLabelsSplit() {
		return labelsSplit;
	}


	public void setLabelsSplit(String[] labelsSplit) {
		this.labelsSplit = labelsSplit;
	}


	public String getDirector() {
		return director;
	}


	public void setDirector(String director) {
		this.director = director;
	}


	public String getWriters() {
		return writers;
	}


	public void setWriters(String writers) {
		this.writers = writers;
	}


	public String getCast() {
		return cast;
	}


	public void setCast(String cast) {
		this.cast = cast;
	}


	public String getLocations() {
		return locations;
	}


	public void setLocations(String locations) {
		this.locations = locations;
	}


	public String[] getLocationSplit() {
		return locationSplit;
	}


	public void setLocationSplit(String[] locationSplit) {
		this.locationSplit = locationSplit;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String[] getLanguageSplit() {
		return languageSplit;
	}


	public void setLanguageSplit(String[] languageSplit) {
		this.languageSplit = languageSplit;
	}


	public Date getReleaseTime() {
		return releaseTime;
	}


	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}


	public String getReleaseTimeStr() {
		return releaseTimeStr;
	}


	public void setReleaseTimeStr(String releaseTimeStr) {
		this.releaseTimeStr = releaseTimeStr;
	}


	public String getYear() {
		return year;
	}


	public void setYear(String year) {
		this.year = year;
	}


	public String getReleaseTimeFormat() {
		return releaseTimeFormat;
	}


	public void setReleaseTimeFormat(String releaseTimeFormat) {
		this.releaseTimeFormat = releaseTimeFormat;
	}


	public String getDuration() {
		return duration;
	}


	public void setDuration(String duration) {
		this.duration = duration;
	}


	public String getDoubanId() {
		return doubanId;
	}


	public void setDoubanId(String doubanId) {
		this.doubanId = doubanId;
	}


	public String getImdbId() {
		return imdbId;
	}


	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}


	public Double getDoubanScore() {
		return doubanScore;
	}


	public void setDoubanScore(Double doubanScore) {
		this.doubanScore = doubanScore;
	}


	public String getDoubanScoreCalc() {
		return doubanScoreCalc;
	}


	public void setDoubanScoreCalc(String doubanScoreCalc) {
		this.doubanScoreCalc = doubanScoreCalc;
	}


	public Double getImdbScore() {
		return imdbScore;
	}


	public void setImdbScore(Double imdbScore) {
		this.imdbScore = imdbScore;
	}


	public Integer getAttentionRate() {
		return attentionRate;
	}


	public void setAttentionRate(Integer attentionRate) {
		this.attentionRate = attentionRate;
	}


	public Integer getCategory() {
		return category;
	}


	public void setCategory(Integer category) {
		this.category = category;
	}


	public Integer getPresentSeason() {
		return presentSeason;
	}


	public void setPresentSeason(Integer presentSeason) {
		this.presentSeason = presentSeason;
	}


	public Integer getTotalEpisode() {
		return totalEpisode;
	}


	public void setTotalEpisode(Integer totalEpisode) {
		this.totalEpisode = totalEpisode;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getCreateTimeStr() {
		return createTimeStr;
	}


	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public String getUpdateTimeStr() {
		return updateTimeStr;
	}


	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}


	public Date getResourceWriteTime() {
		return resourceWriteTime;
	}


	public void setResourceWriteTime(Date resourceWriteTime) {
		this.resourceWriteTime = resourceWriteTime;
	}


	public String getResourceWriteTimeStr() {
		return resourceWriteTimeStr;
	}


	public void setResourceWriteTimeStr(String resourceWriteTimeStr) {
		this.resourceWriteTimeStr = resourceWriteTimeStr;
	}


	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}


	public String getSummaryShow() {
		return summaryShow;
	}


	public void setSummaryShow(String summaryShow) {
		this.summaryShow = summaryShow;
	}


	public String getOptimalResourceId() {
		return optimalResourceId;
	}


	public void setOptimalResourceId(String optimalResourceId) {
		this.optimalResourceId = optimalResourceId;
	}


	public Integer getCountClick() {
		return countClick;
	}


	public void setCountClick(Integer countClick) {
		this.countClick = countClick;
	}


	public Integer getCountComment() {
		return countComment;
	}


	public void setCountComment(Integer countComment) {
		this.countComment = countComment;
	}


	public Integer getCountDownload() {
		return countDownload;
	}


	public void setCountDownload(Integer countDownload) {
		this.countDownload = countDownload;
	}


	public String getResourceId() {
		return resourceId;
	}


	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getFormat() {
		return format;
	}


	public void setFormat(String format) {
		this.format = format;
	}


	public Integer getDefinition() {
		return definition;
	}


	public void setDefinition(Integer definition) {
		this.definition = definition;
	}


	public String getDefinitionCalc() {
		return definitionCalc;
	}


	public void setDefinitionCalc(String definitionCalc) {
		this.definitionCalc = definitionCalc;
	}


	public String getQuality() {
		return quality;
	}


	public void setQuality(String quality) {
		this.quality = quality;
	}


	public String getResolution() {
		return resolution;
	}


	public void setResolution(String resolution) {
		this.resolution = resolution;
	}


	public Integer getSpeed() {
		return speed;
	}


	public void setSpeed(Integer speed) {
		this.speed = speed;
	}


	public String getDownloadLink() {
		return downloadLink;
	}


	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}


	public String getBusDownloadLink() {
		return busDownloadLink;
	}


	public void setBusDownloadLink(String busDownloadLink) {
		this.busDownloadLink = busDownloadLink;
	}


	public String getThunderDecoding() {
		return thunderDecoding;
	}


	public void setThunderDecoding(String thunderDecoding) {
		this.thunderDecoding = thunderDecoding;
	}


	public Boolean getIsLocalLink() {
		return isLocalLink;
	}


	public void setIsLocalLink(Boolean isLocalLink) {
		this.isLocalLink = isLocalLink;
	}


	public String getLinkType() {
		return linkType;
	}


	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}


	public Integer getSeason() {
		return season;
	}


	public void setSeason(Integer season) {
		this.season = season;
	}


	public Integer getEpisodeStart() {
		return episodeStart;
	}


	public void setEpisodeStart(Integer episodeStart) {
		this.episodeStart = episodeStart;
	}


	public Integer getEpisodeEnd() {
		return episodeEnd;
	}


	public void setEpisodeEnd(Integer episodeEnd) {
		this.episodeEnd = episodeEnd;
	}


	public String getSubtitle() {
		return subtitle;
	}


	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}


	public String getPhotos() {
		return photos;
	}


	public void setPhotos(String photos) {
		this.photos = photos;
	}


	public String getBusPhotos() {
		return busPhotos;
	}


	public void setBusPhotos(String busPhotos) {
		this.busPhotos = busPhotos;
	}


	public List<String> getBusPhotosList() {
		return busPhotosList;
	}


	public void setBusPhotosList(List<String> busPhotosList) {
		this.busPhotosList = busPhotosList;
	}


	public String getComeFromUrl() {
		return comeFromUrl;
	}


	public void setComeFromUrl(String comeFromUrl) {
		this.comeFromUrl = comeFromUrl;
	}
	
	public MovieInfoEntity getMovie(){
		MovieInfoEntity movie = new MovieInfoEntity();
		movie.setAnotherName(this.getAnotherName());
		movie.setAttentionRate(this.getAttentionRate());
		movie.setCast(this.getCast());
		movie.setCategory(this.getCategory());
		movie.setCountClick(this.getCountClick());
		movie.setCountComment(this.getCountComment());
		movie.setCountDownload(this.getCountDownload());
		movie.setCreateTime(this.getCreateTime());
		movie.setDirector(this.getDirector());
		movie.setDoubanId(this.getDoubanId());
		movie.setDoubanScore(this.getDoubanScore());
		movie.setDuration(this.getDuration());
		movie.setIcon(this.getIcon());
		movie.setImdbId(this.getImdbId());
		movie.setImdbScore(this.getImdbScore());
		movie.setLabels(this.getLabels());
		movie.setLanguage(this.getLanguage());
		movie.setLocations(this.getLocations());
		movie.setMovieId(this.getMovieId());
		movie.setOptimalResourceId(this.getOptimalResourceId());
		movie.setPoster(this.getPoster());
		movie.setPresentSeason(this.getPresentSeason());
		movie.setPureName(this.getPureName());
		movie.setReleaseTime(this.getReleaseTime());
		movie.setResourceWriteTime(this.getResourceWriteTime());
		movie.setSummary(this.getSummary());
		movie.setTotalEpisode(this.getTotalEpisode());
		movie.setUpdateTime(this.getUpdateTime());
		movie.setWriters(this.getWriters());
		movie.setYear(this.getYear());
		return movie;
	}
	
	public ResourceInfoEntity getResource(){
		ResourceInfoEntity resource = new ResourceInfoEntity();
		resource.setComeFromUrl(this.getComeFromUrl());
		resource.setDefinition(this.getDefinition());
		resource.setDownloadLink(this.getDownloadLink());
		resource.setEpisodeEnd(this.getEpisodeEnd());
		resource.setEpisodeStart(this.getEpisodeStart());
		resource.setFormat(this.getFormat());
		resource.setMovieId(this.getMovieId());
		resource.setPhotos(this.getPhotos());
		resource.setPureName(this.getPureName());
		resource.setQuality(this.getQuality());
		resource.setResolution(this.getResolution());
		resource.setSeason(this.getSeason());
		resource.setSize(this.getSize());
		resource.setSpeed(this.getSpeed());
		resource.setResourceId(this.getResourceId());
		resource.setSubtitle(this.getSubtitle());
		return resource;
	}
	
	public void parse(){
    	SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.timeFormat.get(10));
    	if(this.getCreateTime() != null){
    		this.setCreateTimeStr(sdf.format(this.getCreateTime()));
    	}
    	if(this.getUpdateTime() != null){
    		this.setUpdateTimeStr(sdf.format(this.getUpdateTime()));
    	}
    	if(this.getResourceWriteTime() != null){
    		this.setResourceWriteTimeStr(sdf.format(this.getResourceWriteTime()));
    	}
    	if(this.getReleaseTime() != null){
    		if(StringUtils.isNotBlank(this.getReleaseTimeFormat())){
    			sdf = new SimpleDateFormat(this.getReleaseTimeFormat());
    		}
    		this.setReleaseTimeStr(sdf.format(this.getReleaseTime()));
    	}
    	
    	if(StringUtils.isBlank(this.getYear())){
    		this.setYear(this.getReleaseTimeStr().substring(0,4));
    	}
    	
    	StringBuffer pureNameShowSB = new StringBuffer(this.getPureName());
    	if(this.getCategory() == MovieCategoryEnum.tv.getCode()){
    		if(this.getTotalEpisode() != null && this.getEpisodeEnd() != null){
    			if(this.getEpisodeEnd().intValue() == this.getTotalEpisode().intValue()){
        			pureNameShowSB.append(" 全集");
        		}else {
        			pureNameShowSB.append(" 更新至 EP");
        			if(this.getEpisodeEnd() < 10){
        				pureNameShowSB.append("0");
        			}
        			pureNameShowSB.append(this.getEpisodeEnd());
        		}
    		}
    	}else if(StringUtils.isNotBlank(this.getAnotherName())){
    		String[] anotherNameSplit = this.getAnotherName().split(RegexConstant.slashSep);
    		pureNameShowSB.append(" / ").append(anotherNameSplit[0]);
    		this.setAnotherNameSplit(anotherNameSplit);
    	}
    	String pureNameShow =  StringUtil.StringLimit(pureNameShowSB.toString(), 26);
    	this.setPureNameShow(pureNameShow);
    	
    	
    	if(StringUtils.isNotEmpty(this.getIcon())){
    		String iconView = ConfigUtil.getPropertyValue("iconView");
    		this.setBusIcon(iconView + "/" + this.getIcon());
    	}
    	
    	if(StringUtils.isNotBlank(this.getPoster())){
    		List<String> posterShorts = JSON.parseArray(this.getPoster(), String.class);
    		List<String> posterList = new ArrayList<String>();
    		String posterStr = "";
    		for(String poster : posterShorts){
    			String posterView = ConfigUtil.getPropertyValue("posterView");
    			String posterUri = posterView + "/" + poster;
    			posterList.add(posterUri);
    			posterStr += ","+posterUri;
    		}
    		this.setBusPosterList(posterList);
    		this.setBusPoster(posterStr.substring(1));
    	}
    	
    	if(StringUtils.isNotBlank(this.getLabels())){
    		this.setLabelsSplit(this.getLabels().split(RegexConstant.slashSep));
    	}
    	
    	if(StringUtils.isNotBlank(this.getLanguage())){
    		this.setLanguageSplit(this.getLanguage().split(RegexConstant.slashSep));
    	}
    	
    	if(this.getDoubanScore() != null){
    		BigDecimal divisor = new BigDecimal(2);
    		BigDecimal dividend = new BigDecimal(this.getDoubanScore());
    		BigDecimal quotient = dividend.divide(divisor, 1, BigDecimal.ROUND_FLOOR);
    		String quotientStr = quotient.toString();
    		String[] intAndPoint = quotientStr.split("\\.");
    		String integerStr = intAndPoint[0];
    		int point = Integer.valueOf(intAndPoint[1]);
    		if(point <= 1){
    			integerStr += "0";
    		}else if(point <= 6){
    			integerStr += "5";
    		}else if(point <= 9){
    			integerStr = (Integer.valueOf(integerStr)+1)+"0";
    		}
    		this.setDoubanScoreCalc(integerStr);
    	}else {
    		this.setDoubanScoreCalc("00");
    	}
    	
    	if(StringUtils.isNotBlank(this.getSummary())){
    		
    		String summary = Pattern.compile("(\\s|　)*<br>(\\s|　)*").matcher(this.getSummary()).replaceAll("");
    		String summaryShow =  StringUtil.StringLimit(summary, 124);
        	this.setSummaryShow(summaryShow);
    	}else {
    		this.setSummaryShow("...");
    	}
    	
    }


	public static void parse(List<MovieResourceEntity> movieResources){
		for(MovieResourceEntity movieResource : movieResources){
			movieResource.parse();
		}
	}
}
