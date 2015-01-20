package com.feinno.ha.database;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.ha.interfaces.center.HAPackageInfo;
import com.feinno.util.DateUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 
 * <b>描述: </b>该类是用于HA_Deployment的数据库操作的辅助类
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class HADBDeploymentHelper {

	/** 通过PackageId获得对应的WORKERPACKAGE */
	public static final String SQL_SELECT_WORKERPACKAGE = "SELECT PackageId,PackageFile,UploadTime,UploadUser,Comment,ServiceName,ServiceVersion,Controllable,ServicePorts,Metas,StartParams,Instances,MinMemory,MaxMemory FROM HA_WorkerPackage  WHERE PackageId = ? ";

	/** 通过PackageId获得对应的SIMPLE_WORKERPACKAGE */
	public static final String SQL_SELECT_SIMPLE_WORKERPACKAGE = "SELECT PackageId,PackageFile,ServiceVersion FROM HA_WorkerPackage  WHERE PackageId = ? ";

	/** 通过ServiceDeployName获取对应的WorkerPackageFile信息 **/
	public static final String SQL_SELECT_WORKER_PACKAGE_BY_SERVICE_DEPLOY_NAME = "SELECT HA_WorkerPackage.PackageId,HA_WorkerPackage.ServiceName,HA_WorkerPackage.ServiceVersion,HA_WorkerPackage.PackageFile,HA_WorkerPackage.UploadTime,HA_WorkerPackage.`Comment` FROM HA_WorkerPackage WHERE HA_WorkerPackage.ServiceName=? ";

	/** 清理过期的worker部署信息 */
	public static final String SQL_CLEAN_EXPIRED_WORKERDEPLOYMENTSERVER = "DELETE FROM  HA_WorkerDeploymentServer WHERE (SELECT COUNT(1) AS num FROM HA_WorkerDeployment WHERE HA_WorkerDeploymentServer.ServiceDeployName = HA_WorkerDeployment.ServiceDeployName) = 0";

	/** 获取某一个服务在某一个服务器组中的历史部署机器 */
	public static final String SQL_SELECT_WORKER_HISTORY_SERVER_LIST = "SELECT s.ServerName FROM HA_WorkerDeployment w ,HA_WorkerPackage p,HA_WorkerDeploymentServer s WHERE w.ServerGroup = ? AND w.PackageId = p.PackageId AND w.ServiceDeployName = s.ServiceDeployName AND ServiceName = ?";

	/** 通过服务名称和群组，找到该服务所位于的服务器名称 */
	public static final String SQL_SELECT_WORKERDEPLOYMENT_SERVER = "SELECT s.ServerName FROM HA_WorkerDeployment w,HA_WorkerDeploymentServer s WHERE w.ServiceDeployName = s.ServiceDeployName AND w.ServiceDeployName = ?";

	/** 查询一个服务器已使用的内存 */
	public static final String SQL_SELECT_SERVER_FREE_MEMORY = "SELECT SUM(UsedMEMORY) ServerMemroy FROM HA_WorkerDeploymentServer WHERE ServerName = ?";

	/** 查询一个服务器组下面的全部服务器名称 */
	public static final String SQL_SELECT_SERVER_BY_SERVERGROUP = "SELECT ServerName FROM HA_ServerGroupMember WHERE ServerGroup = ?";

	/** 获取一台服务器的内存 */
	public static final String SQL_SELECT_SERVER_MEMORY_BY_SERVERNAME = "SELECT Memory FROM HA_Server WHERE ServerName = ?";

	/** 移除指定服务名称下的服务部署位置 */
	public static final String SQL_DELETE_WORKER_DEPLOYMENT_SERVER_BY_SERVICEDEPLOYNAME = "DELETE FROM HA_WorkerDeploymentServer WHERE ServiceDeployName = ?";

	/** 为指定服务添加部署位置 */
	public static final String SQL_INSERT_WORKER_DEPLOYMENT_SERVER = "INSERT INTO `HA_WorkerDeploymentServer` (`ServiceDeployName`, `ServerName`, `UsedMemory`) VALUES(?,?,?);";

	/** 通过serviceDeployName获取serviceName */
	public static final String SQL_SELECT_SERVICENAME_BY_SERVICEDEPLOYNAME = "SELECT p.ServiceName FROM HA_WorkerDeployment w,HA_WorkerPackage p WHERE w.PackageId = p.PackageId AND w.ServiceDeployName = ?";

	/** 日志对象的引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HADBDeploymentHelper.class);

	/**
	 * 通过ServiceDeployName获取对应的WorkerPackageFile信息
	 * 
	 * @param serviceName
	 * @return
	 * @throws SQLException
	 */
	public static JsonArray getWorkerPackageByServiceName(String serviceName) throws SQLException {
		LOGGER.info("Get Worker Package by serviceName.serviceName={}", serviceName);
		Database database = HADatabaseFactory.getHADatabase();
		DataTable table = database.executeTable(SQL_SELECT_WORKER_PACKAGE_BY_SERVICE_DEPLOY_NAME,
				new Object[] { serviceName });
		JsonArray jsonArray = new JsonArray();
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("PackageId", row.getInt("PackageId"));
				jsonObject.addProperty("ServiceName", row.getString("ServiceName"));
				jsonObject.addProperty("ServiceVersion", row.getString("ServiceVersion"));
				jsonObject.addProperty("UploadTime",
						DateUtil.formatDate(row.getDateTime("UploadTime"), "yyyy-MM-dd HH:mm:ss"));
				jsonObject.addProperty("PackageFile", row.getString("PackageFile"));
				jsonObject.addProperty("PackageName",
						row.getString("PackageFile") != null ? row.getString("PackageFile").split("name=")[1] : "");
				jsonObject.addProperty("UploadTime",
						DateUtil.formatDate(row.getDateTime("UploadTime"), "yyyy-MM-dd HH:mm:ss"));
				jsonObject.addProperty("Comment", row.getString("Comment"));
				jsonArray.add(jsonObject);
			}
		}
		return jsonArray;
	}

	/** 这是一个用于getSimpleWorkerPackageInfo方法的缓存 */
	private static final Map<Integer, HAPackageInfo> PACKAGE_INFO_CACHE = new HashMap<Integer, HAPackageInfo>();

	/**
	 * 通过PackageId获取HA_WorkerPackage(本方法只能获取到简化的WorkerPackage表内容)
	 * 
	 * @param packageId
	 * @return
	 * @throws SQLException
	 */
	public static HAPackageInfo getSimpleWorkerPackageInfo(int packageId) throws SQLException {
		LOGGER.info("Get {} package info.", packageId);
		HAPackageInfo packageInfo = PACKAGE_INFO_CACHE.get(packageId);
		if (packageInfo == null) {
			packageInfo = new HAPackageInfo();
			Database database = HADatabaseFactory.getHADatabase();
			DataTable table = database.executeTable(SQL_SELECT_SIMPLE_WORKERPACKAGE, new Object[] { packageId });
			if (table.getRowCount() > 0) {
				for (DataRow row : table.getRows()) {
					packageInfo.setPackageId(row.getInt("PackageId"));
					packageInfo.setPackageUrl(row.getString("PackageFile"));
					packageInfo.setServiceVersion(row.getString("ServiceVersion"));
					PACKAGE_INFO_CACHE.put(packageId, packageInfo);
					break;
				}
			}
		}
		return packageInfo;
	}

	/**
	 * 通过PackageId获取HA_WorkerPackage(本方法只能获取到简化的WorkerPackage表内容)
	 * 
	 * @param packageId
	 * @return
	 * @throws SQLException
	 */
	public static HAWorkerPackage getWorkerPackage(int packageId) throws SQLException {
		LOGGER.info("Get {} package info.", packageId);
		Database database = HADatabaseFactory.getHADatabase();
		DataTable table = database.executeTable(SQL_SELECT_WORKERPACKAGE, new Object[] { packageId });
		if (table.getRowCount() > 0) {
			HAWorkerPackage packageInfo = new HAWorkerPackage();
			for (DataRow row : table.getRows()) {
				packageInfo.setPackageId(row.getInt("PackageId"));
				packageInfo.setPackageFile(row.getString("PackageFile"));
				packageInfo.setUploadTimedatetime(row.getDateTime("UploadTime"));
				packageInfo.setUploadUser(row.getString("UploadUser"));
				packageInfo.setComment(row.getString("Comment"));
				packageInfo.setServiceName(row.getString("ServiceName"));
				packageInfo.setServiceVersion(row.getString("ServiceVersion"));
				packageInfo.setControllable(row.getBoolean("Controllable"));
				packageInfo.setServicePorts(row.getString("ServicePorts"));
				packageInfo.setMetastext(row.getString("Metas"));
				packageInfo.setStartParamsvarchar(row.getString("StartParams"));
				packageInfo.setInstances(row.getInt("Instances"));
				packageInfo.setMinMemory(row.getInt("MinMemory"));
				packageInfo.setMaxMemory(row.getInt("MaxMemory"));
				return packageInfo;
			}
		}
		return null;
	}

	/**
	 * 通过serviceDeployName获取serviceName
	 * 
	 * @param serviceDeployName
	 * @return
	 * @throws SQLException
	 */
	public static String getServiceNameByServerDeployName(String serviceDeployName) throws SQLException {
		Database database = HADatabaseFactory.getHADatabase();
		DataTable table = database.executeTable(SQL_SELECT_SERVICENAME_BY_SERVICEDEPLOYNAME,
				new Object[] { serviceDeployName });
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				return row.getString("ServiceName");
			}
		}
		return null;
	}

	/**
	 * 清理过期的服务器部署信息
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static boolean cleanExpiredWorkerDeploymentServer() throws SQLException {
		Database database = HADatabaseFactory.getHADatabase();
		database.executeNonQuery(SQL_CLEAN_EXPIRED_WORKERDEPLOYMENTSERVER);
		return true;
	}

	/**
	 * 遍历服务器组下的HA_WorkerDeploymentServer,关联HA_WorkerDeployment，
	 * 通过得到的packageId关联HA_WorkerPackage得到serveName与当前的serverName匹配
	 * 
	 * @param serviceName
	 * @param serverGroup
	 * @return
	 */
	public static Set<String> getServiceHistoryServer(String serviceName, String serverGroup) throws SQLException {
		Set<String> serverSet = new HashSet<String>();
		if (serviceName == null || serverGroup == null) {
			return serverSet;
		}
		Database database = HADatabaseFactory.getHADatabase();
		DataTable table = database.executeTable(SQL_SELECT_WORKER_HISTORY_SERVER_LIST, new Object[] { serverGroup,
				serviceName });
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				serverSet.add(row.getString("ServerName"));
			}
		}

		return serverSet;
	}

	/**
	 * 通过服务部署名称获取该服务锁部署的服务器
	 * 
	 * @param serviceDeployName
	 * @param serverGroup
	 * @return
	 * @throws SQLException
	 */
	public static Set<String> getServerByServiceDeployName(String serviceDeployName, String serverGroup)
			throws SQLException {
		Set<String> result = new HashSet<String>();
		Set<String> serverSet = new HashSet<String>();
		if (serviceDeployName == null || serverGroup == null) {
			return serverSet;
		}
		Database database = HADatabaseFactory.getHADatabase();
		DataTable table = database.executeTable(SQL_SELECT_WORKERDEPLOYMENT_SERVER, new Object[] { serviceDeployName });
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				serverSet.add(row.getString("ServerName"));
			}
		}

		Set<String> serverSetInGroup = getServerByServerGroup(serverGroup);
		// 如果该server位于组内，则返回该server，因为一个server可以位于多个组，所以才需要此处再判断一次
		for (String server : serverSet) {
			if (serverSetInGroup.contains(server)) {
				result.add(server);
			}
		}

		return result;

	}

	/**
	 * 获取某一台服务器的剩余内存
	 * 
	 * @return
	 */
	public static int getFreeMemoryByServer(String serverName) throws SQLException {
		int result = Integer.MAX_VALUE;
		Database database = HADatabaseFactory.getHADatabase();
		DataTable table = database.executeTable(SQL_SELECT_SERVER_FREE_MEMORY, new Object[] { serverName });
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				result = row.getInt("ServerMemroy");
				break;
			}
		}
		return result;
	}

	/**
	 * 获取某一台服务器的总内存
	 * 
	 * @return
	 */
	public static int getServerMemory(String serverName) throws SQLException {
		int result = Integer.MIN_VALUE;
		Database database = HADatabaseFactory.getHADatabase();
		DataTable table = database.executeTable(SQL_SELECT_SERVER_MEMORY_BY_SERVERNAME, new Object[] { serverName });
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				result = row.getInt("Memory");
				break;
			}
		}
		return result;
	}

	/**
	 * 获取某一个服务器组下的全部机器
	 * 
	 * @return
	 */
	public static Set<String> getServerByServerGroup(String serverGroup) throws SQLException {
		Set<String> serverSet = new HashSet<String>();
		Database database = HADatabaseFactory.getHADatabase();
		DataTable table = database.executeTable(SQL_SELECT_SERVER_BY_SERVERGROUP, new Object[] { serverGroup });
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				serverSet.add(row.getString("ServerName"));
			}
		}
		return serverSet;
	}

	public static void saveWorkerDeploymentServer(String serviceDeployName, Collection<String> serverList,
			int needMemory) throws SQLException {
		LOGGER.info(String.format(
				"Save HA_WorkerDeploymentServer, serviceDeployName is %s, serverSet is %s, need memory is %s",
				serviceDeployName, serverList, needMemory));
		Database database = HADatabaseFactory.getHADatabase();
		// 1. clean HA_WorkerDeploymentServer
		database.executeNonQuery(SQL_DELETE_WORKER_DEPLOYMENT_SERVER_BY_SERVICEDEPLOYNAME,
				new Object[] { serviceDeployName });
		// 2. save HA_WorkerDeploymentServer
		for (String server : serverList) {
			database.executeNonQuery(SQL_INSERT_WORKER_DEPLOYMENT_SERVER, new Object[] { serviceDeployName, server,
					needMemory });
		}
	}
}
