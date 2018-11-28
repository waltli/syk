package com.sbolo.syk.common.constants;

import org.apache.commons.lang3.StringUtils;

import com.sbolo.syk.common.tools.StringUtil;

public enum UserSettleEnum {

	DOWN(1, "已设置"),
	NOT(0, "未设置");
	
	private Integer code;
	private String desc;

	UserSettleEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static Integer getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		UserSettleEnum[] values = UserSettleEnum.values();
		for(UserSettleEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static Integer getCodeByDesc(String desc){
		UserSettleEnum[] values = UserSettleEnum.values();
		for(UserSettleEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(Integer code){
		MovieDictEnum[] values = MovieDictEnum.values();
		for(MovieDictEnum value : values){
			if(value.getCode().equals(code)){
				return value.getDesc();
			}
		}
		return null;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
