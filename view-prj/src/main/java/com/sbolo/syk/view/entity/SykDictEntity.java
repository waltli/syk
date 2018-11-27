package com.sbolo.syk.view.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`syk_dict`")
public class SykDictEntity {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`prn`")
    private String prn;

    @Column(name = "`parent_prn`")
    private String parentPrn;

    @Column(name = "`val`")
    private String val;

    @Column(name = "`st`")
    private String st;

    @Column(name = "`tier`")
    private Integer tier;

    @Column(name = "`create_time`")
    private Date createTime;

    @Column(name = "`update_time`")
    private Date updateTime;

    @Column(name = "`remark`")
    private String remark;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return prn
     */
    public String getPrn() {
        return prn;
    }

    /**
     * @param prn
     */
    public void setPrn(String prn) {
        this.prn = prn;
    }

    /**
     * @return parent_prn
     */
    public String getParentPrn() {
        return parentPrn;
    }

    /**
     * @param parentPrn
     */
    public void setParentPrn(String parentPrn) {
        this.parentPrn = parentPrn;
    }

    /**
     * @return val
     */
    public String getVal() {
        return val;
    }

    /**
     * @param val
     */
    public void setVal(String val) {
        this.val = val;
    }

    /**
     * @return st
     */
    public String getSt() {
        return st;
    }

    /**
     * @param st
     */
    public void setSt(String st) {
        this.st = st;
    }

    /**
     * @return tier
     */
    public Integer getTier() {
        return tier;
    }

    /**
     * @param tier
     */
    public void setTier(Integer tier) {
        this.tier = tier;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}