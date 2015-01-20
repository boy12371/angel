/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@SuppressWarnings("restriction")
class GetCategoryListHandler implements HttpHandler
{
	@Override
	public void handle(HttpExchange t) throws IOException
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
		t.getResponseHeaders().add("Cookie", "213123213");
		t.sendResponseHeaders(200, buffer.length);
		OutputStream stream = t.getResponseBody();
		stream.write(buffer);
		stream.close();
		t.close();
	}
}
