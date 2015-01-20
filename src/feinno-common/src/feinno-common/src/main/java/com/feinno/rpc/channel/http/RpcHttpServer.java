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

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.rpc.channel.RpcBody;
import com.feinno.rpc.channel.RpcBodyExtension;
import com.feinno.rpc.channel.RpcConnectionShortServer;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.RpcRequest;
import com.feinno.rpc.channel.RpcRequestHeader;
import com.feinno.rpc.channel.RpcResponse;
import com.feinno.rpc.channel.RpcResponseHeader;
import com.feinno.rpc.channel.RpcServerChannel;
import com.feinno.serialization.Serializer;
import com.feinno.util.NumberUtils;
import com.feinno.util.io.StreamHelper;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcHttpServer extends RpcConnectionShortServer
{

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcHttpServer.class);
	private HttpAsyncExchange exchange;

	protected RpcHttpServer(RpcServerChannel channel, HttpAsyncExchange e)
	{
		super(channel, getClientEndpoint(e));
		this.exchange = e;
	}

	private void parseToRpcRequestHeader(HttpEntityEnclosingRequest request, RpcRequestHeader header)
	{
		Header userAgent = request.getFirstHeader("User-Agent");
		if (userAgent != null)
		{
			if (userAgent.getValue() != null && userAgent.getValue() != "")
			{
				String strs[] = userAgent.getValue().split("@");
				if (strs.length == 2)
				{
					header.setFromService(strs[0]);
					header.setFromComputer(strs[1]);
				}
			}
		}

		String uri = request.getRequestLine().getUri();
		if (uri.contains("?"))
		{
			String strs[] = uri.split("\\?");
			header.setContextUri(strs[0]);
			String[] args = strs[1].split("&");
			for (String arg : args)
			{
				if (arg.contains("="))
				{
					String[] params = arg.split("=");
					if (params[0].toLowerCase().equals("s"))
					{
						header.setToService(params[1]);
					}
					else if (params[0].toLowerCase().equals("m"))
					{
						header.setToMethod(params[1]);
					}
				}
			}
		}
		else
		{
			LOGGER.error(uri + " 不合法的请求路径");
		}
	}

	@SuppressWarnings("finally")
	private RpcRequest parseToRpcRequest(HttpEntityEnclosingRequest request)
	{
		RpcRequest result = new RpcRequest();
		parseToRpcRequestHeader(request, result.getHeader());

		try
		{
			// 获取请求的body-data的字节流
			InputStream in = request.getEntity().getContent();

			// 读取一个shot型的head长度
			byte[] headLength = new byte[2];
			StreamHelper.safeRead(in, headLength, 0, headLength.length);
			ByteBuffer buffer = ByteBuffer.wrap(headLength);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			short iHeadLength = buffer.getShort();

			// 如果长度为0直接读取body
			if (iHeadLength == 0)
			{
				int bodyLength = (int) (request.getEntity().getContentLength() - 2);
				byte[] bodyBuffer = new byte[bodyLength];
				StreamHelper.safeRead(in, bodyBuffer, 0, bodyBuffer.length);
				RpcBody body = new RpcBody(bodyBuffer, false);
				result.setBody(body);
				result.getHeader().setBodyLength(bodyLength);
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
				result.setBody(body);
				result.getHeader().setBodyLength(bodyLength);

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
				result.getHeader().setExtensions(rpcBodyExtensions);
				// 开始读取扩展的extension
				for (RpcBodyExtension ext : rpcBodyExtensions)
				{
					int id = ext.getId();
					byte[] extBuffer = new byte[ext.getLength()];
					StreamHelper.safeRead(in, extBuffer, 0, ext.getLength());
					result.putRawExtension(id, extBuffer);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		finally
		{
			return result;
		}
	}

	public void receive()
	{
		HttpEntityEnclosingRequest reuqest = (HttpEntityEnclosingRequest) exchange.getRequest();
		RpcRequest rpcRequest = parseToRpcRequest(reuqest);
		requestReceived(rpcRequest);
	}

	@Override
	public void doSendResponse(RpcResponse rpcResponse) throws IOException
	{
		parseHttpResponse(rpcResponse);
		exchange.submitResponse(); 
	}

	private void parseHttpResponse(RpcResponse rpcResponse) throws IOException
	{
		HttpResponse response = exchange.getResponse();
	 	response.addHeader("Warning", String.valueOf(rpcResponse.getReturnCode().intValue()));
	    response.addHeader("Content-Type", "application/x-google-protobuf");
		RpcResponseHeader header = rpcResponse.getHeader();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		// 写头长度
		if (rpcResponse.getExtensions() == null || rpcResponse.getExtensions().isEmpty())
		{
			byte[] headLength = new byte[2];
			headLength = NumberUtils.shortToByteArray((short) 0);
			stream.write(headLength);

			rpcResponse.getBody().encode(stream);
		}
		else
		{
			// rpcHttp头信息
			RpcHttpExtensionHeaderEntity extensionHeader = new RpcHttpExtensionHeaderEntity();

			// 设置body长度到头信息里面
			extensionHeader.setBodyLength(header.getBodyLength() + 1);

			// 获取并设置rpcHttp扩展的信息
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
			ByteBuffer headLengthBuffer = ByteBuffer.allocate(2);
			headLengthBuffer.order(ByteOrder.LITTLE_ENDIAN);
			headLengthBuffer.putShort((short) headLength);
			// 将所有信息写入流
			stream.write(headLengthBuffer.array());
			extensionHeader.writeTo(stream);
			rpcResponse.getBody().encode(stream);
			stream.write(extensionStream.toByteArray());
		}
		stream.flush();
		response.setEntity(new ByteArrayEntity(stream.toByteArray()));
		//System.out.println("response is "+new String(stream.toByteArray()));
		//response.addHeader("Content-Length",String.valueOf(stream.toByteArray().length));
	}

	private static RpcEndpoint getClientEndpoint(HttpAsyncExchange e)
	{
		return null;
	}

}
