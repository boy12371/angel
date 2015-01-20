/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-23
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 只能标在能够AppBean的抽象基类中
 * 
 * 如
 * 	RemoteAppBean
 * 	HttpAppBean
 * 
 * @author 高磊 gaolei@feinno.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AppBeanBaseType
{
}
