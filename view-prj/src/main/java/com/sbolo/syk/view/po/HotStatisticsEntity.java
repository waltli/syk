package com.sbolo.syk.view.po;

import java.util.Date;

public class HotStatisticsEntity {
    private Integer id;
    
    private String hotId;

    private String movieId;

    private String pureName;

    private Date releaseTime;
    
    private Double doubanScore;
    
    private Double imdbScore;
    
    private Integer triggerType;

    private Date createTime;

    private Date updateTime;
    
    private Integer hotCount;

	public String getHotId() {
		return hotId;
	}

	public void setHotId(String hotId) {
		this.hotId = hotId;
	}

	public Integer getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(Integer triggerType) {
		this.triggerType = triggerType;
	}

	public Double getDoubanScore() {
		return doubanScore;
	}

	public void setDoubanScore(Double doubanScore) {
		this.doubanScore = doubanScore;
	}

	public Double getImdbScore() {
		return imdbScore;
	}

	public void setImdbScore(Double imdbScore) {
		this.imdbScore = imdbScore;
	}

	public Integer getHotCount() {
		return hotCount;
	}

	public void setHotCount(Integer hotCount) {
		this.hotCount = hotCount;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId == null ? null : movieId.trim();
    }

    public String getPureName() {
        return pureName;
    }

    public void setPureName(String pureName) {
        this.pureName = pureName == null ? null : pureName.trim();
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}