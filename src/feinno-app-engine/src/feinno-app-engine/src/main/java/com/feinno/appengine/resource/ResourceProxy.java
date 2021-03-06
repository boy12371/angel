/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft 2011-2-1
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.resource;

import java.util.HashMap;
import java.util.Map;

import com.feinno.appengine.ContextUri;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ResourceProxy
{
	private Map<String, ResourceLocator> locators;
	
	public ResourceProxy()
	{
		locators = new HashMap<String, ResourceLocator>();
	}
	
	public void addLocator(String uriProtocol, ResourceLocator locator)
	{
		locators.put(uriProtocol, locator);
	}
	
	protected Resource locateResource(ContextUri uri)
	{
		String protocol = uri.getProtocol();
		ResourceLocator locator = locators.get(protocol);
		return locator.locate(uri);
	}
	
	protected Resource locateResource(Integer a)
	{
		ResourceLocator locator = locators.get("id");
		return locator.locate(a);
	}
}
