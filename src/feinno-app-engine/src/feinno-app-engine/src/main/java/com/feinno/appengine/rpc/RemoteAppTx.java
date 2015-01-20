/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-15
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.appengine.rpc;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.AppTxWithContext;

/**
 * 
 * RemoteAppBean的调用事务对象，每次RemoteAppBean的调用都会产生一个新的RemoteAppBeanTx
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class RemoteAppTx<A, R, C extends AppContext> extends AppTxWithContext<C>
{
	protected A args;
	protected String contextUri;
	protected byte[] contextData;
	
	/**
	 * 
	 * 得到请求参数
	 * @return
	 */
	public A args()
	{
		return args;
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
	
	/**
	 * 
	 * 正确返回
	 */
	public abstract void end();

	/**
	 * 
	 * 正确返回
	 * @param results
	 */
	public abstract void end(R results);
	/**
	 * 
	 * 错误返回
	 * @param error
	 */
	public abstract void end(Exception error);
}
