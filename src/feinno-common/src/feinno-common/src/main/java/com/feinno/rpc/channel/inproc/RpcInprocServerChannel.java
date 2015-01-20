/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-18
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.inproc;

import com.feinno.rpc.channel.RpcConnection;
import com.feinno.rpc.channel.RpcServerChannel;
import com.feinno.rpc.channel.RpcServerTransaction;
import com.feinno.util.EventHandler;

/**
 * 进程内服务器调用
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcInprocServerChannel extends RpcServerChannel
{
	public static final RpcInprocServerChannel INSTANCE = new RpcInprocServerChannel();
	
	private RpcInprocServerChannel()
	{
		super(RpcInprocClientChannel.SETTINGS, RpcInprocEndpoint.INSTANCE);
		
		this.getConnectionCreated().addListener(new EventHandler<RpcConnection>() {
			@Override
			public void run(Object sender, RpcConnection args)
			{
				args.getTransactionCreated().addListener(new EventHandler<RpcServerTransaction>() {
					@Override
					public void run(Object sender, RpcServerTransaction args)
					{
						RpcInprocServerChannel.this.getTransactionCreated().fireEvent(args);						
					}
				});
			}
		});
	}

	/*
	 * @see com.feinno.rpc.channel.RpcServerChannel#doStart()
	 */
	/**
	 * 启动Channel, 没啥好启动的
	 * @throws Throwable
	 */
	@Override
	protected void doStart() throws Exception
	{
		// do nothing		
	}
	
	/*
	 * @see com.feinno.rpc.channel.RpcServerChannel#doStop()
	 */
	/**
	 * 也没啥好停止的
	 * @throws Throwable
	 */
	@Override
	protected void doStop() throws Exception
	{
		// do nothing
	}
}
