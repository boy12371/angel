/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Sep 5, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

import java.util.List;

import com.feinno.util.KeyValuePair;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */

public class AppBeanBaseClassInfo
{
	private String type;
	private List<KeyValuePair<String, String>> genericParams;
	
	public String getType()
	{
		return type;
	}

	public List<KeyValuePair<String, String>> getGenericParams()
	{
		return genericParams;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setGenericParams(List<KeyValuePair<String, String>> genericParams)
	{
		this.genericParams = genericParams;
	}
}