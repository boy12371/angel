/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-7-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.route.router;

import com.feinno.appengine.AppContext;
import com.feinno.rpc.channel.RpcEndpoint;

/**
 * 空
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppBeanRouterNull extends AppBeanRouter
{
	public AppBeanRouterNull(String appName)
	{
		super(appName, null, "");
	}

	@Override
	public RpcEndpoint route(AppContext ctx, String version)
	{
		throw new IllegalArgumentException("Missing Application:" + this.appName());
	}

	@Override
	public RpcEndpoint routeByHash(AppContext ctx, String version, int hash)
	{
		throw new IllegalArgumentException("Missing Application:" + this.appName());
	}
}
