package com.sbolo.syk.fetch.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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
		Object isLogin = session.getAttribute("isLogin");
		
		if(isLogin != null){
			if((boolean) isLogin){
				return true;
			}
		}
		//获取请求的URL
		String url = request.getRequestURI();
		String contextPath = request.getContextPath();
		String uri = url.replace(contextPath, "");
		
		Map<String, String[]> parameterMap = request.getParameterMap();
		if(parameterMap != null && parameterMap.size() > 0) {
			String params = "";
			for(String key : parameterMap.keySet()) {
				String[] strings = parameterMap.get(key);
				String val = "";
				for(String str :strings) {
					val += ("," + str);
				}
				val = val.substring(1);
				params  = params + key + "=" + val + "&";
			}
			if(StringUtils.isNotBlank(params)) {
				params = "?"+params.substring(0, params.length()-1);
				uri += params;
			}
		}
		
		
		//不符合条件的，跳转到登录界面
		response.sendRedirect(contextPath+"/login?cb="+uri);
		
		return false;
	}

}
