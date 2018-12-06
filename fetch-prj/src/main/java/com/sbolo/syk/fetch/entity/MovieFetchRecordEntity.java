package com.sbolo.syk.fetch.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`movie_fetch_record`")
public class MovieFetchRecordEntity {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`prn`")
    private String prn;

    @Column(name = "`operate_type`")
    private Integer operateType;

    @Column(name = "`rely_data`")
    private String relyData;

    @Column(name = "`data_prn`")
    private String dataPrn;

    @Column(name = "`data_json`")
    private String dataJson;
    
    @Column(name = "`st`")
    private Integer st;

    @Column(name = "`create_time`")
    private Date createTime;

    @Column(name = "`update_time`")
    private Date updateTime;

    @Column(name = "`has_migrated`")
    private Boolean hasMigrated;

    public Integer getSt() {
		return st;
	}

	public void setSt(Integer st) {
		this.st = st;
	}

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
     * @return operate_type
     */
    public Integer getOperateType() {
        return operateType;
    }

    /**
     * @param operateType
     */
    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getRelyData() {
		return relyData;
	}

	public void setRelyData(String relyData) {
		this.relyData = relyData;
	}

	/**
     * @return data_prn
     */
    public String getDataPrn() {
        return dataPrn;
    }

    /**
     * @param dataPrn
     */
    public void setDataPrn(String dataPrn) {
        this.dataPrn = dataPrn;
    }

    /**
     * @return data_json
     */
    public String getDataJson() {
        return dataJson;
    }

    /**
     * @param dataJson
     */
    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
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
     * @return has_migrated
     */
    public Boolean getHasMigrated() {
        return hasMigrated;
    }

    /**
     * @param hasMigrated
     */
    public void setHasMigrated(Boolean hasMigrated) {
        this.hasMigrated = hasMigrated;
    }
}