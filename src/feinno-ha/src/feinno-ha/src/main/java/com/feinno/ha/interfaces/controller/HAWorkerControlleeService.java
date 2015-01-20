/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-9
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.controller;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;

/**
 * 由Worker服务提供的启动接口
 * 
 * @author 高磊 gaolei@feinno.com
 */
@RpcService("HAWorkerControlleeService")
public interface HAWorkerControlleeService {
	@RpcMethod("Ping")
	void ping();

	@RpcMethod("Start")
	void start() throws Exception;

	@RpcMethod("Stop")
	void stop() throws Exception;
}
