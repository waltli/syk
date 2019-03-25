package com.sbolo.syk.common.enums;

import com.sbolo.syk.common.tools.StringUtil;

public enum TimeDirectEnum {
	UP_IN_YEAR(1, "yyyy", "年的最后月"),
	UP_IN_MONTH(2, "yyyy-MM", "月的最后一天"),
	UP_IN_DAY(3, "yyyy-MM-dd", "日的最后一小时"),
	UP_IN_HOUR(4, "yyyy-MM-dd HH", "小时的最后一分钟"),
	UP_IN_MINUTE(5, "yyyy-MM-dd HH:mm", "分钟的最后一秒"),
	
	DOWN_IN_YEAR(6, "yyyy", "年的开始月"),
	DOWN_IN_MONTH(7, "yyyy-MM", "月的开始日"),
	DOWN_IN_DAY(8, "yyyy-MM-dd", "日的开始时"),
	DOWN_IN_HOUR(9, "yyyy-MM-dd HH", "时的开始分"),
	DOWN_IN_MINUTE(10, "yyyy-MM-dd HH:mm", "分的开始秒");
	
	
	private Integer code;
	private String format;
	private String desc;

	TimeDirectEnum(Integer code, String format, String desc){
		this.code = code;
		this.format = format;
		this.desc = desc;
	}
	
	public static Integer getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		TimeDirectEnum[] values = TimeDirectEnum.values();
		for(TimeDirectEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static Integer getCodeByDesc(String desc){
		TimeDirectEnum[] values = TimeDirectEnum.values();
		for(TimeDirectEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(Integer code){
		TimeDirectEnum[] values = TimeDirectEnum.values();
		for(TimeDirectEnum value : values){
			if(value.getCode() == code){
				return value.getDesc();
			}
		}
		return null;
	}
	
	public static String getFormatByCode(Integer code){
		TimeDirectEnum[] values = TimeDirectEnum.values();
		for(TimeDirectEnum value : values){
			if(value.getCode() == code){
				return value.getFormat();
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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	
}
