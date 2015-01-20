/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-18
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.inproc;

import com.feinno.rpc.channel.RpcChannelSettings;
import com.feinno.rpc.channel.RpcChannelSupportFlag;
import com.feinno.rpc.channel.RpcClientChannel;
import com.feinno.rpc.channel.RpcConnection;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.util.Flags;
/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcInprocClientChannel extends RpcClientChannel
{	
	public static final Flags<RpcChannelSupportFlag> SUPPORTS = Flags.of(RpcChannelSupportFlag.NONE);
	public static final int MAX_BODY_SIZE = 64 * 1024 * 1024;
	public static final RpcChannelSettings SETTINGS = new RpcChannelSettings("inproc", SUPPORTS, MAX_BODY_SIZE);  
	public static final RpcInprocClientChannel INSTANCE = new RpcInprocClientChannel();
	
	/**
	 * {在这里补充功能说明}
	 * @param protocol
	 * @param settings
	 */
	public RpcInprocClientChannel()
	{
		super(SETTINGS);
	}
	
	public RpcConnection createConnection(RpcEndpoint ep)
	{
		return new RpcInprocConnectionClient();
	}	
}
