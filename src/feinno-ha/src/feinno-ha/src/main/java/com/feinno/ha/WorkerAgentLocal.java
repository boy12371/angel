/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 15, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha;

import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.spi.LocalConfigurator;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class WorkerAgentLocal extends WorkerAgent {

	public WorkerAgentLocal() {
		ConfigurationManager.setConfigurator(new LocalConfigurator());
	}

	@Override
	public void start() throws Exception {
		// 1. 初始化日志
		initLogging();
		// 2. 启动
		getServiceComponent().start();
	}

	@Override
	public void stop() throws Exception {
		getServiceComponent().stop();
	}
}
