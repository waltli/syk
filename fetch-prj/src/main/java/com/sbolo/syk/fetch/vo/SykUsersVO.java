package com.sbolo.syk.fetch.vo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`syk_users`")
public class SykUsersVO {
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
     * 用户名
     */
    @Column(name = "`username`")
    private String username;

    /**
     * 密码
     */
    @Column(name = "`password`")
    private String password;

    /**
     * 手机号
     */
    @Column(name = "`mobile`")
    private String mobile;

    /**
     * 邮箱地址
     */
    @Column(name = "`email`")
    private String email;

    /**
     * 微信信息prn
     */
    @Column(name = "`wechat_info_prn`")
    private String wechatInfoPrn;

    /**
     * 微博信息prn
     */
    @Column(name = "`weibo_info_prn`")
    private String weiboInfoPrn;

    /**
     * 用户类型
     */
    @Column(name = "`user_type`")
    private Integer userType;

    /**
     * 状态
     */
    @Column(name = "`st`")
    private Integer st;

    /**
     * 头像uri
     */
    @Column(name = "`avatar_uri`")
    private String avatarUri;

    /**
     * 昵称
     */
    @Column(name = "`nikename`")
    private String nikename;

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
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
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

    /**
     * 获取微信信息prn
     *
     * @return wechat_info_prn - 微信信息prn
     */
    public String getWechatInfoPrn() {
        return wechatInfoPrn;
    }

    /**
     * 设置微信信息prn
     *
     * @param wechatInfoPrn 微信信息prn
     */
    public void setWechatInfoPrn(String wechatInfoPrn) {
        this.wechatInfoPrn = wechatInfoPrn;
    }

    /**
     * 获取微博信息prn
     *
     * @return weibo_info_prn - 微博信息prn
     */
    public String getWeiboInfoPrn() {
        return weiboInfoPrn;
    }

    /**
     * 设置微博信息prn
     *
     * @param weiboInfoPrn 微博信息prn
     */
    public void setWeiboInfoPrn(String weiboInfoPrn) {
        this.weiboInfoPrn = weiboInfoPrn;
    }

    /**
     * 获取用户类型
     *
     * @return user_type - 用户类型
     */
    public Integer getUserType() {
        return userType;
    }

    /**
     * 设置用户类型
     *
     * @param userType 用户类型
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
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

    /**
     * 获取昵称
     *
     * @return nikename - 昵称
     */
    public String getNikename() {
        return nikename;
    }

    /**
     * 设置昵称
     *
     * @param nikename 昵称
     */
    public void setNikename(String nikename) {
        this.nikename = nikename;
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