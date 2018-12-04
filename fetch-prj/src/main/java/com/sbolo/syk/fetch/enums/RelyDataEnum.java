package com.sbolo.syk.fetch.enums;

public enum RelyDataEnum {

	moive("movie_info", "电影"),
	resource("resource_info", "资源"),
	dict("movie_dict", "电影词典");
	
	private String code;
	private String descp;

	RelyDataEnum(String code, String descp){
		this.code = code;
		this.descp = descp;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

}
