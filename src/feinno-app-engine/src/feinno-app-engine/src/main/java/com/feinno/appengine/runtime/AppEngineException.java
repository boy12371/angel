/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jul 18, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.runtime;

/**
 * 
 * 
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineException extends Exception
{
	private AppEngineErrorCode code;
	
	public AppEngineException(AppEngineErrorCode code, String reason)
	{
		this(code, reason, null);
	}
	
	public AppEngineException(AppEngineErrorCode code, String reason, Throwable cause)
	{
		super("<" + code + ">:" + reason, cause);
		this.code = code;
	}
	
	public AppEngineErrorCode getCode()
	{
		return code;
	}
	
	private static final long serialVersionUID = 8341740615880775921L;
}
