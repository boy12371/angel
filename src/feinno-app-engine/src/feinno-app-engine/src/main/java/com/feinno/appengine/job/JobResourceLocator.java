/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-6-9
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job;

import com.feinno.appengine.ContextUri;
import com.feinno.appengine.resource.Resource;
import com.feinno.appengine.resource.ResourceGroup;
import com.feinno.appengine.resource.ResourceLocator;

/**
 * 定位Job的资源
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class JobResourceLocator implements ResourceLocator
{
	private ResourceGroup<? extends Resource> group; 
	
	@Override
	public void setParams(ResourceGroup<? extends Resource> group, String[] params) throws Exception
	{
		this.group = group;
	}

	@Override
	public Resource locate(ContextUri uri)
	{
		if (uri instanceof JobContextUri) {
			int index = ((JobContextUri)uri).getIndex();
			return group.getResource(index);  
		} else {
			throw new IllegalArgumentException("没实现呢");
		}
	}

	@Override
	public Resource locate(Integer a) {
		// TODO Auto-generated method stub
		return group.getResource(a.intValue()); 
	}
}
