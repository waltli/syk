package com.sbolo.syk.fetch.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`movie_label`")
public class MovieLabelEntity {
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
     * 标签名
     */
    @Column(name = "`label_name`")
    private String labelName;

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
     * 获取标签名
     *
     * @return label_name - 标签名
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * 设置标签名
     *
     * @param labelName 标签名
     */
    public void setLabelName(String labelName) {
        this.labelName = labelName;
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