package com.sbolo.syk.fetch.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "`movie_hot_stat`")
public class MovieHotStatEntity {
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
     * 豆瓣分数
     */
    @Column(name = "`douban_score`")
    private BigDecimal doubanScore;

    /**
     * imdb分数
     */
    @Column(name = "`imdb_score`")
    private BigDecimal imdbScore;

    /**
     * 触发方式
     */
    @Column(name = "`trigger_type`")
    private Integer triggerType;
    
    @Column(name="`trigger_ip`")
    private String triggerIp;
    
    @Column(name="`trigger_addr`")
    private String triggerAddr;

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

    public String getTriggerIp() {
		return triggerIp;
	}

	public void setTriggerIp(String triggerIp) {
		this.triggerIp = triggerIp;
	}

	public String getTriggerAddr() {
		return triggerAddr;
	}

	public void setTriggerAddr(String triggerAddr) {
		this.triggerAddr = triggerAddr;
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
     * 获取豆瓣分数
     *
     * @return douban_score - 豆瓣分数
     */
    public BigDecimal getDoubanScore() {
        return doubanScore;
    }

    /**
     * 设置豆瓣分数
     *
     * @param doubanScore 豆瓣分数
     */
    public void setDoubanScore(BigDecimal doubanScore) {
        this.doubanScore = doubanScore;
    }

    /**
     * 获取imdb分数
     *
     * @return imdb_score - imdb分数
     */
    public BigDecimal getImdbScore() {
        return imdbScore;
    }

    /**
     * 设置imdb分数
     *
     * @param imdbScore imdb分数
     */
    public void setImdbScore(BigDecimal imdbScore) {
        this.imdbScore = imdbScore;
    }

    /**
     * 获取触发方式
     *
     * @return trigger_type - 触发方式
     */
    public Integer getTriggerType() {
        return triggerType;
    }

    /**
     * 设置触发方式
     *
     * @param triggerType 触发方式
     */
    public void setTriggerType(Integer triggerType) {
        this.triggerType = triggerType;
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