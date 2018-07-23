package com.sbolo.syk.fetch.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Table(name = "`movie_info`")
public class MovieInfoEntity {
    /**
     * 自增id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 全局唯一标识
     */
    @Column(name = "`prn`")
    private String prn;

    /**
     * 图标uri
     */
    @Column(name = "`icon_uri`")
    private String iconUri;

    /**
     * 多个posterUri组成的json
     */
    @Column(name = "`poster_uri_json`")
    private String posterUriJson;
    
    @Column(name = "`photo_uri_json`")
    private String photoUriJson;

    /**
     * 电影名
     */
    @Column(name = "`pure_name`")
    private String pureName;

    /**
     * 别名
     */
    @Column(name = "`another_name`")
    private String anotherName;

    /**
     * 多个label，逗号间隔
     */
    @Column(name = "`labels`")
    private String labels;
    
    /**
     * 多个导演，逗号间隔
     */
    @Column(name = "`directors`")
    private String directors;

    /**
     * 多个编剧，逗号间隔
     */
    @Column(name = "`writers`")
    private String writers;

    /**
     * 多个演员，逗号间隔
     */
    @Column(name = "`casts`")
    private String casts;

    /**
     * 多个制片国家/地区，逗号间隔
     */
    @Column(name = "`locations`")
    private String locations;
    
    /**
     * 多个电影语言，逗号间隔
     */
    @Column(name = "`languages`")
    private String languages;

    /**
     * 上映时间
     */
    @Column(name = "`release_time`")
    private Date releaseTime;

    /**
     * 上映时间的格式
     */
    @Column(name = "`release_time_format`")
    private String releaseTimeFormat;

    /**
     * 上映年代
     */
    @Column(name = "`year`")
    private String year;

    /**
     * 电影时长
     */
    @Column(name = "`duration`")
    private String duration;

    /**
     * 剧情概要
     */
    @Column(name = "`summary`")
    private String summary;

    /**
     * 豆瓣ID
     */
    @Column(name = "`douban_id`")
    private String doubanId;

    /**
     * IMDB ID
     */
    @Column(name = "`imdb_id`")
    private String imdbId;

    /**
     * 豆瓣评分
     */
    @Column(name = "`douban_score`")
    private Double doubanScore;

    /**
     * IMDB评分
     */
    @Column(name = "`imdb_score`")
    private Double imdbScore;

    /**
     * 关注度
     */
    @Column(name = "`attention_rate`")
    private Integer attentionRate;

    /**
     * 类别 tv/movie
     */
    @Column(name = "`category`")
    private Integer category;

    /**
     * 当前第几季
     */
    @Column(name = "`present_season`")
    private Integer presentSeason;

    /**
     * 总集数
     */
    @Column(name = "`total_episode`")
    private Integer totalEpisode;

    /**
     * 状态 1---可用 2---可删除
     */
    @Column(name = "`st`")
    private Integer st;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;

    /**
     * 资源写入时间
     */
    @Column(name = "`resource_write_time`")
    private Date resourceWriteTime;

    /**
     * 最佳资源的prn
     */
    @Column(name = "`optimal_resource_prn`")
    private String optimalResourcePrn;

    /**
     * 点击次数
     */
    @Column(name = "`count_click`")
    private Integer countClick;

    /**
     * 评论次数
     */
    @Column(name = "`count_comment`")
    private Integer countComment;

    /**
     * 下载次数
     */
    @Column(name = "`count_download`")
    private Integer countDownload;

	public String getPhotoUriJson() {
		return photoUriJson;
	}

	public void setPhotoUriJson(String photoUriJson) {
		this.photoUriJson = photoUriJson;
	}

	/**
     * 获取自增id
     *
     * @return id - 自增id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增id
     *
     * @param id 自增id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取全局唯一标识
     *
     * @return prn - 全局唯一标识
     */
    public String getPrn() {
        return prn;
    }

    /**
     * 设置全局唯一标识
     *
     * @param prn 全局唯一标识
     */
    public void setPrn(String prn) {
        this.prn = prn;
    }

    /**
     * 获取图标uri
     *
     * @return icon_uri - 图标uri
     */
    public String getIconUri() {
        return iconUri;
    }

    /**
     * 设置图标uri
     *
     * @param iconUri 图标uri
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
     * @return pure_name - 电影名
     */
    public String getPureName() {
        return pureName;
    }

    /**
     * 设置电影名
     *
     * @param pureName 电影名
     */
    public void setPureName(String pureName) {
        this.pureName = pureName;
    }

    /**
     * 获取别名
     *
     * @return another_name - 别名
     */
    public String getAnotherName() {
        return anotherName;
    }

    /**
     * 设置别名
     *
     * @param anotherName 别名
     */
    public void setAnotherName(String anotherName) {
        this.anotherName = anotherName;
    }

    /**
     * 获取多个label，逗号间隔
     *
     * @return labels - 多个label，逗号间隔
     */
    public String getLabels() {
        return labels;
    }

    /**
     * 设置多个label，逗号间隔
     *
     * @param labels 多个label，逗号间隔
     */
    public void setLabels(String labels) {
        this.labels = labels;
    }

    /**
     * 获取多个导演，逗号间隔
     *
     * @return directors - 多个导演，逗号间隔
     */
    public String getDirectors() {
        return directors;
    }

    /**
     * 设置多个导演，逗号间隔
     *
     * @param directors 多个导演，逗号间隔
     */
    public void setDirectors(String directors) {
        this.directors = directors;
    }

    /**
     * 获取多个编剧，逗号间隔
     *
     * @return writers - 多个编剧，逗号间隔
     */
    public String getWriters() {
        return writers;
    }

    /**
     * 设置多个编剧，逗号间隔
     *
     * @param writers 多个编剧，逗号间隔
     */
    public void setWriters(String writers) {
        this.writers = writers;
    }

    /**
     * 获取多个演员，逗号间隔
     *
     * @return casts - 多个演员，逗号间隔
     */
    public String getCasts() {
        return casts;
    }

    /**
     * 设置多个演员，逗号间隔
     *
     * @param casts 多个演员，逗号间隔
     */
    public void setCasts(String casts) {
        this.casts = casts;
    }

    /**
     * 获取多个制片国家/地区，逗号间隔
     *
     * @return locations - 多个制片国家/地区，逗号间隔
     */
    public String getLocations() {
        return locations;
    }

    /**
     * 设置多个制片国家/地区，逗号间隔
     *
     * @param locations 多个制片国家/地区，逗号间隔
     */
    public void setLocations(String locations) {
        this.locations = locations;
    }

    /**
     * 获取多个电影语言，逗号间隔
     *
     * @return languages - 多个电影语言，逗号间隔
     */
    public String getLanguages() {
        return languages;
    }

    /**
     * 设置多个电影语言，逗号间隔
     *
     * @param languages 多个电影语言，逗号间隔
     */
    public void setLanguages(String languages) {
        this.languages = languages;
    }

    /**
     * 获取上映时间
     *
     * @return release_time - 上映时间
     */
    public Date getReleaseTime() {
        return releaseTime;
    }

    /**
     * 设置上映时间
     *
     * @param releaseTime 上映时间
     */
    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    /**
     * 获取上映时间的格式
     *
     * @return release_time_format - 上映时间的格式
     */
    public String getReleaseTimeFormat() {
        return releaseTimeFormat;
    }

    /**
     * 设置上映时间的格式
     *
     * @param releaseTimeFormat 上映时间的格式
     */
    public void setReleaseTimeFormat(String releaseTimeFormat) {
        this.releaseTimeFormat = releaseTimeFormat;
    }

    /**
     * 获取上映年代
     *
     * @return year - 上映年代
     */
    public String getYear() {
        return year;
    }

    /**
     * 设置上映年代
     *
     * @param year 上映年代
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * 获取电影时长
     *
     * @return duration - 电影时长
     */
    public String getDuration() {
        return duration;
    }

    /**
     * 设置电影时长
     *
     * @param duration 电影时长
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * 获取剧情概要
     *
     * @return summary - 剧情概要
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 设置剧情概要
     *
     * @param summary 剧情概要
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * 获取豆瓣ID
     *
     * @return douban_id - 豆瓣ID
     */
    public String getDoubanId() {
        return doubanId;
    }

    /**
     * 设置豆瓣ID
     *
     * @param doubanId 豆瓣ID
     */
    public void setDoubanId(String doubanId) {
        this.doubanId = doubanId;
    }

    /**
     * 获取IMDB ID
     *
     * @return imdb_id - IMDB ID
     */
    public String getImdbId() {
        return imdbId;
    }

    /**
     * 设置IMDB ID
     *
     * @param imdbId IMDB ID
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
     * @return attention_rate - 关注度
     */
    public Integer getAttentionRate() {
        return attentionRate;
    }

    /**
     * 设置关注度
     *
     * @param attentionRate 关注度
     */
    public void setAttentionRate(Integer attentionRate) {
        this.attentionRate = attentionRate;
    }

    /**
     * 获取类别 tv/movie
     *
     * @return category - 类别 tv/movie
     */
    public Integer getCategory() {
        return category;
    }

    /**
     * 设置类别 tv/movie
     *
     * @param category 类别 tv/movie
     */
    public void setCategory(Integer category) {
        this.category = category;
    }

    /**
     * 获取当前第几季
     *
     * @return present_season - 当前第几季
     */
    public Integer getPresentSeason() {
        return presentSeason;
    }

    /**
     * 设置当前第几季
     *
     * @param presentSeason 当前第几季
     */
    public void setPresentSeason(Integer presentSeason) {
        this.presentSeason = presentSeason;
    }

    /**
     * 获取总集数
     *
     * @return total_episode - 总集数
     */
    public Integer getTotalEpisode() {
        return totalEpisode;
    }

    /**
     * 设置总集数
     *
     * @param totalEpisode 总集数
     */
    public void setTotalEpisode(Integer totalEpisode) {
        this.totalEpisode = totalEpisode;
    }

    /**
     * 获取状态 1---可用 2---可删除
     *
     * @return st - 状态 1---可用 2---可删除
     */
    public Integer getSt() {
        return st;
    }

    /**
     * 设置状态 1---可用 2---可删除
     *
     * @param st 状态 1---可用 2---可删除
     */
    public void setSt(Integer st) {
        this.st = st;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取资源写入时间
     *
     * @return resource_write_time - 资源写入时间
     */
    public Date getResourceWriteTime() {
        return resourceWriteTime;
    }

    /**
     * 设置资源写入时间
     *
     * @param resourceWriteTime 资源写入时间
     */
    public void setResourceWriteTime(Date resourceWriteTime) {
        this.resourceWriteTime = resourceWriteTime;
    }

    /**
     * 获取最佳资源的prn
     *
     * @return optimal_resource_prn - 最佳资源的prn
     */
    public String getOptimalResourcePrn() {
        return optimalResourcePrn;
    }

    /**
     * 设置最佳资源的prn
     *
     * @param optimalResourcePrn 最佳资源的prn
     */
    public void setOptimalResourcePrn(String optimalResourcePrn) {
        this.optimalResourcePrn = optimalResourcePrn;
    }

    /**
     * 获取点击次数
     *
     * @return count_click - 点击次数
     */
    public Integer getCountClick() {
        return countClick;
    }

    /**
     * 设置点击次数
     *
     * @param countClick 点击次数
     */
    public void setCountClick(Integer countClick) {
        this.countClick = countClick;
    }

    /**
     * 获取评论次数
     *
     * @return count_comment - 评论次数
     */
    public Integer getCountComment() {
        return countComment;
    }

    /**
     * 设置评论次数
     *
     * @param countComment 评论次数
     */
    public void setCountComment(Integer countComment) {
        this.countComment = countComment;
    }

    /**
     * 获取下载次数
     *
     * @return count_download - 下载次数
     */
    public Integer getCountDownload() {
        return countDownload;
    }

    /**
     * 设置下载次数
     *
     * @param countDownload 下载次数
     */
    public void setCountDownload(Integer countDownload) {
        this.countDownload = countDownload;
    }
}