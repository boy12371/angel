/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-9
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.logging.configuration;

import java.util.HashMap;
import java.util.Map;

import com.feinno.util.ConfigBean;

/**
 * log日志配置环境参数类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class LogSettings extends ConfigBean {
	/**
	 * 日志缓存
	 */
	private CacheSettings cache = new CacheSettings();
	/**
	 * 日志输出级别
	 */
	private String level;

	/** 设置子日志配置项 */
	private Loggers loggers = new Loggers();

	private Map<String, FilterSetting> filter = new HashMap<String, FilterSetting>();

	/**
	 * 日志输出模板
	 */
	private AppenderSettings appenders = new AppenderSettings();

	public CacheSettings getCache() {
		return cache;
	}

	public String getLevel() {
		return level;
	}

	public AppenderSettings getAppenders() {
		return appenders;
	}

	public final Loggers getLoggers() {
		return loggers;
	}

	public final Map<String, FilterSetting> getFilter() {
		return filter;
	}

	// public static final String CONFIG_ROOT = System.getProperty("user.dir");
	// private static final Logger logger = Logger.getLogger(LogSettings.class);
	// public static void main(String args []){
	// Properties props = new Properties();
	// InputStream is = null;
	// try {
	// is = new FileInputStream(CONFIG_ROOT+"/logSetting.properties");
	// props.load(is);
	// LogManager.loadSettings(props);
	// } catch (FileNotFoundException e) {
	// logger.info("logSetting.properties load error "+e.getMessage());
	// } catch (IOException e) {
	// logger.info("logSetting.properties load error "+e.getMessage());
	// }
	//
	// }

}
