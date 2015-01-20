/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.testing;

import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.AppContext;
import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.rpc.RemoteAppBean;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.configuration.ConfigurationException;
/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@Deprecated
public class AppEngineTestManager
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AppEngineTestManager.class);
	private static Object syncRoot = new Object();
	private static Hashtable<String, AppBean> beans = new Hashtable<String, AppBean>();
	private static Hashtable<String, AppBean> localizedBeans = new Hashtable<String, AppBean>();
	
	public static void initialize() throws Exception, ConfigurationException
	{
		//
		// 初始化Manager
		AppEngineManager manager = AppEngineManager.INSTANCE;
		manager.initialize();

		//
		// 初始化extension
//		if (settings.getExtension() != null) {
//			Class<?> clazz = Class.forName(settings.getExtension());
//			AppEngineExtension ext = (AppEngineExtension) clazz.newInstance();
//			ext.setup(manager);
//		}
//		InitialUtil.init(ResourceFactory.class);		
	}
	
	public static AppBean getBean(Class<? extends AppBean> beanClazz)
	{
		AppName anno = beanClazz.getAnnotation(AppName.class);
		if (anno == null)
			throw new IllegalArgumentException("class is not a AppBean. need @AppName");
		
		String categoryMinusName = anno.category() + "-" + anno.name();
		AppBean bean = beans.get(categoryMinusName);
		
		if (bean == null) {
			synchronized (syncRoot) {
				try {
					LOGGER.info("AppEngine loading {} for test", beanClazz); 
					bean = beanClazz.newInstance();
					LOGGER.info("AppBean {} created try load()", beanClazz);
					bean.load();
					beans.put(categoryMinusName, bean);
				} catch (Exception error) {
					LOGGER.error("AppEngine loading failed:" + beanClazz.getName(), error);
				}	
			}
		}
		return bean;
	}
	
	public static void localizedRemoteBean(Class<? extends RemoteAppBean<?, ?, ? extends AppContext>> clazz)
	{
		AppBean bean = getBean(clazz);
		localizedBeans.put(bean.getCategoryMinusName(), bean);
	}
	
	@SuppressWarnings("unchecked")
	public static RemoteAppBean<?, ?, ? extends AppContext> getLocalizedRemoteAppBean(Class<? extends RemoteAppBean<?, ?, ? extends AppContext>> clazz)
	{
		AppBean bean = getBean(clazz);
		if (localizedBeans.get(bean.getCategoryMinusName()) != null) {
			return (RemoteAppBean<?, ?, ? extends AppContext>)bean;
		} else {
			return null;
		}
	}
}
