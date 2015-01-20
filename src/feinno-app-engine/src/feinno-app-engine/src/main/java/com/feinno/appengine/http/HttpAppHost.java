/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-15
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.http.server.JettyServer;
import com.feinno.appengine.runtime.AppHost;
import com.feinno.appengine.testing.HttpDebugProxy;
import com.feinno.ha.ServiceSettings;
import com.feinno.rpc.duplex.RpcDuplexClient;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HttpAppHost extends AppHost
{
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpAppHost.class);
	private JettyServer server;
	private int port;
	private String host;

	public HttpAppHost()
	{
		port = ServiceSettings.INSTANCE.getServicePort("rpc_http");
		host = ServiceSettings.INSTANCE.getServerAddress(); 
		if (host == null) {
			host = "0.0.0.0";
			LOGGER.warn("!!!!! *NO* ServiceHost Configured, using 0.0.0.0 !.");
		}
		
		server = new JettyServer(host,port,false);
	}

	protected void start() throws Exception
	{
		
		server.start(); 
	}

	@Override
	protected void stop() throws Exception
	{
		server.stop();
	}

	@Override
	protected void register(AppBean bean, AppBeanAnnotations annos)
	{
		// TODO call prepare, setup etc
		String categoryMinusName = annos.getCategoryMinusName();
		if (LOGGER.isDebugEnabled()) {
			String type = annos.getClassInfo().getType();
			LOGGER.debug("type [{}], servie name [{}]", type, categoryMinusName);
		}

		if (bean instanceof HttpAppBean) {
			server.registerBean(categoryMinusName, (HttpAppBean<?>) bean);
		} else {
			LOGGER.warn("bean [{}] is NOT a instance of HttpAppBean, rejected.", bean);
		}
	}

	@Override
	public void unload(String catMinusName)
	{
		super.unload(catMinusName);
		server.unregisterBean(catMinusName);
	}

	@Override
	protected String getServiceUrl()
	{
		// TODO Auto-generated method stub
		return "http=http://" + host + ":" + port;
	}

	@Override
	public void registerInjectorService(RpcDuplexClient client)
	{
		port = HttpDebugProxy.HTTP_DEBUG_PORT;
		host = ServiceSettings.INSTANCE.getServerAddress(); 
		if (host == null) {
			host = "0.0.0.0";
			LOGGER.warn("!!!!! *NO* ServiceHost Configured, using 0.0.0.0 !.");
		}
		server = new JettyServer(host,port,false);
		// Map<String, AppBean> apps
		for (String categoryMinusName : apps.keySet()) {
			server.registerBean(categoryMinusName, (HttpAppBean<?>) apps.get(categoryMinusName));
		}
		try {
			start();
		} catch (Exception e) {
			LOGGER.warn(String.format("RegisterInjectorService HttpAppHost failed.host=%s,port=%s", host, port), e);
		}
	}
}
