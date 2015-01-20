package com.feinno.database;

import java.util.Properties;

import com.feinno.database.pool.DBConnectionPoolType;

/**
 * 操作消息漫游数据库的辅助类，继承Database
 * 再支持原有数据库操作的基础上，可在此类中进行对UPHMDB的操作的扩展
 * @author honghao
 * 
 */
public class UPHMDBHelper extends Database {

	public UPHMDBHelper(String dbName, Properties configs, DBConnectionPoolType type) throws Exception {
		super(dbName, configs, type);
	}
}
