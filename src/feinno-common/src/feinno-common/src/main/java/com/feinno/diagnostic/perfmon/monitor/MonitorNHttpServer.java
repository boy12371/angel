/*
 * FAE, Feinno App Engine
 * 
 * Create by gaolei 2011-9-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor;

import java.io.IOException;

import com.feinno.http.NHttpServer;
 

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */ 
public class MonitorNHttpServer
{
	 
	private static final String WEB_ROOT = "/";
	private static final String RESOURCE_ROOT = "monitor";
	private static final String DEFAULT_CONTENT = "index.html";

	private NHttpServer server;

	public MonitorNHttpServer(int port) throws IOException
	{
		server = new NHttpServer(port);
		server.add("/GetCategoryList",new NGetCategoryListHandler());
		server.add("/Subscribe",new NSubscribeHandler());
		server.add("/Unsubcribe", new NUnsubscribeCounter());
		server.add("/Pull", new NPullHandler());
		server.add("*", new NCachedStaticResourceHandler(WEB_ROOT, RESOURCE_ROOT, DEFAULT_CONTENT, MonitorNHttpServer.class));
		 
		server.start();
	}

	public static void main(String[] args) throws Exception
	{ 
		new MonitorNHttpServer(8081);
		System.in.read();
	}
}
