/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.ContextUri;
/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class JobContext extends AppContext
{
	private JobContextUri uri;
	
	public JobContext(int index)
	{
		uri = new JobContextUri(index);
	}

	/*
	 * @see com.feinno.appengine.AppContext#getContextUri()
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
	 * @see com.feinno.appengine.AppContext#decode(java.lang.String, byte[])
	 */
	/**
	 * {在这里补充功能说明}
	 * @param uri
	 * @param datas
	 * @throws Exception
	 */
	@Override
	public void decode(String uri, byte[] datas) throws Exception
	{
		throw new UnsupportedOperationException("NotSupported");
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
		throw new UnsupportedOperationException("NotSupported");
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
		throw new UnsupportedOperationException("NotSupported");
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
		throw new UnsupportedOperationException("NotSupported");
	}

	@Override
	public byte[] encode(int demands)
	{
		throw new UnsupportedOperationException("NotSupported");
	}
}
