package com.feinno.diagnostic.perfmon.monitor.legacy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import com.feinno.diagnostic.perfmon.CounterCategory;
import com.feinno.diagnostic.perfmon.PerformanceCounterFactory;
import com.feinno.diagnostic.perfmon.monitor.ParameterFilter;
import com.feinno.diagnostic.perfmon.spi.TransactionCounter;
import com.feinno.diagnostic.perfmon.spi.TransactionCounterSnapshot;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class ShowTypesHandler implements HttpHandler
{
	@Override
	public void handle(HttpExchange t) throws IOException
	{
		Map<String, Object> params = (Map<String, Object>) t.getAttribute(ParameterFilter.PARAMETERS);
		String type = (String)params.get("type");
		
		CounterCategory category = PerformanceCounterFactory.getCategory("apps");
		AppBeanPerformanceCounters counter = null;
		if (category != null) {
			counter = (AppBeanPerformanceCounters)category.getInstanceRefer(type); 
		}
		
		if (counter == null) {
			t.sendResponseHeaders(200, 0);
			t.close();
			return;
		}

		TransactionCounter tx = (TransactionCounter)counter.getTx();
		TransactionCounterSnapshot snapshot = (TransactionCounterSnapshot) tx.getObserverSnapshot();
		double val = 0d;
		StringBuilder str = new StringBuilder();
		str.append("{");
		str.append("\"concurrent\":");
		str.append(snapshot.getFired() - snapshot.getTotal());
		str.append(",\"invocations\":");
		str.append(snapshot.getFired());
		str.append(",\"minElapsed\":");
		try{
			val = (double)snapshot.getTotalCost() / snapshot.getTotal() / 1E6;
			
		}catch(Exception ex){
			val = 0d;
		}
		str.append("NaN".equals(String.valueOf(val))?0d:val);
		str.append(",\"maxElapsed\":");
		str.append("NaN".equals(String.valueOf(val))?0d:val);
		str.append(",\"lastElapsed\":");
		str.append("NaN".equals(String.valueOf(val))?0d:val);
		str.append(",\"processed\":");
		str.append(snapshot.getTotal());
		str.append(",\"execptions\":");
		str.append(snapshot.getFailed());
		str.append(",\"lastErrMsg\":\"\"");
		str.append("}");
		byte[] body = str.toString().getBytes(Charset.forName("UTF-8"));
		t.sendResponseHeaders(200, body.length);
		t.getResponseBody().write(body);
		t.getResponseBody().close();
		t.close();
	}
}
