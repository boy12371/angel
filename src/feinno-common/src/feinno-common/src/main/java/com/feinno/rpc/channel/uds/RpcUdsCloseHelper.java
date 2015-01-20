/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-10-28
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.uds;

import java.io.Closeable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etsy.net.UnixDomainSocket;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcUdsCloseHelper
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcUdsCloseHelper.class);
	
	public static void safeClose(Closeable o)
	{
		if (o == null)
			return;
		
		try {
			o.close();
		} catch (Exception ex) {
			LOGGER.error("safe close {} failed {}", o, ex);
		}
	}
	
	public static void safeClose(UnixDomainSocket socket)
	{
		if (socket == null)
			return;
		
		try {
			socket.close();	
		} catch (Exception ex) {
			LOGGER.error("safe close {} failed {}", socket, ex);
		}
	}
}
