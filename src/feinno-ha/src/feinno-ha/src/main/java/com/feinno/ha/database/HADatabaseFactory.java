package com.feinno.ha.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;

/**
 * 
 * <b>描述: </b>该类用于feinno-ha中对数据库连接进行统一管理的类，可以帮助从资源文件获得数据库的连接
 * <p>
 * <b>功能: </b>该类用于feinno-ha中对数据库连接进行统一管理的类
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class HADatabaseFactory {

	/** HADB的名字 */
	public static String DATABASE_HADB_NAME = "HADB";

	/** BaseFAEDB的名字 */
	public static String DATABASE_BASE_FAEDB_NAME = "BaseFAEDB";

	/** BaseHADB的名字 */
	public static String DATABASE_BASE_HADB_NAME = "BaseHADB";

	/** 当前应用的根路径 */
	public static final String APP_ROOT_PATH = System.getProperty("user.dir") + File.separator;

	/** 当前日志引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HADatabaseFactory.class);

	/**
	 * 获得HADB的数据库连接
	 * 
	 * @return
	 */
	public static Database getHADatabase() {
		return getDatabase(DATABASE_HADB_NAME);
	}

	public static void setHADBProperties(String properties) {
		DATABASE_HADB_NAME = properties;
	}

	/**
	 * 获得一个数据库{@link Database}引用，如果当前引用为空，会通过dbName的名字寻找相应的配置文件，使用这个配置文件进行加载
	 * 
	 * @param dbName
	 * @return
	 */
	public static Database getDatabase(String dbName) {
		Database database = DatabaseManager.getDatabase(dbName);
		if (database == null) {
			LOGGER.info("No found {} database reference. Begin to create.", dbName);
			try {
				database = DatabaseManager.getDatabase(dbName, getProperties(dbName));
			} catch (Exception e) {
				LOGGER.error(String.format("load {} database properties error. ", dbName), e);
				throw new RuntimeException(e);
			}
		}
		return database;
	}

	/**
	 * 获得一个数据库{@link Database}引用，如果当前引用为空，会通过dbName的名字寻找相应的配置文件，使用这个配置文件进行加载
	 * 
	 * @param dbName
	 * @return
	 */
	public static Database getDatabase(String dbName, Properties configs) {
		Database database = DatabaseManager.getDatabase(dbName);
		if (database == null) {
			LOGGER.info("No found {} database reference. Begin to create.", dbName);
			try {
				database = DatabaseManager.getDatabase(dbName, configs);
			} catch (Exception e) {
				LOGGER.error(String.format("load {} database properties error. ", dbName), e);
				throw new RuntimeException(e);
			}
		}
		return database;
	}

	/**
	 * 根据资源文件名字获取数据库配置的资源文件，当资源文件不存在时，抛出相应异常，调用者需要处理这些异常
	 * 
	 * @param proName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Properties getProperties(String proName) throws FileNotFoundException, IOException {
		if (!proName.endsWith(".properties")) {
			proName += ".properties";
		}
		// 获取该资源文件，如果资源文件不存在，则抛出异常
		Properties props = new Properties();
		props.load(new FileInputStream(APP_ROOT_PATH + proName));
		return props;
	}

}
