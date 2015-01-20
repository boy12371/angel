/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-10-6
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.observation;

import com.feinno.util.EnumInteger;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public enum ObserverReportMode implements EnumInteger
{
	NONE(0),
	SUMMARY(1),
	ALL(2),
	;
	
	private int value;
	
	ObserverReportMode(int value)
	{
		this.value = value;
	}
	
	@Override
	public int intValue()
	{
		return value;
	}
}
