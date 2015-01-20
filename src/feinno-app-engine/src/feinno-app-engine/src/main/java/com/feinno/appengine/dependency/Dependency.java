/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-29
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.dependency;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 依赖检测
 * 
 * @author 高磊 gaolei@feinno.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Dependency
{
	String value() default "";
}
