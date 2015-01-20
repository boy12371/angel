/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-10-26
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.worker;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAWorkerRegisterArgs extends ProtoEntity
{
	@ProtoMember(value = 1, required = true)
	private String serviceName;
	
	@ProtoMember(value = 2, required = true)
	private String serverName;
	
	@ProtoMember(value = 3, required = true)
	private int workerPid;
	
	@ProtoMember(value = 4, required = false)
	private String servicePorts;
	
	@ProtoMember(value = 5, required = false)
	private int deploymentId;

	public String getServiceName()
	{
		return serviceName;
	}

	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	public String getServerName()
	{
		return serverName;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	public int getWorkerPid()
	{
		return workerPid;
	}

	public void setWorkerPid(int workerPid)
	{
		this.workerPid = workerPid;
	}

	public String getServicePorts()
	{
		return servicePorts;
	}

	public void setServicePorts(String servicePorts)
	{
		this.servicePorts = servicePorts;
	}

	public int getDeploymentId()
	{
		return deploymentId;
	}

	public void setDeploymentId(int deploymentId)
	{
		this.deploymentId = deploymentId;
	}
}
