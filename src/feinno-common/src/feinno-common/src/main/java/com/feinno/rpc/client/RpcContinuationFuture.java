/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-9-6
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.client;

import org.apache.commons.javaflow.Continuation;

import com.feinno.rpc.channel.RpcException;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.threading.ThreadContextName;
import com.feinno.threading.ThreadContext;
import com.feinno.util.EventHandler;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcContinuationFuture
{
	private Continuation c;
	private RpcFuture future;
	
	public RpcContinuationFuture(RpcFuture future)
	{
		this.future = future;
	}
	
	public <V> V getResult(Class<V> clazz) throws RpcException
	{
		c = (Continuation)ThreadContext.getCurrent().getNamedContext(ThreadContextName.JAVA_FLOW);
		
		if (future.isDone()) {
			return future.getResult(clazz);
		}
		future.addListener(new EventHandler<RpcResults>() {
			@Override
			public void run(Object sender, RpcResults e)
			{
				Continuation.continueWith(c); 				
			}
		});
		Continuation.suspend();
		return future.getResult(clazz);
	}
}
