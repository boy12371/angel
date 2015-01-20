/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-12-20
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.resource;

/**
 * 
 * 一种假的资源, 用于在某些必须要的情况
 * 
 * @author 杜宇 duyu@feinno.com
 */
public class FakeResource implements Resource
{
	public ResourceType type()
	{
		return ResourceType.NONE;
	}

	public String name()
	{
		return "none";
	}

	public int index()
	{
		return -1;
	}
}
