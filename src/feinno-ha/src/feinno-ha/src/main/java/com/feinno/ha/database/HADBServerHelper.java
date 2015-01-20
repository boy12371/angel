package com.feinno.ha.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.database.Database;

/**
 * 
 * <b>描述: </b>该类是用于HA_Server的数据库操作的辅助类
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class HADBServerHelper {

	/** 通过Server获得对应的Group */
	public static final String SQL_SELECT_SERVER_GROUP = "SELECT ServerGroup FROM HA_ServerGroupMember WHERE ServerName = ? ";

	public static final String SQL_SELECT_ALL_SERVER_GROUP = "SELECT GroupName FROM HA_ServerGroup";

	public static final String SQL_SELECT_SERVER_BY_GROUP_NAME = "SELECT ServerName FROM HA_ServerGroupMember WHERE ServerGroup = ?";

	/** 日志对象的引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HADBServerHelper.class);

	/**
	 * 获取serverGroup列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getServerGroupList() throws SQLException {
		LOGGER.info("Get Server Group List ");
		List<String> serverGroupList = new ArrayList<String>();
		Database database = HADatabaseFactory.getHADatabase();
		DataTable table = database.executeTable(SQL_SELECT_ALL_SERVER_GROUP);
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				serverGroupList.add(row.getString("GroupName"));
			}
		}
		return serverGroupList;
	}

	/**
	 * 通过ServerName去的该Server对应的Group
	 * 
	 * @param serverName
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getServerGroupList(String serverName) throws SQLException {
		LOGGER.info("Get Server Group List , server = {}", serverName);
		List<String> serverGroupList = new ArrayList<String>();
		Database database = HADatabaseFactory.getHADatabase();
		DataTable table = database.executeTable(SQL_SELECT_SERVER_GROUP, new Object[] { serverName });
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				serverGroupList.add(row.getString("ServerGroup"));
			}
		}
		return serverGroupList;
	}

	/**
	 * 通过serverGroupName获得该group下的全部server
	 * 
	 * @param serverGroupName
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getServerListByGroupName(String serverGroupName) throws SQLException {
		LOGGER.info("Get server list by {} group .", serverGroupName);
		List<String> serverGroupList = new ArrayList<String>();
		Database database = HADatabaseFactory.getHADatabase();
		DataTable table = database.executeTable(SQL_SELECT_SERVER_BY_GROUP_NAME, new Object[] { serverGroupName });
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				serverGroupList.add(row.getString("ServerName"));
			}
		}
		return serverGroupList;
	}
}
