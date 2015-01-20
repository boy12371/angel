/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-29
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.rpc.duplex;

import java.util.concurrent.Executor;

import com.feinno.rpc.channel.RpcConnection;
import com.feinno.rpc.channel.RpcServerChannel;
import com.feinno.rpc.channel.RpcServerTransaction;
import com.feinno.rpc.server.RpcServiceDispather;
import com.feinno.util.Event;
import com.feinno.util.EventHandler;

/**
 * 
 * rpc双工服务器端
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcDuplexServer
{
	private RpcServerChannel channel;
	private RpcServiceDispather dispather;
	
	public RpcDuplexServer(RpcServerChannel channel)
	{
		this.dispather = new RpcServiceDispather();
		this.channel = channel;
		
		channel.getTransactionCreated().addListener(new EventHandler<RpcServerTransaction>() {
			@Override
			public void run(Object sender, RpcServerTransaction args)
			{
				dispather.processTransaction(args);
			}
		});
		
		channel.getConnectionCreated().addListener(new EventHandler<RpcConnection>() {
			@Override
			public void run(Object sender, RpcConnection e)
			{
				// e.getTransactionCreated().addListener(new EventHandler<RpcServerTransaction>);
			}
		});
	}
	
	public Event<RpcConnection> getConnectionCreated()
	{
		return channel.getConnectionCreated();
	}
	
	public Event<RpcConnection> getConnectionDestroyed()
	{
		return channel.getConnectionCreated();
	}
	
	public RpcServerChannel getChannel() 
	{
		return this.channel;		
	}
	
	public void registerService(Object service)
	{
		dispather.addService(service);
	}

	public void setExecutor(Executor executor)
	{
		dispather.setExecutor(executor);
	}
}
