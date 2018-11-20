package com.sbolo.syk.common.constants;

import org.apache.commons.lang3.StringUtils;

import com.sbolo.syk.common.tools.StringUtil;

/**
 * 0000 0000 0000 0000
 * 高级管理员 管理员 用户 黑名单
 * @author Walter
 *
 */
public enum RoleEnum {
	/*最高权限
	二进制 1000 0000 0000 0000
	十进制   32768
	十六进制 8000*/
	ROOT(32768L, "1000000000000000", "root", "最高权限"),
	
	/*初级管理员
	二进制 0000 0001 0000 0000
	十进制   256
	十六进制 100*/
	MG1(256L, "0000000100000000", "mg1", "初级管理员"),
	
	/*初级用户
	二进制 0000 0000 0001 0000
	十进制   16
	十六进制 10*/
	USER1(16L, "0000000000010000", "user1", "初级用户");
	
	private Long dec;
	private String bin;
	private String title;
	private String direct;

	RoleEnum(Long dec, String bin, String title, String direct){
		this.dec = dec;
		this.bin = bin;
		this.title = title;
		this.direct = direct;
	}
	
	public static String getBinByName(String name){
		name = StringUtil.replaceBlank2(name).toUpperCase();
		RoleEnum[] values = RoleEnum.values();
		for(RoleEnum value : values){
			if(value.name().equals(name)){
				return value.getBin();
			}
		}
		return null;
	}
	
	public static String getBinByTitle(String title){
		title = StringUtil.replaceBlank2(title).toUpperCase();
		RoleEnum[] values = RoleEnum.values();
		for(RoleEnum value : values){
			if(value.name().equals(title)){
				return value.getBin();
			}
		}
		return null;
	}
	
	public Long getDec() {
		return dec;
	}

	public void setDec(Long dec) {
		this.dec = dec;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

}
