/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft 2011-2-1
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.resource;

import com.feinno.appengine.ContextUri;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public interface ResourceLocator
{
	void setParams(ResourceGroup<? extends Resource> group, String[] params) throws Exception;
	Resource locate(ContextUri uri);
	Resource locate(Integer a);
}
