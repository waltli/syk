package com.sbolo.syk.view.enums;

import org.apache.commons.lang3.StringUtils;

import com.sbolo.syk.common.tools.StringUtil;

/**
 * 字典状态
 * @author Cathy
 *
 */
public enum OpenTypeEnum {

	QQ(1, "qq"),
	WEIBO(2, "weibo");
	
	private Integer code;
	private String direct;

	OpenTypeEnum(Integer code, String direct){
		this.code = code;
		this.direct = direct;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

}
