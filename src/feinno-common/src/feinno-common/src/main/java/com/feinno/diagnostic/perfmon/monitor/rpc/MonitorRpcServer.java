package com.feinno.diagnostic.perfmon.monitor.rpc;

import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.server.RpcServiceBootstrap;
import com.feinno.threading.ExecutorFactory;

/**
 * 
 * @author jingmiao
 * 
 */
public class MonitorRpcServer {
	public MonitorRpcServer(int port) throws Exception {
		// 注册服务端通道
		RpcServiceBootstrap.registerChannel(new RpcTcpServerChannel(port));
		// 注册监控monitor
		RpcServiceBootstrap.registerService(new MonitorServiceImpl());
		// 设置工作线程池
		RpcServiceBootstrap.setExecutor(ExecutorFactory.newFixedExecutor(
				"mock", 32, 32 * 1000));

		RpcServiceBootstrap.start();
	}

	public static void main(String[] args) throws Exception {
		new MonitorRpcServer(8081);
	}
}
