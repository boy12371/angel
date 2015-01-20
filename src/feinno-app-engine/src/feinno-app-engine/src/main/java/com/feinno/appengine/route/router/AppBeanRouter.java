/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-21
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.route.router;

import java.util.List;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.runtime.configuration.RunningWorkerEntity;
import com.feinno.rpc.channel.RpcEndpoint;

/**
 * 路由一个AppBean的地址
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class AppBeanRouter
{
	private String appName;
	private String contextClassName;
	private String serviceType;
	
	public String getContextClassName()
	{
		return contextClassName;
	}
	
	public String getSerivceType()
	{
		return serviceType;
	}
	
	public AppBeanRouter(String appName, String contextClassName, String serviceType)
	{
		this.appName = appName;
		this.contextClassName = contextClassName;
		this.serviceType = serviceType;
	}
	
	public String appName()
	{
		return appName;
	}
	
	public abstract RpcEndpoint route(AppContext ctx, String version);
	
	public abstract RpcEndpoint routeByHash(AppContext ctx, String version, int hash);
	/**
	 * 
	 * 用于接受刷新workers的请求，不是所有的子类都需要继承
	 * @param worker
	 */
	public void updateWorkers(List<RunningWorkerEntity> worker)
	{
	}
}