package com.sbolo.syk.common.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.sbolo.syk.common.constants.CommonConstants;

public class BaseController {
	protected Object getUser(HttpServletRequest request) {
		if(request == null){
			throw new RuntimeException("The parameter \"request\" that must be available!");
		}
		return getUser(request.getSession());
	}
	
	protected Object getUser(HttpSession session) {
		if(session == null){
			throw new RuntimeException("The parameter \"session\" that must be available!");
		}
		Object attribute = session.getAttribute(CommonConstants.USER);
		return attribute;
	}
	
	protected String getUserName(HttpServletRequest request){
		if(request == null){
			throw new RuntimeException("The parameter \"request\" that must be available!");
		}
		return getUserName(request.getSession());
	}
	
	protected String getUserName(HttpSession session){
		if(session == null){
			throw new RuntimeException("The parameter \"session\" that must be available!");
		}
		Object attribute = session.getAttribute(CommonConstants.USERNAME);
		if(attribute != null){
			return attribute.toString();
		}
		return null;
	}
	
	protected String getHost(HttpServletRequest request){
		if(request == null){
			throw new RuntimeException("The parameter \"request\" that must be available!");
		}
		int serverPort = request.getServerPort();
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		String contextPath = request.getContextPath();
		
		StringBuffer sb = new StringBuffer();
		sb.append(scheme).append(serverName).append(":").append(serverPort).append(contextPath);
		return sb.toString();
	}
	
	protected String getClientIP(HttpServletRequest request) {
		if(request == null){
			throw new RuntimeException("The parameter \"request\" that must be available!");
		}
		
		String forwardIP = request.getHeader("x-forwarded-for");
		if(StringUtils.isNotBlank(forwardIP)) {
			return forwardIP.split(",")[0];
		}

		String realIP = request.getHeader("X-Real-IP");
		if(StringUtils.isNotBlank(realIP)) {
			return realIP;
		}
		
		return request.getRemoteAddr();
	}
	
	protected String getUserAgent(HttpServletRequest request) {
		if(request == null){
			throw new RuntimeException("The parameter \"request\" that must be available!");
		}
		
		return request.getHeader("User-Agent");
	}
}
