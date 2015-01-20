/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-7-27
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.runtime;

/**
 * AppEngine当前的运行时类型
 * 
 * @author 高磊 gaolei@feinno.com
 */
public enum AppEngineRunMode
{
	/** 未进行任何操作时 */
	INIT,	
	
	/** 安装, 只有本时刻可运行bean的setup方法 */
	SETUP,
	
	/** 正在加载BEAN并初始化 */
	LOADING,
	
	/** 运行中 */
	RUNNING,
	
	/** 停止中 */
	STOPING,
}
