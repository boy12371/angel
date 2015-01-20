/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job.spi;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.database.pool.DBConnectionPoolType;
import com.feinno.util.ServiceEnviornment;

/**
 * job任务数据库操作基础锁
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class JobDatabaseCoordinator implements JobCoordinator {
	private static Database db;
	private static Properties configs;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JobDatabaseCoordinator.class);

	public JobDatabaseCoordinator() throws IOException, ConfigurationException {
		if (db == null) {
			configs = new Properties();
			configs.load(new StringReader(ConfigurationManager.loadText(
					"FAEDB.properties", null, null)));
			db = DatabaseManager.getDatabase("FAEDB", configs, DBConnectionPoolType.C3p0);
		}
	}

	/**
	 * 获得数据库行锁
	 * 
	 * @param jobName
	 * @param index
	 * @param lockTimeout
	 * @return
	 */
	public int aquireJobLock(String jobName, int index, int lockTimeout) {
		String[] parameters = { "@JobName", "@JobIndex", "@LockWorker",
				"@LockServer", "@TimeoutSeconds" };
		int result = 0;
		String workerName = ServiceEnviornment.getServiceName();
		String serverName = ServiceEnviornment.getComputerName();
		LOGGER.info(String
				.format("start exceute USP_GetJobLock. JobName = %s, index = %s, lockTimeout = %s, workerName = %s, serverName = %s",
						jobName, index, lockTimeout, workerName, serverName));
		try {
			DataTable reader = db.spExecuteTable("USP_GetJobLock", parameters,
					jobName, index, workerName, serverName, lockTimeout);
			if (reader.getRowCount() > 0) {
				DataRow row = reader.getRow(0);
				Object obj = row.getObject(1);
				if (obj instanceof Long) {
					result = ((Long)obj).intValue();
				}else if (obj instanceof Integer) {
					result = ((Integer)obj).intValue();
				}else {
					throw new SQLException("Object ["+obj+"] conversion  to Integer failed.");
				}
			}
		} catch (SQLException e) {
			LOGGER.error(String
					.format("aquireJobLock() Failed To Get Task Lock: %s : \r\n\r\n %s",
							jobName, e.getMessage()));
		}
		LOGGER.info(String
				.format("end exceute USP_GetJobLock. JobName = %s, index = %s, lockTimeout = %s, workerName = %s, serverName = %s",
						jobName, index, lockTimeout, workerName, serverName));
		return result;
	}

	/**
	 * 数据库解锁
	 * 
	 * @param jobName
	 * @param index
	 */
	public void releaseJobLock(String jobName, int index) {
		String[] parameters = { "@JobName", "@JobIndex" };
		try {
			LOGGER.info(String
					.format("start exceute USP_ReleaseJobLock. JobName = %s, index = %s",
							jobName, index));
			db.spExecuteNonQuery("USP_ReleaseJobLock", parameters, jobName,
					index);

			LOGGER.info(String.format(
					"end exceute USP_ReleaseJobLock. JobName = %s, index = %s",
					jobName, index));
		} catch (SQLException e) {
			LOGGER.error(String
					.format("releaseJobLock() erroe. jobName = %s, index = %s, error info = %s",
							jobName, index, e.getMessage()));
		}
	}

}
