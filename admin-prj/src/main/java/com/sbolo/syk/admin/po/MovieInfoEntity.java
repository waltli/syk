package com.sbolo.syk.admin.po;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.StringUtil;

public class MovieInfoEntity {
    private Integer id;

    private String movieId;

    private String icon;   	//icon的名字
    
    private String busIcon;  //带有uri的icon
    
    private String iconUrl; //待下载的ICON链接
    
    private String poster;  //poster名字被json转换成的字符串集
    
    private String busPoster; //比上面多了uri
    
    private List<String> busPhotosList;  //本地photo带UriList
    
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
    private List<LabelMappingEntity> labelList;
    
    private List<LocationMappingEntity> locationList;

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
    
    private Integer action;  //Business property, that will direct 'insert' or 'update' or 'abandon'.

	public List<LabelMappingEntity> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<LabelMappingEntity> labelList) {
		this.labelList = labelList;
	}

	public List<LocationMappingEntity> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<LocationMappingEntity> locationList) {
		this.locationList = locationList;
	}

	public Integer getMovieStatus() {
		return movieStatus;
	}

	public void setMovieStatus(Integer movieStatus) {
		this.movieStatus = movieStatus;
	}

	public List<String> getBusPhotosList() {
		return busPhotosList;
	}

	public void setBusPhotosList(List<String> busPhotosList) {
		this.busPhotosList = busPhotosList;
	}

	public String getBusIcon() {
		return busIcon;
	}

	public void setBusIcon(String busIcon) {
		this.busIcon = busIcon;
	}

	public String getBusPoster() {
		return busPoster;
	}

	public void setBusPoster(String busPoster) {
		this.busPoster = busPoster;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
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

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public Integer getTotalEpisode() {
		return totalEpisode;
	}

	public void setTotalEpisode(Integer totalEpisode) {
		this.totalEpisode = totalEpisode;
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

	public String[] getAnotherNameSplit() {
		return anotherNameSplit;
	}

	public void setAnotherNameSplit(String[] anotherNameSplit) {
		this.anotherNameSplit = anotherNameSplit;
	}

	public String[] getLocationSplit() {
		return locationSplit;
	}

	public void setLocationSplit(String[] locationSplit) {
		this.locationSplit = locationSplit;
	}

	public String[] getLanguageSplit() {
		return languageSplit;
	}

	public void setLanguageSplit(String[] languageSplit) {
		this.languageSplit = languageSplit;
	}

	public String getSummaryShow() {
		return summaryShow;
	}

	public void setSummaryShow(String summaryShow) {
		this.summaryShow = summaryShow;
	}

	public Integer getPresentSeason() {
		return presentSeason;
	}

	public void setPresentSeason(Integer presentSeason) {
		this.presentSeason = presentSeason;
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

	public String getPureNameShow() {
		return pureNameShow;
	}

	public void setPureNameShow(String pureNameShow) {
		this.pureNameShow = pureNameShow;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getDoubanScoreCalc() {
		return doubanScoreCalc;
	}

	public void setDoubanScoreCalc(String doubanScoreCalc) {
		this.doubanScoreCalc = doubanScoreCalc;
	}

	public String getOptimalResourceId() {
		return optimalResourceId;
	}

	public void setOptimalResourceId(String optimalResourceId) {
		this.optimalResourceId = optimalResourceId;
	}

	public String[] getLabelsSplit() {
		return labelsSplit;
	}

	public void setLabelsSplit(String[] labelsSplit) {
		this.labelsSplit = labelsSplit;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public String getAnotherName() {
		return anotherName;
	}

	public void setAnotherName(String anotherName) {
		this.anotherName = anotherName;
	}

	public String getReleaseTimeStr() {
		return releaseTimeStr;
	}

	public void setReleaseTimeStr(String releaseTimeStr) {
		this.releaseTimeStr = releaseTimeStr;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public String getReleaseTimeFormat() {
		return releaseTimeFormat;
	}

	public void setReleaseTimeFormat(String releaseTimeFormat) {
		this.releaseTimeFormat = releaseTimeFormat;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId == null ? null : movieId.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getPureName() {
        return pureName;
    }

    public void setPureName(String pureName) {
        this.pureName = pureName == null ? null : pureName.trim();
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director == null ? null : director.trim();
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers == null ? null : writers.trim();
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast == null ? null : cast.trim();
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations == null ? null : locations.trim();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration == null ? null : duration.trim();
    }

    public String getDoubanId() {
        return doubanId;
    }

    public void setDoubanId(String doubanId) {
        this.doubanId = doubanId == null ? null : doubanId.trim();
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId == null ? null : imdbId.trim();
    }

    public Double getDoubanScore() {
        return doubanScore;
    }

    public void setDoubanScore(Double doubanScore) {
        this.doubanScore = doubanScore;
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

	public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
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
    
    public static void parse(List<MovieInfoEntity> list){
    	for(MovieInfoEntity entity : list){
    		entity.parse();
    	}
    }
    
    public MovieInfoEntity changeOptionManual(MovieInfoEntity modiMovie){
    	MovieInfoEntity mie = new MovieInfoEntity();
    	boolean hasChange = false;
    	
    	if(StringUtils.isNotBlank(modiMovie.getPureName())){
    		if(StringUtils.isBlank(this.getPureName()) || !this.getPureName().equals(modiMovie.getPureName())){
    			mie.setPureName(modiMovie.getPureName());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiMovie.getAnotherName())){
    		if(StringUtils.isBlank(this.getAnotherName()) || !this.getAnotherName().equals(modiMovie.getAnotherName())){
    			mie.setAnotherName(modiMovie.getAnotherName());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiMovie.getDirector())){
    		if(StringUtils.isBlank(this.getDirector()) || !this.getDirector().equals(modiMovie.getDirector())){
    			mie.setDirector(modiMovie.getDirector());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiMovie.getWriters())){
    		if(StringUtils.isBlank(this.getWriters()) || !this.getWriters().equals(modiMovie.getWriters())){
    			mie.setWriters(modiMovie.getWriters());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiMovie.getCast())){
    		if(StringUtils.isBlank(this.getCast()) || !this.getCast().equals(modiMovie.getCast())){
    			mie.setCast(modiMovie.getCast());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiMovie.getLanguage())){
    		if(StringUtils.isBlank(this.getLanguage()) || !this.getLanguage().equals(modiMovie.getLanguage())){
    			mie.setLanguage(modiMovie.getLanguage());
    			hasChange = true;
    		}
    	}
    	if(modiMovie.getReleaseTime() != null){
        	if(this.getReleaseTime() == null || !this.getReleaseTime().equals(modiMovie.getReleaseTime())){
    			mie.setReleaseTime(modiMovie.getReleaseTime());
        		mie.setReleaseTimeFormat(modiMovie.getReleaseTimeFormat());
        		mie.setReleaseTimeStr(modiMovie.getReleaseTimeStr());
    			hasChange = true;
        	}
    	}
    	
    	if(StringUtils.isNotBlank(modiMovie.getYear())){
    		if(StringUtils.isBlank(this.getYear()) || !this.getYear().equals(modiMovie.getYear())){
    			mie.setYear(modiMovie.getYear());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiMovie.getDuration())){
    		if(StringUtils.isBlank(this.getDuration()) || !this.getDuration().equals(modiMovie.getDuration())){
    			mie.setDuration(modiMovie.getDuration());
    			hasChange = true;
    		}
    	}
    	
    	if(modiMovie.getCategory() != null){
    		if(this.getCategory() == null || this.getCategory().intValue() != modiMovie.getCategory().intValue()){
    			mie.setCategory(modiMovie.getCategory());
    			hasChange = true;
    		}
    	}
    	
    	if(modiMovie.getPresentSeason() != null){
    		if(this.getPresentSeason() == null || this.getPresentSeason().intValue() != modiMovie.getPresentSeason().intValue()){
    			mie.setPresentSeason(modiMovie.getPresentSeason());
    			hasChange = true;
    		}
    	}
    	
    	if(modiMovie.getTotalEpisode() != null){
    		if(this.getTotalEpisode() == null || this.getTotalEpisode().intValue() != modiMovie.getTotalEpisode().intValue()){
    			mie.setTotalEpisode(modiMovie.getTotalEpisode());
    			hasChange = true;
    		}
    	}
    	
    	if(modiMovie.getDoubanScore() != null){
    		if(this.getDoubanScore() == null || this.getDoubanScore().doubleValue() != modiMovie.getDoubanScore().doubleValue()){
    			mie.setDoubanScore(modiMovie.getDoubanScore());
    			hasChange = true;
    		}
    	}
    	
    	if(modiMovie.getAttentionRate() != null){
    		if(this.getAttentionRate() == null || this.getAttentionRate().intValue() != modiMovie.getAttentionRate().intValue()){
    			mie.setAttentionRate(modiMovie.getAttentionRate());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiMovie.getSummary())){
    		if(StringUtils.isBlank(this.getSummary()) || !this.getSummary().equals(modiMovie.getSummary())){
    			mie.setSummary(modiMovie.getSummary());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiMovie.getBusIcon())){
    		if(StringUtils.isBlank(this.getBusIcon()) || !this.getBusIcon().equals(modiMovie.getBusIcon())){
    			mie.setBusIcon(modiMovie.getBusIcon());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiMovie.getBusPoster())){
    		if(StringUtils.isBlank(this.getBusPoster()) || !this.getBusPoster().equals(modiMovie.getBusPoster())){
    			mie.setBusPoster(modiMovie.getBusPoster());
    			hasChange = true;
    		}
    	}
    	
    	if(hasChange){
    		mie.setMovieId(this.getMovieId());
    		mie.setUpdateTime(new Date());
    		return mie;
    	}
    	return null;
    }
    
}