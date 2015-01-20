/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-4
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel;

/**
 * 
 * rpc异常, 在且仅在rpc调用时的客户端抛出
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcException extends Exception
{
	private RpcClientTransaction tx;
	
	private StackTraceElement[] stack = new StackTraceElement[0];
	
	public RpcClientTransaction getTransaction()
	{
		return this.tx;
	}
	
	public RpcReturnCode getReturnCode()
	{
		return this.tx.getResponse().getReturnCode();
	}
	
	/**
	 * 
	 * 当Transaction结束后触发异常
	 * @param tx
	 * @param response
	 */
	public RpcException(RpcClientTransaction tx)
	{
		super(formatMessage(tx), getCause(tx));
		this.tx = tx;
	}
	/**
	 * 
	 * 当await超时时构造异常，此时没有tx信息
	 * @param tx
	 * @param response
	 */
	public RpcException(String msg)
	{
		super(msg);
	}

	private static String formatMessage(RpcClientTransaction tx)
	{
		return String.format("RpcException<%s> on %s ,Connection : %s", 
				tx.getResponse().getReturnCode().toString(), 
				tx.getMethodCache().getKey().getServiceUrl(),tx.getConnection());
	}
	
	private static Throwable getCause(RpcClientTransaction tx)
	{
		RpcBody body = tx.getResponse().getBody();
		if (body == null) {
			return null;
		} else {
			return body.getError();
		}
	}

	public  Throwable getInnerException()
	{
		RpcBody body = tx.getResponse().getBody();
		if (body == null) {
			return null;
		} else {
			return body.getError();
		}
	}
	
	public StackTraceElement[] getStackTrace(){
		return stack;
	}
	
	private static final long serialVersionUID = 5291218232582789402L;
}