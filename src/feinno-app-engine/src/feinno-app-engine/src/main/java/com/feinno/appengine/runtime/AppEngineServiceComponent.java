/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft 2011-2-1
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.runtime;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.configuration.Appbeans;
import com.feinno.appengine.configuration.FAE_Application;
import com.feinno.appengine.testing.AppBeanInjectorServiceImps;
import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.ha.ServiceComponent;
import com.feinno.ha.ServiceSettings;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.duplex.RpcDuplexServer;
import com.google.gson.Gson;

/**
 * AppEngine 服务抽象类
 * 
 * reload/refresh app过程中，仅可能发生卸载操作，不会有新增组件出现
 * 在一次refresh操作中，如果发现组件已经全部被卸载，则8分钟后，停止服务，jvm退出(return code 0)
 * 如果还有剩余的组件，则仅记录卸载操作（将组件标识为已卸载），但是不停止任何服务
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineServiceComponent implements ServiceComponent
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AppEngineServiceComponent.class);

	private AppEngineManager manager;
	private AppEngineZookeeperConnector zkConnector;
	private ScheduledExecutorService scheduler;

	private static final int SHUTDOWN_DELAY_NO_COMPONENT = 8 * 1000;
	private static final int SHUTDOWN_DELAY_STOP_COMMAND = 8 * 60 * 1000;

	/*
	 * @see com.feinno.ha.ServiceComponent#start()
	 */
	/**
	 * {在这里补充功能说明}
	 */
	@Override
	public void start() throws Exception
	{
		//
		// 初始化延迟退出执行器
		scheduler = Executors.newScheduledThreadPool(1);
		
		//
		// 启动AppEngine需要以下步骤, 任何步骤抛出异常直接结束
		// 
		// 1.1. 读取appengine.properties配置(HA服务外壳已经启动)
		// 1.2. 启动AppEngine核心部分，并加载extension
		// 1.3. extension负责加载扩展的AppBeanHost, 以及ResourceLoader
		// 1.4. 加载ResourceFactory
		LOGGER.info("initializing... AppEngineManager");
		manager = AppEngineManager.INSTANCE;
		manager.initialize();
 
		if(ServiceSettings.INSTANCE.isNoZK())
			startNoZK();
		else
		{
			//
			// 2.1. 读取FAE_Application表，依次加载AppBean
			LOGGER.info("initializing... load FAE_Application");
			ConfigTable<Integer, FAE_Application> apps = ConfigurationManager.loadTable(Integer.class, FAE_Application.class, "FAE_Application", null);
			
			LOGGER.info("initializing... load apps");
			List<FAE_Application> loadApps = loadApps(apps);
			
			if (loadApps.size() == 0) {
				// 
				// 如果未加载任何app, 8秒后自杀(不能马上自杀)
				LOGGER.error("no app loaded kill in 8 secs");
				dieSoft(SHUTDOWN_DELAY_NO_COMPONENT, 0);
				return;
			}
			
			// 获取被加载得appBeanId
			List<Integer> appBeanIds = new ArrayList<Integer>();
			for (FAE_Application application : loadApps ) {
				appBeanIds.add(application.getBeanId());
			}
			
			//
			// 2.2. 启动Host, 打开监听端口
			LOGGER.info("initializing... start hosts");
			manager.bringupHosts();
			
			// 3.2. 注册回调更新接口 TODO
	//		LOGGER.info("initializing... register callback service");
	//		ServiceSettings.getControlleeAgent().getDuplexClient();
	//		ServiceSettings.getWorkerAgent()
	//		
	//		if(rpcDuplexClient!=null)
	//			rpcDuplexClient.registerCallbackService(new AppRefresher() {
	//				@Override
	//				public void refresh(RpcNull foo, RpcContext ctx)
	//				{
	//					//
	//					// TODO:refresh只支持卸载应用, 暂不支持
	//					// throw new UnsupportedOperationException("没实现呢, 暂时不支持refresh");
	//					ctx.end();
	//				}
	//			});
	
			//
			// 3.1. 连接ZooKeeper注册自己
			LOGGER.info("initializing... create zookeeper connector");
			String zkHosts = manager.getSettings().getZkHosts();
			String serviceUrls = manager.getServiceUrls();
			String workerName = ServiceSettings.INSTANCE.getServiceName();
			// 如果有主从ZK，则当前为worker为这些主从ZK中的全局worker
			String otherZKHosts = manager.getSettings().getOtherZkHosts();
			boolean isGlobalWorker = !com.feinno.util.StringUtils.isNullOrEmpty(otherZKHosts);
			zkConnector = new AppEngineZookeeperConnector(zkHosts, workerName, serviceUrls, appBeanIds, isGlobalWorker);
			zkConnector.connect();

			// 向其他Site的ZK服务器注册worker信息
			if (!com.feinno.util.StringUtils.isNullOrEmpty(otherZKHosts)) {
				for (String zk : otherZKHosts.split("\\|")) {
					zkConnector = new AppEngineZookeeperConnector(zk, workerName, serviceUrls, appBeanIds, isGlobalWorker);
					zkConnector.connect();
				}
			}
			
			// 4 如果开启了debug，并且有debug端口，那么启动debug端口进行监听，等待远程IDE得注入，实现debug
			if (AppEngineManager.INSTANCE.getSettings().isDebugMode()) {
				LOGGER.info("Initialization debug serviec.");
				try {
					int debugPort = ServiceSettings.INSTANCE.getServicePort("debug");
					RpcTcpServerChannel channel = new RpcTcpServerChannel(debugPort);
					RpcDuplexServer server = new RpcDuplexServer(channel);
					server.registerService(AppBeanInjectorServiceImps.INSTANCE);
					channel.start();

				} catch (Exception e) {
					LOGGER.warn("Start debug service failed! {}", e);
				}
			}
		}
	}
	
	private void startNoZK() throws Exception
	{
		LOGGER.info("start nozk ....");
		//
		// 2.1. 读取ha.xml meta信息，依次加载AppBean		
		List<String> beans = ServiceSettings.INSTANCE.getStartAppBeans();
		
		if(beans == null || beans.size()==0)
			throw new IllegalAccessException("start nozk error,-beans without categroyname params.");
		
		String appbeansText = ServiceSettings.INSTANCE.getMetas().get("appbeans");
		
		byte[] buf = appbeansText.getBytes(Charset.forName("UTF-8"));
		String annotations = new String(buf);
		//泛型解析异常
		LOGGER.info("start nozk,appbeans:{}",appbeansText);
		Gson gson = new Gson();
		Appbeans appBeans = gson.fromJson(annotations, Appbeans.class);
		
		if(appBeans == null || appBeans.getAppBeans().size()==0)
			throw new IllegalAccessException("start nozk error,meta appbeans:" + appbeansText);
		int numAppLoaded = 0;
		StringBuffer loadApp = new StringBuffer("start nozk,loaded app:");
		for(AppBeanAnnotations an : appBeans.getAppBeans())
		{
			LOGGER.info("do with :{}",an.getCategoryMinusName());
			
			if(beans.size()==1 && beans.get(0).equals("all"))
			{
				LOGGER.info("load all,this one is:{}"+an.getCategoryMinusName());
				manager.loadAppBean(an);
				loadApp.append(an.getCategoryMinusName()).append(",");
				numAppLoaded++;
			}
			else
			{
				for(String app : beans)
				{
					if(app.equals(an.getCategoryMinusName()))
					{
						LOGGER.info("load appbenas,this one is:{}"+an.getCategoryMinusName());
						manager.loadAppBean(an);
						loadApp.append(an.getCategoryMinusName()).append(",");
						numAppLoaded++;
					}
						
				}
			}
		}
		LOGGER.info(loadApp.toString());

		if (numAppLoaded == 0) {
			// 
			// 如果未加载任何app, 8秒后自杀(不能马上自杀)
			LOGGER.error("no app loaded kill in 8 secs");
			dieSoft(SHUTDOWN_DELAY_NO_COMPONENT, 0);
			return;
		}
		
		//
		// 2.2. 启动Host, 打开监听端口
		LOGGER.info("initializing... start hosts");
		manager.bringupHosts();

	}
	
	/*
	 * @see com.feinno.ha.ServiceComponent#stop()
	 */
	/**
	 * {在这里补充功能说明}
	 */
	@Override
	public void stop()
	{
		LOGGER.info("Got stop command, engine is shutting down....");
		//
		// 推迟8分钟后，再stop
		dieSoft(SHUTDOWN_DELAY_STOP_COMMAND, 0);
		LOGGER.info("EXIT.");
	}

	private List<FAE_Application> filterApp(ConfigTable<Integer, FAE_Application> table)
	{
		List<FAE_Application> apps = new ArrayList<FAE_Application>();
		String realWorkerName;
		for (FAE_Application a : table.getValues()) {
			// 如果不属于本机启动，则放过

			LOGGER.info("got AppBean: {}-{}-{} ", new Object[] { a.getPackageId(), a.getAppCategory(), a.getAppName() });
			//TODO 新的如何验证？
//			realWorkerName = a.getPackageId() + a.getServerGroup() + a.getServiceDeployName();
//
//			if (!realWorkerName.equals(ServiceSettings.INSTANCE.getServiceName())) {
//				LOGGER.info("worker {} NotMatch skip App {}", realWorkerName, ServiceSettings.INSTANCE.getServiceName());
//				continue;
//			}
			
			
			String[] groups = ServiceSettings.INSTANCE.getServerGroups();
			boolean isInGroups = false;
			if(groups!=null)
				for(String group : groups)
				{
					if(group.equals(a.getServerGroup()))
						isInGroups = true;
				}
			if(!isInGroups || !ServiceSettings.INSTANCE.getServiceName().equals(a.getServiceDeployName()))
			{
				LOGGER.info("BeanId {} NotMatch skip App, ServerGroup-ServiceDeployName:{}", a.getBeanId(),a.getServerGroup()+"-"+a.getServiceDeployName());
				continue;
			}
			// 如果被禁用了, 放过
			LOGGER.info("BeanId: {} isEnable: {} " , a.getBeanId() , a.isEnabled());
			if (!a.isEnabled())
				continue;
			apps.add(a);
		}
		return apps;
	}

	private List<FAE_Application> loadApps(ConfigTable<Integer, FAE_Application> table) throws Exception
	{
		List<FAE_Application> apps = filterApp(table);
		for (FAE_Application a : apps) {
			LOGGER.info("try load AppBean: {}", a.getAppCategory(), a.getAppName());
			manager.loadAppBean(a);
		}
		return apps;
	}

// TODO
//	private synchronized void reloadApps()
//	{
//	}

	private void dieSoft(int delay, final int code)
	{
		scheduler.schedule(new Runnable() {
			@Override
			public void run()
			{
				quit(code);
			}
		}, delay, TimeUnit.MILLISECONDS);
	}

	private void quit(int code)
	{
		LOGGER.info("shutting down managers....");
		try {
			// die hard
			// manager.shutdownHosts();
			LOGGER.info("shutting down zk client....");
			if (zkConnector != null) {
				try {
					zkConnector.close();
				} catch (Exception e) {
					LOGGER.warn("zk close operation interrupted.", e);
				}
			}
			LOGGER.info("shutting down jvm.");
		} finally {
			System.exit(code);
		}
	}
}
