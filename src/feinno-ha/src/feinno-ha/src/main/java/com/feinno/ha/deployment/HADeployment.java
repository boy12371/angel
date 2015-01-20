/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Aug 24, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.deployment;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.interfaces.master.HAMasterDeployment;

/**
 * 保存一个服务的部署配置及运行状态, 可在此类上进行各种操作
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HADeployment {
	private int packageId;
	private String serviceName; // HA_Deployment.ServiceDeployName
	private String serverGroupName;
	private HAMasterDeployment info; // HA_Deployment
	private HAServerGroup serverGroup; // HA_Deployment

	private List<HADeploymentOne> deployOnes; // 对应机器组中的每个服务器的部署配置

	private static final Logger LOGGER = LoggerFactory.getLogger(HADeployment.class);

	/**
	 * 
	 * 构造函数
	 * 
	 * @param info
	 */
	public HADeployment(String serviceName, String serverGroupName) {

		this.serviceName = serviceName;
		this.serverGroupName = serverGroupName;
		// Step 1.加载当前service的部署信息
		info = HADatabaseHelper.readDeployment(serviceName);
		// 如果部署信息为空,可能该条数据库记录已被移除, 这时只能执行停止操作, 此时serverGroup需要靠构造函数传进来
		if (info != null) {
			packageId = info.getPackageId();
			this.serverGroupName = info.getServerGroup();
		}

		// Step 2.获取服务器组信息,得到每台服务器的HAMasterEndpoint,
		// 如果有Master不可用,则无法执行xxxAll的操作
		serverGroup = HADatabaseHelper.readServerGroup(serverGroupName);

		// Step 3. 创建每一台Server部署对象
		deployOnes = new ArrayList<HADeploymentOne>();
		if (serverGroup != null && serverGroup.getServers() != null) {
			for (HAServer server : serverGroup.getServers()) {
				deployOnes.add(new HADeploymentOne(this, server));
			}
		}
	}

	public void refresh() {
		// 加载最新的服务器组
		HAServerGroup newServerGroup = HADatabaseHelper.readServerGroup(serverGroupName);
		if (newServerGroup != null && newServerGroup.getServers() != null && newServerGroup.getServerCount() > 0) {
			// 1. 寻找不存在的服务器，将其移除
			List<HADeploymentOne> removeDeploymentOnes = new ArrayList<HADeploymentOne>();
			for (HADeploymentOne deploymentOne : deployOnes) {
				boolean isExists = false;
				for (HAServer server : newServerGroup.getServers()) {
					if (server.equals(deploymentOne.getServer())) {
						isExists = true;
						break;
					}
				}
				if (!isExists) {
					// 如果老的在新的中不存在，则需要移除
					removeDeploymentOnes.add(deploymentOne);
				}
			}
			// 如果老的在新的中不存在，则需要移除
			for (HADeploymentOne removeDeploymentOne : removeDeploymentOnes) {
				deployOnes.remove(removeDeploymentOne);
			}

			// 2. 寻找新加入的服务器
			for (HAServer server : newServerGroup.getServers()) {
				boolean isExists = false;
				for (HADeploymentOne deploymentOne : deployOnes) {
					if (server.equals(deploymentOne.getServer())) {
						isExists = true;
						break;
					}
				}
				if (!isExists) {
					// 如果新的在老的中不存在，则是新增的服务器，需要将这些服务器加入到列表中
					deployOnes.add(new HADeploymentOne(this, server));
				}
			}
		} else {
			// 如果服务器组中没有内容了，则清理掉之前的服务器信息
			List<HADeploymentOne> oldDeploymentOnes = deployOnes;
			deployOnes = new ArrayList<HADeploymentOne>();
			oldDeploymentOnes.clear();
			oldDeploymentOnes = null;
		}
	}

	public int getPackageId() {
		return packageId;
	}

	public HAServerGroup getServerGroup() {
		return serverGroup;
	}

	/**
	 * 
	 * 服务名称(ServiceDeployName)
	 * 
	 * @return
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * 返回该service的全部部署信息
	 * 
	 * @return
	 */
	public List<HADeploymentOne> getDeploymentOnes() {
		return deployOnes;
	}

	/**
	 * 
	 * 是否所有当前运行的信息都是最新版本
	 * 
	 * @return
	 */
	public boolean isAllUpdated() {
		for (HADeploymentOne deploymentOne : deployOnes) {
			if (!deploymentOne.isUpdated()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * 启动所有服务至最新版本 不论执行多少次, 永远返回一组HATask 这组HATask标记了服务在每台机器上的部署情况
	 * 
	 * HATask有两种情况, 1. 正在执行 2. 已经不需要执行了
	 * 
	 * 如果不需要执行了, 是否需要在这个List里头返回
	 * 
	 * @return
	 */
	public List<HATask> startAllByEngine() {
		refresh();
		List<HATask> futures = new ArrayList<HATask>();
		for (HADeploymentOne deploymentOne : deployOnes) {
			try {
				HATask task = deploymentOne.startByEngine();
				if (task != null) {
					futures.add(task);
				}
			} catch (Exception e) {
				// 此时出现问题,也会一并将问题交由Future处理
				LOGGER.error("startAllByEngine ", e);
				HAServer server = deploymentOne.getServer();
				HATask task = new HATask(server.getServerName(), serviceName, "START");
				task.complete(e);
				futures.add(task);
			}
		}
		return futures;
	}

	/**
	 * 
	 * 启动所有服务至最新版本 不论执行多少次, 永远返回一组HATask 这组HATask标记了服务在每台机器上的部署情况
	 * 
	 * HATask有两种情况, 1. 正在执行 2. 已经不需要执行了
	 * 
	 * 如果不需要执行了, 是否需要在这个List里头返回
	 * 
	 * @return
	 */
	public List<HATask> hotSwapAllByEngine() {
		refresh();
		List<HATask> futures = new ArrayList<HATask>();
		for (HADeploymentOne deploymentOne : deployOnes) {
			try {
				HATask task = deploymentOne.hotSwapByEngine();
				if (task != null) {
					futures.add(task);
				}
			} catch (Exception e) {
				// 此时出现问题,也会一并将问题交由Future处理
				LOGGER.error("hotSwapAllByEngine ", e);
				HAServer server = deploymentOne.getServer();
				HATask task = new HATask(server.getServerName(), serviceName, "HOTSWAP");
				task.complete(e);
				futures.add(task);
			}
		}
		return futures;
	}

	/**
	 * 
	 * 启动所有服务至最新版本
	 * 
	 * @return
	 */
	public List<HATask> startAll() {
		refresh();
		List<HATask> futures = new ArrayList<HATask>();
		for (HADeploymentOne deploymentOne : deployOnes) {
			try {
				futures.add(deploymentOne.start());
			} catch (Exception e) {
				// 此时出现问题,也会一并将问题交由Future处理
				LOGGER.error("startAll ", e);
				HAServer server = deploymentOne.getServer();
				HATask task = new HATask(server.getServerName(), serviceName, "START");
				task.complete(e);
				futures.add(task);
			}
		}
		return futures;
	}

	/**
	 * 
	 * 停止所有worker
	 * 
	 * @return
	 */
	public List<HATask> stopAll() {
		refresh();
		List<HATask> futures = new ArrayList<HATask>();
		for (HADeploymentOne deploymentOne : deployOnes) {
			try {
				futures.add(deploymentOne.stop());
			} catch (Exception e) {
				// 此时出现问题,也会一并将问题交由Future处理
				LOGGER.error("stopAll ", e);
				HAServer server = deploymentOne.getServer();
				HATask task = new HATask(server.getServerName(), serviceName, "STOP");
				task.complete(e);
				futures.add(task);
			}
		}
		return futures;
	}

	/**
	 * 
	 * 更新所有worker
	 * 
	 * @return
	 */
	public List<HATask> updateAll() {
		refresh();
		List<HATask> futures = new ArrayList<HATask>();
		for (HADeploymentOne deploymentOne : deployOnes) {
			try {
				futures.add(deploymentOne.update());
			} catch (Exception e) {
				// 此时出现问题,也会一并将问题交由Future处理
				LOGGER.error("updateAll ", e);
				HAServer server = deploymentOne.getServer();
				HATask task = new HATask(server.getServerName(), serviceName, "UPDATE");
				task.complete(e);
				futures.add(task);
			}
		}
		return futures;
	}

	/**
	 * 
	 * kill所有worker
	 * 
	 * @return
	 */
	public List<HATask> killAll() {
		refresh();
		List<HATask> futures = new ArrayList<HATask>();
		for (HADeploymentOne deploymentOne : deployOnes) {
			try {
				futures.add(deploymentOne.kill());
			} catch (Exception e) {
				// 此时出现问题,也会一并将问题交由Future处理
				LOGGER.error("killAll ", e);
				HAServer server = deploymentOne.getServer();
				HATask task = new HATask(server.getServerName(), serviceName, "KILL");
				task.complete(e);
				futures.add(task);
			}
		}
		return futures;
	}

	/**
	 * 
	 * 延迟停止所有进程
	 * 
	 * @return
	 */
	public List<HATask> delayStopAll() {
		refresh();
		List<HATask> futures = new ArrayList<HATask>();
		for (HADeploymentOne deploymentOne : deployOnes) {
			try {
				futures.add(deploymentOne.delayStop());
			} catch (Exception e) {
				// 此时出现问题,也会一并将问题交由Future处理
				LOGGER.error("delayStopAll ", e);
				HAServer server = deploymentOne.getServer();
				HATask task = new HATask(server.getServerName(), serviceName, "DELAYSTOP");
				task.complete(e);
				futures.add(task);
			}
		}
		return futures;
	}

	/**
	 * 
	 * 返回所有正在运行的任务
	 * 
	 * @return
	 */
	public List<HATask> getRunningTasks() {
		List<HATask> taskList = new ArrayList<HATask>();
		for (HADeploymentOne deploymentOne : deployOnes) {
			if (deploymentOne != null && deploymentOne.getRunningTask() != null) {
				taskList.add(deploymentOne.getRunningTask());
			}
		}
		return taskList;
	}
}
