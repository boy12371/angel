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
import java.util.List;
import java.util.Map;

import com.feinno.diagnostic.observation.ObserverReport;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@SuppressWarnings("restriction")
public class PullHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange t) throws IOException {
		Map<String, Object> params = (Map<String, Object>) t
				.getAttribute(ParameterFilter.PARAMETERS);
		String cookie = (String) params.get("cookie"); // TODO getFromT
		PullManager mgr = PullManager.getInstance(cookie, false);

		if (mgr == null) {
			t.sendResponseHeaders(404, 0);
			return;
		}
		int index = 0;
		List<ObserverReport> list = mgr.pull();
		StringBuilder str = new StringBuilder();
		str.append("{reports:[");
		for (ObserverReport r : list) {
			str.append(r.encodeToJson());
			if (++index < list.size())
				str.append(",");
		}
		str.append("]}");
		byte[] buffer = str.toString().getBytes();
		t.sendResponseHeaders(200, buffer.length);
		OutputStream stream = t.getResponseBody();
		stream.write(buffer);
		stream.close();
		t.close();
	}
}
