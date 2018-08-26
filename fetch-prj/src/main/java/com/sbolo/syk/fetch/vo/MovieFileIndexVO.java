package com.sbolo.syk.fetch.vo;

import java.util.Date;
import javax.persistence.*;

public class MovieFileIndexVO {
    /**
     * 自增id
     */
    private Long id;

    /**
     * 原文件URL
     */
    private String sourceUrl;

    /**
     * 文件修正后并上传到服务器后的uri
     */
    private String fixUri;

    /**
     * 文件类型 1---icon 2---poster 3---photo 4---shot 5---torrent
     */
    private Integer fileV;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
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
     * 获取文件类型 1---icon 2---poster 3---photo 4---shot 5---torrent
     *
     * @return file_v - 文件类型 1---icon 2---poster 3---photo 4---shot 5---torrent
     */
    public Integer getFileV() {
        return fileV;
    }

    /**
     * 设置文件类型 1---icon 2---poster 3---photo 4---shot 5---torrent
     *
     * @param fileV 文件类型 1---icon 2---poster 3---photo 4---shot 5---torrent
     */
    public void setFileV(Integer fileV) {
        this.fileV = fileV;
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