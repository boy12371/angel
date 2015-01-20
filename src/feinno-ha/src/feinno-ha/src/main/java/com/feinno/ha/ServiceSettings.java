/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-26
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.ha;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.spi.LocalConfigurator;
import com.feinno.ha.StartupOptionEnum.StartupOptionException;
import com.feinno.ha.database.HADatabaseFactory;
import com.feinno.ha.interfaces.center.HACenterDeploymentService;
import com.feinno.ha.interfaces.center.HAPackageInfo;
import com.feinno.ha.util.HAConfigUtils;
import com.feinno.rpc.RpcEndpointFactory;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.rpc.server.builtin.RpcPingResults;
import com.feinno.util.ConfigBean;
import com.feinno.util.ConfigHelper;
import com.feinno.util.PropertiesUtil;
import com.feinno.util.ServiceEnviornment;
import com.feinno.util.StringUtils;

/**
 * 当前服务的配置Properties及相关环境变量, 配置包含如下部分<br>
 * <p>
 * <li>1. 来自本目录下的ha.xml, 包含serviceName, serviceComponent...</li>
 * <li>2. 来自HAMaster的启动参数,通过getOpt获得</li>
 * <li>3. 来自系统环境变量</li>
 * <li>3. 来自HA_Deployment下的</li>
 * </p>
 * 
 * 1. Controllable (-ha 8090) <br>
 * 2. TextConfigLoader (default: local, -config HADB.properties -config agent) <br>
 * 3. HAConfigLoader (default -config ) <br>
 * 4. HAWorkerAgent (-agent) <br>
 * 5. HAServiceClassName <br>
 * 
 * - 外壳注册启动调用 gen -ha 8090 -agent<br>
 * 
 * 调试启动 gen -agent 192.168.1.100:8080<br>
 * 
 * 本地非Agent调试，使用数据库配置启动 gen -config config.properties<br>
 * 
 * 本地启动方式 gen -ports rpc=8080;http=8005;monitor=8007 -config config.properties<br>
 * 
 * 其他参数 gen -serverName SERVER-01 伪造服务器名 gen -serviceName IBS 伪造WorkerId<br>
 * 
 * 静态启动方式 -ports "rpc=9001" -nozk -beans all 或者 -beans core-RegisterSipcApp
 * contact-AddBuddyListSipcApp
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ServiceSettings {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceSettings.class);
	public static final ServiceSettings INSTANCE = new ServiceSettings();

	/** 服务名称,从ha.xml中获取 */
	private String serviceName;

	/** ha-center的url */
	private String centerUrl;
	
	/** 服务版本号，从ha.xml中获取 */
	private String version;

	/** 启动入口,从ha.xml中获取 */
	private String workerComponent;

	/** 服务端口及相应端口号,从启动参数中获取 */
	private String servicePorts;

	/** 服务端口及相应端口号,存储于Map中便于查询 */
	private Map<String, Integer> servicePortsMap;

	/** 元数据,从ha.xml中获取 */
	private Map<String, String> metas;

	/** 部署包ID */
	private int packageId = -1;

	/** 部署包信息 */
	private HAPackageInfo packageInfo = null;

	/** 服务器组 */
	private String[] serverGroups = null;

	private String siteName;
	private String serverAddress;

	/**
	 * 
	 * 初始化开发环境,用于单元测试等场合<br>
	 */
	public static void init() throws IOException {
		ServiceSettings.init("HA.properties");
	}

	/**
	 * 初始化ServiceSetting,设置HADB.properties
	 * 
	 * @param serviceName
	 * @throws IOException
	 */
	public static void init(String configFile) throws IOException {
		HAProperties props = ConfigHelper.loadConfigBean(configFile, HAProperties.class);

		String dbConfigFile = props.getConfigDatabase();
		try {
			ConfigHelper.loadProperties(dbConfigFile); // test config File
		} catch (Exception ex) {
			throw new IllegalArgumentException("configDatabase verfiy failed, please check config", ex);
		}

		HADatabaseFactory.setHADBProperties(dbConfigFile);
		LocalConfigurator configurator = new LocalConfigurator();
		ConfigurationManager.setConfigurator(configurator);

		ServiceSettings settings = ServiceSettings.INSTANCE;

		settings.serviceName = props.getServiceName();
		String siteName = props.getSiteName();
		if (StringUtils.isNullOrEmpty(siteName)) {
			throw new IllegalArgumentException("null SiteName, please check your ha.properties");
		}
		settings.siteName = siteName;

		for (String key : props.getServicePorts().keySet()) {
			String v = props.getServicePorts().get(key);
			settings.servicePortsMap.put(key, Integer.valueOf(v));
			LOGGER.info("putServicePort {}={}", key, v);
		}
	}

	/**
	 * 私有的构造方法保证了该类对象的单例性
	 */
	private ServiceSettings() {
		servicePortsMap = new Hashtable<String, Integer>();
		metas = new Hashtable<String, String>();
		serviceName = getServiceName();
	}

	/**
	 * 加载ha.xml
	 * 
	 * @throws FileNotFoundException
	 */
	public void loadFromXml() throws FileNotFoundException {
		String path = "ha.xml";
		LOGGER.info("load HA info.");
		Properties props = PropertiesUtil.xmlToProperties(new FileInputStream(path));
		ConfigBean configBean = ConfigBean.valueOf(props, ConfigBean.class);
		// serviceName由package.info中的ServiceDeployName来指定
		// serviceName = configBean.getChild("worker").getFieldValue("name");
		workerComponent = configBean.getChild("worker").getFieldValue("serviceComponent");
		version =  configBean.getChild("worker").getFieldValue("version");
		LOGGER.info("load servicePorts.");
		servicePorts = configBean.getChild("worker").getFieldValue("servicePorts");
		if (servicePorts != null && servicePorts.trim().length() > 0) {
			for (String port : servicePorts.split(";")) {// 向端口表中存放端口类型,用于占位
				servicePortsMap.put(port, -1);
			}
		}
		LOGGER.info("load metas.");
		if (configBean.getChild("metas") != null && configBean.getChild("metas").getChild("meta") != null) {
			for (String key : configBean.getChild("metas").getChild("meta").childKeySet()) {
				String body = configBean.getChild("metas").getChild("meta").getFieldValue(key);
				if (body != null) {
					metas.put(key, body);
				}
			}
		}
	}

	/**
	 * 分配与装载启动参数
	 * 
	 * @param args
	 */
	public void assignOpts(String[] args) {
		LOGGER.info("Read startup option : {}", Arrays.toString(args));
		StartupOptionEnum currentOption = null;
		for (int i = 0; i < args.length; i++) {
			String argsStr = args[i];
			if (argsStr.startsWith("-")) {
				// 如果当前是一个启动项，那么从启动项的枚举列表中找到当前的项，设置为开启
				String optionName = argsStr.substring(1, argsStr.length()).toUpperCase();
				try {
					currentOption = StartupOptionEnum.valueOf(optionName.trim());
					if (currentOption == null) {
						throw new RuntimeException(String.format("Startup options %s Error.", argsStr));
					}
					currentOption.setEnable(true);
				} catch (Exception e) {
					LOGGER.warn(String.format("Not found startup option [%s] . So ignore.", optionName.trim()), e);
				}
			} else {
				// 如果是一个启动参数，且当前的启动项为空，那么需要报告错误，该参数找不到对应的启动项
				if (currentOption == null) {
					throw new RuntimeException(String.format("Startup options %s Error.", argsStr));
				}
				currentOption.addArgs(argsStr);
			}
		}
	}

	/**
	 * 进行配置的完整性验证
	 * 
	 * @return
	 */
	public void verifyConfig() throws StartupOptionException {

		// 1. 验证全部启动参数的格式
		LOGGER.info("Verify startup config ");
		for (StartupOptionEnum option : StartupOptionEnum.values()) {
			// 如果某个操作箱验证失败，会抛出一个StartupOptionException，以告知失败原因
			option.verifyArgs();
		}

		// 2. 提取并解析重要的参数
		if (StartupOptionEnum.PORTS.isEnable()) {
			if (StartupOptionEnum.PORTS.getArgs().size() > 0) {
				String[] ports = StartupOptionEnum.PORTS.getArgs().get(0).split(";");
				for (String port : ports) {
					String[] args = port.split("=");
					if (servicePortsMap.get(args[0]) != null) {
						// 顺便验证端口字段是不是一个正确的数字
						try {
							servicePortsMap.put(args[0], Integer.valueOf(args[1]));
						} catch (Exception e) {
							throw new StartupOptionException(
									"Startup options -agent error,  For example '-ports rpc=8088;http=7022;monitor=8088;'.");
						}
					}
				}
				servicePorts = StartupOptionEnum.PORTS.getArgs().get(0);
			}
		}
	}

	/**
	 * 获得启动时的选项参数
	 * 
	 * @param opt
	 * @return
	 */
	public List<String> getOpts(String opt) {
		StartupOptionEnum option = StartupOptionEnum.valueOf(opt.trim().toUpperCase());
		return option.getArgs();
	}

	/**
	 * 获得启动时的选项参数
	 * 
	 * @param opt
	 * @param params
	 * @return
	 */
	public String getOpt(String opt, int params) {
		return getOpts(opt).get(params);
	}

	/**
	 * 获得入口类全名称，该名称从ha.xml中获取
	 * 
	 * @return
	 */
	public String getWorkerComponent() {
		return workerComponent;
	}
	
	/**
	 * 获得当前服务版本号
	 * @return
	 */
	public String getVersion(){
		return version;
	}

	/**
	 * 根据-agent启动选项，返回对应的Center的RpcEndpoint，如未开启该选项，则返回null
	 * 
	 * @return
	 */
	public RpcEndpoint getHACenterEp() {
		if (StartupOptionEnum.AGENT.isEnable()) {
			String centerUrl = getCenterUrl();
			RpcEndpoint ep = RpcEndpointFactory.parse(centerUrl);
			return ep;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 获取HACenter服务器地址
	 * 
	 * @return
	 */
	public String getCenterUrl() {
		if (centerUrl == null && StartupOptionEnum.AGENT.isEnable()) {
			if (StartupOptionEnum.AGENT.getArgs().size() > 0) {
				centerUrl = StartupOptionEnum.AGENT.getArgs().get(0);
			} else {
				centerUrl = HAConfigUtils.getHAEnv("HA_CENTER_URL");
			}
		}
		return centerUrl;
	}

	/**
	 * 根据-ha启动选项，返回对应的HAMaster的RpcEndpoint，如未开启该选项，则返回null
	 * 
	 * @return
	 */
	public RpcEndpoint getControllerEp() {
		if (StartupOptionEnum.HA.isEnable()) {
			String controllerProt = null;
			// 取启动参数中的masterd地址，如果未加地址默认连接环境变量HA_CONTROLLER_PORT
			if (StartupOptionEnum.HA.getArgs().size() > 0) {
				controllerProt = StartupOptionEnum.HA.getArgs().get(0);
			} else {
				controllerProt = HAConfigUtils.getHAEnv("HA_CONTROLLER_PORT");
			}
			RpcEndpoint ep = RpcEndpointFactory.parse("tcp://127.0.0.1:" + controllerProt);
			return ep;
		} else {
			return null;
		}
	}

	/**
	 * 根据启动参数判断是否注册ZK信息，是否启用AppBeanRouteManager信息
	 * 
	 * @return
	 */
	public boolean isNoZK() {
		if (StartupOptionEnum.NOZK.isEnable())
			return true;
		else
			return false;
	}

	/**
	 * 根据启动参数获取静态启动的AppBeans的categary-name,如果启动参数没有，则返回null
	 * 
	 * @return
	 */
	public List<String> getStartAppBeans() {
		if (StartupOptionEnum.BEANS.isEnable())
			return StartupOptionEnum.BEANS.getArgs();
		else
			return null;
	}

	public Map<String, String> getMetas() {
		return metas;
	}

	/**
	 * 获得从ha.xml中获取的服务名称
	 * 
	 * @return
	 */
	public String getServiceName() {
		if (serviceName != null && serviceName.length() > 0) {
			return serviceName;
		} else {
			Properties properties = new Properties();
			try {
				File packageInfo = new File("package.info");
				if (packageInfo.exists()) {
					properties.load(new FileInputStream(packageInfo));
					serviceName = properties.getProperty("serviceDeployName");
					RpcPingResults.serviceName = serviceName;
				} else {
					InputStream inputStream = ServiceSettings.class.getResourceAsStream("/package.info");
					if (inputStream != null) {
						properties.load(inputStream);
						serviceName = properties.getProperty("serviceDeployName");
						RpcPingResults.serviceName = serviceName;
					}

				}
			} catch (Exception e) {
				serviceName = null;
				LOGGER.error("load package.info [ServiceDeployName] failed.", e);
			}
		}
		serviceName = serviceName != null ? serviceName : ServiceEnviornment.getServiceName() + "_UNKNOW";
		return serviceName;

	}

	/**
	 * 获得启动参数传入的端口号
	 * 
	 * @return
	 */
	public String getServicePorts() {
		return servicePorts;
	}

	/**
	 * 获得启动参数传入的某一个类型的端口号
	 * 
	 * @param name
	 * @return
	 */
	public int getServicePort(String name) {
		try {
			return Integer.valueOf(servicePortsMap.get(name));
		} catch (Exception e) {
			throw new RuntimeException("getServicePort is failed. name = " + name + " ", e);
		}
	}

	private RpcDuplexClient workerDuplexClient;
	private RpcDuplexClient controlleeDuplexClient;

	public RpcDuplexClient getWorkerDuplexClient() {
		return workerDuplexClient;
	}

	public RpcDuplexClient getControlleeDuplexClient() {
		return controlleeDuplexClient;
	}

	void setWorkerDuplexClient(RpcDuplexClient client) {
		this.workerDuplexClient = client;
	}

	void setControlleeDuplexClient(RpcDuplexClient client) {
		this.controlleeDuplexClient = client;
	}

	/**
	 * 
	 * 获取本机对外提供的服务地址
	 * 
	 * @return
	 */
	public String getServerAddress() {
		if (serverAddress == null) {
			try {
				serverAddress = HAConfigUtils.getHAEnv("HA_SERVER_ADDRESS");
			} catch (Exception ex) {
				LOGGER.error("no env config serverAddress assume to 127.0.0.1");
				serverAddress = "127.0.0.1";
			}
		}
		return serverAddress;
	}

	/**
	 * 
	 * 获取本机的Site名称
	 * 
	 * @return
	 */
	public String getSiteName() {
		if (siteName == null) {
			try {
				siteName = HAConfigUtils.getHAEnv("HA_SITE");
			} catch (Exception ex) {
				LOGGER.error("can't find siteName assume empty.");
				siteName = "";
			}
		}
		return siteName;
	}

	/**
	 * 
	 * 从本地文件package.info取
	 * 
	 * @return
	 */
	public int getPackageId() {
		if (packageId > 0) {
			return packageId;
		} else {
			try {
				Properties properties = new Properties();
				properties.load(new FileInputStream("package.info"));
				packageId = Integer.valueOf(properties.getProperty("packageId"));
			} catch (Exception e) {
				packageId = -1;
				LOGGER.error("load package.info [packageId] failed.", e);
			}
		}
		return packageId;
	}

	/**
	 * 
	 * 通过Rpc接口从Center取
	 * 
	 * @return
	 */
	public synchronized HAPackageInfo getPackageInfo() {
		if (packageInfo != null) {
			return packageInfo;
		}
		if (workerDuplexClient != null) {
			HACenterDeploymentService centerDeploymentService = workerDuplexClient
					.getService(HACenterDeploymentService.class);
			packageInfo = centerDeploymentService.getPackageInfo(this.getPackageId());
			return packageInfo;
		} else {
			throw new RuntimeException(String.format(
					"workerDuplexClient is null,so invoke getPackageInfo failed , params = %s ", this.getPackageId()));
		}
	}

	/**
	 * 
	 * 是否在服务器组中
	 * 
	 * @param groupName
	 */
	public boolean isInServerGroup(String groupName) {
		String[] serverGroups = this.getServerGroups();
		if (serverGroups != null) {
			for (String groupNameTemp : serverGroups) {
				if (groupName.equals(groupNameTemp)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * 获取所属的服务器组
	 * 
	 * @return
	 */
	public synchronized String[] getServerGroups() {
		if (serverGroups != null) {
			return serverGroups;
		}
		if (workerDuplexClient != null) {
			HACenterDeploymentService centerDeploymentService = workerDuplexClient
					.getService(HACenterDeploymentService.class);
			serverGroups = centerDeploymentService.getServerGroups(ServiceEnviornment.getComputerName());
			return serverGroups;
		} else {
			throw new RuntimeException(String.format(
					"workerDuplexClient is null,so invoke getServerGroup failed , params = %s ", serviceName));
		}

	}

}
