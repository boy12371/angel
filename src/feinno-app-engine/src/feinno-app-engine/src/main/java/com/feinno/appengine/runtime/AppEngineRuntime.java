/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-29
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.runtime;

import com.feinno.appengine.AppBean;

/**
 * AppEngine运行时
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineRuntime
{
	/**
	 * 
	 * 启动一个正式的AppEngine
	 */
	public static void start()
	{
		throw new UnsupportedOperationException("没实现呢");
	}
	
	/**
	 * 
	 * 启动一个AppEngine的空壳
	 */
	public static void startShell()
	{
				
	}
	/**
	 * 
	 * 在启动的空壳中, 册一个AppBean
	 * @param beanClazz
	 */
	public static void registerAppBean(Class<? extends AppBean> beanClazz)
	{
	}
}
