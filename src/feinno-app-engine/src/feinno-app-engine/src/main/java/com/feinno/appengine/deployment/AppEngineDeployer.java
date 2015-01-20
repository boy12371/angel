/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-8-20
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.deployment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.deployment.HATask;

/**
 * 
 * AppEngine部署器, 实现以下功能<br>
 * 需要处理的情况, 1. 变更部署 2. 热重启 3. 刷新灰度条件
 * 
 * <pre>
 * AppEngineDeployer deployer = new AppEngineDeployer();
 * deployer.getAppBeans(); // 获取当前信息用于显示
 * deployer.exportDeployments(); // 输出部署信息
 * deployer.runDeploy(); // 运行部署信息
 * deployer.syncZookeeper(); // 同步Zookeeper中的Applications结点
 * </pre>
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineDeployer {

	private AppEngineRefresher refresher;

	private static AppEngineDeployer INSTANCE = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(AppEngineDeployer.class);

	private AppEngineDeployer() {

	}

	public synchronized static AppEngineDeployer getInstance(String zkHosts) {
		if (INSTANCE == null) {
			try {
				INSTANCE = new AppEngineDeployer();
				INSTANCE.initialize(zkHosts);
			} catch (Exception e) {
				LOGGER.error("Initialize AppEngineDeployer failed. ", e);
			}
		}
		return INSTANCE;
	}

	private void initialize(String zkHosts) throws Exception {
		AppEngineRefresher.initialize(zkHosts);
		refresher = AppEngineRefresher.INSTANCE;
	}

	/**
	 * 
	 * 获取所有正在进行部署的任务
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<HATask> getDeployTasks() throws Exception {
		// 返回所有的部署任务
		refresher.refreshDatabase();
		Map<String, HATask> ret = new HashMap<String, HATask>();
		for (AppBeanDeployment bean : refresher.getAppBeans()) {
			for (HATask t : bean.getRunningTasks()) {
				String key = t.getIdentifier();
				if (ret.get(key) == null) {
					ret.put(key, t);
				}
			}
		}
		return ret.values();
	}

	/**
	 * 
	 * 获取所有AppBean的运行状态
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<AppBeanDeployment> getAppBeans() throws Exception {
		refresher.refreshDatabase();
		return refresher.getAppBeans();
	}

	/**
	 * 
	 * 部署选中AppBeans
	 */
	public List<HATask> deploySelected(List<Integer> beans) {
		List<HATask> taskList = new ArrayList<HATask>();
		for (int i : beans) {
			AppBeanDeployment bean = refresher.getAppBean(i);
			try {
				taskList.addAll(bean.evolution());
			} catch (Exception ex) {
				LOGGER.error(String.format("deploySelected error,beans = %s", beans.toString()), ex);
				HATask task = new HATask("  ", "BeanId:" + i, " deploy failed. ");
				task.complete(ex);
				taskList.add(task);
			}
		}
		return taskList;
	}

	/**
	 * 
	 * 默认部署全部, 不提供一些私有化判断, 部署全部
	 * 
	 * @return
	 */
	public void deployAll() {
		// List<AppBeanDeployment> deployments =
		// AppEngineRefresher.INSTANCE.getAppBeans();
		// for (AppBeanDeployment bean: deployments) {
		// //
		// // filter no need to update
		//
		// // run deploy
		// Future<Exception> task = bean.evolution();
		//
		// if (bean.isEvoluting()) {
		//
		// }
		// //
		// }
		//
		// // 任务列表
		// for ($app in [select *, max(BeanId) as MaxBeanId FAE_Application
		// group by PackageId, ServerGroup, WorkerInstance, GrayFactors]) {
		// // 得到部署包信息
		// $package = select * from HA_Package where PackageId = $app.PackageId;
		//
		// // 生成ServiceDeployName: 由package中ha.xml的ServiceName +
		// 所有符合条件中最大的BeanId生成
		// $serviceDeployName = $package.serviceName + "-" + $app.MaxBeanId
		//
		// // 尝试更新HA_Deployment信息,
		// // 判断是否已经存在一致版本的部署
		// $deployment = select * from HA_Deployment where ServiceDeployName =
		// $serviceDeployName
		// if $deployment != null && ... // 如果版本一致，跳过
		// continue;
		//
		// // 添加新部署
		// insert into HA_Deployment values (
		// $serviceDeployName,
		// $app.PackageId,
		// $app.ServerGroup,
		// "AUTO", // WorkerUpdateMode,
		// "AUTO", // WorkerStartMode
		// "FAE", // ServiceTag, 服务标记，当将HA作为中间层服务时由程序添加，为"FAE"
		// "", // PortSettings, 全部为动态端口
		// "-Xmx1024", // FAE全局配置
		// "{ "maxLifeHour":1024, "maxAllowMemory":6400 }" // 定制配置，全局一致
		// );
		//
		// $deployment = select * from HA_Deployment
		//
		// //
		// // 更新FAE_Application表的ServiceDeployName字段
		// update FAE_Application set
		// ServiceDeployName = $serviceDeployName
		// where
		// PackageId = @app.PackageId,
		// ServerGroup = @app.ServerGroup,
		// WorkerInstance = @app.WorkerInstance
		// GrayFactors = @app.GrayFactors
		// }
		// return null;
	}
}
