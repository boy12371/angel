package com.feinno.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.pool.DBConnectionPoolType;

/**
 * 
 * <b>描述: </b>与数据库进行交互的类，主要为{@link Table}提供和数据库进行操作的协助类
 * <p>
 * <b>功能: </b>为{@link Table}提供和数据库进行操作的协助类
 * <p>
 * <b>用法: </b>此类与{@link Table}配合使用效果最佳
 * 
 * <pre>
 * String tableName = ...
 * MonDBHelper monDBHelper = DatabaseManager.getMonDBHelper("test", dbConfigs);//创建实例
 * 
 * Table table = new Table(tableName);
 * table.addColumns...
 * monDBHelper.createTable(table); //创建表
 * Table table = monDBHelper.getTable(tableName) //获得表结构
 * </pre>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class MonDBHelper extends Database {

	private String TABLE_SCHEMA = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(MonDBHelper.class);

	public MonDBHelper(String dbName, Properties configs) throws Exception {
		this(dbName, configs, DBConnectionPoolType.C3p0);
	}

	public MonDBHelper(String dbName, Properties configs, DBConnectionPoolType type) throws Exception {
		super(dbName, configs, type);
		if (configs.getProperty("Database") != null && configs.getProperty("Database").length() > 0) {
			TABLE_SCHEMA = configs.getProperty("Database");
		} else {
			String jdbcUrl = configs.getProperty("JdbcUrl");
			if (jdbcUrl != null && jdbcUrl.length() > 0 && jdbcUrl.lastIndexOf("/") > 0
					&& jdbcUrl.lastIndexOf("/") != jdbcUrl.length()) {
				TABLE_SCHEMA = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1, jdbcUrl.length());
				if (TABLE_SCHEMA.indexOf("?") > 0) {
					TABLE_SCHEMA = TABLE_SCHEMA.substring(0, TABLE_SCHEMA.indexOf("?"));
				}
			}
		}
	}

	/**
	 * 根据表结构创建数据库表
	 * 
	 * @param table
	 * @return
	 * @throws TableExistException
	 *             表已存在
	 * @throws SQLException
	 *             数据库访问错误
	 */
	public boolean createTable(Table table) throws TableExistException, SQLException {

		if (table == null || !table.check()) {
			return false;
		}

		// 判断是否存在相同的表名
		if (isExistTable(table.getTableName())) {
			throw new TableExistException(String.format("%s Table is Exits.", table.getTableName()));
		}
		// 不存在相同的表名,则创建此表
		return execute(table.toString());
	}

	/**
	 * 根据表名判断表是否存在
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 *             数据库访问错误
	 */
	public boolean isExistTable(String tableName) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// TODO 注意，此处使用的TABLE_SCHEMA未默认的test，切记需要修改
			conn = getConnection();
			stmt = conn
					.prepareStatement("select `TABLE_NAME` from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_SCHEMA`=? and `TABLE_NAME`=?");
			stmt.setString(1, TABLE_SCHEMA);
			stmt.setString(2, tableName);
			rs = (ResultSet) stmt.executeQuery();
			boolean isExist = rs.next();
			return isExist;
		} finally {
			DatabaseHelper.attemptClose(rs);
			DatabaseHelper.attemptClose(stmt);
			DatabaseHelper.attemptClose(conn);
		}
	}

	/**
	 * 根据表名获得数据库表结构
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public Table getTable(String tableName) throws SQLException {
		if (tableName == null || tableName.indexOf("'") != -1 || !isExistTable(tableName)) {
			return null;
		}
		Table table = null;
		Connection conn = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			conn = getConnection();

			// 取列内容
			rs1 = conn.getMetaData().getColumns(TABLE_SCHEMA, null, tableName, null);
			table = Table.valueOf(rs1);

			if (table == null) {
				return null;
			}

			// 如果有主键，则在此处添加主键到Table对象中
			rs2 = conn.getMetaData().getPrimaryKeys(null, null, tableName);
			while (rs2.next()) {
				table.addPrimaryKey(rs2.getObject(4).toString());
			}
		} catch (SQLException e) {
			// 数据库访问错误向上抛出异常，告诉调用者本地出错，返回数据可能有异常，需处理
			throw e;
		} finally {
			DatabaseHelper.attemptClose(rs1);
			DatabaseHelper.attemptClose(rs2);
			DatabaseHelper.attemptClose(conn);
		}
		return table;
	}

	/**
	 * 将表名修改为...
	 * 
	 * @param srcName
	 *            原始表表名
	 * @param descName
	 *            修改后的表名
	 * @return
	 * @throws SQLException
	 */
	public boolean renameTable(String srcName, String descName) throws TableExistException, TableNotExistException,
			SQLException {
		if (srcName == null || descName == null || srcName.indexOf("'") != -1 || descName.indexOf("'") != -1) {
			return false;
		}
		// 目标表名已存在,抛出异常
		if (isExistTable(descName)) {
			throw new TableExistException(String.format("descName:%s Table is Exist.", descName));
		}
		// 当源表存在时，才可以进行更名，否则更名失败
		if (isExistTable(srcName)) {
			return execute(String.format("ALTER  TABLE `%s` RENAME TO `%s`", srcName, descName));
		} else {
			throw new TableNotExistException(String.format("srcName:%s Table is not Exist.", srcName));
		}
	}

	/**
	 * 为此类提供的SQL语句执行方法
	 * 
	 * @param sql
	 * @return
	 */
	public boolean execute(String sql) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			return true;
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			try {
				DatabaseHelper.attemptClose(stmt);
				DatabaseHelper.attemptClose(conn);
			} catch (Exception ex) {
				LOGGER.error("{}", ex);
			}
		}
		return false;
	}
}
