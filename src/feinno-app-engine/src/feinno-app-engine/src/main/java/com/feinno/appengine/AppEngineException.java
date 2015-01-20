/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine;

/**
 * AppEngine的错误对象
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineException extends Exception
{
	public static final int FORMAT_FAILED = 400;
	public static final int SERVICE_NOT_FOUND = 404;
	public static final int SERVER_ERROR = 500;
	public static final int SERVER_BUSY = 503;
	
	private int code;
	
	public int code()
	{
		return code;
	}
	
	public AppEngineException(int code, String message, Throwable error)
	{
		super(message, error);
		this.code = code;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6094302211965878894L;
}
