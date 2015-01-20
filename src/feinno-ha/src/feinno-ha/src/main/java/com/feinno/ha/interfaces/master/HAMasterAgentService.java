package com.feinno.ha.interfaces.master;

import java.sql.SQLException;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;

/**
 * @author zhouyanxjs
 * 
 */
@RpcService(HAMasterAgentService.SERVICE_NAME)
public interface HAMasterAgentService {
	static String SERVICE_NAME = "MasterAgentService";

	/**
	 * 注册连接
	 */
	@RpcMethod("Register")
	void register(HAMasterRegisterArgs input) throws SQLException;

	/**
	 * 保持心跳并上报状态, 在第一次连接或出现报警及worker状态变更时，应立刻进行Keepalive
	 */
	@RpcMethod("Heartbeat")
	void heartbeat(HAMasterHeartbeatArgs input) throws SQLException;

	/**
	 * 获取监控相关配置
	 * 
	 * @return
	 */
	@RpcMethod("GetMonitorConfig")
	HAMasterMonitorConfig getMonitorConfig();

	/**
	 * 获取部署相关配置
	 * 
	 * @return
	 */
	@RpcMethod("GetDeployments")
	HAMasterDeployment[] getDeployments();
}