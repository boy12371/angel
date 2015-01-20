/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-11-25
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.feinno.serialization.Codec;
import com.feinno.serialization.Serializer;
import com.feinno.util.Outter;
import com.feinno.util.io.StreamHelper;

/**
 * Rpc请求参数, 抽象后
 * 
 * @author 高磊 gaolei@feinno.com
 */
public final class RpcRequest extends RpcMessage<RpcRequestHeader>
{
	private static final Codec requestCodec = Serializer.getCodec(RpcRequestHeader.class);
	
	public RpcRequest()
	{
		super(true, new RpcRequestHeader());
	}
	
	private RpcRequest(RpcRequestHeader header)
	{
		super(true, header);
	}
	
	public void writeToStream(OutputStream out) throws IOException
	{
		RpcBinaryIdentity idt = new RpcBinaryIdentity();
		idt.setPacketMark(RpcBinaryIdentity.REQUEST_MARK);
		idt.setPacketLength(0);
		idt.setPacketOption((short) 0);

		int bodyLength = 0;
		int packetLength = 0;
		RpcBody body = getBody();
		
		ByteArrayOutputStream bodyOutput = new ByteArrayOutputStream();
		if (body != null) {
			body.encode(bodyOutput);
			bodyLength = bodyOutput.size() + 1;
			packetLength += bodyOutput.size();
		}
		 
		RpcRequestHeader h = getHeader();
		h.setBodyLength(bodyLength);
		
		Outter<Integer> len = new Outter<Integer>(Integer.valueOf(packetLength));		
		h.setExtensions(writeExtensions(len, bodyOutput));
		packetLength = len.value();
		
		ByteArrayOutputStream headerOutput = new ByteArrayOutputStream();
		requestCodec.encode(h, headerOutput);
		
		idt.setHeaderSize((short)headerOutput.size());
		packetLength += headerOutput.size() + RpcBinaryIdentity.IDENTITY_SIZE;
		idt.setPacketLength(packetLength);
		
		out.write(idt.toBuffer());
		out.write(headerOutput.toByteArray());
		out.write(bodyOutput.toByteArray());
	}
	

	public static RpcRequest fromBuffer(InputStream in, RpcBinaryIdentity idt) throws IOException
	{
		byte[] headerBuffer = new byte[idt.getHeaderSize()];
		StreamHelper.safeRead(in, headerBuffer, 0, idt.getHeaderSize());
		
		RpcRequestHeader h = requestCodec.decode(headerBuffer);
		RpcRequest request = new RpcRequest(h);

		int bodySize = h.getBodyLength() - 1;
		if (bodySize > 0) {
			byte[] bodyBuffer = new byte[bodySize];
			StreamHelper.safeRead(in, bodyBuffer, 0, bodySize);
			RpcBody body = new RpcBody(bodyBuffer, false);
			request.setBody(body);
		} else if (bodySize == 0) {
			RpcBody body = new RpcBody(RpcBody.EMPTY_BUFFER, false);
			request.setBody(body);
		} 
		
		if (h.getExtensions() != null) {
			for (RpcBodyExtension ext: h.getExtensions()) {
				int id = ext.getId();
				byte[] extBuffer = new byte[ext.getLength()];
				StreamHelper.safeRead(in, extBuffer, 0, ext.getLength());
				request.putRawExtension(id, extBuffer);
			}
		}
		return request;
	}

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("RPC Request Header：\n" + getHeader().toString());
		return buffer.toString();
	}
}
