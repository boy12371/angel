/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-15
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.route.router;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.route.gray.CondBuilder;
import com.feinno.appengine.route.gray.ParserException;
import com.feinno.appengine.route.gray.conds.Cond;
import com.feinno.rpc.channel.RpcEndpoint;

/**
 * 用于处理灰度发布的Router
 * TODO
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppBeanRouterGrayOne extends AppBeanRouter
{
	private Cond cond;
	private String targetVersion;
	private AppBeanRouter innerRouter;
	
	public AppBeanRouterGrayOne(String appName, String grayFactors, AppBeanRouter router, String contextType, String serviceType,String targetVersion)
	{
		super(appName, contextType, serviceType);
		try {
			cond = CondBuilder.parse(grayFactors);
			innerRouter = router;
			this.targetVersion = targetVersion;
		} catch (ParserException e) {
			throw new IllegalArgumentException("invailed condition:" + grayFactors, e);
		}
	}

	@Override
	public RpcEndpoint route(AppContext ctx, String version)
	{
		if (cond.apply(ctx)) {
			return innerRouter.route(ctx, version);
		} else {
			return null;
		}
	}

	@Override
	public RpcEndpoint routeByHash(AppContext ctx, String version, int hash)
	{
		if (cond.apply(ctx)) {
			return innerRouter.routeByHash(ctx, version, hash);
		} else {
			return null;
		}
	}	
	
	public boolean hitVersion(String version)
	{
		if(version == null) 
		{
			return false;
		} 
		else 
		{
			return version.equals(targetVersion);	
		}
	}
	
	public RpcEndpoint pickOne(AppContext ctx, String version)
	{
		return innerRouter.route(ctx, version);
	}

}
