package com.sbolo.syk.admin.vo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;


public class MovieHotStatVO {

    /**
     * 全局唯一标识
     */
    private String prn;

    /**
     * 电影prn
     */
    private String moviePrn;

    /**
     * 电影名
     */
    
    private String pureName;

    /**
     * 上映时间
     */
    
    private Date releaseTime;

    /**
     * 豆瓣分数
     */
    
    private BigDecimal doubanScore;

    /**
     * imdb分数
     */
    
    private BigDecimal imdbScore;

    /**
     * 触发方式
     */
    
    private Integer triggerType;

    /**
     * 创建时间
     */
    
    private Date createTime;

    /**
     * 修改时间
     */
    
    private Date updateTime;


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
     * 获取电影prn
     *
     * 
     */
    public String getMoviePrn() {
        return moviePrn;
    }

    /**
     * 设置电影prn
     *
     * 
     */
    public void setMoviePrn(String moviePrn) {
        this.moviePrn = moviePrn;
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
     * 获取豆瓣分数
     *
     * 
     */
    public BigDecimal getDoubanScore() {
        return doubanScore;
    }

    /**
     * 设置豆瓣分数
     *
     * 
     */
    public void setDoubanScore(BigDecimal doubanScore) {
        this.doubanScore = doubanScore;
    }

    /**
     * 获取imdb分数
     *
     * 
     */
    public BigDecimal getImdbScore() {
        return imdbScore;
    }

    /**
     * 设置imdb分数
     *
     * 
     */
    public void setImdbScore(BigDecimal imdbScore) {
        this.imdbScore = imdbScore;
    }

    /**
     * 获取触发方式
     *
     * 
     */
    public Integer getTriggerType() {
        return triggerType;
    }

    /**
     * 设置触发方式
     *
     * 
     */
    public void setTriggerType(Integer triggerType) {
        this.triggerType = triggerType;
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
     * 获取修改时间
     *
     * 
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * 
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}