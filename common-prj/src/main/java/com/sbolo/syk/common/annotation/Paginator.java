package com.sbolo.syk.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Paginator {
	ResultTypeEnum value() default ResultTypeEnum.page;
	
	public enum ResultTypeEnum {
		//整页刷新
		page,
		//json数据
		json
	}
}
