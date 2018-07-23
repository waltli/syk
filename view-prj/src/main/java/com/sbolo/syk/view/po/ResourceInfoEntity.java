package com.sbolo.syk.view.po;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.constants.RegexConstant;
import com.sbolo.syk.common.tools.ConfigUtil;
import com.sbolo.syk.common.tools.StringUtil;

public class ResourceInfoEntity {
    private Integer id;

    private String resourceId;

    private String movieId;
    
    private String pureName;
    
    private Date releaseTime;
    
    private String size;

    private String format;

    private Integer definition;
    
    private String definitionCalc;

    private String quality;

    private String resolution;

    private Integer speed;

    private String downloadLink;  //本地link纯名字
    
    private String busDownloadLink;  //link带有URI
    
    private String thunderDecoding;
    
    private Boolean isLocalLink;
    
    private String linkType;

    private Integer season;

    private Integer episodeStart;
    
    private Integer episodeEnd;

    private String subtitle;

    private String photos;  //本地photo名字字符串
    
    private String busPhotos;  //本地photo带Uri字符串
    
    private List<String> busPhotosList;  //本地photo带UriList
    
    private String comeFromUrl;
    
    private Integer resourceStatus;

    private Date createTime;
    
    private String createTimeStr;

    private Date updateTime;
    
    private String updateTimeStr;
    
    private Integer action;  //Business property, that will direct 'insert' or 'update' or 'abandon'.

	public Integer getResourceStatus() {
		return resourceStatus;
	}

	public void setResourceStatus(Integer resourceStatus) {
		this.resourceStatus = resourceStatus;
	}

	public String getBusPhotos() {
		return busPhotos;
	}

	public void setBusPhotos(String busPhotos) {
		this.busPhotos = busPhotos;
	}

	public List<String> getBusPhotosList() {
		return busPhotosList;
	}

	public void setBusPhotosList(List<String> busPhotosList) {
		this.busPhotosList = busPhotosList;
	}

	public String getBusDownloadLink() {
		return busDownloadLink;
	}

	public void setBusDownloadLink(String busDownloadLink) {
		this.busDownloadLink = busDownloadLink;
	}

	public String getThunderDecoding() {
		return thunderDecoding;
	}

	public void setThunderDecoding(String thunderDecoding) {
		this.thunderDecoding = thunderDecoding;
	}

	public Boolean getIsLocalLink() {
		return isLocalLink;
	}

	public void setIsLocalLink(Boolean isLocalLink) {
		this.isLocalLink = isLocalLink;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
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

	public String getDefinitionCalc() {
		return definitionCalc;
	}

	public void setDefinitionCalc(String definitionCalc) {
		this.definitionCalc = definitionCalc;
	}

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public String getPureName() {
		return pureName;
	}

	public void setPureName(String pureName) {
		this.pureName = pureName;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId == null ? null : resourceId.trim();
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId == null ? null : movieId.trim();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format == null ? null : format.trim();
    }

    public Integer getDefinition() {
        return definition;
    }

    public void setDefinition(Integer definition) {
        this.definition = definition;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality == null ? null : quality.trim();
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution == null ? null : resolution.trim();
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink == null ? null : downloadLink.trim();
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

	public Integer getEpisodeStart() {
		return episodeStart;
	}

	public void setEpisodeStart(Integer episodeStart) {
		this.episodeStart = episodeStart;
	}

	public Integer getEpisodeEnd() {
		return episodeEnd;
	}

	public void setEpisodeEnd(Integer episodeEnd) {
		this.episodeEnd = episodeEnd;
	}

	public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle == null ? null : subtitle.trim();
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos == null ? null : photos.trim();
    }

    public String getComeFromUrl() {
        return comeFromUrl;
    }

    public void setComeFromUrl(String comeFromUrl) {
        this.comeFromUrl = comeFromUrl == null ? null : comeFromUrl.trim();
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
    
    public ResourceInfoEntity changeOptionManual(ResourceInfoEntity modiResource){
    	ResourceInfoEntity rie = new ResourceInfoEntity();
    	boolean hasChange = false;
    	
    	if(StringUtils.isNotBlank(modiResource.getBusDownloadLink())){
    		if(StringUtils.isBlank(this.getBusDownloadLink()) || !this.getBusDownloadLink().equals(modiResource.getBusDownloadLink())){
    			rie.setBusDownloadLink(modiResource.getBusDownloadLink());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiResource.getSize())){
    		if(StringUtils.isBlank(this.getSize()) || !this.getSize().equals(modiResource.getSize())){
    			rie.setSize(modiResource.getSize());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiResource.getFormat())){
    		if(StringUtils.isBlank(this.getFormat()) || !this.getFormat().equals(modiResource.getFormat())){
    			rie.setFormat(modiResource.getFormat());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiResource.getQuality())){
    		if(StringUtils.isBlank(this.getQuality()) || !this.getQuality().equals(modiResource.getQuality())){
    			rie.setQuality(modiResource.getQuality());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiResource.getResolution())){
    		if(StringUtils.isBlank(this.getResolution()) || !this.getResolution().equals(modiResource.getResolution())){
    			rie.setResolution(modiResource.getResolution());
    			hasChange = true;
    		}
    	}
    	
    	if(modiResource.getEpisodeStart() != null){
    		if(this.getEpisodeStart() == null || this.getEpisodeStart().intValue() != modiResource.getEpisodeStart().intValue()){
    			rie.setEpisodeStart(modiResource.getEpisodeStart());
    			hasChange = true;
    		}
    	}
    	
    	if(modiResource.getEpisodeEnd() != null){
    		if(this.getEpisodeEnd() == null || this.getEpisodeEnd().intValue() != modiResource.getEpisodeEnd().intValue()){
    			rie.setEpisodeEnd(modiResource.getEpisodeEnd());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiResource.getSubtitle())){
    		if(StringUtils.isBlank(this.getSubtitle()) || !this.getSubtitle().equals(modiResource.getSubtitle())){
    			rie.setSubtitle(modiResource.getSubtitle());
    			hasChange = true;
    		}
    	}
    	
    	if(StringUtils.isNotBlank(modiResource.getBusPhotos())){
    		if(StringUtils.isBlank(this.getBusPhotos()) || !this.getBusPhotos().equals(modiResource.getBusPhotos())){
    			rie.setBusPhotos(modiResource.getBusPhotos());
    			hasChange = true;
    		}
    	}
    	
    	if(hasChange){
    		return rie;
    	}
    	return null;
    	
    }
    
    public void parse(){
    	SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.timeFormat.get(10));
    	
    	if(this.getCreateTime() != null){
    		this.setCreateTimeStr(sdf.format(this.getCreateTime()));
    	}
    	if(this.getUpdateTime() != null){
    		this.setUpdateTimeStr(sdf.format(this.getUpdateTime()));
    	}
    	
    	if(StringUtils.isNotBlank(this.getDownloadLink())){
    		String busDownloadLinkStr = "";
    		if(StringUtil.isLocalLink(this.getDownloadLink())){
    			String torrentView = ConfigUtil.getPropertyValue("torrentView");
    			busDownloadLinkStr = torrentView + "/" + this.getDownloadLink();
    			this.setIsLocalLink(true);
    		}else {
    			busDownloadLinkStr = this.getDownloadLink();
    		}
    		this.setBusDownloadLink(busDownloadLinkStr);
    		
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
    	
    	if(StringUtils.isNotBlank(this.getPhotos())){
    		JSONArray array = JSON.parseArray(this.getPhotos());
    		List<String> photoList = new ArrayList<String>();
    		String busPhotosStr = "";
    		for(int i=0; i<array.size(); i++){
    			String fileName = array.getString(i);
    			String photoView = ConfigUtil.getPropertyValue("photoView");
    			String photoUrl = photoView + "/" + fileName;
    			photoList.add(photoUrl);
    			busPhotosStr += (","+photoUrl);
    		}
    		this.setBusPhotos(busPhotosStr.substring(1));
    		this.setBusPhotosList(photoList);
    	}
    	
    }
    
    public static void parse(List<ResourceInfoEntity> list){
    	for(ResourceInfoEntity entity : list){
    		entity.parse();
    	}
    }
    
}