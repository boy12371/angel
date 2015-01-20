package com.feinno.ha.center;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.database.HADBConfigHelper;
import com.feinno.ha.database.HADBStatusHelper;

/**
 * 
 * <b>描述:
 * </b>这是WorkerEndpoint/MasterEndpoint的监控，它是做为一个单独的线程，在每间隔指定时间对WorkerEndpoint
 * /MasterEndpoint表进行扫描， 寻找到过期或失去连接的Worker或Master，对其服务状态进行设置
 * <p>
 * <b>功能: </b>它是做为一个单独的线程，在每间隔指定时间对WorkerEndpoint、MasterEndpoint表进行扫描，
 * 寻找到过期或是去连接的Worker或Master，对其服务状态进行设置
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class EndpointMonitor implements Runnable {

	/** WorkerAgent监控的执行线程 */
	private static ScheduledExecutorService executor;

	/** WorkerAgent监控的Future */
	private static ScheduledFuture<?> future;

	/** WorkerAgent的唯一实例 */
	private static EndpointMonitor INSTANCE = new EndpointMonitor();

	/** 每次监控轮训时的延迟时间，默认为一分钟 */
	private static final int MONITOR_DELAY = 60 * 1000;

	/** 至连接过期的时间,单位为分钟,默认为十分钟 */
	private static final int updateExpiredMinute = 10;

	/** 删除连接过期的时间,单位为小时,默认为24 */
	private static final int removeExpiredHour = 24;

	/** 日志对象的引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EndpointMonitor.class);

	/**
	 * 一定是私有的，不是外部想创建一个实例就能创建一个实例的，因此是单例的
	 */
	private EndpointMonitor() {

	}

	/** 开启WorkerAgent的运行状态监控 */
	public synchronized static void start() {
		if (executor != null) {
			throw new RuntimeException("WorkerAgentMonitor already in use.");
		}
		executor = Executors.newScheduledThreadPool(1);
		future = executor.scheduleWithFixedDelay(INSTANCE, MONITOR_DELAY, MONITOR_DELAY, TimeUnit.MILLISECONDS);
	}

	/**
	 * 关闭WorkerAgent的运行状态监控
	 */
	public synchronized static void stop() {
		if (future != null && executor != null) {
			future.cancel(true);
			executor.shutdown();
			executor = null;
			future = null;
		}
	}

	public void run() {
		Thread.currentThread().setName("EndpointMonitor");
		try {

			// 1. 更新超时的Master连接为过期
			HADBStatusHelper.updateExpiredMasterConnected(updateExpiredMinute);

			// 2. 更新超时的Worker连接为过期
			HADBStatusHelper.updateExpiredWorkerConnected(updateExpiredMinute);

			// 3. 删除超时的连接
			HADBStatusHelper.removeStopWorkerPoint(removeExpiredHour);

			// 4. 删除超时的连接同时移除HA_ActiveConfig表中的对应失效数据
			HADBConfigHelper.removeExpiredActiveConfig();

		} catch (Exception e) {
			LOGGER.error("error : ", e);
		}
	}
}
