/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Sep 13, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import com.feinno.util.EnumInteger;

/**
 * 用于保存在Session中存在的上下文
 * 
 * @author 高磊 gaolei@feinno.com
 */
public enum SessionContextName implements EnumInteger
{
	DATA(1),
	;
	private int value;
	private SessionContextName(int value) 
	{
		this.value = value;
	}
	@Override
	public int intValue()
	{
		return value;
	}
}
