/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-18
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.database;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>描述：</b>即将废弃，全部采用{@link DataTable}处理 <br/>
 * <p>
 * 此类主要功能是代理ResultSet对象，重写ResultSet的close方法。
 * 在使用完DataReader后，必须显示调用close()方法，用来释放相关的资源，如ResultSet、Statement、Connection；
 * 
 * @author 高磊 gaolei@feinno.com
 */
@Deprecated
public class DataReader
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DataReader.class);
	
	private Date openedTime;
	private ResultSet rs;
	//private Statement stmt;
	private Connection conn;
	private boolean flag = true;
	private boolean closed = false;
	
	//DBOverRpc DataReader的所有ResultSet存储在此
	private List<CachedRowSet> allResultSet = new ArrayList<CachedRowSet>();
	private int curResultSet = 0;
	//sql 语句 排查问题用
	private String sql = "";

	/**
	 * 构造函数，在DataReader执行close方法时关闭connection
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	public DataReader(List<CachedRowSet> cachedRowSetList, Connection conn)
	{
		this(cachedRowSetList, conn, true);
	}
	
	/**
	 * 构造函数，在DataReader执行close方法时如果flag参数为true关闭connection，为false不关闭connection
	 * @param rs
	 * @param stmt
	 * @param conn
	 * @param flag	
	 */
	public DataReader(List<CachedRowSet> cachedRowSetList, Connection conn, boolean flag)
	{
		DataReaderGuardian.INSTANCE.checkReaders();
		if(cachedRowSetList!=null && cachedRowSetList.size()>0)
			this.rs = cachedRowSetList.get(0);
		else
			this.rs = null;
		allResultSet = cachedRowSetList;
		this.conn = conn;
		this.flag = flag;
		this.openedTime = new Date();
		this.closed = false;
		
		DataReaderGuardian.INSTANCE.addReader(this);
	}	

	/**
	 * 主要重写的close方法
	 */
	public void close()
	{
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				LOGGER.error("rs close failed", e);
			}
		}
		
//		if (stmt != null) {
//			try {
//				stmt.close();
//			} catch (SQLException e) {
//				LOGGER.error("stmt close failed", e);
//			}
//		}
		
		if ((conn != null) && flag) {
			try {
				conn.close();
			} catch (SQLException e) {
				LOGGER.error("conn close failed", e);
			}
		}
		
		allResultSet = null;
		
		closed = true;
	}
	
	public Date getOpenedTime()
	{
		return openedTime;
	}
	
	/**
	 * 等同ResultSet的next()方法
	 * @return
	 * @throws SQLException
	 */
	public boolean read() throws SQLException
	{
		if(rs!=null && rs.getMetaData().getColumnCount()>0)
			return rs.next();
		else
			return false;
	}

	public boolean wasNull() throws SQLException
	{
		return rs.wasNull();
	}

	public String getString(int columnIndex) throws SQLException
	{
		return rs.getString(columnIndex);
	}

	public boolean getBoolean(int columnIndex) throws SQLException
	{
		//return rs.getBoolean(columnIndex);
		return (rs.getByte(columnIndex) == 1);
	}
	
	public byte getByte(int columnIndex) throws SQLException
	{
		return rs.getByte(columnIndex);
	}

	public short getShort(int columnIndex) throws SQLException
	{
		return rs.getShort(columnIndex);
	}

	public int getInt(int columnIndex) throws SQLException
	{
		return rs.getInt(columnIndex);
	}

	public long getLong(int columnIndex) throws SQLException
	{
		return rs.getLong(columnIndex);
	}

	public float getFloat(int columnIndex) throws SQLException
	{
		return rs.getFloat(columnIndex);
	}

	public double getDouble(int columnIndex) throws SQLException
	{
		return rs.getDouble(columnIndex);
	}

//	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException
//	{
//		return rs.getBigDecimal(columnIndex, scale);
//	}

	public byte[] getBytes(int columnIndex) throws SQLException
	{
		return rs.getBytes(columnIndex);
	}

	public java.util.Date getDateTime(int columnIndex) throws SQLException
	{
		return (java.util.Date)rs.getObject(columnIndex);
	}


	public java.sql.Timestamp getTimestamp(int columnIndex) throws SQLException
	{
		return rs.getTimestamp(columnIndex);
	}

	public java.io.InputStream getAsciiStream(int columnIndex) throws SQLException
	{
		return rs.getAsciiStream(columnIndex);
	}

//	public java.io.InputStream getUnicodeStream(int columnIndex) throws SQLException
//	{
//		return rs.getUnicodeStream(columnIndex);
//	}

	public java.io.InputStream getBinaryStream(int columnIndex) throws SQLException
	{
		return rs.getBinaryStream(columnIndex);
	}

	public String getString(String columnLabel) throws SQLException
	{
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getString(columnIndex);
		//return rs.getString(columnLabel);
	}

	public boolean getBoolean(String columnLabel) throws SQLException
	{
		//return rs.getBoolean(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return (rs.getByte(columnIndex) == 1);
	}
	
	public byte getByte(String columnLabel) throws SQLException
	{
		//return rs.getByte(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getByte(columnIndex);
	}

	public short getShort(String columnLabel) throws SQLException
	{
		//return rs.getShort(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getShort(columnIndex);
	}

	public int getInt(String columnLabel) throws SQLException
	{
		//return rs.getInt(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getInt(columnIndex);
	}

	public long getLong(String columnLabel) throws SQLException
	{
		//return rs.getLong(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getLong(columnIndex);
	}

	public float getFloat(String columnLabel) throws SQLException
	{
		//return rs.getFloat(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getFloat(columnIndex);
	}

	public double getDouble(String columnLabel) throws SQLException
	{
		//return rs.getDouble(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getDouble(columnIndex);
	}

//	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException
//	{
//		return rs.getBigDecimal(columnLabel, scale);
//	}

	public byte[] getBytes(String columnLabel) throws SQLException
	{
		//return rs.getBytes(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getBytes(columnIndex);
	}

	public java.util.Date getDateTime(String columnLabel) throws SQLException
	{
		//return (java.util.Date)rs.getObject(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return (java.util.Date)rs.getObject(columnIndex);
	}


	public java.sql.Timestamp getTimestamp(String columnLabel) throws SQLException
	{
		//return rs.getTimestamp(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getTimestamp(columnIndex);
	}

	public java.io.InputStream getAsciiStream(String columnLabel) throws SQLException
	{
		//return rs.getAsciiStream(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getAsciiStream(columnIndex);
	}

	public java.io.InputStream getBinaryStream(String columnLabel) throws SQLException
	{
		//return rs.getBinaryStream(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getBinaryStream(columnIndex);
	}

	public SQLWarning getWarnings() throws SQLException
	{
		return rs.getWarnings();
	}

	public void clearWarnings() throws SQLException
	{
		rs.clearWarnings();
	}

	public String getCursorName() throws SQLException
	{
		return rs.getCursorName();
	}

	public ResultSetMetaData getMetaData() throws SQLException
	{
		return rs.getMetaData();
	}

	public Object getObject(int columnIndex) throws SQLException
	{
		return rs.getObject(columnIndex);
	}

	public Object getObject(String columnLabel) throws SQLException
	{
		//return rs.getObject(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getObject(columnIndex);
	}

	public int findColumn(String columnLabel) throws SQLException
	{
		//return rs.findColumn(columnLabel);
		return getColIdxByColumnLabel(columnLabel);
	}

	public java.io.Reader getCharacterStream(int columnIndex) throws SQLException
	{
		return rs.getCharacterStream(columnIndex);
	}

	public java.io.Reader getCharacterStream(String columnLabel) throws SQLException
	{
		//return rs.getCharacterStream(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getCharacterStream(columnIndex);
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException
	{
		return rs.getBigDecimal(columnIndex);
	}

	public BigDecimal getBigDecimal(String columnLabel) throws SQLException
	{
		//return rs.getBigDecimal(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getBigDecimal(columnIndex);
	}

//	public boolean isBeforeFirst() throws SQLException
//	{
//		return rs.isBeforeFirst();
//	}
//
//	public boolean isAfterLast() throws SQLException
//	{
//		return rs.isAfterLast();
//	}
//
//	public boolean isFirst() throws SQLException
//	{
//		return rs.isFirst();
//	}
//
//	public boolean isLast() throws SQLException
//	{
//		return rs.isLast();
//	}
//
//	public void beforeFirst() throws SQLException
//	{
//		rs.beforeFirst();
//	}
//
//	public void afterLast() throws SQLException
//	{
//		rs.afterLast();
//	}
//
//	public boolean first() throws SQLException
//	{
//		return rs.first();
//	}
//
//	public boolean last() throws SQLException
//	{
//		return rs.last();
//	}

	public int getRow() throws SQLException
	{
		return rs.getRow();
	}

	public boolean absolute(int row) throws SQLException
	{
		return rs.absolute(row);
	}

	public boolean relative(int rows) throws SQLException
	{
		return rs.relative(rows);
	}

	public boolean previous() throws SQLException
	{
		return rs.previous();
	}

	public void setFetchDirection(int direction) throws SQLException
	{
		rs.setFetchDirection(direction);
	}

	public int getFetchDirection() throws SQLException
	{
		return rs.getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException
	{
		rs.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException
	{
		return rs.getFetchSize();
	}

	public int getType() throws SQLException
	{
		return rs.getType();
	}

	public int getConcurrency() throws SQLException
	{
		return rs.getConcurrency();
	}

	public boolean rowUpdated() throws SQLException
	{
		return rs.rowUpdated();
	}

	public boolean rowInserted() throws SQLException
	{
		return rs.rowInserted();
	}

	public boolean rowDeleted() throws SQLException
	{
		return rs.rowDeleted();
	}

	public void refreshRow() throws SQLException
	{
		rs.refreshRow();
	}

	public void moveToInsertRow() throws SQLException
	{
		rs.moveToInsertRow();
	}

	public void moveToCurrentRow() throws SQLException
	{
		rs.moveToCurrentRow();
	}

	public Statement getStatement() throws SQLException
	{
		return rs.getStatement();
	}

	public Object getObject(int columnIndex, java.util.Map<String, Class<?>> map) throws SQLException
	{
		return rs.getObject(columnIndex, map);
	}

	public Ref getRef(int columnIndex) throws SQLException
	{
		return rs.getRef(columnIndex);
	}

	public Blob getBlob(int columnIndex) throws SQLException
	{
		return rs.getBlob(columnIndex);
	}

	public Clob getClob(int columnIndex) throws SQLException
	{
		return rs.getClob(columnIndex);
	}

	public Array getArray(int columnIndex) throws SQLException
	{
		return rs.getArray(columnIndex);
	}

	public Object getObject(String columnLabel, java.util.Map<String, Class<?>> map) throws SQLException
	{
		//return rs.getObject(columnLabel, map);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getObject(columnIndex, map);
	}

	public Ref getRef(String columnLabel) throws SQLException
	{
		//return rs.getRef(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getRef(columnIndex);
	}

	public Blob getBlob(String columnLabel) throws SQLException
	{
		//return rs.getBlob(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getBlob(columnIndex);
	}

	public Clob getClob(String columnLabel) throws SQLException
	{
		//return rs.getClob(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getClob(columnIndex);
	}

	public Array getArray(String columnLabel) throws SQLException
	{
		//return rs.getArray(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getArray(columnIndex);
	}
/*
	public java.sql.Date getDate(int columnIndex, Calendar cal) throws SQLException
	{
		return rs.getDate(columnIndex, cal);
	}

	public java.sql.Date getDate(String columnLabel, Calendar cal) throws SQLException
	{
		return rs.getDate(columnLabel, cal);
	}

	public java.sql.Time getTime(int columnIndex, Calendar cal) throws SQLException
	{
		return rs.getTime(columnIndex, cal);
	}

	public java.sql.Time getTime(String columnLabel, Calendar cal) throws SQLException
	{
		return rs.getTime(columnLabel, cal);
	}

	public java.sql.Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException
	{
		return rs.getTimestamp(columnIndex, cal);
	}

	public java.sql.Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException
	{
		return rs.getTimestamp(columnLabel, cal);
	}*/

	public java.net.URL getURL(int columnIndex) throws SQLException
	{
		return rs.getURL(columnIndex);
	}

	public java.net.URL getURL(String columnLabel) throws SQLException
	{
		//return rs.getURL(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getURL(columnIndex);
	}

//	public RowId getRowId(int columnIndex) throws SQLException
//	{
//		return rs.getRowId(columnIndex);		
//	}
//
//	public RowId getRowId(String columnLabel) throws SQLException
//	{
//		//return rs.getRowId(columnLabel);
//		int columnIndex = getColIdxByColumnLabel(columnLabel);
//		return rs.getRowId(columnIndex);
//	}

//	public int getHoldability() throws SQLException
//	{
//		return rs.getHoldability();
//	}

//	public boolean isClosed() throws SQLException
//	{
//		return rs.isClosed();
//	}

	public NClob getNClob(int columnIndex) throws SQLException
	{
		return rs.getNClob(columnIndex);
	}

	public NClob getNClob(String columnLabel) throws SQLException
	{
		//return rs.getNClob(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getNClob(columnIndex);
	}

	public SQLXML getSQLXML(int columnIndex) throws SQLException
	{
		return rs.getSQLXML(columnIndex);
	}

	public SQLXML getSQLXML(String columnLabel) throws SQLException
	{
		//return rs.getSQLXML(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getSQLXML(columnIndex);
	}

	public String getNString(int columnIndex) throws SQLException
	{
		return rs.getNString(columnIndex);
	}

	public String getNString(String columnLabel) throws SQLException
	{
		//return rs.getNString(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getNString(columnIndex);
	}

	public java.io.Reader getNCharacterStream(int columnIndex) throws SQLException
	{
		return rs.getNCharacterStream(columnIndex);
	}

	public java.io.Reader getNCharacterStream(String columnLabel) throws SQLException
	{
		//return rs.getNCharacterStream(columnLabel);
		int columnIndex = getColIdxByColumnLabel(columnLabel);
		return rs.getNCharacterStream(columnIndex);
	}
 	
	public void nextResult() throws SQLException
	{
//		if(stmt != null)//本地连接模式
//		{
//			stmt.getMoreResults();
//			rs = stmt.getResultSet();
//		}
//		else //DBOverRpc
//		{
			curResultSet++;
			if(curResultSet < allResultSet.size())
				rs = allResultSet.get(curResultSet);
			else
				rs = null;
//		}
	}

	@Override
	public String toString()
	{
		//return stmt.toString();
		try {
			return rs.getMetaData().getTableName(1);
		} catch (SQLException e) {
			return "unknown table";
		}
	}
	
	public boolean closed()
	{
		return closed;
	}

	public List<CachedRowSet> getAllResultSet() {
		return allResultSet;
	}

	public void setAllResultSet(List<CachedRowSet> moreResultSet) {
		this.allResultSet = moreResultSet;
	}
	
	public ResultSet getCurResultSet()
	{
		return rs;
	}

    private int getColIdxByColumnLabel(String columnLabel)
            throws SQLException
    {
    	ResultSetMetaData rowSetMD = rs.getMetaData();
        int i = rowSetMD.getColumnCount();
        for(int j = 1; j <= i; j++)
        {
            String s1 = rowSetMD.getColumnLabel(j);
            if(s1 != null && columnLabel.equalsIgnoreCase(s1))
                return j;
        }
        throw new SQLException("cachedrowsetimpl.invalcolnm");
    }

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
    
    

}
