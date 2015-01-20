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
import com.feinno.serialization.protobuf.types.ProtoByteArray;
import com.feinno.serialization.protobuf.types.ProtoInteger;
import com.feinno.serialization.protobuf.types.ProtoLong;

public class DatabaseRpcDistributed extends DatabaseRpc {

	/**
	 * Rpc方式的Database实现
	 * 
	 * @author lichunlei
	 *
	 */
	private String dbName;
		
		private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRpc.class);
		

		public DatabaseRpcDistributed(String dbName) {
			super(dbName);
			this.dbName= dbName;
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
				
				ProtoInteger result= DatabaseRpcClientDistributed.getInstance().spExecuteNonQuery(dbName,sqlArgs);
				
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
				
				ProtoByteArray result= DatabaseRpcClientDistributed.getInstance().spExecuteReader(dbName,sqlArgs);

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
				
				ProtoByteArray result= DatabaseRpcClientDistributed.getInstance().spExecuteTable(dbName,sqlArgs);
				
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
				
				ProtoInteger result= DatabaseRpcClientDistributed.getInstance().executeNonQuery(dbName,sqlArgs);
				
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
				
				ProtoLong result= DatabaseRpcClientDistributed.getInstance().executeInsertWithAutoColumn(dbName,sqlArgs);
				
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
				
				ProtoByteArray result= DatabaseRpcClientDistributed.getInstance().executeReader(dbName,sqlArgs);			
				
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
				
				ProtoByteArray result= DatabaseRpcClientDistributed.getInstance().executeTable(dbName,sqlArgs);			

				DataTable table = createDataTable(result);			
				
				return table;

				
			} catch (Exception e) {
				LOGGER.error("{}", e);
				throw new SQLException(e);
			}
		}

		private DataReader createDataReader(ProtoByteArray ba) throws ClassNotFoundException, IOException
		{
			if(ba == null)
				return null;
			
			List<CachedRowSet> list = SqlArgs.decodeRowSet(ba.getValue());
			DataReader reader = null;
			if(list!=null)
			{
//				reader = new DataReader(list.get(0),null,null);
//				reader.setAllResultSet(list);
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
				
				ProtoByteArray result= DatabaseRpcClientDistributed.getInstance().spExecuteReader(dbName,sqlArgs);

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
				
				ProtoByteArray result= DatabaseRpcClientDistributed.getInstance().executeReader(dbName,sqlArgs);			
				
				List<DataTable> tables = createDataTables(result);
				
				return tables;

				
			} catch (Exception e) {
				LOGGER.error("{}", e);
				throw new SQLException(e);
			}
		}

		
	}
