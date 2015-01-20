package com.feinno.database.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;


public abstract class ConnectionPoolAdapter {
	
	protected DataSource dataSource;
	protected DBConnectionPoolType poolType = DBConnectionPoolType.C3p0;
	
	ConnectionPoolAdapter(Properties configs,DBConnectionPoolType type) throws Exception
	{
		poolType = type;
	}
	
	public Connection getConnection() throws SQLException 
	{
		return dataSource.getConnection();
	}

}
