/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-7-26
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.center;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@RpcService("HACenterConsoleService")
public interface HACenterConsoleService {
	@RpcMethod("GetMasterEndpoint")
	HAMasterEndpoint getMasterEndpoint(String serverName);

	/**
	 * 
	 * 返回所有所有已连接的workerEndpoint, 从HA_WorkerEndpoint表中查询
	 * 
	 * @param args
	 *            : serverName, 可以为空, 为空时不限服务器, serviceName, 可以为空,
	 *            为空时不限服务名称(不可同时为空)
	 * @return
	 */
	@RpcMethod("GetWorkerEndpoints")
	HAWorkerEndpoint[] getWorkerEndpoints(HAConsoleOperationArgs args);
}
