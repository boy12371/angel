/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-21
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBeanDescriptor;
import com.feinno.appengine.annotation.PeerSite;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.configuration.AppBeanBaseClassInfo;
import com.feinno.appengine.configuration.CFG_Site;
import com.feinno.appengine.route.router.AppBeanRouter;
import com.feinno.appengine.route.router.AppBeanRouterCrossSite;
import com.feinno.appengine.route.router.AppBeanRouterGray;
import com.feinno.appengine.route.router.AppBeanRouterGrayOne;
import com.feinno.appengine.route.router.AppBeanRouterRandom;
import com.feinno.appengine.route.router.AppBeanRouterStatic;
import com.feinno.appengine.rpc.RemoteAppBean;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.appengine.runtime.configuration.ApplicationEntity;
import com.feinno.util.Action3;
import com.feinno.util.DictionaryList;
import com.feinno.util.Func;
import com.feinno.util.StringUtils;
import com.feinno.util.DictionaryList.UpdateMode;

/**
 * 路由一个在AppEngine中注册的Worker的地址<br>
 * TODO appWorkerId的变更会导致碎数据的出现，但是危害性很低，可以后期处理
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class AppBeanRouteManager
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AppBeanRouteManager.class);
	private static final String GATEWAY_APPID = "gateway";
	
	private DictionaryList<AppBeanParams, ApplicationEntity> appsByParams;
	private Map<AppBeanParams, AppBeanRouter> routers;
	private AppEngineRouteClient routeClient;
	//如果是全局的globalRouter使用的话，则这个值设为true;这种情况下，都是直接路由到app不需要经过AEG
	private boolean isGlobalRouter = false;
	
	public void setGlobalRouter(boolean isGlobalRouter) {
		this.isGlobalRouter = isGlobalRouter;
	}


	public AppBeanRouteManager()
	{
		appsByParams = new DictionaryList<AppBeanParams, ApplicationEntity>();
		routers = new Hashtable<AppBeanParams, AppBeanRouter>();
	}
	

	public void setRouteClient(AppEngineRouteClient client)
	{
		routeClient = client;
	}
	
	public AppBeanRouter getRouter(AppBeanParams params)
	{
		return routers.get(params);
	}
	
	public abstract List<AppBeanParams> getRequestParams(ApplicationEntity app);
	
	public void onUpdateRouter(AppBeanParams params, AppBeanRouter router)
	{
		//
		// do nothing in base class
	}
	
	public void updateApplications(List<ApplicationEntity> apps)
	{
		DictionaryList<AppBeanParams, ApplicationEntity> lists = new DictionaryList<AppBeanParams, ApplicationEntity>();
		lists.fillWithKeys(apps, new Func<ApplicationEntity, List<AppBeanParams>>() {
			@Override
			public List<AppBeanParams> exec(ApplicationEntity obj)
			{
				return getRequestParams(obj);
			}
		});
		
		appsByParams.compareAll(lists, new Action3<AppBeanParams, UpdateMode, List<ApplicationEntity>>() {
			@Override
			public void run(AppBeanParams key, UpdateMode mode, List<ApplicationEntity> apps)
			{
				try {
					AppBeanRouter router = null;
					LOGGER.debug("{} Applications for {}", mode, key); 
					switch (mode) {
					case INSERT:
					case UPDATE:
						router = createRouter(apps);
						routers.put(key, router);
						onUpdateRouter(key, router);
						break;
					case DELETE:
						routers.remove(key);
						onUpdateRouter(key, null);
						break;
					}
					LOGGER.debug("NewRouter for {} = {}", key, router);
				} catch (Exception ex) {
					LOGGER.error("UpdateApplication for {} failed {}", key, ex);
					// AppBeanRouteManager throw new UnsupportedOperationException("没实现呢");
				}
			}
		});
		appsByParams = lists;
	}
	
	private AppBeanRouter createRouter(List<ApplicationEntity> apps)
	{
		//
		// Annotation的配置第一条Application记录为准
		// 根据@PeerSite判断创建跨Site的Router还是单Site Router
		ApplicationEntity app1 = apps.get(0);
		AppBeanAnnotations annos = app1.getAppBeanAnnotations();
		
		String appName = annos.getCategoryMinusName();
		AppBeanBaseClassInfo baseType = app1.getAppBeanAnnotations().getClassInfo().getBaseClass();
		AppBeanDescriptor descriptor = AppEngineManager.INSTANCE.getDescriptor(baseType.getType());
		
		String contextType = baseType.getGenericParams().get(descriptor.getContextGenericOrder()).getValue();
		String currentSite = AppEngineManager.INSTANCE.currentSite();
		
		boolean isPeerSite = annos.getAppBeanAnnotation(PeerSite.class) != null;
		if (!isPeerSite) {
			//
			// 没有标记@PeerSite的RemoteAppBean
			// 如果存在本Site记录，则直接使用本Site记录，否则创建Gateway记录
			//如果是全局的GlobalRouteManager,直接使用zk记录
			String beanSite = app1.getSite();
			if (StringUtils.isNullOrEmpty(beanSite) || currentSite.equals(beanSite) || isGlobalRouter) {
				return createRouterLocalSite(appName, apps, contextType, descriptor);
			} else {
				if (!RemoteAppBean.class.getName().equals(baseType.getType())) {
					String emsg = "only RemoteAppBean can route to another site:" + app1.getCategoryMinusName();
					LOGGER.error(emsg);
					throw new IllegalArgumentException(emsg);
				}
				
				if (apps.size() > 1) {
					String emsg = "外Site的RemoteAppBean只允许有一条记录，灰度由对方的AEG处理" + app1.getCategoryMinusName();
					LOGGER.error(emsg);
					throw new IllegalArgumentException(emsg);
				}
				
				if (!app1.getAppWorkerId().equals(GATEWAY_APPID)) {
					String emsg = "外Site的RemoteAppBean只允许有一条记录，appId=gateway" + app1.getCategoryMinusName();
					LOGGER.error(emsg);
					throw new IllegalArgumentException(emsg);
				}
				
				CFG_Site site = AppEngineManager.INSTANCE.getSite(app1.getSite());
				return createRouterRemoteSite(appName, site, contextType, descriptor);
			}
		} else {
			//
			// 只有RemoteAppBean能够拥有这个标记，其他类型报错处理
			// 默认需要路由到所有的PeerSite去
			// TODO: 下一个版本支持MessageAppBean的路由			
			if (!RemoteAppBean.class.getName().equals(baseType.getType())) {
				String emsg = "only RemoteAppBean can annotated with @PeerSite illegal:" + baseType.getType();
				LOGGER.error(emsg);
				throw new IllegalArgumentException(emsg);
			}

			Map<String, AppBeanRouter> routersBySite = new HashMap<String, AppBeanRouter>();
			
			for (CFG_Site site: AppEngineManager.INSTANCE.getSites()) {
				if (!site.isPeerSite())
					continue;
				
				String siteName = site.getName();
				AppBeanRouter router;
				if (currentSite.equals(siteName)  || isGlobalRouter) {
					router = createRouterLocalSite(appName, apps, contextType, descriptor);
				} else {
					router = createRouterRemoteSite(appName, site, contextType, descriptor);
				}
				routersBySite.put(siteName, router);
			}

			return new AppBeanRouterCrossSite(appName, routersBySite, contextType, descriptor.getServiceType(), isPeerSite);
		}
	}

	/**
	 * 
	 * 创建本Site得Router路由
	 * @param appName
	 * @param apps
	 * @return
	 */
	private AppBeanRouter createRouterLocalSite(String appName, List<ApplicationEntity> apps, String contextType, AppBeanDescriptor descriptor)
	{
		AppBeanRouter one = null;
		List<AppBeanRouterGrayOne> grays = new ArrayList<AppBeanRouterGrayOne>();

		for (ApplicationEntity app : apps) {
			String grayFactors = app.getGrayFactors();
			if (StringUtils.isNullOrEmpty(grayFactors)) {
				if (one == null) {
					one = createRouterOne(appName, app, contextType, descriptor);
				} else {
					String msg = "amibigous RemoteAppBean:" + appName;
					LOGGER.error(msg);
					throw new IllegalArgumentException(msg); 
				}
			} else {
				AppBeanRouterGrayOne grayApp = createRouterGrayOne(appName, app, contextType, descriptor);
				grays.add(grayApp);
			}
		}
		
		if (grays.size() == 0) {
			return one;
		} else {
			return new AppBeanRouterGray(appName, one, grays, contextType, descriptor.getServiceType());
		}
	}
	
	private AppBeanRouter createRouterRemoteSite(String appName, CFG_Site site, String contextType, AppBeanDescriptor descriptor)
	{
		return new AppBeanRouterStatic(appName, site.getAppEngineGatewayEp(), contextType, descriptor.getServiceType());
	}

	private AppBeanRouter createRouterOne(String appName, ApplicationEntity app, String contextType, AppBeanDescriptor descriptor)
	{
		if (app.getAppWorkerId().equals(GATEWAY_APPID)) {
			String siteName = app.getSite();
			CFG_Site site = AppEngineManager.INSTANCE.getSite(siteName);
			return new AppBeanRouterStatic(appName, site.getAppEngineGatewayEp(), contextType, descriptor.getServiceType());
		} else {
			return new AppBeanRouterRandom(routeClient, app, contextType, descriptor.getServiceType());
		}
	}

	private AppBeanRouterGrayOne createRouterGrayOne(String appName, ApplicationEntity app, String contextType, AppBeanDescriptor descriptor)
	{
		AppBeanRouter innerRouter = createRouterOne(appName, app, contextType, descriptor);
		return new AppBeanRouterGrayOne(appName, app.getGrayFactors(), innerRouter, contextType, descriptor.getServiceType(),app.getAppVersion());
	}
}