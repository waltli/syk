package com.sbolo.syk.view.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.view.exception.OpenUserException;
import com.sbolo.syk.view.service.LoginService;
import com.sbolo.syk.view.vo.LoginPreVO;
import com.sbolo.syk.view.vo.SykUserVO;

import okhttp3.Response;

@RestController
@RequestMapping("login")
public class LoginController {
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private LoginService loginService;
	
	@GetMapping("cb_qq")
	public RequestResult<SykUserVO> cbQQ(HttpSession session, String code, String state) {
		RequestResult<SykUserVO> result = null;
		try {
//			String qqState = String.valueOf(session.getAttribute("qqState"));
//			if(!state.equals(qqState)) {
//				throw new OpenUserException("登录条码不一致！");
//			}
			SykUserVO user = loginService.qqLogin(code);
			result = new RequestResult<>(user);
		} catch (Exception e) {
			result = RequestResult.error(e);
			result.setCode(360);
			log.error("", e);
		}
		
		return result;
	}
	
	@GetMapping("pre")
	public RequestResult<LoginPreVO> getLoginPre(Integer openType) throws UnsupportedEncodingException{
		LoginPreVO loginPre = new LoginPreVO(openType, "abc888888");
		loginPre.notNull();
		return new RequestResult<>(loginPre);
	}
}
