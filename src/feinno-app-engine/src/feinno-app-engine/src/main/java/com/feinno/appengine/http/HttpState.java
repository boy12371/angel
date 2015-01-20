/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Nov 12, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.http;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface HttpState {
	public static final String PARAM_FIELD = "paramField";
	public static final String VALUE = "value";
	/**
	 * 
	 * 使用哪个字段标记状态
	 * @return
	 */
	HttpStateParamField paramField();
	
	/**
	 * 
	 * 字段的值
	 * @return
	 */
	String value();
}
