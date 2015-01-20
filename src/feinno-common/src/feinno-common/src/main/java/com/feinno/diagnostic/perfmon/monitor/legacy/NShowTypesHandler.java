package com.feinno.diagnostic.perfmon.monitor.legacy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

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

import com.feinno.diagnostic.perfmon.CounterCategory;
import com.feinno.diagnostic.perfmon.PerformanceCounterFactory;
import com.feinno.diagnostic.perfmon.monitor.UrlParameterHelper;
import com.feinno.diagnostic.perfmon.spi.TransactionCounter;
import com.feinno.diagnostic.perfmon.spi.TransactionCounterSnapshot;

public class NShowTypesHandler implements HttpAsyncRequestHandler<HttpRequest>
{
	@Override
	public void handle(HttpRequest request, HttpAsyncExchange httpExchange, HttpContext context) throws HttpException, IOException
	{
		HttpResponse response = httpExchange.getResponse();
		
		HashMap<String, String> args = UrlParameterHelper.getArgs(request.getRequestLine().getUri());

		String type = args.get("type");

		CounterCategory category = PerformanceCounterFactory.getCategory("apps");
		AppBeanPerformanceCounters counter = null;
		if (category != null)
		{
			counter = (AppBeanPerformanceCounters) category.getInstanceRefer(type);
		}

		if (counter == null)
		{
			response.setStatusCode(200);
			httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
			return;
		}

		TransactionCounter tx = (TransactionCounter) counter.getTx();
		TransactionCounterSnapshot snapshot = (TransactionCounterSnapshot) tx.getObserverSnapshot();
		double val = 0d;
		StringBuilder str = new StringBuilder();
		str.append("{");
		str.append("\"concurrent\":");
		str.append(snapshot.getFired() - snapshot.getTotal());
		str.append(",\"invocations\":");
		str.append(snapshot.getFired());
		str.append(",\"minElapsed\":");
		try
		{
			val = (double) snapshot.getTotalCost() / snapshot.getTotal() / 1E6;

		}
		catch (Exception ex)
		{
			val = 0d;
		}
		str.append("NaN".equals(String.valueOf(val)) ? 0d : val);
		str.append(",\"maxElapsed\":");
		str.append("NaN".equals(String.valueOf(val)) ? 0d : val);
		str.append(",\"lastElapsed\":");
		str.append("NaN".equals(String.valueOf(val)) ? 0d : val);
		str.append(",\"processed\":");
		str.append(snapshot.getTotal());
		str.append(",\"execptions\":");
		str.append(snapshot.getFailed());
		str.append(",\"lastErrMsg\":\"\"");
		str.append("}");
		byte[] body = str.toString().getBytes(Charset.forName("UTF-8"));
		response.setEntity(new ByteArrayEntity(body));
		response.setStatusCode(200);
		httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
	}

	@Override
	public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest request, HttpContext context) throws HttpException, IOException
	{
		return new BasicAsyncRequestConsumer();
	}

}
