package com.sbolo.syk.common.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

public class MovieInfoVOBack {

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
    
    private List<MovieLabelVO> labelList;
    
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

    private List<MovieLocationVO> locationList;
    
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 资源写入时间
     */
    private Date resourceWriteTime;

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
    
    private List<String> photoUrlList;
    
    private List<String> posterUrlList;
    
    private List<String> photoUriList;
    
    private List<String> posterUriList;
    
    private Integer action;  //Business property, that will direct 'insert' or 'update' or 'abandon'.

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

	public List<String> getPosterUrlList() {
		return posterUrlList;
	}

	public void setPosterUrlList(List<String> posterUrlList) {
		this.posterUrlList = posterUrlList;
	}

	public String getPhotoUriJson() {
		return photoUriJson;
	}

	public void setPhotoUriJson(String photoUriJson) {
		this.photoUriJson = photoUriJson;
	}

	public List<String> getPhotoUrlList() {
		return photoUrlList;
	}

	public void setPhotoUrlList(List<String> photoUrlList) {
		this.photoUrlList = photoUrlList;
	}

	public List<MovieLabelVO> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<MovieLabelVO> labelList) {
		this.labelList = labelList;
	}

	public List<MovieLocationVO> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<MovieLocationVO> locationList) {
		this.locationList = locationList;
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

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
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
}