/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Sep 5, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */

public class AppBeanClassInfo
{
	private String type;
	private String version;
	private AppBeanBaseClassInfo baseClass;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public AppBeanBaseClassInfo getBaseClass()
	{
		return baseClass;
	}

	public void setBaseClass(AppBeanBaseClassInfo baseClass)
	{
		this.baseClass = baseClass;
	}
}
