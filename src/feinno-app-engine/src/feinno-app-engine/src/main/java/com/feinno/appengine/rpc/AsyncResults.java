/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.rpc;

/**
 * 带错误信息的异步回调包装类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AsyncResults<R>
{
	private Exception error;
	private R value;

	public AsyncResults(Exception error, R value)
	{
		this.error = error;
		this.value = value;
	}

	public Exception error()
	{
		return error;
	}

	public R value()
	{
		return value;
	}

	public void setError(Exception error)
	{
		this.error = error;
	}

	public void setValue(R value)
	{
		this.value = value;
	}
}
