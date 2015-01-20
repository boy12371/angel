/*
 * FAE, Feinno App Engine
 *  
 * Create by zhangyali 2010-12-14
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.logging.appender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

import com.feinno.database.DatabaseHelper;
import com.feinno.database.DatabaseManager;
import com.feinno.database.MonDBHelper;
import com.feinno.database.Table;
import com.feinno.logging.LogEvent;
import com.feinno.logging.common.LogCommon;

/**
 * 日志工具类,提供普通工具类方法,将日志信息保存导数据。
 * 
 * @author zhangyali
 */
public class JdbcAppender implements Appender {

	// 表后缀名称
	private static final String tableSuffix = "yyyyMMdd";

	/**
	 * 数据库地址
	 */
	private String databaseURL;
	/**
	 * 数据库用户名
	 */
	private String databaseUser;
	/**
	 * 数据库密码
	 */
	private String databasePassword;

	/**
	 * 数据库名称
	 */
	private String database;
	/**
	 * 数据库驱动
	 */
	private String driverClass;
	/**
	 * 数据库表前缀名称
	 */
	private String tablePrefix;

	/**
	 * 当前表名称
	 */
	private String currentTableName;

	/**
	 * 提前要创建的 表名称
	 */
	private String nextTableName;
	/**
	 * 插入的insert语句, 形如: insert into tableName(name,age,address) values(?,?,?)
	 */
	protected String insertSql;

	/**
	 * 实时记录日志时,存放日志记录,够一定的条数再插入数据库
	 */
	private Queue<LogEvent> list = new LinkedList<LogEvent>();

	/**
	 * 与数据库交互的Helper
	 */
	private MonDBHelper monDBHelper = null;
	/**
	 * 此Appender是否有效标志,默认为有效
	 */
	private boolean valid = true;

	/**
	 * 当前时间毫秒数,每天创建一个数据库表
	 */
	private long currentTime;
	/**
	 * 当 缓存不启用时, 用来记录操作时间
	 */
	private long flagTime;

	private void initDBHelper() {
		Properties properties = new Properties();
		properties.setProperty("DriverClass", driverClass);
		properties.setProperty("JdbcUrl", databaseURL);
		properties.setProperty("User", databaseUser);
		properties.setProperty("Password", databasePassword);
		if (database != null) {
			properties.setProperty("Database", database);
		}
		monDBHelper = DatabaseManager.getMonDBHelper("LogDB", properties);
	}

	/**
	 * 带参构造器
	 * 
	 * @param driverClass
	 *            数据库驱动
	 * @param databaseURL
	 *            数据库地址
	 * @param databaseUser
	 *            数据库用户名
	 * @param databasePassword
	 *            数据库密码
	 */
	public JdbcAppender(String databaseURL, String driverClass, String databaseUser, String databasePassword,
			String database, String tableName) {
		this.driverClass = driverClass;
		this.databaseURL = databaseURL;
		this.databaseUser = databaseUser;
		this.databasePassword = databasePassword;
		this.database = database;
		initDBHelper();
		tablePrefix = tableName;
		flagTime = System.currentTimeMillis();
		// 表后缀名称为当天的日期
		currentTableName = tableName + "_" + LogCommon.convertDate(tableSuffix);
		// 设置创建明天的 日志 表名
		nextTableName = tableName + "_" + LogCommon.convertNextDate(tableSuffix);

		// 创建当天数据库表
		Table table = createLogTableObject(currentTableName);
		// 创建当天数据库表
		Table tableNextDay = createLogTableObject(nextTableName);
		createTable(table);
		createTable(tableNextDay);

		// 创建插入sql语句
		this.insertSql = "insert into `"
				+ currentTableName
				+ "`(Time,LoggerName,Level,Message,Error,Marker,ThreadId,ThreadName,Pid,ServiceName,Computer) values(?,?,?,?,?,?,?,?,?,?,?)";
				//+ "`(Time,LoggerName,Level,Message,Error,Marker,ThreadId,ThreadName,Pid,ServiceName,Computer,UserIdentity,RequestId) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

		// 每天晚上 12点 创建数据库表
		Calendar calTime = Calendar.getInstance();
		calTime.set(Calendar.HOUR_OF_DAY, 0);
		calTime.set(Calendar.MINUTE, 0);
		calTime.set(Calendar.SECOND, 0);
		this.currentTime = calTime.getTimeInMillis();
	}

	/**
	 * 获取Appender是否可用
	 */
	public boolean isEnabled() {
		return valid;
	}

	/**
	 * 设置Appender是否可用 true-可用 false-不可用
	 */
	public void setValid(boolean enabled) {
		this.valid = enabled;
	}

	/**
	 * 向指定的数据库表插入批量日志数据
	 * 
	 * @param events
	 *            日志数据集合
	 */
	public void doAppend(Queue<LogEvent> events) {

		Connection connection = null;
		PreparedStatement pStmt = null;
		try {
			connection = monDBHelper.getConnection();

			if (System.currentTimeMillis() - currentTime >= 24 * 3600 * 1000)
				switchTable();
			// 关闭事务自动提交
			connection.setAutoCommit(false);
			pStmt = connection.prepareStatement(insertSql);
			// pStmt.isClosed();
			// pStmt.s
			// 批量插入的数据
			for (Iterator<LogEvent> it = events.iterator(); it.hasNext();) {
				insertEvent(pStmt, it.next());
				pStmt.addBatch();
			}
			pStmt.executeBatch();
			// 提交
			connection.commit();
		} catch (SQLException e) {

			// 出现异常时,再次记录数据库
			// close(pStmt, connection);
			// connection = null;
			firstAppend(events);
			System.err.println(new Date() + " dataBase connection error. " + LogCommon.formaError(e));
		} finally {
			DatabaseHelper.attemptClose(pStmt);
			DatabaseHelper.attemptClose(connection);
		}
	}

	/**
	 * 发生异常时,再次记入数据库
	 * 
	 * @param events
	 *            日志数据集合
	 */
	public void firstAppend(Queue<LogEvent> events) {
		Connection connection = null;
		PreparedStatement pStmt = null;
		try {
			connection = monDBHelper.getConnection();
			connection.setAutoCommit(false);
			pStmt = connection.prepareStatement(insertSql);
			// 批量插入的数据
			for (Iterator<LogEvent> it = events.iterator(); it.hasNext();) {
				insertEvent(pStmt, it.next());
				pStmt.addBatch();
			}
			pStmt.executeBatch();
			// 提交
			connection.commit();
		} catch (SQLException e) {
			// close(pStmt, connection);
			// connection = null;
			System.err.println(new Date() + "get dataBase connection error. " + LogCommon.formaError(e));
		} finally {
			DatabaseHelper.attemptClose(pStmt);
			DatabaseHelper.attemptClose(connection);
		}
	}

	/**
	 * 向指定的数据库表实时插入日志数据
	 * 
	 * @param events
	 *            日志
	 */
	public void doAppend(LogEvent evt) {

		list.add(evt);
		// 5条日志插入数据库一次
		if (list.size() >= 5) {
			doAppend(list);
			// 重新记录操作时间
			flagTime = System.currentTimeMillis();
			list.clear();
		}
		// 当list 集合中有日志记录,并且大于等于100毫秒时 开始向数据库插入
		else {
			long diffMs = System.currentTimeMillis() - flagTime;
			if (list.size() > 0 && (diffMs > 100 || diffMs < -100)) {
				doAppend(list);
				// 重新记录操作时间
				flagTime = System.currentTimeMillis();
				list.clear();
			}
		}
	}

	/**
	 * 插入数据
	 * 
	 * @param pStmt
	 *            PreparedStatement
	 * @param event
	 *            日志
	 * @throws SQLException
	 */
	private void insertEvent(PreparedStatement pStmt, LogEvent event) throws SQLException {
		int i = 1;
		pStmt.setTimestamp(i++, new Timestamp(event.getTimeStamp()));
		pStmt.setString(i++, event.getLoggerName());
		pStmt.setInt(i++, event.getLevel().intValue());
		pStmt.setString(i++, event.getFormattedMessage());
		pStmt.setString(i++, event.getThrowableString());
		pStmt.setString(i++, event.getMarkerString());
		pStmt.setLong(i++, event.getThreadId());
		pStmt.setString(i++, event.getThreadName());
		pStmt.setInt(i++, event.getPid());
		pStmt.setString(i++, event.getServiceName());
		pStmt.setString(i++, event.getComputer());
		//pStmt.setString(i++, event.getUserIdentify());
		//pStmt.setString(i++, event.getRequestId());
	}

	/**
	 * 将日志事件转换为字符串
	 * 
	 * @param event
	 *            日志事件
	 * @return 返回字符串
	 */
	public String convertEvent(LogEvent event) {
		return event.toString();
	}

	public synchronized void switchTable() {
		// 如果创建数据表失败，则开启一个新的线程每间隔指定时间创建一次
		if (!createTable()) {
			Thread createTableThread = new Thread(new Runnable() {
				@Override
				public void run() {
					int max = 43200;
					int count = 0;
					while (!createTable() && count < max) {
						try {
							// 如果创建失败,则每间隔2秒重新创建一次,但是总次数不能高于43200次，
							// 这个限制是为了防止该线程无限度的跑下去，这个次数正好是24小时的量
							count++;
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							System.err.println(new Date() + LogCommon.formaError(e));
						}
					}
				}
			});
			createTableThread.setName("LogDBCreater_" + LogCommon.convertDate("yyyyMMddHHmmss"));
			createTableThread.start();
		}
	}

	/**
	 * 每天创建一张表,并重新 设置 创建和插入语句.注意此方法的执行顺序很重要
	 */
	public synchronized boolean createTable() {
		try {
			// 重新为当前表命名
			currentTableName = tablePrefix + "_" + LogCommon.convertDate(tableSuffix);
			nextTableName = tablePrefix + "_" + LogCommon.convertNextDate(tableSuffix);

			// 创建当天数据库表
			final Table table = createLogTableObject(currentTableName);
			// 创建下一天天数据库表
			final Table tableNextDay = createLogTableObject(nextTableName);
			// 重新设置当前时间毫秒数
			Calendar calTime = Calendar.getInstance();
			calTime.set(Calendar.HOUR_OF_DAY, 0);
			calTime.set(Calendar.MINUTE, 0);
			calTime.set(Calendar.SECOND, 0);
			this.currentTime = calTime.getTimeInMillis();

			boolean isSuccess = createTable(table) && createTable(tableNextDay);
			if (isSuccess) {
				// 如果创建数据表成功，则更新SQL语句
				this.insertSql = "insert into `"
						+ currentTableName
						+ "`(Time,LoggerName,Level,Message,Error,Marker,ThreadId,ThreadName,Pid,ServiceName,Computer) values(?,?,?,?,?,?,?,?,?,?,?)";
				//+ "`(Time,LoggerName,Level,Message,Error,Marker,ThreadId,ThreadName,Pid,ServiceName,Computer,UserIdentity,RequestId) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			}
			return isSuccess;
		} catch (Exception e) {
			System.err.println(new Date() + LogCommon.formaError(e));
			return false;
		}
	}

	private synchronized boolean createTable(Table table) {

		try {
			if (monDBHelper.isExistTable(table.getTableName())) {
				// Step3. 有相同的表名，判断表结构是否相同
				if (!table.equals(monDBHelper.getTable(table.getTableName()))) {
					// Step4. 表结构不相同，则修改数据库中的表名，修改后再创建新表
					monDBHelper.renameTable(table.getTableName(), table.getTableName() + System.currentTimeMillis());
					monDBHelper.createTable(table);
				}
			} else {
				// Step5. 没有相同的表名,直接创建既可
				monDBHelper.createTable(table);
			}
		} catch (Exception e) {
			System.err.println(new Date() + LogCommon.formaError(e));
			return false;
		}
		return true;
	}

	/**
	 * 创建一个新的日志表结构
	 * 
	 * @param tablename
	 * @return
	 */
	private Table createLogTableObject(String tablename) {
		Table table = new Table(tablename);
		table.addColumns(Table.Column.createDateTimeColumn("Time", false, null));
		table.addColumns(Table.Column.createVarcharColumn("LoggerName", 256, false, null));
		table.addColumns(Table.Column.createIntColumn("Level", false, null));
		table.addColumns(Table.Column.createTextColumn("Message", true));
		table.addColumns(Table.Column.createTextColumn("Error", true));
		table.addColumns(Table.Column.createVarcharColumn("Marker", 256, true, null));
		table.addColumns(Table.Column.createIntColumn("ThreadId", false, null));
		table.addColumns(Table.Column.createVarcharColumn("ThreadName", 128, false, null));
		table.addColumns(Table.Column.createIntColumn("Pid", false, null));
		table.addColumns(Table.Column.createVarcharColumn("ServiceName", 128, true, null));
		table.addColumns(Table.Column.createVarcharColumn("Computer", 128, true, null));
		//table.addColumns(Table.Column.createVarcharColumn("UserIdentity", 128, true, null));
		//table.addColumns(Table.Column.createVarcharColumn("RequestId", 128, true, null));
		String extension = "KEY `IX_Time` (`Time`) USING BTREE,KEY `IX_LoggerName` (`LoggerName`) USING BTREE,KEY `IX_Marker` (`Marker`) USING BTREE";
		table.setExtension(extension);
		return table;
	}

//	/**
//	 * 获取数据库连接
//	 */
//	private synchronized Connection getConnection() throws SQLException {
//		if (monDBHelper != null) {
//			connection = monDBHelper.getConnection();
//		} else {
//			System.err.print("LogDB is null.");
//		}
//		return connection;
//	}

//	/**
//	 * 关闭数据库连接
//	 * 
//	 * @param pStmt
//	 *            PreparedStatement
//	 * @param con
//	 *            Connection
//	 */
//	private void close(PreparedStatement pStmt, Connection con) {
//		if (pStmt != null) {
//			try {
//				pStmt.close();
//			} catch (SQLException e) {
//				System.err.println(new Date() + LogCommon.formaError(e));
//			}
//		}
//		if (con != null) {
//			try {
//				con.close();
//			} catch (SQLException e) {
//				System.err.println(new Date() + LogCommon.formaError(e));
//			}
//			con = null;
//		}
//	}

	public void destroy() {

	}

}
