/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-3-2
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.client;

import com.feinno.rpc.channel.RpcClientTransaction;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.serialization.Codec;
import com.feinno.serialization.Serializer;

/**
 * 客户端包装类, 一次创建, 缓存使用
 * 针对返回类型可以进行缓冲, 降低一次字典索引的费用
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcMethodStub
{
	private RpcClientTransactionHandler handler;
	private Class<?> resultsClass;
	private Codec resultsCodec;
	
	public RpcMethodStub(RpcClientTransactionHandler handler)
	{
		this.handler = handler;
	}
	

	/**
	 * 
	 * 创建一个transaction, 用于需要额外设置其他参数的场合
	 * @param args
	 * @return
	 */
	public RpcClientTransaction createTransaction()
	{
		RpcClientTransaction tx = handler.createTransaction();
		return tx;
	}
	
	/**
	 * 
	 * 创建一个transaction, 用于需要额外设置extensions的场合
	 * @param args
	 * @return
	 */
	public RpcClientTransaction createTransaction(Object args)
	{
		RpcClientTransaction tx = handler.createTransaction();
		tx.setArgs(args);
		return tx;
	}
	
	/**
	 * 
	 * 创建一个transaction, 用于需要额外设置extensions的场合
	 * @param args
	 * @return
	 */
	public RpcClientTransaction createTransaction(byte[] args)
	{
		RpcClientTransaction tx = handler.createTransaction();
		tx.setArgs(args);
		return tx;
	}
	
	
	/**
	 * 
	 * 调用rpc方法, contextUri在后续的版本中将通过Extension实现, 不在单独存在
	 * @param args
	 * @param contextUri
	 * @param timeout
	 * @return
	 */
	@Deprecated
	public RpcFuture invoke(Object args, String contextUri, int timeout)
	{
		RpcClientTransaction tx = handler.createTransaction();
		tx.setArgs(args);
		tx.setTimeout(timeout);
		tx.setContextUri(contextUri);
		return tx.begin();
	}
	
	/**
	 * 
	 * 调用rpc方法, contextUri在后续的版本中将通过Extension实现, 不在单独存在
	 * @param args
	 * @param contextUri
	 * @return
	 */
	@Deprecated
	public RpcFuture invoke(Object args, String contextUri)
	{
		RpcClientTransaction tx = handler.createTransaction();
		tx.setArgs(args);
		tx.setContextUri(contextUri);
		return tx.begin();
	}
	
	/**
	 * 
	 * 调用rpc方法, 返回RpcFuture, 用户可选择同步或异步的后续处理
	 * @see RpcFuture
	 * @param args
	 * @param timeout
	 * @return
	 */
	public RpcFuture invoke(Object args, int timeout)
	{
		RpcClientTransaction tx = handler.createTransaction();
		tx.setArgs(args);
		tx.setTimeout(timeout);
		return tx.begin();
	}
	
	/**
	 * 
	 * 调用rpc方法, 返回RpcFuture, 用户可选择同步或异步的后续处理
	 * @see RpcFuture
	 * @param args
	 * @return
	 */
	public RpcFuture invoke(Object args)
	{
		RpcClientTransaction tx = handler.createTransaction();
		tx.setArgs(args);
		return tx.begin();
	}
	
	public void setResultsClass(Class<?> clazz)
	{
		this.resultsClass = clazz;
		this.resultsCodec = Serializer.getCodec(clazz);
	}
	
	public Class<?> getResultsClass()
	{
		return resultsClass;
	}
	
	public Codec getResultsCodec()
	{
		return resultsCodec;
	}
}
