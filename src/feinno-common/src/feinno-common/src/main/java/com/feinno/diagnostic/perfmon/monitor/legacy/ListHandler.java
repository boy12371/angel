package com.feinno.diagnostic.perfmon.monitor.legacy;

import java.io.IOException;
import java.nio.charset.Charset;

import com.feinno.diagnostic.perfmon.CounterCategory;
import com.feinno.diagnostic.perfmon.CounterCategoryInstance;
import com.feinno.diagnostic.perfmon.PerformanceCounterFactory;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class ListHandler implements HttpHandler
{
	@Override
	public void handle(HttpExchange arg0) throws IOException
	{
		CounterCategory category = PerformanceCounterFactory.getCategory("apps");
		if (category == null) {
			arg0.sendResponseHeaders(200, 0);
			arg0.close();
			return;
		}

		StringBuilder str = new StringBuilder();
		for (CounterCategoryInstance i : category.getAllInstances()) {
			str.append(i.getInstanceName());
			str.append(",");
		}
		byte[] body = str.toString().getBytes(Charset.forName("UTF-8"));
		arg0.sendResponseHeaders(200, body.length);
		arg0.getResponseBody().write(body);
		arg0.getResponseBody().close();
		arg0.close();
	}
}
