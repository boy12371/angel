/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 23, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.center;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.database.ActiveConfigBean;
import com.feinno.ha.database.HADBConfigHelper;
import com.feinno.ha.interfaces.configuration.HAConfigArgs;
import com.feinno.ha.interfaces.worker.HAWorkerAgentCallbackService;

/**
 * <b>描述:
 * </b>该类作为配置信息的监视器，通过比对配置表中Version的变化，找出变更的配置，通知应用进行更新，当然，这些操作值针对订阅了自动更新的配置有效
 * <p>
 * <b>功能: </b>通过比对配置表中Version的变化，找出变更的配置，通知应用进行更新
 * <p>
 * <b>用法: </b>作为一个单独线程运行
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ConfigrationSubscriptionMonitor implements Runnable {

	/** 配置表监控的执行线程 */
	private static ScheduledExecutorService executor;

	/** 配置表监控的Future */
	private static ScheduledFuture<?> future;

	/** 当前对象的唯一实例 */
	private static ConfigrationSubscriptionMonitor INSTANCE = new ConfigrationSubscriptionMonitor();

	/** 每次监控轮训时的延迟时间 */
	private static final int MONITOR_DELAY = 10 * 1000;

	/** 日志对象的引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigrationSubscriptionMonitor.class);

	/**
	 * 一定是私有的，不是外部想创建一个实例就能创建一个实例的，因此是单例的
	 */
	private ConfigrationSubscriptionMonitor() {

	}

	/** 开启配置表变更的监控 */
	public synchronized static void start() {
		if (executor != null) {
			throw new RuntimeException("ConfigrationSubscriptionMonitor already in use.");
		}
		executor = Executors.newScheduledThreadPool(1);
		future = executor.scheduleWithFixedDelay(INSTANCE, MONITOR_DELAY, MONITOR_DELAY, TimeUnit.MILLISECONDS);
	}

	/**
	 * 关闭配置表变更的监控
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
		Thread.currentThread().setName("ConfigrationSubscriptionMonitor");
		try {
			// 1. 取得变化了版本的记录
			List<ActiveConfigBean> activeConfigList = HADBConfigHelper.getChangeVersionActiveConfig();
			// 2. 对变化了版本的记录进行通知，通知远端来取最新数据
			if (activeConfigList != null && activeConfigList.size() > 0) {
				for (ActiveConfigBean activeConfig : activeConfigList) {
					// 取出该配置对应的Worker连接，通知相应Worker来取最新配置
					WorkerAgent agent = WorkerAgentService.INSTANCE.getWorkerAgent(activeConfig.getServerName(),
							activeConfig.getWorkerPid());
					if (agent != null) {
						HAWorkerAgentCallbackService service = agent.getService(HAWorkerAgentCallbackService.class);
						HAConfigArgs args = new HAConfigArgs();
						args.setPath(activeConfig.getConfigKey());
						args.setParams(activeConfig.getConfigParams());
						args.setType(activeConfig.getConfigtype());
						service.notifyConfigExpired(args);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("error : ", e);
		}
	}
}
