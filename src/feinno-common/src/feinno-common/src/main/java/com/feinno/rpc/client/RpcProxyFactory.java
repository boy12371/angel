/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-11-25
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.feinno.rpc.channel.RpcChannelSettings;
import com.feinno.rpc.channel.RpcChannelSupportFlag;
import com.feinno.rpc.channel.RpcClientChannel;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;

/**
 * 客户端使用Rpc的工厂类, 全静态方法
 *
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcProxyFactory
{
	private static Map<RpcMethodStubKey, RpcMethodStub> stubs = new ConcurrentHashMap<RpcMethodStubKey, RpcMethodStub>();
	
//	public static void transfer(RpcConnection conn, RpcServerTransaction serverTx)
//	{
//		RpcClientTransaction clientTx = conn.createTransaction();
//		RpcRequestHeader header = clientTx.getRequest().getHeader();
//		
//		d
//		clientTx.getRequest().setBody(tx.getRequest().getBody());
//				
//	}
	
	/**
	 * 
	 * 获取一个用于调用服务器端的代理类
	 * @param ep
	 * @param service
	 * @param method
	 * @return
	 */
	public static RpcMethodStub getMethodStub(RpcEndpoint ep, String service, String method)
	{
		// 
		// 与ep中的模式存在依赖性
		// 生存周期,
		try {
			RpcMethodStubKey key = new RpcMethodStubKey(ep, service, method);
			RpcMethodStub stub = stubs.get(key);
			if (stub == null) {
				RpcClientChannel channel = ep.getClientChannel();
				RpcClientTransactionHandler handler = null;
				RpcChannelSettings settings = channel.getSettings();
				if (settings.getSupportFlags().has(RpcChannelSupportFlag.CONNECTION)) {
					if (ep.getParameter("NLB") != null) {
						handler = new RpcClientTransactionHandlerNLB(ep, service, method);					
					} else {
						handler = new RpcClientTransactionHandlerDirect(ep, service, method, null);
					}
				} else {
					handler = new RpcClientTransactionHandlerShort(ep, service, method);
				}
				stub = new RpcMethodStub(handler);
				stubs.put(key, stub);
			}
			return stub;
		} catch (Exception ex) {
			String msg = "GetMethodStub error " + ep.toString() + "/" + service + "." + method;
			throw new IllegalArgumentException(msg, ex);
		}
	}

	/**
	 * 
	 * 获取一个用于调用的透明代理
	 * 
	 * @param ep
	 * @param I 透明代理的声明类
	 * @return
	 */
	public static <I> I getService(RpcEndpoint ep, Class<I> intf)
	{	
		RpcService sa = intf.getAnnotation(RpcService.class);
		if (sa == null) {
			throw new IllegalArgumentException("@RpcService not found in:" + intf);
		}
		String serviceName = sa.value();
		Map<String, RpcMethodStub> stubs = new HashMap<String, RpcMethodStub>(); 
		for (Method method: intf.getMethods()) {
			RpcMethod ma = method.getAnnotation(RpcMethod.class);
			String methodName = ma.value();
			RpcMethodStub stub = getMethodStub(ep, serviceName, methodName);
			if (!method.getReturnType().equals(Void.class) && method.getReturnType() != void.class) {
				stub.setResultsClass(method.getReturnType());
			}
			stubs.put(method.getName(), stub);
		}
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InvocationHandler handler = new RpcInvocationHandler(stubs); 
		return (I)Proxy.newProxyInstance(cl, new Class<?>[] {intf}, handler);
	}

	/**
	 * 
	 * 仅用与兼容老代码
	 * @param ep
	 * @param service
	 * @return
	 */
	@Deprecated
	public static RpcProxy getProxy(RpcEndpoint ep, String service)
	{
		return new RpcProxy(ep, service);
	}
}
