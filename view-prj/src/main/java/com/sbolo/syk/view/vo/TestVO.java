package com.sbolo.syk.view.vo;

import java.util.List;
import java.util.Map;

public class TestVO {
	private String pkey;
	private SykUserVO token;
	private List<SykMessageVO> hotMessages;
	private List<SykMessageVO> messages;
	private Map<String, Object> userTips;
	
	public TestVO(String pkey, SykUserVO token, List<SykMessageVO> hotMessages, List<SykMessageVO> messages, Map<String, Object> userTips) {
		this.pkey = pkey;
		this.token = token;
		this.hotMessages = hotMessages;
		this.messages = messages;
		this.userTips = userTips;
	}
	
	public String getPkey() {
		return pkey;
	}
	public void setPkey(String pkey) {
		this.pkey = pkey;
	}
	public SykUserVO getToken() {
		return token;
	}
	public void setToken(SykUserVO token) {
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

	public Map<String, Object> getUserTips() {
		return userTips;
	}

	public void setUserTips(Map<String, Object> userTips) {
		this.userTips = userTips;
	}
	
	
}
