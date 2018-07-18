package com.sbolo.syk.fetch.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`movie_file_index`")
public class MovieFileIndexEntity {
    /**
     * 自增id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 原文件URL
     */
    @Column(name = "`source_url`")
    private String sourceUrl;

    /**
     * 原文件URL的MD5值
     */
    @Column(name = "`source_url_md5`")
    private String sourceUrlMd5;

    /**
     * 文件修正后并上传到服务器后的uri
     */
    @Column(name = "`fix_uri`")
    private String fixUri;

    /**
     * 文件类型
     */
    @Column(name = "`file_type`")
    private String fileType;

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
     * 获取原文件URL
     *
     * @return source_url - 原文件URL
     */
    public String getSourceUrl() {
        return sourceUrl;
    }

    /**
     * 设置原文件URL
     *
     * @param sourceUrl 原文件URL
     */
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    /**
     * 获取原文件URL的MD5值
     *
     * @return source_url_md5 - 原文件URL的MD5值
     */
    public String getSourceUrlMd5() {
        return sourceUrlMd5;
    }

    /**
     * 设置原文件URL的MD5值
     *
     * @param sourceUrlMd5 原文件URL的MD5值
     */
    public void setSourceUrlMd5(String sourceUrlMd5) {
        this.sourceUrlMd5 = sourceUrlMd5;
    }

    /**
     * 获取文件修正后并上传到服务器后的uri
     *
     * @return fix_uri - 文件修正后并上传到服务器后的uri
     */
    public String getFixUri() {
        return fixUri;
    }

    /**
     * 设置文件修正后并上传到服务器后的uri
     *
     * @param fixUri 文件修正后并上传到服务器后的uri
     */
    public void setFixUri(String fixUri) {
        this.fixUri = fixUri;
    }

    /**
     * 获取文件类型
     *
     * @return file_type - 文件类型
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * 设置文件类型
     *
     * @param fileType 文件类型
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
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