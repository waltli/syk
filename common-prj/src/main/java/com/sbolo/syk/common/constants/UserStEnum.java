package com.sbolo.syk.common.constants;

import com.sbolo.syk.common.tools.StringUtil;

/**
 * 字典状态
 * @author Cathy
 *
 */
public enum UserStEnum {

	TY(0, "停用"),
	QY(1, "启用");
	
	private Integer code;
	private String desc;

	UserStEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static Integer getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		UserStEnum[] values = UserStEnum.values();
		for(UserStEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static Integer getCodeByDesc(String desc){
		UserStEnum[] values = UserStEnum.values();
		for(UserStEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(Integer code){
		UserStEnum[] values = UserStEnum.values();
		for(UserStEnum value : values){
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
