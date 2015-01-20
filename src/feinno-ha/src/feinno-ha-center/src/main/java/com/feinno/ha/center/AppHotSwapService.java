/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 24, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.center;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.rpc.client.RpcMethodStub;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcServiceBase;
import com.feinno.serialization.protobuf.types.ProtoString;
import com.feinno.util.EventHandler;

/**
 * 用于应用热切的RPC服务，该服务负责将指令传递到每一个需要切的Worker应用中
 * 
 * @author 高磊 gaolei@feinno.com
 */

public class AppHotSwapService extends RpcServiceBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppHotSwapService.class);

	public static final AppHotSwapService INSTANCE = new AppHotSwapService("AppHotSwapService");

	protected AppHotSwapService(String name) {
		super(name);
	}

	@RpcMethod("refresh")
	public void refresh(final RpcServerContext ctx) {
		try {
			ProtoString hotServerName = ctx.getExtension(1001, ProtoString.class);
			LOGGER.info("Request hotServerName {} ", hotServerName.getValue());
			List<WorkerAgent> workerAgents = WorkerAgentService.INSTANCE.getWorkerAgents(hotServerName.getValue());
			LOGGER.info("Found {} HotServer", workerAgents.size());
			for (WorkerAgent workerAgent : workerAgents) {
				LOGGER.info(
						"Invoke workerAgent. ServerName={} ,ServiceName={} ,WorkerPid={}",
						new Object[] { workerAgent.getServerName(), workerAgent.getSeviceName(),
								String.valueOf(workerAgent.getWorkerPid()) });
				RpcMethodStub sub = workerAgent.getMethodStub("AppEngineCallbackService", "refresh");
				RpcFuture future = sub.invoke(null);
				future.addListener(new EventHandler<RpcResults>() {
					public void run(Object sender, RpcResults result) {
						if (result.getError() != null) {
							LOGGER.error("AppHotSwapService.process invoke error.", result.getError());
						}
					}
				});
			}
			ctx.end();
		} catch (Exception e) {
			LOGGER.error("AppHotSwapService.process invoke error.", e);
			ctx.endWithError(e);
		}
	}
}