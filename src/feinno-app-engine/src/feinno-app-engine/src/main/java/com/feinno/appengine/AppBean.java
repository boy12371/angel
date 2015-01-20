/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-15
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.appengine;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.configuration.AppBeanSettings;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.configuration.ConfigParams;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.ConfigurationNotFoundException;
import com.feinno.diagnostic.perfmon.PerformanceCounterFactory;
import com.feinno.diagnostic.perfmon.Stopwatch;
import com.feinno.diagnostic.perfmon.monitor.legacy.AppBeanPerformanceCounters;
import com.feinno.threading.ExecutorFactory;
import com.feinno.util.Action;
import com.feinno.util.Combo2;
import com.feinno.util.ConfigBean;
import com.feinno.util.EventHandler;
import com.feinno.util.StringUtils;

/**
 * 
 * FAE中App运行到最小单位，表示一个应用 实现AppBean接口可以让一个对象运行在FAE平台上
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class AppBean implements AppTxHandler {
	public static final String SETTINGS_FILE = "./META-INF/version.properties";
	public static final String SETTINGS_VERSION_FIELD = "version";
	private static final String SETTINS_SERVICE_NAME_FIELD = "serviceName";
	private static final Logger LOGGER_S = LoggerFactory.getLogger(AppBean.class);
	public static final String APPBEAN_SETTING_FILE = "appbean.properties";

	public final Logger LOGGER;

	private String category;
	private String name;
	private String version;
	private String categoryMinusName;
	private String serviceName;
	private AppBeanInjector injector;
	private AppBeanPerformanceCounters counters;
	private AppBeanSessionCouters sessionCounters;

	private AppBeanSettings settings;
	private Executor executor;
	private String executorName;
	private AtomicInteger sessions;

	private GCFriendlyConCurrentFixedSizeQ<Combo2<AppTx, Action<AppTx>>> waitQueque;

	protected AppBean() {
		String beanName = this.getClass().getName();
		LOGGER = LoggerFactory.getLogger(beanName);
		LOGGER.info("AppBean {} loaded", beanName);

		injector = new AppBeanInjector(this);

		AppName anno = this.getClass().getAnnotation(AppName.class);
		if (anno == null) {
			throw new IllegalArgumentException("AppBean missing @AppName annotation");
		}

		category = anno.category();
		name = anno.name();
		version = loadVersion(this.getClass());
		serviceName = loadServiceName(this.getClass());
		categoryMinusName = anno.category() + "-" + anno.name();
		counters = PerformanceCounterFactory.getCounters(AppBeanPerformanceCounters.class, categoryMinusName);
		sessionCounters = (PerformanceCounterFactory.getCounters(AppBeanSessionCouters.class, categoryMinusName));
		sessions = new AtomicInteger();

		//
		// 读取配置，创建线程池
		settings = loadSettings(this.categoryMinusName, this.serviceName);

		if (settings != null) {
			executorName = settings.getThreadPool();
			if (!StringUtils.isNullOrEmpty(executorName)) {
				executor = ExecutorFactory.getExecutor(executorName);
			} else {
				executorName = categoryMinusName;
			}
			if (executor == null) {
				executor = ExecutorFactory.newFixedExecutor(executorName, settings.getMaxThreads(),
						settings.getMaxQueueLength());
			}
		} else {
			//
			// 设置为默认值
			executor = AppEngineManager.INSTANCE.getDefaultExecutor();
			executorName = AppEngineManager.DEFAULT_EXECUTOR_NAME;
		}
		waitQueque = new GCFriendlyConCurrentFixedSizeQ<Combo2<AppTx, Action<AppTx>>>(settings.getMaxQueueLength());
	}

	/**
	 * 
	 * 获得一个注入器
	 */
	public AppBeanInjector getInjector() {
		return injector;
	}

	/**
	 * 
	 * 子类的host创建transaction成功后，需要调用AppHost的transactionBegin方法
	 * 
	 * @param tx
	 */
	protected void processHandlerChain(final AppTx tx, final Action<AppTx> callback) {
		if (tx.terminated()) {
			LOGGER.warn("AppTx is terminated. AppTx = {}", tx);
			return;
		}

		tx.setCurrentApp(this);
		getSessionCounters().getCurrentSession().setRawValue(sessions.get());
		getSessionCounters().getSessionQuequeLength().setRawValue(waitQueque.getSize());
		if (sessions.get() >= settings.getMaxSessions()) {
			if (!waitQueque.offer(new Combo2<AppTx, Action<AppTx>>(tx, callback))) {
				getSessionCounters().getSessionRejectCount().increase();
				 LOGGER.error("Current sessions over the limit and queque size.sessions:{},maxSessions:{}",
				 sessions.get(), settings.getMaxSessions());
				try {
					tx.setError(new AppEngineException(AppEngineException.SERVER_BUSY, categoryMinusName + ":busy",
							null));
				} finally {
					tx.terminate(); // session数在这里没有减少,因为没有挂事件
					if (callback != null) {
						callback.run(tx);
					}
				}
			}
			return;
		}
		this.incrementTx();
		// appTxmanger只管理正在处理的tx,
		// incrementTx和addTx同时出现，保证 session的增加和减少逻辑没有泄露
		AppTxManager.INSTANCE.addTx(tx);
		tx.getTerminatedEvent().addListener(new EventHandler<Throwable>() {
			@Override
			public void run(Object arg0, Throwable arg1) {
				Combo2<AppTx, Action<AppTx>> task = waitQueque.poll();
				while (task != null) {
					if ((AppTx.SERVER_TIMEOUT - task.getV1().getElapseMs()) < 5000) {
						LOGGER.error("apptx wait too long in queque, drop it");
						try {
							task.getV1().setError(
									new AppEngineException(AppEngineException.SERVER_BUSY, categoryMinusName + ":busy",
											null));
						} finally {
							task.getV1().terminate(); // session数在这里没有减少,因为没有挂事件
							if (task.getV2() != null) {
								task.getV2().run(task.getV1());
							}
						}
						task = waitQueque.poll();
					} else {
						processHandlerChain(task.getV1(), task.getV2());
						break;
					}
				}
			}
		});

		try {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					final Stopwatch watch = counters.getTx().begin();
					final AtomicBoolean isCallbackDone = new AtomicBoolean(false);
					injector.processTx(tx, new Action<Exception>() {
						@Override
						public void run(Exception a) {
							// TODO: 计算调用事件
							// long elapesNanos = System.nanoTime() -
							// tx.getCreatedTicks();
							if (isCallbackDone.compareAndSet(false, true)) {
								Throwable e = tx.error();
								if (e == null) {
									watch.end();
								} else {
									watch.fail(e);
								}
								if (callback != null) {
									callback.run(tx);
								}
							} else {
								// 创建异常的目的是为了得到调用栈，这样有助于在这里出现问题后的调试
								RuntimeException exception = new RuntimeException("CallBack is already invoke");
								LOGGER.error("", exception);
							}

						}
					});
				}
			});
		} catch (Exception ex) {
			LOGGER.error("executor error {}", ex);
			tx.terminate();
			tx.setError(new AppEngineException(AppEngineException.SERVER_ERROR, categoryMinusName + ":excuteor", ex));
			if (callback != null) {
				callback.run(tx);
			}
		}
	}

	public String getAppCategory() {
		return category;
	}

	public String getAppName() {
		return name;
	}

	public String getCategoryMinusName() {
		return categoryMinusName;
	}

	/**
	 * 
	 * 获取当前AppBean的version 配置在工程的resource/META-INF/
	 * 
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 
	 * 在此方法中实现所有相关资源的自安装
	 */
	public abstract void setup() throws Exception;

	/**
	 * 
	 * 当AppBean加载时执行此方法
	 */
	public abstract void load() throws Exception;

	/**
	 * 
	 * 当AppBean卸载时执行此方法
	 */
	public abstract void unload() throws Exception;

	/**
	 * 
	 * 准备一个transaction的数据并开始运行
	 * 
	 * @param tx
	 */
	public void prepareTx(AppTx tx) {
	}

	/**
	 * 
	 * 读取版本号
	 * 
	 * @param clazz
	 * @return
	 */
	public static String loadVersion(Class<?> clazz) {
		URL fileUrl = clazz.getClassLoader().getResource(SETTINGS_FILE);
		String version;
		if (fileUrl != null) {
			try {
				InputStream in = clazz.getClassLoader().getResourceAsStream(SETTINGS_FILE);
				Properties props = new Properties();
				props.load(in);
				version = props.getProperty(SETTINGS_VERSION_FIELD);
			} catch (IOException e) {
				LOGGER_S.error("loadVersion failed", e);
				version = "0.0.0.Error";
			}
		} else {
			version = "0.0.0.NotFound";
		}
		return version;
	}

	/**
	 * 
	 * 获取服务名称 TODO: 加个配置文件的实体类重构一下
	 * 
	 * @param clazz
	 * @return
	 */
	public static String loadServiceName(Class<?> clazz) {
		URL fileUrl = clazz.getClassLoader().getResource(SETTINGS_FILE);
		String serviceName;
		if (fileUrl != null) {
			try {
				InputStream in = clazz.getClassLoader().getResourceAsStream(SETTINGS_FILE);
				Properties props = new Properties();
				props.load(in);
				serviceName = props.getProperty(SETTINS_SERVICE_NAME_FIELD);
			} catch (IOException e) {
				LOGGER_S.error("loadVersion failed", e);
				serviceName = "serviceNameError";
			}
		} else {
			serviceName = "serviceNameNotFound";
		}
		return serviceName;
	}

	/**
	 * 
	 * 读取配置, (异常转成RuntimeException了,不爽)
	 * 
	 * @param categryMinusName
	 * @throws ConfigurationException
	 */
	public static AppBeanSettings loadSettings(String categoryMinusName, String service) {
		ConfigParams params = new ConfigParams();
		params.put("app", categoryMinusName);
		params.put("service", service);

		try {
			Properties props = ConfigurationManager.loadProperties(APPBEAN_SETTING_FILE, params, null);
			return ConfigBean.valueOf(props, AppBeanSettings.class);
		} catch (ConfigurationNotFoundException ex) {
			return null;
		} catch (ConfigurationException ex) {
			LOGGER_S.error("loadConfigText(AppBean.properties) failed, {}", ex);
			throw new RuntimeException("loadConfigText(AppBean.properties) failed", ex);
		}
	}

	public static void main(String[] args) {
		String version = AppBean.loadVersion(AppBean.class);
		System.out.println(version);
		String serviceName = AppBean.loadServiceName(AppBean.class);
		System.out.println(serviceName);
		AppBeanSettings settings = AppBean.loadSettings("core-register", serviceName);
		System.out.println(settings.getMaxQueueLength());
	}

	/**
	 * 
	 * @param tx
	 */
	void releaseTx(AppTx tx) {
		sessions.decrementAndGet();
	}

	/**
	 * 
	 * @param tx
	 */
	void incrementTx() {
		sessions.incrementAndGet();
	}

	public AppBeanSessionCouters getSessionCounters() {
		return sessionCounters;
	}

	public Executor getExecutor() {
		return this.executor;
	}
/**
 * 一个固定大小，先进后出， 满了还休息一段时间的队列，所有这些都是为了gc好。
 * @author linsu
 *
 * @param <E>
 */
	final class GCFriendlyConCurrentFixedSizeQ<E> {
		private LinkedList<E> innerQ;
		private int MAXSIZE;
		private int size;
		private boolean GCfriendlyRefuse;
		private Timer checkTimer;

		public GCFriendlyConCurrentFixedSizeQ(int maxSize) {
			this.size = 0;
			innerQ = new LinkedList<E>();
			MAXSIZE = maxSize;
			GCfriendlyRefuse = (maxSize <= 0) ? true : false;
			checkTimer = null;
		}

		public synchronized int getSize() {
			return size;
		}

		public synchronized E poll() {
			E item = innerQ.poll();
			if (item != null)
				size--;
			return item;
		}

		public synchronized boolean offer(E item) {
			if (GCfriendlyRefuse)
				return false;
			else if (size < MAXSIZE) {
				size++;
				innerQ.addFirst(item);
				tryCheck();
				return true;
			} else {
				// stop work for gc's sake
//				coolDownGC();
				return false;
			}
		}
		private void tryCheck() {
			if(checkTimer==null) {
				checkTimer = new Timer(categoryMinusName + "-GCfriendlyQTimer-checkQueque", true);
				checkTimer.schedule(new TimerTask() {
					int runCount = 6;
					int checkCount = 0;
					@Override
					public void run() {
						runCount--;
						if(getSize()>0)
							checkCount++;
						if(runCount==0) {
							this.cancel();
							if(checkCount>=5) { //一分钟内 check 6次, 5次队列都有值
								getSessionCounters().getSessionShrinkCount().increase();
								coolDownGC();
							}else {
								checkTimer.cancel();
								checkTimer=null;
							}								
						}						
					}
				},10*1000,10*1000);
			}		
		}
		public void coolDownGC() {
			GCfriendlyRefuse = true;
			final int orignalMaxSessions = settings.getMaxSessions();
			settings.setMaxSessions(2);
			// make gc cool down, narrow the gate!		
			checkTimer.schedule(new TimerTask() {			
				@Override
				public void run() {
					settings.setMaxSessions(5);
				}
			},60*1000);
			checkTimer.schedule(new TimerTask() {			
				@Override
				public void run() {
					settings.setMaxSessions(orignalMaxSessions);
					GCfriendlyRefuse = false;// resume to work again;
					checkTimer.cancel();
					checkTimer=null;
				}
			},120*1000);
		}
		
	}

}
