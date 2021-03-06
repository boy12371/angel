/*
 * FAE, Feinno App Engine
 *  
 * Create by wanglihui 2010-12-13
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.database;

import java.util.Hashtable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.pool.DBConnectionPoolType;

/**
 * <b>描述: </b>数据库连接管理类<br>
 * <p>
 * <b>功能：</b>数据库访问的管理类<br>
 * <p>
 * <b>用法: </b>获取对某个数据库的访问对象Database<br>
 * <p>
 * 获得Database方式举例如下： TocatPool:
 * 
 * <pre>
 * <code>
 * 	Properties p = new Properties();
 * 	p.setProperty("driverClass", "com.mysql.jdbc.Driver"); 
 * 	p.setProperty("jdbcUrl", "jdbc:mysql://192.168.110.234/IICUPDB_POOL13?autoReconnect=true&cacheCallableStmts=true&callableStmtCacheSize=512&tinyInt1isBit=false&useDynamicCharsetInfo=false&zeroDateTimeBehavior=round  ");
 * 	p.setProperty("username", "sa");
 * 	p.setProperty("password", "Password01!");
 * 	p.setProperty("maxActive", "30");
 *        p.setProperty("maxIdle", "30");
 * 	p.setProperty("minIdle", "10");
 * 	p.setProperty("maxWait","30000");  
 * 	Database d = DatabaseManager.getDatabase("IICUPDB.13", p,DBConnectionPoolType.TomcatPool);
 * 	
 * </code>
 * </pre>
 * 
 * C3p0连接池：
 * 
 * <pre>
 * <code>
 * 	Properties p = new Properties();
 * 	p.setProperty("driverClass", "com.mysql.jdbc.Driver"); 
 * 	p.setProperty("jdbcUrl", "jdbc:mysql://192.168.110.234/IICUPDB_POOL13?autoReconnect=true&cacheCallableStmts=true&callableStmtCacheSize=512&tinyInt1isBit=false&useDynamicCharsetInfo=false&zeroDateTimeBehavior=round  ");
 * 	p.setProperty("user", "sa");
 * 	p.setProperty("password", "Password01!");
 * 	p.setProperty("acquireIncrement", "5");
 * 	p.setProperty("acquireRetryDelay", "500");
 *     p.setProperty("checkoutTimeout", "10000");
 * 	p.setProperty("idleConnectionTestPeriod", "30");
 * 	p.setProperty("initialPoolSize","10");  
 * 	p.setProperty("minPoolSize","10");  
 * 	p.setProperty("maxPoolSize","100");  
 * 	p.setProperty("maxIdleTime","30");  
 * 	p.setProperty("numHelperThreads","3");  
 * 	p.setProperty("maxConnectionAge","50");  
 * 	p.setProperty("automaticTestTable","c3p0Test");
 * 	Database d = DatabaseManager.getDatabase("IICUPDB.13", p,DBConnectionPoolType.C3p0);
 * 	
 * </code>
 * </pre>
 * 
 * 其中c3p0包含的其它属性可以参考如下链接： <br>
 * http://10.10.41.235/trac/fae/wiki/c3p0 <br>
 * 现阶段同时支持C3p0和TomcatPool两个连接池，将来可能只保留一个。
 * 
 * 
 * @author wanglihui
 */
public class DatabaseManager
{
	/**
	 * 单例
	 */
	private static final DatabaseManager INSTANCE = new DatabaseManager();
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManager.class);

	private Hashtable<String, Database> databases;

	/**
	 * 初始化对象
	 */
	private DatabaseManager()
	{
		databases = new Hashtable<String, Database>();
	}

	/**
	 * 为了降低风险，默认还是C3p0
	 * @param dbName
	 * @param configs
	 * @return
	 */
	public static Database getDatabase(String dbName, Properties configs)
	{
		return getDatabase(dbName,configs,DBConnectionPoolType.C3p0);
	}
	
	/**
	 * 
	 * 获取一个拥有唯一名字的数据库对象
	 * 
	 * @param dbName
	 * @param configs
	 * @return Database对象
	 */
	public synchronized static Database getDatabase(String dbName, Properties configs, DBConnectionPoolType type)
	{
		Database db = INSTANCE.databases.get(dbName);
		if (db == null) {
			try {
				LOGGER.info("dbtype  {}:{}", dbName, type);
				db = new Database(dbName, configs, type);

			} catch (Exception e) {
				LOGGER.error("Database {} can't initialize with {}", dbName, configs);
				throw new IllegalArgumentException("Datbase initialize failed", e);
			}
			INSTANCE.databases.put(dbName, db);
		}
		return db;
	}

	/**
	 * 
	 * 获取一个拥有唯一名字的数据库对象<br>
	 * 
	 * @param dbName
	 * @param configs
	 * @return MonDBHelper对象
	 */
	public static MonDBHelper getMonDBHelper(String dbName, Properties configs)
	{
		DBConnectionPoolType type = DBConnectionPoolType.C3p0;
		MonDBHelper db = (MonDBHelper) INSTANCE.databases.get("MonDBHelper_" + dbName);
		if (db == null) {
			try {
				LOGGER.info("dbtype  {}:{}", dbName, type);
				db = new MonDBHelper("MonDBHelper_" + dbName, configs, type);
			} catch (Exception e) {
				LOGGER.error("Database {} can't initialize with {}", dbName, configs);
				throw new IllegalArgumentException("Datbase initialize failed", e);
			}
			INSTANCE.databases.put("MonDBHelper_" + dbName, db);
		}
		return db;
	}
	
	public static UPHMDBHelper getUPHMDBHelper(String dbname, Properties configs) {
		DBConnectionPoolType type = DBConnectionPoolType.C3p0;
		UPHMDBHelper db = (UPHMDBHelper) INSTANCE.databases.get(dbname);
		if (db == null) {
			try {
				LOGGER.info("dbtype {}:{}", dbname, type);
				db = new UPHMDBHelper(dbname, configs, type);
			} catch (Exception e) {
				LOGGER.error("Database {} can't initialize with {}", dbname, configs);
				throw new IllegalArgumentException("Database initialize failed", e);
			}
			INSTANCE.databases.put(dbname, db);
		}
		return db;
	}

	// public static Database getDatabase(String dbName, DatabaseConfigBean
	// config)
	// {
	// return getDatabase(dbName, config.getProperties());
	// }

	public static Database getDatabase(String dbName)
	{
		Database db = INSTANCE.databases.get(dbName);
		return db;
	}

	// public static void main(String[] args)
	// {
	// Properties p = new Properties();
	// p.setProperty("driverClass", "net.sourceforge.jtds.jdbc.Driver");
	// p.setProperty("jdbcUrl",
	// "jdbc:jtds:sqlserver://192.168.110.210:2387/IICHADB");
	// p.setProperty("user", "sa");
	// p.setProperty("password", "Password01!");
	// p.setProperty("maxPoolSize", "160");
	// p.setProperty("minPoolSize", "10");
	// Database d = DatabaseManager.getDatabase("IICHADB", p);
	// Transaction tx = null;
	// try {
	// tx = d.beginTransaction();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// try {
	// String[] params = {"@TableName"};
	// //存储过程USP_CenterLoadConfigTable不能在spExecuteNonQuery方法中执行，因为其返回的是ResultSet。
	// // tx.spExecuteNonQuery("USP_CenterLoadConfigTable",params,
	// "CFG_Carrier");
	//
	// DataReader reader =
	// tx.spExecuteReader("USP_CenterLoadConfigTable",params, "CFG_Carrier");
	// while (reader.read()) {
	// System.out.println(reader.getString(1));
	// }
	//
	// DataTable dataTable =
	// tx.spExecuteTable("USP_CenterLoadConfigTable",params, "CFG_Carrier");
	// if(dataTable.getRowCount()>0)
	// System.out.println(dataTable.getRow(0).getObject(1));
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
}
