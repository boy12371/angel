/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei May 15, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.worker;

import com.feinno.ha.interfaces.configuration.HAConfigArgs;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;

/**
 * 
 * 由客户端Worker提供的duplex回调服务
 * 
 * @author 高磊 gaolei@feinno.com
 */
@RpcService("HAWorkerAgentCallbackService")
public interface HAWorkerAgentCallbackService
{	
	/**
	 * 
	 * 通知配置过期
	 */
	@RpcMethod("NotifyConfigExpired")
	public void notifyConfigExpired(HAConfigArgs args);
}
