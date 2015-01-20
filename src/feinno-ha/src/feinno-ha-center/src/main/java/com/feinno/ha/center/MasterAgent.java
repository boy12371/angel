/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 23, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.center;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.Database;
import com.feinno.database.Transaction;
import com.feinno.ha.ServiceSettings;
import com.feinno.ha.database.HADatabaseFactory;
import com.feinno.ha.interfaces.master.HAMasterHeartbeatArgs;
import com.feinno.ha.interfaces.master.HAMasterRegisterArgs;
import com.feinno.ha.interfaces.master.HAMasterWorkerStatus;
import com.feinno.ha.interfaces.monitor.HAMonitorValuePair;
import com.feinno.ha.service.common.CommonStringUtil;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.duplex.RpcDuplexClientAgent;
import com.feinno.rpc.server.RpcServerContext;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class MasterAgent extends RpcDuplexClientAgent {

	private static String centerURL = null;

	public MasterAgent(RpcServerContext ctx, RpcTcpEndpoint endpoint) {
		super(ctx);
		this.endpoint = endpoint;
		if (centerURL == null) {
			centerURL = "tcp://" + ServiceSettings.INSTANCE.getServerAddress() + ":"
					+ ServiceSettings.INSTANCE.getServicePort("rpc_duplex");
		}
	}

	/**
	 * 保存注册客户端信息，并且写入日志
	 * 
	 * @param input
	 *            客户端的请求参数
	 * @return 是否注册成功
	 * @throws SQLException
	 */
	public boolean processRegister(HAMasterRegisterArgs input) throws SQLException {
		if (registered)
			return registered;
		else {
			serverName = input.getServerName();
			masterVersion = input.getMasterVersion();
			masterStartTime = input.getMasterStartTime();
			masterPID = input.getMasterPid();

			// int rt = DatabaseFactory
			// .updateLog(
			// HADatabaseFactory.getHADatabase(),
			// "UPDATE HA_MasterEndpoint SET CenterUrl = ?, MasterVersion=?, RegisterTime = ?, LastKeepaliveTime = ?, MasterStartTime = ?,ServerAddress=?,Status=? WHERE ServerName = ?",
			// "INSERT INTO HA_MasterEndpoint (ServerName,MasterPid,MasterVersion,CenterUrl,RegisterTime,LastKeepaliveTime,MasterStartTime,ServerAddress,Status) VALUES (?,?,?,?,?,?,?,?,?)",
			// new String[] { centerURL, masterVersion,
			// CommonStringUtil.getCurrentDatetime(masterStartTime),
			// CommonStringUtil.getCurrentDatetime(masterStartTime),
			// CommonStringUtil.getCurrentDatetime(masterStartTime),
			// endpoint.getAddress().getAddress().getHostAddress(), "NORMAL",
			// serverName },
			// new String[] { serverName, masterPID + "", masterVersion,
			// centerURL,
			// CommonStringUtil.getCurrentDatetime(masterStartTime), null,
			// CommonStringUtil.getCurrentDatetime(masterStartTime),
			// endpoint.getAddress().getAddress().getHostAddress(), "NORMAL" });

			String sql = "REPLACE INTO HA_MasterEndpoint (ServerName,MasterPid,MasterVersion,CenterUrl,RegisterTime,LastKeepaliveTime,MasterStartTime,ServerAddress,Status) VALUES (?,?,?,?,?,?,?,?,?)";
			Object[] params = new Object[] { serverName, masterPID + "", masterVersion, centerURL,
					CommonStringUtil.getCurrentDatetime(masterStartTime), new java.util.Date(),
					CommonStringUtil.getCurrentDatetime(masterStartTime),
					endpoint.getAddress().getAddress().getHostAddress(), "NORMAL" };

			int rt = HADatabaseFactory.getHADatabase().executeNonQuery(sql, params);
			if (rt > 0) {
				LOGGER.info("Client {} connected", serverName, "NORMAL");
				registered = true;
				return registered;
			} else {
				return registered;
			}
		}
	}

	/**
	 * 更新活跃日志
	 * 
	 * @param input
	 *            请求参数
	 * @return 更新是否成功
	 * @throws SQLException
	 */
	public boolean processKeepAlive(HAMasterHeartbeatArgs input) {

		Transaction trans = null;
		try {
			if (registered) {
				String cmdDeleteMasterMonitorData = "delete from HA_MasterMonitorData where WorkerPid<>0 and ServerName = ? ";
				String cmdMasterEndpoint = "UPDATE HA_MasterEndpoint SET CenterUrl = ?, LastKeepaliveTime = ?, Status = 'NORMAL' WHERE ServerName = ?";
				String cmdMasterMonitorData = "REPLACE into HA_MasterMonitorData (ServerName,WorkerPid,KeyId,KeyValue,UpdateTime,Alarm) values (?,?,?,?,?,?)";
				String cmdDeleteMasterWorkerStatus = "delete from HA_MasterWorkerStatus where ServerName = ? ";
				String cmdInsertMasterWorkerStatus = "REPLACE into HA_MasterWorkerStatus (ServerName,WorkerPid,ServiceName,WorkerMode,WorkerPorts,WorkerStatus,WorkerAction,PackageId,PackageVersion) values (?,?,?,?,?,?,?,?,?)";

				Database db = HADatabaseFactory.getHADatabase();
				trans = db.beginTransaction();
				int r = trans.executeNonQuery(cmdMasterEndpoint, centerURL, CommonStringUtil.getCurrentDatetime(),
						serverName);
				if (r > 0) {
					// 在第一步会将所有的Worker信息都清空，每次对于Worker都是全量的插入
					trans.executeNonQuery(cmdDeleteMasterMonitorData, serverName);
					trans.executeNonQuery(cmdDeleteMasterWorkerStatus, serverName);

					List<HAMonitorValuePair> values = input.getMachineValues();
					for (int i = 0; values != null && i < values.size(); i++) {
						HAMonitorValuePair v = values.get(i);
						// 如果更新监控信息失败，进行插入操作
						trans.executeNonQuery(cmdMasterMonitorData, serverName, 0, v.getKey(), v.getValue(),
								CommonStringUtil.getCurrentDatetime(), v.getAlarm());
					}
					List<HAMasterWorkerStatus> workerStatus = input.getWorkerStatus();
					for (int i = 0; workerStatus != null && i < workerStatus.size(); i++) {
						HAMasterWorkerStatus ws = workerStatus.get(i);
						trans.executeNonQuery(cmdInsertMasterWorkerStatus, serverName, ws.getPid(),
								ws.getServiceName(), ws.getWorkerMode(), ws.getWorkerPorts(), ws.getWorkerStatus(),
								ws.getWorkerAction(), ws.getPackageId(), ws.getPackageVersion());
						// }
						if (ws != null && ws.getWorkerValues() != null) {
							for (HAMonitorValuePair v : ws.getWorkerValues()) {
								trans.executeNonQuery(cmdMasterMonitorData, serverName, ws.getPid(), v.getKey(),
										v.getValue(), CommonStringUtil.getCurrentDatetime(), v.getAlarm());
							}

						}
					}
					trans.commit();
					return true;
				} else {
					throw new SQLException("Saving heartbeat of Master has Failed. ");
				}
			}
		} catch (SQLException e) {
			LOGGER.error("KeepAlive Error ", e);
			if (trans != null)
				trans.rollback();
		} finally {
			if (trans != null) {
				trans.close();
			}
		}

		return false;
	}

	public String getServerName() {
		return serverName;
	}

	public int getMasterPID() {
		return masterPID;
	}
	
	public RpcTcpEndpoint getEndpoint(){
		return endpoint;
	}

	private String masterVersion;
	private Date masterStartTime;
	private int masterPID;
	private String serverName;
	private RpcTcpEndpoint endpoint;

	private boolean registered = false;
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());;
}
