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
 * 
 * 用于获取部署信息的服务
 * 
 * @author 高磊 gaolei@feinno.com
 */
@RpcService("HACenterDeploymentService")
public interface HACenterDeploymentService {
	@RpcMethod("GetServerGroups")
	String[] getServerGroups(String serviceName);

	@RpcMethod("GetPackageInfo")
	HAPackageInfo getPackageInfo(int packageId);

	// TOOD: 加载更多的信息
}
