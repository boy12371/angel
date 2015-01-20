/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.configuration.AppBeanAnnotationsLoader;
import com.feinno.appengine.configuration.AppEngineSettings;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.appengine.runtime.AppHost;
import com.feinno.appengine.runtime.configuration.ApplicationEntity;
import com.feinno.appengine.runtime.configuration.RunningWorkerEntity;
import com.feinno.appengine.testing.AppBeanInjectorService.InjectArgs;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.ha.ServiceSettings;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.serialization.protobuf.ProtoManager;
import com.feinno.util.PropertiesUtil;

/**
 * 
 * @author Will.jingmiao
 *
 */
public class AppBeanLinkTestProxy {
	/**
	 * 
	 * xxx com.feinno.imps.message.sipc.SendSmsSipcAppBean sid.in(888888888)
	 * 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		//
		// 得到所有特性类型AppBean的proxy地址
		try {
			// ServiceSettings.initDebug("settings.properties"); TODO
			LOGGER.info("AppBeanLinkTestProxy debug running start.");
			ServiceSettings.init();
			// ServiceSettings.init("settings.properties");
			Class<? extends AppBean> beanClazz = (Class<? extends AppBean>) Class.forName(args[0]);
			LOGGER.info("获得运行app:" + beanClazz.getName());
			LOGGER.info("灰度因子:" + args[1]);
			LOGGER.info("启动AppEngineManager的所有基础支持.");
			AppEngineManager.INSTANCE.initialize();
			LOGGER.info("通过类型加载一个AppBean.");
			AppEngineManager.INSTANCE.loadAppBean(beanClazz);

			AppEngineSettings settings = new AppEngineSettings();
			LOGGER.info("获取zk地址.");
			Properties props = ConfigurationManager.loadProperties("appengine.properties", null, null);
			PropertiesUtil.fillBean(props, settings);
			String zkHosts = settings.getZkHosts();
			LOGGER.info("zk地址:" + zkHosts);
			int sessionTimeout = 30000;
			ZooKeeper zk = new ZooKeeper(zkHosts, sessionTimeout, new Watcher() {
				public void process(WatchedEvent event) {
					// 不做处理
				}
			});
			AppName appAn = beanClazz.getAnnotation(AppName.class);
			List<String> appEntitys = getApplicationEntitys(zk, appAn.category(), appAn.name());
			if (appEntitys == null) {
				return;
			}
			List<String> serviceUrls = getRunningWorkerEntitys(zk, appEntitys);
			if (serviceUrls == null) {
				return;
			}
			for (String serviceUrl : serviceUrls) {
				RpcDuplexClient client = new RpcDuplexClient(RpcTcpEndpoint.parse(serviceUrl));
				AppBeanInjectorService injector = client.getService(AppBeanInjectorService.class);
				AppBeanAnnotations annos = AppBeanAnnotationsLoader.getAppBeanAnnotaions(beanClazz);
				AppHost host = AppEngineManager.INSTANCE.getHosts().get(annos.getClassInfo().getBaseClass().getType());
				if (host != null) {
					// LOGGER.info("负载AppBean的Host != null 注册.");
					host.registerInjectorService(client);
				}
				LOGGER.info("connectSync");
				client.connectSync();
				// LOGGER.info("new InjectArgs");
				InjectArgs ia = new InjectArgs();
				LOGGER.info("AppBean的类型信息annos:" + annos.toJsonString());
				ia.setAnnos(annos.toJsonString());
				ia.setGrayFactors(args[1]);
				LOGGER.info("injector InjectArgs");
				injector.inject(ia);
				clients.add(injector);
				// injector.inject();
			}
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					LOGGER.info("保持客户端心跳:" + clients.size());
					for (AppBeanInjectorService injector : clients) {
						injector.ping();
					}
				}
			}, 60 * 1000, 60 * 1000);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static List<String> getApplicationEntitys(ZooKeeper zk, String category, String name) throws Exception {
		LOGGER.info("categoryName=" + category + name);
		List<String> apps = new ArrayList<String>();
		List<String> children = zk.getChildren(FAE_APP_PATH, false, null);
		Map<String, byte[]> datas = new HashMap<String, byte[]>();

		for (String child : children) {
			try {
				byte[] data = zk.getData(FAE_APP_PATH + "/" + child, false, null);
				// LOGGER.info("获得的zk信息长度:"+data.length);
				datas.put(child, data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (Entry<String, byte[]> entry : datas.entrySet()) {
			ApplicationEntity app = new ApplicationEntity();
			try {
				ProtoManager.parseFrom(app, entry.getValue());
				app.setNodeKey(entry.getKey());
				if (category != null && name != null && category.equals(app.getCategory()) && name.equals(app.getName())) {
					String appWorkerId = app.getAppWorkerId();
					apps.add(appWorkerId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("获得匹配app个数:" + apps.size());
		// for(String appsName :apps){
		// LOGGER.info("appNames:"+apps);
		// }
		return apps;
	}

	public static List<String> getRunningWorkerEntitys(ZooKeeper zk, List<String> appEntitys) throws Exception {
		List<String> workers = new ArrayList<String>();
		List<String> children = zk.getChildren(FAE_WORKER_PATH, false, null);
		List<byte[]> dataList = new ArrayList<byte[]>();

		for (String child : children) {
			try {
				byte[] data = zk.getData(FAE_WORKER_PATH + "/" + child, false, null);
				dataList.add(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (byte[] data : dataList) {
			RunningWorkerEntity worker = new RunningWorkerEntity();
			try {
				ProtoManager.parseFrom(worker, data);// worker.getServerName();worker.getServiceUrls();
				LOGGER.info("runningWorker:" + worker.getAppWorkerId() + "-" + worker.getServiceUrls());
				if (appEntitys.contains(worker.getAppWorkerId())) {
					String serviceUrl = worker.getServiceUrl("debug");
					LOGGER.info("匹配的URL debug:" + serviceUrl);
					workers.add(serviceUrl);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("获得匹配上的runningWorker个数:" + workers.size());
		return workers;
	}

	private static List<AppBeanInjectorService> clients = new ArrayList<AppBeanInjectorService>();
	public final static String FAE_APP_PATH = "/FAE/Applications";
	public final static String FAE_WORKER_PATH = "/FAE/RunningWorkers";
	private static final Logger LOGGER = LoggerFactory.getLogger(AppBeanLinkTestProxy.class);
}