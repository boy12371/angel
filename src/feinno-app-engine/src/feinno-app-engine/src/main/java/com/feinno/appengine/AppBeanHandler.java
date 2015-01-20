/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-28
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.appengine;


/**
 * 处理AppTx的处理器，可在执行前拦截或执行后进行监视 
 * 
 * @param
 * @author 高磊 gaolei@feinno.com
 */
public interface AppBeanHandler extends AppTxHandler
{
	/**
	 * 
	 * 处理模式
	 * @return
	 */
	AppBeanHandlerMode getMode();
	
	/**
	 * 
	 * 设置此AppBeanHandler的参数
	 * @param params
	 */
	void setParams(String[] params);
}
