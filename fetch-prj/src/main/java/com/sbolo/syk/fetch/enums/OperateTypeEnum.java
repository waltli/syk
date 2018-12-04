package com.sbolo.syk.fetch.enums;

public enum OperateTypeEnum {

	insert(1, "插入"),
	update(2, "更新");
	
	private Integer code;
	private String descp;

	OperateTypeEnum(Integer code, String descp){
		this.code = code;
		this.descp = descp;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

}
