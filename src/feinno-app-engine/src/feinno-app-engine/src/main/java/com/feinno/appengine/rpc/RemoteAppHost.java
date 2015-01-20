/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-15
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.appengine.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.AppContext;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.runtime.AppHost;
import com.feinno.configuration.ConfigurationNotFoundException;
import com.feinno.ha.ServiceSettings;
import com.feinno.rpc.channel.RpcReturnCode;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcServiceBase;
import com.feinno.rpc.server.RpcServiceBootstrap;

/**
 * Rpc方式下到AppHost
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RemoteAppHost extends AppHost
{
	public static final String SERVICE_NAME = "FAE";
	public static final String ROUTER_SERVICE_NAME = "RouterService";
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteAppHost.class);
	private int port;
	private RpcTcpServerChannel channel;
	
	public RemoteAppHost() throws ConfigurationNotFoundException
	{
		super();
		port = ServiceSettings.INSTANCE.getServicePort("rpc");
		channel = new RpcTcpServerChannel(port);
		RpcServiceBootstrap.registerChannel(channel);

		RpcServiceBootstrap.registerService(new RpcServiceBase(SERVICE_NAME, false) {
			@Override
			public void process(RpcServerContext ctx) {
				processRpcRequest(ctx);
			}
		});
	}
	
	@Override
	public void registerInjectorService(RpcDuplexClient client)
	{
		client.registerCallbackService(new RpcServiceBase(SERVICE_NAME,false) {
			@Override
			public void process(RpcServerContext ctx)
			{
				processRpcRequest(ctx);
			}
		}
		);
	}
	
	private void processRpcRequest(RpcServerContext ctx)
	{
		String categoryMinusName = ctx.getToMethod();
		LOGGER.info("RemoteAppHost.processRpcRequest categoryMinusName:{}",categoryMinusName);
		AppBean b =  super.getBean(categoryMinusName);
		if (b == null) {
			LOGGER.error("RemoteAppHost.processRpcRequest SERVICE_NOT_FOUND! categoryMinusName:{}",categoryMinusName);
			ctx.end(RpcReturnCode.SERVICE_NOT_FOUND, null);
			return;
		}
		
		@SuppressWarnings("unchecked")
		RemoteAppBean<?, ?, ? extends AppContext> bean = (RemoteAppBean<?, ?, ? extends AppContext>)b;
		bean.processRequest(ctx);
	}

	/*
	 * @see com.feinno.appengine.runtime.AppHost#start()
	 */
	/**
	 * {在这里补充功能说明}
	 */
	@Override
	protected void start() throws Exception
	{
		channel.start();
	}

	/*
	 * @see com.feinno.appengine.runtime.AppHost#stop()
	 */
	/**
	 * {在这里补充功能说明}
	 */
	@Override
	protected void stop() throws Exception
	{
		channel.stop();
	}
	/*
	 * @see com.feinno.appengine.runtime.AppHost#register(com.feinno.appengine.AppBean, com.feinno.appengine.configuration.FAE_Application, java.lang.String)
	 */
	/**
	 * {在这里补充功能说明}
	 * @param bean
	 * @param app
	 * @param serviceName
	 */
	@Override
	protected void register(AppBean bean, AppBeanAnnotations annos)
	{
		// seems Nothing to do
	}

	/*
	 * @see com.feinno.appengine.runtime.AppHost#getServiceUrl()
	 */
	/**
	 * {在这里补充功能说明}
	 * @return
	 */
	@Override
	protected String getServiceUrl()
	{
		return String.format("rpc=tcp://%s:%d", ServiceSettings.INSTANCE.getServerAddress(), port);
	}
}
