package com.sbolo.syk.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解配合ResponseInterceptor使用，用于在HttpServletResponse中增加header跨域头部Access-Control-Allow-Origin
 * @author l
 *
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseAddHead {
	

}
