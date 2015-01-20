/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Sep 16, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

/**
 * 用于在Executor中执行Callback的接口类型
 * 
 * @author 高磊 gaolei@feinno.com
 */
public interface FutureCallback extends Runnable
{
	Future<?> getFuture();
}
