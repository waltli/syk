package com.sbolo.syk.common.enums;

public enum MovieTagEnum {
	China(1, "中国", "国产剧"),
	MainLand(1, "中国大陆", "国产剧"),
	Taiwan(1, "台湾", "国产剧"),
	USA(2, "美国", "欧美剧"),
	England(2, "英国", "欧美剧"),
	France(2, "法国", "欧美剧"),
	HongKong(3, "香港", "港剧"),
	Japan(4, "日本", "日剧"),
	Korea(5,"韩国", "韩剧"),
	Other(6,"其他", "其他");
	
	private int code;
	private String location;
	private String tag;

	MovieTagEnum(int code, String location, String tag){
		this.code = code;
		this.location = location;
		this.tag = tag;
	}
	
	public static String getTagByCode(int code){
		MovieTagEnum[] values = MovieTagEnum.values();
		for(MovieTagEnum value : values){
			if(value.getCode() == code){
				return value.getTag();
			}
		}
		return Other.getTag();
	}
	
	public static int getCodeByLocation(String location){
		MovieTagEnum[] values = MovieTagEnum.values();
		for(MovieTagEnum value : values){
			if(value.getLocation().equals(location)){
				return value.getCode();
			}
		}
		return Other.getCode();
	}
	
	public static String getTagByLocation(String location){
		MovieTagEnum[] values = MovieTagEnum.values();
		for(MovieTagEnum value : values){
			if(value.getLocation().equals(location)){
				return value.getTag();
			}
		}
		return Other.getTag();
	}
	
	public static MovieTagEnum getByLocation(String location){
		MovieTagEnum[] values = MovieTagEnum.values();
		for(MovieTagEnum value : values){
			if(value.getLocation().equals(location)){
				return value;
			}
		}
		return MovieTagEnum.Other;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	
}
