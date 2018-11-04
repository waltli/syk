package com.sbolo.syk.view.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbolo.syk.common.mvc.controller.BaseController;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.view.service.SykMessageLikeService;
import com.sbolo.syk.view.service.SykMessageService;
import com.sbolo.syk.view.vo.SykMessageVO;
import com.sbolo.syk.view.vo.SykUsersVO;

@Controller
public class MessageController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	private SykMessageService sykMessageService;
	
	@Autowired
	private SykMessageLikeService sykMessageLikeService;
	
	@PostMapping("pushMessage")
	@ResponseBody
	public RequestResult<String> pushMessage(HttpServletRequest request, HttpSession session, SykMessageVO vo){
		String clientIP = this.getClientIP(request);
		SykUsersVO sykUser = (SykUsersVO) this.getUser(request);
		String userAgent = this.getUserAgent(request);
		sykMessageService.addOne(vo, sykUser.getPrn(), clientIP, userAgent);
		return new RequestResult<>("success");
	}
	
}
