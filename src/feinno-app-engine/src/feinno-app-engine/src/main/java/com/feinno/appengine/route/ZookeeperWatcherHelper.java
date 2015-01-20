/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Aug 20, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.runtime.configuration.ApplicationEntity;
import com.feinno.appengine.runtime.configuration.RunningWorkerEntity;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ZookeeperWatcherHelper
{
	private static final Logger logger = LoggerFactory.getLogger(ZookeeperWatcherHelper.class);

	/**
	 * NOTE:
	 * 
	 * @param workers
	 */
	public static void sortRunningWorkers(List<RunningWorkerEntity> workers)
	{
		// sort, order by appWorkerId, lastTime
		if (logger.isDebugEnabled()) {
			logger.debug("===========================================");
			for (RunningWorkerEntity worker : workers) {
				logger.debug(worker.getServerName() + "\t" + worker.getServiceUrls());
			}
		}

		Collections.sort(workers, workerComparator);
	}

	public static List<RunningWorkerEntity> dropZombies(List<RunningWorkerEntity> workers)
	{ // TODO return List<Wrapper> ?

		sortRunningWorkers(workers);

		// remove duplicated workers
		List<RunningWorkerEntity> res = new ArrayList<RunningWorkerEntity>();
		String lastId = null;
		for (RunningWorkerEntity worker : workers) {
			String id = worker.getAppWorkerId() + worker.getServerName();
			if (lastId == null || !lastId.equals(id)) {
				res.add(worker);
				lastId = id;
			} else {
				continue;
			}
		}
		return res;
	}

	public static Map<String, byte[]> loadPath(ZooKeeper zk, String path, boolean watch) throws KeeperException, InterruptedException
	{
		List<String> children = zk.getChildren(path, watch);
		Map<String, byte[]> datas = new HashMap<String, byte[]>();

		for (String child : children) {
			logger.debug("node name : {}", child);
			try {
				byte[] data = zk.getData(path + "/" + child, false, null);
				datas.put(child, data);
				logger.debug("node data len : {}", data.length);
			} catch (Exception e) {
				logger.warn("exception occurred (ignored) during fetching node[{}] data, ignored.\n Exception:{}",
						child, e);
			}
		}
		return datas;
	}

	public static List<ApplicationEntity> parseApplicationData(Map<String, byte[]> dataList)
	{
		logger.debug("got Application Node data list, size is {}", dataList.size());
		List<ApplicationEntity> apps = new ArrayList<ApplicationEntity>();
		for (Entry<String, byte[]> entry: dataList.entrySet()) {
			ApplicationEntity app = new ApplicationEntity();
			try {
				app.setNodeKey(entry.getKey());
				app.parseFrom(entry.getValue());
				String cat = app.getCategory();
				String name = app.getName();
				if (logger.isDebugEnabled()) {
					logger.debug("got App cat [{}], name [{}]", cat, name);
				}
				apps.add(app);
			} catch (Exception e) {
				logger.error("got exception during entity unmarshalling, name [{}], ignored.", app.getName(), e);
			}
		}
		return apps;
	}

	public static List<RunningWorkerEntity> parseRunningWorkerData(Collection<byte[]> dataList)
	{
		logger.debug("got zk RunningWorkers Node data list, size is {}", dataList.size());
		// parse protobuf
		List<RunningWorkerEntity> raw = new ArrayList<RunningWorkerEntity>();
		for (byte[] data : dataList) {
			RunningWorkerEntity worker = new RunningWorkerEntity();
			try {
				worker.parseFrom(data);
				raw.add(worker);
			} catch (Exception e) {
				logger.warn("parse FAE_RunningWorker failed, ignored.", e);
			}
		}
		List<RunningWorkerEntity> clean = dropZombies(raw);
		return clean;
	}

	static final Comparator<RunningWorkerEntity> workerComparator = new Comparator<RunningWorkerEntity>() {
		@Override
		public int compare(RunningWorkerEntity left, RunningWorkerEntity right)
		{

			String lId = left.getAppWorkerId();
			String rId = right.getAppWorkerId();
			int cv = lId.compareTo(rId);
			if (cv != 0)
				return cv;

			String lsn = left.getServerName();
			String rsn = right.getServerName();
			cv = lsn.compareTo(rsn);
			if (cv != 0)
				return cv;

			long lt = left.getLastTime().getTime();
			long rt = right.getLastTime().getTime();
			if (lt == rt) {
				logger.warn("got duplicated Running worker!!!");
				return 0;
			} else {
				return rt < lt ? -1 : 1;
			}
		}
	};
}
