package com.sbolo.syk.common.constants;

import com.sbolo.syk.common.tools.StringUtil;

public enum ContentTypeEnum {
	JPG("jpg", "image/jpeg"),
	JPEG("jpeg", "image/jpeg"),
	PNG("png", "image/png"),
	GIF("gif", "image/gif"),
	ICO("ico", "image/x-icon"),
	BMP("bmp", "application/x-bmp"),
	TORRENT("torrent", "application/x-bittorrent");
	
	private String format;
	private String contentType;
	
	ContentTypeEnum(String format, String contentType){
		this.format = format;
		this.contentType = contentType;
	}
	
	public static String getFormatByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		ContentTypeEnum[] values = ContentTypeEnum.values();
		for(ContentTypeEnum value : values){
			if(value.name().equals(name)){
				return value.getFormat();
			}
		}
		return null;
	}
	
	public static String getFormatByContentType(String contentType){
		ContentTypeEnum[] values = ContentTypeEnum.values();
		for(ContentTypeEnum value : values){
			if(value.getContentType().equals(contentType)){
				return value.getFormat();
			}
		}
		return null;
	}
	
	public static String getContentTypeByFormat(String format){
		ContentTypeEnum[] values = ContentTypeEnum.values();
		for(ContentTypeEnum value : values){
			if(value.getFormat().equals(format)){
				return value.getContentType();
			}
		}
		return null;
	}
	
	public static ContentTypeEnum getByFormat(String format){
		ContentTypeEnum[] values = ContentTypeEnum.values();
		for(ContentTypeEnum value : values){
			if(value.getFormat().equals(format)){
				return value;
			}
		}
		return null;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
