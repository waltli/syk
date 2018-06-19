package com.sbolo.syk.common.vo;

public class PosterVO {
	private String fetchUrl;
	private String name;
	private Integer flag;
	
	public PosterVO(){}
	
	public PosterVO(String fetchUrl, String name, Integer flag) {
		this.fetchUrl = fetchUrl;
		this.name = name;
		this.flag = flag;
	}
	public String getFetchUrl() {
		return fetchUrl;
	}
	public void setFetchUrl(String fetchUrl) {
		this.fetchUrl = fetchUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
}
