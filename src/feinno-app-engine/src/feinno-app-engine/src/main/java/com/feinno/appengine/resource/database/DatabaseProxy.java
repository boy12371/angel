/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft 2011-2-1
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.resource.database;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.ContextUri;
import com.feinno.appengine.resource.ResourceProxy;
import com.feinno.database.DataReader;
import com.feinno.database.DataTable;

/**
 * 一个DatabaseProxy后面可能包含一组Database 一组Database通过AppContext.getUri()进行定位
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class DatabaseProxy extends ResourceProxy
{
	// TODO 记录错误日志
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseProxy.class);

	public DatabaseProxy()
	{
		super();
	}

	/**
	 * 执行不返回结果集的存储过程
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param spName
	 *            存储过程名
	 * @param params
	 *            参数名
	 * @param values
	 *            参数值
	 * @return (1)SQL 数据操作语言 (DML) 语句的行数 (2)对于无返回内容的SQL语句，返回 0
	 * @throws SQLException
	 */
	public int spExecuteNonQuery(AppContext ctx, String spName, String[] params, Object... values) throws SQLException
	{
		ContextUri uri = ctx.getContextUri();
		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
		LOGGER.info("process spExecuteNonQuery for {} {}", uri, dbResource);
		return dbResource.getDb().spExecuteNonQuery(spName, params, values);
	}

	/**
	 * 执行一个存储过程, 返回一个DataReader对象, 这个DataReader对象在操作后必须执行DataReader对象的close()操作,
	 * 释放资源
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param spName
	 *            存储过程名
	 * @param params
	 *            参数名
	 * @param values
	 *            参数值
	 * @return 包含该查询生成的数据的DataReader对象
	 * @throws SQLException
	 */
	public DataReader spExecuteReader(AppContext ctx, String spName, String[] params, Object... values) throws SQLException
	{
		ContextUri uri = ctx.getContextUri();
		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
		return dbResource.getDb().spExecuteReader(spName, params, values);
	}

	/**
	 * 
	 * 执行一个存储过程, 返回一个DataTable, 结果缓存在DataTable中
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param spName
	 *            存储过程名
	 * @param params
	 *            参数名
	 * @param values
	 *            参数值
	 * @return 包含该查询生成的数据的DataTable对象
	 * @throws SQLException
	 */
	public DataTable spExecuteTable(AppContext ctx, String spName, String[] params, Object... values) throws SQLException
	{
		ContextUri uri = ctx.getContextUri();
		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
		return dbResource.getDb().spExecuteTable(spName, params, values);
	}
	
	/**
	 * 
	 * 执行一个存储过程, 返回一个List<DataTable>
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param spName
	 *            存储过程名
	 * @param params
	 *            参数名
	 * @param values
	 *            参数值
	 * @return 包含该查询生成的数据的List<DataTable>对象
	 * @throws SQLException
	 */
	public List<DataTable> spExecuteTables(AppContext ctx, String spName, String[] params, Object... values) throws SQLException
	{
		ContextUri uri = ctx.getContextUri();
		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
		return dbResource.getDb().spExecuteTables(spName, params, values);
	}

	/**
	 * 执行一个不返回结果的SQL语句
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param sql
	 *            可以包含?参数的sql语句
	 * @param values
	 *            ?对应的参数值
	 * @return (1)SQL 数据操作语言 (DML) 语句的行数 (2)对于无返回内容的SQL语句，返回 0
	 * @throws SQLException
	 */
	public int executeNonQuery(AppContext ctx, String sql, Object... values) throws SQLException
	{
		ContextUri uri = ctx.getContextUri();
		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
		return dbResource.getDb().executeNonQuery(sql, values);
	}

	/**
	 * 
	 * 执行一个SQL语句, 返回一个DataReader对象,
	 * 这个DataReader对象在操作后必须执行DataReader对象的close()操作, 释放资源
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param sql
	 *            可以包含?参数的sql语句
	 * @param values
	 *            ?对应的参数值
	 * @return 包含该查询生成的数据的DataReader对象
	 * @throws SQLException
	 */
	public DataReader executeReader(AppContext ctx, String sql, Object... values) throws SQLException
	{
		ContextUri uri = ctx.getContextUri();
		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
		return dbResource.getDb().executeReader(sql, values);
	}

	/**
	 * 对一个带有自增长字段的表，执行一条insert语句，并返回自增长的值。
	 * 
	 * @param insertSql
	 *            可以包含?参数的insert语句
	 * @param values
	 *            ?对应的参数值
	 * @return 返回自增长字段的值。如果该表不带自增长字段，则返回-1。
	 * @throws SQLException
	 */
	public long executeInsertWithAutoColumn(AppContext ctx, String insertSql, Object... values) throws SQLException
	{
		ContextUri uri = ctx.getContextUri();
		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
		return dbResource.getDb().executeInsertWithAutoColumn(insertSql, values);
	}

	/**
	 * 执行一个SQL语句, 返回一个DataTable, 结果缓存在Table中
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param sql
	 *            可以包含?参数的sql语句
	 * @param values
	 *            ?对应的参数值
	 * @return 包含该查询生成的数据的DataTable对象
	 * @throws SQLException
	 */
	public DataTable executeTable(AppContext ctx, String sql, Object... values) throws SQLException
	{
		ContextUri uri = ctx.getContextUri();
		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
		return dbResource.getDb().executeTable(sql, values);
	}
	
	/**
	 * 执行一个SQL语句, 返回一个List<DataTable>
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param sql
	 *            可以包含?参数的sql语句
	 * @param values
	 *            ?对应的参数值
	 * @return 包含该查询生成的数据的List<DataTable>对象
	 * @throws SQLException
	 */
	public List<DataTable> executeTables(AppContext ctx, String sql, Object... values) throws SQLException
	{
		ContextUri uri = ctx.getContextUri();
		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
		return dbResource.getDb().executeTables(sql, values);
	}
	
	
	/****************异步接口************************/
	/**
	 * 执行不返回结果集的存储过程
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param spName
	 *            存储过程名
	 * @param params
	 *            参数名
	 * @param values
	 *            参数值
	 * @return (1)SQL 数据操作语言 (DML) 语句的行数 (2)对于无返回内容的SQL语句，返回 0
	 * @throws SQLException
	 */
//	public void spExecuteNonQueryAsync(AppContext ctx, String spName, String[] params, Action<DBSyncResult<Integer>> callback,Object... values) throws SQLException
//	{
//		ContextUri uri = ctx.getContextUri();
//		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
//		LOGGER.info("process spExecuteNonQuery for {} {}", uri, dbResource);
//		//return dbResource.getDb().spExecuteNonQuery(spName, params, values);
//		dbResource.getDb().spExecuteNonQueryAsync(spName, params, callback,values);
//	}

	/**
	 * 执行一个存储过程, 返回一个DataReader对象, 这个DataReader对象在操作后必须执行DataReader对象的close()操作,
	 * 释放资源
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param spName
	 *            存储过程名
	 * @param params
	 *            参数名
	 * @param values
	 *            参数值
	 * @return 包含该查询生成的数据的DataReader对象
	 * @throws SQLException
	 */
//	public void spExecuteReaderAsync(AppContext ctx, String spName, String[] params,Action<DBSyncResult<DataReader>> callback, Object... values) throws SQLException
//	{
//		ContextUri uri = ctx.getContextUri();
//		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
//		//return dbResource.getDb().spExecuteReader(spName, params, values);
//		dbResource.getDb().spExecuteReaderAsync(spName, params,callback, values);
//	}

	/**
	 * 
	 * 执行一个存储过程, 返回一个DataTable, 结果缓存在DataTable中
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param spName
	 *            存储过程名
	 * @param params
	 *            参数名
	 * @param values
	 *            参数值
	 * @return 包含该查询生成的数据的DataTable对象
	 * @throws SQLException
	 */
//	public void spExecuteTableAsync(AppContext ctx, String spName, String[] params, Action<DBSyncResult<DataTable>> callback,Object... values) throws SQLException
//	{
//		ContextUri uri = ctx.getContextUri();
//		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
//		//return dbResource.getDb().spExecuteTable(spName, params, values);
//		dbResource.getDb().spExecuteTableAsync(spName, params, callback,values);
//	}

	/**
	 * 执行一个不返回结果的SQL语句
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param sql
	 *            可以包含?参数的sql语句
	 * @param values
	 *            ?对应的参数值
	 * @return (1)SQL 数据操作语言 (DML) 语句的行数 (2)对于无返回内容的SQL语句，返回 0
	 * @throws SQLException
	 */
//	public void executeNonQueryAsync(AppContext ctx, String sql,Action<DBSyncResult<Integer>> callback,Object... values) throws SQLException
//	{
//		ContextUri uri = ctx.getContextUri();
//		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
//		//return dbResource.getDb().executeNonQuery(sql, values);
//		dbResource.getDb().executeNonQueryAsync(sql,callback, values);
//	}

	/**
	 * 
	 * 执行一个SQL语句, 返回一个DataReader对象,
	 * 这个DataReader对象在操作后必须执行DataReader对象的close()操作, 释放资源
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param sql
	 *            可以包含?参数的sql语句
	 * @param values
	 *            ?对应的参数值
	 * @return 包含该查询生成的数据的DataReader对象
	 * @throws SQLException
	 */
//	public void executeReaderAsync(AppContext ctx, String sql,Action<DBSyncResult<DataReader>> callback, Object... values)
//	{
//		ContextUri uri = ctx.getContextUri();
//		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
//	//	return dbResource.getDb().executeReader(sql, values);
//		dbResource.getDb().executeReaderAsync(sql,callback,values);
//	}
	
    /**
     * 对一个带有自增长字段的表，执行一条insert语句，并返回自增长的值。
     * @param insertSql 可以包含?参数的insert语句
     * @param values ?对应的参数值
     * @return 返回自增长字段的值。如果该表不带自增长字段，则返回-1。
     * @throws SQLException
     */
//    public void executeInsertWithAutoColumnAsync(AppContext ctx, String insertSql,Action<DBSyncResult<Long>> callback, Object... values) 
//    {
//        ContextUri uri = ctx.getContextUri();
//        DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
//        //return dbResource.getDb().executeInsertWithAutoColumn(insertSql, values);
//        dbResource.getDb().executeInsertWithAutoColumnAsync(insertSql, callback,values);
//    }


	/**
	 * 执行一个SQL语句, 返回一个DataTable, 结果缓存在Table中
	 * 
	 * @param ctx
	 *            AppContext对象
	 * @param sql
	 *            可以包含?参数的sql语句
	 * @param values
	 *            ?对应的参数值
	 * @return 包含该查询生成的数据的DataTable对象
	 * @throws SQLException
	 */
//	public void executeTableAsync(AppContext ctx, String sql, Action<DBSyncResult<DataTable>> callback,Object... values)
//	{
//		ContextUri uri = ctx.getContextUri();
//		DatabaseResource dbResource = (DatabaseResource) locateResource(uri);
//		//return dbResource.getDb().executeTable(sql, values);
//		dbResource.getDb().executeTableAsync(sql, callback,values);
//	}
	
	/**
	 * 执行一个存储过程, 返回一个DataTable, 结果缓存在DataTable中
	 * 
	 * @param physicalPool
	 *            物理poolID
	 * @param spName
	 *            存储过程名
	 * @param params
	 *            参数名
	 * @param values
	 *            参数值
	 * @return 包含该查询生成的数据的DataTable对象
	 * @throws SQLException
	 */
	public DataTable spExecuteTable(Integer physicalPool, String spName, String[] params, Object... values) throws SQLException {
		DatabaseResource dbResource = (DatabaseResource) locateResource(physicalPool);
		return dbResource.getDb().spExecuteTable(spName, params, values);
	}
	
	/**
	 * 执行不返回结果集的存储过程
	 * 
	 * @param physicalPool
	 *            物理poolID
	 * @param spName
	 *            存储过程名
	 * @param params
	 *            参数名
	 * @param values
	 *            参数值
	 * @return (1)SQL 数据操作语言 (DML) 语句的行数 (2)对于无返回内容的SQL语句，返回 0
	 * @throws SQLException
	 */
	public int spExecuteNonQuery(Integer physicalPool, String spName, String[] params, Object... values) throws SQLException
	{
		DatabaseResource dbResource = (DatabaseResource) locateResource(physicalPool);
		LOGGER.info("process spExecuteNonQuery for {} {}", physicalPool, dbResource);
		return dbResource.getDb().spExecuteNonQuery(spName, params, values);
	}
	
	/**
	 * 执行一个SQL语句, 返回一个DataTable, 结果缓存在Table中
	 * 
	 * @param physicalPool
	 *            物理poolID
	 * @param sql
	 *            可以包含?参数的sql语句
	 * @param values
	 *            ?对应的参数值
	 * @return 包含该查询生成的数据的DataTable对象
	 * @throws SQLException
	 */
	public DataTable executeTable(Integer physicalPool, String sql, Object... values) throws SQLException
	{
		DatabaseResource dbResource = (DatabaseResource) locateResource(physicalPool);
		return dbResource.getDb().executeTable(sql, values);
	}
	
	/**
	 * 执行一个存储过程, 返回一个DataReader,注意要关闭datareader
	 * @param physicalPool
	 * @param spName
	 * @param params
	 * @param values
	 * @return
	 * @throws SQLException
	 */
	@Deprecated
	public DataReader spExecuteReader(Integer physicalPool, String spName, String[] params, Object... values) throws SQLException {
		DatabaseResource dbResource = (DatabaseResource) locateResource(physicalPool);
		return dbResource.getDb().spExecuteReader(spName, params, values);
	}
	
	/**
	 * 执行一个不返回结果的SQL语句
	 * 
	 * @param physicalPool
	 *            物理poolID
	 * @param sql
	 *            可以包含?参数的sql语句
	 * @param values
	 *            ?对应的参数值
	 * @return (1)SQL 数据操作语言 (DML) 语句的行数 (2)对于无返回内容的SQL语句，返回 0
	 * @throws SQLException
	 */
	public int executeNonQuery(Integer physicalPool, String sql, Object... values) throws SQLException
	{
		DatabaseResource dbResource = (DatabaseResource) locateResource(physicalPool);
		return dbResource.getDb().executeNonQuery(sql, values);
	}
	
}
