/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-6-5
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.initialization;

/**
 * 
 * <b>描述: </b>启动双检锁
 * <p>
 * <b>功能: </b>双检锁,保证任务只被执行一次
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 * 
 */
public class InitialLock
{
	private boolean inited = false;
	private Object syncRoot = new Object();
	
	public InitialLock()
	{
	}
	
	public void doInit(Runnable initer)
	{
		if (inited)
			return;
		
		synchronized (syncRoot) {
			if (!inited) {
				initer.run();
				inited = true;
			}
		}
	}
}
