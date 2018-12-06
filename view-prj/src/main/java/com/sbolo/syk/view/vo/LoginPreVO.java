package com.sbolo.syk.view.vo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.sbolo.syk.common.tools.ConfigUtils;

public class LoginPreVO {
	private String authorizeUrl;
	private String appid;
	private String cbUrlEncode;
	private String state;
	private String scope;
	private Integer openType;
	
	public LoginPreVO(Integer openType, String state) {
		this.state = state;
		this.openType = openType;
	}
	
	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
	}
	
	public String getAuthorizeUrl() {
		return authorizeUrl;
	}
	public void setAuthorizeUrl(String authorizeUrl) {
		this.authorizeUrl = authorizeUrl;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getCbUrlEncode() {
		return cbUrlEncode;
	}
	public void setCbUrlEncode(String cbUrlEncode) {
		this.cbUrlEncode = cbUrlEncode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public void notNull() throws UnsupportedEncodingException {
		String appid = ConfigUtils.getPropertyValue("open.qq.appid");
		String scope = ConfigUtils.getPropertyValue("open.qq.scope");
		String callbackUrl = ConfigUtils.getPropertyValue("open.qq.callback");
		String cbUrlEncode = URLEncoder.encode(callbackUrl,"utf-8");
		this.setAppid(appid);
		this.setCbUrlEncode(cbUrlEncode);
		this.setScope(scope);
		this.setAuthorizeUrl("https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id="+appid+"&redirect_uri="+cbUrlEncode+"&state="+state+"&scope="+scope);
	}
}
