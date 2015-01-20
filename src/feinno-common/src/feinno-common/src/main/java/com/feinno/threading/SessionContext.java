/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-21
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import com.feinno.diagnostic.perfmon.Stopwatch;
import com.feinno.util.container.SessionPool;

/**
 * 
 * <b>描述: </b>这是一个用于存储Session信息上下文的Java类，它通过{@link SessionPool}来控制并存的Session数量
 * <p>
 * <b>功能: </b>用于存储Session信息上下文的Java类
 * <p>
 * <b>用法: </b>主要用于{@link ThreadContext}使用，因此具体使用方式请参见{@link ThreadContext}
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 * @see SessionPool
 * @see ThreadContext
 */
public abstract class SessionContext implements Runnable
{
	private int id;
	private int timeout;
	private Stopwatch watch;
	private boolean terminated;
	
	public int getId()
	{
		return id;
	}
	/**
	 * 
	 * @param timeout 毫秒
	 */
	public SessionContext(int timeout)
	{
		this.timeout = timeout;
		
	}
	
	public boolean isTimeout()
	{
		return watch.getMillseconds() > timeout;
	}
	
	public int getSessionId()
	{
		return this.id;
	}
	
	public void setSessionId(int id)
	{
		this.id = id;
	}

	public void terminate(boolean byTimeout)
	{
		// TODO Auto-generated method stub
	}
}
