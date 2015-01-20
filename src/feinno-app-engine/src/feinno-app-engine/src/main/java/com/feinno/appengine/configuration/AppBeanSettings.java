/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-9-6
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

import com.feinno.util.ConfigBean;

/**
 * 
 * AppBean的独立配置
 * 
 * 
 * threadPool=user		#表示线程池的名字
 * maxThreads=100		#线程池中的线程数量
 * maxSessions=8		#未完成的线程数
 * maxQueueLength=1000	#线程池中最大等待数量 
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppBeanSettings extends ConfigBean
{
	private String threadPool;
	private int maxThreads;
	private int maxSessions;
	private int maxQueueLength;
	
	private String logLevel;
	
	public String getThreadPool()
	{
		return threadPool;
	}

	public void setThreadPool(String threadPool)
	{
		this.threadPool = threadPool;
	}	 

	public int getMaxThreads()
	{
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads)
	{
		this.maxThreads = maxThreads;
	}

	public int getMaxSessions()
	{
		return maxSessions;
	}

	public void setMaxSessions(int maxSessions)
	{
		this.maxSessions = maxSessions;
	}

	public int getMaxQueueLength()
	{
		return maxQueueLength;
	}

	public void setMaxQueueLength(int maxQueueLength)
	{
		this.maxQueueLength = maxQueueLength;
	}

	public String getLogLevel()
	{
		return logLevel;
	}

	public void setLogLevel(String logLevel)
	{
		this.logLevel = logLevel;
	}
}
