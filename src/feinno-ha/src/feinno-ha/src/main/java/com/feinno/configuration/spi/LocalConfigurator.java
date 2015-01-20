/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 12, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.configuration.spi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigParams;
import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigTableItem;
import com.feinno.configuration.ConfigType;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.Configurator;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.ha.database.HADatabaseFactory;
import com.feinno.ha.database.HADBConfigHelper;
import com.feinno.ha.interfaces.configuration.HAConfigTableBuffer;
import com.feinno.serialization.protobuf.ProtoEntity;

/**
 * 
 * 本地配置读取, 1. Table从HADB中读取配置信息<br>
 * 2. Text从HADB中读取配置信息, 支持特例化<br>
 * 3. Text的本地配置优先<br>
 * 4. 不支持配置PUSH更新及订阅更新<br>
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class LocalConfigurator implements Configurator {

	/** 日志引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalConfigurator.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.feinno.configuration.Configurator#loadConfigTable(java.lang.Class,
	 * java.lang.Class, java.lang.String,
	 * com.feinno.configuration.ConfigUpdateAction)
	 */
	@Override
	public <K, V extends ConfigTableItem> ConfigTable<K, V> loadConfigTable(Class<K> keyType, Class<V> valueType,
			final String path, ConfigUpdateAction<ConfigTable<K, V>> updateCallback) throws ConfigurationException {
		try {
			// 1.首选获取此配置表的配置信息IICHADB.HA_ConfigTables : TableName DatabaseName
			// LoadParam Version
			HAConfigTableBuffer tableBuffer = new HAConfigTableBuffer();
			String dbName = HADBConfigHelper.getDatabaseName(path, tableBuffer);

			// 2.根据配置信息到具体的数据库获取此配置表的内容
			if (dbName != null) {
				Database database = DatabaseManager.getDatabase(dbName);
				if (database == null) {
					// 如果此数据库的连接不存在，则取到该数据库的配置后进行创建连接
					Properties properties = ConfigurationManager.loadProperties(dbName + ".properties", null, null);
					database = HADatabaseFactory.getDatabase(dbName, properties);
				}
				tableBuffer = HADBConfigHelper.getConfigTable(database, path, tableBuffer);
			} else {
				tableBuffer = HADBConfigHelper.getConfigTableByBaseCenter(path, null);
			}
			// 3.转换为Table，且执行callback
			ConfigTable<K, V> configTableTemp = tableBuffer.toTable(keyType, valueType);
			if (updateCallback != null) {
				updateCallback.run(configTableTemp);
			}
			return configTableTemp;
		} catch (Exception e) {
			throw new ConfigurationException("", e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.feinno.configuration.Configurator#loadConfigText(java.lang.String,
	 * com.feinno.configuration.ConfigParams,
	 * com.feinno.configuration.ConfigUpdateAction)
	 */
	@Override
	public String loadConfigText(String path, ConfigParams params, ConfigUpdateAction<String> updateCallback)
			throws ConfigurationException {
		LOGGER.info("Load config text. path:{},params:{}", path, params != null ? params.toString() : "");
		String text = null;
		try {
			// 首先从本地取出配置文件,此时忽略params参数
			text = getLocalConfigText(path);
			if (updateCallback != null) {
				updateCallback.run(text);
			}
			return text;
		} catch (Exception e) {
			LOGGER.warn("Not found LocalConfigText.Trying to get from the database. path:{},params:{}", path,
					params != null ? params.toString() : "");
		}
		try {
			// 如果从本地取配置文件出现问题，那么转头向DB中索取
			text = getDBConfigText(path, params);
			if (updateCallback != null) {
				updateCallback.run(text);
			}
			return text;
		} catch (Exception e2) {
			LOGGER.error(String.format("Database found error . path:%s,params%s", path,
					params != null ? params.toString() : ""), e2);
			throw new ConfigurationException("", e2);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.feinno.configuration.Configurator#subscribeConfig(com.feinno.
	 * configuration.ConfigType, java.lang.String)
	 */
	@Override
	public void subscribeConfig(ConfigType type, String path, ConfigParams params) {
		// Local的此操作进行忽略
	}

	/**
	 * 根据指定路径，从本地取出对应的文件内容
	 * 
	 * @param path
	 *            该路径为相对路径，而非绝对路径
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String getLocalConfigText(String path) throws FileNotFoundException, IOException {
		FileReader fileReader = new FileReader(System.getProperty("user.dir") + File.separator + path);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuffer sb = new StringBuffer();
		try {
			String s1 = null;
			while ((s1 = bufferedReader.readLine()) != null) {
				sb.append(s1 + "\r\n");
			}
		} catch (IOException e) {
			throw e;
		} finally {
			bufferedReader.close();
			fileReader.close();
		}
		return sb.toString();
	}

	/**
	 * 从数据库中取出指定路径作为KEY，且符合指定的{@link ConfigParams}的记录
	 * 
	 * @param path
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	private String getDBConfigText(final String path, final ConfigParams params) throws SQLException,
			ConfigurationException {
		LOGGER.info("Get configuration form database. path:{},params:{}", path, params != null ? params.toString() : "");
		return HADBConfigHelper.getConfigText(path, params).getText();
	}

	@Override
	public <T extends ProtoEntity> T loadConfig(ConfigType type, String path, ConfigParams params,
			ConfigUpdateAction<T> updateCallback) throws ConfigurationException {
		return null;
	}
}
