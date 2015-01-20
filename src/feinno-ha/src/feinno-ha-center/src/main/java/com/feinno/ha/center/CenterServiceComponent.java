/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 22, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.center;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.spi.LocalConfigurator;
import com.feinno.ha.ServiceComponent;
import com.feinno.ha.ServiceSettings;
import com.feinno.ha.StartupOptionEnum;
import com.feinno.ha.WorkerAgentHA;
import com.feinno.ha.database.HADBConfigHelper;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.duplex.RpcDuplexServer;
import com.feinno.rpc.server.RpcServiceBootstrap;
import com.feinno.threading.ExecutorFactory;

/**
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class CenterServiceComponent implements ServiceComponent {

	/** 当前对外提供服务的引用 */
	private RpcDuplexServer server;

	/** 日志的引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CenterServiceComponent.class);

	/**
	 * 开始执行Center任务
	 */
	public void start() throws Exception {
		// Duplex Rpc服务
		int port = ServiceSettings.INSTANCE.getServicePort("rpc_duplex");
		LOGGER.info("Start HA-Center rpc_duplex services. port={}", port);
		RpcTcpServerChannel channel = new RpcTcpServerChannel(port);
		server = new RpcDuplexServer(channel);

		// MasterAgent管理
		LOGGER.info("Start HA-Center MasterAgent Services.");
		server.registerService(MasterAgentService.INSTANCE);

		// WorkerAgent管理
		LOGGER.info("Start HA-Center WorkerAgent Services.");
		server.registerService(WorkerAgentService.INSTANCE);
		server.registerService(new CenterDeploymentService());

		// 为Desktop提供的热部署相关服务
		LOGGER.info("Start HA-Center AppHotSwapService Services.");
		server.registerService(AppHotSwapService.INSTANCE);

		// 透传
		LOGGER.info("Start HA-Center MasterProxyService Services.");
		server.registerService(MasterProxyService.INSTANCE);
		LOGGER.info("Start HA-Center WorkerProxyService Services.");
		server.registerService(WorkerProxyService.INSTANCE);

		server.setExecutor(ExecutorFactory.newFixedExecutor("SERVER", 10, 10240));

		int port2 = ServiceSettings.INSTANCE.getServicePort("rpc");
		LOGGER.info("Start HA-Center rpc services. port={}", port2);
		RpcTcpServerChannel channel2 = new RpcTcpServerChannel(port2);
		RpcServiceBootstrap.registerChannel(channel2);
		RpcServiceBootstrap.registerService(new ConfigurationService());
		RpcServiceBootstrap.registerService(new CenterConsoleService());
		RpcServiceBootstrap.registerService(new CenterDeploymentService());
		RpcServiceBootstrap.setExecutor(ExecutorFactory.newFixedExecutor("SERVER", 10, 10240));
		channel.start();
		RpcServiceBootstrap.start();

		LOGGER.info("Start Base-Center heartbeat.");
		initBaseCenter();

		LOGGER.info("Start HA-Center Successfully!");
	}

	/**
	 * 初始化日志组件
	 */
	protected boolean initBaseCenter() {
		try {
			ConfigurationManager.setConfigurator(new LocalConfigurator());
			Properties centerProps = ConfigurationManager.loadProperties("BaseCenter.properties", null, null);
			if (centerProps != null && centerProps.getProperty("HA_CENTER_URL") != null) {
				LOGGER.info("Register Base-Center.");
				StartupOptionEnum.AGENT.setEnable(true);
				StartupOptionEnum.AGENT.addArgs(centerProps.getProperty("HA_CENTER_URL"));
				BaseCenterAgent baseCenterAgent = new BaseCenterAgent();
				baseCenterAgent.start();
			} else {
				LOGGER.info("Not Found Base-Center.");
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("Initialization Base-Center failed.", e);
			return false;
		}
	}

	/**
	 * 停止Center服务
	 */
	public void stop() throws Exception {
		LOGGER.info("Stop HA-Center.");
		System.exit(0);
	}
}

/**
 * 
 * <b>描述: </b>类似于WorkerAgentHA的一个东西
 * <p>
 * <b>功能: </b>用于和BascCenter进行通讯，通过保持心跳，来能够随时从BaseCenter中load配置
 * <p>
 * <b>用法: </b>启动后自动保持心跳
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
class BaseCenterAgent extends WorkerAgentHA {

	/** 日志对象引用 **/
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseCenterAgent.class);

	@Override
	public void start() throws Exception {
		// 将父类中的配置管理器倒腾出来
		HADBConfigHelper.baseCenterConfigurator = super.configurator;
		// 因为父类会将全局配置管理器覆盖，因此在这里重新用Local方式覆盖回去
		ConfigurationManager.setConfigurator(new LocalConfigurator());

		try {
			// 1. 向BaseCenter注册当前 Worker
			LOGGER.info("1. Register Base-Center.");
			register();

			LOGGER.info("2. Start Heartbeat Thread.");
			// 2. 如果不存在心跳线程，则创建Worker心跳线程，向远端的服务定时发送心跳包
			if (heartbeatThread == null) {
				heartbeatThread = new Thread(new Runnable() {
					@Override
					public void run() {
						heartbeatProc();
					}
				});
				// 启动心跳线程
				running = true;
				heartbeatThread.start();
			} else if (heartbeatThread != null && running == false) {
				// 如果存在心跳线程，但是线程不在运行，则重新start
				running = true;
				heartbeatThread.start();
			}
		} catch (Exception ex) {
			LOGGER.error("Start BaseCenterAgent failed, Reasons: ", ex);
			throw ex;
		}

	}

}
