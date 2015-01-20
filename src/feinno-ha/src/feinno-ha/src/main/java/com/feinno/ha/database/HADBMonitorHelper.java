package com.feinno.ha.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.ha.interfaces.monitor.HAMonitorDeployment;
import com.feinno.ha.interfaces.monitor.HAMonitorKey;
import com.feinno.ha.interfaces.monitor.HAMonitorScript;
import com.feinno.ha.interfaces.monitor.HAObserverDeployment;

/**
 * 
 * <b>描述: </b>该类是用于HADB-Monitor: 监控及报警相关数据的数据库操作的辅助类
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class HADBMonitorHelper {

	/** 通过ServerGroup获得对应的HA_ObserverReportDeployment数据 */
	public static final String SQL_SELECT_OBSERVER_REPORT_DEPLOYMENT = "SELECT ObserverName,ServerGroup,WorkerCategory,ReportMode,`Interval`,Upload,ThresholdExpr FROM HA_ObserverReportDeployment WHERE ServerGroup in (?) ";

	/** 通过ServerGroup获得对应的HA_MonitorDeployment数据 */
	public static final String SQL_SELECT_Monitor_DEPLOYMENT = "SELECT KeyId,`Interval`,Upload,ThresholdMin,ThresholdMax FROM HA_MonitorDeployment WHERE ServerGroup in (?) ";

	/** 通过KeyId获得对应的HA_MonitorKeyConfig数据 */
	public static final String SQL_SELECT_MONITOR_KEY_CONFIG = "SELECT KeyId,KeyType,ScriptName,ScriptResultOrder FROM HA_MonitorKeyConfig WHERE KeyId in (?) ";

	/** 通过ScriptName获得对应的HA_MonitorScript数据 */
	public static final String SQL_SELECT_MONITOR_Script = "SELECT ScriptName,ScriptType,ScriptText FROM HA_MonitorScript WHERE ScriptName in (?) ";

	/** 日志对象的引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HADBMonitorHelper.class);

	/**
	 * 通过ServerGroup取出
	 * 
	 * @param serverGroupList
	 * @return
	 */
	public static List<HAObserverDeployment> getHAObserverReportDeployments(List<String> serverGroupList)
			throws SQLException {
		LOGGER.info("Get Observer Report Deployment. server group in ({}) ", serverGroupList);
		// 1. 拼接条件参数
		StringBuffer groupParams = new StringBuffer();
		if (serverGroupList != null) {
			for (String group : serverGroupList) {
				groupParams.append("'").append(group).append("',");
			}
		}
		groupParams.append("'*'");// 默认要追加一个 *
		Database database = HADatabaseFactory.getHADatabase();
		List<HAObserverDeployment> haObserverDeployments = new ArrayList<HAObserverDeployment>();
		String sql = SQL_SELECT_OBSERVER_REPORT_DEPLOYMENT.replaceAll("[?]", groupParams.toString());
		DataTable table = database.executeTable(sql);
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				HAObserverDeployment haObserverDeployment = new HAObserverDeployment();
				haObserverDeployment.setObserverName(row.getString("ObserverName"));
				haObserverDeployment.setReportMode(row.getString("ReportMode"));
				haObserverDeployment.setInterval(row.getInt("Interval"));
				haObserverDeployment.setUpload("Y".equals(row.getString("Upload")) ? true : false);
				haObserverDeployment.setThresholdExpr(row.getString("ThresholdExpr"));
				haObserverDeployments.add(haObserverDeployment);
			}
		}
		return haObserverDeployments;
	}

	/**
	 * 通过ServerGroup取出
	 * 
	 * @param serverGroupList
	 * @return
	 */
	public static List<HAMonitorDeployment> getHAMonitorDeployments(List<String> serverGroupList) throws SQLException {
		LOGGER.info("Get Monitor Deployment. server group in ({}) ", serverGroupList);
		// 1. 拼接条件参数
		StringBuffer groupParams = new StringBuffer();
		if (serverGroupList != null) {
			for (String group : serverGroupList) {
				groupParams.append("'").append(group).append("',");
			}
		}
		groupParams.append("'*'");// 默认要追加一个 *
		List<HAMonitorDeployment> haMonitorDeployments = new ArrayList<HAMonitorDeployment>();
		Database database = HADatabaseFactory.getHADatabase();
		String sql = SQL_SELECT_Monitor_DEPLOYMENT.replaceAll("[?]", groupParams.toString());
		DataTable table = database.executeTable(sql);
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				HAMonitorDeployment haMonitorDeployment = new HAMonitorDeployment();
				haMonitorDeployment.setKeyId(row.getInt("KeyId"));
				haMonitorDeployment.setInterval(row.getInt("Interval"));
				haMonitorDeployment.setUpload("Y".equals(row.getString("Upload")) ? true : false);
				haMonitorDeployment.setThresholdMin(row.getFloat("ThresholdMin"));
				haMonitorDeployment.setThresholdMax(row.getFloat("ThresholdMax"));
				haMonitorDeployments.add(haMonitorDeployment);
			}
		}
		return haMonitorDeployments;
	}

	/**
	 * 通过HAMonitorDeployment中的KeyId,获取对应的HAMonitorKey
	 * 
	 * @param haMonitorDeployments
	 * @return
	 * @throws SQLException
	 */
	public static List<HAMonitorKey> getHAMonitorKeys(List<HAMonitorDeployment> haMonitorDeployments)
			throws SQLException {
		// 1. 拼接条件参数
		StringBuffer keyParams = new StringBuffer();
		if (haMonitorDeployments != null) {
			for (HAMonitorDeployment haMonitorDeployment : haMonitorDeployments) {
				keyParams.append(haMonitorDeployment.getKeyId()).append(",");
			}
		}
		if (keyParams.length() > 0) {
			keyParams.deleteCharAt(keyParams.length() - 1);
		}
		List<HAMonitorKey> haMonitorKeys = new ArrayList<HAMonitorKey>();
		if (keyParams.length() > 0) {
			Database database = HADatabaseFactory.getHADatabase();
			String sql = SQL_SELECT_MONITOR_KEY_CONFIG.replaceAll("[?]", keyParams.toString());
			DataTable table = database.executeTable(sql);
			if (table.getRowCount() > 0) {
				for (DataRow row : table.getRows()) {
					HAMonitorKey haMonitorKey = new HAMonitorKey();
					haMonitorKey.setKeyId(row.getInt("KeyId"));
					haMonitorKey.setKeyType(row.getString("KeyType"));
					haMonitorKey.setScriptName(row.getString("ScriptName"));
					haMonitorKey.setScriptResultOrder(row.getInt("ScriptResultOrder"));
					haMonitorKeys.add(haMonitorKey);
				}
			}
		}

		return haMonitorKeys;
	}

	/**
	 * 通过HAMonitorKey中的ScriptName获取对应的HAMonitorScript
	 * 
	 * @param haMonitorKeys
	 * @return
	 * @throws SQLException
	 */
	public static List<HAMonitorScript> getHAMonitorScript(List<HAMonitorKey> haMonitorKeys) throws SQLException {
		// 1. 拼接条件参数
		StringBuffer nameParams = new StringBuffer();
		if (haMonitorKeys != null) {
			for (HAMonitorKey haMonitorKey : haMonitorKeys) {
				nameParams.append("'").append(haMonitorKey.getScriptName()).append("',");
			}
		}
		if (nameParams.length() > 0) {
			nameParams.deleteCharAt(nameParams.length() - 1);
		}
		List<HAMonitorScript> haMonitorScripts = new ArrayList<HAMonitorScript>();
		if (nameParams.length() > 0) {
			Database database = HADatabaseFactory.getHADatabase();
			String sql = SQL_SELECT_MONITOR_Script.replaceAll("[?]", nameParams.toString());
			DataTable table = database.executeTable(sql);
			if (table.getRowCount() > 0) {
				for (DataRow row : table.getRows()) {
					HAMonitorScript haMonitorScript = new HAMonitorScript();
					haMonitorScript.setScriptName(row.getString("ScriptName"));
					haMonitorScript.setScriptMode(row.getString("ScriptType"));
					haMonitorScript.setScriptText(row.getString("ScriptText"));
					haMonitorScripts.add(haMonitorScript);
				}
			}
		}
		return haMonitorScripts;
	}

}
