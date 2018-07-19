package com.sbolo.syk.fetch.vo;

public class PicVO {
	private String fetchUrl;
	private String fileName;
	private String suffix;
	private Integer picV;
	private String picDir;
	private Integer picWidth;
	private Integer picHeight;
	
	public PicVO(){}
	
	public PicVO(String fetchUrl, String fileName, String suffix, Integer picV, String picDir, Integer picWidth, Integer picHeight) {
		this.fetchUrl = fetchUrl;
		this.fileName = fileName;
		this.suffix = suffix;
		this.picV = picV;
		this.picDir = picDir;
		this.picWidth = picWidth;
		this.picHeight = picHeight;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFetchUrl() {
		return fetchUrl;
	}

	public void setFetchUrl(String fetchUrl) {
		this.fetchUrl = fetchUrl;
	}

	public Integer getPicV() {
		return picV;
	}

	public void setPicV(Integer picV) {
		this.picV = picV;
	}

	public String getPicDir() {
		return picDir;
	}

	public void setPicDir(String picDir) {
		this.picDir = picDir;
	}

	public Integer getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(Integer picWidth) {
		this.picWidth = picWidth;
	}

	public Integer getPicHeight() {
		return picHeight;
	}

	public void setPicHeight(Integer picHeight) {
		this.picHeight = picHeight;
	}
}
