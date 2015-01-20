/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Aug 24, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.deployment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.ha.database.HADatabaseFactory;
import com.feinno.ha.interfaces.center.HAWorkerEndpoint;
import com.feinno.ha.interfaces.master.HAMasterDeployment;
import com.feinno.ha.interfaces.master.HAMasterWorkerStatus;
import com.feinno.rpc.RpcEndpointFactory;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.serialization.protobuf.util.TwoTuple;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HADatabaseHelper {

	/** 通过ServerName以及ServiceName获得Master所管理的Worker的运行状态 */
	public static final String SQL_SELECT_MASTER_WORKER_STATUS = "SELECT WorkerPid,ServiceName,WorkerMode,WorkerPorts,WorkerStatus,WorkerAction,PackageId,PackageVersion FROM HA_MasterWorkerStatus WHERE ServerName = ? AND ServiceName=? ";

	/** 通过ServerName以及ServiceName获得Worker自己上报的WorkerEndpoint信息 */
	public static final String SQL_SELECT_WORKER_ENDPOINT = "SELECT ServerName,WorkerId,WorkerPid,CenterUrl,WorkerStatus FROM HA_WorkerEndpoint WHERE Connected = 'CONNECTED' AND ServerName = ? AND WorkerId=?";

	/** 通过Server获得对应的Group */
	public static final String SQL_SELECT_MASTER_DEPLOYMENT = "SELECT HA_WorkerDeployment.ServiceDeployName,HA_WorkerDeployment.PackageId,HA_WorkerPackage.PackageFile,HA_WorkerPackage.ServiceVersion,HA_WorkerPackage.Controllable,HA_WorkerDeployment.WorkerUpdateMode,HA_WorkerDeployment.WorkerStartMode,HA_WorkerPackage.ServicePorts,HA_WorkerDeployment.PortsSettings,HA_WorkerDeployment.StartParams,HA_WorkerDeployment.Settings,HA_WorkerDeployment.ServerGroup FROM HA_WorkerDeployment,HA_WorkerPackage WHERE HA_WorkerDeployment.PackageId = HA_WorkerPackage.PackageId AND HA_WorkerDeployment.ServiceDeployName = ? ";

	/** 通过Server获得对应的Group */
	public static final String SQL_SELECT_SERVER_GROUP = "SELECT HA_ServerGroupMember.ServerName,HA_MasterEndpoint.CenterUrl FROM HA_ServerGroupMember,HA_MasterEndpoint  WHERE HA_ServerGroupMember.ServerName = HA_MasterEndpoint.ServerName AND HA_ServerGroupMember.ServerGroup = ?;";

	/** 向HA_MasterOperationLog表插入操作记录 */
	public static final String SQL_INSERT_MASTER_OPERATION_LOG = "INSERT INTO HA_MasterOperationLog(Id,BeginTime,Duration,ServerName,ServiceName,Action,ActionResult,ActionError) VALUES(?,?,?,?,?,?,?,?)";

	/** 日志引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HADatabaseHelper.class);

	/**
	 * 获取某一台Master所管理的Worker的运行状态
	 * 
	 * @param serverName
	 * @param serviceName
	 * @return
	 */
	public static HAMasterWorkerStatus[] readMasterWorkerStaus(String serverName, String serviceName) {
		LOGGER.info("Read MasterWorkerStaus.");
		Database database = HADatabaseFactory.getHADatabase();
		try {
			DataTable dataTable = database.executeTable(SQL_SELECT_MASTER_WORKER_STATUS, new Object[] { serverName,
					serviceName });
			if (dataTable.getRowCount() > 0) {
				int index = 0;
				HAMasterWorkerStatus[] workerStatusArray = new HAMasterWorkerStatus[dataTable.getRowCount()];
				for (DataRow row : dataTable.getRows()) {
					workerStatusArray[index] = new HAMasterWorkerStatus();
					workerStatusArray[index].setPid(row.getInt("WorkerPid"));
					workerStatusArray[index].setServiceName(row.getString("ServiceName"));
					workerStatusArray[index].setWorkerMode(row.getString("WorkerMode"));
					workerStatusArray[index].setWorkerPorts(row.getString("WorkerPorts"));
					workerStatusArray[index].setWorkerStatus(row.getString("WorkerStatus"));
					workerStatusArray[index].setWorkerAction(row.getString("WorkerAction"));
					workerStatusArray[index].setPackageId(row.getInt("PackageId"));
					workerStatusArray[index].setPackageVersion(row.getString("PackageVersion"));
					// TODO 缺少List<HAMonitorValuePair> workerValues
					index++;
				}
				return workerStatusArray;
			}
		} catch (Exception e) {
			LOGGER.error("Read MasterWorkerStaus failed. ", e);
		}
		return null;
	}

	/**
	 * 获得Worker的运行状态
	 * 
	 * @param serverName
	 * @param serviceName
	 * @return
	 */
	public static HAWorkerEndpoint[] readWorkerEndpoint(String serverName, String serviceName) {
		LOGGER.info("Read WorkerEndpoint.");
		Database database = HADatabaseFactory.getHADatabase();
		try {
			DataTable dataTable = database.executeTable(SQL_SELECT_WORKER_ENDPOINT, new Object[] { serverName,
					serviceName });
			if (dataTable.getRowCount() > 0) {
				int index = 0;
				HAWorkerEndpoint[] workerEndpointArray = new HAWorkerEndpoint[dataTable.getRowCount()];
				for (DataRow row : dataTable.getRows()) {
					workerEndpointArray[index] = new HAWorkerEndpoint();
					workerEndpointArray[index].setServerName(row.getString("ServerName"));
					workerEndpointArray[index].setServiceName(row.getString("WorkerId"));
					workerEndpointArray[index].setPid(row.getInt("WorkerPid"));
					workerEndpointArray[index].setCenterUrl(row.getString("CenterUrl"));
					workerEndpointArray[index].setStatus(row.getString("WorkerStatus"));
					index++;
				}
				return workerEndpointArray;
			}
		} catch (Exception e) {
			LOGGER.error("Read WorkerEndpoint failed. ", e);
		}
		return null;
	}

	/**
	 * 通过serviceName读取部署信息
	 * 
	 * @param serviceName
	 * @return
	 */
	public static HAMasterDeployment readDeployment(String serviceName) {
		LOGGER.info("Read Deployment.");
		Database database = HADatabaseFactory.getHADatabase();
		try {
			DataTable dataTable = database.executeTable(SQL_SELECT_MASTER_DEPLOYMENT, new Object[] { serviceName });
			if (dataTable.getRowCount() > 0) {
				HAMasterDeployment deployment = new HAMasterDeployment();
				for (DataRow row : dataTable.getRows()) {
					deployment.setServiceDeployName(row.getString("ServiceDeployName"));
					deployment.setPackageId(row.getInt("PackageId"));
					deployment.setPackageUrl(row.getString("PackageFile"));
					deployment.setServiceVersion(row.getString("ServiceVersion"));
					deployment.setControllable((Boolean) row.getObject("Controllable"));
					deployment.setWorkerUpdateMode(row.getString("WorkerUpdateMode"));
					deployment.setWorkerStartMode(row.getString("WorkerStartMode"));
					deployment.setServicePorts(row.getString("ServicePorts"));
					deployment.setPortsSettings(row.getString("PortsSettings"));
					deployment.setStartParams(row.getString("StartParams"));
					deployment.setSettings(row.getString("Settings"));
					deployment.setServerGroup(row.getString("ServerGroup"));
					return deployment;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Read Deployment failed. ", e);
		}
		return null;
	}

	/** 这是一个用于缓存ServerGroup的Cache,其中存储了ServerGroup，其中的日期代表的入列时间 */
	private static final Map<String, TwoTuple<HAServerGroup, Date>> SERVER_GROUP_CACHE = new HashMap<String, TwoTuple<HAServerGroup, Date>>();

	/**
	 * 通过serverGroupName去的ServerGroup中每一个Server的Master所连接的Center地址，
	 * 该地址的缓存有利于日后对Master的控制
	 * 
	 * @param serverGroupName
	 * @return
	 */
	public static HAServerGroup readServerGroup(String serverGroupName) {
		LOGGER.info("Read ServerGroup.");
		HAServerGroup serverGroup = null;
		TwoTuple<HAServerGroup, Date> twoTuple = SERVER_GROUP_CACHE.get(serverGroupName);
		if (twoTuple != null && new Date().getTime() - twoTuple.getSecond().getTime() < 3000) {
			serverGroup = twoTuple.getFirst();
		} else {
			serverGroup = new HAServerGroup();
			Database database = HADatabaseFactory.getHADatabase();
			try {
				DataTable dataTable = database.executeTable(SQL_SELECT_SERVER_GROUP, new Object[] { serverGroupName });
				if (dataTable.getRowCount() > 0) {
					List<HAServer> serverList = new ArrayList<HAServer>();
					for (DataRow row : dataTable.getRows()) {
						HAServer server = new HAServer();
						server.setServerName(row.getString("ServerName"));
						RpcEndpoint endpoint = RpcEndpointFactory.parse(row.getString("CenterUrl"));
						server.setMasterCenterEp(endpoint);
						serverList.add(server);
					}
					serverGroup.setServers(serverList);
					SERVER_GROUP_CACHE.put(serverGroupName, new TwoTuple<HAServerGroup, Date>(serverGroup, new Date()));
					return serverGroup;
				}
			} catch (Exception e) {
				LOGGER.error("Read ServerGroup failed. ", e);
			}
		}
		return serverGroup;
	}

	public static boolean writeOperationLog(Date beginTime, long duration, String serverName, String serviceName,
			String action, String actionResult, String actionError) {
		Database database = HADatabaseFactory.getHADatabase();
		try {
			LOGGER.info("Write Operation Log.");
			int result = database.executeNonQuery(SQL_INSERT_MASTER_OPERATION_LOG, new Object[] {
					UUID.randomUUID().toString(), beginTime, duration, serverName, serviceName, action, actionResult,
					actionError });
			return result > 0;
		} catch (Exception e) {
			LOGGER.error("Write Operation Log failed. ", e);
			return false;
		}
	}
}
