package com.sbolo.syk.fetch.vo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.enums.MovieCategoryEnum;
import com.sbolo.syk.common.tools.ConfigUtils;
import com.sbolo.syk.common.tools.StringUtil;

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
    
    private String[] anotherNameArr;

    private String showName;
    
    /**
     * 多个label，逗号间隔
     */
    private String labels;
    
    private String[] labelArr;
    
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
    
    private String[] languageArr;

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
    
    private String summaryShow;

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
    
    private String tag;
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
    
    private ResourceInfoVO optimalResource;
    
    private Integer action;  //Business property, that will direct 'insert' or 'update' or 'abandon'.
    
    private String posterPageUrl;  //待去下载poster的网址
    
    private String iconUrl; //自己服务器拼装后的url
    
    private String iconOutUrl; //待下载的ICON链接
    
    private String iconSubDir; //上传后的子路径
    
    private List<String> photoOutUrlList;
    
    private String photoOutUrlStr;
    
    private List<String> photoUriList;
    
    private String photoSubDirStr;
    
    private List<String> photoUrlList;
    
    private String photoUrlStr;
    
    private List<String> posterOutUrlList;
    
    private String posterOutUrlStr;
    
    private List<String> posterUriList;
    
    private String posterSubDirStr;
    
    private List<String> posterUrlList;
    
    private String posterUrlStr;
    
    private String comeFromUrl;
    
    private List<ResourceInfoVO> resourceList;
    
    private List<MovieDictVO> dictList;

	public List<MovieDictVO> getDictList() {
		return dictList;
	}

	public void setDictList(List<MovieDictVO> dictList) {
		this.dictList = dictList;
	}

	public List<ResourceInfoVO> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<ResourceInfoVO> resourceList) {
		this.resourceList = resourceList;
	}

	public String getComeFromUrl() {
		return comeFromUrl;
	}

	public void setComeFromUrl(String comeFromUrl) {
		this.comeFromUrl = comeFromUrl;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public List<String> getPhotoUrlList() {
		return photoUrlList;
	}

	public void setPhotoUrlList(List<String> photoUrlList) {
		this.photoUrlList = photoUrlList;
	}

	public String getPhotoUrlStr() {
		return photoUrlStr;
	}

	public void setPhotoUrlStr(String photoUrlStr) {
		this.photoUrlStr = photoUrlStr;
	}

	public List<String> getPosterUrlList() {
		return posterUrlList;
	}

	public void setPosterUrlList(List<String> posterUrlList) {
		this.posterUrlList = posterUrlList;
	}

	public String getPosterUrlStr() {
		return posterUrlStr;
	}

	public void setPosterUrlStr(String posterUrlStr) {
		this.posterUrlStr = posterUrlStr;
	}

	public String getIconSubDir() {
		return iconSubDir;
	}

	public void setIconSubDir(String iconSubDir) {
		this.iconSubDir = iconSubDir;
	}

	public List<String> getPhotoOutUrlList() {
		return photoOutUrlList;
	}

	public void setPhotoOutUrlList(List<String> photoOutUrlList) {
		this.photoOutUrlList = photoOutUrlList;
	}

	public String getPhotoOutUrlStr() {
		return photoOutUrlStr;
	}

	public void setPhotoOutUrlStr(String photoOutUrlStr) {
		this.photoOutUrlStr = photoOutUrlStr;
	}

	public String getPhotoSubDirStr() {
		return photoSubDirStr;
	}

	public void setPhotoSubDirStr(String photoSubDirStr) {
		this.photoSubDirStr = photoSubDirStr;
	}

	public List<String> getPosterOutUrlList() {
		return posterOutUrlList;
	}

	public void setPosterOutUrlList(List<String> posterOutUrlList) {
		this.posterOutUrlList = posterOutUrlList;
	}

	public String getPosterOutUrlStr() {
		return posterOutUrlStr;
	}

	public void setPosterOutUrlStr(String posterOutUrlStr) {
		this.posterOutUrlStr = posterOutUrlStr;
	}

	public String getPosterSubDirStr() {
		return posterSubDirStr;
	}

	public void setPosterSubDirStr(String posterSubDirStr) {
		this.posterSubDirStr = posterSubDirStr;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getIconOutUrl() {
		return iconOutUrl;
	}

	public void setIconOutUrl(String iconOutUrl) {
		this.iconOutUrl = iconOutUrl;
	}
    
	public ResourceInfoVO getOptimalResource() {
		return optimalResource;
	}

	public void setOptimalResource(ResourceInfoVO optimalResource) {
		this.optimalResource = optimalResource;
	}

	public String getSummaryShow() {
		return summaryShow;
	}

	public void setSummaryShow(String summaryShow) {
		this.summaryShow = summaryShow;
	}

	public String[] getAnotherNameArr() {
		return anotherNameArr;
	}

	public void setAnotherNameArr(String[] anotherNameArr) {
		this.anotherNameArr = anotherNameArr;
	}

	public String[] getLabelArr() {
		return labelArr;
	}

	public void setLabelArr(String[] labelArr) {
		this.labelArr = labelArr;
	}

	public String[] getLanguageArr() {
		return languageArr;
	}

	public void setLanguageArr(String[] languageArr) {
		this.languageArr = languageArr;
	}

	public String getDoubanScoreCalc() {
		return doubanScoreCalc;
	}

	public void setDoubanScoreCalc(String doubanScoreCalc) {
		this.doubanScoreCalc = doubanScoreCalc;
	}

	public String getStDescp() {
		return stDescp;
	}

	public void setStDescp(String stDescp) {
		this.stDescp = stDescp;
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

	public String getResourceWriteTimeStr() {
		return resourceWriteTimeStr;
	}

	public void setResourceWriteTimeStr(String resourceWriteTimeStr) {
		this.resourceWriteTimeStr = resourceWriteTimeStr;
	}

	public List<String> getPhotoUriList() {
		return photoUriList;
	}

	public void setPhotoUriList(List<String> photoUriList) {
		this.photoUriList = photoUriList;
	}

	public List<String> getPosterUriList() {
		return posterUriList;
	}

	public void setPosterUriList(List<String> posterUriList) {
		this.posterUriList = posterUriList;
	}

	public String getPhotoUriJson() {
		return photoUriJson;
	}

	public void setPhotoUriJson(String photoUriJson) {
		this.photoUriJson = photoUriJson;
	}

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public String getReleaseTimeStr() {
		return releaseTimeStr;
	}

	public void setReleaseTimeStr(String releaseTimeStr) {
		this.releaseTimeStr = releaseTimeStr;
	}

	public String getPosterPageUrl() {
		return posterPageUrl;
	}

	public void setPosterPageUrl(String posterPageUrl) {
		this.posterPageUrl = posterPageUrl;
	}

    /**
     * 获取全局唯一标识
     *
     * 
     */
    public String getPrn() {
        return prn;
    }

    /**
     * 设置全局唯一标识
     *
     * 
     */
    public void setPrn(String prn) {
        this.prn = prn;
    }

    /**
     * 获取图标uri
     *
     * 
     */
    public String getIconUri() {
        return iconUri;
    }

    /**
     * 设置图标uri
     *
     * 
     */
    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }


    public String getPosterUriJson() {
		return posterUriJson;
	}

	public void setPosterUriJson(String posterUriJson) {
		this.posterUriJson = posterUriJson;
	}

	/**
     * 获取电影名
     *
     * 
     */
    public String getPureName() {
        return pureName;
    }

    /**
     * 设置电影名
     *
     * 
     */
    public void setPureName(String pureName) {
        this.pureName = pureName;
    }

    /**
     * 获取别名
     *
     * 
     */
    public String getAnotherName() {
        return anotherName;
    }

    /**
     * 设置别名
     *
     * 
     */
    public void setAnotherName(String anotherName) {
        this.anotherName = anotherName;
    }

    /**
     * 获取多个label，逗号间隔
     *
     * 
     */
    public String getLabels() {
        return labels;
    }

    /**
     * 设置多个label，逗号间隔
     *
     * 
     */
    public void setLabels(String labels) {
        this.labels = labels;
    }

    /**
     * 获取多个导演，逗号间隔
     *
     * 
     */
    public String getDirectors() {
        return directors;
    }

    /**
     * 设置多个导演，逗号间隔
     *
     * 
     */
    public void setDirectors(String directors) {
        this.directors = directors;
    }

    /**
     * 获取多个编剧，逗号间隔
     *
     * 
     */
    public String getWriters() {
        return writers;
    }

    /**
     * 设置多个编剧，逗号间隔
     *
     * 
     */
    public void setWriters(String writers) {
        this.writers = writers;
    }

    /**
     * 获取多个演员，逗号间隔
     *
     * 
     */
    public String getCasts() {
        return casts;
    }

    /**
     * 设置多个演员，逗号间隔
     *
     * 
     */
    public void setCasts(String casts) {
        this.casts = casts;
    }

    /**
     * 获取多个制片国家/地区，逗号间隔
     *
     * 
     */
    public String getLocations() {
        return locations;
    }

    /**
     * 设置多个制片国家/地区，逗号间隔
     *
     * 
     */
    public void setLocations(String locations) {
        this.locations = locations;
    }

    /**
     * 获取多个电影语言，逗号间隔
     *
     * 
     */
    public String getLanguages() {
        return languages;
    }

    /**
     * 设置多个电影语言，逗号间隔
     *
     * 
     */
    public void setLanguages(String languages) {
        this.languages = languages;
    }

    /**
     * 获取上映时间
     *
     * 
     */
    public Date getReleaseTime() {
        return releaseTime;
    }

    /**
     * 设置上映时间
     *
     * 
     */
    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    /**
     * 获取上映时间的格式
     *
     * 
     */
    public String getReleaseTimeFormat() {
        return releaseTimeFormat;
    }

    /**
     * 设置上映时间的格式
     *
     * 
     */
    public void setReleaseTimeFormat(String releaseTimeFormat) {
        this.releaseTimeFormat = releaseTimeFormat;
    }

    /**
     * 获取上映年代
     *
     * 
     */
    public String getYear() {
        return year;
    }

    /**
     * 设置上映年代
     *
     * 
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * 获取电影时长
     *
     * 
     */
    public String getDuration() {
        return duration;
    }

    /**
     * 设置电影时长
     *
     * 
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * 获取剧情概要
     *
     * 
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 设置剧情概要
     *
     * 
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * 获取豆瓣ID
     *
     * 
     */
    public String getDoubanId() {
        return doubanId;
    }

    /**
     * 设置豆瓣ID
     *
     * 
     */
    public void setDoubanId(String doubanId) {
        this.doubanId = doubanId;
    }

    /**
     * 获取IMDB ID
     *
     * 
     */
    public String getImdbId() {
        return imdbId;
    }

    /**
     * 设置IMDB ID
     *
     * 
     */
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
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

	/**
     * 获取关注度
     *
     * 
     */
    public Integer getAttentionRate() {
        return attentionRate;
    }

    /**
     * 设置关注度
     *
     * 
     */
    public void setAttentionRate(Integer attentionRate) {
        this.attentionRate = attentionRate;
    }

    /**
     * 获取类别 tv/movie
     *
     * 
     */
    public Integer getCategory() {
        return category;
    }

    /**
     * 设置类别 tv/movie
     *
     * 
     */
    public void setCategory(Integer category) {
        this.category = category;
    }

    /**
     * 获取当前第几季
     *
     * 
     */
    public Integer getPresentSeason() {
        return presentSeason;
    }

    /**
     * 设置当前第几季
     *
     * 
     */
    public void setPresentSeason(Integer presentSeason) {
        this.presentSeason = presentSeason;
    }

    /**
     * 获取总集数
     *
     * 
     */
    public Integer getTotalEpisode() {
        return totalEpisode;
    }

    /**
     * 设置总集数
     *
     * 
     */
    public void setTotalEpisode(Integer totalEpisode) {
        this.totalEpisode = totalEpisode;
    }

    /**
     * 获取状态 1---可用 2---可删除
     *
     * 
     */
    public Integer getSt() {
        return st;
    }

    /**
     * 设置状态 1---可用 2---可删除
     *
     * 
     */
    public void setSt(Integer st) {
        this.st = st;
    }

    /**
     * 获取创建时间
     *
     * 
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * 
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * 
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * 
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取资源写入时间
     *
     * 
     */
    public Date getResourceWriteTime() {
        return resourceWriteTime;
    }

    /**
     * 设置资源写入时间
     *
     * 
     */
    public void setResourceWriteTime(Date resourceWriteTime) {
        this.resourceWriteTime = resourceWriteTime;
    }

    /**
     * 获取最佳资源的prn
     *
     * 
     */
    public String getOptimalResourcePrn() {
        return optimalResourcePrn;
    }

    /**
     * 设置最佳资源的prn
     *
     * 
     */
    public void setOptimalResourcePrn(String optimalResourcePrn) {
        this.optimalResourcePrn = optimalResourcePrn;
    }

    /**
     * 获取点击次数
     *
     * 
     */
    public Integer getCountClick() {
        return countClick;
    }

    /**
     * 设置点击次数
     *
     * 
     */
    public void setCountClick(Integer countClick) {
        this.countClick = countClick;
    }

    /**
     * 获取评论次数
     *
     * 
     */
    public Integer getCountComment() {
        return countComment;
    }

    /**
     * 设置评论次数
     *
     * 
     */
    public void setCountComment(Integer countComment) {
        this.countComment = countComment;
    }

    /**
     * 获取下载次数
     *
     * 
     */
    public Integer getCountDownload() {
        return countDownload;
    }

    /**
     * 设置下载次数
     *
     * 
     */
    public void setCountDownload(Integer countDownload) {
        this.countDownload = countDownload;
    }
    
    public void parse(){
    	SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.timeFormat.get(10));
    	String bucketHost = ConfigUtils.getPropertyValue("bucket.host");
    	
    	if(this.getCategory() == MovieCategoryEnum.tv.getCode()) {
    		if(this.getOptimalResource() != null && this.getOptimalResource().getEpisodeEnd() != null) {
    			this.setShowName(this.getPureName() + " 更新至" + this.getOptimalResource().getEpisodeEnd() + "集");
    		}
    	}else {
    		this.setShowName(this.getPureName());
    	}
    	
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
    		String posterUrlStr = "";
    		for(String posterUri : posterUris){
    			String posterUrl = bucketHost + posterUri;
    			posterUrlList.add(posterUrl);
    			posterUrlStr += ","+posterUrl;
    		}
    		this.setPosterUrlList(posterUrlList);
    		this.setPosterUrlStr(posterUrlStr.substring(1));
    	}
    	
    	if(StringUtils.isNotBlank(this.getPhotoUriJson())){
    		List<String> photoUris = JSON.parseArray(this.getPhotoUriJson(), String.class);
    		List<String> photoUrlList = new ArrayList<String>();
    		String photoUrlStr = "";
    		for(String photoUri : photoUris){
    			String photoUrl = bucketHost + photoUri;
    			photoUrlList.add(photoUrl);
    			photoUrlStr += ","+photoUrl;
    		}
    		this.setPhotoUrlList(photoUrlList);
    		this.setPhotoUrlStr(photoUrlStr.substring(1));
    	}
    	
    	if(StringUtils.isNotBlank(this.getAnotherName())){
    		this.setAnotherNameArr(this.getAnotherName().split(RegexConstant.slashSep));
    	}
    	
    	if(StringUtils.isNotBlank(this.getLabels())){
    		this.setLabelArr(this.getLabels().split(RegexConstant.slashSep));
    	}
    	
    	if(StringUtils.isNotBlank(this.getLanguages())){
    		this.setLanguageArr(this.getLanguages().split(RegexConstant.slashSep));
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
    
    public static void parse(List<MovieInfoVO> list){
    	for(MovieInfoVO vo : list){
    		vo.parse();
    	}
    }
    
    
}