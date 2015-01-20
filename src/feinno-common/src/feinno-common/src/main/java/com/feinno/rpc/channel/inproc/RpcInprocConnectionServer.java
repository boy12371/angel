/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Apr 7, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.inproc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.rpc.channel.RpcBinaryIdentity;
import com.feinno.rpc.channel.RpcConnectionShortServer;
import com.feinno.rpc.channel.RpcResponse;

/**
 * 
 * 进程内Rpc短连接服务器端
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcInprocConnectionServer extends RpcConnectionShortServer
{
	private RpcInprocConnectionClient client;
	
	private Logger LOGGER = LoggerFactory.getLogger(RpcInprocConnectionServer.class);
	
	protected RpcInprocConnectionServer(RpcInprocConnectionClient client)
	{
		super(RpcInprocServerChannel.INSTANCE, RpcInprocEndpoint.INSTANCE);
		this.client = client;
		RpcInprocServerChannel.INSTANCE.getConnectionCreated().fireEvent(this);
	}

	@Override
	public void doSendResponse(RpcResponse response)
	{
		LOGGER.info("RpcInprocConnectionClient send response.");
		// 为了配合HotServer，进程内调用时，首先需要把RpcResponse序列化一次
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			response.writeToStream(outStream);

			ByteArrayInputStream inputStream = new ByteArrayInputStream(outStream.toByteArray());
			RpcBinaryIdentity idt = RpcBinaryIdentity.fromStream(inputStream);
			response = RpcResponse.fromBuffer(inputStream, idt);
		} catch (IOException e) {
			LOGGER.warn("Conversion inproc rpcResponse failed",e);
		}
		client.responseReceived(response);
	}
}
