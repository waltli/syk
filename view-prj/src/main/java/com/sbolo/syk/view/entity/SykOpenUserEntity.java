package com.sbolo.syk.view.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`syk_open_user`")
public class SykOpenUserEntity {
    /**
     * 自增id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 全局唯一标示
     */
    @Column(name = "`prn`")
    private String prn;

    /**
     * user表prn
     */
    @Column(name = "`user_prn`")
    private String userPrn;

    /**
     * 第三方类型 qq/weibo
     */
    @Column(name = "`open_type`")
    private Integer openType;

    /**
     * 代表用户唯一身份的ID
     */
    @Column(name = "`open_id`")
    private String openId;

    /**
     * 	调用接口需要用到的token，比如利用accessToken发表微博等，如果只是对接登录的话，这个其实没啥用
     */
    @Column(name = "`access_token`")
    private String accessToken;

    /**
     * 授权过期时间，第三方登录授权都是有过期时间的，比如3个月
     */
    @Column(name = "`expired_time`")
    private Long expiredTime;

    /**
     * 昵称
     */
    @Column(name = "`nickname`")
    private String nickname;

    /**
     * 头像
     */
    @Column(name = "`avatar_url`")
    private String avatarUrl;

    @Column(name = "`create_time`")
    private Date createTime;

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
     * 获取全局唯一标示
     *
     * @return prn - 全局唯一标示
     */
    public String getPrn() {
        return prn;
    }

    /**
     * 设置全局唯一标示
     *
     * @param prn 全局唯一标示
     */
    public void setPrn(String prn) {
        this.prn = prn;
    }

    /**
     * 获取user表prn
     *
     * @return user_prn - user表prn
     */
    public String getUserPrn() {
        return userPrn;
    }

    /**
     * 设置user表prn
     *
     * @param userPrn user表prn
     */
    public void setUserPrn(String userPrn) {
        this.userPrn = userPrn;
    }

    /**
     * 获取第三方类型 qq/weibo
     *
     * @return open_type - 第三方类型 qq/weibo
     */
    public Integer getOpenType() {
        return openType;
    }

    /**
     * 设置第三方类型 qq/weibo
     *
     * @param openType 第三方类型 qq/weibo
     */
    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    /**
     * 获取代表用户唯一身份的ID
     *
     * @return open_id - 代表用户唯一身份的ID
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置代表用户唯一身份的ID
     *
     * @param openId 代表用户唯一身份的ID
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取	调用接口需要用到的token，比如利用accessToken发表微博等，如果只是对接登录的话，这个其实没啥用
     *
     * @return access_token - 	调用接口需要用到的token，比如利用accessToken发表微博等，如果只是对接登录的话，这个其实没啥用
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * 设置	调用接口需要用到的token，比如利用accessToken发表微博等，如果只是对接登录的话，这个其实没啥用
     *
     * @param accessToken 	调用接口需要用到的token，比如利用accessToken发表微博等，如果只是对接登录的话，这个其实没啥用
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Long expiredTime) {
		this.expiredTime = expiredTime;
	}

	/**
     * 获取昵称
     *
     * @return nickname - 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取头像
     *
     * @return avatar_url - 头像
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * 设置头像
     *
     * @param avatarUrl 头像
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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
}