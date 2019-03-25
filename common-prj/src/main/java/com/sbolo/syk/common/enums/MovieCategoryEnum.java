package com.sbolo.syk.common.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sbolo.syk.common.tools.StringUtil;

public enum MovieCategoryEnum {
	movie(1, "电影"),
	tv(2,"剧集");
//	variety(3,"综艺");
	
	private int code;
	private String desc;

	MovieCategoryEnum(int code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static int getCodeByDesp(String desp){
		MovieCategoryEnum[] values = MovieCategoryEnum.values();
		for(MovieCategoryEnum value : values){
			if(value.getDesc().equals(desp)){
				return value.getCode();
			}
		}
		return 0;
	}
	
	public static int getCodeByName(String name){
		MovieCategoryEnum[] values = MovieCategoryEnum.values();
		for(MovieCategoryEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return 0;
	}

	public static List<String> getDesps(){
		List<String> desps = new ArrayList<>();
		MovieCategoryEnum[] values = MovieCategoryEnum.values();
		for(MovieCategoryEnum value : values){
			desps.add(value.getDesc());
		}
		return desps;
	}
	
	public static Map<Integer, String> toMap(){
		Map<Integer, String> map = new HashMap<>();
		
		MovieCategoryEnum[] values = MovieCategoryEnum.values();
		for(MovieCategoryEnum value : values){
			map.put(value.getCode(), value.getDesc());
		}
		return map;
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
