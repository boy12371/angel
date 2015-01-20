package com.feinno.appengine.resource.database;

import java.util.Hashtable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.runtime.AppEngineManager;


/**
 * 通过Rpc方式访问DAL的DatabaseManager
 * 
 * @author lichunlei
 *
 */

public class DatabaseRpcManager {

    /**
     * 单例
     */
    private static final DatabaseRpcManager INSTANCE = new DatabaseRpcManager();
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRpcManager.class);

    private Hashtable<String, DatabaseRpc> databaseRpcs;
    private Hashtable<String, DatabaseRpcDistributed> databaseRpcsDistributed;


    /**
     * 初始化对象
     */
    private DatabaseRpcManager() {
        databaseRpcs = new Hashtable<String, DatabaseRpc>();
        databaseRpcsDistributed = new Hashtable<String, DatabaseRpcDistributed>();
    }


    /**
     * 
     * 获取一个拥有唯一名字的数据库对象
     * 
     * @param dbName
     * @param configs
     * @return Database对象
     */
    public static DatabaseRpc getDatabase(String dbName, Properties configs) { // 开关
        if (!AppEngineManager.INSTANCE.getSettings().isDalClientNew()) {

            DatabaseRpc db = INSTANCE.databaseRpcs.get(dbName);
            if (db == null) {
                try {
                    db = new DatabaseRpc(dbName);
                }
                catch (Exception e) {
                    LOGGER.error("DatabaseRpcClient {} can't initialize with {}", dbName, configs);
                    throw new IllegalArgumentException("DatabaseRpcClient initialize failed", e);
                }
                INSTANCE.databaseRpcs.put(dbName, db);
            }
            return db;
        }
        else {

            DatabaseRpcDistributed db = INSTANCE.databaseRpcsDistributed.get(dbName);
            if (db == null) {
                try {
                    db = new DatabaseRpcDistributed(dbName);
                }
                catch (Exception e) {
                    LOGGER.error("DatabaseRpcClientDistributed {} can't initialize with {}", dbName, configs);
                    throw new IllegalArgumentException("DatabaseRpcClientDistributed initialize failed", e);
                }
                INSTANCE.databaseRpcsDistributed.put(dbName, db);
            }
            return db;
        }
    }

    // public static DatabaseRpc getDatabase(String dbName, DatabaseConfigBean
    // config)
    // {
    // return getDatabase(dbName, config.getProperties());
    // }
}
