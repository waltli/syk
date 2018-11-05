package com.sbolo.syk.fetch.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbolo.syk.common.tools.VOUtils;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.fetch.entity.SykUserEntity;
import com.sbolo.syk.fetch.service.SykUserService;
import com.sbolo.syk.fetch.vo.SykUserVO;

@Controller
public class LoginController {
	
	private static final String login = "login.html";
	
	@Autowired
	private SykUserService sykUserService;
	
	@RequestMapping("")
	public String go(){
		return "redirect:/movie/list";
	}
	
	@RequestMapping(value="login")
	public String login(Model model, 
			@RequestParam(value="cb", required=false)String cbUrl){
		if(StringUtils.isNotBlank(cbUrl)){
			model.addAttribute("cb", cbUrl);
		}
		return login;
	}

	@RequestMapping(value="login-work", method=RequestMethod.POST)
	@ResponseBody
	public RequestResult<Map<String, Object>> login(HttpSession session, String username, String password, 
			@RequestParam(value="cb", required=false)String cbUrl) throws Exception{
		RequestResult<Map<String, Object>> result = null;
		SykUserEntity userEntity = sykUserService.getOneByUsernamePassword(username, password);
		
		if(userEntity == null){
			result = RequestResult.error("用户名或密码错误");
			return result;
		}
		
		SykUserVO userVO = VOUtils.po2vo(userEntity, SykUserVO.class);
		
		session.setAttribute("user", userVO);
		
		Map<String, Object> m = new HashMap<>();
		
		m.put("user", userVO);
		m.put("cbUrl", cbUrl);
		result = new RequestResult<>(m);
		
		return result;
	}
}
