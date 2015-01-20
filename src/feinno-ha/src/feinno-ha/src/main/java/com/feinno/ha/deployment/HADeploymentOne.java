/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Aug 24, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.deployment;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.interfaces.center.HAWorkerEndpoint;
import com.feinno.ha.interfaces.master.HAMasterAgentCallbackService;
import com.feinno.ha.interfaces.master.HAMasterOperationArgs;
import com.feinno.ha.interfaces.master.HAMasterWorkerStatus;
import com.feinno.logging.common.LogCommon;
import com.feinno.rpc.channel.RpcClientTransaction;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.rpc.client.RpcMethodStub;
import com.feinno.rpc.client.RpcProxyFactory;
import com.feinno.util.EventHandler;

/**
 * 对应在一台服务器上的部署状态与动作<br>
 * 一台服务器只能部署一个唯一名字(ServiceDeployName)的服务, 并且只允许存在一个处于DelayStop状态的shadow进程
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HADeploymentOne {
	private String serviceName; // 服务部署名称: serviceDeployName
	private HADeployment deployment; // 部署信息
	private HAServer server; // 服务器信息

	private HAMasterWorkerStatus status; // 由Master负责维护的服务信息
	private HAMasterWorkerStatus shadowStatus; // 由Master负责维护的服务信息,处于延迟停止中

	@SuppressWarnings("unused")
	private HAWorkerEndpoint endpoint; // 由Worker负责维护的服务信息

	@SuppressWarnings("unused")
	private HAWorkerEndpoint shadowEndpoint; // 由Worker负责维护的服务信息,处于延迟停止中

	private AtomicReference<HATask> runningTask;

	private static final Logger LOGGER = LoggerFactory.getLogger(HADeploymentOne.class);

	public HADeploymentOne(HADeployment deployment, HAServer server) {
		runningTask = new AtomicReference<HATask>();
		this.serviceName = deployment.getServiceName();
		this.deployment = deployment;
		this.server = server;
		initWorkerStatus();
		initWorkerEndpoint();
	}

	public HATask getRunningTask() {
		return runningTask.get();
	}

	/**
	 * 从数据库中初始化WorkerStatus的状态
	 */
	private void initWorkerStatus() {
		HAMasterWorkerStatus[] statusArray = HADatabaseHelper
				.readMasterWorkerStaus(server.getServerName(), serviceName);
		// 区分 main/shadow
		processMasterWorkerStatus(statusArray);
	}

	/**
	 * 从数据库中初始化initWorkerEndpoint的信息
	 */
	private void initWorkerEndpoint() {
		HAWorkerEndpoint[] workerEndpoints = HADatabaseHelper.readWorkerEndpoint(server.getServerName(), serviceName);
		// 区分 main/shadow
		processWorkerEndpoint(workerEndpoints);
	}

	/** 获得本服务的机器信息 */
	public HAServer getServer() {
		return server;
	}

	public HAMasterWorkerStatus getWorkerStatus() {
		return status;
	}

	/**
	 * 
	 * 是否为最新版本
	 */
	public boolean isUpdated() {
		return deployment.getPackageId() == status.getPackageId();
	}

	/**
	 * 
	 * 是否正在运行
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return HAStatusEnum.MasterWorker.valueOf(status.getWorkerStatus().toUpperCase()) == HAStatusEnum.MasterWorker.RUNNING;
	}

	/**
	 * 
	 * 热重启, 首先执行start(), start()成功后执行delayStop()命令
	 */
	public HATask hotSwapByEngine() {
		initWorkerStatus();
		HATask task = runningTask.get();
		if (task != null && task.getAction().equalsIgnoreCase("Hotswap")) {
			return task;
		}
		// Step 1.获取当前状态
		String statusStr = status != null ? status.getWorkerStatus().toUpperCase() : "STANDBY";
		HAStatusEnum.MasterWorker workerStatus = HAStatusEnum.MasterWorker.valueOf(statusStr);
		// Step 2.判断当前状态是否允许启动，如果不允许，则抛出异常
		switch (workerStatus) {
		case RUNNING:
			HAMasterOperationArgs args = new HAMasterOperationArgs();
			args.setServiceName(serviceName);
			return callMaster("Hotswap", args);
		default:
			throw new RuntimeException(String.format("%s %s is currently in a %s  state, not able to hotSwap.",
					server.getServerName(), serviceName, workerStatus.toString()));
		}
	}

	/**
	 * 
	 * 1. 如果从目前的状态判断上已经启动了, 则返回空 2. 如果已经运行过启动了, 2. 否则启动一个HATask task = start()
	 * 3.
	 * 
	 * @return
	 */
	public HATask startByEngine() {
		initWorkerStatus();
		HATask task = runningTask.get();
		if (task != null && task.getAction().equalsIgnoreCase("StartWorker")) {
			return task;
		}
		// Step 1.获取当前状态
		String statusStr = status != null ? status.getWorkerStatus().toUpperCase() : "STANDBY";
		HAStatusEnum.MasterWorker workerStatus = HAStatusEnum.MasterWorker.valueOf(statusStr);
		// Step 2.判断当前状态是否允许启动，如果不允许，则抛出异常
		switch (workerStatus) {
		case RUNNING:
			return null;
		case STANDBY:
			HAMasterOperationArgs args = new HAMasterOperationArgs();
			args.setServiceName(serviceName);
			return callMaster("StartWorker", args);
		default:
			throw new RuntimeException(String.format("%s %s is currently in a %s  state, not able to start.",
					server.getServerName(), serviceName, workerStatus.toString()));
		}
	}

	/**
	 * 
	 * 启动, 启动包含升级动作
	 * 
	 * @return
	 */
	public HATask start() {
		initWorkerStatus();
		// Step 1.获取当前状态
		String statusStr = status != null ? status.getWorkerStatus().toUpperCase() : "STANDBY";
		HAStatusEnum.MasterWorker workerStatus = HAStatusEnum.MasterWorker.valueOf(statusStr);
		// Step 2.判断当前状态是否允许启动，如果不允许，则抛出异常
		switch (workerStatus) {
		case STANDBY:
			HAMasterOperationArgs args = new HAMasterOperationArgs();
			args.setServiceName(serviceName);
			return callMaster("StartWorker", args);
		default:
			throw new RuntimeException(String.format("%s %s is currently in a %s  state, not able to start.",
					server.getServerName(), serviceName, workerStatus.toString()));
		}

	}

	/**
	 * 
	 * 停止, 停止并杀进程
	 * 
	 * @return
	 */
	public HATask stop() {
		initWorkerStatus();
		// Step 1.获取当前状态
		String statusStr = status != null ? status.getWorkerStatus().toUpperCase() : "STANDBY";
		HAStatusEnum.MasterWorker workerStatus = HAStatusEnum.MasterWorker.valueOf(statusStr);
		// Step 2.判断当前状态是否允许启动，如果不允许，则抛出异常
		switch (workerStatus) {
		case RUNNING:
			HAMasterOperationArgs args = new HAMasterOperationArgs();
			args.setServiceName(serviceName);
			return callMaster("StopWorker", args);
		default:
			throw new RuntimeException(String.format("%s %s is currently in a %s  state, not able to stop.",
					server.getServerName(), serviceName, workerStatus.toString()));
		}
	}

	public HATask update() {
		initWorkerStatus();
		// Step 1.获取当前状态
		String statusStr = status != null ? status.getWorkerStatus().toUpperCase() : "STANDBY";
		HAStatusEnum.MasterWorker workerStatus = HAStatusEnum.MasterWorker.valueOf(statusStr);
		// Step 2.判断当前状态是否允许启动，如果不允许，则抛出异常
		switch (workerStatus) {
		case STANDBY:
			HAMasterOperationArgs args = new HAMasterOperationArgs();
			args.setServiceName(serviceName);
			return callMaster("UpdateWorker", args);
		default:
			throw new RuntimeException(String.format("%s %s is currently in a %s  state, not able to update.",
					server.getServerName(), serviceName, workerStatus.toString()));
		}
	}

	/**
	 * 
	 * 延迟停止, 5分钟后执行stop()命令
	 * 
	 * @return
	 */
	public HATask delayStop() {
		initWorkerStatus();
		// Step 1.获取当前状态
		String statusStr = status != null ? status.getWorkerStatus().toUpperCase() : "STANDBY";
		HAStatusEnum.MasterWorker workerStatus = HAStatusEnum.MasterWorker.valueOf(statusStr);
		// Step 2.判断当前状态是否允许启动，如果不允许，则抛出异常
		switch (workerStatus) {
		case RUNNING:
			HAMasterOperationArgs args = new HAMasterOperationArgs();
			args.setServiceName(serviceName);
			return callMaster("DelayStop", args);
		default:
			throw new RuntimeException(String.format("%s %s is currently in a %s  state, not able to delayStop.",
					server.getServerName(), serviceName, workerStatus.toString()));
		}
	}

	/**
	 * 
	 * 热重启, 首先执行start(), start()成功后执行delayStop()命令
	 */
	public HATask hotSwap() {
		initWorkerStatus();
		// Step 1.获取当前状态
		String statusStr = status != null ? status.getWorkerStatus().toUpperCase() : "STANDBY";
		HAStatusEnum.MasterWorker workerStatus = HAStatusEnum.MasterWorker.valueOf(statusStr);
		// Step 2.判断当前状态是否允许启动，如果不允许，则抛出异常
		switch (workerStatus) {
		case RUNNING:
			HAMasterOperationArgs args = new HAMasterOperationArgs();
			args.setServiceName(serviceName);
			return callMaster("Hotswap", args);
		default:
			throw new RuntimeException(String.format("%s %s is currently in a %s  state, not able to hotSwap.",
					server.getServerName(), serviceName, workerStatus.toString()));
		}
	}

	/**
	 * 
	 * 强制杀掉进程
	 * 
	 * @return
	 */
	public HATask kill() {
		// 任何状态都允许kill
		HAMasterOperationArgs args = new HAMasterOperationArgs();
		args.setPid(status.getPid());
		args.setServiceName(serviceName);
		return callMaster("KillWorker", args);
	}

	/**
	 * 
	 * 强制杀掉Shadow进程
	 * 
	 * @return
	 */
	public HATask killShadow() {
		// 任何状态都允许kill
		HAMasterOperationArgs args = new HAMasterOperationArgs();
		args.setPid(shadowStatus.getPid());
		args.setServiceName(serviceName);
		return callMaster("KillWorker", args);
	}

	/**
	 * 
	 * 强制杀掉进程
	 * 
	 * @return
	 */
	public HATask getStatus() {
		// Step 1.任何状态均可GetStatus
		return callMaster("GetStatus", new HAMasterOperationArgs());
	}

	/**
	 * 调用统一方法Master的方法
	 * 
	 * @param funcName
	 * @return
	 */
	private HATask callMaster(final String funcName, final HAMasterOperationArgs args) {
		final HATask task = new HATask(server.getServerName(), args.getServiceName(), funcName);
		task.setRpcTask(true);
		if (!runningTask.compareAndSet(null, task)) {
			task.complete(null);// 释放新创建的无用的task
			throw new IllegalStateException(funcName + " operation can't exec,server busy ");
		}
		try {
			String service = HAMasterAgentCallbackService.NAME;
			RpcEndpoint endpoint = server.getMasterCenterEp();
			RpcMethodStub stub = RpcProxyFactory.getMethodStub(endpoint, service, funcName);
			RpcClientTransaction tx = stub.createTransaction();
			tx.setTimeout(5 * 60 * 1000);
			tx.setArgs(args);
			tx.putExtension(HAMasterAgentCallbackService.EXT_SERVER_NAME, server.getServerName());
			final Date beginTime = new Date();
			RpcFuture f = tx.begin();
			f.addListener(new EventHandler<RpcResults>() {
				@Override
				public void run(Object sender, RpcResults e) {
					// Step 1. 记录日志
					long duration = new Date().getTime() - beginTime.getTime();
					String actionError = e.getError() != null ? LogCommon.formaError(e.getError()) : "";
					HADatabaseHelper.writeOperationLog(beginTime, duration, server.getServerName(), serviceName,
							funcName, e.getReturnCode().toString(), actionError);
					// Step 2.获取最新服务状态
					initWorkerStatus();
					// Step 3.通知future结束任务
					task.complete(e.getError());
					// Step 4. 同步控制复原
					runningTask.set(null);
				}
			});
		} catch (Exception e) {
			LOGGER.error("Rpc Invoke Center Error. ", e);
			task.complete(e);
		}
		return task;
	}

	/**
	 * 该方法用于处理HAMasterWorkerStatus的main/shadow，当取到多条记录时，判断谁是main谁是shadow很重要
	 * 
	 * @param statusArray
	 */
	private void processMasterWorkerStatus(HAMasterWorkerStatus[] statusArray) {

		// 如果没有，则直接返回
		if (statusArray == null || statusArray.length == 0) {
			status = null;
			shadowStatus = null;
			return;
		}
		// 如果仅有一个，则仅有的这一个作为主服务
		if (statusArray.length == 1) {
			status = statusArray[0];
			shadowStatus = null;
			return;
		}
		// 如果有多个，那么需要详细的判断哪个为主，哪个为辅
		// 判断方式是用过排序法，以WorkerStatus的枚举值最低的在前面
		for (int i = 0; i < statusArray.length; i++) {
			for (int j = i + 1; j < statusArray.length; j++) {
				HAStatusEnum.MasterWorker statusI = HAStatusEnum.MasterWorker.valueOf(statusArray[i].getWorkerStatus()
						.toUpperCase());
				HAStatusEnum.MasterWorker statusJ = HAStatusEnum.MasterWorker.valueOf(statusArray[j].getWorkerStatus()
						.toUpperCase());
				if (statusJ.intValue() < statusI.intValue()) {
					HAMasterWorkerStatus temp = statusArray[i];
					statusArray[i] = statusArray[j];
					statusArray[j] = temp;
				}
			}
		}
		status = statusArray[0];
		shadowStatus = statusArray[1];
	}

	/**
	 * 该方法用于处理HAWorkerEndpoint的main/shadow，当取到多条记录时，判断谁是main谁是shadow很重要
	 * 
	 * @param workerEndpoints
	 */
	private void processWorkerEndpoint(HAWorkerEndpoint[] workerEndpoints) {

		// 如果没有，则直接返回
		if (workerEndpoints == null || workerEndpoints.length == 0) {
			endpoint = null;
			shadowEndpoint = null;
			return;
		}
		// 如果仅有一个，则仅有的这一个作为主服务
		if (workerEndpoints.length == 1) {
			endpoint = workerEndpoints[0];
			shadowEndpoint = null;
			return;
		}
		// 如果有多个，那么需要详细的判断哪个为主，哪个为辅
		// 判断方式是用过排序法，以HAWorkerEndpoint的枚举值最低的在前面
		for (int i = 0; i < workerEndpoints.length; i++) {
			for (int j = i + 1; j < workerEndpoints.length; j++) {
				HAStatusEnum.WorkerEndpoint statusI = HAStatusEnum.WorkerEndpoint.valueOf(workerEndpoints[i]
						.getStatus().toUpperCase());
				HAStatusEnum.WorkerEndpoint statusJ = HAStatusEnum.WorkerEndpoint.valueOf(workerEndpoints[j]
						.getStatus().toUpperCase());
				if (statusJ.intValue() < statusI.intValue()) {
					HAWorkerEndpoint temp = workerEndpoints[i];
					workerEndpoints[i] = workerEndpoints[j];
					workerEndpoints[j] = temp;
				}
			}
		}
		endpoint = workerEndpoints[0];
		shadowEndpoint = workerEndpoints[1];
	}
}
