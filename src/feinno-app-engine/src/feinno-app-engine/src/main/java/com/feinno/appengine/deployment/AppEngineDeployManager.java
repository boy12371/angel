/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Aug 24, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.deployment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.feinno.ha.deployment.HADeployment;
import com.feinno.ha.deployment.HATask;

/**
 * FAE部署管理器,用于合并不同AppBean之间针对HA_Deployment的相同部署请求
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineDeployManager
{
	public static final AppEngineDeployManager INSTANCE = new AppEngineDeployManager();

	private Map<String, HADeployment> deployments;
	
	private AppEngineDeployManager()
	{
		deployments = new Hashtable<String, HADeployment>();
	}
	
	public HADeployment saveDeployment(String serviceName,int packageId, String serverGroup) throws SQLException
	{
		HADeployment deployment = deployments.get(serviceName);
		//对于是否存在这个deployment都要进行workerDeployment信息的保存，无则新增，有则改之
		AppEngineDatabaseHelper.saveDeployment(serviceName, packageId, serverGroup);
		if (deployment != null) {
			return deployment;
		} else {
			deployment = new HADeployment(serviceName, serverGroup);
			deployments.put(serviceName, deployment);
		}
		
		return deployment;
	}
	
	public HADeployment getDeployment(String serviceName, String serverGroup)
	{
		if (serviceName == null) {
			return null;
		}
		HADeployment deployment = deployments.get(serviceName);
		if (deployment == null) {
			deployment = new HADeployment(serviceName, serverGroup);
			deployments.put(serviceName, deployment);
		}
		return deployment;
	}
	
	public List<HATask> getTasks()
	{
		List<HATask> ret = new ArrayList<HATask>();
		//TODO
		return ret;
	}
}
