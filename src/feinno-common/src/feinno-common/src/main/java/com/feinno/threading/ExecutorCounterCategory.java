/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-14
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import com.feinno.diagnostic.perfmon.PerformanceCounter;
import com.feinno.diagnostic.perfmon.PerformanceCounterCategory;
import com.feinno.diagnostic.perfmon.PerformanceCounterType;
import com.feinno.diagnostic.perfmon.SmartCounter;

/**
 * 
 * <b>描述: </b>这是一个线程计数器
 * <p>
 * <b>功能: </b>线程计数器
 * <p>
 * <b>用法: </b>由内部逻辑调用，外部由diagnostic模块负责展现
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 * 
 */
@PerformanceCounterCategory("thread-pool")
public class ExecutorCounterCategory
{
	@PerformanceCounter(name = "size", type = PerformanceCounterType.NUMBER)
	private SmartCounter sizeCounter;
	
	@PerformanceCounter(name = "worker", type = PerformanceCounterType.TRANSACTION)
	private SmartCounter workerCounter;

	public SmartCounter getSizeCounter()
	{
		return sizeCounter;
	}

	public void setSizeCounter(SmartCounter sizeCounter)
	{
		this.sizeCounter = sizeCounter;
	}

	public SmartCounter getWorkerCounter()
	{
		return workerCounter;
	}

	public void setWorkerCounter(SmartCounter workerCounter)
	{
		this.workerCounter = workerCounter;
	}
}
