/*
 * FAE, Feinno App Engine
 * 
 * Create by gaolei 2012-9-7
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.http.NHttpClient;
import com.feinno.rpc.channel.RpcBody;
import com.feinno.rpc.channel.RpcBodyExtension;
import com.feinno.rpc.channel.RpcConnectionShortClient;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.RpcRequest;
import com.feinno.rpc.channel.RpcRequestHeader;
import com.feinno.rpc.channel.RpcResponse;
import com.feinno.rpc.channel.RpcReturnCode;
import com.feinno.serialization.Serializer;
import com.feinno.util.NumberUtils;
import com.feinno.util.io.StreamHelper;

/**
 * {在这里补充类的功能说明}
 * 
 * @author zhouyang
 */
public class RpcHttpClient extends RpcConnectionShortClient
{

	private static Logger LOGGER = LoggerFactory.getLogger(RpcHttpClient.class);

	public RpcHttpClient(RpcEndpoint ep)
	{
		super(ep);
	}

	/**
	 * 把RpcRequest转成HttpRequest
	 * 
	 * @param rpcRequest
	 * @return
	 * @throws IOException
	 */
	private BasicHttpEntityEnclosingRequest parseToHttpRequest(RpcRequest rpcRequest) throws IOException
	{
		StringBuffer url = new StringBuffer();
		url.append("/");
		url.append("rpc.do");
		url.append("?s=");
		url.append(rpcRequest.getHeader().getToService());
		url.append("&m=");
		url.append(rpcRequest.getHeader().getToMethod());

		BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", url.toString());

		RpcRequestHeader header = rpcRequest.getHeader();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// 写头长度
		if (rpcRequest.getExtensions() == null || rpcRequest.getExtensions().isEmpty())
		{
			stream.write(NumberUtils.shortToByteArray((short) 0));

			rpcRequest.getBody().encode(stream);
		}
		else
		{
			// rpcHttp头信息
			RpcHttpExtensionHeaderEntity extensionHeader = new RpcHttpExtensionHeaderEntity();

			// 设置body长度到头信息里面
			extensionHeader.setBodyLength(header.getBodyLength() + 1);

			List<RpcHttpExtensionEntity> rpcHttpExtensionEntitys = new ArrayList<RpcHttpExtensionEntity>();
			ByteArrayOutputStream extensionStream = new ByteArrayOutputStream();

			if (header.getExtensions() != null)
			{
				for (RpcBodyExtension extension : header.getExtensions())
				{
					RpcHttpExtensionEntity entity = new RpcHttpExtensionEntity();
					entity.setId(extension.getId());
					entity.setLength(extension.getLength());
					extension.writeTo(extensionStream);
					rpcHttpExtensionEntitys.add(entity);
				}
			}
			// 设置扩展的信息到头信息里面
			extensionHeader.setExtensions(rpcHttpExtensionEntitys);

			// 获取头信息的长度
			ByteArrayOutputStream headStream = new ByteArrayOutputStream();
			extensionHeader.writeTo(headStream);
			int headLength = headStream.size();

			// 两个byte存放头信息长度
			// 两个byte存放头信息长度
			ByteBuffer headLengthBuffer = ByteBuffer.allocate(2);
			headLengthBuffer.order(ByteOrder.LITTLE_ENDIAN);
			headLengthBuffer.putShort((short) headLength);
			// 将所有信息写入流
			stream.write(headLengthBuffer.array());
			extensionHeader.writeTo(stream);
			rpcRequest.getBody().encode(stream);
			stream.write(extensionStream.toByteArray());
		}
		stream.flush();
		request.setEntity(new ByteArrayEntity(stream.toByteArray()));
		return request;
	}

	@Override
	public void doSendRequest(final RpcRequest req) throws IOException
	{
		final String hostname = ((RpcHttpEndpoint) this.getRemoteEndpoint()).getInetSocketAddress().getHostName();
		final int port = ((RpcHttpEndpoint) this.getRemoteEndpoint()).getInetSocketAddress().getPort();
		BasicHttpEntityEnclosingRequest request = this.parseToHttpRequest(req);

		NHttpClient.Instance.send(hostname, port, request, new FutureCallback<HttpResponse>()
		{
			@Override
			public void cancelled()
			{
				LOGGER.info(hostname + ":" + port + " cancelled");
			}

			@Override
			public void completed(HttpResponse result)
			{
				responseReceived(parseToRpcResponse(result));
				LOGGER.info(hostname + ":" + port + " deal completed");
			}

			@Override
			public void failed(Exception ex)
			{
				LOGGER.error("doSendRequest"+req.toString(), ex);
				ex.printStackTrace();
			}

		});

	}

	/**
	 * 从HttpResponse转换成RpcResponse
	 * 
	 * @param response
	 * @return
	 */
	private RpcResponse parseToRpcResponse(HttpResponse response)
	{
		RpcResponse rpcResponse = new RpcResponse();
		rpcResponse.setReturnCode(RpcReturnCode.valueOf(response.getStatusLine().getStatusCode()));

		try
		{
			// 获取请求的body-data的字节流
			InputStream in = response.getEntity().getContent();
			// 读取一个shot型的head长度
			byte[] headLength = new byte[2];
			StreamHelper.safeRead(in, headLength, 0, headLength.length);
			ByteBuffer buffer = ByteBuffer.wrap(headLength);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			short iHeadLength = buffer.getShort();

			// 如果长度为0直接读取body
			if (iHeadLength == 0)
			{
				int bodyLength = (int) (response.getEntity().getContentLength() - 2);
				byte[] bodyBuffer = new byte[bodyLength];
				StreamHelper.safeRead(in, bodyBuffer, 0, bodyBuffer.length);
				RpcBody body = new RpcBody(bodyBuffer, false);
				rpcResponse.setBody(body);
				rpcResponse.getHeader().setBodyLength(bodyLength);
			}
			// 表示有extension
			else
			{
				// 代表头信息的字节数组
				byte[] headBuffer = new byte[iHeadLength];

				StreamHelper.safeRead(in, headBuffer, 0, headBuffer.length);

				RpcHttpExtensionHeaderEntity extensionHeader = Serializer.decode(RpcHttpExtensionHeaderEntity.class, headBuffer);

				// 真正的body长度
				int bodyLength = extensionHeader.getBodyLength() - 1;

				// 开始读取body
				byte[] bodyBuffer = new byte[bodyLength];
				StreamHelper.safeRead(in, bodyBuffer, 0, bodyBuffer.length);
				RpcBody body = new RpcBody(bodyBuffer, false);
				rpcResponse.setBody(body);
				rpcResponse.getHeader().setBodyLength(bodyLength);

				// 扩展的extension
				List<RpcBodyExtension> rpcBodyExtensions = new ArrayList<RpcBodyExtension>();
				if (extensionHeader != null && extensionHeader.getExtensions() != null)
				{
					for (RpcHttpExtensionEntity entity : extensionHeader.getExtensions())
					{
						RpcBodyExtension rpcBodyExtension = new RpcBodyExtension();
						rpcBodyExtension.setId(entity.getId());
						rpcBodyExtension.setLength(entity.getLength());
						rpcBodyExtensions.add(rpcBodyExtension);
					}
				}
				rpcResponse.getHeader().setExtensions(rpcBodyExtensions);
				// 开始读取扩展的extension
				for (RpcBodyExtension ext : rpcBodyExtensions)
				{
					int id = ext.getId();
					byte[] extBuffer = new byte[ext.getLength()];
					StreamHelper.safeRead(in, extBuffer, 0, ext.getLength());
					rpcResponse.putRawExtension(id, extBuffer);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("parseToRpcResponse", e);
		}
		return rpcResponse;

	}

}
