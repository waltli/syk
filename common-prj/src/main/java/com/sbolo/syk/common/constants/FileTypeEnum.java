package com.sbolo.syk.common.constants;

import com.sbolo.syk.common.tools.StringUtil;

public enum FileTypeEnum {
	PIC("pic", "图片"),
	FOLDER("folder", "文件夹"),
	PDF("pdf", "pdf");
	
	
	private String code;
	private String desc;

	FileTypeEnum(String code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public static String getCodeByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		FileTypeEnum[] values = FileTypeEnum.values();
		for(FileTypeEnum value : values){
			if(value.name().equals(name)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getCodeByDesc(String desc){
		FileTypeEnum[] values = FileTypeEnum.values();
		for(FileTypeEnum value : values){
			if(value.getDesc().equals(desc)){
				return value.getCode();
			}
		}
		return null;
	}
	
	public static String getDescByCode(String code){
		FileTypeEnum[] values = FileTypeEnum.values();
		for(FileTypeEnum value : values){
			if(value.getCode().equals(code)){
				return value.getDesc();
			}
		}
		return null;
	}
	
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	
}
