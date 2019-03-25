package com.sbolo.syk.common.mvc.aspect;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.validation.executable.ExecutableValidator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.AopInvocationException;
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

import com.sbolo.syk.common.tools.Utils;
import com.sbolo.syk.common.ui.ResultApi;

@Aspect
@Component
public class ValidationAspect {
	
	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	private static final String errorPage = "bzjx/error.jsp";
	
	@Around("execution(* com.jwwd.*.controller.*Controller.*(..)) && "
			+ "(@annotation(org.springframework.web.bind.annotation.GetMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.RequestMapping))")
    public Object paginator(ProceedingJoinPoint point) throws Throwable {
		//目标方法周边信息
    	Object target = point.getTarget();
		Signature signature = point.getSignature();
		MethodSignature methodSignature = (MethodSignature)signature;    
		Method targetMethod = methodSignature.getMethod();
		Class<?> returnType = targetMethod.getReturnType();
		ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		HttpServletRequest request = servletRequestAttributes.getRequest();
		HttpServletResponse response = servletRequestAttributes.getResponse();
		
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		//异步注解
		ResponseBody responseBody = targetMethod.getAnnotation(ResponseBody.class);
		RestController restController = point.getTarget().getClass().getAnnotation(RestController.class);
		
		//方法中是否含有需要验证的参数
		boolean methodGo = false;
		Annotation[][] parameterAnnotations = targetMethod.getParameterAnnotations();
		sign : for(Annotation[] annos : parameterAnnotations) {
			for(Annotation anno : annos) {
				if(anno.annotationType().isAnnotationPresent(Constraint.class)){
					methodGo = true;
					break sign;
				}
			}
		}
		
		//参数值数组
		Object[] args = point.getArgs();
		//参数名数组与args[]同下标
		Parameter[] parameters = targetMethod.getParameters();
		//违反约束的javaBean
		List<Object> todos = new ArrayList<>();
		for(int i=0; i<parameters.length; i++){
			Parameter parameter = parameters[i];
			Class<?> parameterType = parameter.getType();
			//排除无需验证的参数
			if(isExcludeClass(parameterType)){
				continue;
			}
			//集合不纳入对象验证中
			if(Collection.class.isAssignableFrom(parameterType) || Map.class.isAssignableFrom(parameterType)){
				continue;
			}
			//基本类型不纳入对象验证中
			if(Utils.isMinimumType(parameterType)){
				continue;
			}
			//自定义对象参数是否需要验证
			Field[] fields = parameterType.getDeclaredFields();
			sign : for(Field field : fields) {
				Annotation[] annotations = field.getAnnotations();
				for(Annotation anno : annotations){
					if(anno.annotationType().isAnnotationPresent(Constraint.class)){
						todos.add(args[i]);
						break sign;
					}
				}
			}
		}
		Set<ConstraintViolation<Object>> constraintViolations = new HashSet<>();
		//验证方法中的参数
		if(methodGo) {
			ExecutableValidator executableValidator = validator.forExecutables();
	    	Set<ConstraintViolation<Object>> methodValidators = executableValidator.validateParameters(target, targetMethod, args);
	    	constraintViolations.addAll(methodValidators);
		}
		
		//验证参数中的自定义对象
		if(todos.size() > 0){
			todos.forEach(obj -> {
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
				result = ResultApi.error(errors);
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
