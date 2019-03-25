package com.sbolo.syk.common.enums;

import com.sbolo.syk.common.tools.StringUtil;

public enum MovieStatusEnum {
	available(1, "可用"),
	deletable(2,"");
	
	private int code;
	private String desc;

	MovieStatusEnum(int code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static int getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		MovieStatusEnum[] values = MovieStatusEnum.values();
		for(MovieStatusEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return 0;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	
}
