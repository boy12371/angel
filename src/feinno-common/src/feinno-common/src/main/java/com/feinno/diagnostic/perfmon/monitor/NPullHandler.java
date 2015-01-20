/*
 * FAE, Feinno App Engine
 * 
 * Create by gaolei 2011-9-22
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.BasicAsyncResponseProducer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HttpContext;

import com.feinno.diagnostic.observation.ObserverReport;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class NPullHandler implements HttpAsyncRequestHandler<HttpRequest>
{
	@Override
	public void handle(HttpRequest request, HttpAsyncExchange httpExchange, HttpContext context) throws HttpException, IOException
	{
		HashMap<String, String> args = UrlParameterHelper.getArgs(request.getRequestLine().getUri());

		String cookie = args.get("cookie");
		PullManager mgr = PullManager.getInstance(cookie, false);
		HttpResponse response = httpExchange.getResponse();
		if (mgr == null)
		{
			response.setStatusCode(404);
			httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
			return;
		}
		int index = 0;
		List<ObserverReport> list = mgr.pull();
		StringBuilder str = new StringBuilder();
		str.append("{reports:[");
		for (ObserverReport r : list)
		{
			str.append(r.encodeToJson());
			if (++index < list.size())
				str.append(",");
		}
		str.append("]}");
		byte[] buffer = str.toString().getBytes();
		response.setStatusCode(200);
		ByteArrayEntity entity = new ByteArrayEntity(buffer);
		response.setEntity(entity);
		httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
	}

	@Override
	public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest request, HttpContext context) throws HttpException, IOException
	{
		return new BasicAsyncRequestConsumer();
	}
}
