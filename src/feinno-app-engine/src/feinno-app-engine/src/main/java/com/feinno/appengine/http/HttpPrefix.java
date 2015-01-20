/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 表示此AppBean处理当前url.startsWith(prefix)的请求
 * 
 * @author 高磊 gaolei@feinno.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface HttpPrefix
{
	public static final String VALUE = "value";
	String value();
}
