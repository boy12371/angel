/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-9-1
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.util;

/**
 * Assert辅助类
 * @see AssertException
 * @author 高磊 gaolei@feinno.com
 */
public class Assert
{
	public static void isTrue(boolean condition)
	{
		isTrue("", condition);
	}
	
	public static void isTrue(String message, boolean condition)
	{
		if (!condition) {
			throw new AssertException(message, "isTrue");
		}
	}

}
