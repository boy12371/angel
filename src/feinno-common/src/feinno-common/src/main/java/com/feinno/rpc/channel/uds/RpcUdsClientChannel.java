/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-7-30
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.uds;

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
public class RpcUdsClientChannel extends RpcClientChannel
{
	public static final Flags<RpcChannelSupportFlag> SUPPORTS = Flags.of(RpcChannelSupportFlag.NONE);
	public static final int MAX_BODY_SIZE = 64 * 1024 * 1024;
	public static final RpcChannelSettings SETTINGS = new RpcChannelSettings("uds", SUPPORTS, MAX_BODY_SIZE);  
	public static final RpcUdsClientChannel INSTANCE = new RpcUdsClientChannel();
	
	public RpcUdsClientChannel()
	{
		super(SETTINGS);
	}

	@Override
	public RpcConnection createConnection(RpcEndpoint ep)
	{
		return new RpcUdsConnectionClient(ep);
	}
}
