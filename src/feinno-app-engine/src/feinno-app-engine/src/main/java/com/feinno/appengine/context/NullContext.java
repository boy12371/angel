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
 * 无状态服务使用, 完全依赖输入参数k
 * 
 * @author 高磊 gaolei@feinno.com
 */
public final class NullContext extends AppContext
{
	public static final NullContext INSTANCE = new NullContext();
	public static final byte[] EMPTY_BUFFER = new byte[0];
	private NullContextUri uri = new NullContextUri();
	
	
	
	
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
		return null;
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
		throw new UnsupportedOperationException("NullContext not support named value");
	}

	@Override
	public byte[] encode(int demands)
	{
		return EMPTY_BUFFER;
	}
}
