package com.sbolo.syk.common.constants;

import com.sbolo.syk.common.tools.StringUtil;

public enum MovieDictEnum {

	LABEL("LABEL", "标签类型"),
	LOCATION("LOCATION", "地区类型");
	
	private String code;
	private String desc;

	MovieDictEnum(String code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
