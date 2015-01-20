/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Aug 23, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.deployment;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.configuration.FAE_Application;
import com.feinno.appengine.route.ZookeeperWatcher;
import com.feinno.appengine.runtime.configuration.ApplicationEntity;
import com.feinno.appengine.runtime.configuration.RunningWorkerEntity;
import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.util.Action;
import com.feinno.util.DictionaryList;
import com.feinno.util.Func;

/**
 * 
 * 维护所有AppBean的部署及运行状态的类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineRefresher {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppEngineRefresher.class);

	private static final String FAE_APP_PATH = "/FAE/Applications";

	public static final AppEngineRefresher INSTANCE = new AppEngineRefresher();

	public static void initialize(String zkHosts) throws Exception {
		INSTANCE.refreshDatabase();
		INSTANCE.initZookeeper(zkHosts);
	}

	// from database.FAE_Application
	private Map<Integer, AppBeanDeployment> beans;

	// from Zookeeper/Applications
	private Map<Integer, ApplicationEntity> applications;

	// from Zookeeper/RunningWorkers
	private DictionaryList<String, RunningWorkerEntity> runningWorkers;

	private ZookeeperWatcher watcher;

	public AppEngineRefresher() {
		beans = new Hashtable<Integer, AppBeanDeployment>();
		applications = new Hashtable<Integer, ApplicationEntity>();
	}

	public void refreshDatabase() throws Exception {
		List<Integer> beanIdList = new ArrayList<Integer>();
		// 从FAE_Application中读取已经部署的数据
		DataTable table = AppEngineDatabaseHelper.readApplications();
		for (DataRow row : table.getRows()) {
			FAE_Application app = new FAE_Application(row);
			AppBeanDeployment bean = beans.get(app.getBeanId());
			if (bean == null) {
				bean = new AppBeanDeployment(app);
				beans.put(app.getBeanId(), bean);
			} else {
				bean.reloadDb(app);
			}
			beanIdList.add(app.getBeanId());
		}

		//
		// 从FAE_AppBean中获取未部署的AppBean, 按category-name
		table = AppEngineDatabaseHelper.readUndeployedAppBeans();
		for (DataRow row : table.getRows()) {
			int beanId = row.getInt("BeanId");
			int packageId = row.getInt("PackageId");
			String category = row.getString("AppCategory");
			String name = row.getString("AppName");
			String annotations = row.getString("Annotations");
			AppBeanDeployment bean = new AppBeanDeployment(packageId, beanId, category, name, annotations);
			beans.put(beanId, bean);
			beanIdList.add(beanId);
		}

		// 如果不存在,则需要移除
		List<Integer> remmoveBeanIdList = new ArrayList<Integer>();
		for (Integer beanId : beans.keySet()) {
			boolean isExists = false;
			for (int id : beanIdList) {
				if (id == beanId) {
					isExists = true;
				}
			}
			if (!isExists) {
				remmoveBeanIdList.add(beanId);
			}
		}
		for (Integer beanId : remmoveBeanIdList) {
			beans.remove(beanId);
		}
	}

	public void initZookeeper(String zkHosts) throws Exception {
		LOGGER.info("begin create ZooKeeperWatcher");
		watcher = new ZookeeperWatcher(zkHosts);

		watcher.setAppsUpdateCallback(new Action<List<ApplicationEntity>>() {
			@Override
			public void run(List<ApplicationEntity> a) {
				updateApplications(a);
			}
		});

		watcher.setWorkersUpdaterCallback(new Action<List<RunningWorkerEntity>>() {
			@Override
			public void run(List<RunningWorkerEntity> a) {
				updateRunningWorkers(a);
			}
		});

		watcher.init();
	}

	public List<AppBeanDeployment> getAppBeans() {
		List<AppBeanDeployment> ret = new ArrayList<AppBeanDeployment>();
		for (AppBeanDeployment d : beans.values()) {
			ret.add(d);
		}
		return ret;
	}

	private void updateApplications(List<ApplicationEntity> apps) {
		try {
			applications = new Hashtable<Integer, ApplicationEntity>();
			for (ApplicationEntity app : apps) {
				applications.put(app.getBeanId(), app);

				//
				// 这段代码用于检测已经卸载的Bean,为简化,暂不实现
				// if (beans.get(app.getBeanId()) == null) {
				// AppBeanDeployment deployment = new AppBeanDeployment(app);
				// beans.put(app.getBeanId(), deployment);
				// }
			}
		} catch (Exception ex) {
			LOGGER.error("updateApplication failed {}", ex);
		}
	}

	private void updateRunningWorkers(List<RunningWorkerEntity> rws) {
		try {
			DictionaryList<String, RunningWorkerEntity> list = new DictionaryList<String, RunningWorkerEntity>();
			list.fillWith(rws, new Func<RunningWorkerEntity, String>() {
				@Override
				public String exec(RunningWorkerEntity e) {
					return e.getAppWorkerId();
				}
			});
			runningWorkers = list;
		} catch (Exception ex) {
			LOGGER.error("updateRunningWorkers failed {}", ex);
		}
	}

	/**
	 * 
	 * 获取正在运行的ApplicationEntity, Zookeeper中的数据
	 * 
	 * @param appId
	 * @return
	 */
	public ApplicationEntity getApplication(int appId) {
		return applications.get(appId);
	}

	/**
	 * 
	 * 获取某个正在运行的runningWorkers数据
	 * 
	 * @param serviceName
	 * @return
	 */
	public List<RunningWorkerEntity> getRunningWorkers(String serviceName) {
		if (serviceName == null) {
			return null;
		} else {
			return runningWorkers.get(serviceName);
		}
	}

	/**
	 * 
	 * 获取热服务中某个正在运行的runningWorkers数据
	 * 
	 * @param serviceName
	 * @return
	 */
	public List<RunningWorkerEntity> getHotRunningWorkers(String serviceName, Integer beanId) {
		if (serviceName == null)
			return null;
		if (beanId == null || beanId == -1) {
			return runningWorkers.get(serviceName);
		}
		List<RunningWorkerEntity> list = runningWorkers.get(serviceName);
		List<RunningWorkerEntity> result = new ArrayList<RunningWorkerEntity>();
		for (RunningWorkerEntity entity : list) {
			if (entity != null && entity.getAppBeans() != null) {
				if (entity.getAppBeans().contains(beanId)) {
					result.add(entity);
				}
			}
		}
		return result;
	}

	public void syncApplication(FAE_Application app) throws Exception {
		try {
			ZooKeeper zk = watcher.getZk();
			byte[] buffer = app.toApplicationEntity().toByteArray();
			ApplicationEntity entity = AppEngineRefresher.INSTANCE.getApplication(app.getZookeeperAppId());
			Stat stat = null;
			if (entity != null) {
				stat = zk.exists(FAE_APP_PATH + "/" + entity.getNodeKey(), false);
			}
			if (stat != null) {
				if (app.isEnabled()) {
					zk.setData(FAE_APP_PATH + "/" + entity.getNodeKey(), buffer, stat.getVersion());
				} else {
					zk.delete(FAE_APP_PATH + "/" + entity.getNodeKey(), stat.getVersion());
				}
			} else {
				if (app.isEnabled()) {
					zk.create(FAE_APP_PATH + "/" + ApplicationEntity.generateNoteKey(), buffer, Ids.OPEN_ACL_UNSAFE,
							CreateMode.PERSISTENT);
				}
			}
		} catch (Exception e) {
			LOGGER.error("create beanId={} FAE_Application node failed", e);
			throw e;
		}
	}

	public void removeZKApplication(FAE_Application app) throws Exception {
		try {
			ZooKeeper zk = watcher.getZk();
			ApplicationEntity entity = AppEngineRefresher.INSTANCE.getApplication(app.getZookeeperAppId());
			Stat stat = null;
			if (entity != null) {
				stat = zk.exists(FAE_APP_PATH + "/" + entity.getNodeKey(), false);
			}
			if (stat != null) {
				zk.delete(FAE_APP_PATH + "/" + entity.getNodeKey(), stat.getVersion());
			}
		} catch (Exception e) {
			LOGGER.error("remove beanId={} FAE_Application node failed", e);
			throw e;
		}
	}

	public AppBeanDeployment getAppBean(int i) {
		// TODO Auto-generated method stub
		return beans.get(i);
	}
}
