/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 15, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigType;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.logging.LogEvent;
import com.feinno.logging.spi.LogManager;
import com.feinno.util.PropertiesUtil;

/**
 * 
 * 服务启动代理
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class WorkerAgent {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkerAgent.class);

	private ServiceComponent comp;

	public ServiceComponent getServiceComponent() throws Exception {
		if (comp == null) {
			String compType = ServiceSettings.INSTANCE.getWorkerComponent();
			try {
				Class<?> clazz = Class.forName(compType);
				comp = (ServiceComponent) clazz.newInstance();
			} catch (Exception ex) {
				LOGGER.error("Load ServiceComponent failed {} {}", compType, ex);
			}
		}
		return comp;
	}

	/**
	 * 初始化日志组件
	 */
	protected boolean initLogging() {
		try {
			LogEvent.serviceName = ServiceSettings.INSTANCE.getServiceName();
			ConfigurationManager.loadText("logging.xml", null, new ConfigUpdateAction<String>() {
				@Override
				public void run(String str) throws Exception {
					if (str != null && str.trim().length() > 0) {
						LOGGER.info("Load logging config.");
						LogManager.loadSettings(PropertiesUtil.xmlToProperties(str));
					} else {
						LOGGER.info("logging.xml is empty.");
					}
				}
			});
			ConfigurationManager.subscribeConfigUpdate(ConfigType.TEXT, "logging.xml", null);
			return true;
		} catch (ConfigurationException e) {
			LOGGER.error("Initialization logging failed.", e);
			return false;
		}
	}

	public abstract void start() throws Exception;

	public abstract void stop() throws Exception;
}
