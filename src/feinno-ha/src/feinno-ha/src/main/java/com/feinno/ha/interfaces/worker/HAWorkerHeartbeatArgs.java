package com.feinno.ha.interfaces.worker;

import java.util.List;

import com.feinno.diagnostic.observation.ObserverReportEntity;
import com.feinno.ha.interfaces.monitor.HAMonitorValuePair;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * 
 * Worke心跳包
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAWorkerHeartbeatArgs extends ProtoEntity
{
	@ProtoMember(value = 1, required = true)
	private String status;
	
	@ProtoMember(value = 2, required = true)
	private String statusEx;
	
	@ProtoMember(3)
	private List<HAMonitorValuePair> valuePairs;
	
	@ProtoMember(4)
	private List<ObserverReportEntity> observerReports;

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getStatusEx()
	{
		return statusEx;
	}

	public void setStatusEx(String statusEx)
	{
		this.statusEx = statusEx;
	}

	public List<HAMonitorValuePair> getValuePairs()
	{
		return valuePairs;
	}

	public void setValuePairs(List<HAMonitorValuePair> valuePairs)
	{
		this.valuePairs = valuePairs;
	}

	public List<ObserverReportEntity> getObserverReports()
	{
		return observerReports;
	}

	public void setObserverReports(List<ObserverReportEntity> observerReports)
	{
		this.observerReports = observerReports;
	}
}
