/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.context;

import java.util.Hashtable;
import java.util.Map.Entry;

import com.feinno.appengine.ContextUri;
import com.feinno.util.ObjectHelper;
/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class SessionContextUri extends ContextUri
{
	private String sessId = null;
	private Hashtable<String, String> parameters = new Hashtable<String, String>();
	/*
	 * @see com.feinno.appengine.ContextUri#getProtocol()
	 */
	/**
	 * {在这里补充功能说明}
	 * @return
	 */
	@Override
	public String getProtocol()
	{
		return "sess";
	}

	/*
	 * @see com.feinno.appengine.ContextUri#getValue()
	 */
	/**
	 * {在这里补充功能说明}
	 * @return
	 */
	@Override
	public String getValue()
	{
		return sessId;
	}

	/*
	 * @see com.feinno.appengine.ContextUri#setValue(java.lang.String)
	 */
	/**
	 * {在这里补充功能说明}
	 * @param value
	 */
	@Override
	protected void setValue(String value)
	{
		sessId = value;
	}

	/*
	 * @see com.feinno.appengine.ContextUri#getParameter(java.lang.String)
	 */
	/**
	 * {在这里补充功能说明}
	 * @param p
	 * @return
	 */
	@Override
	public String getParameter(String p)
	{
		return parameters.get(p); 
	}

	/*
	 * @see com.feinno.appengine.ContextUri#setParameter(java.lang.String, java.lang.String)
	 */
	/**
	 * {在这里补充功能说明}
	 * @param field
	 * @param value
	 */
	@Override
	protected void setParameter(String p, String value)
	{
		parameters.put(p, value);
	}

	/*
	 * @see com.feinno.appengine.ContextUri#toString()
	 */
	/**
	 * {在这里补充功能说明}
	 * @return
	 */
	@Override
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		s.append("sess:");
		s.append(sessId);
		for (Entry<String, String> e: parameters.entrySet()) {
			s.append(';');
			s.append(e.getKey());
			s.append('=');
			s.append(e.getValue());
		}
		return s.toString();
	}

	@Override
	public int getRouteHash()
	{
		return ObjectHelper.compatibleGetHashCode(sessId); 
	}
}