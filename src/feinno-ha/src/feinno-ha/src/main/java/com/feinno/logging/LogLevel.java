/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-11-23
 * 
 * Copyright (c) 2010  北京新媒传信科技有限公司
 */
package com.feinno.logging;

import com.feinno.util.EnumInteger;

/**
 * 记录日志的级别
 * 
 * @author gaolei
 */
public enum LogLevel implements EnumInteger
{
	ALL(0),
	TRACE(10000),
	DEBUG(20000),
	INFO(30000),
	WARN(50000),
	ERROR(80000),
	OFF(Integer.MAX_VALUE);
	
	private int value;
	
	private LogLevel(int value)
	{
		this.value = value;	
	}
	
	/**
	 * 为每一个枚举值设置对应的int值
	 */
	public int intValue(){
		return value;
	}
}
