/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft 2011-2-1
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.context;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.ContextUri;

/**
 * 会话Context, 按照唯一生成的SessionId进行缓存
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class SessionContext extends AppContext
{
	private SessionContextUri uri;
	/*
	 * @see com.feinno.appengine.AppContext#decode(java.lang.String, byte[])
	 */
	/**
	 * {在这里补充功能说明}
	 * @param str
	 * @param buffer
	 */
	@Override
	public void decode(String str, byte[] buffer)
	{
		uri = ContextUri.parse(SessionContextUri.class, str);
	}

	/*
	 * @see com.feinno.appengine.AppContext#getUri()
	 */
	/**
	 * {在这里补充功能说明}
	 * @return
	 */
	@Override
	public ContextUri getContextUri()
	{
		return uri;
	}

	/*
	 * @see com.feinno.appengine.AppContext#getSiteName()
	 */
	/**
	 * {在这里补充功能说明}
	 * @return
	 */
	@Override
	public String getSiteName()
	{
		return null;
	}

	/*
	 * @see com.feinno.appengine.AppContext#getNamedValue(int)
	 */
	/**
	 * {在这里补充功能说明}
	 * @param id
	 * @return
	 */
	@Override
	public Object getNamedValue(String id)
	{
		throw new UnsupportedOperationException("没实现呢");
	}

	/*
	 * @see com.feinno.appengine.AppContext#putNamedValue(int, java.lang.Object)
	 */
	/**
	 * {在这里补充功能说明}
	 * @param id
	 * @param value
	 */
	@Override
	public void putNamedValue(String id, Object value)
	{
		throw new UnsupportedOperationException("没实现呢");
	}

	@Override
	public byte[] encode(int demands)
	{
		throw new UnsupportedOperationException("没实现呢");
	}
}
