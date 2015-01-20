/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-15
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.appengine.runtime;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.configuration.AppBeanAnnotation;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.rpc.duplex.RpcDuplexClient;

/**
 * 负载某一种AppBean的Host， AppBeanHost有如下职责，<br>
 * 1. 将AppBean从FAE_Application中加载到系统中 <br>
 * 2. 负责分发AppBean，本质上一样的AppBeanHost不需要新开监听 <br>
 * 3. 启动监听端口，或使用其他AppBeanHost的端口 <br>
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class AppHost
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AppHost.class);
	protected Map<String, AppBean> apps;
	private Map<String,URLClassLoader> appLoaders;

	protected AppHost()
	{
		apps = new Hashtable<String, AppBean>();
		appLoaders = new Hashtable<String,URLClassLoader>();
	}

	/**
	 * 
	 * 通过category-name, 找到一个在这个host中注册的AppBean
	 * 
	 * @param category-name 形如: core-GetUserInfo
	 * @return
	 */
	protected AppBean getBean(String categoryMinusName)
	{
		return apps.get(categoryMinusName);
	}

	// /**
	// *
	// * {在这里补充功能说明}
	// * @return
	// */
	// protected Map<String, AppBean> getBeanMap()
	// {
	// return apps;
	// }

	// /**
	// *
	// * 初始化一个
	// * @param anns
	// * List of Annotation
	// * @return category-name; 或者null, 若category或name二者中任意一个为null
	// * @see http://research.feinno.com/trac/fae/wiki/FAE/Annotations
	// */

	/**
	 * 初始化一个app, 由外壳调用
	 * AppBeanAnnottions中会包含需要初始化的全部信息
	 * 
	 */
	AppBean loadAppBean(AppBeanAnnotations annos) throws Exception
	{
		String appBeanClass = annos.getClassInfo().getType();
		LOGGER.debug("appBeanType is {}", appBeanClass);

		AppBeanAnnotation appName = annos.getAppBeanAnnotation(AppName.class);
		String category = appName.getFieldValue("category");
		String name = appName.getFieldValue("name");
		String categoryMinusName = category + "-" + name;
		
		Class<?> clazz = Class.forName(appBeanClass);
		AppBean bean = (AppBean) clazz.newInstance();
		bean.setVersion(annos.getClassInfo().getVersion());

		bean.load();
		
		LOGGER.debug("CategoryMinusName is {}", categoryMinusName);
		apps.put(categoryMinusName, bean);
		register(bean, annos);
		
		return bean;
	}
	
	/**
	 * 初始化一个app, 由外壳调用,动态加载时使用
	 * AppBeanAnnottions中会包含需要初始化的全部信息
	 * 
	 */
	AppBean loadAppBean(AppBeanAnnotations annos,Class<?> clazz) throws Exception
	{
		String appBeanClass = annos.getClassInfo().getType();
		LOGGER.debug("appBeanType is {}", appBeanClass);

		AppBeanAnnotation appName = annos.getAppBeanAnnotation(AppName.class);
		String category = appName.getFieldValue("category");
		String name = appName.getFieldValue("name");
		String categoryMinusName = category + "-" + name;
		
		//Class<?> clazz = Class.forName(appBeanClass);
		AppBean bean = (AppBean) clazz.newInstance();
		bean.setVersion(annos.getClassInfo().getVersion());
		bean.load();
		
		LOGGER.debug("CategoryMinusName is {}", categoryMinusName);
		apps.put(categoryMinusName, bean);
		register(bean, annos);
		
		return bean;
	}
	
	/**
	 * 动态初始化一个app, 由外壳调用
	 * AppBeanAnnottions中会包含需要初始化的全部信息
	 * 
	 */
	synchronized AppBean loadAppBeanDynamic(AppBeanAnnotations annos,URL[] urls) throws Exception
	{
		String appBeanClass = annos.getClassInfo().getType();
		LOGGER.debug("loadAppBeanDynamic appBeanType is {}", appBeanClass);

		AppBeanAnnotation appName = annos.getAppBeanAnnotation(AppName.class);
		String category = appName.getFieldValue("category");
		String name = appName.getFieldValue("name");
		String categoryMinusName = category + "-" + name;
		String jarPath = urls[0].toString();
		URLClassLoader appLoader = appLoaders.get(jarPath);
		if(appLoader ==null)
		{
			appLoader = new URLClassLoader(urls,this.getClass().getClassLoader());
			appLoaders.put(jarPath,appLoader);
		}
		Class<?> clazz = appLoader.loadClass(appBeanClass);
		AppBean bean = (AppBean) clazz.newInstance();
		bean.setVersion(annos.getClassInfo().getVersion());
		bean.load();
		
		LOGGER.debug("loadAppBeanDynamic CategoryMinusName is {}", categoryMinusName);
		apps.put(categoryMinusName, bean);
		register(bean, annos);
		
		return bean;
	}

//	void loadAppBean(Class<?> clazz) throws Exception
//	{
//		AppBeanAnnotations annos = AppBeanAnnotationsLoader.getAppBeanAnnotaions(clazz);
//		loadAppBean(annos,clazz);
//	}

	/**
	 * 
	 * 卸载一个app, 目前不支持
	 * 
	 * @param appId
	 */
	void unload(int appId)
	{
		throw new RuntimeException("unload not implemented");
	}

	public void unload(String catMinusName)
	{
		apps.remove(catMinusName);
	}


	/**
	 * 
	 * 注册一个用于内部调试的接口
	 * 
	 * @return
	 */
	public abstract void registerInjectorService(RpcDuplexClient client);
	
	/**
	 * 
	 * 启动这个AppHost, 启动监听
	 */
	protected abstract void start() throws Exception;

	/**
	 * 
	 * 停止这个AppHost, 并停止监听
	 */
	protected abstract void stop() throws Exception;

	/**
	 * 
	 * 注册Bean
	 * 
	 * @param bean 待注册的bean的服务名(categoryMinusName)
	 */
	// TODO 增加了一个categoryMinusName参数，免得重复生成。这个接口是否需要重构?
	protected abstract void register(AppBean bean, AppBeanAnnotations annos);

	/**
	 * 
	 * 返回服务监听的Url, 如果服务没有实质监听, 则返回null
	 * 
	 * @return
	 */
	protected abstract String getServiceUrl();
}
