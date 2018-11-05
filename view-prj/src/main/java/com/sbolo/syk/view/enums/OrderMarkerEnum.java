package com.sbolo.syk.view.enums;

import org.apache.commons.lang3.StringUtils;

import com.sbolo.syk.common.tools.StringUtil;

/**
 * 字典状态
 * @author Cathy
 *
 */
public enum OrderMarkerEnum {

	LATE("late", "create_time", "DESC"),
	EARLY("early", "create_time", "ASC"),
	HOT("hot", "like_count", "DESC");
	
	private String code;
	private String sort;
	private String direct;

	OrderMarkerEnum(String code, String sort, String direct){
		this.code = code;
		this.sort = sort;
		this.direct = direct;
	}
	
	public static OrderMarkerEnum getEnumByCode(String code){
		if(StringUtils.isBlank(code)) {
			return OrderMarkerEnum.LATE;
		}
		OrderMarkerEnum[] values = OrderMarkerEnum.values();
		for(OrderMarkerEnum value : values){
			if(value.getCode().equals(code)){
				return value;
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

}
