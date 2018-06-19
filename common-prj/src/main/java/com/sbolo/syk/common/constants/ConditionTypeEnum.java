package com.sbolo.syk.common.constants;

import com.sbolo.syk.common.tools.StringUtil;

public enum ConditionTypeEnum {
	GROUP(1, "分组"),
	CONDITION(2, "条件");
	
	private Integer code;
	private String desc;

	ConditionTypeEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static Integer getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		ConditionTypeEnum[] values = ConditionTypeEnum.values();
		for(ConditionTypeEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static Integer getCodeByDesc(String desc){
		ConditionTypeEnum[] values = ConditionTypeEnum.values();
		for(ConditionTypeEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(Integer code){
		ConditionTypeEnum[] values = ConditionTypeEnum.values();
		for(ConditionTypeEnum value : values){
			if(value.getCode().equals(code)){
				return value.getDesc();
			}
		}
		return null;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
