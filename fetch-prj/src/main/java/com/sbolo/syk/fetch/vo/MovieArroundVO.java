package com.sbolo.syk.fetch.vo;

import java.util.Date;
import java.util.List;

public class MovieArroundVO {
	private MovieInfoVO info;
	private MovieInfoVO db;
	private String pureName;
	private Integer category;
	private String comeFromUrl;
	private Integer action;
	private Date releaseTime;
	private String prn;
	private List<ResourceInfoVO> resourceList;
	
	public String getPrn() {
		return prn;
	}

	public void setPrn(String prn) {
		this.prn = prn;
	}

	public List<ResourceInfoVO> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<ResourceInfoVO> resourceList) {
		this.resourceList = resourceList;
		if(this.info != null) {
			this.info.setResourceList(resourceList);
		}
		
		if(this.db != null) {
			this.db.setResourceList(resourceList);
		}
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getPureName() {
		return pureName;
	}
	
	public void setPureName(String pureName) {
		this.pureName = pureName;
	}
	
	public Integer getCategory() {
		return category;
	}
	
	public void setCategory(Integer category) {
		this.category = category;
	}
	
	public String getComeFromUrl() {
		return comeFromUrl;
	}
	
	public void setComeFromUrl(String comeFromUrl) {
		this.comeFromUrl = comeFromUrl;
	}
	
	public Integer getAction() {
		return action;
	}
	
	public void setAction(Integer action) {
		this.action = action;
		if(this.info != null) {
			this.info.setAction(action);
		}
		
		if(this.db != null) {
			this.db.setAction(action);
		}
	}
	
	public MovieInfoVO getInfo() {
		return info;
	}
	
	public void setInfo(MovieInfoVO info) {
		this.info = info;
		this.category = info.getCategory();
		this.comeFromUrl = info.getComeFromUrl();
		this.pureName = info.getPureName();
		this.releaseTime = info.getReleaseTime();
		this.prn = info.getPrn();
		this.resourceList = info.getResourceList();
	}
	
	public MovieInfoVO getDb() {
		return db;
	}
	
	public void setDb(MovieInfoVO db) {
		db.setResourceList(this.getResourceList());
		this.db = db;
		this.prn = db.getPrn();
	}
	
}
