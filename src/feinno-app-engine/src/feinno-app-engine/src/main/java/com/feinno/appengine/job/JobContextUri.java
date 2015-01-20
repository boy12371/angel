/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job;

import com.feinno.appengine.ContextUri;

/**
 * Job的上下文Uri
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class JobContextUri extends ContextUri
{
	private int index;
	
	public JobContextUri(int index)
	{
		this.index = index;
	}

	@Override
	public String getProtocol()
	{
		return "job";
	}

	@Override
	public String getValue()
	{
		return Integer.toString(index);
	}

	@Override
	protected void setValue(String value)
	{
		index = Integer.parseInt(value);
	}

	@Override
	public String getParameter(String p)
	{
		return null;
	}

	@Override
	protected void setParameter(String p, String value)
	{
		throw new UnsupportedOperationException("NotSupport");
	}

	@Override
	public String toString()
	{
		return "job:" + index;
	}

	@Override
	public int getRouteHash()
	{
		throw new UnsupportedOperationException("HolyShit!!! never should call this");
	}
	
	public int getIndex()
	{
		return index;
	}
}
