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

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.BasicAsyncResponseProducer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HttpContext;

import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReport;
import com.feinno.diagnostic.observation.ObserverReportMode;
import com.feinno.diagnostic.observation.ObserverInspector.ReportCallback;
import com.feinno.util.TimeSpan;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class NSubscribeHandler implements HttpAsyncRequestHandler<HttpRequest>
{
	@Override
	public void handle(HttpRequest data, HttpAsyncExchange httpExchange, HttpContext context) throws HttpException, IOException
	{
		HashMap<String, String> args = UrlParameterHelper.getArgs(data.getRequestLine().getUri());
		String category = args.get("category");
		String option = args.get("instance");
		String interval = args.get("invterval");
		String cookie = args.get("cookie");
		// request

		Observable ob = ObserverManager.getObserverItem(category);
		ObserverReportMode mode = ObserverReportMode.valueOf(option.toUpperCase());
		HttpResponse response = httpExchange.getResponse();
		if (ob == null)
		{
			response.setStatusCode(404);
			httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
			return;
		}

		final PullManager manager = PullManager.getInstance(cookie, true);

		TimeSpan span = new TimeSpan(Integer.parseInt(interval) * TimeSpan.SECOND_MILLIS);
		ObserverManager.addInspector(ob, mode, span, new ReportCallback()
		{
			@Override
			public boolean handle(ObserverReport report)
			{
				if (!manager.isActive())
				{
					return false;
				}
				else
				{
					manager.enqueueReport(report);
					return true;
				}
			}
		});
		response.setStatusCode(200);
		httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
	}

	@Override
	public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest request, HttpContext context) throws HttpException, IOException
	{
		return new BasicAsyncRequestConsumer();
	}

}
