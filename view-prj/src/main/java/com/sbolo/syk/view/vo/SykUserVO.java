package com.sbolo.syk.view.vo;

import java.util.Date;
import javax.persistence.*;

public class SykUserVO {
    /**
     * 自增id
     */
    private Long id;

    /**
     * 全局唯一标识
     */
    private String prn;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 用户类型
     */
    private Long userPower;

    /**
     * 状态
     */
    private Integer st;

    /**
     * 头像uri
     */
    private String avatarUri;

    /**
     * 昵称
     */
    private String nickname;
    
    private Integer isSettle;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public Integer getIsSettle() {
		return isSettle;
	}

	public void setIsSettle(Integer isSettle) {
		this.isSettle = isSettle;
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
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取邮箱地址
     *
     * @return email - 邮箱地址
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱地址
     *
     * @param email 邮箱地址
     */
    public void setEmail(String email) {
        this.email = email;
    }

	public Long getUserPower() {
		return userPower;
	}

	public void setUserPower(Long userPower) {
		this.userPower = userPower;
	}

	/**
     * 获取状态
     *
     * @return st - 状态
     */
    public Integer getSt() {
        return st;
    }

    /**
     * 设置状态
     *
     * @param st 状态
     */
    public void setSt(Integer st) {
        this.st = st;
    }

    /**
     * 获取头像uri
     *
     * @return avatar_uri - 头像uri
     */
    public String getAvatarUri() {
        return avatarUri;
    }

    /**
     * 设置头像uri
     *
     * @param avatarUri 头像uri
     */
    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }


    public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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