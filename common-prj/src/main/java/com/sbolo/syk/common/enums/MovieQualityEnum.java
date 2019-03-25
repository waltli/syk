package com.sbolo.syk.common.enums;

import com.sbolo.syk.common.tools.StringUtil;

public enum MovieQualityEnum {
	CAM(1, "CAM"),
	HDCAM(1,"HDCAM"),
	
	TS(2, "TS"),
	HDTS(2, "HDTS"),
	TC(3, "TC"),
	HDTC(3, "HDTC"),
	
	DVD(5,"DVD"),
	DVDSCR(5, "DVDSCR"),
	SCR(5, "DVDSCR"),
	DVDRIP(6,"DVDRip"),
	
	R5(6,"R5"),
	
	TVRIP(6,"TVRip"),
	HDRIP(6,"HDRip"),
	HDTV(7,"HDTV"),
	HDDVD(7,"HDDVD"),
	HD(7,"HD"),
	
	WEBDL(8,"WEB-DL"),
	WEB(8,"Web"),
	WEBRIP(8,"WebRip"),
	
	BRRIP(9,"BRRip"),
	BR(9,"BluRay"),
	BDRIP(9,"BD"),
	BLURAY(9,"BluRay"),
	BLURAYDISC(9,"BD"),
	BD(9,"BD"),
	
	REMUX(10,"REMUX");
	
	private int score;
	private String quality;

	MovieQualityEnum(int score, String quality){
		this.score = score;
		this.quality = quality;
	}
	
	public static int getScoreByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		MovieQualityEnum[] values = MovieQualityEnum.values();
		for(MovieQualityEnum value : values){
			if(value.name().equals(name)){
				return value.getScore();
			}
		}
		return 0;
	}
	
	public static boolean isWorst(String quality){
		if(quality == null){
			return false;
		}
		quality = StringUtil.replaceBlank2(quality).toUpperCase();
		MovieQualityEnum[] values = MovieQualityEnum.values();
		for(MovieQualityEnum value : values){
			if(value.getScore() <= 4 && quality.equals(value.name())){
				return true;
			}
		}
		return false;
	}
	
	public static MovieQualityEnum getEnumByName(String name){
		if(name == null){
			return null;
		}
		MovieQualityEnum[] values = MovieQualityEnum.values();
		for(MovieQualityEnum value : values){
			if(value.name().equals(name)){
				return value;
			}
		}
		if("蓝光".equals(name)){
			return MovieQualityEnum.BLURAY;
		}
		return null;
	}
	
	public static void main(String[] args) {
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}
	
	
}
