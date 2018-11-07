package com.sbolo.syk.view.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.mvc.controller.BaseController;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.view.entity.SykMessageEntity;
import com.sbolo.syk.view.entity.SykMessageLikeEntity;
import com.sbolo.syk.view.enums.OrderMarkerEnum;
import com.sbolo.syk.view.service.SykMessageLikeService;
import com.sbolo.syk.view.service.SykMessageService;
import com.sbolo.syk.view.vo.SykMessageVO;
import com.sbolo.syk.view.vo.SykUserVO;
import com.sbolo.syk.view.vo.TestVO;

@RestController
@RequestMapping("msg")
public class MessageController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	private SykMessageService sykMessageService;
	
	@Autowired
	private SykMessageLikeService sykMessageLikeService;
	
	private static final Integer pageSize = 10;
	
	@PostMapping("push")
	public RequestResult<String> pushMessage(HttpServletRequest request, HttpSession session, SykMessageVO vo) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		String clientIP = this.getClientIP(request);
		SykUserVO sykUser = (SykUserVO) this.getUser(request);
		String userAgent = this.getUserAgent(request);
		sykMessageService.addOne(vo, sykUser.getPrn(), clientIP, userAgent);
		return new RequestResult<>("success");
	}
	
	@GetMapping("gets")
	public RequestResult<TestVO> getMessages(
			@RequestParam(value="pkey",required=true) String pkey,
			@RequestParam(value="page",defaultValue="1", required=false) Integer pageNum,
			@RequestParam(value="orderMarker", required=false) String orderMarker,
			HttpServletRequest request) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		SykUserVO token = (SykUserVO) this.getUser(request);
		TestVO test = sykMessageService.getListByPage(pkey, token, pageNum, pageSize, orderMarker);
		return new RequestResult<>(test);
	}
	
	@PostMapping("like")
	public RequestResult<Map<String, Object>> like(HttpServletRequest request, String msgPrn){
		SykUserVO token = (SykUserVO) this.getUser(request);
		String gaverIp = this.getClientIP(request);
		SykMessageLikeEntity likeEntity = sykMessageLikeService.getByMsgPrnAndGaverPrn(msgPrn, token.getPrn());
		boolean marker = true;
		if(likeEntity == null) {
			sykMessageService.giveLike(msgPrn, token.getPrn(), gaverIp);
		}else {
			sykMessageService.backLike(msgPrn, likeEntity.getPrn());
			marker = false;
		}
		SykMessageEntity one = sykMessageService.getOne(msgPrn);
		Map<String, Object> m = new HashMap<>();
		m.put("likeCount", one.getLikeCount());
		m.put("marker", marker);
		return new RequestResult<>(m);
	}
	
	@PostMapping("delete")
	public RequestResult<String> delete(String msgPrn){
		List<String> msgPrnl = sykMessageService.getByParentPrns(msgPrn);
		if(msgPrnl == null || msgPrnl.size() == 0) {
			throw new BusinessException("未获取到消息数据。");
		}
		sykMessageService.remove(msgPrnl);
		return new RequestResult<>("success");
	}
	
}
