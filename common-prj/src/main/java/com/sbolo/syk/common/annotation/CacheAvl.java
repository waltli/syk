package com.sbolo.syk.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheAvl { 
	/**
	 * 主key
	 * 
	 * @return
	 */
	String key() default "";

	/**
	 * mapKey
	 * 
	 * @return
	 */
	String hKey() default "";

	/**
	 * 超时时长
	 * 
	 * @return
	 */
	long timeout() default -1;

	/**
	 * 超时时长所用到的单位，默认为天
	 * 
	 * @return
	 */
	TimeUnit timeUnit() default TimeUnit.DAYS;

	/**
	 * 有效期：2017/12/6
	 * 
	 * @return
	 */
	String expireAt() default "";

	/**
	 * 有效期格式：yyyy/MM/dd
	 * 
	 * @return
	 */
	String format() default "";
}
