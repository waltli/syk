package com.sbolo.syk.view.vo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.StringUtil;
import com.sbolo.syk.view.po.ResourceInfoEntity;

public class ResourceInfoVO {
    /**
     * 自增id
     */
    private Long id;

    /**
     * 全局唯一标识
     */
    private String prn;

    /**
     * 电影prn
     */
    private String moviePrn;

    /**
     * 电影名
     */
    private String pureName;

    /**
     * 上映时间
     */
    private Date releaseTime;
    
    private String releaseTimeStr;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 格式
     */
    private String format;

    /**
     * 清晰度
     */
    private Integer definition;

    /**
     * 影片质量-- HD  BD.....
     */
    private String quality;

    /**
     * 分辨率 720P 1080P 4K....
     */
    private String resolution;

    /**
     * 连接速度
     */
    private Integer speed;

    /**
     * 下载地址
     */
    private String downloadLink;
    
    private String downloadLinkUrl;

    /**
     * 第几季
     */
    private Integer season;

    /**
     * 集数开始（如果只有一集则该字段为空）
     */
    private Integer episodeStart;

    /**
     * 集数结束（如果只有一集则则填写该字段）
     */
    private Integer episodeEnd;

    /**
     * 字幕
     */
    private String subtitle;

    /**
     * 多个资源截图uri组成的json
     */
    private String shotUriJson;
    
    private List<String> shotUrlList;

    /**
     * 采集自哪个网站
     */
    private String comeFromUrl;

    /**
     * 资源状态
     */
    private Integer st;
    
    private String stDescp;

    /**
     * 创建时间
     */
    private Date createTime;
    
    private String createTimeStr;

    /**
     * 修改时间
     */
    private Date updateTime;
    
    private String updateTimeStr;
    
    private String linkType;

    public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getDownloadLinkUrl() {
		return downloadLinkUrl;
	}

	public void setDownloadLinkUrl(String downloadLinkUrl) {
		this.downloadLinkUrl = downloadLinkUrl;
	}

	public String getReleaseTimeStr() {
		return releaseTimeStr;
	}

	public void setReleaseTimeStr(String releaseTimeStr) {
		this.releaseTimeStr = releaseTimeStr;
	}

	public List<String> getShotUrlList() {
		return shotUrlList;
	}

	public void setShotUrlList(List<String> shotUrlList) {
		this.shotUrlList = shotUrlList;
	}

	public String getStDescp() {
		return stDescp;
	}

	public void setStDescp(String stDescp) {
		this.stDescp = stDescp;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
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
     * 获取电影prn
     *
     * @return movie_prn - 电影prn
     */
    public String getMoviePrn() {
        return moviePrn;
    }

    /**
     * 设置电影prn
     *
     * @param moviePrn 电影prn
     */
    public void setMoviePrn(String moviePrn) {
        this.moviePrn = moviePrn;
    }

    /**
     * 获取电影名
     *
     * @return pure_name - 电影名
     */
    public String getPureName() {
        return pureName;
    }

    /**
     * 设置电影名
     *
     * @param pureName 电影名
     */
    public void setPureName(String pureName) {
        this.pureName = pureName;
    }

    /**
     * 获取上映时间
     *
     * @return release_time - 上映时间
     */
    public Date getReleaseTime() {
        return releaseTime;
    }

    /**
     * 设置上映时间
     *
     * @param releaseTime 上映时间
     */
    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    /**
     * 获取文件大小
     *
     * @return size - 文件大小
     */
    public String getSize() {
        return size;
    }

    /**
     * 设置文件大小
     *
     * @param size 文件大小
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * 获取格式
     *
     * @return format - 格式
     */
    public String getFormat() {
        return format;
    }

    /**
     * 设置格式
     *
     * @param format 格式
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * 获取清晰度
     *
     * @return definition - 清晰度
     */
    public Integer getDefinition() {
        return definition;
    }

    /**
     * 设置清晰度
     *
     * @param definition 清晰度
     */
    public void setDefinition(Integer definition) {
        this.definition = definition;
    }

    /**
     * 获取影片质量-- HD  BD.....
     *
     * @return quality - 影片质量-- HD  BD.....
     */
    public String getQuality() {
        return quality;
    }

    /**
     * 设置影片质量-- HD  BD.....
     *
     * @param quality 影片质量-- HD  BD.....
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * 获取分辨率 720P 1080P 4K....
     *
     * @return resolution - 分辨率 720P 1080P 4K....
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * 设置分辨率 720P 1080P 4K....
     *
     * @param resolution 分辨率 720P 1080P 4K....
     */
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    /**
     * 获取连接速度
     *
     * @return speed - 连接速度
     */
    public Integer getSpeed() {
        return speed;
    }

    /**
     * 设置连接速度
     *
     * @param speed 连接速度
     */
    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    /**
     * 获取下载地址
     *
     * @return download_link - 下载地址
     */
    public String getDownloadLink() {
        return downloadLink;
    }

    /**
     * 设置下载地址
     *
     * @param downloadLink 下载地址
     */
    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    /**
     * 获取第几季
     *
     * @return season - 第几季
     */
    public Integer getSeason() {
        return season;
    }

    /**
     * 设置第几季
     *
     * @param season 第几季
     */
    public void setSeason(Integer season) {
        this.season = season;
    }

    /**
     * 获取集数开始（如果只有一集则该字段为空）
     *
     * @return episode_start - 集数开始（如果只有一集则该字段为空）
     */
    public Integer getEpisodeStart() {
        return episodeStart;
    }

    /**
     * 设置集数开始（如果只有一集则该字段为空）
     *
     * @param episodeStart 集数开始（如果只有一集则该字段为空）
     */
    public void setEpisodeStart(Integer episodeStart) {
        this.episodeStart = episodeStart;
    }

    /**
     * 获取集数结束（如果只有一集则则填写该字段）
     *
     * @return episode_end - 集数结束（如果只有一集则则填写该字段）
     */
    public Integer getEpisodeEnd() {
        return episodeEnd;
    }

    /**
     * 设置集数结束（如果只有一集则则填写该字段）
     *
     * @param episodeEnd 集数结束（如果只有一集则则填写该字段）
     */
    public void setEpisodeEnd(Integer episodeEnd) {
        this.episodeEnd = episodeEnd;
    }

    /**
     * 获取字幕
     *
     * @return subtitle - 字幕
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * 设置字幕
     *
     * @param subtitle 字幕
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

	public String getShotUriJson() {
		return shotUriJson;
	}

	public void setShotUriJson(String shotUriJson) {
		this.shotUriJson = shotUriJson;
	}

	/**
     * 获取采集自哪个网站
     *
     * @return come_from_url - 采集自哪个网站
     */
    public String getComeFromUrl() {
        return comeFromUrl;
    }

    /**
     * 设置采集自哪个网站
     *
     * @param comeFromUrl 采集自哪个网站
     */
    public void setComeFromUrl(String comeFromUrl) {
        this.comeFromUrl = comeFromUrl;
    }

    /**
     * 获取资源状态
     *
     * @return st - 资源状态
     */
    public Integer getSt() {
        return st;
    }

    /**
     * 设置资源状态
     *
     * @param st 资源状态
     */
    public void setSt(Integer st) {
        this.st = st;
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
    
    public void parse(){
    	SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.timeFormat.get(10));
    	String bucketHost = ConfigUtil.getPropertyValue("bucket.host");
    	if(this.getCreateTime() != null){
    		this.setCreateTimeStr(sdf.format(this.getCreateTime()));
    	}
    	if(this.getUpdateTime() != null){
    		this.setUpdateTimeStr(sdf.format(this.getUpdateTime()));
    	}
    	
    	if(StringUtils.isNotBlank(this.getDownloadLink())){
    		String downloadLinkUrl = "";
    		if(StringUtil.isLocalLink(this.getDownloadLink())){
    			downloadLinkUrl = bucketHost + this.getDownloadLink();
//    			this.setIsLocalLink(true);
    		}else {
    			downloadLinkUrl = this.getDownloadLink();
    		}
    		this.setDownloadLinkUrl(downloadLinkUrl);
    		
    		if(Pattern.compile(RegexConstant.magnet).matcher(this.getDownloadLink()).find()){
    			this.setLinkType("magnet");
    		}else if(Pattern.compile(RegexConstant.thunder).matcher(this.getDownloadLink()).find()){
    			this.setLinkType("thunder");
    		}else if(Pattern.compile(RegexConstant.ed2k).matcher(this.getDownloadLink()).find()){
    			this.setLinkType("ed2k");
    		}else if(Pattern.compile(RegexConstant.baiduNet).matcher(this.getDownloadLink()).find()){
    			this.setLinkType("baiduNet");
    		}else if(Pattern.compile(RegexConstant.torrent).matcher(this.getDownloadLink()).find()){
    			this.setLinkType("bt");
    		}else {
    			this.setLinkType("other");
    		}
    	}
    	
    	if(StringUtils.isNotBlank(this.getShotUriJson())){
    		JSONArray array = JSON.parseArray(this.getShotUriJson());
    		List<String> shotUrlList = new ArrayList<String>();
//    		String busPhotosStr = "";
    		for(int i=0; i<array.size(); i++){
    			String shotUri = array.getString(i);
    			String shotUrl = bucketHost + "/" + shotUri;
    			shotUrlList.add(shotUrl);
//    			busPhotosStr += (","+photoUrl);
    		}
//    		this.setBusPhotos(busPhotosStr.substring(1));
    		this.setShotUrlList(shotUrlList);
    	}
    	
    }
    
    public static void parse(List<ResourceInfoVO> list){
    	for(ResourceInfoVO vo : list){
    		vo.parse();
    	}
    }
    
}