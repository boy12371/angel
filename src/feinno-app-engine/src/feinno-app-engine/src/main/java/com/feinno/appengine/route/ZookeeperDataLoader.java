/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Aug 20, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ZookeeperDataLoader implements Runnable, Watcher
{
	public static interface DataMonitor
	{
		void update(Map<String, byte[]> datas);
	}
	
	private static final Logger logger = LoggerFactory.getLogger(ZookeeperDataLoader.class);
	
	private LinkedBlockingQueue<String> notificationQueue = new LinkedBlockingQueue<String>();
	private Map<String, DataMonitor> monitors = new HashMap<String, DataMonitor>();
	private Set<String> notified = new java.util.HashSet<String>();	// 不会被其他线程使用

	private long updateInterval;
	private Thread notificationRecorder;
	private ZooKeeper zk;

	private volatile boolean shutdown = false;

	public ZookeeperDataLoader(long updateInterval, ZooKeeper zk)
	{
		this.updateInterval = updateInterval;
		this.zk = zk;
		this.notificationRecorder = new Thread(this, "DataLoader");
		notificationRecorder.start();
	}

	public void load(String node, DataMonitor dm)
	{
		logger.debug("loading node {}", node);
		monitors.put(node, dm);
		logger.debug("monitors put {}-{}", node, dm);
		load(node);
	}

	public void shutdown()
	{
		this.shutdown = true;
		notificationRecorder.interrupt();
	}

	private void load(String node)
	{
		logger.debug("loading and watching [{}] node", node);
		Stat stat = new Stat();
		try {
			List<String> children = zk.getChildren(node, this);
			Map<String, byte[]> datas = new HashMap<String, byte[]>();
			
			logger.debug("got list");
			for (String child : children) {
				logger.debug("node name : {}", child);
				try {
					byte[] data = zk.getData(node + "/" + child, this, stat);
					datas.put(child, data);
					logger.debug("node data len : {}", data.length);
				} catch (Exception e) {
					logger.warn("exception occurred (ignored) during fetching node[{}] data, ignored.\n Exception:{}",
							child, e);
				}
			}

			DataMonitor dm = this.monitors.get(node);
			if (dm != null) {
				logger.debug("updating..");
				dm.update(datas);
			} else {
				logger.debug("NO DataMonitor found");
			}
		} catch (Exception e) {
			// add this node to watch list
			handleThis(node);
			logger.warn("exception occurred  during *getChildren*.", e);
		}
	}

	private void drainAll()
	{
		for (String node : notified) {
			load(node);
		}
		notified.clear();
	}

	private void recordNodeNotification(String node)
	{
		notified.add(node);
	}

	private boolean hasNotifications()
	{
		return notified.size() > 0;
	}

	private void handleThis(String node)
	{
		try {
			notificationQueue.put(node);
		} catch (InterruptedException e) {
			logger.warn("notificationQueue interrupted.");
		}
	}

	/**
	 * 在updateInterval..
	 */
	@Override
	public void run()
	{
		logger.debug("zk data loader started.");
		String node;
		while (!shutdown) {
			try {
				if (!hasNotifications()) {
					// 没有累积事件，死等
					logger.debug("waiting...(no timeout)");
					node = notificationQueue.take();
					recordNodeNotification(node);
				} else {
					// 有累积事件
					node = notificationQueue.poll(updateInterval, TimeUnit.MILLISECONDS);
					if (node == null) {
						// 指定时间间隔内，无数据更新，视为稳定
						logger.debug("got notification (from timer), stabilized, reloading nodes");
						drainAll();
					} else {
						// 记录变化的节点，推迟数据加载
						logger.debug("got notification (node : {}).", node);
						recordNodeNotification(node);
					}
				}
			} catch (InterruptedException e) {
				logger.warn("queue polling operation interrupted.", e);
				// break; // never
			}
		}
		logger.info("loader thread quit.");
	}

	String toParentNode(String node)
	{
		String arg = node;
		if (node.endsWith("/")) {
			arg = node.substring(0, node.length() - 1);
		}
		return arg.substring(0, node.lastIndexOf('/'));
	}

	// ZooKeeper event thread
	@Override
	public void process(WatchedEvent event)
	{
		logger.debug("processing WatchedEvent {}", event);
		EventType et = event.getType();
		if (et == EventType.NodeChildrenChanged) {
			logger.debug("processing NodeChildrenChanged {}", event);
			handleThis(event.getPath());
		} else if (et == EventType.NodeDataChanged) {
			// TODO 将事件转化为父节点的事件, 有点太不经济吧？
			logger.debug("processing NodeDataChanged {}", event);
			handleThis(toParentNode(event.getPath()));
		} else {
			logger.debug("got irrelevant event {}", event);
		}
	}
}
