/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-27
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.util.StringUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 使用resources目录作为静态页面负载的http服务器处理类
 * 
 * @author 高磊 gaolei@feinno.com
 */
@SuppressWarnings("restriction")
public class CachedStaticResourceHandler implements HttpHandler
{
	private static class Entry 
	{
		private int statusCode;
		private byte[] buffer;
	}
	
	private String requestRoot;
	private String resourceRoot;
	private String defaultContent;
	private Class<?> rootClazz;
	private Object syncRoot;
	private Map<String, Entry> entrys;

	private static final Logger LOGGER = LoggerFactory.getLogger(CachedStaticResourceHandler.class);
	
	public CachedStaticResourceHandler(String requestRoot, String resourceRoot, String defaultContent, Class<?> clazz)
	{
		this.requestRoot = requestRoot;
		this.resourceRoot = resourceRoot;
		if (!this.resourceRoot.endsWith("/")) {
			this.resourceRoot = this.resourceRoot + "/";
		}
		if (!this.resourceRoot.startsWith("/")) {
			this.resourceRoot = "/" + this.resourceRoot; 
		}
		this.rootClazz = clazz;
		syncRoot = new Object();
		entrys = new Hashtable<String, Entry>();

		if (defaultContent != null) {
			this.defaultContent = defaultContent;
		} else {
			this.defaultContent = "";
		}
	}

	@Override
	public void handle(HttpExchange e) throws IOException
	{
		LOGGER.info("http request received {}",e);
		String rpath = e.getRequestURI().getRawPath().substring(requestRoot.length());
		if (StringUtils.isNullOrEmpty(rpath)) {
			if (!"".equals(defaultContent)) {
				String redirect = e.getRequestURI().getRawPath() +defaultContent;
				e.getResponseHeaders().add("Location", redirect);
				e.sendResponseHeaders(302, 0);
				e.close();
				LOGGER.info("redirect to {}", redirect);
				return;
			}
		}
		
		Entry entry;
		synchronized (syncRoot) {
			entry = entrys.get(rpath);
			if (entry == null) {
				entry = loadEntry(rpath);
				entrys.put(rpath, entry);
			}
		}
		if (entry.statusCode != 200) {
			e.sendResponseHeaders(entry.statusCode, 0);
			e.close();
		} else {
			e.sendResponseHeaders(200, entry.buffer.length);
			OutputStream stream = e.getResponseBody();
			stream.write(entry.buffer, 0, entry.buffer.length);
			stream.close();
			e.close();
		}
		LOGGER.info("request {} get {}", e.getRequestURI().getRawPath(), entry.statusCode);
	}
	
	private Entry loadEntry(String rpath)
	{
		Entry etr;
		String path = resourceRoot + rpath;
		URL fileUrl = rootClazz.getResource(path);
		
		if (fileUrl == null) {
			etr = new Entry();
			etr.statusCode = 404;
			LOGGER.info("resource {} not found", path);
			return etr;
		} else {
			try {
				InputStream in = rootClazz.getResourceAsStream(path);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				while (true) {
					int len = in.read(buffer, 0, buffer.length);
					if (len > 0) {
						out.write(buffer, 0, len);
					} else {
						break;
					}
				}
				etr = new Entry();
				etr.statusCode = 200;
				etr.buffer = out.toByteArray();
				LOGGER.info("load {} into cache", rpath);
				return etr;
			} catch (IOException ex) {
				LOGGER.error("load stream failed {}:" + rpath, ex);
				etr = new Entry();
				etr.statusCode = 500;
				return etr;
			}
		}
	}
}
