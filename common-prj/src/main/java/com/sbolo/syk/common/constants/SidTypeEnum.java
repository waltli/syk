package com.sbolo.syk.common.constants;

import com.sbolo.syk.common.tools.StringUtil;

public enum SidTypeEnum {

	CL(1, "常量"),
	XH(2, "序号"),
	DROOLS(3, "drools"),
	ZCZD(4, "组成字段");
	
	private Integer code;
	private String desc;

	SidTypeEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static Integer getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		SidTypeEnum[] values = SidTypeEnum.values();
		for(SidTypeEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static Integer getCodeByDesc(String desc){
		SidTypeEnum[] values = SidTypeEnum.values();
		for(SidTypeEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(Integer code){
		SidTypeEnum[] values = SidTypeEnum.values();
		for(SidTypeEnum value : values){
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
