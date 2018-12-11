package com.sbolo.syk.view.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
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
		
		if(user != null){
			return true;
		}
		
		if(this.isXHR(request)) {
			RequestResult<String> result = new RequestResult<>();
			result.setCode(HttpStatus.UNAUTHORIZED.value());
			result.setError("请先登录。");
			String json = JSON.toJSONString(result);
			response.setContentType("application/json;charset=utf-8");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write(json);
		}else {
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
			//跳转到登录界面
			response.sendRedirect(contextPath+"/login?cb="+uri);
		}
		return false;
	}
	
	private boolean isXHR(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
            return true;
        }

        String uri = request.getRequestURI();
        if (StringUtils.containsIgnoreCase(uri, ".xhr") || 
        		StringUtils.containsIgnoreCase(uri, ".json") || 
        		StringUtils.containsIgnoreCase(uri, ".xml")) {
        	
            return true;
        }

        return false;
    }

}
