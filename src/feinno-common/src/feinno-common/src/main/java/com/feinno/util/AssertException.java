/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-9-1
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.util;

/**
 * 断言失败异常
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AssertException extends RuntimeException
{
	private static final long serialVersionUID = -7224224972517328627L;
	
	public AssertException(String message, String condition)
	{
		super("Assert " + condition + ":" + message);
				
	}
}
