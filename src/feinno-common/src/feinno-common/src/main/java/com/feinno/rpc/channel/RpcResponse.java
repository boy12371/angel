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
 * Rpc应答实体类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public final class RpcResponse extends RpcMessage<RpcResponseHeader>
{
	private static final Codec responseCodec = Serializer.getCodec(RpcResponseHeader.class);
	
	public static RpcResponse createResults(Object results)
	{
		RpcResponse response = new RpcResponse();
		response.setReturnCode(RpcReturnCode.OK);
		if (results != null) {
			response.setBody(new RpcBody(results, false));
		}
		return response;
	}

	public static RpcResponse createError(Throwable error)
	{
		return createError(RpcReturnCode.SERVER_ERROR, error);
	}
	
	public static RpcResponse createError(RpcReturnCode code, Throwable error)
	{
		RpcResponse response = new RpcResponse();
		response.setReturnCode(code);
		if (error != null) {
			response.setBody(new RpcBody(error, true));
		}
		return response;
	}
	
	private RpcReturnCode returnCode;
	private Codec bodyCodec;
	
	public RpcResponse()
	{
		super(false, new RpcResponseHeader());
	}

	private RpcResponse(RpcResponseHeader header)
	{
		super(false, header);
		returnCode = RpcReturnCode.valueOf(header.getResponseCode());
	}
	
	public RpcReturnCode getReturnCode()
	{
		return returnCode;
	}
	
	public void setReturnCode(RpcReturnCode code)
	{
		returnCode = code;
		getHeader().setResponseCode(code.intValue());
	}
	
	public Codec setBodyCodec()
	{
		return bodyCodec;
	}
	
	public void setBodyCodec(Codec codec)
	{
		this.bodyCodec = codec;
	}
	
	public void writeToStream(OutputStream out) throws IOException
	{
		RpcBinaryIdentity idt = new RpcBinaryIdentity();
		idt.setPacketMark(RpcBinaryIdentity.RESPONSE_MARK);
		idt.setPacketOption((short) 0);

		int bodyLength = 0;
		int packetLength = 0;		
		RpcBody body = getBody();
		ByteArrayOutputStream bodyOutput = new ByteArrayOutputStream();
	
		if (body != null) {
			try {
				if (bodyCodec != null) {
					body.encode(bodyCodec, bodyOutput);
				} else {
					body.encode(bodyOutput);
				}
			} catch (Exception e) {
				// 如果序列化失败，则将序列化失败的异常作为结果发回对端
				body = new RpcBody(e, true);
				body.encode(bodyOutput);
				setReturnCode(RpcReturnCode.SERVER_ERROR);
			}

			bodyLength = bodyOutput.size() + 1;
			packetLength += bodyOutput.size();
		}
		 
		RpcResponseHeader h = getHeader();
		h.setBodyLength(bodyLength);
		
		Outter<Integer> len = new Outter<Integer>(Integer.valueOf(packetLength));
		h.setExtensions(writeExtensions(len, bodyOutput));
		packetLength = len.value();

		ByteArrayOutputStream headerOutput = new ByteArrayOutputStream();
		responseCodec.encode(h, headerOutput);
		
		idt.setHeaderSize((short)headerOutput.size());
		packetLength += headerOutput.size() + RpcBinaryIdentity.IDENTITY_SIZE;
		idt.setPacketLength(packetLength);
		
		out.write(idt.toBuffer());
		out.write(headerOutput.toByteArray());
		out.write(bodyOutput.toByteArray());	
	}
	
	public static RpcResponse fromBuffer(InputStream in, RpcBinaryIdentity idt) throws IOException
	{
		byte[] headerBuffer = new byte[idt.getHeaderSize()];
		StreamHelper.safeRead(in, headerBuffer, 0, idt.getHeaderSize());
		
		RpcResponseHeader h = responseCodec.decode(headerBuffer);
		RpcResponse response = new RpcResponse(h);

		boolean asError = response.returnCode != RpcReturnCode.OK;
		int bodySize = h.getBodyLength() - 1;
		
		if (bodySize > 0) {
			byte[] bodyBuffer = new byte[bodySize];
			StreamHelper.safeRead(in, bodyBuffer, 0, bodySize);
			RpcBody body = new RpcBody(bodyBuffer, asError);
			response.setBody(body);			
		} else if (bodySize == 0) {
			RpcBody body = new RpcBody(RpcBody.EMPTY_BUFFER, asError);
			response.setBody(body);
		} else if (bodySize < 0) {
			response.setBody(null);
		}
		
		if (h.getExtensions() != null) {
			for (RpcBodyExtension ext: h.getExtensions()) {
				int id = ext.getId();
				byte[] extBuffer = new byte[ext.getLength()];
				StreamHelper.safeRead(in, extBuffer, 0, ext.getLength());
				response.putRawExtension(id, extBuffer);
			}
		}
		return response;
	}
}