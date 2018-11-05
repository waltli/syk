package com.sbolo.syk.view.vo;

import java.util.Date;

public class SykMessageVO {
	
    private Long id;

    private String prn;

    private String pkey;

    private String msgContent;

    private Integer msgLevel;

    private String parentPrn;

    private String parentPrns;
    
    private String rootPrn;

    private Integer likeCount;

    private String authorPrn;

    private String authorIp;

    private String authorLocation;
    
    private String userAgent;

    private Date createTime;

    private Date updateTime;
    
    private SykUsersVO author;

    public SykUsersVO getAuthor() {
		return author;
	}

	public void setAuthor(SykUsersVO author) {
		this.author = author;
	}

	public String getRootPrn() {
		return rootPrn;
	}

	public void setRootPrn(String rootPrn) {
		this.rootPrn = rootPrn;
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
     * @return pkey
     */
    public String getPkey() {
        return pkey;
    }

    /**
     * @param pkey
     */
    public void setPkey(String pkey) {
        this.pkey = pkey;
    }

    /**
     * @return msg_content
     */
    public String getMsgContent() {
        return msgContent;
    }

    /**
     * @param msgContent
     */
    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    /**
     * @return msg_level
     */
    public Integer getMsgLevel() {
        return msgLevel;
    }

    /**
     * @param msgLevel
     */
    public void setMsgLevel(Integer msgLevel) {
        this.msgLevel = msgLevel;
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
     * @return parent_prns
     */
    public String getParentPrns() {
        return parentPrns;
    }

    /**
     * @param parentPrns
     */
    public void setParentPrns(String parentPrns) {
        this.parentPrns = parentPrns;
    }

    /**
     * @return like_count
     */
    public Integer getLikeCount() {
        return likeCount;
    }

    /**
     * @param likeCount
     */
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    /**
     * @return author_prn
     */
    public String getAuthorPrn() {
        return authorPrn;
    }

    /**
     * @param authorPrn
     */
    public void setAuthorPrn(String authorPrn) {
        this.authorPrn = authorPrn;
    }

    /**
     * @return author_ip
     */
    public String getAuthorIp() {
        return authorIp;
    }

    /**
     * @param authorIp
     */
    public void setAuthorIp(String authorIp) {
        this.authorIp = authorIp;
    }

    /**
     * @return author_location
     */
    public String getAuthorLocation() {
        return authorLocation;
    }

    /**
     * @param authorLocation
     */
    public void setAuthorLocation(String authorLocation) {
        this.authorLocation = authorLocation;
    }

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
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