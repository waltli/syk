package com.sbolo.syk.fetch.vo;

import java.util.List;

public class GatherVO {
	private String pureName;
	private List<String> precisions;
	private List<LinkInfoVO> linkInfos;
	private List<String> printScreens;
	private String comeFromUrl;
	private String doubaDetailUril;
	
	public GatherVO(){}
	
	public GatherVO(String pureName, List<String> precisions,
			List<LinkInfoVO> linkInfos, List<String> printScreens,
			String comeFromUrl, String doubaDetailUril) {
		this.pureName = pureName;
		this.precisions = precisions;
		this.linkInfos = linkInfos;
		this.printScreens = printScreens;
		this.comeFromUrl = comeFromUrl;
		this.doubaDetailUril = doubaDetailUril;
	}
	public String getDoubaDetailUril() {
		return doubaDetailUril;
	}
	public void setDoubaDetailUril(String doubaDetailUril) {
		this.doubaDetailUril = doubaDetailUril;
	}
	public String getPureName() {
		return pureName;
	}
	public void setPureName(String pureName) {
		this.pureName = pureName;
	}
	public List<String> getPrecisions() {
		return precisions;
	}
	public void setPrecisions(List<String> precisions) {
		this.precisions = precisions;
	}
	public List<LinkInfoVO> getLinkInfos() {
		return linkInfos;
	}
	public void setLinkInfos(List<LinkInfoVO> linkInfos) {
		this.linkInfos = linkInfos;
	}
	public List<String> getPrintScreens() {
		return printScreens;
	}
	public void setPrintScreens(List<String> printScreens) {
		this.printScreens = printScreens;
	}
	public String getComeFromUrl() {
		return comeFromUrl;
	}
	public void setComeFromUrl(String comeFromUrl) {
		this.comeFromUrl = comeFromUrl;
	}
	
}
