/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Aug 3, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.center;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.database.HADBDeploymentHelper;
import com.feinno.ha.database.HADBServerHelper;
import com.feinno.ha.interfaces.center.HACenterDeploymentService;
import com.feinno.ha.interfaces.center.HAPackageInfo;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class CenterDeploymentService implements HACenterDeploymentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CenterDeploymentService.class);

	public String[] getServerGroups(String serverName) {
		try {
			List<String> groupList = HADBServerHelper.getServerGroupList(serverName);
			String[] groupArray = new String[groupList.size()];
			return groupList.toArray(groupArray);
		} catch (SQLException e) {
			LOGGER.error(String.format("%s getServerGroups failed.", serverName), e);
			throw new RuntimeException(String.format("%s getServerGroups failed.", serverName), e);
		}

	}

	public HAPackageInfo getPackageInfo(int packageId) {
		try {
			return HADBDeploymentHelper.getSimpleWorkerPackageInfo(packageId);
		} catch (SQLException e) {
			LOGGER.error(String.format("%s getPackageInfo failed.", packageId), e);
			throw new RuntimeException(String.format("%s getPackageInfo failed.", packageId), e);
		}

	}
}
