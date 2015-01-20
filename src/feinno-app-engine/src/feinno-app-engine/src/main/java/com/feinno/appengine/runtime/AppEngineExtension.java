/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-9
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.runtime;

import com.feinno.initialization.InitialException;

/**
 * AppEngine 扩展模块
 * 
 * @author 高磊 gaolei@feinno.com
 */
public interface AppEngineExtension
{
	void setup(AppEngineManager manager) throws InitialException;
}
