package com.sbolo.syk.view.vo;

import java.util.List;
import java.util.Map;

public class TestVO {
	private String pkey;
	private SykUsersVO token;
	private List<SykMessageVO> hotMessages;
	private List<SykMessageVO> messages;
	private Map<String, Integer> userTips;
	public String getPkey() {
		return pkey;
	}
	public void setPkey(String pkey) {
		this.pkey = pkey;
	}
	public SykUsersVO getToken() {
		return token;
	}
	public void setToken(SykUsersVO token) {
		this.token = token;
	}
	public List<SykMessageVO> getHotMessages() {
		return hotMessages;
	}
	public void setHotMessages(List<SykMessageVO> hotMessages) {
		this.hotMessages = hotMessages;
	}
	public List<SykMessageVO> getMessages() {
		return messages;
	}
	public void setMessages(List<SykMessageVO> messages) {
		this.messages = messages;
	}
	public Map<String, Integer> getUserTips() {
		return userTips;
	}
	public void setUserTips(Map<String, Integer> userTips) {
		this.userTips = userTips;
	}
	
	
}
