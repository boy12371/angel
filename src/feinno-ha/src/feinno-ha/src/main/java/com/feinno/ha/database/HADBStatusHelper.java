package com.feinno.ha.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.ha.interfaces.center.HAMasterEndpoint;
import com.feinno.ha.interfaces.center.HAWorkerEndpoint;
import com.feinno.ha.interfaces.worker.HAWorkerRegisterArgs;

/**
 * 
 * <b>描述: </b>该类是用于HA_Worker或HA_Master的数据库操作的辅助类
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class HADBStatusHelper {

	/** 新增Worker运行状态数据 */
	public static final String SQL_INSERT_WORKER_ENDPOINT = "INSERT INTO HA_WorkerEndpoint(ServerName,WorkerPid,WorkerId,CenterUrl,ServicePorts,DeploymentId,RegisterTime,LastHeartbeat,Connected,WorkerStatus,WorkerStatusEx) VALUES(?,?,?,?,?,?,?,?,?,?,?) ";

	/** 查询MasterEndpoint */
	public static final String SQL_SELECT_MASTER_ENDPOINT_BY_SERVERNAME = "SELECT CenterUrl FROM HA_MasterEndpoint WHERE Status = 'NORMAL' and ServerName = ? ";

	/** 获取运行中的WorkerEndpoint */
	public static final String SQL_SELECT_WORKER_ENDPOINT_RUNNING = "SELECT ServerName,WorkerPid,WorkerId,CenterUrl,WorkerStatus FROM HA_WorkerEndpoint WHERE Connected = 'CONNECTED' and WorkerStatus = 'STARTED' ";

	/** 获取Worker运行状态数据 */
	public static final String SQL_SELECT_WORKER_ENDPOINT = "SELECT ServerName,WorkerPid,WorkerId,ServicePorts,DeploymentId,RegisterTime,LastHeartbeat,Connected,WorkerStatus,WorkerStatusEx FROM HA_WorkerEndpoint WHERE ServerName = ? and WorkerPid = ? and WorkerId = ? ";

	public static final String SQL_SELECT_CENTER_URL_BY_WORKER_ENDPOINT = "SELECT CenterUrl FROM HA_WorkerEndpoint WHERE WorkerId = ? and Connected = 'CONNECTED' and WorkerStatus = 'STARTED'  ";

	/** 更新Worker运行状态数据 */
	public static final String SQL_UPDATE_WORKER_ENDPOINT = "UPDATE HA_WorkerEndpoint  SET CenterUrl = ? , ServicePorts = ? , DeploymentId = ?, RegisterTime = ? , LastHeartbeat = ? ,Connected = ? ,WorkerStatus = ? ,WorkerStatusEx = ?   WHERE ServerName = ? and WorkerPid = ? and WorkerId = ? ";

	/** 更新最后一次心跳时间 */
	public static final String SQL_UPDATE_HEARTBEAT = "UPDATE HA_WorkerEndpoint  SET Connected = 'CONNECTED' , LastHeartbeat = ? , WorkerStatus = ? , WorkerStatusEx = ? WHERE ServerName = ? and WorkerPid = ? ";

	/** 更新连接状态 */
	public static final String SQL_UPDATE_CONNECTED = "UPDATE HA_WorkerEndpoint  SET Connected = ? WHERE ServerName = ? and WorkerPid = ? ";

	/** 更新服务状态 */
	public static final String SQL_UPDATE_WORKERSTATUS = "UPDATE HA_WorkerEndpoint  SET WorkerStatus = ? WHERE ServerName = ? and WorkerPid = ? ";

	/** 更新最后一次心跳时间是10分钟前的WorkerEndpoint的连接状态 */
	public static final String SQL_UPDATE_WORKER_CONNECTED_EXPIRED = "UPDATE HA_WorkerEndpoint  SET Connected = 'EXPIRED' WHERE Connected = 'CONNECTED' and LastHeartbeat < ? ";

	/** 更新最后一次心跳时间是10分钟前的MasterEndpoint的连接状态 */
	public static final String SQL_UPDATE_MASTER_CONNECTED_EXPIRED = "UPDATE HA_MasterEndpoint SET Status = 'DISCONNECTED' WHERE Status = 'NORMAL' and LastKeepaliveTime < ? ";

	/** 获得最后一次心跳时间是24小时前的WorkerEndpoint的记录 */
	public static final String SQL_SELECT_STOP_WORKER = "select ServerName,WorkerPid from HA_WorkerEndpoint WHERE LastHeartbeat < ? ";

	/** 删除最后一次心跳时间是24小时前的WorkerEndpoint的记录 */
	public static final String SQL_DELETE_STOP_WORKER = "delete from HA_WorkerEndpoint WHERE LastHeartbeat < ? ";

	/** 新增Worker监控数据 */
	public static final String SQL_INSERT_WORKER_MONITOR_DATA = "REPLACE INTO HA_WorkerMonitorData(ServerName,WorkerPid,KeyId,KeyValue,UpdateTime,Alarm) VALUES(?,?,?,?,?,?) ";

	/** 新增Master监控数据 */
	public static final String SQL_INSERT_MASTER_MONITOR_DATA = "REPLACE INTO HA_MasterMonitorData(ServerName,WorkerPid,KeyId,KeyValue,UpdateTime,Alarm) VALUES(?,?,?,?,?,?) ";

	/** 日志对象的引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HADBStatusHelper.class);

	/**
	 * 通过serverName获取HAMasterEndpoint
	 * 
	 * @param serverName
	 * @param workerId
	 * @param workerPid
	 * @param deploymentId
	 * @param PackageId
	 * @param workerStatus
	 * @return
	 */
	public static HAMasterEndpoint getHAMasterEndpoint(String serverName) throws SQLException {
		LOGGER.info("getHAMasterEndpoint.");
		Database database = HADatabaseFactory.getHADatabase();

		DataTable dataTable = database.executeTable(SQL_SELECT_MASTER_ENDPOINT_BY_SERVERNAME,
				new Object[] { serverName });
		HAMasterEndpoint masterEndpoint = new HAMasterEndpoint();
		for (DataRow row : dataTable.getRows()) {
			masterEndpoint.setCenterUrl(row.getString("CenterUrl"));
			break;
		}
		return masterEndpoint;
	}

	/**
	 * 通过serverName或者serviceName获取HAWorkerEndpoint
	 * 
	 * @param serverName
	 * @param workerId
	 * @param workerPid
	 * @param deploymentId
	 * @param PackageId
	 * @param workerStatus
	 * @return
	 */
	public static HAWorkerEndpoint[] getHAWorkerEndpointArray(String serverName, String serviceName)
			throws SQLException {
		LOGGER.info("GetHAWorkerEndpointArray serverName={},serviceName={}.", serverName, serviceName);
		Database database = HADatabaseFactory.getHADatabase();

		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append(SQL_SELECT_WORKER_ENDPOINT_RUNNING);
		List<Object> paramList = new ArrayList<Object>();
		if (serverName != null && serverName.length() > 0) {
			sqlSB.append("and ServerName = ? ");
			paramList.add(serverName);
		}
		if (serviceName != null && serviceName.length() > 0) {
			sqlSB.append("and WorkerId = ? ");
			paramList.add(serviceName);
		}
		DataTable dataTable = database.executeTable(sqlSB.toString(), paramList.toArray());
		int pos = 0;
		HAWorkerEndpoint[] result = new HAWorkerEndpoint[dataTable.getRowCount()];
		for (DataRow row : dataTable.getRows()) {
			HAWorkerEndpoint workerEndpoint = new HAWorkerEndpoint();
			workerEndpoint.setServerName(row.getString("ServerName"));
			workerEndpoint.setServiceName(row.getString("WorkerId"));
			workerEndpoint.setPid(row.getInt("WorkerPid"));
			workerEndpoint.setCenterUrl(row.getString("CenterUrl"));
			workerEndpoint.setStatus(row.getString("WorkerStatus"));
			result[pos++] = workerEndpoint;
		}
		return result;
	}

	/**
	 * 通过serverName获取该其所连接的Center服务器，已经排重
	 * 
	 * @param serverName
	 * @return
	 * @throws SQLException
	 */
	public static Set<String> getCenterUrlByServerName(String serviceName) throws SQLException {
		LOGGER.info("GetCenterUrlByServerName,serverName = {}.", serviceName);
		Database database = HADatabaseFactory.getHADatabase();
		DataTable dataTable = database.executeTable(SQL_SELECT_CENTER_URL_BY_WORKER_ENDPOINT, serviceName);
		Set<String> centerUrlSet = new HashSet<String>();
		for (DataRow row : dataTable.getRows()) {
			centerUrlSet.add(row.getString("CenterUrl"));
		}
		return centerUrlSet;
	}

	/**
	 * 注册一个WorkEndpoint信息
	 * 
	 * @param serverName
	 * @param workerId
	 * @param workerPid
	 * @param deploymentId
	 * @param PackageId
	 * @param workerStatus
	 * @return
	 */
	public static boolean regWorkEndpoint(HAWorkerRegisterArgs args, String centerUrl) throws SQLException {
		LOGGER.info("reg WorkEndpoint.");
		// 1. 取参数
		String serverName = args.getServerName();
		String workerId = args.getServiceName();
		int workerPid = args.getWorkerPid();
		String servicePorts = args.getServicePorts();
		int deploymentId = args.getDeploymentId();
		String workerStatusEx = "";
		// 2. 插入数据库
		Database database = HADatabaseFactory.getHADatabase();
		boolean isExists = database.executeTable(SQL_SELECT_WORKER_ENDPOINT,
				new Object[] { serverName, workerPid, workerId }).getRowCount() > 0;
		if (isExists) {
			Object[] saveParams = new Object[] { centerUrl, servicePorts, deploymentId, new java.util.Date(),
					new java.util.Date(), Connected.CONNECTED.getText(), WorkerStatus.STARTED.getText(),
					workerStatusEx, serverName, workerPid, workerId };
			int code = database.executeNonQuery(SQL_UPDATE_WORKER_ENDPOINT, saveParams);
			return code > 0;
		} else {
			Object[] saveParams = new Object[] { serverName, workerPid, workerId, centerUrl, servicePorts,
					deploymentId, new java.util.Date(), new java.util.Date(), Connected.CONNECTED.getText(),
					WorkerStatus.STARTED.getText(), workerStatusEx };
			int code = database.executeNonQuery(SQL_INSERT_WORKER_ENDPOINT, saveParams);
			return code > 0;
		}
	}

	/**
	 * 更新指定WorkerEndpoint的最后心跳时间
	 * 
	 * @param serverName
	 * @param workerPid
	 * @return
	 * @throws SQLException
	 */
	public static boolean heartbeat(String serverName, int workerPid, String status, String statusEx)
			throws SQLException {
		LOGGER.info(String.format("%s %s %s %s heartbeat", serverName, workerPid, status, statusEx));
		Database database = HADatabaseFactory.getHADatabase();
		Object[] heartbeatParams = new Object[] { new Date(Calendar.getInstance().getTimeInMillis()), status, statusEx,
				serverName, workerPid };
		int code = database.executeNonQuery(SQL_UPDATE_HEARTBEAT, heartbeatParams);
		return code > 0;

	}

	/**
	 * 更新连接状态
	 * 
	 * @param serverName
	 * @param workerPid
	 * @param connected
	 * @return
	 * @throws SQLException
	 */
	public static boolean updateConnected(String serverName, int workerPid, Connected connected) throws SQLException {
		LOGGER.info(String.format("update %s %s Connected to ", serverName, workerPid, connected.getText()));
		Database database = HADatabaseFactory.getHADatabase();
		Object[] heartbeatParams = new Object[] { connected.getText(), serverName, workerPid };
		int code = database.executeNonQuery(SQL_UPDATE_CONNECTED, heartbeatParams);
		return code > 0;
	}

	/**
	 * 更新Worker连接状态为过期
	 * 
	 * @param expiredMinute
	 * @return
	 * @throws SQLException
	 */
	public static boolean updateExpiredWorkerConnected(int expiredMinute) throws SQLException {
		LOGGER.info("Update Worker Expired Connected");
		Database database = HADatabaseFactory.getHADatabase();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -expiredMinute);
		Object[] heartbeatParams = new Object[] { new Date(calendar.getTimeInMillis()) };
		int code = database.executeNonQuery(SQL_UPDATE_WORKER_CONNECTED_EXPIRED, heartbeatParams);
		return code > 0;
	}

	/**
	 * 更新Master连接状态为过期
	 * 
	 * @param expiredMinute
	 * @return
	 * @throws SQLException
	 */
	public static boolean updateExpiredMasterConnected(int expiredMinute) throws SQLException {
		LOGGER.info("Update Master Expired Connected");
		Database database = HADatabaseFactory.getHADatabase();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -expiredMinute);
		Object[] heartbeatParams = new Object[] { new Date(calendar.getTimeInMillis()) };
		int code = database.executeNonQuery(SQL_UPDATE_MASTER_CONNECTED_EXPIRED, heartbeatParams);
		return code > 0;
	}

	/**
	 * 删除超时的连接状态
	 * 
	 * @param expiredHour
	 * @return
	 * @throws SQLException
	 */
	public static void removeStopWorkerPoint(int expiredHour) throws SQLException {
		LOGGER.info("Remove stop worker point.");
		Database database = HADatabaseFactory.getHADatabase();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -expiredHour);
		Object[] heartbeatParams = new Object[] { new Date(calendar.getTimeInMillis()) };
		// 删除超时的服务
		database.executeNonQuery(SQL_DELETE_STOP_WORKER, heartbeatParams);
	}

	/**
	 * 更新服务运行状态
	 * 
	 * @param serverName
	 * @param workerPid
	 * @param workerStatus
	 * @return
	 * @throws SQLException
	 */
	public static boolean updateWorkerStatus(String serverName, int workerPid, WorkerStatus workerStatus)
			throws SQLException {
		LOGGER.info(String.format("Update Worker %s %s Connected to %s", serverName, workerPid, workerStatus.getText()));
		Database database = HADatabaseFactory.getHADatabase();
		Object[] heartbeatParams = new Object[] { serverName, workerPid, workerStatus.getText() };
		int code = database.executeNonQuery(SQL_UPDATE_WORKERSTATUS, heartbeatParams);
		return code > 0;
	}

	/**
	 * 新增保存Worker监控记录
	 * 
	 * @param serverName
	 * @param workerPid
	 * @param keyId
	 * @param keyValue
	 * @param alarm
	 * @return
	 * @throws SQLException
	 */
	public static boolean saveWorkerMonitorData(String serverName, int workerPid, int keyId, float keyValue,
			String alarm) throws SQLException {
		LOGGER.info(String.format("Save Worker Monitor Data. serverName: %s ,workerPid: %s ", serverName, workerPid));
		Database database = HADatabaseFactory.getHADatabase();
		Object[] heartbeatParams = new Object[] { serverName, workerPid, keyId, keyValue,
				new Date(Calendar.getInstance().getTimeInMillis()), alarm != null ? alarm : "" };
		int code = database.executeNonQuery(SQL_INSERT_WORKER_MONITOR_DATA, heartbeatParams);
		return code > 0;
	}

	// /**
	// * 新增保存Master监控记录
	// *
	// * @param serverName
	// * @param workerPid
	// * @param keyId
	// * @param keyValue
	// * @param alarm
	// * @return
	// * @throws SQLException
	// */
	// public static boolean saveMasterMonitorData(String serverName, int
	// workerPid, int keyId, float keyValue,
	// String alarm) throws SQLException {
	// LOGGER.info(String.format("Save Master Monitor Data. serverName: %s ,workerPid: %s ",
	// serverName, workerPid));
	// Database database = HADatabaseFactory.getHADatabase();
	// Object[] heartbeatParams = new Object[] { serverName, workerPid, keyId,
	// keyValue,
	// new Date(Calendar.getInstance().getTimeInMillis()), alarm != null ? alarm
	// : "" };
	// int code = database.executeNonQuery(SQL_INSERT_MASTER_MONITOR_DATA,
	// heartbeatParams);
	// return code > 0;
	// }

	/** 连接状态的枚举类型 */
	public static enum Connected {
		CONNECTED("CONNECTED"), DISCONNECTED("DISCONNECTED"), EXPIRED("EXPIRED");
		String text;

		private Connected(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	/** 服务状态的枚举类型 */
	public static enum WorkerStatus {
		STARTED("STARTED"), STOPPING("STOPPING"), STOPPED("STOPPED");
		String text;

		private WorkerStatus(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}
}
