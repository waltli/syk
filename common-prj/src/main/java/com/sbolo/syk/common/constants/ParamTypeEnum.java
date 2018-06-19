package com.sbolo.syk.common.constants;

import com.sbolo.syk.common.tools.StringUtil;

/**
 * 
 * 方法参数类型
 * @author Cathy
 *
 */
public enum ParamTypeEnum {


	BYTE(1, "byte"),
	SHORT(2, "short"),
	INT(3, "int"),
	LONG(4, "long"),
	FLOAT(5, "float"),
	DOUBLE(6, "double"),
	BOOLEAN(7, "boolean"),
	CHAR(8, "char"),
	STRING(9, "String"),
	LIST(10, "List"),
	OBJECT(11, "Object"),
	VOID(12,"void");
	
	
	private Integer code;
	private String desc;

	ParamTypeEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static Integer getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		ParamTypeEnum[] values = ParamTypeEnum.values();
		for(ParamTypeEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static Integer getCodeByDesc(String desc){
		ParamTypeEnum[] values = ParamTypeEnum.values();
		for(ParamTypeEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(Integer code){
		ParamTypeEnum[] values = ParamTypeEnum.values();
		for(ParamTypeEnum value : values){
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
