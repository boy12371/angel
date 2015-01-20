/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.http;

import com.feinno.appengine.AppBeanDescriptor;
import com.feinno.appengine.route.AppBeanRouteManager;
import com.feinno.appengine.runtime.AppHost;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HttpAppBeanDescriptor implements AppBeanDescriptor
{	
	public static final HttpAppBeanDescriptor INSTANCE = new HttpAppBeanDescriptor();

	@Override
	public AppHost createHost() throws Exception
	{
		return new HttpAppHost();
	}

	@Override
	public AppBeanRouteManager createRouteManager()
	{
		return new HttpAppBeanRouteManager();
	}

	@Override
	public int getContextGenericOrder()
	{
		return 0;
	}

	@Override
	public String getServiceType()
	{
		return "http";
	}
}
