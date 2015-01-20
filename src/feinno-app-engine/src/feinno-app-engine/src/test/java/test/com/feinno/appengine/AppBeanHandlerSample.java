/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.appengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBeanHandler;
import com.feinno.appengine.AppBeanHandlerMode;
import com.feinno.appengine.AppTx;
import com.feinno.util.Action;
/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppBeanHandlerSample implements AppBeanHandler
{	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppBeanHandlerSample.class);
	boolean raiseError = false;

	/*
	 * @see com.feinno.appengine.AppTxHandler#processTx(com.feinno.appengine.AppTx, com.feinno.util.Action)
	 */
	/**
	 * {在这里补充功能说明}
	 * @param tx
	 * @param callback
	 */
	@Override
	public void processTx(AppTx tx, Action<Exception> callback)
	{
		LOGGER.info("Handler enter");
		if (raiseError) {
			callback.run(new Exception("Handler error"));
		} else {
			callback.run(null);
		}
	}

	/*
	 * @see com.feinno.appengine.AppBeanHandler#getMode()
	 */
	/**
	 * {在这里补充功能说明}
	 * @return
	 */
	@Override
	public AppBeanHandlerMode getMode()
	{
		return AppBeanHandlerMode.RUN_ALWAYS;
	}

	/*
	 * @see com.feinno.appengine.AppBeanHandler#setParams(java.lang.String[])
	 */
	/**
	 * {在这里补充功能说明}
	 * @param params
	 */
	@Override
	public void setParams(String[] params)
	{
		if (params != null && params.length > 0) { 
			if (params[0].equals("error"))
				raiseError = true;
		}
	}
}
