package com.sbolo.syk.fetch.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`resource_info`")
public class ResourceInfoEntity {
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
     * 电影prn
     */
    @Column(name = "`movie_prn`")
    private String moviePrn;

    /**
     * 电影名
     */
    @Column(name = "`pure_name`")
    private String pureName;

    /**
     * 上映时间
     */
    @Column(name = "`release_time`")
    private Date releaseTime;

    /**
     * 文件大小
     */
    @Column(name = "`size`")
    private String size;

    /**
     * 格式
     */
    @Column(name = "`format`")
    private String format;

    /**
     * 清晰度
     */
    @Column(name = "`definition`")
    private Integer definition;

    /**
     * 影片质量-- HD  BD.....
     */
    @Column(name = "`quality`")
    private String quality;

    /**
     * 分辨率 720P 1080P 4K....
     */
    @Column(name = "`resolution`")
    private String resolution;

    /**
     * 连接速度
     */
    @Column(name = "`speed`")
    private Integer speed;

    /**
     * 下载地址
     */
    @Column(name = "`download_link`")
    private String downloadLink;

    /**
     * 第几季
     */
    @Column(name = "`season`")
    private Integer season;

    /**
     * 集数开始（如果只有一集则该字段为空）
     */
    @Column(name = "`episode_start`")
    private Integer episodeStart;

    /**
     * 集数结束（如果只有一集则则填写该字段）
     */
    @Column(name = "`episode_end`")
    private Integer episodeEnd;

    /**
     * 字幕
     */
    @Column(name = "`subtitle`")
    private String subtitle;

    /**
     * 多个资源截图uri组成的json
     */
    @Column(name = "`printscreen_uri_json`")
    private String printscreenUriJson;

    /**
     * 采集自哪个网站
     */
    @Column(name = "`come_from_url`")
    private String comeFromUrl;

    /**
     * 资源状态
     */
    @Column(name = "`st`")
    private Integer st;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;

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
     * 获取电影prn
     *
     * @return movie_prn - 电影prn
     */
    public String getMoviePrn() {
        return moviePrn;
    }

    /**
     * 设置电影prn
     *
     * @param moviePrn 电影prn
     */
    public void setMoviePrn(String moviePrn) {
        this.moviePrn = moviePrn;
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
     * 获取文件大小
     *
     * @return size - 文件大小
     */
    public String getSize() {
        return size;
    }

    /**
     * 设置文件大小
     *
     * @param size 文件大小
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * 获取格式
     *
     * @return format - 格式
     */
    public String getFormat() {
        return format;
    }

    /**
     * 设置格式
     *
     * @param format 格式
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * 获取清晰度
     *
     * @return definition - 清晰度
     */
    public Integer getDefinition() {
        return definition;
    }

    /**
     * 设置清晰度
     *
     * @param definition 清晰度
     */
    public void setDefinition(Integer definition) {
        this.definition = definition;
    }

    /**
     * 获取影片质量-- HD  BD.....
     *
     * @return quality - 影片质量-- HD  BD.....
     */
    public String getQuality() {
        return quality;
    }

    /**
     * 设置影片质量-- HD  BD.....
     *
     * @param quality 影片质量-- HD  BD.....
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * 获取分辨率 720P 1080P 4K....
     *
     * @return resolution - 分辨率 720P 1080P 4K....
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * 设置分辨率 720P 1080P 4K....
     *
     * @param resolution 分辨率 720P 1080P 4K....
     */
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    /**
     * 获取连接速度
     *
     * @return speed - 连接速度
     */
    public Integer getSpeed() {
        return speed;
    }

    /**
     * 设置连接速度
     *
     * @param speed 连接速度
     */
    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    /**
     * 获取下载地址
     *
     * @return download_link - 下载地址
     */
    public String getDownloadLink() {
        return downloadLink;
    }

    /**
     * 设置下载地址
     *
     * @param downloadLink 下载地址
     */
    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    /**
     * 获取第几季
     *
     * @return season - 第几季
     */
    public Integer getSeason() {
        return season;
    }

    /**
     * 设置第几季
     *
     * @param season 第几季
     */
    public void setSeason(Integer season) {
        this.season = season;
    }

    /**
     * 获取集数开始（如果只有一集则该字段为空）
     *
     * @return episode_start - 集数开始（如果只有一集则该字段为空）
     */
    public Integer getEpisodeStart() {
        return episodeStart;
    }

    /**
     * 设置集数开始（如果只有一集则该字段为空）
     *
     * @param episodeStart 集数开始（如果只有一集则该字段为空）
     */
    public void setEpisodeStart(Integer episodeStart) {
        this.episodeStart = episodeStart;
    }

    /**
     * 获取集数结束（如果只有一集则则填写该字段）
     *
     * @return episode_end - 集数结束（如果只有一集则则填写该字段）
     */
    public Integer getEpisodeEnd() {
        return episodeEnd;
    }

    /**
     * 设置集数结束（如果只有一集则则填写该字段）
     *
     * @param episodeEnd 集数结束（如果只有一集则则填写该字段）
     */
    public void setEpisodeEnd(Integer episodeEnd) {
        this.episodeEnd = episodeEnd;
    }

    /**
     * 获取字幕
     *
     * @return subtitle - 字幕
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * 设置字幕
     *
     * @param subtitle 字幕
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPrintscreenUriJson() {
		return printscreenUriJson;
	}

	public void setPrintscreenUriJson(String printscreenUriJson) {
		this.printscreenUriJson = printscreenUriJson;
	}

	/**
     * 获取采集自哪个网站
     *
     * @return come_from_url - 采集自哪个网站
     */
    public String getComeFromUrl() {
        return comeFromUrl;
    }

    /**
     * 设置采集自哪个网站
     *
     * @param comeFromUrl 采集自哪个网站
     */
    public void setComeFromUrl(String comeFromUrl) {
        this.comeFromUrl = comeFromUrl;
    }

    /**
     * 获取资源状态
     *
     * @return st - 资源状态
     */
    public Integer getSt() {
        return st;
    }

    /**
     * 设置资源状态
     *
     * @param st 资源状态
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
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}