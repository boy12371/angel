/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-7-26
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.server;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.configuration.FAE_Application;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.appengine.runtime.AppEngineZookeeperConnector;
import com.feinno.appengine.testing.AppBeanInjectorServiceImps;
import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.ha.ServiceComponent;
import com.feinno.ha.ServiceSettings;
import com.feinno.ha.interfaces.center.HACenterDeploymentService;
import com.feinno.ha.interfaces.center.HAPackageInfo;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.rpc.duplex.RpcDuplexServer;
import com.feinno.util.ServiceEnviornment;
import com.feinno.util.StringUtils;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineServerComponent implements ServiceComponent
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppEngineServerComponent.class);
	private static final boolean isCheckCommonFile = false;
	
	private AppEngineManager manager;
	private AppEngineZookeeperConnector zkConnector;
	private ScheduledExecutorService scheduler;
	private RpcDuplexClient client;
	private String[] groups;
	private String downloadPath = "./downloads/";
	private String packagePath = "./packages/";
	private String libPath = "./lib/";
	private String commonPath = "./common/";
	private Hashtable<Integer,Integer> beanPackage = new Hashtable<Integer,Integer>();
	private String haDesktopUrl;
	
	
	private static final int SHUTDOWN_DELAY_NO_COMPONENT = 8 * 1000;
	private static final int SHUTDOWN_DELAY_STOP_COMMAND = 8 * 60 * 1000;
	
	@Override
	public void start() throws Exception
	{
		ConfigurationManager.loadProperties("HADesktop.properties", null, new ConfigUpdateAction<Properties>(){

			@Override
			public void run(Properties e) throws Exception {
				haDesktopUrl = e.getProperty("host");
			}
		});
		
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
		// 2.2. 启动Host, 打开监听端口
		LOGGER.info("initializing... start hosts");
		manager.bringupHosts();
		
		//注册回调接口
		client = ServiceSettings.INSTANCE.getWorkerDuplexClient();
    	client.registerCallbackService(new AppEngineCallbackService() {
				public void refresh() {
					// TODO 如何判断哪些App需要更新？
					HACenterDeploymentService service = client.getService(HACenterDeploymentService.class);
					groups = service.getServerGroups(ServiceEnviornment.getComputerName());
					ConfigTable<Integer, FAE_Application> apps;
					try {
						apps = ConfigurationManager.loadTable(Integer.class, FAE_Application.class, "FAE_Application", null);
						LOGGER.info("refresh... load apps");
						loadApps(apps);
					} catch (ConfigurationException e) {
						LOGGER.error("refresh error:{}",e);
					} catch (Exception e) {
						LOGGER.error("refresh error:{}",e);
					}					
				}
			});
    	
    	//	
		
		// 3.1. 连接ZooKeeper注册自己
		LOGGER.info("initializing... create zookeeper connector");
		String zkHosts = manager.getSettings().getZkHosts();
		String serviceUrls = manager.getServiceUrls();
		String workerName = ServiceSettings.INSTANCE.getServiceName();
				
		zkConnector = new AppEngineZookeeperConnector(zkHosts, workerName, serviceUrls);
		zkConnector.connect();
		
    	// 2.1. 读取FAE_Application表，依次加载AppBean
		HACenterDeploymentService service = client.getService(HACenterDeploymentService.class);
		groups = service.getServerGroups(ServiceEnviornment.getComputerName());
		ConfigTable<Integer, FAE_Application> apps = ConfigurationManager.loadTable(Integer.class, FAE_Application.class, "FAE_Application", null);

		LOGGER.info("initializing... load apps");
		int numAppLoaded = loadApps(apps);
		

		
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
	
	synchronized private int loadApps(ConfigTable<Integer, FAE_Application> table) throws Exception
	{
		Collection<FAE_Application> apps = table.getValues();
		HACenterDeploymentService service = client.getService(HACenterDeploymentService.class);
		Map<String,String> libFileMd5 = FileHelper.getDirMD5(new File(libPath), false);
		Map<String,String> commonFileMd5 = FileHelper.getDirMD5(new File(commonPath), false);
		for (FAE_Application app : apps) {
			try
			{
				if(!app.isEnabled())
				{
					LOGGER.warn("app.isEnabled()=false;skip AppBean: {}-{}", app.getAppCategory(), app.getAppName());
					continue;
				}
				String group = app.getServerGroup();
				//if (contains(groups, group) && !StringUtils.isNullOrEmpty(app.getHotServerName()) ) {
				if (contains(groups, group) && ServiceSettings.INSTANCE.getServiceName().equals(app.getHotServerName()) ) {
					
					//refresh的时候，如果已经加载过这个bean，并且packageId没变的话，则不需要重新加载
					if(beanPackage.containsKey(app.getBeanId()) &&  (beanPackage.get(app.getBeanId()) == app.getPackageId()) )
						continue;					
					 
					// TODO Start
					HAPackageInfo pack = service.getPackageInfo(app.getPackageId());
					LOGGER.info("try download package: {}", haDesktopUrl+pack.getPackageUrl());
					//downladPackage函数内部判断是否需要下载
					String packPath = downladPackage(pack);		
					//检查lib/common文件夹下的文件是否完全相同
					if(isCheckCommonFile)
					{
						LOGGER.info("try chech package's lib files and common files:{}", pack.getPackageUrl());
						Map<String,String> libFileMd5_me = FileHelper.getDirMD5(new File(packPath+"/lib"), false);
						Map<String,String> commonFileMd5_me = FileHelper.getDirMD5(new File(packPath+"/common"), false);
						if(!CompareFiles(libFileMd5,libFileMd5_me))
							throw new Exception("lib files not as same as package:"+ packPath);
						if(!CompareFiles(commonFileMd5,commonFileMd5_me))
							throw new Exception("common files not as same as package:"+ packPath);			
					}
					
					LOGGER.info("try load AppBean: {}-{}", app.getAppCategory(), app.getAppName());
					//List<URL> urlImps = FileHelper.getURL(new File(packPath + "/imps"),false);
					List<URL> urlWorker = FileHelper.getURL(new File(packPath + "/worker"),false);
					//urlWorker.addAll()
					URL[] allUrls = new URL[urlWorker.size()];
					urlWorker.toArray(allUrls);
					manager.loadAppBeanDynamic(app,allUrls);
					beanPackage.put(app.getBeanId(), app.getPackageId());
				}
			}catch(Exception ex)
			{
				LOGGER.error(String.format("load app failed,categoryname:%s, Exception:%s",app.getAppCategory()+"-"+app.getAppName(),ex),ex);
			}
			
		}
		List<Integer> removeId = new ArrayList<Integer>();
		//清理beanPackage中的垃圾数据
		for(Integer beanId : beanPackage.keySet())
		{
			boolean isInApps = false;
			for(FAE_Application app : apps)
				if(beanId.intValue() == app.getBeanId())
					isInApps = true;
			if(!isInApps)
				removeId.add(beanId);
		}
		for(Integer id : removeId)
			beanPackage.remove(id);
		
		List<Integer> beanIds = new ArrayList<Integer>();
		beanIds.addAll(beanPackage.keySet());
		zkConnector.setDynamicApp2ZK(ServiceSettings.INSTANCE.getServiceName(), manager.getServiceUrls(), beanIds);
		LOGGER.info("load app size:{}",beanPackage.size());
		return beanPackage.size();
	}
	

	private boolean CompareFiles(Map<String, String> source,
			Map<String, String> dest) {
		if(source == null || dest == null)
			return false;
		if(source.size()!=dest.size())
			return false;
		for(String key : source.keySet())
		{
			if(!source.get(key).equals(dest.get(key)))
				return false;
		}
		return true;
	}

	private String downladPackage(HAPackageInfo pack) throws Exception {
		//./downloads
		File dirDown = new File(downloadPath);
		if (!dirDown.isDirectory())
		{ //目录不存在
			dirDown.mkdir(); //创建目录
		} 
		//./packages
		File dirPackage = new File(packagePath);
		if (!dirPackage.isDirectory())
		{ //目录不存在
			dirPackage.mkdir(); //创建目录
		} 
		//
		String urlStr = haDesktopUrl+pack.getPackageUrl();
		URL url = new URL(urlStr);
		String fileName = url.getFile().substring(url.getFile().lastIndexOf("name=")+5);
		File downFile = new File(downloadPath+fileName);
		if(!downFile.exists())
		{
		     LOGGER.info("正在获取链接[" + urlStr + "]的内容.../n将其保存为文件[" +downFile.getCanonicalPath() + "]");
			//下载到downloads
			FileHelper.saveToFile(url,downloadPath);
		}
		else
			LOGGER.info("正在获取链接[" + urlStr + "]的内容已经下载过，不再下载");
		File targetDir = new File(packagePath+fileName);
		if(!targetDir.isDirectory())
		{
			LOGGER.info("正在解压缩文件[" + downFile.getCanonicalPath() + "]的内容到："+targetDir.getCanonicalPath());
			//解压缩到packages
			FileHelper.deGzipArchive(targetDir.getCanonicalPath(), downFile.getCanonicalPath());
		}
		
		return targetDir.getCanonicalPath();
	}


	private boolean contains(String[] groups, String group) {
		if(groups == null || groups.length == 0)
			return false;
		for(String str : groups)
			if(str.equals(group))
				return true;
		return false;
	}

}
