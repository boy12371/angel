/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.resource.database;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.configuration.FAE_Resource;
import com.feinno.appengine.configuration.FAE_ResourceKey;
import com.feinno.appengine.resource.Resource;
import com.feinno.appengine.resource.ResourceFactory;
import com.feinno.appengine.resource.ResourceType;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.database.Operation;
import com.feinno.database.pool.DBConnectionPoolType;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class DatabaseResource implements Resource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseResource.class);

	private Database db;
	private DatabaseRpc dbRpc;
	private Database dbXenon;
	private String name;
	private int index;
	private String dbType; //mysql or sqlserver
	private boolean useXenon = false;
	//默认C3p0连接池
	private static DBConnectionPoolType poolType = DBConnectionPoolType.C3p0;

	public DatabaseResource(FAE_Resource resource) throws Exception
	{
		Properties props = new Properties();
		try {
			props.load(new StringReader(resource.getConfig()));
		} catch (IOException e) {
			LOGGER.error("Resource {}.{} load failed!", resource.getName(), resource.getIndex());
			LOGGER.error("properties:" + resource.getConfig(), e);
			throw e;
		}

		this.name = resource.getName();
		this.index = resource.getIndex();
		if(resource.getConfig().indexOf("mysql")>0)
			dbType = "mysql";
		else if(resource.getConfig().indexOf("sqlserver")>0)
			dbType = "sqlserver";
		else
			throw new Exception("unknow dbtype");
		
		String dbName = name + "." + index;
		if(AppEngineManager.INSTANCE.getSettings()!=null && AppEngineManager.INSTANCE.getSettings().userXenon(dbName))
			useXenon = true;
		
		db = DatabaseManager.getDatabase(dbName, props,poolType);
		dbRpc = DatabaseRpcManager.getDatabase(dbName, props);
		
		//dbXenon
		if(useXenon && dbType.equals("mysql"))
		{
			Properties propsXenon = new Properties();
			String xenonName = name + "." + AppEngineManager.INSTANCE.getSettings().Xenon;
			String dbXenonName = xenonName + "." + index;
			try {
				FAE_ResourceKey key = new FAE_ResourceKey();
				key.setResourceName(xenonName);
				key.setResourceIndex(index);				
				FAE_Resource xenonResource = ResourceFactory.FAE_RESOURCE_TABLE.get(key);
				
				propsXenon.load(new StringReader(xenonResource.getConfig()));
				dbXenon = DatabaseManager.getDatabase(dbXenonName,propsXenon,poolType);
				
			} catch (IOException e) {
				LOGGER.error("Resource {}.{} load failed!", xenonName, resource.getIndex());
				throw e;
			}
		}
	}

	/*
	 * @see com.feinno.appengine.resource.Resource#type()
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @return
	 */
	@Override
	public ResourceType type()
	{
		// 默认return ResourceType.DATABASE;类型
		return ResourceType.DATABASE;
	}

	/*
	 * @see com.feinno.appengine.resource.Resource#name()
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @return
	 */
	@Override
	public String name()
	{
		return name;
	}

	/*
	 * @see com.feinno.appengine.resource.Resource#index()
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @return
	 */
	@Override
	public int index()
	{
		return index;
	}

	/**
	 * @return the db
	 */
	Operation getDb()
	{
		//本地测试，没有初始化AppEngineManager的情况
		if(AppEngineManager.INSTANCE.getSettings() == null)
			return db;
		//Xenon优先
		if(useXenon)
			return dbXenon;
		//1 and mysql to DAL
		if("1".equals(AppEngineManager.INSTANCE.getSettings().getDbOverRpc()) && dbType.equals("mysql"))
			return dbRpc;
		else 
			return db;
	}
	
	public String getDBType() {
		return dbType;
	}
}
