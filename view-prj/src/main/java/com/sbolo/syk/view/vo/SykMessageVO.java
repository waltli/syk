package com.sbolo.syk.view.vo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.tools.StringUtil;

public class SykMessageVO implements Comparable<SykMessageVO> {
	
    private Long id;

    private String prn;

    private String pkey;

    private String msgContent;

    private Integer msgLevel;

    private String parentPrn;

    private String prnLine;
    
    private String rootPrn;

    private Integer likeCount;

    private String authorPrn;

    private String authorIp;

    private String authorLocation;
    
    private String userAgent;

    private Date createTime;
    
    private String createTimeStr;
    
    private String createTimeFullStr;
    
    private String createTimeCalc;

    private Date updateTime;
    
    private SykUserVO author;
    
    //当前登录用户对此条回复是否已赞
    private Boolean liked;

    public Boolean getLiked() {
		return liked;
	}

	public void setLiked(Boolean liked) {
		this.liked = liked;
	}

	public String getCreateTimeFullStr() {
		return createTimeFullStr;
	}

	public void setCreateTimeFullStr(String createTimeFullStr) {
		this.createTimeFullStr = createTimeFullStr;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getCreateTimeCalc() {
		return createTimeCalc;
	}

	public void setCreateTimeCalc(String createTimeCalc) {
		this.createTimeCalc = createTimeCalc;
	}

	public SykUserVO getAuthor() {
		return author;
	}

	public void setAuthor(SykUserVO author) {
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

    public String getPrnLine() {
		return prnLine;
	}

	public void setPrnLine(String prnLine) {
		this.prnLine = prnLine;
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
    
    @SuppressWarnings("deprecation")
	public void parse() {
    	if(this.getCreateTime() != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			String createTimeStr = sdf.format(this.getCreateTime());
			this.setCreateTimeStr(createTimeStr);
			
			SimpleDateFormat sdf2 = new SimpleDateFormat(CommonConstants.timeFormat.get(19));
			String createTimeFullStr = sdf2.format(this.getCreateTime());
			this.setCreateTimeFullStr(createTimeFullStr);
			
			Date thisTime = new Date();
			int thisYear = thisTime.getYear();
			int createYear = this.getCreateTime().getYear();
			long timeStamp = this.getCreateTime().getTime();
			long timeStampNow = thisTime.getTime();
			long dis = (timeStampNow - timeStamp) / 1000; //获取秒
			if(dis < 10) {
				this.setCreateTimeCalc("刚刚");
			}else if(dis < 60) {
				this.setCreateTimeCalc(dis+"秒前");
			}else if(dis < 3600) {
				this.setCreateTimeCalc((dis/60)+"分钟前");
			}else if(dis < 86400) {
				this.setCreateTimeCalc((dis/60/60)+"小时前");
			}else if(thisYear == createYear){
				SimpleDateFormat sdf3 = new SimpleDateFormat("MM月dd日");
				this.setCreateTimeCalc(sdf3.format(this.getCreateTime()));
			}else {
				this.setCreateTimeCalc(createTimeStr);
			}
		}
    }
    
    public static void parse(List<SykMessageVO> list){
    	for(SykMessageVO vo : list){
    		vo.parse();
    	}
    }

	@Override
	public int compareTo(SykMessageVO o) {
		if(this.getMsgLevel() < o.getMsgLevel()) {
			return -1;
		}
		return 1;
	}
	
	public void notNull(String authorPrn, String ip, String userAgent) {
		if(this.getCreateTime() == null) {
			this.setCreateTime(new Date());
		}
		
		if(this.getLikeCount() == null) {
			this.setLikeCount(0);
		}
		
		if(StringUtils.isBlank(this.getPrn())) {
			this.setPrn(StringUtil.getId(CommonConstants.message_s));
		}
		
		if(StringUtils.isBlank(this.getAuthorPrn())) {
			this.setAuthorPrn(authorPrn);
		}
		
		if(StringUtils.isBlank(this.getAuthorIp())) {
			this.setAuthorIp(ip);
		}
		
		if(StringUtils.isBlank(this.getParentPrn())) {
			this.setParentPrn("0");
		}
		if(StringUtils.isBlank(this.getPrnLine())) {
			this.setPrnLine(","+this.getPrn()+",");
		}else {
			this.setPrnLine(this.getPrnLine()+this.getPrn()+",");
		}
		if(StringUtils.isBlank(this.getRootPrn())) {
			this.setRootPrn(this.getPrn());
		}
		if(StringUtils.isBlank(this.getUserAgent())) {
			this.setUserAgent(userAgent);
		}
	}
    
}