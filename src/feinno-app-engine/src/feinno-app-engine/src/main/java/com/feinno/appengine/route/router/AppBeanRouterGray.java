/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-15
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.route.router;

import java.io.IOException;
import java.util.List;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.ContextUri;
import com.feinno.rpc.channel.RpcEndpoint;

/**
 * 灰度处理外壳
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppBeanRouterGray extends AppBeanRouter
{
	private AppBeanRouter oneApp;
	private List<AppBeanRouterGrayOne> grayApps;
	
	public AppBeanRouterGray(String appName, AppBeanRouter one, List<AppBeanRouterGrayOne> grays, String contextType, String serviceType)
	{
		super(appName, contextType, serviceType);
		this.oneApp = one;
		this.grayApps = grays;
	}

	@Override
	public RpcEndpoint route(AppContext ctx, String version)
	{
		//
		// 如果目标的Bean与源Bean的版本号一致，则优先灰度，否则开始判断灰度条件
//		for (AppBeanRouterGrayOne app: grayApps) {
//			if (app.hitVersion(version)) {
//				return app.route(ctx, version);
//			}
//		}
		
		//
		// 依次判断灰度条件，如果未命中，则判断主条件
		for (AppBeanRouterGrayOne app: grayApps) {
			RpcEndpoint ep = app.route(getAppContextWrapper(ctx,version), version);
			if (ep != null) {
				return ep;
			}
		}
		
		return oneApp.route(getAppContextWrapper(ctx,version), version);
	}

	private AppContext getAppContextWrapper(final AppContext ctx,final String version) {
		
		AppContext context = new AppContext(){
			
			@Override
			public ContextUri getContextUri() {
				return ctx.getContextUri();
			}

			@Override
			public void decode(String uri, byte[] datas) throws Exception {
				throw new UnsupportedOperationException("getAppContextWrapper not support method");			
				
			}

			@Override
			public byte[] encode(int demands) throws IOException {
				throw new UnsupportedOperationException("getAppContextWrapper not support method");			
			}

			@Override
			public Object getNamedValue(String id) {
				if("version".equals(id))
					return version;
				else
					return ctx.getNamedValue(id);
			}

			@Override
			public void putNamedValue(String id, Object value) {
				throw new UnsupportedOperationException("getAppContextWrapper not support method");			
			}

			@Override
			public String getSiteName() {
				return ctx.getSiteName();
			}
			
		};
		return context;
	}

	@Override
	public RpcEndpoint routeByHash(AppContext ctx, String version, int hash)
	{
		//
		// 如果目标的Bean与源Bean的版本号一致，则优先灰度，否则开始判断灰度条件
//		for (AppBeanRouterGrayOne app: grayApps) {
//			if (app.hitVersion(version)) {
//				return app.routeByHash(ctx, version, hash);
//			}
//		}
		
		//
		// 依次判断灰度条件，如果未命中，则判断主条件
		for (AppBeanRouterGrayOne app: grayApps) {
			RpcEndpoint ep = app.routeByHash(getAppContextWrapper(ctx,version), version, hash);
			if (ep != null) {
				return ep;
			}
		}
		
		return oneApp.route(getAppContextWrapper(ctx,version), version);
	}
}
