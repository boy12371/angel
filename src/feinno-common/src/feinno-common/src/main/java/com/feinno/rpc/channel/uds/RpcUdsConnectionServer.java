/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-7-30
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.uds;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etsy.net.UnixDomainSocket;
import com.feinno.rpc.channel.RpcBinaryIdentity;
import com.feinno.rpc.channel.RpcConnectionShortServer;
import com.feinno.rpc.channel.RpcRequest;
import com.feinno.rpc.channel.RpcResponse;
import com.feinno.rpc.channel.RpcServerChannel;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcUdsConnectionServer extends RpcConnectionShortServer
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcUdsConnectionServer.class);
	private UnixDomainSocket socket;

	public RpcUdsConnectionServer(RpcServerChannel channel, UnixDomainSocket socket) throws IOException
	{
		super(channel, RpcUdsEndpoint.EMPTY);
		this.socket = socket;
	}
	
	public void receive()
	{
		InputStream in = null;
		try {
			in = socket.getInputStream();
			RpcBinaryIdentity idt = RpcBinaryIdentity.fromStream(in);
			RpcRequest request = RpcRequest.fromBuffer(in, idt);
			requestReceived(request);
		} catch (Exception ex) {
			LOGGER.error("UDS receive error", ex);
		} finally {
			RpcUdsCloseHelper.safeClose(in);
		}		
	}

	@Override
	public void doSendResponse(RpcResponse response) throws IOException
	{
		if (socket == null) {
			throw new IllegalStateException("socket alread closed");
		}
			
		OutputStream os = null;
		try {
			os = socket.getOutputStream();
			response.writeToStream(os);
			os.close();
		} catch (Exception ex) {
			LOGGER.error("sendResponse failed {}", ex);
			throw new IOException("sendResponse failed", ex);
		} finally {
			RpcUdsCloseHelper.safeClose(os);
			RpcUdsCloseHelper.safeClose(socket);
			socket = null;
		}
	}
}
