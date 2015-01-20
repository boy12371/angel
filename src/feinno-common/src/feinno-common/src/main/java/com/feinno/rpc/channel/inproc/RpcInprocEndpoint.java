/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-12
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.inproc;

import com.feinno.rpc.channel.RpcClientChannel;
import com.feinno.rpc.channel.RpcEndpoint;

/**
 * 进程内RpcEndpoint, 单例, 仅有这一个
 * 
 * @author 高磊 gaolei@feinno.com
 */
public final class RpcInprocEndpoint extends RpcEndpoint
{
	public static final RpcInprocEndpoint INSTANCE = new RpcInprocEndpoint();
	
	private RpcInprocEndpoint()
	{	
	}

	/*
	 * @see com.feinno.route.Uri#hashCode()
	 */
	/**
	 * 所有RpcInprocEndpoint均相等
	 * @return
	 */
	@Override
	public int hashCode()
	{
		return 1;
	}

	/*
	 * @see com.feinno.route.Uri#equals(java.lang.Object)
	 */
	/**
	 * 所有RpcInprocEndpoint均相等
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof RpcInprocEndpoint);
	}

	/*
	 * @see com.feinno.rpc.RpcEndpoint#getProtocol()
	 */
	/**
	 * {在这里补充功能说明}
	 * @return
	 */
	@Override
	public String getProtocol()
	{
		return "inproc";
	}
	
	/*
	 * @see com.feinno.route.Uri#toString()
	 */
	/**
	 * {在这里补充功能说明}
	 * @return
	 */
	@Override
	public String toString()
	{
		return "inproc://";
	}
	
	/*
	 * @see com.feinno.rpc.RpcEndpoint#getClientChannel()
	 */
	/**
	 * {在这里补充功能说明}
	 * @return
	 */
	@Override
	public RpcClientChannel getClientChannel()
	{
		return RpcInprocClientChannel.INSTANCE;
	}

	/*
	 * @see com.feinno.rpc.RpcEndpoint#parseWithInner(java.lang.String)
	 */
	/**
	 * {在这里补充功能说明}
	 * @param strWithoutProtocol
	 */
	@Override
	protected void parseAddress(String strWithoutProtocol)
	{
		// DO NOTHING
	}
}
