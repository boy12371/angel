/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 23, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.center;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigParams;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.ha.database.HADatabaseFactory;
import com.feinno.ha.database.HADBConfigHelper;
import com.feinno.ha.interfaces.center.HACenterConfigService;
import com.feinno.ha.interfaces.configuration.HAConfigArgs;
import com.feinno.ha.interfaces.configuration.HAConfigTableBuffer;
import com.feinno.ha.interfaces.configuration.HAConfigTextBuffer;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ConfigurationService implements HACenterConfigService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationService.class);

	public HAConfigTableBuffer loadConfigTable(HAConfigArgs input) {
		try {
			// 1.首先获取此配置表的配置信息
			HAConfigTableBuffer tableBuffer = new HAConfigTableBuffer();
			String dbName = HADBConfigHelper.getDatabaseName(input.getPath(), tableBuffer);

			// 2. 读取配置
			Database database = DatabaseManager.getDatabase(dbName);
			if (database == null) {
				Properties properties = ConfigurationManager.loadProperties(dbName + ".properties", null, null);
				database = HADatabaseFactory.getDatabase(dbName, properties);
			}
			return HADBConfigHelper.getConfigTable(database, input.getPath(), tableBuffer);
		} catch (Exception e) {
			LOGGER.error(String.format("loadConfigTable failed.path:%s .", input.getPath()), e);
			throw new RuntimeException(e);
		}
	}

	public HAConfigTextBuffer loadConfigText(HAConfigArgs input) {
		try {
			// 1. 加载
			return HADBConfigHelper.getConfigText(input.getPath(), new ConfigParams(input.getParams()));
		} catch (Exception e) {
			LOGGER.error(String.format("loadConfigText failed.path:%s .", input.getPath()), e);
			// 出错后更新最后一次出错时间
			throw new RuntimeException(e);
		}
	}

}
