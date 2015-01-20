/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei May 15, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.master;

import java.util.List;

import com.feinno.ha.interfaces.monitor.HAMonitorDeployment;
import com.feinno.ha.interfaces.monitor.HAMonitorKey;
import com.feinno.ha.interfaces.monitor.HAMonitorScript;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAMasterMonitorConfig extends ProtoEntity
{
	@ProtoMember(1)
	private List<HAMonitorKey> keys;

	@ProtoMember(2)
	private List<HAMonitorScript> scripts;

	@ProtoMember(3)
	private List<HAMonitorDeployment> deployments;

	public List<HAMonitorKey> getKeys()
	{
		return keys;
	}

	public void setKeys(List<HAMonitorKey> keys)
	{
		this.keys = keys;
	}

	public List<HAMonitorScript> getScripts()
	{
		return scripts;
	}

	public void setScripts(List<HAMonitorScript> scripts)
	{
		this.scripts = scripts;
	}

	public List<HAMonitorDeployment> getDeployments()
	{
		return deployments;
	}

	public void setDeployments(List<HAMonitorDeployment> deployments)
	{
		this.deployments = deployments;
	}
}
