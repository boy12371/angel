/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-5-17
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.worker;

import java.util.List;

import com.feinno.ha.interfaces.monitor.HAMonitorDeployment;
import com.feinno.ha.interfaces.monitor.HAMonitorKey;
import com.feinno.ha.interfaces.monitor.HAMonitorScript;
import com.feinno.ha.interfaces.monitor.HAObserverDeployment;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * HAWorker的监控配置
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAWorkerMonitorConfig extends ProtoEntity
{
	@ProtoMember(1)
	private List<HAMonitorKey> keys;		// HA_MonitorKeyConfig

	@ProtoMember(2)
	private List<HAMonitorScript> scripts;	// HA_MonitorScript

	@ProtoMember(3)
	private List<HAMonitorDeployment> deployments;	// HA_MonitorDeploy,ents
	
	@ProtoMember(4)
	private List<HAObserverDeployment> observers;	// HA_ObserverDeployment

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

	public List<HAObserverDeployment> getObservers()
	{
		return observers;
	}

	public void setObservers(List<HAObserverDeployment> observers)
	{
		this.observers = observers;
	}	
}
