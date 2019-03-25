package com.sbolo.syk.common.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum MovieDictEnum {

	LABEL("LABEL", "标签类型"),
	LOCATION("LOCATION", "地区类型"),
	TAG("TAG", "大标签");
	
	private String code;
	private String desc;

	MovieDictEnum(String code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static List<String> getCodes(){
		List<String> codes = new ArrayList<>();
		MovieDictEnum[] values = MovieDictEnum.values();
		for(MovieDictEnum value : values){
			codes.add(value.getCode());
		}
		return codes;
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
