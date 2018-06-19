package com.sbolo.syk.common.constants;

import com.sbolo.syk.common.tools.StringUtil;

public enum MovieCategoryEnum {
	movie(1, "电影"),
	tv(2,"剧集"),
	variety(3,"综艺");
	
	private int code;
	private String desc;

	MovieCategoryEnum(int code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static int getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		MovieCategoryEnum[] values = MovieCategoryEnum.values();
		for(MovieCategoryEnum value : values){
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
