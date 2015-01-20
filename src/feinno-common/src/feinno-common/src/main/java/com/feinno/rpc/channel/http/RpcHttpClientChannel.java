/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 7, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.http;

import com.feinno.rpc.channel.RpcChannelSettings;
import com.feinno.rpc.channel.RpcChannelSupportFlag;
import com.feinno.rpc.channel.RpcClientChannel;
import com.feinno.rpc.channel.RpcConnection;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.util.Flags;

/**
 * TODO: 使用netty或其他io库实现基于http的rpc
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcHttpClientChannel extends RpcClientChannel
{
	public static final Flags<RpcChannelSupportFlag> SUPPORTS = Flags.of(RpcChannelSupportFlag.NONE);
	public static final int MAX_BODY_SIZE = 64 * 1024 * 1024;
	public static final RpcChannelSettings SETTINGS = new RpcChannelSettings("http", SUPPORTS, MAX_BODY_SIZE);
	public static final RpcClientChannel INSTANCE = new RpcHttpClientChannel();  

	public RpcHttpClientChannel()
    {
	    super(SETTINGS); 
    }

	@Override
    public RpcConnection createConnection(RpcEndpoint ep)
    {
	    return new RpcHttpClient(ep);
    }
	
}
