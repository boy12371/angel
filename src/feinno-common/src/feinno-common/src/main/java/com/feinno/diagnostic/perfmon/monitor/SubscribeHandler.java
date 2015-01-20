/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-22
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObserverInspector.ReportCallback;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReport;
import com.feinno.diagnostic.observation.ObserverReportMode;
import com.feinno.util.TimeSpan;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@SuppressWarnings("restriction")
public class SubscribeHandler implements HttpHandler
{
	@Override
	public void handle(HttpExchange t) throws IOException
	{
		Map<String, Object> params = (Map<String, Object>) t.getAttribute(ParameterFilter.PARAMETERS);
		String category = (String)params.get("category");
		String option = (String)params.get("instance");
		String interval = (String)params.get("invterval");
		String cookie = (String)params.get("cookie"); // todo from request
		
		Observable ob = ObserverManager.getObserverItem(category);
		ObserverReportMode mode = ObserverReportMode.valueOf(option.toUpperCase());
		
		if (ob == null) {
			t.sendResponseHeaders(404, 0);
			return;
		}
		
		final PullManager manager = PullManager.getInstance(cookie, true);
		
		TimeSpan span = new TimeSpan(Integer.parseInt(interval) * TimeSpan.SECOND_MILLIS);
		ObserverManager.addInspector(ob, mode, span,  new ReportCallback() {
			@Override
			public boolean handle(ObserverReport report)
			{
				if (!manager.isActive()) {
					return false;
				} else {
					manager.enqueueReport(report);
					return true;
				}
			}
		});
		t.getRequestBody();
		byte[] buffer =  t.getResponseBody().toString().getBytes();
		t.sendResponseHeaders(200, 0);
        OutputStream stream = t.getResponseBody();
		stream.write(buffer);
		stream.close();
		t.close();
	 
	 
	}
}
