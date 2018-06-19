package com.sbolo.syk.common.constants;

import com.sbolo.syk.common.tools.StringUtil;

public enum TimeDirectEnum {
	UP("23:59:59", "当天最后时分"),
	DOWN("00:00:00", "当天起始"),
	NORMAL("", "无变化");
	
	
	private String code;
	private String desc;

	TimeDirectEnum(String code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static String getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		TimeDirectEnum[] values = TimeDirectEnum.values();
		for(TimeDirectEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getCodeByDesc(String desc){
		TimeDirectEnum[] values = TimeDirectEnum.values();
		for(TimeDirectEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(String code){
		TimeDirectEnum[] values = TimeDirectEnum.values();
		for(TimeDirectEnum value : values){
			if(value.getCode().equals(code)){
				return value.getDesc();
			}
		}
		return null;
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
