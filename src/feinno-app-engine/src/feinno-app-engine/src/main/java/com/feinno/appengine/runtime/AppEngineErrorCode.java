/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jul 18, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.runtime;

import com.feinno.util.EnumInteger;

/**
 * 
 * FAE内部错误码
 * 
 * @author 高磊 gaolei@feinno.com
 */
public enum AppEngineErrorCode implements EnumInteger
{
	APPBEAN_NOT_FOUND(1001),
	// TODO ...
	;
	private int value;
	private AppEngineErrorCode(int value)
	{
		this.value = value;
	}
	@Override
	public int intValue()
	{
		return value;
	}
}
