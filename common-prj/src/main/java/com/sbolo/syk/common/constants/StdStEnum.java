package com.sbolo.syk.common.constants;

import com.sbolo.syk.common.tools.StringUtil;

public enum StdStEnum {



	YX(1, "有效标准"),
	ZF(2, "作废标准");
	
	private Integer code;
	private String desc;

	StdStEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static Integer getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		StdStEnum[] values = StdStEnum.values();
		for(StdStEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static Integer getCodeByDesc(String desc){
		StdStEnum[] values = StdStEnum.values();
		for(StdStEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(Integer code){
		StdStEnum[] values = StdStEnum.values();
		for(StdStEnum value : values){
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
