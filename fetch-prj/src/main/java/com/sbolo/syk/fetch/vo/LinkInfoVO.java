package com.sbolo.syk.fetch.vo;

public class LinkInfoVO {
	private String name;
	private String downloadLink;
	private String linkDecoding = "GBK";
	public String getLinkDecoding() {
		return linkDecoding;
	}
	public void setLinkDecoding(String linkDecoding) {
		this.linkDecoding = linkDecoding;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDownloadLink() {
		return downloadLink;
	}
	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}
}
