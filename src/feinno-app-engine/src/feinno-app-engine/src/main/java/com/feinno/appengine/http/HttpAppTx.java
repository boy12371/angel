/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-15
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.appengine.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.slf4j.MarkerFactory;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.AppTxWithContext;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HttpAppTx<C extends AppContext> extends AppTxWithContext<C>
{
	private String contextUri;
	private byte[] contextData;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Continuation continuation;

	public HttpAppTx(HttpServletRequest request, HttpServletResponse response, Continuation continuation)
	{
		this.request = request;
		this.response = response;
		this.continuation = continuation;
		contextUri = request.getHeader("AppContextUri");
		contextData = base64decode(request.getHeader("AppContextData"));
		if (contextUri != null) {
			setMarker(MarkerFactory.getMarker(contextUri));
		}
	}

	/**
	 * {在这里补充功能说明}
	 * 
	 * @param header
	 * @return
	 */
	private byte[] base64decode(String header)
	{
		return Base64.decode(header); // 暂不用decodeFast
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public HttpServletResponse getResponse()
	{
		return response;
	}

	public void end()
	{
		continuation.complete();
		this.terminate();
	}
	
	/**
	 * 当有错误信息被set的时候，这个错误应该被返回到客户端
	 * 
	 * @param error
	 */
	@Override
	public void setError(Exception error) {
		super.setError(error);
		if (error != null) {
			continuation.complete();
		}
	}

	/*
	 * @see com.feinno.appengine.AppTxWithContext#extractContextUri()
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @return
	 */
	@Override
	protected String extractContextUri()
	{
		return contextUri;
	}

	/*
	 * @see com.feinno.appengine.AppTxWithContext#extractContextData()
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @return
	 */
	@Override
	protected byte[] extractContextData()
	{
		return contextData;
	}
}
