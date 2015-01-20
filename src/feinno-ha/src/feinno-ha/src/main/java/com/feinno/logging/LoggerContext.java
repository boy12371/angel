package com.feinno.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.ILoggerFactory;

import com.feinno.ha.util.HAConfigUtils;
import com.feinno.logging.appender.AppenderAttachable;
import com.feinno.logging.appender.CaptureAppender;
import com.feinno.logging.appender.ConsoleAppender;
import com.feinno.logging.appender.JdbcAppender;
import com.feinno.logging.appender.TextFileAppender;
import com.feinno.logging.configuration.CacheSettings;
import com.feinno.logging.configuration.CaptureAppenderSetting;
import com.feinno.logging.configuration.DatabaseAppenderSetting;
import com.feinno.logging.configuration.FilterSetting;
import com.feinno.logging.configuration.LogSettings;
import com.feinno.logging.configuration.SubLogger;
import com.feinno.logging.filter.CaptureFilter;
import com.feinno.logging.filter.Filter;
import com.feinno.serialization.protobuf.util.ClassUtils;
import com.feinno.util.ConfigBean;
import com.feinno.util.PropertiesUtil;

/**
 * 这是一个持有日志上下文信息的类，它是一个单例的,其中存储着日志的配置信息，appender集合以及曾经创建过的日志{@link Logger} 对象
 * 
 * @author Lv.Mingwei
 * 
 */
public class LoggerContext implements ILoggerFactory {
	/**
	 * 日志对象容器集合
	 */
	Map<String, Logger> loggerCache = null;

	/**
	 * 日志的配置信息
	 */
	private static LogSettings settings = null;

	/**
	 * 日志数量计数器
	 */
	private int size = 0;

	/**
	 * 根日志节点
	 */
	Logger root;

	/**
	 * 是否启用缓存，默认不启用
	 */
	private boolean isEnableCache = false;

	/**
	 * 日志的appender集合
	 */
	private AppenderAttachable appenderAttachable;

	/**
	 * Capture的appender集合
	 */
	private AppenderAttachable captureAttachable;

	/**
	 * 这是一个单例类
	 */
	private static final LoggerContext INSTANCE = new LoggerContext();

	/**
	 * 提供单例获取方法
	 * 
	 * @return
	 */
	public static final LoggerContext getInstance() {
		return INSTANCE;
	}

	/**
	 * 私有的构造器，是此类为单例模式
	 */
	private LoggerContext() {
	}

	static {
		getInstance().initializer();
	}

	/**
	 * 为首次使用做初始化
	 */
	private void initializer() {
		// 初次使用LoggerContext，首先进行初始化
		this.loggerCache = new HashMap<String, Logger>();
		this.root = new Logger(Logger.ROOT_LOGGER_NAME, null, this);
		this.root.setLevel(LogLevel.INFO);
		loggerCache.put(Logger.ROOT_LOGGER_NAME, root);
		size = 1;
		InputStream configStream = null;
		try {
			configStream = HAConfigUtils.getConfigAsStream("logging.xml");
			Properties props = PropertiesUtil.xmlToProperties(configStream);
			loadSettings(props);
		} catch (Exception e) {
			throw new RuntimeException("Load logging.xml failed.", e);
		} finally {
			if (configStream != null) {
				try {
					configStream.close();
				} catch (IOException e) {
					// 此时出错因为日志没有初始化出来,所以无法写入日志...只能打印到控制台中...
					e.printStackTrace();
				}
			}
		}

	}

	public Logger getLogger(final Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	/**
	 * 根据日志名称获取日志对象
	 * 
	 * @param name
	 *            日志名称
	 */
	public Logger getLogger(final String name) {

		if (name == null) {
			throw new IllegalArgumentException("name argument cannot be null");
		}

		if (Logger.ROOT_LOGGER_NAME.equalsIgnoreCase(name)) {
			return root;
		}

		int i = 0;
		Logger logger = root;

		// check if the desired logger exists, if it does, return it
		// without further ado.
		Logger childLogger = (Logger) loggerCache.get(name);
		// if we have the child, then let us return it without wasting time
		if (childLogger != null) {
			return childLogger;
		}

		// if the desired logger does not exist, them create all the loggers
		// in between as well (if they don't already exist)
		String childName;
		while (true) {
			int h = Logger.getSeparatorIndexOf(name, i);
			if (h == -1) {
				childName = name;
			} else {
				childName = name.substring(0, h);
			}
			// move i left of the last point
			i = h + 1;
			synchronized (logger) {
				childLogger = logger.getChildByName(childName);
				if (childLogger == null) {
					childLogger = logger.createChildByName(childName);
					loggerCache.put(childName, childLogger);
					incSize();
				}
			}
			logger = childLogger;
			if (h == -1) {
				return childLogger;
			}
		}
	}

	/**
	 * 为了给所创建的日志计数，不过暂时没有用，呵呵
	 */
	private void incSize() {
		size++;
	}

	int size() {
		return size;
	}

	/**
	 * 加载日志配置信息
	 * 
	 * @param props
	 *            Properties类型文件
	 */
	public synchronized void loadSettings(Properties props) {
		// 为LogSettings对象实例变量赋值
		settings = ConfigBean.valueOf(props, LogSettings.class);
		// 配置信息的日志级别不为空时,设置rootLevel级别,否则是默认级别 INFO
		if (settings.getLevel() != null && !"".equals(settings.getLevel()))
			this.root.setLevel(LogLevel.valueOf(settings.getLevel().toUpperCase()));

		setFilter(this.root, settings.getFilter());

		// 使用XML中存储的子日志等级进行设置
		if (settings.getLoggers() != null && settings.getLoggers().getLogger() != null) {
			Map<String, SubLogger> loggerMap = settings.getLoggers().getLogger();
			for (String key : loggerMap.keySet()) {
				SubLogger subLogger = loggerMap.get(key);
				String keyString = subLogger.getKey().replaceAll("/", ".");
				Logger logger = loggerCache.get(keyString);
				if (logger == null) {
					logger = this.getLogger(keyString);
					loggerCache.put(keyString, logger);
				}
				// 设置子日志等级
				logger.setLevel(LogLevel.valueOf(subLogger.getLevel().toUpperCase()));
				// 设置子日志Filter
				setFilter(logger, subLogger.getFilter());
			}
		}

		// 管理appender
		if (appenderAttachable == null) {
			appenderAttachable = new AppenderAttachable();
		} else {
			appenderAttachable.removeAllAppender();
			appenderAttachable.closeQueue();
		}
		if (captureAttachable == null){
			captureAttachable = new AppenderAttachable();
		}
		else {
			captureAttachable.removeAllAppender();
			captureAttachable.closeQueue();
		}
		// 设置日志缓存
		setEnableCache(settings.getCache());
		// 每个appender独自使用一个try-catch,防止某一个appender创建失败而导致其他的appender也同样创建失败
		try {
			// 如果日志控制台输出可用,则 创建 ConsoleAppender对象 添加到appenderList容器当中
			if (settings.getAppenders().getConsole().isEnabled())
				appenderAttachable.addAppender(new ConsoleAppender());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// 如果日志文件可用,则 创建 TextFileAppender对象 添加到appenderList容器当中
			if (settings.getAppenders().getText().isEnabled())
				appenderAttachable.addAppender(new TextFileAppender(settings.getAppenders().getText().getPath()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// 如果日志记录到数据库启用,则 创建 JdbcAppender对象 添加到appenderList容器当中
			if (settings.getAppenders().getDatabase().isEnabled()) {
				DatabaseAppenderSetting dataBase = settings.getAppenders().getDatabase();
				appenderAttachable.addAppender(new JdbcAppender(dataBase.getUrl(), dataBase.getDriver(), dataBase
						.getUser(), dataBase.getPassword(), dataBase.getDatabase(), dataBase.getTable()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			// 如果日志记录到数据库启用,则 创建 JdbcAppender对象 添加到appenderList容器当中
			if (settings.getAppenders().getCapture().isEnabled()) {
				CaptureAppenderSetting capture = settings.getAppenders().getCapture();
				captureAttachable.addAppender(new CaptureAppender(capture.getPath(), capture.isEnabled(), capture.getClassPath()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置缓存的启用与否
	 * 
	 * @param cache
	 */
	public void setEnableCache(CacheSettings cache) {
		this.isEnableCache = cache.isEnabled();
		if (isEnableCache) {
			// 如果设置缓存了，就初始化一个有缓存特性的队列
			appenderAttachable.initCacheQueue(cache.getBatchCount(), cache.getLazyMs());
			captureAttachable.initCacheQueue(cache.getBatchCount(), cache.getLazyMs());
		} else {
			// 如果没有启用缓存，那么初始化一个可以及时响应时间的队列，但是此队列也要有一定的长度限制，否则无限的膨胀会造成内存溢出
			appenderAttachable.initSyncQueue();
			captureAttachable.initSyncQueue();
		}
	}

	public void setFilter(Logger logger, Map<String, FilterSetting> filterMap) {
		if (filterMap != null && filterMap.size() > 0) {
			Filter filter = null;
			for (String key : filterMap.keySet()) {
				FilterSetting filterSetting = filterMap.get(key);
				String filterClass = filterSetting.getFieldValue("class");
				if (filter == null) {
					filter = ClassUtils.newClassInstance(Filter.class, filterClass);
					if (filterSetting.getLevel() != null && filterSetting.getLevel().length() > 0) {
						filter.setLevel(LogLevel.valueOf(filterSetting.getLevel().toUpperCase()));
					}
				} else {
					Filter filterTemp = ClassUtils.newClassInstance(Filter.class, filterClass);
					if (filterSetting.getLevel() != null && filterSetting.getLevel().length() > 0) {
						filterTemp.setLevel(LogLevel.valueOf(filterSetting.getLevel().toUpperCase()));
					}
					filter.setNextFilter(filterTemp);
				}
			}
			logger.setFilter(filter);
		}
	}

	/**
	 * 得到管理Appender的对象
	 * 
	 * @return
	 */
	public AppenderAttachable getAppenderAttachable() {
		return appenderAttachable;
	}

	/**
	 * 得到管理captureAppender的对象
	 * 
	 * @return
	 */
	public AppenderAttachable getCaptureAttachable() {
		return captureAttachable;
	}
}
