/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 23, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;

/**
 * <b>描述: </b>该类作为系统清理工具类，定期执行清理工作
 * <p>
 * <b>功能: </b>该类作为系统清理工具类，定期执行清理工作
 * <p>
 * <b>用法: </b>作为一个单独线程运行
 * <p>
 * 
 * @author Lv.Mingwei
 */
public class SystemCleaner implements Runnable {

	/** 监控的执行线程 */
	private static ScheduledExecutorService executor;

	/** 配置表监控的Future */
	private static ScheduledFuture<?> future;

	/** 当前对象的唯一实例 */
	private static SystemCleaner INSTANCE = new SystemCleaner();

	/** 扩展任务 */
	private static List<Runnable> EXTENSION_TASK = Collections.synchronizedList(new ArrayList<Runnable>());

	/** 每次监控轮训时的延迟时间默认为24个小时 */
	private static final int MONITOR_DELAY = 24 * 60 * 60 * 1000;

	private static final int AFTER_TIME = -7;

	private static final String GET_LOGDB_TABLE_NAMES = "select `TABLE_NAME` from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_NAME` like 'HA_Logging_%'";

	private static final String GET_MONDB_TABLE_NAMES = "select `TABLE_NAME` from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_NAME` like 'OBR_CLIENT_%'";

	private static final String DORP_TABLE_SQL = "DROP TABLE IF EXISTS ";

	/** 日志对象的引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemCleaner.class);

	/**
	 * 一定是私有的，不是外部想创建一个实例就能创建一个实例的，因此是单例的
	 */
	private SystemCleaner() {

	}

	/** 开启监控 */
	public synchronized static void start() {
		if (executor != null) {
			throw new RuntimeException("SystemCleaner already in use.");
		}
		LOGGER.info("SystemCleaner start.");
		executor = Executors.newScheduledThreadPool(1);
		Calendar nowCalendar = Calendar.getInstance();
		Calendar afterClaendar = Calendar.getInstance();
		afterClaendar.add(Calendar.DATE, 1);
		afterClaendar.set(Calendar.HOUR_OF_DAY, 0);
		afterClaendar.set(Calendar.MINUTE, 0);
		afterClaendar.set(Calendar.MILLISECOND, 0);
		afterClaendar.set(Calendar.SECOND, 0);
		long delayTime = afterClaendar.getTimeInMillis() - nowCalendar.getTimeInMillis();
		LOGGER.info("SystemCleaner delay time {} ms.", delayTime);
		future = executor.scheduleWithFixedDelay(INSTANCE, delayTime, MONITOR_DELAY, TimeUnit.MILLISECONDS);
	}

	/**
	 * 关闭监控
	 */
	public synchronized static void stop() {
		if (future != null && executor != null) {
			future.cancel(true);
			executor.shutdown();
			executor = null;
			future = null;
		}
	}

	/**
	 * 添加一个扩展的定时任务
	 * 
	 * @param action
	 */
	public static void putExtensionTask(Runnable runnable) {
		EXTENSION_TASK.add(runnable);
	}

	public void run() {
		Thread.currentThread().setName("SystemCleaner");
		cleanLogDB();
		cleanMonDB();
		execExtensionTask();
	}

	/**
	 * 清理LogDB
	 * 
	 * @return
	 */
	public boolean cleanLogDB() {
		try {
			LOGGER.info("SystemCleaner start.");
			LOGGER.info("Remove LogDB.");
			Database logDB = DatabaseManager.getDatabase("LogDB");
			logDB = logDB != null ? logDB : DatabaseManager.getDatabase("MonDBHelper_LogDB");
			if (logDB != null) {
				DataTable table = logDB.executeTable(GET_LOGDB_TABLE_NAMES);
				LOGGER.info("Found LogDB {} tables. ", table.getRowCount());
				if (table.getRowCount() > 0) {
					for (DataRow row : table.getRows()) {
						String tableName = row.getString("TABLE_NAME");
						if (after(tableName, AFTER_TIME)) {
							try {
								LOGGER.info("Drop LogDB {}", tableName);
								logDB.executeNonQuery(DORP_TABLE_SQL + "`" + tableName + "`");
							} catch (Exception e) {
								LOGGER.warn("Drop LogDB failed. ", e);
							}
						}
					}
				}
				return true;
			} else {
				LOGGER.warn("LogDB and MonDBHelper_LogDB is null.");
			}
		} catch (Exception e) {
			LOGGER.error("SystemCleaner error : ", e);
		}
		return false;
	}

	/**
	 * 清理MonDB
	 * 
	 * @return
	 */
	public boolean cleanMonDB() {
		try {
			LOGGER.info("Remove MonDB.");
			Database monDB = DatabaseManager.getDatabase("MonDB");
			monDB = monDB != null ? monDB : DatabaseManager.getDatabase("MonDBHelper_MonDB");
			if (monDB != null) {
				DataTable table = monDB.executeTable(GET_MONDB_TABLE_NAMES);
				LOGGER.info("Found MonDB {} tables. ", table.getRowCount());
				if (table.getRowCount() > 0) {
					for (DataRow row : table.getRows()) {
						String tableName = row.getString("TABLE_NAME");
						if (after(tableName, AFTER_TIME)) {
							try {
								LOGGER.info("Drop MonDB {}", tableName);
								monDB.executeNonQuery(DORP_TABLE_SQL + "`" + tableName + "`");
							} catch (Exception e) {
								LOGGER.warn("Drop MonDB failed. ", e);
							}
						}
					}
				}
				return true;
			} else {
				LOGGER.warn("MonDB and MonDBHelper_MonDB is null.");
			}
		} catch (Exception e) {
			LOGGER.error("SystemCleaner error : ", e);
		}
		return false;
	}

	/**
	 * 执行扩展任务
	 * 
	 * @return
	 */
	public boolean execExtensionTask() {
		try {
			LOGGER.info("exec extension task.");
			for (Runnable runnable : EXTENSION_TASK) {
				runnable.run();
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("SystemCleaner error : ", e);
		}
		return false;
	}

	public boolean after(String dateStr, int amount) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = simpleDateFormat.parse(dateStr.substring(dateStr.length() - 8, dateStr.length()));
			Calendar cDate = Calendar.getInstance();
			cDate.setTime(date);
			Calendar afterDate = Calendar.getInstance();
			afterDate.add(Calendar.DATE, amount);
			return afterDate.after(cDate);
		} catch (Exception e) {
			LOGGER.error("after error. ", e);
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		// Properties props = new Properties();
		// props.load(new FileInputStream("mondb.properties"));
		// DatabaseManager.getDatabase("MonDB", props);
		// SystemCleaner.start();
	}
}