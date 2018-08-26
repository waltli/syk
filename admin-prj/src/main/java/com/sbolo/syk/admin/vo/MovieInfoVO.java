package com.sbolo.syk.admin.vo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.tools.ConfigUtil;

public class MovieInfoVO {

    /**
     * 全局唯一标识
     */
    private String prn;

    /**
     * 图标uri
     */
    private String iconUri;

    /**
     * 多个posterUri组成的json
     */
    private String posterUriJson;
    
    
    private String photoUriJson;

    /**
     * 电影名
     */
    private String pureName;

    /**
     * 别名
     */
    private String anotherName;
    
    /**
     * 多个label，逗号间隔
     */
    private String labels;
    
    /**
     * 多个导演，逗号间隔
     */
    private String directors;

    /**
     * 多个编剧，逗号间隔
     */
    private String writers;

    /**
     * 多个演员，逗号间隔
     */
    private String casts;

    /**
     * 多个制片国家/地区，逗号间隔
     */
    private String locations;

    /**
     * 多个电影语言，逗号间隔
     */
    private String languages;
    
    /**
     * 上映时间
     */
    private Date releaseTime;
    
    private String releaseTimeStr;

    /**
     * 上映时间的格式
     */
    private String releaseTimeFormat;

    /**
     * 上映年代
     */
    private String year;

    /**
     * 电影时长
     */
    private String duration;

    /**
     * 剧情概要
     */
    private String summary;
    
    /**
     * 豆瓣ID
     */
    private String doubanId;

    /**
     * IMDB ID
     */
    private String imdbId;

    /**
     * 豆瓣评分
     */
    private Double doubanScore;
    
    private String doubanScoreCalc;

    /**
     * IMDB评分
     */
    private Double imdbScore;

    /**
     * 关注度
     */
    private Integer attentionRate;

    /**
     * 类别 tv/movie
     */
    private Integer category;

    /**
     * 当前第几季
     */
    private Integer presentSeason;

    /**
     * 总集数
     */
    private Integer totalEpisode;

    /**
     * 状态 1---可用 2---可删除
     */
    private Integer st;
    
    private String stDescp;

    /**
     * 创建时间
     */
    private Date createTime;
    
    private String createTimeStr;

    /**
     * 更新时间
     */
    private Date updateTime;
    
    private String updateTimeStr;

    /**
     * 资源写入时间
     */
    private Date resourceWriteTime;
    
    private String resourceWriteTimeStr;

    /**
     * 最佳资源的prn
     */
    private String optimalResourcePrn;

    /**
     * 点击次数
     */
    private Integer countClick;

    /**
     * 评论次数
     */
    private Integer countComment;

    /**
     * 下载次数
     */
    private Integer countDownload;
    
    private String posterPageUrl;  //待去下载poster的网址
    
    private String iconUrl; //待下载的ICON链接
    
    private String iconTempUri; //上传后的临时uri
    
    private List<String> photoUrlList;
    
    private List<String> posterUrlList;
    
    private String posterTempUriStr;
    
    private String photoTempUriStr;
    
    public String getPosterPageUrl() {
		return posterPageUrl;
	}

	public void setPosterPageUrl(String posterPageUrl) {
		this.posterPageUrl = posterPageUrl;
	}

	private Integer action;  //Business property, that will direct 'insert' or 'update' or 'abandon'.
    
    private ResourceInfoVO optimalResource;
    
    public String getPhotoTempUriStr() {
		return photoTempUriStr;
	}

	public void setPhotoTempUriStr(String photoTempUriStr) {
		this.photoTempUriStr = photoTempUriStr;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public List<String> getPhotoUrlList() {
		return photoUrlList;
	}

	public void setPhotoUrlList(List<String> photoUrlList) {
		this.photoUrlList = photoUrlList;
	}

	public List<String> getPosterUrlList() {
		return posterUrlList;
	}

	public void setPosterUrlList(List<String> posterUrlList) {
		this.posterUrlList = posterUrlList;
	}

	public ResourceInfoVO getOptimalResource() {
		return optimalResource;
	}

	public void setOptimalResource(ResourceInfoVO optimalResource) {
		this.optimalResource = optimalResource;
	}

	public String getIconTempUri() {
		return iconTempUri;
	}

	public void setIconTempUri(String iconTempUri) {
		this.iconTempUri = iconTempUri;
	}

	public String getPosterTempUriStr() {
		return posterTempUriStr;
	}

	public void setPosterTempUriStr(String posterTempUriStr) {
		this.posterTempUriStr = posterTempUriStr;
	}

	public String getPrn() {
		return prn;
	}

	public void setPrn(String prn) {
		this.prn = prn;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public String getPosterUriJson() {
		return posterUriJson;
	}

	public void setPosterUriJson(String posterUriJson) {
		this.posterUriJson = posterUriJson;
	}

	public String getPhotoUriJson() {
		return photoUriJson;
	}

	public void setPhotoUriJson(String photoUriJson) {
		this.photoUriJson = photoUriJson;
	}

	public String getPureName() {
		return pureName;
	}

	public void setPureName(String pureName) {
		this.pureName = pureName;
	}

	public String getAnotherName() {
		return anotherName;
	}

	public void setAnotherName(String anotherName) {
		this.anotherName = anotherName;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public String getDirectors() {
		return directors;
	}

	public void setDirectors(String directors) {
		this.directors = directors;
	}

	public String getWriters() {
		return writers;
	}

	public void setWriters(String writers) {
		this.writers = writers;
	}

	public String getCasts() {
		return casts;
	}

	public void setCasts(String casts) {
		this.casts = casts;
	}

	public String getLocations() {
		return locations;
	}

	public void setLocations(String locations) {
		this.locations = locations;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
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

	public String getReleaseTimeFormat() {
		return releaseTimeFormat;
	}

	public void setReleaseTimeFormat(String releaseTimeFormat) {
		this.releaseTimeFormat = releaseTimeFormat;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	public Integer getSt() {
		return st;
	}

	public void setSt(Integer st) {
		this.st = st;
	}

	public String getStDescp() {
		return stDescp;
	}

	public void setStDescp(String stDescp) {
		this.stDescp = stDescp;
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

	public String getOptimalResourcePrn() {
		return optimalResourcePrn;
	}

	public void setOptimalResourcePrn(String optimalResourcePrn) {
		this.optimalResourcePrn = optimalResourcePrn;
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

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public void parse(){
    	SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.timeFormat.get(10));
    	String bucketHost = ConfigUtil.getPropertyValue("bucket.host");
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
    	
    	if(StringUtils.isNotEmpty(this.getIconUri())){
    		this.setIconUrl(bucketHost + this.getIconUri());
    	}
    	
    	if(StringUtils.isNotBlank(this.getPosterUriJson())){
    		List<String> posterUris = JSON.parseArray(this.getPosterUriJson(), String.class);
    		List<String> posterUrlList = new ArrayList<String>();
//    		String posterUrlStr = "";
    		for(String posterUri : posterUris){
    			String posterUrl = bucketHost + posterUri;
    			posterUrlList.add(posterUrl);
//    			posterUrlStr += ","+posterUrl;
    		}
    		this.setPosterUrlList(posterUrlList);
    	}
    	
    	if(StringUtils.isNotBlank(this.getPhotoUriJson())){
    		List<String> photoUris = JSON.parseArray(this.getPhotoUriJson(), String.class);
    		List<String> photoUrlList = new ArrayList<String>();
    		for(String photoUri : photoUris){
    			String photoUrl = bucketHost + photoUri;
    			photoUrlList.add(photoUrl);
    		}
    		this.setPhotoUrlList(photoUrlList);
    	}
    	
//    	if(StringUtils.isNotBlank(this.getAnotherName())){
//    		this.setAnotherNameArr(this.getAnotherName().split(RegexConstant.slashSep));
//    	}
//    	
//    	if(StringUtils.isNotBlank(this.getLabels())){
//    		this.setLabelArr(this.getLabels().split(RegexConstant.slashSep));
//    	}
//    	
//    	if(StringUtils.isNotBlank(this.getLanguages())){
//    		this.setLanguageArr(this.getLanguages().split(RegexConstant.slashSep));
//    	}
    	
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
    	
//    	if(StringUtils.isNotBlank(this.getSummary())){
//    		
//    		String summary = Pattern.compile("(\\s|　)*<br>(\\s|　)*").matcher(this.getSummary()).replaceAll("");
//    		String summaryShow =  StringUtil.StringLimit(summary, 124);
//        	this.setSummaryShow(summaryShow);
//    	}else {
//    		this.setSummaryShow("...");
//    	}
    	
    	if(this.getOptimalResource() != null) {
    		this.getOptimalResource().parse();
    	}
    	
    }
    
    public static void parse(List<MovieInfoVO> list){
    	for(MovieInfoVO vo : list){
    		vo.parse();
    	}
    }
    
    
}