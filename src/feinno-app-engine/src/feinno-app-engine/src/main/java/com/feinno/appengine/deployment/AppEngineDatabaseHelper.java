/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-8-30
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.deployment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;

/**
 * 
 * 所有数据库操作相关代码
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineDatabaseHelper {
	private static Database faedb = DatabaseManager.getDatabase("FAEDB");
	private static Database hadb = DatabaseManager.getDatabase("HADB");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppEngineDatabaseHelper.class);

	public static DataTable readApplications() throws SQLException {
		String sql = "SELECT * from FAE_Application";

		return faedb.executeTable(sql);
	}

	public static DataTable readUndeployedAppBeans() throws SQLException {
		String sql = "SELECT MAX(bean.BeanId) AS BeanId, bean.AppCategory, bean.AppName,bean.Annotations,bean.PackageId "
				+ "FROM FAE_AppBean bean LEFT JOIN FAE_Application app "
				+ "ON bean.AppCategory = app.AppCategory AND bean.AppName = app.AppName " + "WHERE app.BeanId IS NULL "
				+ "GROUP BY bean.AppName, bean.AppCategory";
		return faedb.executeTable(sql);
	}

	public static String generateDeployServiceName(int packageId, String serverGroup) throws SQLException {
		String sql1 = "SELECT max(BeanId) FROM FAE_Application where PackageId = ? "
				+ "and serverGroup = ? and WorkerInstance = ?";

		String sql2 = "SELECT * FROM HA_WorkerPackage where PackageId = ?";

		DataTable table = faedb.executeTable(sql1, packageId, serverGroup, "");
		int maxBeanId = table.getRow(0).getInt("max(BeanId)");

		DataTable table2 = hadb.executeTable(sql2, packageId);
		String serviceTarName = table2.getRow(0).getString("ServiceName");

		String serviceName = serviceTarName + "-" + maxBeanId;
		return serviceName;
	}

	public static void saveDeployment(String serviceName, int packageId, String serverGroup) throws SQLException {
		// String sql3 = "INSERT INTO HA_WorkerDeployment " +
		// "(ServiceDeployName, PackageId, ServerGroup, WorkerUpdateMode, WorkerStartMode,"
		// +
		// "ServiceTag, PortsSettings, StartParams, Settings) VALUES " +
		// "(?,?,?,'AUTO','AUTO','FAE','',?,?) " +
		// "ON DUPLICATE KEY UPDATE " +
		// "PackageId=?,ServerGroup=?,StartParams=?,Settings=?";
		// hadb.executeNonQuery(sql3, serviceName, packageId, serverGroup, "",
		// "", packageId, serverGroup, "", "");
		
		String startParams = "";
		String paramSQL = "SELECT StartParams from HA_WorkerPackage where PackageId=?";
		try {
			DataTable table = hadb.executeTable(paramSQL, packageId);
			if (table != null && table.getRows().size() > 0) {
				startParams = table.getRow(0).getString("StartParams");
			}
		} catch (Exception e) {
			LOGGER.error("GET StartParams by HA_WorkerPackage failed.", e);
		}
		
		String sql3 = "REPLACE INTO HA_WorkerDeployment "
				+ "(ServiceDeployName, PackageId, ServerGroup, WorkerUpdateMode, WorkerStartMode,"
				+ "ServiceTag, PortsSettings, StartParams, Settings) VALUES "
				+ "(?,?,?,'AUTO','AUTO','FAE','',?,?) ";
		hadb.executeNonQuery(sql3, serviceName, packageId, serverGroup, startParams, "");
	}

	public static void deployAppBean(int beanId, String serviceName) throws SQLException {
		String sql = "UPDATE FAE_Application SET ServiceDeployName = ?,DeployStatus='DEPLOYING' WHERE beanId = ?";
		faedb.executeNonQuery(sql, serviceName, beanId);
	}

	public static void syncAppBean(int beanId) throws Exception {
		String sql = "UPDATE FAE_Application SET ZookeeperAppId=?,DeployStatus='RUNNING' WHERE beanId = ?";
		faedb.executeNonQuery(sql, beanId, beanId);
	}
	
	public static boolean isRunService(String serviceDeployName) throws Exception {
		String sql = "select count(*) from FAE_Application where ServiceDeployName=?";
		DataTable table = faedb.executeTable(sql, serviceDeployName);
		long count = Long.valueOf(table.getRow(0).getObject(1).toString());
		return count > 0;
	}

	public static void removeInvalidWorkerDeployment() throws Exception {
		String sql1 = "SELECT ServiceDeployName FROM FAE_Application ";//WHERE Enabled = true
		StringBuffer sqlBuffer = new StringBuffer("DELETE FROM HA_WorkerDeployment WHERE ServiceTag = 'FAE' AND  ServiceDeployName NOT IN (");
		List<String> paramList = new ArrayList<String>();
		DataTable table = faedb.executeTable(sql1);
		for (DataRow row : table.getRows()) {
			sqlBuffer.append("?,");
			paramList.add(row.getString("ServiceDeployName"));
		}
		if (paramList.size() > 0) {
			sqlBuffer.deleteCharAt(sqlBuffer.length()-1);
			sqlBuffer.append(")");
			Object[] params = new Object[paramList.size()];
			paramList.toArray(params);
			hadb.executeNonQuery(sqlBuffer.toString(), params);
		}
	}
}
