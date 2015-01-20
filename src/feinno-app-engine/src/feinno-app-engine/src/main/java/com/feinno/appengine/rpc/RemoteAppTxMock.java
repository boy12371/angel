/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.rpc;

import org.slf4j.MarkerFactory;

import com.feinno.appengine.AppContext;

/**
 * 用于单元测试到RemoteAppBeanTx
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RemoteAppTxMock<A, R, C extends AppContext> extends RemoteAppTx<A, R, C>
{
	private R results;
	
	RemoteAppTxMock(A args, String contextUri, byte[] contextData)
	{
		this.args = args;
		this.contextUri = contextUri;
		this.contextData = contextData;
		if (contextUri != null) {
			setMarker(MarkerFactory.getMarker(contextUri));
		}
	}

	/**
	 * 
	 * 得到请求参数
	 * @return
	 */
	public A args()
	{
		return args;
	}
	
	public R results()
	{
		return results;
	}
	

	/**
	 * 
	 * 设置应答参数
	 * @param result
	 */
	public void end()
	{
		this.results = null;
		terminate();
	}
	
	/**
	 * 
	 * 设置应答参数
	 * @param result
	 */
	public void end(R results)
	{
		this.results = results;
		terminate();
	}
	
	/**
	 * 
	 * 设置应答参数, 错误
	 * @param error
	 */
	public void end(Exception error)
	{
		setError(error);
		terminate();
	}

	/*
	 * @see com.feinno.appengine.AppTxWithContext#extractContextUri()
	 */
	/**
	 * {在这里补充功能说明}
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
	 * @return
	 */
	@Override
	protected byte[] extractContextData()
	{
		return contextData;
	}
}
