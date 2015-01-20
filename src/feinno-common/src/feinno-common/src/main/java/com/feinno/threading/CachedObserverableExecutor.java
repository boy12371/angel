/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 8, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 
 * 具备计数器的CachedObserverExecutor
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class CachedObserverableExecutor extends ObservableExecutor
{ 
	private ThreadPoolExecutor innerExecutor;
	
	public CachedObserverableExecutor(String name, Executor executor)
	{
		super(name, executor);
		this.innerExecutor = (ThreadPoolExecutor)executor;
	}
	
	@Override
	public void execute(Runnable task)
	{
		this.getSizeCounter().setRawValue(innerExecutor.getActiveCount());
		super.execute(task);
	}
}
