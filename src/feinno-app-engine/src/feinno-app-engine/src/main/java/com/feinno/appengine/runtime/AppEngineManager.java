/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-9
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.runtime;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;

import org.antlr.grammar.v3.ANTLRv3Parser.treeSpec_return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.AppBeanDescriptor;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.configuration.AppBeanAnnotationsLoader;
import com.feinno.appengine.configuration.AppEngineSettings;
import com.feinno.appengine.configuration.CFG_Site;
import com.feinno.appengine.configuration.FAE_Application;
import com.feinno.appengine.http.HttpAppBean;
import com.feinno.appengine.http.HttpAppBeanDescriptor;
import com.feinno.appengine.job.JobAppBean;
import com.feinno.appengine.job.JobAppBeanDescriptor;
import com.feinno.appengine.job.JobResourceLocator;
import com.feinno.appengine.resource.ResourceFactory;
import com.feinno.appengine.resource.ResourceLocator;
import com.feinno.appengine.rpc.RemoteAppBean;
import com.feinno.appengine.rpc.RemoteAppBeanDescriptor;
import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.ha.ServiceSettings;
import com.feinno.initialization.InitialUtil;
import com.feinno.rpc.channel.inproc.RpcInprocServerChannel;
import com.feinno.rpc.server.RpcServiceBootstrap;
import com.feinno.threading.ExecutorFactory;
import com.feinno.util.PropertiesUtil;

/**
 * AppEngine的管理类
 * 
 * 管理如下内容
 * 
 * - 支持不同AppBean的AppHost - sites (为了跨IDC的应用) - locator (可以迁移出去)
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineManager
{
	public static final String DEFAULT_EXECUTOR_NAME = "apps";
	public static final AppEngineManager INSTANCE = new AppEngineManager();
	public static final Logger LOGGER = LoggerFactory.getLogger(AppEngineManager.class);

	private Map<String, Class<? extends ResourceLocator>> locatorTypes;
	private Map<String, AppHost> hosts;
	
	private Map<String, AppBeanDescriptor> descriptors;

	private String currentSite;
	private Map<String, CFG_Site> sites;
	private AppEngineSettings settings;

	private List<String> serviceUrls = new ArrayList<String>();

	private AppEngineManager()
	{
		descriptors = new Hashtable<String, AppBeanDescriptor>();
		locatorTypes = new Hashtable<String, Class<? extends ResourceLocator>>();
		hosts = new Hashtable<String, AppHost>();		
	}

	/**
	 * 
	 * 启动AppEngineManager的所有基础支持
	 * 
	 * 1.1. 读取appengine.properties配置(HA服务外壳已经启动) 1.2.
	 * 启动AppEngine核心部分，并加载extension 1.3. extension负责加载扩展的AppBeanHost,
	 * 以及ResourceLoader 1.4. 加载ResourceFactory
	 * 
	 * 当初始化完成时，AppEngineManager可以进行AE及Extension中支持的AppBean的加载与hosting
	 * 
	 * @throws Throwable
	 */
	public void initialize() throws Exception
	{
		//
		// 读取配置
		this.settings = new AppEngineSettings();
		Properties props = ConfigurationManager.loadProperties("appengine.properties", null, null);
		PropertiesUtil.fillBean(props, settings);
		settings.setUseXenon(settings.getUseXenon());
		
		
		RpcServiceBootstrap.registerChannel(RpcInprocServerChannel.INSTANCE);

		//
		// 注册基本的AppBean类型	
		descriptors.put(RemoteAppBean.class.getName(), RemoteAppBeanDescriptor.INSTANCE);
		descriptors.put(HttpAppBean.class.getName(), HttpAppBeanDescriptor.INSTANCE);
		descriptors.put(JobAppBean.class.getName(), JobAppBeanDescriptor.INSTANCE);

		//
		// 加载Sites
		currentSite = ServiceSettings.INSTANCE.getSiteName();
		ConfigurationManager.loadTable(String.class, CFG_Site.class, "CFG_Site",
				new ConfigUpdateAction<ConfigTable<String, CFG_Site>>() {
					@Override
					public void run(ConfigTable<String, CFG_Site> a) throws Exception
					{
						Hashtable<String, CFG_Site> tbl = new Hashtable<String, CFG_Site>();
						for (CFG_Site s : a.getValues()) {
							tbl.put(s.getName(), s);
						}
						sites = tbl;
					}
				});

		//
		// 初始化extension
		if (settings.getExtension() != null) {
			Class<?> clazz = Class.forName(settings.getExtension());
			AppEngineExtension ext = (AppEngineExtension) clazz.newInstance();
			ext.setup(this);
		}
//		registerLocatorType("PoolLocator", PoolLocator.class);
//		registerLocatorType("GlobalLocator", GlobalLocator.class);
//		registerLocatorType("JobResourceLocator", JobResourceLocator.class);
		//
		// 初始化资源配置表
		InitialUtil.init(ResourceFactory.class);
	}

	public AppEngineSettings getSettings()
	{
		return settings;
	}

	/**
	 * 
	 * 注册一个Host类型
	 * @param beanType
	 * @param hostClazz
	 */
	public void registerBeanType(Class<? extends AppBean> beanType, AppBeanDescriptor descriptor)
	{
		LOGGER.warn("AppEngine add beanType {} - {}", beanType, descriptor);
		descriptors.put(beanType.getName(), descriptor);
		try {
			getHost(beanType.getName());
		} catch (Exception e) {
			LOGGER.error("getHost error:{}",e);
		}
	}
	
	public AppBeanDescriptor getDescriptor(String beanTypeName)
	{
		return descriptors.get(beanTypeName);
	}
	
	/**
	 * 
	 * 注册一个ResourceLocator
	 * 
	 * @param locatorName
	 * @param locatorType
	 */
	public void registerLocatorType(String locatorName, Class<? extends ResourceLocator> locatorType)
	{
		LOGGER.warn("AppEngine register LocatorType {} - {}", locatorName, locatorType);
		locatorTypes.put(locatorName, locatorType);
	}

	/**
	 * 
	 * 获取一个Locator
	 * 
	 * @param locatorName
	 * @return
	 */
	public ResourceLocator getLocator(String locatorName)
	{
		try {
			@SuppressWarnings("unchecked")
			Class<ResourceLocator> clazz = (Class<ResourceLocator>) locatorTypes.get(locatorName);
			ResourceLocator locator = clazz.newInstance();
			if (locator == null) {
				throw new IllegalArgumentException("Locator not found:" + locatorName);
			} else {
				return locator;
			}
		} catch (Exception e) {
			LOGGER.error("Locator not found:" + locatorName, e);
			throw new IllegalArgumentException("Locator create failed:" + locatorName, e);
		}
	}

	/**
	 * 
	 * 找到一个支持类型的AppHost, 如果找不到, 就按照类型创建一个
	 * 所有的应用必须在启动host之前先加载，否则有可能其对应的host不会被启动。
	 * @param type
	 * @return
	 * @throws Exception 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AppHost getHost(String beanType) throws Exception
	{
		//
		// 找到已经加载的host
		AppHost host = hosts.get(beanType);

		if (host == null) {
			AppBeanDescriptor descriptor = descriptors.get(beanType);
			if (descriptor != null) {
				host = descriptor.createHost();
				hosts.put(beanType , host);
				LOGGER.info("AppHost of type [{}] Created", beanType);
			}
		}
		return host;
	}
	
	public AppHost getHost(Class<?> beanClazz) throws Exception
	{
		return getHost(beanClazz.getName());
	}

	public Map<String, AppHost> getHosts()
	{
		return hosts;
	}

	public ResourceLocator getResourceLocator(String locatorName)
	{
		throw new UnsupportedOperationException("还没实现");
	}

	/**
	 * 
	 * 通过全局配置表加载一个AppBean
	 * @param app
	 * @throws Exception
	 */
	public void loadAppBean(FAE_Application app) throws Exception
	{
		String appType = app.getAnnotations().getClassInfo().getBaseClass().getType();
		LOGGER.debug(">>> appType is {}", appType);
		AppHost host = getHost(appType);
		if (host == null) {
			throw new RuntimeException("can NOT get AppHost for type[" + appType + "]");
		}
		LOGGER.debug(">>> host is {}", host);
		
		AppBeanAnnotations annos = app.getAnnotations();
		host.loadAppBean(annos);
	}
	
	/**
	 * 
	 * 通过ha.xml metas 加载一个AppBean
	 * @param app
	 * @throws Exception
	 */
	public void loadAppBean(AppBeanAnnotations annos) throws Exception
	{
		String appType = annos.getClassInfo().getBaseClass().getType();
		LOGGER.debug(">>> appType is {}", appType);
		AppHost host = getHost(appType);
		if (host == null) {
			throw new RuntimeException("can NOT get AppHost for type[" + appType + "]");
		}
		LOGGER.debug(">>> host is {}", host);
		
		host.loadAppBean(annos);
	}
	
	public void loadAppBeanDynamic(FAE_Application app, URL[] urls) throws Exception {
		String appType = app.getAnnotations().getClassInfo().getBaseClass().getType();
		LOGGER.debug(">>> loadAppBeanDynamic appType is {},categoryname is {}", appType,app.getAppCategory()+"-"+app.getAppName());
		AppHost host = getHost(appType);
		if (host == null) {
			throw new RuntimeException("can NOT get AppHost for type[" + appType + "]");
		}
		LOGGER.debug(">>>loadAppBeanDynamic host is {}", host);
		
		AppBeanAnnotations annos = app.getAnnotations();
		host.loadAppBeanDynamic(annos,urls);		
	}
	
	/**
	 * 
	 * 通过类型加载一个AppBean
	 * @param <E>
	 * @param clazz
	 * @throws Throwable 
	 */
	@SuppressWarnings("unchecked")
	public <E extends AppBean> E loadAppBean(Class<E> clazz) throws Exception
	{
		AppBeanAnnotations annos = AppBeanAnnotationsLoader.getAppBeanAnnotaions(clazz);
		annos.getClassInfo().setVersion(ServiceSettings.INSTANCE.getVersion());
		String beanType = annos.getClassInfo().getBaseClass().getType();
		AppHost host = getHost(beanType);
		
		if (host == null) {
			throw new RuntimeException("Can NOT get AppHost for type[" + beanType + "]");
		}
		LOGGER.debug(">>> host is {},load app is {}", host, annos.getAppCategory() + "-" + annos.getAppName());
		E e = (E) host.getBean(annos.getAppCategory() + "-" + annos.getAppName());
		return e != null ? e : (E) host.loadAppBean(annos, clazz);
	}

	public void unloadApp(String catMinusName)
	{
		for (AppHost host : hosts.values()) {
			host.unload(catMinusName);
		}
	}

	/**
	 * 
	 * @param stopOnError
	 *            false : 尽量启动所有host; true : 遇到任何错误直接返回
	 * @return <p>
	 *         false, (stopOnError == true && 任意一个host启动失败) || (stopOnError ==
	 *         false && 没能成功启动任何host)
	 *         <p>
	 *         true, 其他情形
	 * @throws Throwable 
	 */
	public void bringupHosts() throws Exception
	{
		Set<Map.Entry<String, AppHost>> entries = hosts.entrySet();
		for (Map.Entry<String, AppHost> entry : entries) {
			String type = entry.getKey();
			AppHost host = entry.getValue();
			LOGGER.info("bringing up host [type : {}]", type);
			host.start();
			serviceUrls.add(host.getServiceUrl());		
		}
	}

	public void shutdownHosts()
	{
		Set<Map.Entry<String, AppHost>> entries = hosts.entrySet();
		for (Map.Entry<String, AppHost> entry : entries) {
			AppHost host = entry.getValue();
			String type = entry.getKey();
			LOGGER.info("shutting down host [type : {}]", type);
			try {
				host.stop();
			} catch (Exception t) {
				LOGGER.info("host shutdown failed [{}]", host);
			}
		}
	}

	public String getServiceUrls()
	{
		StringBuilder sb = new StringBuilder();
		for (String url : serviceUrls) {
			if (url != null) {
				sb.append(url).append(";");
			}
		}
		try {
			if (settings.isDebugMode()) {
				int debugPort = ServiceSettings.INSTANCE.getServicePort("debug");
				sb.append(String.format("debug=tcp://%s:%d", ServiceSettings.INSTANCE.getServerAddress(), debugPort));
				sb.append(";");
			}
		}catch (Exception e) {
			LOGGER.warn("Not found debug port.");
		}
		return sb.toString();
	}

	public CFG_Site getSite(String siteName)
	{
		CFG_Site site = sites.get(siteName);
		if (site == null)
			throw new IllegalArgumentException("unknown site:" + siteName);

		return site;
	}

	/**
	 * 
	 * 当前所在Site
	 * @return
	 */
	public String currentSite()
	{
		return currentSite;
	}

	/**
	 * 
	 * 获取所有的Site
	 * @return
	 */
	public Collection<CFG_Site> getSites()
	{
		return sites.values();
	}

	/**
	 * 
	 * 获取默认的线程池
	 * @return
	 */
	public Executor getDefaultExecutor()
	{
		synchronized (this) {
			Executor r = ExecutorFactory.getExecutor(DEFAULT_EXECUTOR_NAME);
			if (r == null) {
				r = ExecutorFactory.newFixedExecutor(DEFAULT_EXECUTOR_NAME, settings.getWorkerThreads(), settings.getRequestQueueSize());
			}
			return r;
		}
	}
}
