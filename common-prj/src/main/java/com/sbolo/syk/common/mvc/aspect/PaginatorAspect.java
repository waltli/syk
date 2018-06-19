package com.sbolo.syk.common.mvc.aspect;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;

@Aspect
@Component
public class PaginatorAspect {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Around("@annotation(com.sbolo.syk.common.annotation.Paginator)")
    public Object paginator(ProceedingJoinPoint point) throws Throwable {
		Object obj = point.proceed();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Map<String, Object> params = fetchParams(request);
		String paramsJson = JSON.toJSONString(params);
		Signature signature = point.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method targetMethod = methodSignature.getMethod();
		ResponseBody responseBody = targetMethod.getAnnotation(ResponseBody.class);
		
		if(responseBody == null){
        	request.setAttribute("params", params);
        	request.setAttribute("paramsJson", paramsJson);
        }else if(obj instanceof Map){
        	Map result = (Map) obj;
        	result.put("params", params);
        	result.put("paramsJson", paramsJson);
        }
		return obj;
    }
	
	private Map<String, Object> fetchParams(HttpServletRequest request){
		Enumeration<String> d = request.getParameterNames();
		Map<String, Object> params = new HashMap<String, Object>();
		while(d.hasMoreElements()){
			String key = d.nextElement();
			String value = request.getParameter(key);
			if(StringUtils.isBlank(value) || "page".equals(key)){
				continue;
			}
			params.put(key, value);
		}
		return params;
	}
}
