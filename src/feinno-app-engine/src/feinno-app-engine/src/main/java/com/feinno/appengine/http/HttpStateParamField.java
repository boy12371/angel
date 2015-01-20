/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Nov 12, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.http;

import com.feinno.util.EnumInteger;

/**
 * HTTP状态参数
 * 
 * @author 高磊 gaolei@feinno.com
 */
public enum HttpStateParamField implements EnumInteger
{
	NONE(0),
	CONTEXT(1),
	QUERY_STRING(2),
	URL(3),
	COOKIE(4),
	HEADER(5),
	;
	private int value;
	private HttpStateParamField(int value)
	{
		this.value = value;
	}
	@Override
	public int intValue()
	{
		return value;
	}
}
