/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.rpc;

import com.feinno.appengine.AppBeanDescriptor;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RemoteAppBeanDescriptor implements AppBeanDescriptor
{
	public static final RemoteAppBeanDescriptor INSTANCE = new RemoteAppBeanDescriptor();
	
	private RemoteAppBeanDescriptor()
	{		
	}
	
	@Override
	public RemoteAppHost createHost() throws Exception
	{
		return new RemoteAppHost();
	}
	
	@Override
	public RemoteAppBeanRouteManager createRouteManager()
	{
		return new RemoteAppBeanRouteManager();
	}

	@Override
	public int getContextGenericOrder()
	{
		return 2;
	}

	@Override
	public String getServiceType()
	{
		return "rpc";
	}
}
