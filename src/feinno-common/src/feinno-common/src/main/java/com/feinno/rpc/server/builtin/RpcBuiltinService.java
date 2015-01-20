/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-3-2
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.server.builtin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcServiceBase;
import com.feinno.rpc.server.RpcServiceBootstrap;
import com.feinno.util.ServiceEnviornment;

/**
 * Rpc内置服务
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcBuiltinService extends RpcServiceBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcBuiltinService.class);

	public RpcBuiltinService() {
		super("__Builtin");
	}

	@RpcMethod("Ping")
	public void ping(RpcServerContext ctx) {
		try {
			RpcPingResults results = new RpcPingResults();
			results.setServerName(ServiceEnviornment.getComputerName());
			String serviceName = results.getServiceName();
			serviceName = serviceName == null ? System.getProperty("sun.java.command") : serviceName;
			results.setServiceName(serviceName);
			results.setServices(RpcServiceBootstrap.getRunningService());
			ctx.end(results);
		} catch (Exception e) {
			LOGGER.error("__Builtin.Ping failed", e);
			ctx.end(e);
		}
	}

	/**
	 * 
	 * 枚举，包含类型信息 类似于生成中间类型
	 * 
	 * @RpcMethod("EnumService") {在这里补充功能说明}
	 * @param ctx
	 */
	public void enumService(RpcServerContext ctx) {
	}

	/**
	 * 
	 * 仅针对一长连接服务交换一条缓存信息，交换后可提升性能 TODO: 将在1.5.1版本中实现
	 * 
	 * @param ctx
	 */
	@RpcMethod("Negociate")
	public void negociate(RpcServerContext ctx) {
		throw new UnsupportedOperationException("没实现呢");
		// RpcConnection conn = ctx.getConnection();
		// if (!(conn instanceof RpcConnectionReal)) {
		// ctx.end(RpcReturnCode.SERVER_ASSERT_FAILED, null);
		// }
		//
		// RpcNegociateArgs a = ctx.getArgs(RpcNegociateArgs.class);
		// //
		// // 先协商fromId
		// int fromId =
		// RpcServerMethodManager.INSTANCE.getOrAddFrom(a.getFromComputer(),
		// a.getFromService());
		//
		// //
		// // 协商toMethodCache
		// RpcServerMethodCache cache =
		// RpcServerMethodManager.INSTANCE.getMethodCache(
		// a.getFromComputer(), a.getFromService(), a.getToService(),
		// a.getToMethod());
		//
		// ((RpcConnectionReal)conn).put(cache);
		//
		// RpcNegociateResults results = new RpcNegociateResults();
		// results.setFromId(cache.getFromId());
		// results.setToId(cache.getToId());
		// ctx.end(results);
	}
}
