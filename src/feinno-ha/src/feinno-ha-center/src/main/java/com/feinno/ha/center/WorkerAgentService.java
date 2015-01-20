/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 23, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.center;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigParams;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.diagnostic.observation.ObserverReport;
import com.feinno.diagnostic.observation.ObserverReportEntity;
import com.feinno.diagnostic.observation.ObserverReportHelper;
import com.feinno.ha.ServiceSettings;
import com.feinno.ha.SystemCleaner;
import com.feinno.ha.database.HADBConfigHelper;
import com.feinno.ha.database.HADBMonitorHelper;
import com.feinno.ha.database.HADBServerHelper;
import com.feinno.ha.database.HADBStatusHelper;
import com.feinno.ha.database.HADBStatusHelper.Connected;
import com.feinno.ha.database.HADatabaseFactory;
import com.feinno.ha.interfaces.configuration.HAConfigArgs;
import com.feinno.ha.interfaces.configuration.HAConfigTableBuffer;
import com.feinno.ha.interfaces.configuration.HAConfigTextBuffer;
import com.feinno.ha.interfaces.monitor.HAMonitorDeployment;
import com.feinno.ha.interfaces.monitor.HAMonitorKey;
import com.feinno.ha.interfaces.monitor.HAMonitorScript;
import com.feinno.ha.interfaces.monitor.HAMonitorValuePair;
import com.feinno.ha.interfaces.monitor.HAObserverDeployment;
import com.feinno.ha.interfaces.worker.HAWorkerAgentService;
import com.feinno.ha.interfaces.worker.HAWorkerHeartbeatArgs;
import com.feinno.ha.interfaces.worker.HAWorkerMonitorConfig;
import com.feinno.ha.interfaces.worker.HAWorkerRegisterArgs;
import com.feinno.logging.common.FireEventQueue;
import com.feinno.logging.common.LogCommon;
import com.feinno.rpc.channel.RpcConnection;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcServiceBase;
import com.feinno.util.Action;
import com.feinno.util.EventHandler;

/**
 * 
 * 管理所有WorkerAgent的服务
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class WorkerAgentService extends RpcServiceBase {

	/** 当前为一个单例对象 */
	public static final WorkerAgentService INSTANCE = new WorkerAgentService();

	/** 该Map用于缓存链接上来的Worker */
	private static Map<String, WorkerAgent> WORKER_AGENT_CACHE = null;

	/** 配置表的缓存,当针对同一张表进行多次读取时,从此处读取 */
	private static Map<String, HAConfigTableBuffer> CONFIG_TABLE_CACHE = null;

	/** 一个具有事件触发功能的队列，该队列存储活跃的Worker心跳，当某一个Worker在一定时间没有发送心跳，则移除此队列以及对应的Map缓存 */
	private static FireEventQueue<String> fireEventQueue = null;

	/** 当前center的URL */
	public static String centerURL = null;

	/** observer监控数据库的配置 */
	private static final String MONITOR_DB_NAME = "mondb.properties";

	/** 用于observer监控数据存储的对象 */
	private static ObserverReportHelper observerReportHelper = null;

	/** 日志对象的引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkerAgentService.class);

	static {
		WORKER_AGENT_CACHE = Collections.synchronizedMap(new HashMap<String, WorkerAgent>());
		CONFIG_TABLE_CACHE = Collections.synchronizedMap(new HashMap<String, HAConfigTableBuffer>());
		fireEventQueue = FireEventQueue.newFreshBoxFireEventQueue(10 * 60 * 1000, new Action<String>() {
			// 如果某一连接长时间未发送心跳包，将触发本方法，本方法会移除Worker的缓存
			public void run(String workerName) {
				WORKER_AGENT_CACHE.remove(workerName);
			}
		});
		centerURL = "tcp://" + ServiceSettings.INSTANCE.getServerAddress() + ":"
				+ ServiceSettings.INSTANCE.getServicePort("rpc_duplex");

		// 启动WorkerAgent的监控，他会定期清理失效的连接
		EndpointMonitor.start();
		// 启动配置表订阅管理的监控，他会通知客户端哪些配置变更
		ConfigrationSubscriptionMonitor.start();
	}

	/**
	 * 私有的构造方法，满足单例模式
	 */
	private WorkerAgentService() {
		super(HAWorkerAgentService.SERVICE_NAME);
	}

	/**
	 * 连接到服务器端
	 */
	@RpcMethod("Connect")
	public void connect(RpcServerContext ctx) {

		// 通过入参，创建对应的WorkerAgent对象
		HAWorkerRegisterArgs args = ctx.getArgs(HAWorkerRegisterArgs.class);
		String workerName = getWorkerName(args.getServerName(), args.getWorkerPid());
		WorkerAgent agent = WORKER_AGENT_CACHE.get(workerName);
		if (agent != null) {
			// 如果已存在WorkerAgent对象，那么需要避免多台同样标识着同样服务器的Master同时注册
			LOGGER.warn("workerName {} already in use.", workerName);
			ctx.endWithError(new RuntimeException(String.format("workerName %s already in use.", workerName)));
		}
		// 缓存WorkerAgent
		agent = new WorkerAgent(ctx);
		agent.setServerName(args.getServerName());
		agent.setSeviceName(args.getServiceName());
		agent.setWorkerPid(args.getWorkerPid());
		WORKER_AGENT_CACHE.put(workerName, agent);
		// 放入WorkerAgent到连接中
		ctx.getConnection().setAttachment(agent);

		// 加入一个监听，如果出现断连，那么要清理掉这个连接
		ctx.getConnection().getDisconnected().addListener(new EventHandler<Throwable>() {
			public void run(Object sender, Throwable e) {
				RpcConnection conn = (RpcConnection) sender;
				workerAgentDisconnected((WorkerAgent) conn.getAttachment());
			}
		});
		ctx.end();
	}

	/**
	 * 
	 * 启动完毕, 端口都打开监听以后, 进行此操作
	 * 
	 * @param ctx
	 */
	@RpcMethod("Register")
	public void register(RpcServerContext ctx) {
		HAWorkerRegisterArgs args = ctx.getArgs(HAWorkerRegisterArgs.class);
		try {
			HADBStatusHelper.regWorkEndpoint(args, centerURL);
			ctx.end();
		} catch (Exception e) {
			String workerName = getWorkerName(args.getServerName(), args.getWorkerPid());
			LOGGER.error(String.format("Workeragent registration failed: workerName:%s . ", workerName), e);
			ctx.endWithError(new RuntimeException("register workeragent failed. ", e));
		}
	}

	/**
	 * 保持worker心跳
	 */
	@RpcMethod("Heartbeat")
	public void heartbeat(RpcServerContext ctx) {
		WorkerAgent agent = getWorkerAgent(ctx);
		HAWorkerHeartbeatArgs input = ctx.getArgs(HAWorkerHeartbeatArgs.class);
		try {
			String serverName = agent.getServerName();
			int workerPid = agent.getWorkerPid();

			// 将最新心跳加入到队列中,并更新数据库中的最新心跳时间
			fireEventQueue.add(getWorkerName(agent.getServerName(), agent.getWorkerPid()));
			HADBStatusHelper.heartbeat(serverName, workerPid, input.getStatus(), input.getStatusEx());

			// 如果当前的心跳包中有监控信息，那么连监控信息一起保存下来
			if (input.getValuePairs() != null) {
				for (HAMonitorValuePair haMonitorValuePair : input.getValuePairs()) {
					HADBStatusHelper.saveWorkerMonitorData(serverName, workerPid, haMonitorValuePair.getKey(),
							haMonitorValuePair.getValue(), haMonitorValuePair.getAlarm());
				}
			}

			// 如果当前心跳包有性能监控的信息，则写入性能监视器保存下来的数据
			if (input.getObserverReports() != null) {
				// 如果没初始化observer的写入对象,则需要初始化
				if (observerReportHelper == null) {
					Properties properties = ConfigurationManager.loadProperties(MONITOR_DB_NAME, null, null);
					observerReportHelper = ObserverReportHelper.createServerHelper(properties);
					// 如果创建出来了，则添加一个定时清理OBR表的任务
					if (observerReportHelper != null) {
						SystemCleaner.putExtensionTask(getOBRCleanTask(properties));
					}
				}

				for (ObserverReportEntity observerReportEntity : input.getObserverReports()) {
					// 奇怪的写法，因为ObserverReportRow的构造方法被修饰为protected,在ha中指有通过下面这种方式才能进行转化
					ObserverReport observerReport = ObserverReport.decodeFromProtobuf(observerReportEntity
							.toByteArray());
					observerReportHelper.writeReport(observerReport, serverName, agent.getSeviceName());
				}
			}
			ctx.end();
		} catch (Exception e) {
			String workerName = getWorkerName(agent.getServerName(), agent.getWorkerPid());
			LOGGER.error(String.format("Heartbeat update failed.workerName:%s .", workerName), e);
			ctx.endWithError(new RuntimeException("heartbeat update database failed. ", e));
		}
	}

	/**
	 * 获取监控配置
	 */
	@RpcMethod("GetMonitorConfig")
	public void getMonitorConfig(RpcServerContext ctx) {
		WorkerAgent agent = getWorkerAgent(ctx);
		try {
			// Step 1.从连接中取得服务器名称
			String serverName = agent.getServerName();
			// Step 2.通过HADBMonitorHelper取得监控配置
			List<String> serverGroupList = HADBServerHelper.getServerGroupList(serverName);
			List<HAMonitorDeployment> deployments = HADBMonitorHelper.getHAMonitorDeployments(serverGroupList);
			List<HAObserverDeployment> observers = HADBMonitorHelper.getHAObserverReportDeployments(serverGroupList);
			List<HAMonitorKey> haMonitorKeys = HADBMonitorHelper.getHAMonitorKeys(deployments);
			List<HAMonitorScript> haMonitorScripts = HADBMonitorHelper.getHAMonitorScript(haMonitorKeys);
			// Step 3.拼接监控数据
			HAWorkerMonitorConfig monitorConfig = new HAWorkerMonitorConfig();
			monitorConfig.setDeployments(deployments);
			monitorConfig.setKeys(haMonitorKeys);
			monitorConfig.setObservers(observers);
			monitorConfig.setScripts(haMonitorScripts);
			// Step 4.结束,回执
			ctx.end(monitorConfig);
		} catch (Exception e) {
			String workerName = getWorkerName(agent.getServerName(), agent.getWorkerPid());
			LOGGER.error(String.format("GetMonitorConfig failed.workerName:%s .", workerName), e);
			ctx.endWithError(new RuntimeException("GetMonitorConfig failed.workerName. ", e));
		}

	}

	/**
	 * 获取配置表
	 * 
	 * @param ctx
	 */
	@RpcMethod("LoadConfigTable")
	public void loadConfigTable(RpcServerContext ctx) {
		WorkerAgent agent = getWorkerAgent(ctx);
		HAConfigArgs input = ctx.getArgs(HAConfigArgs.class);
		try {
			LOGGER.info("Load config table.table:{}", input.getPath());
			// 1.首先获取此配置表的配置信息
			HAConfigTableBuffer tableBuffer = new HAConfigTableBuffer();
			String dbName = HADBConfigHelper.getDatabaseName(input.getPath(), tableBuffer);

			// 2. 从缓存中读取配置，如果缓存中没有数据或者缓存中的数据版本与当前最新版本不相符，则更新缓存中的配置
			HAConfigTableBuffer tableBufferCache = CONFIG_TABLE_CACHE.get(tableBuffer.getTableName());
			if (tableBufferCache == null || tableBuffer.getVersion() == null
					|| !tableBuffer.getVersion().equals(tableBufferCache.getVersion())
					|| "FAE_Application".equalsIgnoreCase(tableBuffer.getTableName())) {
				synchronized (CONFIG_TABLE_CACHE) {
					if (CONFIG_TABLE_CACHE.get(tableBuffer.getTableName()) == null || tableBuffer.getVersion() == null
							|| !tableBuffer.getVersion().equals(tableBufferCache.getVersion())
							|| "FAE_Application".equalsIgnoreCase(tableBuffer.getTableName())) {
						if (dbName != null) {
							LOGGER.info("Load config table and refresh cache.table:{}", input.getPath());
							Database database = DatabaseManager.getDatabase(dbName);
							if (database == null) {
								Properties properties = ConfigurationManager.loadProperties(dbName + ".properties",
										null, null);
								database = HADatabaseFactory.getDatabase(dbName, properties);
							}
							tableBufferCache = HADBConfigHelper.getConfigTable(database, input.getPath(), tableBuffer);
						} else {
							tableBufferCache = HADBConfigHelper.getConfigTableByBaseCenter(input.getPath(), null);
						}

						CONFIG_TABLE_CACHE.put(tableBuffer.getTableName(), tableBufferCache);
					} else {
						tableBufferCache = CONFIG_TABLE_CACHE.get(tableBuffer.getTableName());
					}
				}
			}

			// 3.更新最后一次读取时间
			HADBConfigHelper.updateLastReadTime(agent.getServerName(), agent.getWorkerPid(), input.getType(),
					input.getPath(), input.getParams());
			LOGGER.info("Load config table success.table:{}", input.getPath());
			ctx.end(tableBufferCache);
		} catch (Exception e) {
			String workerName = getWorkerName(agent.getServerName(), agent.getWorkerPid());
			LOGGER.error(String.format("loadConfigTable failed.workerName:%s .", workerName), e);
			// 出错后更新最后一次出错时间
			HADBConfigHelper.updateLastError(agent.getServerName(), agent.getWorkerPid(), input.getType(),
					input.getPath(), input.getParams(), LogCommon.formaError(e));
			ctx.endWithError(e);
		}
	}

	/**
	 * 获取监控表的清理任务
	 * 
	 * @param monDBprops
	 * @return
	 */
	private Runnable getOBRCleanTask(final Properties monDBprops) {
		return new Runnable() {
			@Override
			public void run() {
				Database monDBHelper = DatabaseManager.getMonDBHelper(monDBprops.getProperty("Database"), monDBprops);
				try {
					LOGGER.info("Clean OBR_SERVER_apps expired data.");
					monDBHelper
							.executeNonQuery("delete from `OBR_SERVER_apps` where Updatetime < date_sub(now(), interval 3 hour)");
				} catch (SQLException e) {
					LOGGER.error("Clean OBR_SERVER_apps expired data failed.", e);
				}
				try {
					LOGGER.info("Clean OBR_SERVER_database-sp expired data.");
					monDBHelper
							.executeNonQuery("delete from `OBR_SERVER_database-sp` where Updatetime < date_sub(now(), interval 3 hour)");
				} catch (SQLException e) {
					LOGGER.error("Clean OBR_SERVER_database-sp expired data failed.", e);
				}
				try {
					LOGGER.info("Clean OBR_SERVER_rpc:tcp-channel expired data.");
					monDBHelper
							.executeNonQuery("delete from `OBR_SERVER_rpc:tcp-channel` where Updatetime < date_sub(now(), interval 3 hour)");
				} catch (SQLException e) {
					LOGGER.error("Clean OBR_SERVER_rpc:tcp-channel expired data failed.", e);
				}
				try {
					LOGGER.info("Clean OBR_SERVER_rpc-client expired data.");
					monDBHelper
							.executeNonQuery("delete from `OBR_SERVER_rpc-client` where Updatetime < date_sub(now(), interval 3 hour)");
				} catch (SQLException e) {
					LOGGER.error("Clean OBR_SERVER_rpc-client expired data failed.", e);
				}
				try {
					LOGGER.info("Clean OBR_SERVER_rpc-server expired data.");
					monDBHelper
							.executeNonQuery("delete from `OBR_SERVER_rpc-server` where Updatetime < date_sub(now(), interval 3 hour)");
				} catch (SQLException e) {
					LOGGER.error("Clean OBR_SERVER_rpc-server expired data failed.", e);
				}
			}

		};
	}

	/**
	 * 获取配置文本
	 * 
	 * @param ctx
	 */
	@RpcMethod("LoadConfigText")
	public void loadConfigText(RpcServerContext ctx) {
		WorkerAgent agent = getWorkerAgent(ctx);
		HAConfigArgs input = ctx.getArgs(HAConfigArgs.class);
		try {
			LOGGER.info("Load config text.path:{}, params{}", input.getPath(), input.getParams());
			// 1. 加载
			HAConfigTextBuffer textBuffer = HADBConfigHelper.getConfigText(input.getPath(),
					new ConfigParams(input.getParams()));

			// 2. 更新最后一次获取时间
			HADBConfigHelper.updateLastReadTime(agent.getServerName(), agent.getWorkerPid(), input.getType(),
					input.getPath(), input.getParams());
			LOGGER.info("Load config text success.path:{}, params{}", input.getPath(), input.getParams());
			ctx.end(textBuffer);
		} catch (Exception e) {
			String workerName = getWorkerName(agent.getServerName(), agent.getWorkerPid());
			LOGGER.error(String.format("loadConfigText failed.workerName:%s .", workerName), e);
			// 出错后更新最后一次出错时间
			HADBConfigHelper.updateLastError(agent.getServerName(), agent.getWorkerPid(), input.getType(),
					input.getPath(), input.getParams(), LogCommon.formaError(e));
			ctx.endWithError(e);
		}
	}

	/**
	 * 订阅配置更新
	 * 
	 * @param ctx
	 */
	@RpcMethod("SubscribeConfig")
	public void subscribeConfig(RpcServerContext ctx) {
		WorkerAgent agent = getWorkerAgent(ctx);
		HAConfigArgs input = ctx.getArgs(HAConfigArgs.class);
		try {
			// 1. 订阅某个配置的自动更新
			HADBConfigHelper.subscribeConfig(true, agent.getServerName(), agent.getWorkerPid(), input.getType(),
					input.getPath(), input.getParams());
			ctx.end();
		} catch (Exception e) {
			String workerName = getWorkerName(agent.getServerName(), agent.getWorkerPid());
			LOGGER.error(String.format("subscribeConfig failed.workerName:%s .", workerName), e);
			ctx.endWithError(e);
		}
	}

	/**
	 * 
	 * 更新已订阅的配置版本
	 * 
	 * @param ctx
	 */
	@RpcMethod("UpdateConfigVersion")
	public void updateConfigVersion(RpcServerContext ctx) {
		WorkerAgent agent = getWorkerAgent(ctx);
		HAConfigArgs input = ctx.getArgs(HAConfigArgs.class);
		try {
			// 更新配置表版本号
			HADBConfigHelper.updateVersion(agent.getServerName(), agent.getWorkerPid(), input.getType(),
					input.getPath(), input.getParams(), input.getVersion());
			ctx.end();
		} catch (Exception e) {
			String workerName = getWorkerName(agent.getServerName(), agent.getWorkerPid());
			LOGGER.error(String.format("updateConfigVersion failed.workerName:%s .", workerName), e);
			ctx.endWithError(e);
		}

	}

	/**
	 * 通过workerName获得此worker的信息及连接
	 * 
	 * @param workerName
	 * @return
	 */
	public WorkerAgent getWorkerAgent(String workerName) {
		return WORKER_AGENT_CACHE.get(workerName);
	}

	/**
	 * 通过serviceName获取对应的WorkerAgent列表
	 * 
	 * @param serviceName
	 * @return
	 */
	public List<WorkerAgent> getWorkerAgents(String serviceName) {
		List<WorkerAgent> workerAgents = new ArrayList<WorkerAgent>();
		for (WorkerAgent workerAgent : WORKER_AGENT_CACHE.values()) {
			if (workerAgent != null && workerAgent.getSeviceName().equals(serviceName)) {
				workerAgents.add(workerAgent);
			}
		}
		return workerAgents;
	}

	/**
	 * 通过serverName与workerPid获得连接对象，此对象同样是在connect时被放入WORKER_AGENT_CACHE中
	 * 
	 * @param serverName
	 * @param workerPid
	 * @return
	 */
	public WorkerAgent getWorkerAgent(String serverName, int workerPid) {
		return WORKER_AGENT_CACHE.get(getWorkerName(serverName, workerPid));
	}

	/**
	 * 通过ServerName与WorkerPid获得一个WorkerName
	 * 
	 * @param serverName
	 * @param workerPid
	 * @return
	 */
	private String getWorkerName(String serverName, int workerPid) {
		return serverName + "@" + workerPid;
	}

	/**
	 * 从连接中获取WorkerAgent对象,此对象在connect时被放入连接中
	 * 
	 * @param ctx
	 * @return
	 */
	private WorkerAgent getWorkerAgent(RpcServerContext ctx) {
		return (WorkerAgent) ctx.getConnection().getAttachment();
	}

	/**
	 * 断开某一个Worker
	 * 
	 * @param agent
	 */
	private void workerAgentDisconnected(WorkerAgent agent) {
		String serverName = agent.getServerName();
		LOGGER.error("WorkerAgent Disconnected. workerName:{} .", serverName);
		int workerPid = agent.getWorkerPid();
		// 清理该Worker在缓存中的记录
		WORKER_AGENT_CACHE.remove(getWorkerName(serverName, workerPid));
		try {
			// 更新数据库的连接状态为过期
			HADBStatusHelper.updateConnected(serverName, workerPid, Connected.DISCONNECTED);
		} catch (Exception e) {
			String workerName = getWorkerName(agent.getServerName(), agent.getWorkerPid());
			LOGGER.error(String.format("WorkerAgent disconnected failed. workerName:%s .", workerName), e);
		}
	}
}
