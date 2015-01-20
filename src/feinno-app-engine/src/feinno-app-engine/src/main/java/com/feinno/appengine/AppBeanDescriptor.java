/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine;

import com.feinno.appengine.route.AppBeanRouteManager;
import com.feinno.appengine.runtime.AppHost;

/**
 * 一种类型的AppBean的描述器, 注册在AppEngineManager当中, 负责以下功能<br>
 * 1. 生成Host
 * 2. 生成
 * 
 * @author 高磊 gaolei@feinno.com
 */
public interface AppBeanDescriptor
{	
	/**
	 * 创建一个负载此AppBean的Host
	 * @return
	 * @throws Exception
	 */
	AppHost createHost() throws Exception;
	
	/**
	 * 获取AppContext在上下文泛型参数中所处的次序
	 * @return
	 */
	int getContextGenericOrder(); 
	
	/**
	 * 创建一个客户端路由组件
	 * @return
	 */
	AppBeanRouteManager createRouteManager();

	/**
	 * 获取服务类型, 服务类型会决定端口的使用
	 * @return
	 */
	String getServiceType();
}
