/*
 * FAE, Feinno App Engine
 *  
 * Create by zhangyali 2011-01-05
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import com.feinno.logging.LoggerContext;


/**
 * 绑定日志工厂类
 * 
 * @author zhangyali@feinno.com
 */
public class StaticLoggerBinder implements LoggerFactoryBinder
{
	/**
	 * 单例模式, StaticLoggerBinder类对象
	 */
	private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

	/**
	 * 获取 StaticLoggerBinder类对象
	 */
	public static final StaticLoggerBinder getSingleton()
	{
		return SINGLETON;
	}

	private static final String loggerFactoryClassStr = LoggerContext.class.getName();

	/**
	 * 日志工厂变量
	 */
	private final ILoggerFactory loggerFactory;

	/**
	 * 构造器
	 */
	private StaticLoggerBinder()
	{
		loggerFactory = LoggerContext.getInstance();
	}

	/**
	 * 获取 工厂对象
	 */
	public ILoggerFactory getLoggerFactory()
	{
		return loggerFactory;
	}

	public String getLoggerFactoryClassStr()
	{
		return loggerFactoryClassStr;
	}

}
