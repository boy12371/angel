/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor;

import java.io.IOException;
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

import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReportColumn;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */ 
class NGetCategoryListHandler implements HttpAsyncRequestHandler<HttpRequest>
{
	@Override
    public void handle(HttpRequest data, HttpAsyncExchange httpExchange, HttpContext context) throws HttpException, IOException
    {
		StringBuilder body = new StringBuilder();
		body.append("{categorys:[");
		int f_index = 0;
		
		for (Observable ob : ObserverManager.getAllObserverItems()) {
			int s_index = 0;
			body.append("{category:{");
			String name = ob.getObserverName();
			body.append("name:");
			body.append("'" + name + "'");
			body.append(",instance:");
			body.append("'" + ob.getObserverUnits().size() + "'");
			body.append(",columns:[");
			List<ObserverReportColumn> cols = ob.getObserverColumns();
			for (ObserverReportColumn col : cols) {
				body.append("{counter:{name:");
				body.append("'" + col.getName() + "'");
				body.append("}}");
				if (++s_index < cols.size())
					body.append(",");
			}
			body.append("]}}");
			if (++f_index < ObserverManager.getAllObserverItems().size())
				body.append(",");
		}
		body.append("]}");

		byte[] buffer = body.toString().getBytes();
		// SET COOKIE
		HttpResponse response = httpExchange.getResponse();
		response.setHeader("Cookie", "213123213");
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
