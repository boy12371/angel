/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.route.router;

import com.feinno.appengine.AppContext;
import com.feinno.rpc.channel.RpcEndpoint;

/**
 * 用于固定地址的路由，比如统一交给Gateway，或转移到本地
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppBeanRouterStatic extends AppBeanRouter
{
	private RpcEndpoint staticEp;
	
	public AppBeanRouterStatic(String categoryMinusName, RpcEndpoint staticEp, String contextType, String serviceType)
	{
		super(categoryMinusName, contextType, serviceType);
		this.staticEp = staticEp;
	}
	
	@Override
	public RpcEndpoint route(AppContext ctx, String version)
	{
		return staticEp;
	}

	@Override
	public RpcEndpoint routeByHash(AppContext ctx, String version, int hash)
	{
		return staticEp;
	}
}
