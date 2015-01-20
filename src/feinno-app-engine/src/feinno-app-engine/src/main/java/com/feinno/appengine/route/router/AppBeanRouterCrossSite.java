/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-15
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.route.router;

import java.util.Map;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.rpc.channel.RpcEndpoint;
import com.mysql.jdbc.StringUtils;

/**
 * 用于跨Site的路由
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppBeanRouterCrossSite extends AppBeanRouter
{
	private boolean isPeerSite;
	private Map<String, AppBeanRouter> routersByApp;
	
	public AppBeanRouterCrossSite(String appName, Map<String, AppBeanRouter> routers, String contextType, String serviceType, boolean isPeerSite)
	{
		super(appName, contextType, serviceType);
		routersByApp = routers;
		this.isPeerSite = isPeerSite;
	}
	
	@Override
	public RpcEndpoint route(AppContext ctx, String version)
	{
//		String targetSite = ctx.getSiteName();
		String targetSite =  AppEngineManager.INSTANCE.currentSite();
		if (StringUtils.isNullOrEmpty(targetSite)) {
			if (isPeerSite) {
				throw new IllegalArgumentException("AppBean:" + appName() + " 标记了@PeerSite, 路由是必须提供siteName");
			} else {
				targetSite = AppEngineManager.INSTANCE.currentSite(); 
			}
		} 

		AppBeanRouter router = routersByApp.get(targetSite);
		if (router == null) {
			throw new IllegalArgumentException("AppBean:" + appName() + " 未找到对site的配置:" + targetSite);
		}
		return router.route(ctx, version);
	}

	@Override
	public RpcEndpoint routeByHash(AppContext ctx, String version, int hash)
	{
		throw new UnsupportedOperationException("CrossSite not support routeByHash");
	}
}
