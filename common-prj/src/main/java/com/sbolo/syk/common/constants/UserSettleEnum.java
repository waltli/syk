package com.sbolo.syk.common.constants;

import org.apache.commons.lang3.StringUtils;

import com.sbolo.syk.common.tools.StringUtil;

/**
 * 字典状态
 * @author Cathy
 *
 */
public enum UserSettleEnum {

	DOWN(1, "已设置"),
	NOT(0, "未设置");
	
	private Integer code;
	private String direct;

	UserSettleEnum(Integer code, String direct){
		this.code = code;
		this.direct = direct;
	}
	
	public static Integer getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		DicStEnum[] values = DicStEnum.values();
		for(DicStEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static Integer getCodeByDesc(String desc){
		DicStEnum[] values = DicStEnum.values();
		for(DicStEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(Integer code){
		DicStEnum[] values = DicStEnum.values();
		for(DicStEnum value : values){
			if(value.getCode().equals(code)){
				return value.getDesc();
			}
		}
		return null;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
