/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Sep 2, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.rpc;

import java.util.ArrayList;
import java.util.List;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.configuration.AppBeanAnnotation;
import com.feinno.appengine.route.AppBeanParams;
import com.feinno.appengine.route.AppBeanRouteManager;
import com.feinno.appengine.route.router.AppBeanRouter;
import com.feinno.appengine.runtime.configuration.ApplicationEntity;
import com.feinno.rpc.channel.RpcEndpoint;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RemoteAppBeanRouteManager extends AppBeanRouteManager
{
	public RemoteAppBeanRouteManager()
	{
	}

	@Override
	public List<AppBeanParams> getRequestParams(ApplicationEntity app)
	{
		AppBeanAnnotation anno = app.getAppBeanAnnotations().getAppBeanAnnotation(AppName.class);
		
		String category = anno.getFieldValue("category");
		String name = anno.getFieldValue("name");
		RemoteAppBeanParams params = new RemoteAppBeanParams(category + "-" + name);
		List<AppBeanParams> ret = new ArrayList<AppBeanParams>();
		ret.add(params);
		return ret;
		
	}
	
	public AppBeanRouter getRouter(String categoryMinusName)
	{
		AppBeanParams params = new RemoteAppBeanParams(categoryMinusName);
		return getRouter(params); 
	}
	
	public RpcEndpoint routeRemoteAppBean(String categoryMinusName, AppContext ctx, String version)
	{
		AppBeanRouter router = getRouter(categoryMinusName);
		if (router == null) {
			return null;
		} else {
			return router.route(ctx, version);
		}
	}
}
