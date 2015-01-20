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

import com.etsy.net.JUDS;
import com.etsy.net.UnixDomainSocketClient;
import com.feinno.rpc.channel.RpcBinaryIdentity;
import com.feinno.rpc.channel.RpcBody;
import com.feinno.rpc.channel.RpcConnectionShortClient;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.RpcRequest;
import com.feinno.rpc.channel.RpcResponse;
import com.feinno.rpc.channel.RpcReturnCode;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcUdsConnectionClient extends RpcConnectionShortClient
{
	private static Logger LOGGER = LoggerFactory.getLogger(RpcUdsConnectionClient.class);
	
	public RpcUdsConnectionClient(RpcEndpoint ep)
	{
		super(ep);
	}

	@Override
	public void doSendRequest(RpcRequest request) throws IOException
	{
		String path = ((RpcUdsEndpoint) this.getRemoteEndpoint()).getPath();

		RpcResponse response = null;
		UnixDomainSocketClient socket = null;
		InputStream in = null;
		OutputStream out = null;

		try {
			socket = new UnixDomainSocketClient(path, JUDS.SOCK_STREAM);
		} catch (Exception ex) {
			LOGGER.error("sendRequest() failed", ex);
			throw new IOException("create UDS failed", ex);
		}

		in = socket.getInputStream();
		out = socket.getOutputStream();
		try {
			request.writeToStream(out);
		} catch (Exception ex) {
			response = new RpcResponse();
			response.setReturnCode(RpcReturnCode.SEND_FAILED);
			response.setBody(new RpcBody(ex, true));
		}

		try {
			if (response == null) {
				RpcBinaryIdentity idt = RpcBinaryIdentity.fromStream(in);
				response = RpcResponse.fromBuffer(in, idt);
			}
		} catch (Exception ex) {
			response = new RpcResponse();
			response.setReturnCode(RpcReturnCode.REQUEST_FORMAT_FAILED);
			response.setBody(new RpcBody(ex, true));
		}
		responseReceived(response);
		
		RpcUdsCloseHelper.safeClose(in);
		RpcUdsCloseHelper.safeClose(out);
		RpcUdsCloseHelper.safeClose(socket);
	}
}
