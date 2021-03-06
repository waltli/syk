package com.sbolo.syk.view.controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.util.HtmlUtils;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.enums.MatchRuleEnum;
import com.sbolo.syk.common.enums.TriggerEnum;
import com.sbolo.syk.common.exception.BusinessException;
import com.sbolo.syk.common.http.HttpUtils;
import com.sbolo.syk.common.http.HttpUtils.HttpResult;
import com.sbolo.syk.common.http.callback.HttpSendCallback;
import com.sbolo.syk.common.mvc.controller.BaseController;
import com.sbolo.syk.common.tools.SensitiveWordUtils;
import com.sbolo.syk.common.ui.ResultApi;
import com.sbolo.syk.view.entity.SykMessageEntity;
import com.sbolo.syk.view.entity.SykMessageLikeEntity;
import com.sbolo.syk.view.enums.OrderMarkerEnum;
import com.sbolo.syk.view.service.MovieInfoService;
import com.sbolo.syk.view.service.SykMessageLikeService;
import com.sbolo.syk.view.service.SykMessageService;
import com.sbolo.syk.view.vo.SykMessageVO;
import com.sbolo.syk.view.vo.SykUserVO;
import com.sbolo.syk.view.vo.TestVO;

import okhttp3.Response;

@RestController
@RequestMapping("msg")
public class MessageController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	private SykMessageService sykMessageService;
	
	@Autowired
	private SykMessageLikeService sykMessageLikeService;
	
	@Autowired
	private MovieInfoService movieInfoService;
	
	private static final Integer pageSize = 10;
	
	@PostMapping("push")
	public ResultApi<SykMessageVO> pushMessage(HttpServletRequest request, HttpSession session, SykMessageVO vo) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		String clientIP = this.getClientIP(request);
		String userAgent = this.getUserAgent(request);
		SykUserVO sykUser = (SykUserVO) this.getUser(request);
		vo.notNull(sykUser.getPrn(), clientIP, userAgent);
		String msgContent = vo.getMsgContent();
		vo.setMsgContent(HtmlUtils.htmlEscape(msgContent));
		Set<String> sensitiveWords = SensitiveWordUtils.getSensitiveWords(msgContent, MatchRuleEnum.MIN_MATCH);
		if(sensitiveWords == null || sensitiveWords.size() <= 0) {
			sykMessageService.addOne(vo, clientIP);
			movieInfoService.lazyHot(vo.getPkey(), TriggerEnum.comment.getCode(), clientIP);
		}
		vo.setAuthor(sykUser);
		vo.parse();
		return new ResultApi<>(vo);
	}
	
	@GetMapping("gets")
	public ResultApi<TestVO> getMessages(
			@RequestParam(value="pkey",required=true) String pkey,
//			@RequestParam(value="page",defaultValue="1", required=false) Integer pageNum,
			@RequestParam(value="orderMarker", required=false) String orderMarker,
			HttpServletRequest request, HttpSession session) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		SykUserVO token = (SykUserVO) this.getUser(request);
//		if(token == null) {
//			token = new SykUserVO();
//			token.setPrn("41877");
//			token.setAvatarUri("//qzapp.qlogo.cn/qzapp/101263695/E7F30E126A43785C2F97053AE2162341/100");
//			token.setNickname("qxw");
//			session.setAttribute(CommonConstants.USER, token);
//		}
		TestVO test = sykMessageService.getListByPage(pkey, token, orderMarker);
		return new ResultApi<>(test);
	}
	
	@PostMapping("like")
	public ResultApi<Map<String, Object>> like(HttpServletRequest request, String msgPrn){
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
		return new ResultApi<>(m);
	}
	
	@PostMapping("delete")
	public ResultApi<String> delete(HttpServletRequest request, String msgPrn){
		SykUserVO token = (SykUserVO) this.getUser(request);
		SykMessageEntity msgEntity = sykMessageService.getByPrn(msgPrn);
		if(msgEntity == null) {
			throw new BusinessException("获取数据失败！");
		}
		if(!token.getPrn().equals(msgEntity.getAuthorPrn())) {
			throw new BusinessException("无法操作别人的数据！");
		}
		
		List<String> msgPrnl = sykMessageService.getByPrnLine(msgPrn);
		if(msgPrnl == null || msgPrnl.size() == 0) {
			throw new BusinessException("未获取到消息数据。");
		}
		sykMessageService.remove(msgPrnl, msgEntity.getPkey());
		return new ResultApi<>("success");
	}
	
}
