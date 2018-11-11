package com.sbolo.syk.view.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.constants.CommonConstants;
import com.sbolo.syk.common.ui.RequestResult;
import com.sbolo.syk.view.vo.SykUserVO;

public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		//获取Session
		HttpSession session = request.getSession();
		Object user = session.getAttribute(CommonConstants.USER);
		
		if(user == null){
			SykUserVO sykUser = new SykUserVO();
			sykUser.setPrn("41877");
			sykUser.setAvatarUri("//qzapp.qlogo.cn/qzapp/101263695/E7F30E126A43785C2F97053AE2162341/100");
			sykUser.setNickname("qxw");
			session.setAttribute(CommonConstants.USER, sykUser);
			
//			RequestResult<String> result = new RequestResult<>();
//			result.setCode(300);
//			result.setError("请先登录。");
//			String json = JSON.toJSONString(result);
//			response.setContentType("application/json;charset=utf-8");
//			response.getWriter().write(json);
//			return false;
		}
		return true;
//		
	}

}
