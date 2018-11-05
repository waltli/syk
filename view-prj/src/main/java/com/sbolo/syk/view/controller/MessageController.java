package com.sbolo.syk.view.controller;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbolo.syk.common.mvc.controller.BaseController;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.view.enums.OrderMarkerEnum;
import com.sbolo.syk.view.service.SykMessageLikeService;
import com.sbolo.syk.view.service.SykMessageService;
import com.sbolo.syk.view.vo.SykMessageVO;
import com.sbolo.syk.view.vo.SykUserVO;
import com.sbolo.syk.view.vo.TestVO;

@Controller
public class MessageController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	private SykMessageService sykMessageService;
	
	@Autowired
	private SykMessageLikeService sykMessageLikeService;
	
	private static final Integer pageSize = 10;
	
	@PostMapping("pushMessage")
	@ResponseBody
	public RequestResult<String> pushMessage(HttpServletRequest request, HttpSession session, SykMessageVO vo) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		String clientIP = this.getClientIP(request);
		SykUserVO sykUser = (SykUserVO) this.getUser(request);
		String userAgent = this.getUserAgent(request);
		sykMessageService.addOne(vo, sykUser.getPrn(), clientIP, userAgent);
		return new RequestResult<>("success");
	}
	
	@GetMapping("getMessages")
	@ResponseBody
	public RequestResult<TestVO> getMessages(
			@RequestParam(value="pkey",required=true) String pkey,
			@RequestParam(value="page",defaultValue="1", required=false) Integer pageNum,
			@RequestParam(value="orderMarker", required=false) String orderMarker,
			HttpServletRequest request) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		SykUserVO token = (SykUserVO) this.getUser(request);
		TestVO test = sykMessageService.getListByPage(pkey, token, pageNum, pageSize, orderMarker);
		return new RequestResult<>(test);
	}
	
}
