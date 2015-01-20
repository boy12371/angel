/*
 * FAE, Feinno App Engine
 *  
 * Create by 李会军 2011-1-19
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.tcp;

import static org.jboss.netty.buffer.ChannelBuffers.wrappedBuffer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.rpc.channel.RpcMessage;
import com.feinno.rpc.channel.RpcRequest;
import com.feinno.rpc.channel.RpcResponse;

/**
 * 基于TCP传输的RPC消息编码器
 * 
 * @author 李会军
 */
public class RpcTcpMessageEncoder extends OneToOneEncoder
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcTcpMessageEncoder.class);
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception
	{
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			RpcMessage message = (RpcMessage)msg;
			if (message.isRequest()) {
				RpcRequest request = (RpcRequest)msg;
				request.writeToStream(output);
			} else {
				RpcResponse response = (RpcResponse)msg;
				response.writeToStream(output);
			}
			
			ChannelBuffer buffer = wrappedBuffer(ByteOrder.BIG_ENDIAN, output.toByteArray());
			return buffer;
		} catch (Exception ex) {
			LOGGER.error("messageEncode failed! {}", ex);
			throw ex;
		}
	}
}
