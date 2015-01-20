/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-26
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.ha;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.diagnostic.perfmon.monitor.MonitorHttpServer;
import com.feinno.diagnostic.perfmon.monitor.rpc.MonitorTcpServer;

/**
 * 
 * 所有基于HA环境的入口类<br>
 * USAGE: 两种启动方式
 * <p>
 * Usage:<br>
 * 外壳注册启动调用<br>
 * gen -ha 8090 -agent tcp://192.168.1.100:8080/ -ports rpc_tcp=7068
 * monitor=7820 rpc_http=7077 -log level=INFO
 * 
 * 测试启动 gen -agent
 * 
 * 本地非Agent调试，使用数据库配置启动 gen -config config.properties
 * 
 * 其他参数 -server SERVER-01 伪造服务器名 -workerId IBS 伪造WorkerId
 * </p>
 * <p>
 * 1. 读取启动参数<br>
 * 2. 通过启动方式(local,ha,test), 决定如何构建ServiceSettings类<br>
 * 3. 根据是否为HA模式，创建WorkerAgent 4. 根据配置模式，设置Configuration<br>
 * 5. 设置Logging, 在此之前, Logging为默认的INFO级别, 且输出到标准输出上<br>
 * 6. 根据是否受控, <br>
 * - 创建WorkerControllee, 接受请求并启动服务 <br>
 * - 直接启动服务 <br>
 * 7. 启动WorkerAgent线程
 * </p>
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class Genesis {

	/** 日志引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Genesis.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		// Step 1.如果没有参数，则使用默认初始化的Service
		if (args == null || args.length == 0) {
			args = new String[] { "-config", "HADB.properties" };
		}

		// Step 2.初始化启动所需要的对象引用
		WorkerAgent agent = null;
		ServiceSettings settings = ServiceSettings.INSTANCE;

		// Step 3.启动开始
		try {
			LOGGER.info("1. Load ha.xml.");
			settings.loadFromXml();

			LOGGER.info("2. Assign and verify startup options.");
			settings.assignOpts(args);
			settings.verifyConfig();

			LOGGER.info("3. Initialization WorkerAgent.");
			if (settings.getHACenterEp() != null) {
				LOGGER.info("Use HACenter WorkerAgent.");
				WorkerAgentHA agentHA = new WorkerAgentHA();
				ServiceSettings.INSTANCE.setWorkerDuplexClient(agentHA.getDuplexClient());
				agent = agentHA;
			} else {
				LOGGER.info("Use Local WorkerAgent.");
				agent = new WorkerAgentLocal();
			}

			LOGGER.info("4. Initialization HA controller.");
			if (settings.getControllerEp() != null) {
				LOGGER.info("Use HAMaster controller.");
				WorkerControllee controllee = new WorkerControllee(agent);
				controllee.register();
				ServiceSettings.INSTANCE.setControlleeDuplexClient(controllee.getDuplexClient());
			} else {
				LOGGER.info("Use Local controller.");
				agent.start();
				LOGGER.info("Service Starts Successfully!");
				consoleMonitor(agent);
			}
			LOGGER.info("5. Initialization Rpc monitor.");
			new MonitorTcpServer();
			LOGGER.info("Start RpcMonitor.");

			int port = ServiceSettings.INSTANCE.getServicePort("monitor");
			if (port > 0) {
				try {
					new MonitorHttpServer(port);
					LOGGER.info("Start HttpMonitor on:" + port);
				} catch (Exception ex) {
					LOGGER.error("StartMonitor on {} failed! {}", port, ex);
				}
			}

			// 全部启动完毕后启动系统的定时清理工作
			SystemCleaner.start();
		} catch (Exception ex) {
			LOGGER.error("Service Start Failed! {}", ex);
			TimeUnit.MILLISECONDS.sleep(2000);
			System.exit(1);
			return;
		}
		LOGGER.info("Service Starts Successfully!");
	}

	/**
	 * 控制台监控程序，当用户在控制台输入"stop"时，当前的agent会进行关闭
	 * 
	 * @param agent
	 */
	public static void consoleMonitor(WorkerAgent agent) {
		try {
			Scanner scanner = new Scanner(System.in);
			while (true) {
				System.out.print(">");
				String line = scanner.next();
				if (line.equalsIgnoreCase("stop")) {
					LOGGER.info("Begin to stop service");
					try {
						agent.stop();
						LOGGER.info("Service stopped");
					} catch (Exception ex) {
						LOGGER.error("Stop failed {}", ex);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Console input error.", e);
		}
	}
}