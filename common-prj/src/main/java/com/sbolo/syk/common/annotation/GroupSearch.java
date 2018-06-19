package com.sbolo.syk.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GroupSearch {
	/**
	 * 显示名称
	 * 
	 * @return
	 */
	String desc() default "";
	/**
	 * 实际列
	 * 
	 * @return
	 */
	String value() default "";
	/**
	 * 关联表
	 * 
	 * @return
	 */
	String relationTable() default "";
	/**
	 * 关联表的列
	 * 
	 * @return
	 */
	String relationColumn() default "";
	/**
	 * 主表与关联表关联的列
	 * @return
	 */
	String mainRelationColumn() default "";
	/**
	 * 如果是字典表 关联其类型
	 * @return
	 */
	String dictType() default "";
}
