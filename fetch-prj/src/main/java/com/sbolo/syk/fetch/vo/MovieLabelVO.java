package com.sbolo.syk.fetch.vo;

import java.util.Date;
import javax.persistence.*;

import com.sbolo.syk.common.tools.UIDGenerator;


public class MovieLabelVO {

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
     * 标签名
     */
    
    private String labelName;

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
     * 获取标签名
     *
     * 
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * 设置标签名
     *
     * 
     */
    public void setLabelName(String labelName) {
        this.labelName = labelName;
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
    
    public static MovieLabelVO buildLabel(String labelName, String moviePrn, String pureName, Date releaseTime, Date thisTime) {
		MovieLabelVO label = new MovieLabelVO();
		label.setLabelName(labelName);
		label.setPrn(UIDGenerator.getUID()+"");
		label.setMoviePrn(moviePrn);
		label.setPureName(pureName);
		label.setReleaseTime(releaseTime);
		label.setCreateTime(thisTime);
		return label;
	}
}