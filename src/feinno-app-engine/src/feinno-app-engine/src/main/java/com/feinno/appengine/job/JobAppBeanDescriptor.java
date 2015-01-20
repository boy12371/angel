/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job;

import com.feinno.appengine.AppBeanDescriptor;
import com.feinno.appengine.route.AppBeanRouteManager;
import com.feinno.appengine.runtime.AppHost;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class JobAppBeanDescriptor implements AppBeanDescriptor
{
	public static final JobAppBeanDescriptor INSTANCE = new JobAppBeanDescriptor();
	
	public JobAppBeanDescriptor()
	{
	}

	@Override
	public AppHost createHost() throws Exception
	{
		return new JobAppHost(); 
	}

	@Override
	public AppBeanRouteManager createRouteManager()
	{
		throw new UnsupportedOperationException("NotSupport route job app bean");
	}

	@Override
	public int getContextGenericOrder()
	{
		return -1;
	}

	@Override
	public String getServiceType()
	{
		return "";
	}
}
