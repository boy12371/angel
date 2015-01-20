/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-22
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HttpContext;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */ 
public class NUnsubscribeCounter implements HttpAsyncRequestHandler<HttpRequest>
{

	@Override
    public void handle(HttpRequest data, HttpAsyncExchange httpExchange, HttpContext context) throws HttpException, IOException
    {
	 
	    
    }

	@Override
    public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest request, HttpContext context) throws HttpException, IOException
    {
	    return new BasicAsyncRequestConsumer();
    }
 
}
