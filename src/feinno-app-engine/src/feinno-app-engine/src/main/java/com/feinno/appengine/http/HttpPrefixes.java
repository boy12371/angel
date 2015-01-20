/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-31
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个HttpAppBean都需要管理那些指令
 * 
 * @author 高磊 gaolei@feinno.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface HttpPrefixes {
	HttpPrefix[] value();
}
