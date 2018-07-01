package com.sbolo.syk.fetch.vo;

public class PicVO {
	private String fetchUrl;
	private String fileName;
	private Integer flag;
	
	public PicVO(){}
	
	public PicVO(String fetchUrl, String fileName, Integer flag) {
		this.fetchUrl = fetchUrl;
		this.fileName = fileName;
		this.flag = flag;
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

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}
