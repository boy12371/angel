/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-29
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.appengine.message;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.AppTx;
import com.feinno.appengine.annotation.AppBeanBaseType;
import com.feinno.util.Action;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@AppBeanBaseType
public abstract class MessageAppBean<E, C> extends AppBean
{
	@Override
	public void processTx(AppTx tx, Action<Exception> callback)
	{
		throw new UnsupportedOperationException("没实现呢");
	}
	
	public abstract void process(MessageAppTx<E> tx);
}
