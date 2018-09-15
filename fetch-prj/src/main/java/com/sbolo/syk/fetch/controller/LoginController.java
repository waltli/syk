package com.sbolo.syk.fetch.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
	private static final String login = "login.html";
	
	@RequestMapping(value="login")
	public String login(Model model, 
			@RequestParam(value="cb", required=false)String cbUrl){
		if(StringUtils.isNotBlank(cbUrl)){
			model.addAttribute("cb", cbUrl);
		}
		return login;
	}

	@RequestMapping(value="login-work", method=RequestMethod.POST)
	public String login(HttpSession session, String user, String pwd, 
			@RequestParam(value="cb", required=false)String cbUrl){
		
		if(user.equals("syk") && pwd.equals("adminchanying123")){
			session.setAttribute("isLogin", true);
		}
		
		if(StringUtils.isNotBlank(cbUrl)){
			return "redirect:"+cbUrl;
		}
		
		return "redirect:/movie/list";
	}
}
