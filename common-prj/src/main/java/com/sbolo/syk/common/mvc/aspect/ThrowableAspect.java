package com.sbolo.syk.common.mvc.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sbolo.syk.common.annotation.CacheAvl;
import com.sbolo.syk.common.annotation.CacheDel;
import com.sbolo.syk.common.tools.DateUtil;
import com.sbolo.syk.common.ui.ResultApi;

@Aspect
@Component
public class ThrowableAspect {
	
	private static final Logger log = LoggerFactory.getLogger(ThrowableAspect.class);
	
	private static final String error = "error.html";
	
	@Around("execution(* com.sbolo.syk.*.controller.*Controller.*(..)) && "
			+ "(@annotation(org.springframework.web.bind.annotation.GetMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.RequestMapping))")
	public Object throwableHandler(ProceedingJoinPoint point) throws Throwable {
		//目标方法周边信息
		Signature signature = point.getSignature();    
		MethodSignature methodSignature = (MethodSignature)signature;    
		Method targetMethod = methodSignature.getMethod();
		Class<?> returnType = targetMethod.getReturnType();
		
		//request、response
		ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		HttpServletRequest request = servletRequestAttributes.getRequest();
		
		try {
			Object obj = point.proceed();
			return obj;
		} catch (Throwable e) {
			log.error("", e);
			ResultApi<String> result = ResultApi.error(e);
			
			ResponseBody responseBody = targetMethod.getAnnotation(ResponseBody.class);
			RestController restController = point.getTarget().getClass().getAnnotation(RestController.class);
			
			if(returnType.equals(ResultApi.class)) {
				return result;
			}else if(responseBody == null && restController == null && String.class.equals(returnType)) {
				request.setAttribute("result", result);
				return error;
			}
			throw e;
		}
	}
}
