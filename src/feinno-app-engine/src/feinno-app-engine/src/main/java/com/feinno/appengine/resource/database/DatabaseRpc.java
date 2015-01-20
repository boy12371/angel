package com.feinno.appengine.resource.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.DataReader;
import com.feinno.database.DataTable;
import com.feinno.database.Operation;
import com.feinno.serialization.protobuf.types.ProtoByteArray;
import com.feinno.serialization.protobuf.types.ProtoInteger;
import com.feinno.serialization.protobuf.types.ProtoLong;


/**
 * Rpc方式的Database实现
 * 
 * @author lichunlei
 *
 */

public class DatabaseRpc implements Operation {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRpc.class);
	
	private String dbName;

	public DatabaseRpc(String dbName) {
			this.dbName = dbName;
		}

	@Override
	public int spExecuteNonQuery(String spName, String[] params,
			Object... values) throws SQLException {
		
		SqlArgs sqlArgs = new SqlArgs();
		sqlArgs.setDbName(dbName);
		try {
			if (params.length != values.length) {
				throw new IllegalArgumentException("params.length != values.length");
			}			
			sqlArgs.setSpName(spName);
			sqlArgs.setParams(params);
			byte[] vs = SqlArgs.encode(values);
			sqlArgs.setValues(vs);
			
			ProtoInteger result= DatabaseRpcClient.getInstance().spExecuteNonQuery(sqlArgs);
			
			return result.getValue();
			
		} catch (Exception e) {
			LOGGER.error("{}", e);
			throw new SQLException(e);
		}
		
	}

	@Override
	public DataReader spExecuteReader(String spName, String[] params,
			Object... values) throws SQLException {
				
		SqlArgs sqlArgs = new SqlArgs();
		sqlArgs.setDbName(dbName);
		try {
			if (params.length != values.length) {
				throw new IllegalArgumentException("params.length != values.length");
			}			
			sqlArgs.setSpName(spName);
			sqlArgs.setParams(params);
			byte[] vs = SqlArgs.encode(values);
			sqlArgs.setValues(vs);
			
			ProtoByteArray result= DatabaseRpcClient.getInstance().spExecuteReader(sqlArgs);

			DataReader reader = createDataReader(result);
			
			return reader;
			
		} catch (Exception e) {
			LOGGER.error("{}", e);
			throw new SQLException(e);
		}
	}

	@Override
	public DataTable spExecuteTable(String spName, String[] params,
			Object... values) throws SQLException {
		SqlArgs sqlArgs = new SqlArgs();
		sqlArgs.setDbName(dbName);
		try {
			if (params.length != values.length) {
				throw new IllegalArgumentException("params.length != values.length");
			}			
			sqlArgs.setSpName(spName);
			sqlArgs.setParams(params);
			byte[] vs = SqlArgs.encode(values);
			sqlArgs.setValues(vs);
			
			ProtoByteArray result= DatabaseRpcClient.getInstance().spExecuteTable(sqlArgs);
			
			DataTable table = createDataTable(result);
			
			return table;
			
		} catch (Exception e) {
			LOGGER.error("{}", e);
			throw new SQLException(e);
		}
	}

	@Override
	public int executeNonQuery(String sql, Object... values)
			throws SQLException {
		SqlArgs sqlArgs = new SqlArgs();
		sqlArgs.setDbName(dbName);
		try {
			
			sqlArgs.setSql(sql);
			byte[] vs = SqlArgs.encode(values);
			sqlArgs.setValues(vs);
			
			ProtoInteger result= DatabaseRpcClient.getInstance().executeNonQuery(sqlArgs);
			
			return result.getValue();
			
		} catch (Exception e) {
			LOGGER.error("{}", e);
			throw new SQLException(e);
		}
	}

	@Override
	public long executeInsertWithAutoColumn(String insertSql, Object... values)
			throws SQLException {
		SqlArgs sqlArgs = new SqlArgs();
		sqlArgs.setDbName(dbName);
		try {
			
			sqlArgs.setSql(insertSql);
			byte[] vs = SqlArgs.encode(values);
			sqlArgs.setValues(vs);
			
			ProtoLong result= DatabaseRpcClient.getInstance().executeInsertWithAutoColumn(sqlArgs);
			
			return result.getValue();
			
		} catch (Exception e) {
			LOGGER.error("{}", e);
			throw new SQLException(e);
		}
	}

	@Override
	public DataReader executeReader(String sql, Object... values)
			throws SQLException {
		
		SqlArgs sqlArgs = new SqlArgs();
		sqlArgs.setDbName(dbName);
		try {
			
			sqlArgs.setSql(sql);
			byte[] vs = SqlArgs.encode(values);
			sqlArgs.setValues(vs);
			
			ProtoByteArray result= DatabaseRpcClient.getInstance().executeReader(sqlArgs);			
			
			DataReader  reader = createDataReader(result);
			
			return reader;

			
		} catch (Exception e) {
			LOGGER.error("{}", e);
			throw new SQLException(e);
		}
	}

	@Override
	public DataTable executeTable(String sql, Object... values)
			throws SQLException {
		SqlArgs sqlArgs = new SqlArgs();
		sqlArgs.setDbName(dbName);
		try {
			
			sqlArgs.setSql(sql);
			byte[] vs = SqlArgs.encode(values);
			sqlArgs.setValues(vs);
			
			ProtoByteArray result= DatabaseRpcClient.getInstance().executeTable(sqlArgs);			

			DataTable table = createDataTable(result);			
			
			return table;

			
		} catch (Exception e) {
			LOGGER.error("{}", e);
			throw new SQLException(e);
		}
	}

//	@Override
//	public void spExecuteNonQueryAsync(String spName, String[] params,
//			final Action<DBSyncResult<Integer>> callback, Object... values) {
//		
//		SqlArgs sqlArgs = new SqlArgs();
//		sqlArgs.setDbName(dbName);
//		sqlArgs.setSpName(spName);
//		sqlArgs.setParams(params);
//		final DBSyncResult<Integer> result = new DBSyncResult<Integer>();
//		
//		try {
//			byte[] vs = SqlArgs.encode(values);
//			sqlArgs.setValues(vs);					
//		} catch (IOException e) {
//			LOGGER.error("{}", e);			
//			result.setException(e);
//			callback.run(result);
//			return;
//		}
//		DatabaseRpcClient.getInstance().spExecuteNonQuerySync(sqlArgs,new Action<DBSyncResult<ProtoInteger>>(){
//
//			@Override
//			public void run(DBSyncResult<ProtoInteger> a) {
//				if(a.getException()!=null)
//					result.setException(a.getException());
//				else
//				{
//					ProtoInteger r = a.getResult();
//					result.setResult(r.getValue());
//				}
//				callback.run(result);
//			}
//			
//		});
//	}
//
//	@Override
//	public void spExecuteReaderAsync(String spName, String[] params,
//			final Action<DBSyncResult<DataReader>> callback, Object... values) {
//		
//		SqlArgs sqlArgs = new SqlArgs();
//		sqlArgs.setDbName(dbName);
//		sqlArgs.setSpName(spName);
//		sqlArgs.setParams(params);
//		final DBSyncResult<DataReader> result = new DBSyncResult<DataReader>();
//		try {
//			byte[] vs = SqlArgs.encode(values);
//			sqlArgs.setValues(vs);					
//		} catch (IOException e) {
//			LOGGER.error("{}", e);			
//			result.setException(e);
//			callback.run(result);
//			return;
//		}
//		DatabaseRpcClient.getInstance().spExecuteReaderSync(sqlArgs,new Action<DBSyncResult<ProtoByteArray>>(){
//
//			@Override
//			public void run(DBSyncResult<ProtoByteArray> a) {
//				if(a.getException()!=null)
//					result.setException(a.getException());
//				else
//				{
//					ProtoByteArray r = a.getResult();
//					DataReader reader = null;
//					try {
//						reader = createDataReader(r);
//					} catch (ClassNotFoundException e) {
//						LOGGER.error("{}",e);
//						result.setException(e);
//					} catch (IOException e) {
//						LOGGER.error("{}",e);
//						result.setException(e);
//					}
//					result.setResult(reader);
//				}
//				callback.run(result);				
//			}
//			
//		});
//		
//	}
//
//	@Override
//	public void spExecuteTableAsync(String spName, String[] params,
//			final Action<DBSyncResult<DataTable>> callback, Object... values) {
//
//
//		SqlArgs sqlArgs = new SqlArgs();
//		sqlArgs.setDbName(dbName);
//		sqlArgs.setSpName(spName);
//		sqlArgs.setParams(params);
//		final DBSyncResult<DataTable> result = new DBSyncResult<DataTable>();
//		
//		try {
//			byte[] vs = SqlArgs.encode(values);
//			sqlArgs.setValues(vs);					
//		} catch (IOException e) {
//			LOGGER.error("{}", e);
//			
//			result.setException(e);
//			callback.run(result);
//			return;
//		}
//		DatabaseRpcClient.getInstance().spExecuteTableSync(sqlArgs,new Action<DBSyncResult<ProtoByteArray>>(){
//
//			@Override
//			public void run(DBSyncResult<ProtoByteArray> a) {
//				if(a.getException()!=null)
//					result.setException(a.getException());
//				else
//				{
//					ProtoByteArray r = a.getResult();
//					DataTable table = null;
//					try {
//						table = createDataTable(r);
//					} catch (ClassNotFoundException e) {
//						LOGGER.error("{}",e);
//						result.setException(e);
//					} catch (IOException e) {
//						LOGGER.error("{}",e);
//						result.setException(e);
//					} catch (SQLException e) {
//						LOGGER.error("{}",e);
//						result.setException(e);
//					}
//					result.setResult(table);
//				}
//				callback.run(result);				
//			}
//			
//		});
//		
//	}
//
//	@Override
//	public void executeNonQueryAsync(String sql,
//			final Action<DBSyncResult<Integer>> callback, Object... values) {
//		
//		SqlArgs sqlArgs = new SqlArgs();
//		sqlArgs.setDbName(dbName);
//		sqlArgs.setSql(sql);
//		final DBSyncResult<Integer> result = new DBSyncResult<Integer>();
//		try {
//			byte[] vs = SqlArgs.encode(values);
//			sqlArgs.setValues(vs);					
//		} catch (IOException e) {
//			LOGGER.error("{}", e);			
//			result.setException(e);
//			callback.run(result);
//			return;
//		}
//		DatabaseRpcClient.getInstance().executeNonQuerySync(sqlArgs,new Action<DBSyncResult<ProtoInteger>>(){
//
//			@Override
//			public void run(DBSyncResult<ProtoInteger> a) {
//				if(a.getException()!=null)
//					result.setException(a.getException());
//				else
//				{
//					ProtoInteger r = a.getResult();
//					result.setResult(r.getValue());
//				}
//				callback.run(result);
//			}
//			
//		});
//		
//	}
//
//	@Override
//	public void executeInsertWithAutoColumnAsync(String insertSql,
//			final Action<DBSyncResult<Long>> callback, Object... values) {
//
//		SqlArgs sqlArgs = new SqlArgs();
//		sqlArgs.setDbName(dbName);
//		sqlArgs.setSql(insertSql);
//
//		final DBSyncResult<Long> result = new DBSyncResult<Long>();
//		try {
//			byte[] vs = SqlArgs.encode(values);
//			sqlArgs.setValues(vs);					
//		} catch (IOException e) {
//			LOGGER.error("{}", e);			
//			result.setException(e);
//			callback.run(result);
//			return;
//		}
//		DatabaseRpcClient.getInstance().executeInsertWithAutoColumnSync(sqlArgs,new Action<DBSyncResult<ProtoLong>>(){
//
//			@Override
//			public void run(DBSyncResult<ProtoLong> a) {
//				if(a.getException()!=null)
//					result.setException(a.getException());
//				else
//				{
//					ProtoLong r = a.getResult();
//					result.setResult(r.getValue());
//				}
//				callback.run(result);
//			}
//			
//		});
//		
//	}
//
//	@Override
//	public void executeReaderAsync(String sql,
//			final Action<DBSyncResult<DataReader>> callback, Object... values) {
//		
//		SqlArgs sqlArgs = new SqlArgs();
//		sqlArgs.setDbName(dbName);
//		sqlArgs.setSql(sql);
//		final DBSyncResult<DataReader> result = new DBSyncResult<DataReader>();
//		try {
//			byte[] vs = SqlArgs.encode(values);
//			sqlArgs.setValues(vs);					
//		} catch (IOException e) {
//			LOGGER.error("{}", e);			
//			result.setException(e);
//			callback.run(result);
//			return;
//		}
//		DatabaseRpcClient.getInstance().executeReaderSync(sqlArgs,new Action<DBSyncResult<ProtoByteArray>>(){
//
//			@Override
//			public void run(DBSyncResult<ProtoByteArray> a) {
//				if(a.getException()!=null)
//					result.setException(a.getException());
//				else
//				{
//					ProtoByteArray r = a.getResult();
//					DataReader reader = null;
//					try {
//						reader = createDataReader(r);
//					} catch (ClassNotFoundException e) {
//						LOGGER.error("{}",e);
//						result.setException(e);
//					} catch (IOException e) {
//						LOGGER.error("{}",e);
//						result.setException(e);
//					}
//					result.setResult(reader);
//				}
//				callback.run(result);				
//			}
//			
//		});
//		
//	}
//
//	@Override
//	public void executeTableAsync(String sql,
//			final Action<DBSyncResult<DataTable>> callback, Object... values) {
//		
//		SqlArgs sqlArgs = new SqlArgs();
//		sqlArgs.setDbName(dbName);
//		sqlArgs.setSpName(sql);
//		final DBSyncResult<DataTable> result = new DBSyncResult<DataTable>();
//		
//		try {
//			byte[] vs = SqlArgs.encode(values);
//			sqlArgs.setValues(vs);					
//		} catch (IOException e) {
//			LOGGER.error("{}", e);
//			
//			result.setException(e);
//			callback.run(result);
//			return;
//		}
//		DatabaseRpcClient.getInstance().executeTableSync(sqlArgs,new Action<DBSyncResult<ProtoByteArray>>(){
//
//			@Override
//			public void run(DBSyncResult<ProtoByteArray> a) {
//				if(a.getException()!=null)
//					result.setException(a.getException());
//				else
//				{
//					ProtoByteArray r = a.getResult();
//					DataTable table = null;
//					try {
//						table = createDataTable(r);
//					} catch (ClassNotFoundException e) {
//						LOGGER.error("{}",e);
//						result.setException(e);
//					} catch (IOException e) {
//						LOGGER.error("{}",e);
//						result.setException(e);
//					} catch (SQLException e) {
//						LOGGER.error("{}",e);
//						result.setException(e);
//					}
//					result.setResult(table);
//				}
//				callback.run(result);				
//			}
//			
//		});
//		
//	}

	private DataReader createDataReader(ProtoByteArray ba) throws ClassNotFoundException, IOException
	{
		if(ba == null)
			return null;
		
		List<CachedRowSet> list = SqlArgs.decodeRowSet(ba.getValue());
		DataReader reader = null;
		if(list!=null)
		{
//			reader = new DataReader(list.get(0),null,null);
//			reader.setAllResultSet(list);
			reader = new DataReader(list,null);
		}
		else
			reader = new DataReader(null,null);
		return reader;
	}
	
	private DataTable createDataTable(ProtoByteArray ba) throws ClassNotFoundException, IOException, SQLException
	{
		if(ba == null)
			return null;		
		List<CachedRowSet> list = SqlArgs.decodeRowSet(ba.getValue());
		DataTable table = null;
		if(list!=null && list.size()>0)
			table = new DataTable(list.get(0));
		else
			table = new DataTable(null);
		return table;
	}
	
	private List<DataTable> createDataTables(ProtoByteArray ba) throws ClassNotFoundException, IOException, SQLException
	{
		if(ba == null)
			return null;		
		List<CachedRowSet> list = SqlArgs.decodeRowSet(ba.getValue());
		List<DataTable> tables = null;
		if(list!=null && list.size()>0)
		{
			tables = new ArrayList<DataTable>();
			for(CachedRowSet crs : list)
			{
				tables.add(new DataTable(crs));
			}
		}
		return tables;
	}

	/**
	 * 
	 * reader的Rpc接口传多个ResultSet，所以调用excuteReader的远程接口
	 */
	@Override
	public List<DataTable> spExecuteTables(String spName, String[] params,
			Object... values) throws SQLException {
		SqlArgs sqlArgs = new SqlArgs();
		sqlArgs.setDbName(dbName);
		try {
			if (params.length != values.length) {
				throw new IllegalArgumentException("params.length != values.length");
			}			
			sqlArgs.setSpName(spName);
			sqlArgs.setParams(params);
			byte[] vs = SqlArgs.encode(values);
			sqlArgs.setValues(vs);
			
			ProtoByteArray result= DatabaseRpcClient.getInstance().spExecuteReader(sqlArgs);

			List<DataTable> tables = createDataTables(result);
			
			return tables;
			
		} catch (Exception e) {
			LOGGER.error("{}", e);
			throw new SQLException(e);
		}
	}

	/**
	 * reader的Rpc接口传多个ResultSet，所以调用excuteReader的远程接口
	 */
	@Override
	public List<DataTable> executeTables(String sql, Object... values)
			throws SQLException {

		SqlArgs sqlArgs = new SqlArgs();
		sqlArgs.setDbName(dbName);
		try {
			
			sqlArgs.setSql(sql);
			byte[] vs = SqlArgs.encode(values);
			sqlArgs.setValues(vs);
			
			ProtoByteArray result= DatabaseRpcClient.getInstance().executeReader(sqlArgs);			
			
			List<DataTable> tables = createDataTables(result);
			
			return tables;

			
		} catch (Exception e) {
			LOGGER.error("{}", e);
			throw new SQLException(e);
		}
	}

	
}
