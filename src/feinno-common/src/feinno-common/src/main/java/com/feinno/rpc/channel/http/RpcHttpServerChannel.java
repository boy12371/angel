/*
 * FAE, Feinno App Engine
 * 
 * Create by gaolei 2012-9-7
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.http;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HttpContext;

import com.feinno.http.NHttpServer;
import com.feinno.rpc.channel.RpcServerChannel;

/**
 * RpcOverHttp的服务器端
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcHttpServerChannel extends RpcServerChannel
{
	private NHttpServer server;

	public RpcHttpServerChannel(int port)
	{
		this(new InetSocketAddress(port));
	}

	public RpcHttpServerChannel(String host, int port)
	{
		this(new InetSocketAddress(host, port));
	}

	public RpcHttpServerChannel(InetSocketAddress isa)
	{
		super(RpcHttpClientChannel.SETTINGS, new RpcHttpEndpoint(isa));

		RpcHttpEndpoint serverEndpoint = (RpcHttpEndpoint) this.getServerEndpoint();
		server = new NHttpServer(serverEndpoint.getAddress().getHostAddress(), serverEndpoint.getInetSocketAddress().getPort());
		server.setServerchannel(this);
		server.add("/rpc.do", new HttpAsyncRequestHandler<HttpRequest>()
		{
			@Override
			public HttpAsyncRequestConsumer processRequest(HttpRequest request, HttpContext context) throws HttpException, IOException
			{
				return new BasicAsyncRequestConsumer();
			}

			@Override
			public void handle(HttpRequest request, HttpAsyncExchange httpExchange, HttpContext context) throws HttpException, IOException
			{
				HttpResponse response = httpExchange.getResponse();
				response.setStatusCode(HttpStatus.SC_OK);
				if (request.getRequestLine().getMethod().toLowerCase().equals("post"))
				{
					//System.out.println("request is "+request);
					RpcHttpServer s = new RpcHttpServer(RpcHttpServerChannel.this, httpExchange); 
					s.receive();
				}
			}
		});

		// 这里没有意义因为在RpcConnectionShortServer中直接用的serverchannel的firevent方法

		/*this.getConnectionCreated().addListener(new EventHandler<RpcConnection>()
		{

			@Override
			public void run(Object sender, RpcConnection args)
			{
				args.getTransactionCreated().addListener(new EventHandler<RpcServerTransaction>()
				{

					@Override
					public void run(Object sender, RpcServerTransaction args)
					{
						RpcHttpServerChannel.this.getTransactionCreated().fireEvent(args);
					}
				});
			}
		});
*/
	}

	@Override
	protected void doStart() throws Exception
	{
		server.start();
	}

	@Override
	protected void doStop() throws Exception
	{
		server.stop();
	}
}
