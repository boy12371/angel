/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-21
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import com.feinno.diagnostic.perfmon.Stopwatch;


/**
 * 
 * SessionTask是一个能够在异步调用时接续原线程池的数据结构, 结合SessionExecutor对一个任务接续并控制的特性, 
 * 完成对存在异步操作的业务线程进行更好的控制
 * 
 * @author 高磊 gaolei@feinno.com
 * @see SessionExecutor
 * @see SesssionPool
 * @see Future
 * @See
 */
public abstract interface SessionTask extends Runnable
{
	/**
	 * 
	 * 获取Task开始时的秒表信息, 此信息可用来判断TIMEOUT, 但处理Timeout不属于
	 * @return
	 */
	Stopwatch getStopwatch();
		
	/**
	 * 
	 * 停止此业务
	 * @param cause
	 */
	void terminate(Exception cause);
	
	/**
	 * 
	 * 判断此Session是否停止
	 * @return
	 */
	boolean isTerminated();
}