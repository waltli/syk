package com.sbolo.syk.fetch.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`movie_dict`")
public class MovieDictEntity {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`code`")
    private String code;

    @Column(name = "`parent_code`")
    private String parentCode;
    
    @Column(name = "`root_code`")
    private String rootCode;

    @Column(name = "`val`")
    private String val;

    @Column(name = "`st`")
    private Integer st;

    @Column(name = "`tier`")
    private Integer tier;

    @Column(name = "`create_time`")
    private Date createTime;

    @Column(name = "`update_time`")
    private Date updateTime;

    @Column(name = "`remark`")
    private String remark;
    
    @Column(name = "`in_order`")
    private Integer inOrder;
    
    public Integer getInOrder() {
		return inOrder;
	}

	public void setInOrder(Integer inOrder) {
		this.inOrder = inOrder;
	}

	public String getRootCode() {
		return rootCode;
	}

	public void setRootCode(String rootCode) {
		this.rootCode = rootCode;
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

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
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

    public Integer getSt() {
		return st;
	}

	public void setSt(Integer st) {
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