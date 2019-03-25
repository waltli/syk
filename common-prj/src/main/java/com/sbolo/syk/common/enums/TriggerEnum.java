package com.sbolo.syk.common.enums;

import com.sbolo.syk.common.tools.StringUtil;

public enum TriggerEnum {
	click(1, "点击"),
	download(2,"下载"),
	comment(3,"评论");
	
	private int code;
	private String desc;

	TriggerEnum(int code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static int getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		TriggerEnum[] values = TriggerEnum.values();
		for(TriggerEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return 0;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	
}
