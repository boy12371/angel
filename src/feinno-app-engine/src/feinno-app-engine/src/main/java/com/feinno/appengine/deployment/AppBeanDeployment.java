/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-8-20
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.deployment;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.configuration.AppBeanDeployStatus;
import com.feinno.appengine.configuration.FAEAppAnnotationDecoder;
import com.feinno.appengine.configuration.FAE_Application;
import com.feinno.appengine.runtime.configuration.ApplicationEntity;
import com.feinno.appengine.runtime.configuration.RunningWorkerEntity;
import com.feinno.ha.database.HADBDeploymentHelper;
import com.feinno.ha.database.HADBStatusHelper;
import com.feinno.ha.deployment.HADatabaseHelper;
import com.feinno.ha.deployment.HADeployment;
import com.feinno.ha.deployment.HAServer;
import com.feinno.ha.deployment.HAServerGroup;
import com.feinno.ha.deployment.HATask;
import com.feinno.ha.interfaces.center.HAPackageInfo;
import com.feinno.logging.common.FireEventQueue;
import com.feinno.rpc.channel.RpcClientTransaction;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.client.RpcMethodStub;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.serialization.protobuf.util.TwoTuple;
import com.feinno.threading.ExecutorFactory;
import com.feinno.util.Action;
import com.feinno.util.Assert;
import com.feinno.util.EventHandler;
import com.feinno.util.StringUtils;
import com.mchange.util.AssertException;

/**
 * 
 * 针对一个AppBean的部署类,能够获取当前运行状态,并执行自适应的部署操作evolution(),将部署同步到最新状态
 * 
 * <pre>
 * 		appDb = FAE_Application
 * 		appZk = from Zookeeper where appId = FAE_Application.BeanId
 * 		appZk = from Zookeeper where FAE_Application running worker id
 * </pre>
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppBeanDeployment {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppBeanDeployment.class);

	private int beanId;
	private int packageId;
	private String category;
	private String name;
	private String warnColor;
	private AppBeanAnnotations annotations;

	//
	// 数据库中配置的现状, 所有部署操作基于FAE_Application表已经被正确编辑后的情况
	private FAE_Application appDb;

	// /**
	// *
	// * 从Zookeeper目标创建，暂不需要
	// * @param app
	// */
	// public AppBeanDeployment(ApplicationEntity app)
	// {
	// beanId = app.getBeanId();
	// category = app.getCategory();
	// name = app.getName();
	// zookeeperAppId = app.getBeanId();
	// }

	/**
	 * 
	 * 从数据库记录创建
	 * 
	 * @param dbItem
	 */
	public AppBeanDeployment(FAE_Application app) throws Exception {
		appDb = app;
		beanId = appDb.getBeanId();
		category = appDb.getAppCategory();
		name = appDb.getAppName();
		this.annotations = appDb.getAnnotations();
	}

	/**
	 * 
	 * 创建一个未部署的AppBean
	 * 
	 * @param beanId
	 * @param category
	 * @param name
	 */
	public AppBeanDeployment(int packageId, int beanId, String category, String name, String annotations) {
		this.beanId = beanId;
		this.packageId = packageId;
		this.category = category;
		this.name = name;
		byte[] buf = annotations.getBytes(Charset.forName("UTF-8"));
		try {
			this.annotations = FAEAppAnnotationDecoder.decode(new String(buf));
		} catch (RuntimeException e) {
			LOGGER.error("FAEAppAnnotationDecoder failed, appName {}, id {} \n annotationsText : \n {}", new Object[] {
					name, beanId, annotations });
		}
	}

	/**
	 * 
	 * 获取BeanId, 在任何时候都有数据
	 * 
	 * @return
	 */
	public int getBeanId() {
		return beanId;
	}

	/**
	 * 
	 * 获取Category, 在任何时候都有数据
	 * 
	 * @return
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * 
	 * 获取Category, 在任何时候都有数据
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * 获取Category-name, 在任何时候都有数据
	 * 
	 * @return
	 */
	public String getCategoryMinusName() {
		return category + "-" + name;
	}

	/**
	 * 
	 * 重新加载数据库数据
	 * 
	 * @param app
	 */
	public void reloadDb(FAE_Application app) {
		appDb = app;
	}

	public HADeployment getDeployment() {
		return deployment;
	}

	public HADeployment getDeploymentShadow() {
		return deploymentShadow;
	}

	/**
	 * 
	 * 转换成为Json数据格式
	 * 
	 * @return
	 */
	public AppBeanDeploymentJson toJsonObject() {
		AppBeanDeploymentJson json = new AppBeanDeploymentJson();
		json.setBeanId(beanId);
		json.setCategoryMinusName(getCategoryMinusName());
		if (annotations != null && annotations.getClassInfo() != null
				&& annotations.getClassInfo().getBaseClass() != null) {
			json.setBeanType(annotations.getClassInfo().getBaseClass().getType());
			json.setVersion(annotations.getClassInfo().getVersion());
		}
		if (appDb != null) {
			json.setEnabled(appDb.isEnabled());
			json.setWarnColor(warnColor);
			json.setGrayFactors(appDb.getGrayFactorys());
			json.setHotServerName(appDb.getHotServerName());
			json.setLocalSites(appDb.getLocalSites());
			json.setRunningInfos(this.getRunningInfos());
			json.setServerGroups(appDb.getServerGroup());
			json.setStatus(this.getStatus().toString());
			json.setStatusText(this.getStatusText());
			json.setPackageId(appDb.getPackageId());
			json.setServiceDeployName(appDb.getServiceDeployName());
			HAPackageInfo packageInfo = getPackageInfo(appDb.getPackageId());
			if (packageInfo != null) {
				json.setPackageName(packageInfo.getPackageUrl().split("name=")[1]);
			} else {
				json.setPackageName("无法识别");
			}
		} else {
			json.setStatus(AppBeanDeployStatus.UNDEPLOYED.toString());
			HAPackageInfo packageInfo = getPackageInfo(packageId);
			if (packageInfo != null) {
				String packageFile = packageInfo.getPackageUrl().split("name=")[1];
				json.setPackageName("未部署" + packageFile.substring(packageFile.indexOf("-")));
			} else {
				json.setPackageName("未部署-无法识别");
			}
		}

		return json;
	}

	public HAPackageInfo getPackageInfo(int packageId) {
		try {
			HAPackageInfo packageInfo = HADBDeploymentHelper.getSimpleWorkerPackageInfo(packageId);
			return packageInfo;
		} catch (SQLException e) {
			LOGGER.error("", e);
			return null;
		}
	}

	// /**
	// *
	// * 获取Zookeeper中符合当前配置的AppBean(CategoryMinusName, GrayFactor, BeanId)
	// * @return
	// */
	// public ApplicationEntity getApplication()
	// {
	// return AppEngineRefresher.INSTANCE.getApplication(zookeeperAppId);
	// }
	//
	// /**
	// *
	// * 得到已经进入回收状态但仍旧运行的Worker
	// * @return
	// */
	// public List<RunningWorkerEntity> getRunningWorkers(String serviceName)
	// {
	// if (deploymentShadow != null) {
	// return
	// AppEngineRefresher.INSTANCE.getRunningWorkers(deploymentShadow.getServiceName());
	// } else {
	// return null;
	// }
	// }

	/**
	 * 返回HTML格式如下
	 * 
	 * <pre>
	 * core-register-17
	 * -----------------------
	 * # APP-01
	 * monitor=<a herf=http://192.168.110.203:6070>http://192.168.110.203:6070</a>
	 * rpc=tcp://192.168.110.203:8090
	 * # APP-02
	 * monitor=http://192.168.110.203:6070
	 * rpc=tcp://192.168.110.203:8090
	 * </pre>
	 */
	private String getRunningInfos() {
		if (appDb == null) {
			return "";
		}

		StringBuilder str = new StringBuilder();
		str.append("<p>&nbsp;<strong>[" + category + "-" + name + "]</strong><hr>");

		int appId = appDb.getBeanId();
		int appId2 = appDb.getZookeeperAppId();

		String serviceName;
		int beanId = -1;
		String serviceName2 = null;
		int beanId2 = -1;

		if (appId2 > 0 && appId2 != appId) {
			ApplicationEntity app = AppEngineRefresher.INSTANCE.getApplication(appId2);
			serviceName = app == null ? null : app.getAppWorkerId();
			beanId = app == null ? -1 : app.getBeanId();
			serviceName2 = appDb.getServiceDeployName();
			beanId2 = appDb.getBeanId();
			if (StringUtils.equal(serviceName, serviceName2)) {
				serviceName2 = null;
			}
		} else {
			ApplicationEntity app = AppEngineRefresher.INSTANCE.getApplication(appId);
			serviceName = app == null ? null : app.getAppWorkerId();
			serviceName = serviceName == null ? appDb.getServiceDeployName() : serviceName;
			beanId = appDb.getBeanId();
		}

		if (serviceName != null) {
			str.append("&nbsp;ServiceName=" + serviceName);
			formatWorkersInfo(str, serviceName, beanId);
			str.append("<hr>");
		}

		if (serviceName2 != null) {
			str.append("&nbsp;ServiceName=" + serviceName2);
			formatWorkersInfo(str, serviceName2, beanId2);
			str.append("<hr>");
		}
		str.append("</p>");
		return str.toString();
	}

	private void formatWorkersInfo(StringBuilder str, String serviceName, int beanId) {
		List<RunningWorkerEntity> workers = getRunningWorkers(serviceName, beanId);
		for (RunningWorkerEntity worker : workers) {
			str.append("<hr>");
			str.append("&nbsp;#&nbsp;");
			str.append(worker.getServerName());
			str.append("<br>");
			String[] serverUrls = worker.getServiceUrls().split(";");
			if (serverUrls != null && serverUrls.length > 0) {
				for (String url : serverUrls) {
					if (url.startsWith("monitor=")) {
						String urlTemp = url.split("=")[1];
						str.append("&nbsp;monitor=<a target='_blank' href='");
						str.append(urlTemp);
						str.append("'>").append(urlTemp).append("</a><br>");
					} else {
						str.append("&nbsp;");
						str.append(url);
						str.append("<br>");
					}
				}
			}
		}
	}

	private AppBeanDeployStatus getStatus() {
		if (appDb == null) {
			return AppBeanDeployStatus.UNDEPLOYED;
		} else {
			return appDb.getDeployStatus();
		}
	}

	// /**
	// *
	// * 获取用于显示的WorkerCount
	// * @param serviceName
	// * @return
	// */
	// private String getWorkersCountText(String serviceName)
	// {
	// List<RunningWorkerEntity> ws =
	// AppEngineRefresher.INSTANCE.getRunningWorkers(serviceName);
	// return ws == null ? "" : "" + ws.size();
	// }

	/**
	 * 返回状态显示字段:<br>
	 * 字段格式: N/Group(M) 有更新|更新中...p% n/Group(m)<br>
	 * N/Group(M) 当前启动的服务数量/当前配置的服务数量<br>
	 * 
	 * <pre>
	 * 3/3(Core)
	 * Modified! 3/3(Core)
	 * Updating...3->2/3(Core) 
	 * Updated 3->3/3(Core)	
	 * 3/3(Core) Releasing...0/3(Core)
	 * 3/3(Core)
	 * 
	 * 3/3(Core)
	 * Modified 3/3(Core)
	 * Updating... 3/3(Core)->2/2(Test)
	 * Updated 3/3(Core)->2/2(Test)
	 * 2/2(Test) Releasing...2/3(Core)
	 * 2/2(Test)
	 * </pre>
	 * 
	 * @return
	 */
	public String getStatusText() {
		if (appDb == null) {
			// 未部署
			return AppBeanDeployStatus.UNDEPLOYED.getText();
		}

		//
		// 获取当前正在运行的workers

		// 当前Zookeeper数据
		AppEngineRefresher refresher = AppEngineRefresher.INSTANCE;

		try {
			warnColor = null; // 前台告警颜色
			HAServerGroup group = HADatabaseHelper.readServerGroup(appDb.getServerGroup());
			switch (appDb.getDeployStatus()) {
			case DISABLED:
				// 已禁用
				return AppBeanDeployStatus.DISABLED.getText();
			case RUNNING:
				//
				// 运行中，zk与数据库中的内容保持一致：3/3(Core)
				// 如果存在需要释放的数据，需要显示: 释放中...3/3(Core)
				Assert.isTrue("beanId==zk", appDb.getBeanId() == appDb.getZookeeperAppId());
				{

					String n = getRunningWorkersText(appDb.getBeanId());
					String g = getServerGroupText(group);
					warnColor = n.equals(String.valueOf(group.getServerCount())) ? "green" : "red";
					if (deploymentShadow != null) {
						// TODO: 增加正在运行但Worker未释放的信息
					}
					return n + "/" + g;
				}
			case MODIFIED:
				//
				// 已修改
				ApplicationEntity app = refresher.getApplication(appDb.getZookeeperAppId());
				if (app != null) {

					String n = getRunningWorkersText(app.getAppWorkerId(), app.getBeanId());
					String g = getServerGroupText(group);

					if (deploymentShadow != null) {
						// TODO: 增加正在运行但Worker未释放的信息
					}
					return AppBeanDeployStatus.MODIFIED.getText() + " [" + appDb.getZookeeperAppId() + "]" + n + "/"
							+ g + " ";
				} else {
					return AppBeanDeployStatus.MODIFIED.getText() + " " + AppBeanDeployStatus.UNDEPLOYED.getText();
				}
			case DEPLOYING:
				//
				// 部署中, 数据库已经更新, 存在一个部署任务
				// Assert.isTrue("beanId!=zk", appDb.getBeanId() !=
				// appDb.getZookeeperAppId());
				ApplicationEntity app0 = refresher.getApplication(appDb.getZookeeperAppId());
				// Assert.isTrue("serviceDeployName!=zk",
				// appDb.getServiceDeployName() != app0.getAppWorkerId());

				String g0 = app0 != null ? app0.getServerGroup() : "";
				String g1 = appDb.getServerGroup();

				String n0 = getRunningWorkersText(appDb.getBeanId());

				List<RunningWorkerEntity> ws = getRunningWorkers(appDb.getServiceDeployName(), appDb.getBeanId());
				String n1 = ws == null ? "0" : "" + ws.size();
				String a;
				if (g0.equals(g1)) {
					warnColor = n1.equals(String.valueOf(group.getServerCount())) ? "green" : "red";
					a = n0 + "->" + n1 + "/" + getServerGroupText(group);
				} else {
					HAServerGroup newGroup = HADatabaseHelper.readServerGroup(g1);
					warnColor = n1.equals(String.valueOf(newGroup.getServerCount())) ? "green" : "red";
					a = n0 + "/" + getServerGroupText(group) + "->" + n1 + "/" + getServerGroupText(newGroup);
				}
				return AppBeanDeployStatus.DEPLOYING.getText() + a;
			default:
				return "UNEXCEPTED:" + appDb.getDeployStatus();
			}
		} catch (AssertException ex) {
			return ex.getMessage();
		} catch (Exception ex) {
			return "ERROR:" + ex.getMessage();
		}
	}

	/**
	 * 
	 * 返回格式化过的服务器组信息 "4(ServerGroup)"
	 * 
	 * @param serverGroup
	 * @return
	 */
	private String getServerGroupText(HAServerGroup group) {
		if (group == null) {
			return "?:(" + name + ")";
		} else {
			return group.getServerCount() + "(" + name + ")";
		}
	}

	private List<RunningWorkerEntity> getRunningWorkers(String serverName, int beanId) {
		// 热部署需要区分此beanId是否部署成功，冷部署不需要区分
		if (appDb.getHotServerName() != null && appDb.getHotServerName().trim().length() > 0) {
			return AppEngineRefresher.INSTANCE.getHotRunningWorkers(serverName, beanId);
		} else {
			return AppEngineRefresher.INSTANCE.getRunningWorkers(serverName);
		}
	}

	private String getRunningWorkersText(int appId) {
		ApplicationEntity app = AppEngineRefresher.INSTANCE.getApplication(appId);
		if (app == null) {
			return "?";
		} else {
			List<RunningWorkerEntity> ws = getRunningWorkers(app.getAppWorkerId(), appId);
			return ws == null ? "0" : "" + ws.size();
		}
	}

	private String getRunningWorkersText(String serviceName, int beanId) {
		if (StringUtils.isNullOrEmpty(serviceName)) {
			return "?";
		} else {
			List<RunningWorkerEntity> ws = getRunningWorkers(serviceName, beanId);
			return ws == null ? "0" : "" + ws.size();
		}
	}

	//
	// 只有DEPLOYING状态,及后续的回收状态才会存在此数据
	private HADeployment deployment;
	private HADeployment deploymentShadow;

	/**
	 * 
	 * 在AppBeanDeployStatus.MODIFIED状态可以发起全部操作
	 * 
	 * @return
	 */
	public List<HATask> evolution() throws Exception {
		List<HATask> taskList = new ArrayList<HATask>();
		// Step 1.如果不是修改后的状态, 则不执行后续操作
		if (appDb.getDeployStatus() != AppBeanDeployStatus.MODIFIED) {
			HATask task = new HATask("ServerGroup=" + appDb.getServerGroup(), appDb.getServiceDeployName(), "Category "
					+ appDb.getAppCategory() + "-" + appDb.getAppName()
					+ " status is not 'MODIFIED', so can't be deployed");
			task.complete(null);
			taskList.add(task);
			return taskList;
		}

		// Step 2.判断是否为热部署

		ApplicationEntity zkApp = AppEngineRefresher.INSTANCE.getApplication(appDb.getZookeeperAppId());

		String serviceName = AppEngineDatabaseHelper.generateDeployServiceName(appDb.getPackageId(),
				appDb.getServerGroup());

		if (zkApp == null) {
			// 如果是初次，则全部启动
			return startAllByEngine();
		} else if (zkApp != null && !appDb.getServiceDeployName().equals(zkApp.getAppWorkerId())) {
			// 如果是包更新，则全部启动
			return startAllByEngine();
		} else if (zkApp != null && !serviceName.equals(zkApp.getAppWorkerId())) {
			// 如果是包更新，则全部启动
			return startAllByEngine();
		} else if (zkApp != null && !appDb.getServerGroup().equals(zkApp.getServerGroup())) {
			// 如果是服务器组变更，则更新HA_Deployment表并重启
			return startAllByEngine();
		} else if (zkApp != null && !appDb.getGrayFactorys().equals(zkApp.getGrayFactors())) {
			// 如果仅仅是灰度变更，那么更新zk的app节点即可
			syncZookeeper();
			HATask task1 = new HATask("ServerGroup=" + appDb.getServerGroup(), appDb.getServiceDeployName(),
					"Category " + appDb.getAppCategory() + "-" + appDb.getAppName() + " 灰度变更，因此同步更新ZooKeeper.");
			task1.complete(null);
			taskList.add(task1);
			if (!appDb.isEnabled()) {
				// 如果zk中有数据，但是该APP却处于未开启状态，则移除ZK中的该条数据
				AppEngineRefresher.INSTANCE.removeZKApplication(appDb);
				AppEngineDatabaseHelper.syncAppBean(appDb.getBeanId());
				HATask task2 = new HATask("ServerGroup=" + appDb.getServerGroup(), appDb.getServiceDeployName(),
						"Category " + appDb.getAppCategory() + "-" + appDb.getAppName()
								+ " 该App为关闭状态，因此移除存在于ZooKeeper的记录并且同步数据库的运行状态.");
				task2.complete(null);
				taskList.add(task2);
			}
			return taskList;
		} else if (zkApp != null && !appDb.isEnabled()) {
			// 如果zk中有数据，但是该APP却处于未开启状态，则移除ZK中的该条数据
			AppEngineRefresher.INSTANCE.removeZKApplication(appDb);
			AppEngineDatabaseHelper.syncAppBean(appDb.getBeanId());
			HATask task = new HATask("ServerGroup=" + appDb.getServerGroup(), appDb.getServiceDeployName(), "Category "
					+ appDb.getAppCategory() + "-" + appDb.getAppName() + " 该App为关闭状态，因此移除存在于ZooKeeper的记录并且同步数据库的运行状态.");
			task.complete(null);
			taskList.add(task);
			return taskList;
		} else {
			return startAllByEngine();
		}
	}

	public List<HATask> startAllByEngine() throws Exception {
		// 区别对待冷热部署
		if (appDb.getHotServerName() != null && appDb.getHotServerName().trim().length() > 0) {
			// 热部署
			String serviceName = appDb.getHotServerName();
			return hotStart(serviceName);
		} else {
			// 冷部署
			String serviceName = AppEngineDatabaseHelper.generateDeployServiceName(appDb.getPackageId(),
					appDb.getServerGroup());
			return coolStart(serviceName);
		}
	}

	public List<HATask> coolStart(String serviceName) throws Exception {
		final AppEngineDeployManager manager = AppEngineDeployManager.INSTANCE;
		deploymentShadow = manager.getDeployment(appDb.getServiceDeployName(), appDb.getServerGroup());
		deployment = manager.saveDeployment(serviceName, appDb.getPackageId(), appDb.getServerGroup());
		// 如果发现两个ServerName相同，则不需要停掉之前的Server
		if (deployment.getServiceName().equals(appDb.getServiceDeployName())) {
			deploymentShadow = null;
		}
		AppEngineDatabaseHelper.deployAppBean(appDb.getBeanId(), deployment.getServiceName());
		return deployment.startAllByEngine();
	}

	public List<HATask> hotStart(String serviceName) throws Exception {
		// Step 1.寻找到有这个ServerName连接的Center的地址,向其发送部署命令，存储到队列中
		List<HATask> taskList = new ArrayList<HATask>();
		Set<String> centerUrlSet = HADBStatusHelper.getCenterUrlByServerName(serviceName);
		// 热启动不需要停掉之前的部署信息
		deploymentShadow = null;
		LOGGER.info("Found {} centerUrl.", centerUrlSet.size());
		if (centerUrlSet != null && centerUrlSet.size() > 0) {
			for (String centerUrl : centerUrlSet) {
				hotSwapEventQueue.add(new TwoTuple<String, String>(serviceName, centerUrl));
				HATask task1 = new HATask("centerUrl=" + centerUrl, "hostServerName=" + serviceName, "通知refresh");
				task1.complete(null);
				taskList.add(task1);
			}
		}
		// Step 2.更新FAE_Application表的serviceDeployName的名称为hotServerName
		AppEngineDatabaseHelper.deployAppBean(appDb.getBeanId(), serviceName);
		return taskList;
	}

	/**
	 * 这个队列主要负责调用center，使热部署的指令通过center转发到对应的worker上
	 */
	private static FireEventQueue<TwoTuple<String, String>> hotSwapEventQueue = FireEventQueue
			.newFreshBoxFireEventQueue(2000, new Action<TwoTuple<String, String>>() {
				@Override
				public void run(TwoTuple<String, String> twoTuple) {
					try {
						// 通过hacenter进行透传操作
						String hotServerName = twoTuple.getFirst();
						String centerURL = twoTuple.getSecond();
						LOGGER.info("Post {} ,hotServerName={}", centerURL, hotServerName);
						RpcDuplexClient client = new RpcDuplexClient(RpcTcpEndpoint.parse(centerURL));
						client.setExecutor(ExecutorFactory.getExecutor("RPC-Invoke"));
						client.connectSync();
						RpcMethodStub stub = client.getMethodStub("AppHotSwapService", "refresh");
						RpcClientTransaction tx = stub.createTransaction();
						tx.putExtension(1001, hotServerName);
						// 设置超时时间5分钟
						tx.setTimeout(5 * 60 * 1000);

						// 异步处理请求结果
						// 开始计时
						final RpcFuture future = tx.begin();
						future.addListener(new EventHandler<RpcResults>() {
							public void run(Object sender, RpcResults result) {
								if (result.getError() != null) {
									LOGGER.error("hotSwapEventQueue failed ", result.getError());
								}
							}
						});
					} catch (Exception e) {
						LOGGER.error("hotSwapEventQueue failed,centerURL = " + twoTuple.getSecond(), e);
					}
				}
			});

	public List<HATask> hotSwapAllByEngine(String serviceName) throws Exception {
		final AppEngineDeployManager manager = AppEngineDeployManager.INSTANCE;
		deployment = manager.saveDeployment(serviceName, appDb.getPackageId(), appDb.getServerGroup());
		AppEngineDatabaseHelper.deployAppBean(appDb.getBeanId(), deployment.getServiceName());
		return deployment.hotSwapAllByEngine();
	}

	public void finishDeploy() throws Exception {
		if (appDb != null && appDb.getDeployStatus() == AppBeanDeployStatus.DEPLOYING) {
			List<RunningWorkerEntity> workers = getRunningWorkers(appDb.getServiceDeployName(), appDb.getBeanId());
			HAServerGroup group = HADatabaseHelper.readServerGroup(appDb.getServerGroup());

			boolean successed = true;
			for (HAServer server : group.getServers()) {
				boolean gocha = false;
				for (RunningWorkerEntity worker : workers) {
					if (StringUtils.equal(worker.getServerName(), server.getServerName())) {
						gocha = true;
					}
				}
				if (!gocha) {
					successed = false;
					break;
				}
			}

			if (successed) {
				syncZookeeper();
			}
		}
	}

	private void syncZookeeper() throws Exception {
		try {
			AppEngineRefresher.INSTANCE.syncApplication(appDb);
			AppEngineDatabaseHelper.syncAppBean(appDb.getBeanId());
			if (deploymentShadow != null) {
				// 仅仅当FAE_Application中不存在这个service的信息时，才清理掉这个进程
				if (!AppEngineDatabaseHelper.isRunService(deploymentShadow.getServiceName())) {
					deploymentShadow.delayStopAll();
				}
			}
			AppEngineDatabaseHelper.removeInvalidWorkerDeployment();
		} catch (Exception ex) {
			LOGGER.error("syncZookeeper failed", ex);
			throw ex;
		}
	}

	public List<HATask> getRunningTasks() {
		List<HATask> tasks = new ArrayList<HATask>();
		if (deployment != null) {
			tasks.addAll(deployment.getRunningTasks());
		}
		if (deploymentShadow != null) {
			tasks.addAll(deploymentShadow.getRunningTasks());
		}
		return tasks;
	}

}
