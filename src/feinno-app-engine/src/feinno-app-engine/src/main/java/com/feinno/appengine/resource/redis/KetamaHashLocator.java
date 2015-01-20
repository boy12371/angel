/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-6-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.resource.redis;

import java.util.ArrayList;

import com.feinno.appengine.ContextUri;
import com.feinno.appengine.resource.Resource;
import com.feinno.appengine.resource.ResourceGroup;
import com.feinno.appengine.resource.ResourceLocator;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class KetamaHashLocator implements ResourceLocator
{
	ResourceGroup<?> group;
	KetamaNodeLocator<Integer> locator;
	
	@Override
	public void setParams(ResourceGroup<? extends Resource> group, String[] params) throws Exception
	{
		this.group = group;
		ArrayList<Integer> array = new ArrayList<Integer>();
		for (Resource r: group.resources()) {
			array.add(r.index());
		}
		locator = new KetamaNodeLocator<Integer>(array, KetamaNodeLocator.VIRTUAL_NODE_COUNT);
	}

	@Override
	public Resource locate(ContextUri uri)
	{
		int hashCode = uri.getRouteHash();
		int o = locator.getNodeForKey(hashCode);
		return group.getResource(o);
	}

	@Override
	public Resource locate(Integer a) {
		// TODO Auto-generated method stub
		return group.getResource(a.intValue());
	}
}
