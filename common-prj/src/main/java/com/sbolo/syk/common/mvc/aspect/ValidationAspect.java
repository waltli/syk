package com.sbolo.syk.common.mvc.aspect;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Constraint;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.AopInvocationException;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.sbolo.syk.common.javassist.JavassistBean;
import com.sbolo.syk.common.javassist.JavassistField;
import com.sbolo.syk.common.tools.ReflectionUtils;
import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.common.ui.RequestResult;

@Aspect
@Component
public class ValidationAspect {
	
	private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private static final String errorPage = "bzjx/error.jsp";
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Around("execution(* com.sbolo.syk.common.lims.*.controller.*Controller.*(..)) && "
			+ "(@annotation(org.springframework.web.bind.annotation.GetMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.RequestMapping))")
    public Object paginator(ProceedingJoinPoint point) throws Throwable {
		//目标方法周边信息
		Signature signature = point.getSignature();    
		MethodSignature methodSignature = (MethodSignature)signature;    
		Method targetMethod = methodSignature.getMethod();
		Class<?> returnType = targetMethod.getReturnType();
		
		//request、response
		ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		HttpServletRequest request = servletRequestAttributes.getRequest();
		HttpServletResponse response = servletRequestAttributes.getResponse();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		//异步注解
		ResponseBody responseBody = targetMethod.getAnnotation(ResponseBody.class);
		RestController restController = point.getTarget().getClass().getAnnotation(RestController.class);
		
		if((responseBody != null || restController != null) && !returnType.equals(RequestResult.class)){
			throw new AopInvocationException("异步请求的返回类型必须为RequestResult！"); 
		}else if((responseBody == null && restController == null) && !returnType.equals(String.class)){
			throw new AopInvocationException(  
					"异步请求：返回类型必须为RequestResult，并在方法上添加@ResponseBody注解。\n"
							+ "页面跳转：返回类型必须为String！");
		}
		
		Object[] args = point.getArgs();
		//与args[]同下标
		Parameter[] parameters = targetMethod.getParameters();
		//待生成javabean集合
		List<JavassistField> assists = new ArrayList<>();
		//违反约束的javaBean
		List<Object> realApply = new ArrayList<>();
		for(int i=0; i<parameters.length; i++){
			Parameter parameter = parameters[i];
			Class<?> parameterType = parameter.getType();
			//排除无需验证的参数
			if(this.isExcludeClass(parameterType)){
				continue;
			}
			//是否基本类型
			if(Utils.isMinimumType(parameterType)){
				Annotation[] parameterAnnotations = parameter.getDeclaredAnnotations();
				List<Annotation> constraintAnnotations = new ArrayList<>();
				for(Annotation anno : parameterAnnotations){
					if(anno.annotationType().isAnnotationPresent(Constraint.class)){
						constraintAnnotations.add(anno);
					}
				}
				if(constraintAnnotations.size() == 0){
					continue;
				}
				JavassistField assist = new JavassistField();
				String fieldName = parameter.getName();
				assist.setFieldName(fieldName);
				Object parameterValue = args[i];
				assist.setValue(parameterValue);
				assist.setClazz(parameterType);
				assist.setAnnotations(constraintAnnotations);
				assists.add(assist);
				continue;
			}
			
			//是否集合（不含map）
			if(Collection.class.isAssignableFrom(parameterType)){
				realApply.addAll((Collection) args[i]);
			}else {
				realApply.add(args[i]);
			}
			
		}
		
		//生成代理bean
		if(assists.size() > 0){
			JavassistBean javassistBean = new JavassistBean(assists);
			realApply.add(javassistBean.getObject());
		}
		
		//约束违反集合
		Set<ConstraintViolation<Object>> constraintViolations = new HashSet<>();
		if(realApply.size() > 0){
			Validator validator = factory.getValidator();
			realApply.forEach(obj -> {
				constraintViolations.addAll(validator.validate(obj));
			});
		}
		Object result = null;
		if(constraintViolations.size() > 0){
			String[] errors = new String[constraintViolations.size()];
			int i=0;
			Iterator<ConstraintViolation<Object>> it = constraintViolations.iterator();
	        while(it.hasNext()){
	        	errors[i++] = it.next().getMessage();
	        }
			if(responseBody != null || restController != null){
				//异步提交
				result = RequestResult.error(errors);
			}else {
				//跳转到错误页面
				request.setAttribute("errorInfo", errors);
				result = errorPage;
			}
		}else {
			//执行实际请求方法
			result = point.proceed();
		}
		return result;
    }
	
	private boolean isExcludeClass(Class<?> parameterType){
    	if(RedirectAttributes.class.isAssignableFrom(parameterType) ||
    			ServletRequest.class.isAssignableFrom(parameterType) ||
    			WebRequest.class.isAssignableFrom(parameterType) ||
				MultipartRequest.class.isAssignableFrom(parameterType) ||
				HttpSession.class.isAssignableFrom(parameterType) ||
				Principal.class.isAssignableFrom(parameterType) ||
				InputStream.class.isAssignableFrom(parameterType) ||
				Reader.class.isAssignableFrom(parameterType) ||
				HttpMethod.class == parameterType ||
				Locale.class == parameterType ||
				TimeZone.class == parameterType ||
				"java.time.ZoneId".equals(parameterType.getName()) ||
				ServletResponse.class.isAssignableFrom(parameterType) ||
				OutputStream.class.isAssignableFrom(parameterType) ||
				Writer.class.isAssignableFrom(parameterType) ||
				SessionStatus.class == parameterType ||
		    	Pageable.class.equals(parameterType) ||
		    	Sort.class.equals(parameterType)){
    		return true;
    	}
    	return false;

    }
}
