/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 7, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import com.feinno.util.EnumInteger;

/**
 * 预定义线程上下文
 * 
 * @author 高磊 gaolei@feinno.com
 */
public enum ThreadContextName implements EnumInteger
{
	ROOT(0),
	LOGGING_MARKER(1),
	EXECUTOR(2),
	SESSION_ID(3),
	SESSION_TASK(4),
	JAVA_FLOW(5),
	;
	private int value;
	private ThreadContextName(int value) 
	{
		this.value = value;
	}
	@Override
	public int intValue()
	{
		return value;
	}
}
