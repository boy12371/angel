/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 23, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.center;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.database.HADBStatusHelper;
import com.feinno.ha.interfaces.center.HACenterConsoleService;
import com.feinno.ha.interfaces.center.HAConsoleOperationArgs;
import com.feinno.ha.interfaces.center.HAMasterEndpoint;
import com.feinno.ha.interfaces.center.HAWorkerEndpoint;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class CenterConsoleService implements HACenterConsoleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CenterConsoleService.class);

	/**
	 * 通过serverName获得HAMasterEndpoint
	 */
	public HAMasterEndpoint getMasterEndpoint(String serverName) {
		if (serverName == null || serverName.length() == 0) {
			throw new RuntimeException("ServerName empty.");
		}
		LOGGER.info("getMasterEndpoint. ServerName : {} ", serverName);
		try {
			return HADBStatusHelper.getHAMasterEndpoint(serverName);
		} catch (SQLException e) {
			LOGGER.error(String.format("getMasterEndpoint failed, ServerName : %s ", serverName), e);
			throw new RuntimeException(String.format("getMasterEndpoint failed, ServerName : %s ", serverName), e);
		}
	}

	/**
	 * 通过serverName或serviceName获取HAWorkerEndpoint
	 */
	public HAWorkerEndpoint[] getWorkerEndpoints(HAConsoleOperationArgs args) {
		String serverName = args.getServerName();
		String serviceName = args.getServiceName();
		if ((serverName == null || serverName.length() == 0) && (serviceName == null || serviceName.length() == 0)) {
			throw new RuntimeException("ServerName and serviceName can not be both empty");
		}
		LOGGER.info("getWorkerEndpoints. ServerName : {},serviceName : {} ", serverName, serviceName);
		try {
			return HADBStatusHelper.getHAWorkerEndpointArray(serverName, serviceName);
		} catch (SQLException e) {
			LOGGER.error(String.format("getWorkerEndpoints failed, ServerName : %s , serviceName : %s", serverName,
					serviceName), e);
			throw new RuntimeException(String.format("getWorkerEndpoints failed, ServerName : %s , serviceName : %s",
					serverName, serviceName), e);
		}
	}
}
