package com.sbolo.syk.fetch.vo;

import java.util.Date;
import javax.persistence.*;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.MovieStatusEnum;

public class MovieDictVO {
    private Long id;

    private String code;

    private String parentCode;
    
    private String rootCode;

    private String val;

    private Integer st;

    private Integer tier;

    private Date createTime;

    private Date updateTime;

    private String remark;
    
    private Integer inOrder;
    
    private MovieDictVO() {}
    
    public static MovieDictVO buildRoot(String code, String val, Date createTime) {
    	MovieDictVO vo = new MovieDictVO();
    	vo.setCode(code);
    	vo.setParentCode(CommonConstants.DICT_TOP);
    	vo.setRootCode(code);
    	vo.setVal(val);
    	vo.setSt(MovieStatusEnum.available.getCode());
    	vo.setTier(1);
    	vo.setCreateTime(createTime);
    	vo.setInOrder(5);
    	return vo;
    }
    
    public static MovieDictVO build(String code, String parentCode, String rootCode, String val, Integer tier, Date createTime) {
    	MovieDictVO vo = new MovieDictVO();
    	vo.setCode(code);
    	vo.setParentCode(parentCode);
    	vo.setRootCode(rootCode);
    	vo.setVal(val);
    	vo.setSt(MovieStatusEnum.available.getCode());
    	vo.setTier(tier);
    	vo.setCreateTime(createTime);
    	vo.setInOrder(50);
    	return vo;
    }
    
    public static MovieDictVO build(String code, String parentCode, String rootCode, String val, Integer tier, Date createTime, int inOrder) {
    	MovieDictVO vo = build(code, parentCode, rootCode, val, tier, createTime);
    	vo.setInOrder(inOrder);
    	return vo;
    }

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