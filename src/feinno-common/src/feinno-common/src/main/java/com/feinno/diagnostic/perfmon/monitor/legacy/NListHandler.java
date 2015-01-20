package com.feinno.diagnostic.perfmon.monitor.legacy;

import java.io.IOException;
import java.nio.charset.Charset;

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
import com.feinno.diagnostic.perfmon.CounterCategoryInstance;
import com.feinno.diagnostic.perfmon.PerformanceCounterFactory;
 
public class NListHandler  implements HttpAsyncRequestHandler<HttpRequest>
{
	@Override
    public void handle(HttpRequest data, HttpAsyncExchange httpExchange, HttpContext context) throws HttpException, IOException
    {
		CounterCategory category = PerformanceCounterFactory.getCategory("apps");
		HttpResponse response = httpExchange.getResponse();
		if (category == null) {
			response.setStatusCode(200);
			httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
			return;
		}

		StringBuilder str = new StringBuilder();
		for (CounterCategoryInstance i : category.getAllInstances()) {
			str.append(i.getInstanceName());
			str.append(",");
		}
		byte[] body = str.toString().getBytes(Charset.forName("UTF-8"));
		response.setStatusCode(200);
		ByteArrayEntity entity = new ByteArrayEntity(body);
		response.setEntity(entity);
		httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
    }

	@Override
    public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest request, HttpContext context) throws HttpException, IOException
    {
		return new BasicAsyncRequestConsumer();
    }

}
