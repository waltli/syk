package com.sbolo.syk.view.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.mvc.controller.BaseController;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.view.exception.OpenUserException;
import com.sbolo.syk.view.service.AccountService;
import com.sbolo.syk.view.vo.LoginPreVO;
import com.sbolo.syk.view.vo.SykUserVO;

import okhttp3.Response;

@RestController
public class LoginController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("cb_qq")
	public void cbQQ(HttpServletResponse response, HttpSession session, String code, String state) throws IOException {
		RequestResult<SykUserVO> result = null;
		try {
//			String qqState = String.valueOf(session.getAttribute("qqState"));
//			if(!state.equals(qqState)) {
//				throw new OpenUserException("登录条码不一致！");
//			}
			SykUserVO user = accountService.qqLogin(code);
			this.setUserInfo(session, user, user.getUsername());
			result = new RequestResult<>(user);
		} catch (Exception e) {
			result = RequestResult.error(e);
			result.setCode(360);
			log.error("", e);
		}
		String jsonData = JSON.toJSONString(result);
		StringBuilder js = new StringBuilder();
		js.append("<script type='text/javascript' charset='utf-8'>");
		js.append("try {");
		js.append("parent.syk.user.openBack(").append(jsonData).append(");");
		js.append("} catch (e) {");
		js.append("console.log(e);");
		js.append("}");
		js.append("</script>");
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write(js.toString());
		} catch (IOException e) {
			log.error("回写信息失败", e);
		} finally {
			if (pw != null) {
				pw.flush();
				pw.close();
			}
		}
	}
	
	@GetMapping("pre")
	public RequestResult<LoginPreVO> getLoginPre(Integer openType) throws UnsupportedEncodingException{
		LoginPreVO loginPre = new LoginPreVO(openType, "abc888888");
		loginPre.notNull();
		return new RequestResult<>(loginPre);
	}
	
	@PostMapping("logout")
	public RequestResult<String> logout(HttpSession session, String username) throws Exception{
		String hasUsername = this.getUserName(session);
		if(!hasUsername.equals(username)) {
			throw new Exception("当前登录用户不一致");
		}
		this.removeUser(session);
		return new RequestResult<>("success");
	}
}
