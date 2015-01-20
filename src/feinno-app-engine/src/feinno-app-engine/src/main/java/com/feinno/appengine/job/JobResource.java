/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.feinno.appengine.resource.ResourceType;

/**
 * 表明一个Job所使用的Resource
 * resource="" 表示这个Job并不依赖于Resource
 * 
 * @author 高磊 gaolei@feinno.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JobResource
{
	ResourceType type() default ResourceType.NONE;
	String resource() default "";
	boolean parallel();
}
