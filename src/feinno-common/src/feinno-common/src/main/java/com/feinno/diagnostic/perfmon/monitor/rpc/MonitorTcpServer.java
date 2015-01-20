/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor.rpc;

import com.feinno.rpc.server.RpcServiceBootstrap;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@SuppressWarnings("restriction")
public class MonitorTcpServer {

	public MonitorTcpServer() {
		// 注册监控monitor
		RpcServiceBootstrap.registerService(new MonitorServiceImpl());
	}
}
