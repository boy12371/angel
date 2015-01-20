package com.feinno.ha.service;

import java.io.File;
import java.io.FileInputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.center.MasterAgentService;
import com.feinno.ha.center.MasterProxyService;
import com.feinno.ha.service.activity.node.ActivityFinalNodeQueue;
import com.feinno.logging.spi.LogManager;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.duplex.RpcDuplexServer;
import com.feinno.threading.ExecutorFactory;
import com.feinno.util.PropertiesUtil;

public class TestRunner {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// try {
		// DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		// DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
		// } catch (SQLException e1) {
		// e1.printStackTrace();
		// }
		// Properties propts = PropertiesUtil.xmlToProperties(new
		// FileInputStream(System.getProperty("user.dir")
		// + File.separator + "logging.xml"));
		// LogManager.loadSettings(propts);
		// int port = 11211;
		LOGGER.info("Rpc port is ");

		// RpcTcpServerChannel channel = new RpcTcpServerChannel(port);
		// RpcDuplexServer server = new RpcDuplexServer(channel);
		//
		// server.registerService(MasterAgentService.INSTANCE);
		// server.registerService(new
		// MasterProxyService("RpcSampleAgentCallbackService"));
		//
		// server.setExecutor(ExecutorFactory.newFixed Executor("SERVER", 10,
		// 10240));
		// channel.start();
		//
		// ActivityFinalNodeQueue.initEndpointQueue();
		LOGGER.info("Finished initializing the queue of endpoint.");
		LOGGER.info("Hacenter was started.");
		LOGGER.info("Hacenter was started.");
		LOGGER.info("Hacenter was started.");
		LOGGER.info("Hacenter was started.");
		LOGGER.info("Hacenter was started.");
		LOGGER.info("Hacenter was started.");
		LOGGER.info("Hacenter was started.");
		LOGGER.info("Hacenter was started.");
		for (int i = 0; i < 10; i++) {
			LOGGER.debug("test " + i);
			Thread.sleep(1000);
		}
		// LOGGER.error("aaa");
		// LOGGER.error("aaaaa");
		// LOGGER.error(" ",new RuntimeException("test"));
		AA aa = new AA();
		aa.test();
	}

	private static class AA {
		public void test() {
			log.info("this is AAA");
		}

		private Logger log = LoggerFactory.getLogger(AA.class);
	}

	private static Logger LOGGER = LoggerFactory.getLogger(TestRunner.class);
}
