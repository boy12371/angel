/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-2-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.http;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;

import com.feinno.appengine.AppBeanWithContext;
import com.feinno.appengine.AppContext;
import com.feinno.appengine.AppTx;
import com.feinno.appengine.annotation.AppBeanBaseType;
import com.feinno.util.Action;
/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@AppBeanBaseType
public abstract class HttpAppBean<C extends AppContext> extends AppBeanWithContext<C>
{
	/**
	 * {在这里补充功能说明}
	 * @param genericParamOrder
	 */
	protected HttpAppBean()
	{
		super(0);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void processTx(AppTx tx, Action<Exception> callback)
	{
		HttpAppTx<C> htx = (HttpAppTx<C>)tx;
		htx.setTerminateCallback(callback);
		try {
			process(htx);
		} catch (Exception e) {
			LOGGER.error("process failed", e);
			try {
				htx.getResponse().sendError(500, e.toString());
			} catch (IOException e1) {
				LOGGER.error("process error failed", e1);
			}
		}
	}	
	
	public abstract void process(HttpAppTx<C> tx) throws Exception;

	public void processRequest(HttpServletRequest request, HttpServletResponse response, Continuation c)
	{
		HttpAppTx<C> tx = new HttpAppTx<C>(request, response, c);
		try {
			LOGGER.info("loading context");
			super.loadContext(tx);
			LOGGER.info("context loaded");
			super.processHandlerChain(tx, null);
		} catch (Exception e) {
			LOGGER.error("process failed", e);
			try {
				response.sendError(500, e.toString());
				tx.end();
			} catch (IOException e1) {
				LOGGER.error("process error failed", e1);
			}
		}
	}
}