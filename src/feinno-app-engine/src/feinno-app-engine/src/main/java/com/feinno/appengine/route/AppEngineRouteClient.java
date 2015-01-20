/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Sep 2, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.AppBeanDescriptor;
import com.feinno.appengine.configuration.AppBeanClassInfo;
import com.feinno.appengine.route.router.AppBeanRouter;
import com.feinno.appengine.rpc.RemoteAppBean;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.appengine.runtime.configuration.ApplicationEntity;
import com.feinno.appengine.runtime.configuration.RunningWorkerEntity;
import com.feinno.ha.ServiceSettings;
import com.feinno.util.Action;
import com.feinno.util.Action3;
import com.feinno.util.DictionaryList;
import com.feinno.util.Func;
import com.feinno.util.DictionaryList.UpdateMode;

/**
 * FAE中寻找AppBean的客户端路由组件
 * 与C#的版本不同，目前仅支持初始化本Site的配置
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineRouteClient
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AppEngineRouteClient.class);
	
	private static AppEngineRouteClient instance = null;
	// private static Map<String, AppEngineRouteClient> multiSiteInstances;

	public static AppEngineRouteClient getInstance()
	{
		return instance;
	}
	
	//
	// TODO 目前只支持这一种初始化，可以省略掉多重初始化的场景
	public synchronized static void initialize(Class<? extends AppBean> beanClazz,String zkHosts) throws Exception
	{
		if(ServiceSettings.INSTANCE.isNoZK())
			return;
		
		if (instance == null) {
			instance = new AppEngineRouteClient(zkHosts);
		}
		instance.initRouteManager(beanClazz);
	}
	
	// 支持强制设置远程调用是否走本Site的初始化方法
	public static void initialize(Class<? extends AppBean> beanClazz,String zkHosts, boolean isGlobal) throws Exception {
		
		if(ServiceSettings.INSTANCE.isNoZK())
			return;
		
		if (instance == null) {
			instance = new AppEngineRouteClient(zkHosts);
		}
		instance.initRouteManager(beanClazz, isGlobal);
	}
	
	public static void initialize(Class<? extends AppBean> beanClazz) throws Exception
	{
		String zkHosts = AppEngineManager.INSTANCE.getSettings().getZkHosts();
		initialize(beanClazz,zkHosts);
	}
	
	private DictionaryList<String, ApplicationEntity> appLists;
	private DictionaryList<String, RunningWorkerEntity> workerLists;
	
	private ZookeeperWatcher watcher;
	private Map<String, AppBeanRouteManager> managers;
	private DictionaryList<String, AppBeanRouter> routersByWorkerId;
	
	public AppEngineRouteClient(String zkHosts) throws IOException
	{
		managers = new Hashtable<String, AppBeanRouteManager>();
		appLists = new DictionaryList<String, ApplicationEntity>();
		workerLists = new DictionaryList<String, RunningWorkerEntity>();
		routersByWorkerId = new DictionaryList<String, AppBeanRouter>();
		
		//
		// 已经在锁内了
		LOGGER.info("begin create ZooKeeperWatcher");
		//String zkHosts = AppEngineManager.INSTANCE.getSettings().getZkHosts();
		watcher = new ZookeeperWatcher(zkHosts);

		watcher.setAppsUpdateCallback(new Action<List<ApplicationEntity>>() {
			@Override
			public void run(List<ApplicationEntity> a)
			{
				updateApplications(a);
			}
		});

		watcher.setWorkersUpdaterCallback(new Action<List<RunningWorkerEntity>>() {
			@Override
			public void run(List<RunningWorkerEntity> a)
			{
				updateRunningWorkers(a);
			}
		});
		
		watcher.init();
	}
	
	public AppBeanRouteManager getRouteManager(Class<? extends AppBean> beanClazz)
	{
		return managers.get(beanClazz.getName());
	}
	
	public void initRouteManager(Class<? extends AppBean> beanClazz)
	{
		initRouteManager(beanClazz,false);
	}
	
	public void initRouteManager(Class<? extends AppBean> beanClazz,boolean isGlobal)
	{
		String key = beanClazz.getName();
		if (managers.get(key) != null) {
			return;
		}
		
		AppBeanDescriptor descriptor = AppEngineManager.INSTANCE.getDescriptor(key);
		
		if (descriptor == null) {
			throw new IllegalArgumentException("unknown bean type:" + key);
		}
		
		AppBeanRouteManager manager = descriptor.createRouteManager();
		manager.setGlobalRouter(isGlobal);
		manager.setRouteClient(this);
		managers.put(key, manager);		
		loadApplications(appLists);
	}
	
	//
	// TODO: 去除Update过程中产生的垃圾Routers
	private void updateApplications(List<ApplicationEntity> apps)
	{
		if (apps == null) {
			apps = new ArrayList<ApplicationEntity>();
		}	
		
		DictionaryList<String, ApplicationEntity> lists = new DictionaryList<String, ApplicationEntity>();
		lists.fillWith(apps, new Func<ApplicationEntity, String>() {
			@Override
			public String exec(ApplicationEntity obj)
			{
				AppBeanClassInfo info = obj.getAppBeanAnnotations().getClassInfo();
				return info.getBaseClass().getType();
			}
		});
		appLists = lists;
		loadApplications(lists);
	}
	
	private void loadApplications(DictionaryList<String, ApplicationEntity> lists)
	{
		if (lists == null)
			return;
		
		for (String key: managers.keySet()) {
			AppBeanRouteManager manager = managers.get(key);
			
			List<ApplicationEntity> list = lists.get(key);
			if (list == null) {
				list = new ArrayList<ApplicationEntity>();
			}
			try {
				LOGGER.info("begin updateApplications for manager: {} count={}", key, list.size());
				manager.updateApplications(list);
			} catch (Exception ex) {
				LOGGER.error("updateApplications failed {}", ex);
			}
		}
	}

	private void updateRunningWorkers(List<RunningWorkerEntity> workers)
	{
		DictionaryList<String, RunningWorkerEntity> lists = new DictionaryList<String, RunningWorkerEntity>();
		lists.fillWith(workers, new Func<RunningWorkerEntity, String>() {
			@Override
			public String exec(RunningWorkerEntity obj)
			{
				return obj.getAppWorkerId();
			}
		});
		
		workerLists.compareAll(lists, new Action3<String, UpdateMode, List<RunningWorkerEntity>>() {
			@Override
			public void run(String key, UpdateMode mode, List<RunningWorkerEntity> list)
			{
				List<AppBeanRouter> routers = routersByWorkerId.get(key);
				for (AppBeanRouter router: routers) {
					try {
						LOGGER.info("update RunningWorker for {}", router);
						router.updateWorkers(list);
					} catch (Exception ex) {
						LOGGER.error("update RunningWorker failed {} {}", router, ex);
					}
				}
				// TODO: 清理垃圾router
			}
		});
		workerLists = lists;
	}

	public List<RunningWorkerEntity> loadWorkers(AppBeanRouter router, String appWorkerId)
	{
		List<RunningWorkerEntity> workers = workerLists.get(appWorkerId);
		routersByWorkerId.put(appWorkerId, router);
		return workers;
	}
	public DictionaryList<String, RunningWorkerEntity> getWorkerLists() {
		return workerLists;
	}
	
	public void removeRouter(AppBeanRouter router)
	{
		// TODO
	}

}
