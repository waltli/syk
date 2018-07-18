package com.sbolo.syk.fetch.vo;

import java.util.Date;
import javax.persistence.*;

public class MovieFileIndexVO {
    /**
     * 自增id
     */
    private Long id;

    /**
     * 原文件rul
     */
    private String sourceUrl;
    
    /**
     * 原文件URL的MD5值
     */
    private String sourceUrlMd5;

    /**
     * 文件修正后并上传到服务器后的uri
     */
    private String fixUri;
    
    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public String getSourceUrlMd5() {
		return sourceUrlMd5;
	}

	public void setSourceUrlMd5(String sourceUrlMd5) {
		this.sourceUrlMd5 = sourceUrlMd5;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
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
     * 获取原文件rul
     *
     * @return source_url - 原文件rul
     */
    public String getSourceUrl() {
        return sourceUrl;
    }

    /**
     * 设置原文件rul
     *
     * @param sourceUrl 原文件rul
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