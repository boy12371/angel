/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-7-27
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.runtime;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.runtime.configuration.RunningWorkerEntity;
import com.feinno.ha.ServiceSettings;
import com.feinno.util.ServiceEnviornment;

/**
 * AppEngine对ZooKeeper的连接器， 当启动一个Worker时，如果它启动了任何的远程AppBean,
 * 都会在ZooKeeper中注册一条RunningWorker记录, 并附加上自己提供服务的端口
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineZookeeperConnector {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppEngineZookeeperConnector.class);
	/**
	 * ZooKeeper会话超时时间, 好像是zk 3.3.2的缺省最大值的一半
	 */
	private static final int ZK_SESSION_TIMEOUT = 10 * 2000;
	private static final String ZK_RUNIING_WORKERS_PATH_PREFIX = "/FAE/RunningWorkers/";

	private ZooKeeper zk;
	private String zkHosts;
	private String workerName;
	private byte[] zkData;
	private org.apache.zookeeper.Watcher dummyWatcher;
	private String acturalPath;

	// private boolean quiting;

	public AppEngineZookeeperConnector(String zkHosts, String workerName, String serviceUrls) throws IOException {
		this.zkHosts = zkHosts;
		this.workerName = workerName;
		this.zkData = buildZKNodeData(workerName, serviceUrls,null,false);
		this.dummyWatcher = new DummyWatcher();
	}

	public AppEngineZookeeperConnector(String zkHosts, String workerName, String serviceUrls, List<Integer> appBeanIds,boolean isGlobal) throws IOException {
		this.zkHosts = zkHosts;
		this.workerName = workerName;
		this.zkData = buildZKNodeData(workerName, serviceUrls,appBeanIds,isGlobal);
		this.dummyWatcher = new DummyWatcher();
	}
	
	public void connect() throws KeeperException, InterruptedException, IOException {
		zk = registerWorker2ZK(zkData);
	}

	private ZooKeeper registerWorker2ZK(byte[] data) throws IOException, KeeperException, InterruptedException {
		String zkNode = ZK_RUNIING_WORKERS_PATH_PREFIX + workerName + "-";

		ZooKeeper newZkClient = new ZooKeeper(zkHosts, ZK_SESSION_TIMEOUT, dummyWatcher); // watcher为null时，日志很难看
		try {
			acturalPath = newZkClient.create(zkNode, data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			newZkClient.register(new ComponentZKWatcher(data));
		} catch (Exception t) {
			// 如果创建失败，则关闭并回收client端， 重抛异常
			try {
				newZkClient.close();
			} catch (Exception e) {
				LOGGER.warn("create new zookeeper client failed.", e);
			}
			throw new RuntimeException("", t);
		}
		return newZkClient;
	}
	
	public void setDynamicApp2ZK(String workerName, String serviceUrls,List<Integer> apps) throws IOException, KeeperException, InterruptedException {
		//String zkNode = ZK_RUNIING_WORKERS_PATH_PREFIX + workerName + "-";
		LOGGER.info("setDynamicApp2ZK actural path is:" + acturalPath);
		try {
			Stat stat = zk.exists(acturalPath, false);
			byte[] data = buildZKNodeData(workerName,serviceUrls,apps,false);
			zkData = data;
			if(stat != null)
			{
				LOGGER.info("stat != null. setDynamicApp2ZK actural path is:" + acturalPath);
				zk.setData(acturalPath,zkData, -1);
				zk.register(new ComponentZKWatcher(zkData));
			}
			else
				this.connect();
	
		} catch (Exception t) {
			// 如果创建失败，则关闭并回收client端， 重抛异常
			try {
				zk.close();
			} catch (Exception e) {
				LOGGER.warn("create new zookeeper client failed.", e);
			}
			throw new RuntimeException("", t);
		}
	}

	public void close() throws InterruptedException {
		if (zk != null) {
			zk.close();
		}
	}

	private byte[] buildZKNodeData(String workerName, String serviceUrls,List<Integer> apps,boolean isGlobal) {
		RunningWorkerEntity worker = new RunningWorkerEntity();
		worker.setAppWorkerId(workerName);
		worker.setServerName(ServiceEnviornment.getComputerName());
		int port = ServiceSettings.INSTANCE.getServicePort("monitor");
		String host = ServiceSettings.INSTANCE.getServerAddress();
		if (port > 0) {
			serviceUrls += ("monitor=http://" + host + ":" + port + ";");
		} else {
			LOGGER.warn("server address not found, http monitor registration abort.");
		}
		worker.setServiceUrls(serviceUrls);
		worker.setLastTime(new java.util.Date(System.currentTimeMillis()));
		worker.setAppBeans(apps);
		worker.setGlobal(isGlobal);
		return worker.toByteArray();
	}

	private static class DummyWatcher implements org.apache.zookeeper.Watcher {
		@Override
		public void process(WatchedEvent arg0) {
		}
	}

	private class ComponentZKWatcher implements org.apache.zookeeper.Watcher {
		private final byte[] data;

		/**
		 * @param data
		 *            RunningWorker在zk上的注册信息
		 */
		public ComponentZKWatcher(byte[] data) {
			this.data = new byte[data.length];
			System.arraycopy(data, 0, this.data, 0, data.length);
		}

		@Override
		public void process(WatchedEvent event) {
			if (event.getType() == Event.EventType.None) {
				switch (event.getState()) {
				case SyncConnected:
					LOGGER.info("zk connected.");
					break;
				case Disconnected:
					LOGGER.warn("zk disconnected, passively waiting SyncConnected");
					break;
				case Expired:
				case AuthFailed:
					LOGGER.warn("zk session expired");
					try {
						close();
					} catch (Exception e) {
						LOGGER.warn("zk close failure.", e);
					}
					LOGGER.info("recovering");

					for (;;) {
						try {
							zk = registerWorker2ZK(data);
							LOGGER.warn("running worker registered (recovery mode).");
							break;
						} catch (Exception e) {
							try {
								LOGGER.warn("recovery failed, will retry (5000ms later).");
								Thread.sleep(5 * 1000);
							} catch (InterruptedException e1) {
								LOGGER.warn("dream interrupted");
							}
						}
					}
					break;
				default:
					LOGGER.warn("what? we got an unexpected event.");
				}
			}
		}
	}
}
