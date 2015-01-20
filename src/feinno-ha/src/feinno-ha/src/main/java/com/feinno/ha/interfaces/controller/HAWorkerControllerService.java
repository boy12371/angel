/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei May 15, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.controller;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;

/**
 * 由hamasterd提供的用于提供worker控制的服务
 * 
 * @author 高磊 gaolei@feinno.com
 */

@RpcService("HAWorkerControllerService")
public interface HAWorkerControllerService
{
	/**
	 * 
	 * 连接HAMaster并注册自己
	 */
	@RpcMethod("Register")
	void register(HAWorkerControllerRegisterArgs args);
}
