/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-10-25
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.ha;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;
import com.feinno.configuration.ConfigType;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.Configurator;
import com.feinno.configuration.spi.HAConfigurator;
import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObserverInspector.ReportCallback;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReport;
import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.diagnostic.observation.ObserverReportEntity;
import com.feinno.diagnostic.observation.ObserverReportHelper;
import com.feinno.diagnostic.observation.ObserverReportMode;
import com.feinno.diagnostic.observation.ObserverReportRow;
import com.feinno.ha.interfaces.monitor.HAObserverDeployment;
import com.feinno.ha.interfaces.worker.HAWorkerAgentService;
import com.feinno.ha.interfaces.worker.HAWorkerHeartbeatArgs;
import com.feinno.ha.interfaces.worker.HAWorkerMonitorConfig;
import com.feinno.ha.interfaces.worker.HAWorkerRegisterArgs;
import com.feinno.ha.util.HAConfigUtils;
import com.feinno.ha.util.thresholds.ThresholdExpression;
import com.feinno.ha.util.thresholds.ThresholdsBuilder;
import com.feinno.logging.LogEvent;
import com.feinno.logging.common.ConcurrentFixedSizeQueue;
import com.feinno.logging.configuration.HaTracingCaputreConfigTableItem;
import com.feinno.logging.filter.FilterManager;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.threading.ExecutorFactory;
import com.feinno.util.DateTime;
import com.feinno.util.ServiceEnviornment;
import com.feinno.util.TimeSpan;

/**
 * 
 * <b>描述: </b>WorkerAgent负责和HACenter建立长连接, 并保持心跳, 自动重连
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class WorkerAgentHA extends WorkerAgent {

	/** Worker运行标志 */
	protected boolean running;

	/** 最后一次心跳时间 */
	protected DateTime lastHeartbeat;

	/** 客户端连接 */
	protected RpcDuplexClient client;

	/** HA-Center中的心跳及配置获取服务 */
	protected HAWorkerAgentService workerAgentService;

	/** 用于心跳的线程 */
	protected Thread heartbeatThread;

	/** 用于HA方式的配置加载器 */
	protected Configurator configurator;

	/** 心跳延迟的最小时间，最终的心跳时间是与服务端监控配置进行协商后的结果 */
	private static final int HEARTBEAT_MIN_DELAY = 60 * 1000;// TODO 60 * 1000

	/** 心跳延迟的最终生效时间，该时间是于服务器端监控配置协商后的结果，取所有时间的最小值 */
	private static int HEARTBEAT_EFFECTIVE_DELAY = Integer.MAX_VALUE;

	private static MonitorProcessor monitorProcessor = null;

	/** observer监控数据库的配置 */
	private static final String MONITOR_DB_NAME = "mondb.properties";

	/** 日志引用 */
	private static Logger LOGGER = LoggerFactory.getLogger(WorkerAgentHA.class);

	/**
	 * 构造方法中初始化HACenter的连接
	 */
	public WorkerAgentHA() {
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 取得HACenter的连接地址后连接远端并获得远端的HAWorkerAgentService服务接口
		RpcEndpoint centerEp = ServiceSettings.INSTANCE.getHACenterEp();
		client = new RpcDuplexClient(centerEp);
		client.setExecutor(ExecutorFactory.newFixedExecutor("Client", 10, 10240));
		workerAgentService = client.getService(HAWorkerAgentService.class);
		configurator = new HAConfigurator(client);
		ConfigurationManager.setConfigurator(configurator);
	}

	/**
	 * 向HACenter中注册Worker
	 * 
	 * @throws Exception
	 */
	public void register() throws Exception {
		try {
			client.connectSync();
			HAWorkerRegisterArgs args = new HAWorkerRegisterArgs();
			args.setServerName(ServiceEnviornment.getComputerName());
			args.setServiceName(ServiceSettings.INSTANCE.getServiceName());
			args.setWorkerPid(ServiceEnviornment.getPid());
			args.setServicePorts(ServiceSettings.INSTANCE.getServicePorts());
			workerAgentService.connect(args);
			workerAgentService.register(args);
			LOGGER.info("HA-Center Connection Successfully.");
		} catch (Exception ex) {
			LOGGER.error(String.format("Register %s error", ServiceSettings.INSTANCE.getHACenterEp()), ex);
			throw ex;
		}
	}

	public void close() {
		try {
			if (client != null) {
				client.close();
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Close %s error", ServiceSettings.INSTANCE.getHACenterEp()), e);
		}
	}

	/**
	 * 启动WorkerAgent
	 */
	@Override
	public void start() throws Exception {
		try {
			// 1. 向Center注册当前 Worker
			LOGGER.info("1. Register HA-Center.");
			register();

			// 2. 初始化日志
			LOGGER.info("2. Initialization looging config.");
			if (!initLogging() || !loadMarker()) {
				LOGGER.error("Initialization logging failed.Please check config.");
				return;
			}

			// 3. 启动Worker
			LOGGER.info("3. Start Service Component.");
			getServiceComponent().start();

			// 4. 初始化监控,此时包含了心跳延迟时间的协商
			LOGGER.info("4. Initialization Monitor.");
			initMonitor();

			LOGGER.info("5. Start Heartbeat Thread.");
			// 5. 如果不存在心跳线程，则创建Worker心跳线程，向远端的服务定时发送心跳包
			if (heartbeatThread == null) {
				heartbeatThread = new Thread(new Runnable() {
					@Override
					public void run() {
						heartbeatProc();
					}
				});
				// 启动心跳线程
				running = true;
				heartbeatThread.start();
			} else if (heartbeatThread != null && running == false) {
				// 如果存在心跳线程，但是线程不在运行，则重新start
				running = true;
				heartbeatThread.start();
			}
		} catch (Exception ex) {
			LOGGER.error("Start WorkerAgentHA failed, Reasons: ", ex);
			throw ex;
		}
	}

	/**
	 * 停止Worker
	 */
	@Override
	public void stop() throws Exception {
		heartbeat(Status.STOPPING);
		// 状态置为停止，并中断线程
		running = false;
		// 关闭
		getServiceComponent().stop();
		heartbeat(Status.STOPPED);
	}

	/**
	 * 定时发送心跳数据
	 */
	protected void heartbeatProc() {
		while (running) {
			try {
				// 1. 如果连接断了，需要进行失败重连
				if (!client.isConnected()) {
					LOGGER.warn("HA-Center Connection is broken,begin Reconnection.");
					// init();
					close();
					register();
					LOGGER.warn("HA-Center Reconnection is done.");
				}
				// 2. 发送心跳数据
				heartbeat(Status.STARTED);
				Thread.sleep(HEARTBEAT_EFFECTIVE_DELAY);
			} catch (Exception ex) {
				int waitTime = HEARTBEAT_EFFECTIVE_DELAY >> 2;
				LOGGER.error(String.format("WorkerAgentHA.heartbeatProc failed. Sleep %s ms. Reasons: ", waitTime), ex);
				// 如果心跳发送失败，那么尝试休眠心跳频率的1/4时间再进行重新发送，
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					LOGGER.error(String.format(
							" WorkerAgentHA.heartbeatProc failed,try sleep %s s ,but interrupted Reasons: ", waitTime),
							e);
				}
			}
		}
	}

	/**
	 * 初始化监控及心跳延迟时间
	 */
	private void initMonitor() {
		try {
			monitorProcessor = new MonitorProcessor(workerAgentService.getMonitorConfig());
			HEARTBEAT_EFFECTIVE_DELAY = monitorProcessor.initMonitorAndGetDelayTime();

			if (HEARTBEAT_EFFECTIVE_DELAY < HEARTBEAT_MIN_DELAY) {
				// 如果心跳时间小于默认的时间，则使用默认的心跳时间
				HEARTBEAT_EFFECTIVE_DELAY = HEARTBEAT_MIN_DELAY;
				LOGGER.info("Worker-Center heartbeat delay time less than the minimum time limit,");
				LOGGER.info("Worker-Center Final heartbeat interval is {} ms.", HEARTBEAT_EFFECTIVE_DELAY);
			} else if (HEARTBEAT_EFFECTIVE_DELAY > 5 * HEARTBEAT_MIN_DELAY) {
				// 如果心跳时间大于5倍的默认时间，则使用5倍的默认的心跳时间
				HEARTBEAT_EFFECTIVE_DELAY = 5 * HEARTBEAT_MIN_DELAY;
				LOGGER.info("Worker-Center heartbeat delay time than the maximum time limit.");
				LOGGER.info("Worker-Center Final heartbeat interval is {} ms.", HEARTBEAT_EFFECTIVE_DELAY);
			} else {
				LOGGER.info("Worker-Center heartbeat interval is {} ms.", HEARTBEAT_EFFECTIVE_DELAY);
			}

		} catch (Exception e) {
			HEARTBEAT_EFFECTIVE_DELAY = HEARTBEAT_MIN_DELAY;
			LOGGER.error("Init Monitor failed,now enabled by default delay.", e);
		}
	}

	/**
	 * 获得监控报表
	 * 
	 * @return
	 */
	public List<ObserverReportEntity> createMonitorProfile() {
		if (monitorProcessor == null) {
			return new ArrayList<ObserverReportEntity>();
		}
		List<ObserverReport> reportList = monitorProcessor.getUploadReport();
		List<ObserverReportEntity> reportEntityList = new ArrayList<ObserverReportEntity>(reportList.size());
		for (ObserverReport report : reportList) {
			try {
				byte[] buffer = report.encodeToProtobuf();
				ObserverReportEntity reportEntity = new ObserverReportEntity();
				reportEntity.parseFrom(buffer);
				reportEntityList.add(reportEntity);
			} catch (IOException e) {
				LOGGER.error("Create Monitor Profile failed.", e);
			}
		}
		return reportEntityList;
	}

	/**
	 * 发送心跳数据，在发送心跳的同时发送监控数据
	 */
	public void heartbeat(Status status) {
		try {
			lastHeartbeat = DateTime.now();
			HAWorkerHeartbeatArgs args = new HAWorkerHeartbeatArgs();
			args.setStatus(status.toString());
			args.setStatusEx("");
			args.setObserverReports(createMonitorProfile());
			workerAgentService.heartbeat(args);
		} catch (Exception e) {
			LOGGER.error("Send heartbeat failed. ", e);
		}
	}

	/**
	 * 加载Marker，返回成功或失败
	 * 
	 * @return
	 */
	public boolean loadMarker() {
		try {
			// 获取Marker后对Marker
			ConfigurationManager.loadTable(String.class, HALoggingMarkerTableItem.class, "HA_LoggingMarker",
					new ConfigUpdateAction<ConfigTable<String, HALoggingMarkerTableItem>>() {
						public void run(ConfigTable<String, HALoggingMarkerTableItem> value) throws Exception {
							LOGGER.info("Load Marker from HA-Center.");
							FilterManager.clearMarker();
							if (value != null && value.getValues() != null && value.getValues().size() > 0) {
								LogEvent.isEnableDefaultMarker = true;
								for (HALoggingMarkerTableItem loggingMarker : value.getValues()) {
									FilterManager.addMarker(MarkerFactory.getMarker(loggingMarker.getMarker()));
								}
							} else {
								LogEvent.isEnableDefaultMarker = false;
							}
						}
					});
			// 对Marker的后续变更进行订阅
			ConfigurationManager.subscribeConfigUpdate(ConfigType.TABLE, "HA_LoggingMarker", null);
			
			// 获取TraceCapture
			ConfigurationManager.loadTable(String.class, HaTracingCaputreConfigTableItem.class, "HA_TraceingCapture",
					new ConfigUpdateAction<ConfigTable<String, HaTracingCaputreConfigTableItem>>() {
						public void run(ConfigTable<String, HaTracingCaputreConfigTableItem> value) throws Exception {
							LOGGER.info("Load Capture from HA-Center.");
							FilterManager.clearCapture();
							if (value != null && value.getValues() != null && value.getValues().size() > 0) {
								for (HaTracingCaputreConfigTableItem tracingCapture : value.getValues()) {
									FilterManager.addCapture(tracingCapture.getIdentity());
									LOGGER.info("addCapture" + tracingCapture.getIdentity());
								}
							} else {
								//LogEvent.isEnableDefaultMarker = false;
							}
						}
					});
			// 对Capture的后续变更进行订阅
			ConfigurationManager.subscribeConfigUpdate(ConfigType.TABLE, "HA_TraceingCapture", null);
			return true;
		} catch (Exception e) {
			LOGGER.error("Load Marker from HA-Center failed. ", e);
			return false;
		}
	}

	public void getMonitorConfig() {
	}

	public DateTime getLastHearbeat() {
		return lastHeartbeat;
	}

	public RpcDuplexClient getDuplexClient() {
		return client;
	}

	private static enum Status {
		STARTED, STOPPING, STOPPED,
	}

	/**
	 * 
	 * <b>描述: </b>顾名思义，监控处理器，处理HACenter要求监控的数据
	 * <p>
	 * 
	 * @author Lv.Mingwei
	 */
	private static class MonitorProcessor {

		/** 监控配置 */
		private HAWorkerMonitorConfig monitorConfig = null;

		/** 本地监控报告写入数据库的辅助类 */
		private static ObserverReportHelper clientReportHelper = null;

		/** 待上传的监控报告cache，一个设有最大值的Queue */
		private static ConcurrentFixedSizeQueue<ObserverReport> uploadReportCache = new ConcurrentFixedSizeQueue<ObserverReport>(
				1024);

		public MonitorProcessor(HAWorkerMonitorConfig monitorConfig) {
			this.monitorConfig = monitorConfig;
		}

		/**
		 * 初始化监控以及获取最小时延
		 * 
		 * @return
		 */
		private int initMonitorAndGetDelayTime() {
			// 1. 进行observer的监控初始化以及心跳延迟时间的协商
			List<HAObserverDeployment> observerDeployments = monitorConfig.getObservers();
			if (observerDeployments != null && observerDeployments.size() > 0) {
				boolean isNeedUpload = false;
				int delayTime = Integer.MAX_VALUE;
				LOGGER.info("Found {} observer deployment information.", observerDeployments.size());
				for (HAObserverDeployment observerDeployment : observerDeployments) {
					// 注册该条监控
					addObserver(observerDeployment);
					// 取该条监控的时延，以判断是否是最小时延
					// 此处去掉了observerDeployment.isUpload()的判断，因为无论如何，数据终将有流向center的渠道
					if (observerDeployment != null && observerDeployment.getInterval() < delayTime) {
						delayTime = observerDeployment.getInterval();
						isNeedUpload = true;
					}
				}
				// 如果没有需要上传的，则依旧使用默认值作为心跳间隔时间
				if (!isNeedUpload) {
					delayTime = HEARTBEAT_MIN_DELAY;
				}
				LOGGER.info("{} ms as the observer Deployment delay time.", delayTime);
				return delayTime;
				// return 1000;
			} else {
				LOGGER.info("Observer Deployment is empty,now enabled by default delay {} ms.", HEARTBEAT_MIN_DELAY);
				return HEARTBEAT_MIN_DELAY;
				// return 1000;
			}
		}

		/**
		 * 添加一个监控观察者
		 * 
		 * @param observerDeployment
		 */
		private void addObserver(HAObserverDeployment observerDeployment) {
			// 设置监控
			Observable observable = ObserverManager.getObserverItem(observerDeployment.getObserverName());
			if (observable == null) {
				return;
			}
			// 创建监控间隔时间
			TimeSpan span = new TimeSpan(observerDeployment.getInterval());
			// 创建监控回调函数
			ReportCallback callback = createReportCallback(observerDeployment);
			// 创建监控模式
			ObserverReportMode reportMode = ObserverReportMode
					.valueOf(observerDeployment.getReportMode().toUpperCase());
			// 注册监控信息
			ObserverManager.addInspector(observable, reportMode, span, callback);
		}

		/**
		 * 创建监控报告的回调类，它有以下两个功能<br>
		 * 1. 将监控到的信息存入本地数据库<br>
		 * 2. 如果监控信息是需要上传的数据，且已经达到了HA-Center配置的告警阀值，则放入待上传队列中，等待在指定的监控时间内向HA-
		 * Center报告
		 * 
		 * @param observerDeployment
		 * @return
		 */
		private ReportCallback createReportCallback(final HAObserverDeployment observerDeployment) {
			return new ReportCallback() {
				@Override
				public boolean handle(final ObserverReport report) {
					try {
						// Step 1.写入本地数据库
						if (clientReportHelper == null) {
							InputStream mondbStream = null;
							try {
								mondbStream = HAConfigUtils.getConfigAsStream(MONITOR_DB_NAME);
								Properties properties = new Properties();
								properties.load(mondbStream);
								clientReportHelper = ObserverReportHelper.createClientHelper(properties);
							} finally {
								if (mondbStream != null) {
									mondbStream.close();
								}
							}
						}
						String serviceName = ServiceSettings.INSTANCE.getServiceName();
						String workerPid = String.valueOf(ServiceEnviornment.getPid());
						clientReportHelper.writeReport(report, serviceName, workerPid);

						// Step 2.向HA-Center进行告警
						if (observerDeployment.isUpload()) {
							// 如果开启了上传，则无论如何都需要把指标传给Center
							uploadReportCache.add(report);
						} else {
							// 如果没有开启上传，则需判断是否符合告警的条件，如果符合，则将待告警信息写入待上传的缓存中
							String thresholdExpr = observerDeployment.getThresholdExpr();
							if (observerDeployment.isUpload()) {
								ObserverReport alarmReport = matchThresholdExpression(report, thresholdExpr);
								if (alarmReport.getRows().size() > 0) {
									// 这个缓存会与HA-Center进行信条时将告警信息捎带过去
									uploadReportCache.add(alarmReport);
								}
							}
						}
					} catch (Exception e) {
						LOGGER.error("Observer call back run error.", e);
					}
					return true;
				}

			};
		}

		/**
		 * 用于判断监控报告与阀值公式是否匹配的方法，该方法来觉得某一个监控报告是否超出阀值
		 * 
		 * @param report
		 * @param thresholdExpr
		 * @return
		 */
		private ObserverReport matchThresholdExpression(ObserverReport report, String thresholdExpr) {
			// 初始化一个空的用于报警的监控报告
			ObserverReport alarmReport = new ObserverReport(report.getCategory(), report.getColumns(), report.getTime());
			if (thresholdExpr == null || thresholdExpr.length() == 0) {
				LOGGER.info("ThresholdExpr is empty. So ignore.");
				return alarmReport;
			}
			try {
				// 取出表达式
				HashMap<String, ThresholdExpression> exprMap = ThresholdsBuilder.parse(thresholdExpr);
				// 开始逐个匹配，找到符合表达式的，放入alarmReport对象中
				if (exprMap != null && exprMap.size() > 0) {
					Iterator<String> iterator = exprMap.keySet().iterator();
					while (iterator.hasNext()) {
						String fieldName = iterator.next();
						List<ObserverReportColumn> cols = report.getColumns();
						List<ObserverReportRow> rows = report.getRows();
						for (int i = 0; i < cols.size(); i++) {
							ObserverReportColumn col = cols.get(i);
							if (col.getName().trim().equals(fieldName)) {
								for (ObserverReportRow row : rows) {
									String[] data = row.getData();
									double value = Double.valueOf(data[i]);
									switch (exprMap.get(fieldName).getSymbols()) {
									case GREATER_THAN:
										if (value > exprMap.get(fieldName).getValue()) {
											alarmReport.getRows().add(row);
										}
										break;
									case LESS_THAN:
										if (value < exprMap.get(fieldName).getValue()) {
											alarmReport.getRows().add(row);
										}
										break;
									case GREATER_THAN_OR_EQUALT:
										if (value >= exprMap.get(fieldName).getValue()) {
											alarmReport.getRows().add(row);
										}
										break;
									case LESS_THAN_OR_EQUALT:
										if (value <= exprMap.get(fieldName).getValue()) {
											alarmReport.getRows().add(row);
										}
										break;
									case NONE:
										break;
									default:
										break;
									}
								}
								break;
							}
						}
					}
				}

			} catch (Exception e) {
				LOGGER.error("Parse Thresholds Exception.", e);
			}
			return alarmReport;
		}

		/**
		 * 获得当前需要上传的监控报告
		 * 
		 * @return
		 */
		public List<ObserverReport> getUploadReport() {
			List<ObserverReport> reports = new ArrayList<ObserverReport>();
			synchronized (uploadReportCache) {
				for (ObserverReport reportTemp : uploadReportCache) {
					reports.add(reportTemp);
				}
				uploadReportCache.clear();
			}
			return reports;
		}
	}

	public static class HALoggingMarkerTableItem extends ConfigTableItem {

		@ConfigTableField(value = "id", isKeyField = true)
		private int id;

		@ConfigTableField("marker")
		private String marker;

		@ConfigTableField("createTime")
		private Date createTime;

		public final int getId() {
			return id;
		}

		public final void setId(int id) {
			this.id = id;
		}

		public final String getMarker() {
			return marker;
		}

		public final void setMarker(String marker) {
			this.marker = marker;
		}

		public final Date getCreateTime() {
			return createTime;
		}

		public final void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

	}
}
