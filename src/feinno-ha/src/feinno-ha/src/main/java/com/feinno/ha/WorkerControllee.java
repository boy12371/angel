/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 15, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.interfaces.controller.HAWorkerControlleeService;
import com.feinno.ha.interfaces.controller.HAWorkerControllerRegisterArgs;
import com.feinno.ha.interfaces.controller.HAWorkerControllerService;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.threading.ExecutorFactory;
import com.feinno.util.ServiceEnviornment;

/**
 * 
 * 受控方实现
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class WorkerControllee implements HAWorkerControlleeService {
	private WorkerAgent agent;
	private RpcDuplexClient duplexClient;
	private HAWorkerControllerService service;

	/** 用于定期的心跳检查Master是否挂掉，如果挂掉，进行重连 */
	private Thread heartbeatThread;
	private static final int HEARTBEAT_TIME = 10 * 1000;

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkerControllee.class);

	public WorkerControllee(WorkerAgent agent) {
		this.agent = agent;
	}

	public RpcDuplexClient getDuplexClient() {
		return duplexClient;
	}

	public void register() throws Exception {
		RpcEndpoint ep = ServiceSettings.INSTANCE.getControllerEp();
		duplexClient = new RpcDuplexClient(ep);
		duplexClient.setExecutor(ExecutorFactory.newFixedExecutor("CLIENT", 10, 10240));
		duplexClient.registerCallbackService(this);
		service = duplexClient.getService(HAWorkerControllerService.class);
		duplexClient.connectSync();
		HAWorkerControllerRegisterArgs args = new HAWorkerControllerRegisterArgs();
		args.setPid(ServiceEnviornment.getPid());
		args.setWorkerId(ServiceSettings.INSTANCE.getServiceName());
		service.register(args);
		// 启动心跳监控，如果本次注册完成后发现断连，则自动连接
		heartbeatThread = new Thread(new HeartbeatThread(this, HEARTBEAT_TIME));
		heartbeatThread.start();
	}

	@Override
	public void start() throws Exception {
		agent.start();
	}

	@Override
	public void stop() throws Exception {
		agent.stop();
	}

	@Override
	public void ping() {
	}

	/**
	 * <b>描述: </b>此内部线程类用于监控HaMaster是否断开连接，每间隔指定时间判断是否断开连接，则自动重连
	 * <p>
	 * <b>功能: </b>
	 * <p>
	 * <b>用法: </b>
	 * <p>
	 * 
	 * @author Lv.Mingwei
	 * 
	 */
	private static class HeartbeatThread implements Runnable {

		/** 每次循环判断的间隔时间 */
		private int heartbeatTime = 10 * 1000;

		private WorkerControllee workerControllee;

		public HeartbeatThread(WorkerControllee workerControllee, int heartbeatTime) {
			this.workerControllee = workerControllee;
			this.heartbeatTime = heartbeatTime;
		}

		@Override
		public void run() {
			while (true) {
				try {
					TimeUnit.MILLISECONDS.sleep(heartbeatTime);
					if (!workerControllee.duplexClient.isConnected()) {
						LOGGER.warn("Found HAMaster is disconnected, try to reconnect.{}", ServiceSettings.INSTANCE
								.getControllerEp().toString());
						workerControllee.register();
					}
				} catch (Exception e) {
					LOGGER.error("WorkerControllee Heartbeat Thread Found Error.", e);
				}
			}
		}

	}
}
