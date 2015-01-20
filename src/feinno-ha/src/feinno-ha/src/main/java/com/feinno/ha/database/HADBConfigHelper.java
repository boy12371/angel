package com.feinno.ha.database;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigParams;
import com.feinno.configuration.ConfigType;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.Configurator;
import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.ha.interfaces.configuration.HAConfigTableBuffer;
import com.feinno.ha.interfaces.configuration.HAConfigTableRow;
import com.feinno.ha.interfaces.configuration.HAConfigTextBuffer;
import com.feinno.serialization.protobuf.util.TwoTuple;

/**
 * 
 * <b>描述: </b>数据库中配置表处理的帮助类，用来辅助完成获取及解析配置表的信息
 * <p>
 * <b>功能: </b>用来辅助完成获取及解析配置表的信息
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class HADBConfigHelper {

	/** 这个一般给BaseCenter用，当在当前中没有找到数据时，会使用这个配置加载器进行配置加载 */
	public static Configurator baseCenterConfigurator;

	/** 获取配置表中文本记录 */
	public static final String SQL_SELECT_CONFIG_TEXT = "SELECT ConfigKey, ConfigParams,ConfigText,Version FROM HA_ConfigTexts WHERE ConfigKey = ?";

	/** 获取FAE配置表中文本记录 */
	public static final String SQL_SELECT_FAE_CONFIG_TEXT = "SELECT ConfigKey, ConfigParam as ConfigParams,ConfigText,Version FROM FAE_ConfigText WHERE ConfigKey = ?";

	/** 更新最后获取时间 */
	public static final String SQL_UPDATE_CONFIG_LASTTIME = "UPDATE HA_ActiveConfig  SET LastReadTime = ? , LastError = '' WHERE ServerName = ? AND WorkerPid = ? AND ConfigType = ? AND ConfigKey = ? AND ConfigParams = ? ";

	/** 插入版本号 */
	public static final String SQL_INSERT_CONFIG_VERSION = "INSERT INTO HA_ActiveConfig  (ServerName,WorkerPid,ConfigType,ConfigKey,ConfigParams,LastReadTime,LastVersion,IsSubscribed,LastError) VALUES (?,?,?,?,?,?,?,?,?)";

	/** 找到已经过期的配置订阅信息 */
	public static final String SQL_SELECT_EXPIRED_CONFIG_LASTTIME = "SELECT a.ServerName,a.WorkerPid FROM HA_ActiveConfig a left join HA_WorkerEndpoint b on a.ServerName = b.ServerName AND a.WorkerPid = b.WorkerPid where b.WorkerPid is null ";

	/** 更新最后获取时间 */
	public static final String SQL_UPDATE_CONFIG_VERSION = "UPDATE HA_ActiveConfig  SET LastVersion = ? WHERE ServerName = ? AND WorkerPid = ? AND ConfigType = ? AND ConfigKey = ? AND ConfigParams = ?";

	/** 更新最后一次出错内容 */
	public static final String SQL_UPDATE_CONFIG_LASTERROR = "UPDATE HA_ActiveConfig  SET LastError = ? WHERE ServerName = ? AND WorkerPid = ? AND ConfigType = ? AND ConfigKey = ? AND ConfigParams = ?";

	/** 订阅自动推送 */
	public static final String SQL_UPDATE_CONFIG_SUBSCRIBE = "UPDATE HA_ActiveConfig  SET IsSubscribed = ? WHERE ServerName = ? AND WorkerPid = ? AND ConfigType = ? AND ConfigKey = ? AND ConfigParams = ?";

	/** 获得Table配置不同版本的记录 */
	public static final String SQL_GET_CONFIG_DIFF_TABLE_VERSION_ = "SELECT a.ServerName,a.WorkerPid,a.ConfigType,a.ConfigKey,a.ConfigParams,a.LastReadTime,a.LastVersion,a.IsSubscribed,a.LastError FROM HA_ActiveConfig a,HA_ConfigTables b WHERE a.ConfigKey = b.TableName and a.LastVersion <> b.Version and a.IsSubscribed = true ";

	/** 或得已订阅的Text类型的配置记录 */
	public static final String SQL_GET_SUBSCRIBE_TEXT = "SELECT a.ServerName,a.WorkerPid,a.ConfigType,a.ConfigKey,a.ConfigParams,a.LastReadTime,a.LastVersion,a.IsSubscribed,a.LastError FROM HA_ActiveConfig a WHERE a.IsSubscribed = true and a.ConfigType in ('UNKOWN','TEXT') ";

	/** 删除某台服务器的订阅记录 */
	public static final String SQL_DELETE_EXPIRED_ACTIVE_CONFIG = "delete from HA_ActiveConfig WHERE ServerName = ? AND WorkerPid = ? ";

	/** 日志对象的引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HADBConfigHelper.class);

	// 初始化BaseDB
	static {
		try {
			Database database = HADatabaseFactory.getHADatabase();
			DataTable baseDataTable = database.executeTable(SQL_SELECT_CONFIG_TEXT,
					new Object[] { "BaseDB.properties" });
			if (baseDataTable.getRowCount() > 0) {
				for (DataRow row : baseDataTable.getRows()) {
					ConfigParams params = new ConfigParams(row.getString("ConfigParams"));
					ConfigParams faeDBParams = new ConfigParams("table=FAEDB");
					ConfigParams haDBParams = new ConfigParams("table=IICHADB");
					if (params.match(faeDBParams) > 0) {
						Properties properties = new Properties();
						properties.load(ConfigurationManager.convertToStream(row.getString("ConfigText")));
						HADatabaseFactory.getDatabase(HADatabaseFactory.DATABASE_BASE_FAEDB_NAME, properties);
					} else if (params.match(haDBParams) > 0) {
						Properties properties = new Properties();
						properties.load(ConfigurationManager.convertToStream(row.getString("ConfigText")));
						HADatabaseFactory.getDatabase(HADatabaseFactory.DATABASE_BASE_HADB_NAME, properties);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Init BaseDB failed", e);
		}
	}

	/**
	 * 该方法用于LoadConfigTable时使用，它可以获取指定的表所位于的数据库名称，并取得该表当前的版本号，
	 * 放入传入的tableBuffer参数中<br>
	 * 
	 * @param database
	 * @param path
	 * @param tableBuffer
	 * @return
	 * @throws SQLException
	 */
	public static String getDatabaseName(String path, HAConfigTableBuffer tableBuffer) throws SQLException {
		try {
			Database database = HADatabaseFactory.getHADatabase();
			Database baseDB = DatabaseManager.getDatabase(HADatabaseFactory.DATABASE_BASE_HADB_NAME);
			String[] params = new String[] { "@TableName" };

			// 调用存储过程，获得某一表所位于的数据库名称以及版本号
			DataTable cfTable = database != null ? database.spExecuteTable("USP_GetConfigTable", params, path) : null;
			DataTable baseCFTable = baseDB != null ? baseDB.spExecuteTable("USP_GetConfigTable", params, path) : null;

			if ((cfTable == null || cfTable.getRowCount() <= 0)) {
				if (baseCFTable != null && baseCFTable.getRowCount() > 0) {
					cfTable = baseCFTable;
				} else {
					LOGGER.error("GetDatabaseName failed", new RuntimeException("Not found " + path
							+ " by ConfigTables"));
					return null;
				}
			}
			// 以两个时间最新的为准
			if (baseCFTable != null && baseCFTable.getRowCount() > 0) {
				if (baseCFTable.getRow(0).getTimestamp("Version").after(cfTable.getRow(0).getTimestamp("Version"))) {
					tableBuffer.setVersion(baseCFTable.getRow(0).getTimestamp("Version"));
				} else {
					tableBuffer.setVersion(cfTable.getRow(0).getTimestamp("Version"));
				}
			} else {
				tableBuffer.setVersion(cfTable.getRow(0).getTimestamp("Version"));
			}
			tableBuffer.setTableName(path);
			String dbName = cfTable.getRow(0).getString("DatabaseName");
			return dbName;
		} catch (Exception e) {
			LOGGER.error("GetDatabaseName failed", new RuntimeException("Not found " + path + " by ConfigTables"));
			return null;
		}

	}

	/**
	 * 该方法从指定数据库中获取某一张表的全部数据，将数据放入传入的tableBuffer中返回给调用者
	 * 
	 * @param database
	 * @param path
	 * @param params
	 * @param tableBuffer
	 * @return
	 * @throws SQLException
	 */
	public static HAConfigTableBuffer getConfigTable(Database database, String path, HAConfigTableBuffer tableBuffer)
			throws SQLException {
		DataTable table = database.spExecuteTable("USP_LoadConfigTable", new String[] { "@TableName" }, path);
		List<String> cols = new ArrayList<String>(table.getColumnCount());
		for (int i = 0; i < table.getColumnCount(); i++) {
			cols.add(table.getColumn(i + 1).getColumnName());
		}
		tableBuffer.setColumns(cols);
		List<HAConfigTableRow> rows = new ArrayList<HAConfigTableRow>(table.getRowCount());
		for (int i = 0; i < table.getRowCount(); i++) {
			HAConfigTableRow row = new HAConfigTableRow();
			DataRow dr = table.getRow(i);
			ArrayList<String> vals = new ArrayList<String>(table.getColumnCount());
			for (int j = 0; j < table.getColumnCount(); j++) {
				if (dr.getObject(j + 1) == null)
					vals.add("");
				else {
					if (dr.getObject(j + 1).toString().indexOf("ClobImpl") >= 0) {
						Clob clob = ((Clob) dr.getObject(j + 1));
						String s = clob.getSubString(1, (int) clob.length());
						vals.add(s);
					} else
						vals.add(dr.getObject(j + 1).toString());
				}
			}
			row.setValues(vals);
			rows.add(row);
		}
		tableBuffer.setRows(rows);
		return tableBuffer;

	}

	/**
	 * 获得变化版本的ActiveConfig记录
	 * 
	 * @param database
	 * @return
	 * @throws SQLException
	 * @throws ConfigurationException
	 */
	public static List<ActiveConfigBean> getChangeVersionActiveConfig() throws SQLException, ConfigurationException {
		// 1.获取Version版本号不同的Table类型的配置信息(因为Table没有特例化配置params,因此单纯的联合查询既可)
		Database database = HADatabaseFactory.getHADatabase();
		List<ActiveConfigBean> activeConfigList = new ArrayList<ActiveConfigBean>();
		DataTable table = database.executeTable(SQL_GET_CONFIG_DIFF_TABLE_VERSION_, new Object[] {});
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				ActiveConfigBean temp = new ActiveConfigBean();
				temp.setServerName(row.getString("ServerName"));
				temp.setWorkerPid(row.getInt("WorkerPid"));
				temp.setConfigtype(ConfigType.valueOf(row.getString("ConfigType")));
				temp.setConfigKey(row.getString("ConfigKey"));
				temp.setConfigParams(row.getString("ConfigParams"));
				temp.setLastReadTime(row.getDateTime("LastReadTime"));
				temp.setLastVersion(row.getDateTime("LastVersion"));
				temp.setSubscribed((Boolean) row.getObject("IsSubscribed"));
				temp.setLastError(row.getString("LastError"));
				activeConfigList.add(temp);
			}
		}

		// 2.获取Version版本号不同的Text类型的配置信息(需要重新计算params的匹配度，因此无法使用联合查询)
		table = database.executeTable(SQL_GET_SUBSCRIBE_TEXT, new Object[] {});
		if (table.getRowCount() > 0) {
			for (DataRow row : table.getRows()) {
				HAConfigTextBuffer textBuffer = getConfigText(row.getString("ConfigKey"),
						new ConfigParams(row.getString("ConfigParams")));
				// 如果activeConfig中某一条的记录的版本号与最新版本号不同，那么将本条记录加入到集合中
				if (!textBuffer.getVersion().equals(row.getDateTime("LastVersion"))) {
					ActiveConfigBean temp = new ActiveConfigBean();
					temp.setServerName(row.getString("ServerName"));
					temp.setWorkerPid(row.getInt("WorkerPid"));
					temp.setConfigtype(ConfigType.valueOf(row.getString("ConfigType")));
					temp.setConfigKey(row.getString("ConfigKey"));
					temp.setConfigParams(row.getString("ConfigParams"));
					temp.setLastReadTime(row.getDateTime("LastReadTime"));
					temp.setLastVersion(row.getDateTime("LastVersion"));
					temp.setSubscribed((Boolean) row.getObject("IsSubscribed"));
					temp.setLastError(row.getString("LastError"));
					activeConfigList.add(temp);
				}
			}
		}

		return activeConfigList;
	}

	/**
	 * 更新最后一次读取时间
	 * 
	 * @param serverName
	 * @param workerPid
	 * @param type
	 * @param path
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static boolean updateLastReadTime(String serverName, int workerPid, ConfigType type, String path,
			String params) throws SQLException {
		Database database = HADatabaseFactory.getHADatabase();
		Object[] updateParams = new Object[] { new java.util.Date(), serverName, workerPid, type.toString(), path,
				params };
		int code = database.executeNonQuery(SQL_UPDATE_CONFIG_LASTTIME, updateParams);
		if (code > 0) {
			// 如果有值被成功更新，那么直接返回成功
			return true;
		}
		// 否则执行插入
		Object[] insertParams = new Object[] { serverName, workerPid, type.toString(), path, params,
				new java.util.Date(), new java.util.Date(), 0, "" };
		code = database.executeNonQuery(SQL_INSERT_CONFIG_VERSION, insertParams);
		return code > 0;
	}

	/**
	 * 删除过期的订阅内容
	 * 
	 * @throws SQLException
	 */
	public static boolean removeExpiredActiveConfig() throws SQLException {
		try {
			LOGGER.info("Remove expired HA_ActiveConfig.");
			Database database = HADatabaseFactory.getHADatabase();
			DataTable table = database.executeTable(SQL_SELECT_EXPIRED_CONFIG_LASTTIME);
			for (DataRow row : table.getRows()) {
				try {
					String serverName = row.getString("ServerName");
					int workerPid = row.getInt("WorkerPid");
					database.executeNonQuery(SQL_DELETE_EXPIRED_ACTIVE_CONFIG, serverName, workerPid);
				} catch (Exception e) {
					LOGGER.error("removeExpiredActiveConfig failed.", e);
				}

			}
			return true;
		} catch (Exception e) {
			LOGGER.error("removeExpiredActiveConfig failed. ", e);
			return false;
		}

	}

	/**
	 * 更新最后一次版本时间
	 * 
	 * @param serverName
	 * @param workerPid
	 * @param type
	 * @param path
	 * @param params
	 * @param dateTime
	 * @return
	 * @throws SQLException
	 */
	public static boolean updateVersion(String serverName, int workerPid, ConfigType type, String path, String params,
			Date version) throws SQLException {
		Database database = HADatabaseFactory.getHADatabase();
		Object[] updateParams = new Object[] { new java.util.Date(version.getTime()), serverName, workerPid,
				type.toString(), path, params };
		int code = database.executeNonQuery(SQL_UPDATE_CONFIG_VERSION, updateParams);
		if (code > 0) {
			// 如果有值被成功更新，那么直接返回成功
			return true;
		}
		// 否则执行插入
		Object[] insertParams = new Object[] { serverName, workerPid, type.toString(), path, params,
				new java.util.Date(), new java.util.Date(version.getTime()), 0, "" };
		code = database.executeNonQuery(SQL_INSERT_CONFIG_VERSION, insertParams);
		return code > 0;
	}

	/**
	 * 更新最后一次出错时间<br>
	 * 该时间可以为空，为空代表最终更新成功了
	 * 
	 * @param serverName
	 * @param workerPid
	 * @param type
	 * @param path
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static boolean updateLastError(String serverName, int workerPid, ConfigType type, String path,
			String params, String lastError) {
		Database database = HADatabaseFactory.getHADatabase();
		Object[] updateParams = new Object[] { lastError, serverName, workerPid, type.toString(), path, params };
		try {
			int code = database.executeNonQuery(SQL_UPDATE_CONFIG_LASTERROR, updateParams);
			return code > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 更新最后一次版本时间
	 * 
	 * @param serverName
	 * @param workerPid
	 * @param type
	 * @param path
	 * @param params
	 * @param dateTime
	 * @return
	 * @throws SQLException
	 */
	public static boolean subscribeConfig(boolean isSubscribe, String serverName, int workerPid, ConfigType type,
			String path, String params) throws SQLException {
		Database database = HADatabaseFactory.getHADatabase();
		Object[] updateParams = new Object[] { isSubscribe ? 1 : 0, serverName, workerPid, type.toString(), path,
				params };
		int code = database.executeNonQuery(SQL_UPDATE_CONFIG_SUBSCRIBE, updateParams);
		return code > 0;
	}

	/**
	 * 从指定的数据库中获取文本类型的配置记录
	 * 
	 * @param database
	 * @param path
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws ConfigurationException
	 */
	public static HAConfigTextBuffer getConfigText(final String path, final ConfigParams params) throws SQLException,
			ConfigurationException {
		Database database = HADatabaseFactory.getHADatabase();
		DataTable dataTable = null;
		dataTable = database.executeTable(SQL_SELECT_CONFIG_TEXT, new Object[] { path });

		// 如果从数据库中取出多条记录，则对每条记录根据匹配度进行排序，选择匹配度最高的记录
		if (dataTable != null && dataTable.getRowCount() > 0) {
			return configTextFilter(dataTable, path, params);
		} else {
			LOGGER.warn("ConfigText is  no record,Try BaseHADB. key:{},params:{}", path, params);
			Database baseDB = DatabaseManager.getDatabase(HADatabaseFactory.DATABASE_BASE_FAEDB_NAME);
			if (baseDB != null) {
				String sql = SQL_SELECT_FAE_CONFIG_TEXT;
				dataTable = baseDB.executeTable(sql, new Object[] { path });
				if (dataTable != null && dataTable.getRowCount() > 0) {
					return configTextFilter(dataTable, path, params);
				}
			} else {
				LOGGER.warn("BaseDB is null.");
			}
		}
		HAConfigTextBuffer textBuffer = getConfigTextByBaseCenter(path, params);
		if (textBuffer != null) {
			return textBuffer;
		}
		LOGGER.error("Invoke getDBConfigText function. No record. key:{},params:{}", path, params);
		throw new ConfigurationException(String.format("invoke getDBConfigText function. No record. key:%s,params:%s",
				path, params));
	}

	/**
	 * 从BaseCenter中获取Text类型的数据
	 * 
	 * @param path
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws ConfigurationException
	 */
	public static HAConfigTextBuffer getConfigTextByBaseCenter(final String path, final ConfigParams params)
			throws SQLException, ConfigurationException {
		if (baseCenterConfigurator == null) {
			LOGGER.warn("Base Center Configurator is empty.");
			return null;
		}
		LOGGER.info("Try to get the config text from Base-Center");
		try {
			HAConfigTextBuffer textBuffer = ConfigurationManager.loadConfig(baseCenterConfigurator, ConfigType.TEXT,
					path, params, null);
			if (textBuffer != null) {
				return textBuffer;
			}
		} catch (Exception e) {
			LOGGER.info("Try to get the config text by Base-Center failed", e);
			throw new ConfigurationException("Try to get the config text by Base-Center failed.",e);
		}
		LOGGER.info("No record in Base-Center. key:{},params:{}", path, params);
		return null;
	}

	/**
	 * 从BaseCenter中获取Table类型数据
	 * 
	 * @param path
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws ConfigurationException
	 */
	public static HAConfigTableBuffer getConfigTableByBaseCenter(final String path, final ConfigParams params)
			throws SQLException, ConfigurationException {
		if (baseCenterConfigurator == null) {
			LOGGER.warn("Base Center Configurator is empty.");
			return null;
		}
		LOGGER.info("Try to get the config table by Base-Center");
		try {
			HAConfigTableBuffer tableBuffer = ConfigurationManager.loadConfig(baseCenterConfigurator, ConfigType.TABLE,
					path, params, null);
			if (tableBuffer != null) {
				return tableBuffer;
			}
		} catch (Exception e) {
			LOGGER.info("Try to get the config table by Base-Center failed", e);
			throw new ConfigurationException("Try to get the config table by Base-Center failed.",e);
		}
		LOGGER.info("No record in Base-Center. key:{},params:{}", path, params);
		return null;
	}

	/**
	 * 文本配置类型的过滤，经过过滤后，仅取出最符合条件的一条文本记录
	 * 
	 * @param dataTable
	 * @param path
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws ConfigurationException
	 */
	private static HAConfigTextBuffer configTextFilter(DataTable dataTable, final String path, final ConfigParams params)
			throws SQLException, ConfigurationException {

		// LOGGER.info("Found {} result", dataTable.getRowCount());

		// 进行数据整理，将每一条符合条件的记录组成一个二元组，将多个二元组放入List中，用于下阶段的排序
		List<TwoTuple<ConfigParams, HAConfigTextBuffer>> resultList = sortConfigText(dataTable, params);

		// 取出匹配度最高的记录
		TwoTuple<ConfigParams, HAConfigTextBuffer> result = resultList.get(0);

		// 如果匹配度最高的记录，它的匹配度高于了1个，那么直接使用此条记录作为最终结果
		if (params != null && params.matchRegex(result.getFirst()) > 0) {
			// 经过排序后，确实有命中的，那么返回该条结果
			return result.getSecond();
		} else {
			// 如果匹配度最高的记录的命中率都是0，说明没有一个记录被命中上，那么遍历全部记录，寻找默认选项，默认项是params字段为空的那条记录
			for (TwoTuple<ConfigParams, HAConfigTextBuffer> twoTupleTemp : resultList) {
				if (twoTupleTemp.getFirst().keySet().size() == 0) {
					return twoTupleTemp.getSecond();
				}
			}
			// 如果连默认选项都没找到，那么最终返回错误
			LOGGER.error("No found default ConfigText. key:{},params:{}", path, params);
			throw new ConfigurationException(String.format("No found default ConfigText. key:%s,params:%s", path,
					params));
		}

	}

	/**
	 * 根据指定的params条件，对文本进行排序，命中率最高的在前面
	 * 
	 * @param dataTable
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws ConfigurationException
	 */
	private static List<TwoTuple<ConfigParams, HAConfigTextBuffer>> sortConfigText(DataTable dataTable,
			final ConfigParams params) throws SQLException, ConfigurationException {
		List<TwoTuple<ConfigParams, HAConfigTextBuffer>> resultList = new ArrayList<TwoTuple<ConfigParams, HAConfigTextBuffer>>();
		// 遍历数据，将数据整理成二元组格式的数据，放入List中，用于下一步的排序
		for (DataRow dataRow : dataTable.getRows()) {
			String configParams = (String) dataRow.getObject("ConfigParams");
			HAConfigTextBuffer textBuffer = new HAConfigTextBuffer();
			textBuffer.setText((String) dataRow.getObject("ConfigText"));
			textBuffer.setVersion(dataRow.getDateTime("Version"));
			ConfigParams paramsTemp = new ConfigParams(configParams);
			resultList.add(new TwoTuple<ConfigParams, HAConfigTextBuffer>(paramsTemp, textBuffer));
		}
		// 对上一步整理好的数据进行排序，排序依据是二元组第一元ConfigParams中的参数匹配度
		Collections.sort(resultList, new Comparator<TwoTuple<ConfigParams, HAConfigTextBuffer>>() {
			@Override
			public int compare(TwoTuple<ConfigParams, HAConfigTextBuffer> o1,
					TwoTuple<ConfigParams, HAConfigTextBuffer> o2) {
				return params.matchRegex(o2.getFirst()) - params.matchRegex(o1.getFirst());
			}
		});
		return resultList;
	}

}
