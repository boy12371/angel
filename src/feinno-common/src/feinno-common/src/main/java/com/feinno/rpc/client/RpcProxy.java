/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-11-23
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.rpc.client;

import java.util.Map;

import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.RpcException;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.util.Action;
import com.feinno.util.EventHandler;

/**
 * 
 * @deprecated 已过期, 仅用于兼容老版本代码
 * @see RpcMethodStub 
 * 
 * @auther gaolei@feinno.com
 */
@Deprecated
public class RpcProxy
{
	private String service;
	private RpcEndpoint ep;

	public RpcProxy(RpcEndpoint ep, String service)
	{
		this.ep = ep;
		this.service = service;
		// timeout = TIMEOUT;
	}

	/**
	 * 
	 * 同步调用接口
	 * 
	 * @param <T>
	 * @param method
	 * @param args
	 * @param returnType
	 * @return
	 */
	public <T> T invoke(String method, Object args, Class<T> returnType)
	{
		return invoke(method, args, null, returnType);
	}
	
	/**
	 * 
	 * 同步调用接口
	 * 
	 * @param <T>
	 * @param method
	 * @param args
	 * @param returnType
	 * @param timeout
	 * @return
	 */
	public <T> T invoke(String method, Object args, Class<T> returnType, int timeout)
	{
		return invoke(method, args, null, returnType, timeout);
	}

	/**
	 * 
	 * 同步调用接口
	 * 
	 * @param <T>
	 * @param method
	 * @param args
	 * @param contextUri
	 * @param returnType
	 * @return
	 * @throws RpcException
	 */
	public <T> T invoke(String method, Object args, String contextUri, Class<T> returnType)
	{
		try {
			RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, service, method);
			RpcFuture future = stub.invoke(args, contextUri);
			return future.syncGet(returnType);
		} catch (RpcException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * 
	 * 同步调用接口
	 * 
	 * @param <T>
	 * @param method
	 * @param args
	 * @param contextUri
	 * @param returnType
	 * @param timeout
	 * @return
	 * @throws RpcException
	 */
	public <T> T invoke(String method, Object args, String contextUri, Class<T> returnType, int timeout)
	{
		try {
			RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, service, method);
			RpcFuture future = stub.invoke(args, contextUri, timeout);
			return future.syncGet(returnType);
		} catch (RpcException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 
	 * 异步调用接口，简单
	 * 
	 * @param method
	 * @param args
	 * @param callback
	 */
	public void invoke(String method, Object args, final Action<RpcResults> callback)
	{
		invoke(method, args, null, null, callback);
	}

	/**
	 * 
	 * 异步调用接口，简单
	 * 
	 * @param method
	 * @param args
	 * @param callback
	 * @param timeout
	 */
	public void invoke(String method, Object args, final Action<RpcResults> callback, int timeout)
	{
		invoke(method, args, null, null, callback, timeout);
	}

	/**
	 * 
	 * 异步调用接口，包含ContextUri
	 * 
	 * @param <E>
	 * @param method
	 * @param args
	 * @param callback
	 */
	public void invoke(String method, Object args, String contextUri, final Action<RpcResults> callback)
	{
		invoke(method, args, contextUri, null, callback);
	}

	/**
	 * 
	 * 异步调用接口，包含ContextUri
	 * 
	 * @param <E>
	 * @param method
	 * @param args
	 * @param callback
	 * @param timeout
	 */
	public void invoke(String method, Object args, String contextUri, final Action<RpcResults> callback, int timeout)
	{
		invoke(method, args, contextUri, null, callback, timeout);
	}
	
	/**
	 * 
	 * 异步调用接口，包含ContextUri与Extensions
	 * 
	 * @param method
	 * @param args
	 * @param contextUri
	 * @param extensions
	 * @param callback
	 */
	public void invoke(String method, Object args, String contextUri, Map<Integer, Object> extensions, final Action<RpcResults> callback)
	{
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, service, method);
		RpcFuture future = stub.invoke(args, contextUri);
		future.addListener(new EventHandler<RpcResults>() {
			@Override
			public void run(Object sender, RpcResults e)
			{
				callback.run(e);
			}
		});
	}
	
	/**
	 * 
	 * 异步调用接口，包含ContextUri与Extensions
	 * 
	 * @param method
	 * @param args
	 * @param contextUri
	 * @param extensions
	 * @param callback
	 * @param timeout
	 */
	public void invoke(String method, Object args, String contextUri, Map<Integer, Object> extensions, final Action<RpcResults> callback, int timeout)
	{
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, service, method);
		RpcFuture future = stub.invoke(args, contextUri, timeout);
		future.addListener(new EventHandler<RpcResults>() {
			@Override
			public void run(Object sender, RpcResults e)
			{
				callback.run(e);
			}
		});
	}
}

// /**
// *
// * 创建一个Transaction，可用于Gateway或更灵活的领域
// * @param method
// * @param args
// */
// public RpcClientTransaction createTransaction(String method, Object args)
// {
// // RpcRequest request = new RpcRequest();
// // request.setFromComputer(ServiceEnviornment.getComputerName());
// // request.setFromService(ServiceEnviornment.getServiceName());
// // request.setToService(serviceName);
// // request.setToMethod(method);
// //
// // //l
// // // 当请求对象为空时不设置消息体
// // if (args != null) {
// // request.setBody(new RpcBody(args, false));
// // }
// //
// // return conn.newTransaction(request);
// throw new UnsupportedOperationException("没实现呢");
// }