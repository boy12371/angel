/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-11-26
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.rpc.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.rpc.channel.RpcResponse;
import com.feinno.rpc.channel.RpcReturnCode;
import com.feinno.rpc.channel.RpcServerMethodHandler;
import com.feinno.rpc.channel.RpcServerTransaction;
import com.feinno.threading.ExecutorBusyException;
import com.feinno.threading.ExecutorFactory;

/**
 * Rpc服务器端功能分发
 * 
 * @author gaolei@feinno.com
 */
public class RpcServiceDispather
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcServiceDispather.class);
	private static final Executor DEFAULT_EXECUTOR = ExecutorFactory.newFixedExecutor("rpc", 100, 100 * 100);  
	
	private Map<String, RpcServiceBase> services;
	private Executor executor; 
	
	/**
	 * 
	 * 设置线程池
	 * @param executor
	 */
	public void setExecutor(Executor executor)
	{
		this.executor = executor;
	}
	
	/**
	 * 
	 * 获取线程池
	 * @return
	 */
	public Executor getExecutor()
	{
		return executor;
	}
	
	/**
	 * 
	 * constructor 
	 */
	public RpcServiceDispather()
	{
		services = new HashMap<String, RpcServiceBase>();
		executor = DEFAULT_EXECUTOR;
	}
	
	/**
	 * 
	 * 添加一个服务, 允许两种对象<br>
	 * 1. 从RpcServiceBase派生<br>
	 * 2. 实现由@RpcService标记的interface<br>
	 * @param service
	 */
	public void addService(Object service)
	{
		if (service instanceof RpcServiceBase) {
			RpcServiceBase s = (RpcServiceBase)service;
			services.put(s.getName(), s);
		} else {
			RpcTransparentServiceDecorator decorator = RpcTransparentServiceDecorator.create(service);
			services.put(decorator.getName(), decorator);	
		}	
	}
	
	/**
	 * 
	 * 获取已添加的服务
	 * @param service
	 * @return
	 */
	public RpcServiceBase getService(String service)
	{
		return services.get(service);
	}
	
	/**
	 * 获取运行时的服务名称
	 * @return
	 */
	public String[] getRunningService() {
		Set<String> names = services.keySet();
		return names.toArray(new String[names.size()]);
	}

	/**
	 * 
	 * Rpc事件处理总入口, 此方法在I/O线程中执行, 并转发给应用线程池
	 * @param tx
	 */
	public void processTransaction(RpcServerTransaction tx)
	{
		RpcServerMethodHandler handler = tx.getMethodCache().getHandler();
		final RpcServerContext ctx = new RpcServerContext(tx);
		
		if (handler == null) {
			String serviceName = ctx.getToService();
			RpcServiceBase service = services.get(serviceName);
			if (service == null) {
				ctx.end(RpcReturnCode.SERVICE_NOT_FOUND, null);
				return;
			}
			
			handler = (RpcServerMethodHandler) service.getMethodHanlder(ctx.getToMethod());
			if (handler != null) {
				if (handler.getExecutor() == null) {
					handler.setExecutor(this.getExecutor());
				}
				tx.getMethodCache().setHandler(handler);
			} else {
				if (service.isFixed()) {
					ctx.endWithResponse(RpcResponse.createError(RpcReturnCode.METHOD_NOT_FOUND, null));
					return;
				} else {
					final RpcServiceBase s2 = service;
					handler = new RpcServerMethodHandler(ctx.getToService(), ctx.getToMethod()) {
						@Override
						public void run(RpcServerContext ctx)
						{
							s2.process(ctx);
						}
					};
					handler.setExecutor(this.getExecutor());
				}
			}
		}
		
		final RpcServerMethodHandler h2 = handler;
		try {
			h2.execute(ctx);
		} catch (ExecutorBusyException ex) {
			ctx.end(RpcReturnCode.SERVER_BUSY, null);	
			LOGGER.error("executor.execute busy {}", ex);
		} catch (Exception ex) {
			ctx.end(RpcReturnCode.SERVER_ERROR, ex);
			LOGGER.error("executor.execute failed {}", ex);
		}
	}
}
