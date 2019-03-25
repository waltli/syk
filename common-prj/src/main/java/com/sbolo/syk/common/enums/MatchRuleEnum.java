package com.sbolo.syk.common.enums;

import com.sbolo.syk.common.tools.StringUtil;

public enum MatchRuleEnum {

	MIN_MATCH(0, "最小匹配规则"),
	MAX_MATCH(1, "最大匹配规则");
	
	private Integer code;
	private String desc;

	MatchRuleEnum(Integer code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static Integer getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		MatchRuleEnum[] values = MatchRuleEnum.values();
		for(MatchRuleEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static Integer getCodeByDesc(String desc){
		MatchRuleEnum[] values = MatchRuleEnum.values();
		for(MatchRuleEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(Integer code){
		MatchRuleEnum[] values = MatchRuleEnum.values();
		for(MatchRuleEnum value : values){
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
