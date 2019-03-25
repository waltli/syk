package com.sbolo.syk.common.enums;

import com.sbolo.syk.common.tools.StringUtil;

/**
 * 
 * @author Cathy
 *
 */
public enum StatusEnum {


	CS(1, "初始"),
	JH(2, "激活"),
	ZF(3, "作废"),
	SC(4, "删除");
	
	private Integer code;
	private String desc;

	StatusEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static Integer getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		StatusEnum[] values = StatusEnum.values();
		for(StatusEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static Integer getCodeByDesc(String desc){
		StatusEnum[] values = StatusEnum.values();
		for(StatusEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(Integer code){
		StatusEnum[] values = StatusEnum.values();
		for(StatusEnum value : values){
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
