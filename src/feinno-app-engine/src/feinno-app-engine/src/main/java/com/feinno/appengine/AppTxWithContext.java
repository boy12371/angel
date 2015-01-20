/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine;


/**
 * 带有Context的App事务相关类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class AppTxWithContext<C extends AppContext> extends AppTx
{
	private C context = null;

	public C context()
	{
		return context;
	}
	
	protected void setContext(C context)
	{
		this.context = context;
	}
	
	public C getContext()
	{
		return context;
	}
	
	/**
	 * 
	 * 返回ContextUri的值
	 * @return
	 */
	protected abstract String extractContextUri();
	
	/**
	 * 
	 * 返回ContextData的数据
	 * @return
	 */
	protected abstract byte[] extractContextData();
}
