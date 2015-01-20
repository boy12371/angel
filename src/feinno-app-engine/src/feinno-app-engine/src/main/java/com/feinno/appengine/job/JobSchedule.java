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

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JobSchedule 
{
	/**
	 * 
	 * Job的运行时限
	 * @return
	 */
	String cron();
	
	/**
	 * 
	 * job单个运行的超时时间
	 * @return
	 */
	int timeout() default 180;
}
