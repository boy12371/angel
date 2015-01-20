package com.feinno.http;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.BasicAsyncResponseProducer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HttpContext;

public class DefaultHandler implements HttpAsyncRequestHandler<HttpRequest>
{

	@Override
	public void handle(HttpRequest request, HttpAsyncExchange httpexchange, HttpContext context) throws HttpException, IOException
	{
		for (Header header : request.getAllHeaders())
		{
			System.out.println(header.getName() + "----" + header.getValue());// request.getFirstHeader("Accept")
		}
		HttpResponse response = httpexchange.getResponse();
		response.setStatusCode(HttpStatus.SC_OK);
		NStringEntity entity = new NStringEntity("<html><body><h1>default deal</h1></body></html>", ContentType.create("text/html", "UTF-8"));
		response.setEntity(entity);
		httpexchange.submitResponse(new BasicAsyncResponseProducer(response));
	}

	@Override
	public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest request, HttpContext context) throws HttpException, IOException
	{
		return new BasicAsyncRequestConsumer();
	}

}
