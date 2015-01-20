/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.feinno.diagnostic.perfmon.monitor.legacy.ListHandler;
import com.feinno.diagnostic.perfmon.monitor.legacy.ShowTypesHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@SuppressWarnings("restriction")
public class MonitorHttpServer
{
//	public void start()
//	{
//		HttpServer server = new HttpServer();
////		server.create
//		
//	}
	private static final String WEB_ROOT = "/";
	private static final String RESOURCE_ROOT = "monitor";
	private static final String DEFAULT_CONTENT = "index.html";
	
	private HttpServer server;
	
	public MonitorHttpServer(int port) throws IOException
	{
		server = HttpServer.create(new InetSocketAddress(port), 0);
		registerStaticHandlers(server);
		server.createContext("/", new CachedStaticResourceHandler(WEB_ROOT, RESOURCE_ROOT, DEFAULT_CONTENT, MonitorHttpServer.class));
		server.createContext("/GetCategoryList", new GetCategoryListHandler());
		server.createContext("/Subscribe", new SubscribeHandler()).getFilters().add(new ParameterFilter());
		server.createContext("/Unsubcribe", new UnsubscribeCounter()).getFilters().add(new ParameterFilter());
		server.createContext("/Pull", new PullHandler()).getFilters().add(new ParameterFilter());
		
		server.createContext("/list", new ListHandler());
		server.createContext("/showTypes", new ShowTypesHandler()).getFilters().add(new ParameterFilter());
		server.start();
	}
	
	
	private void registerStaticHandlers(HttpServer server2)
	{
				
	}


	public static void main(String[] args) throws Exception
	{
		new MonitorHttpServer(8089);
		System.in.read();
	}
}
