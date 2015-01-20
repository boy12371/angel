package com.feinno.appengine.runtime.configuration;

import java.util.Date;
import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class RunningWorkerEntity extends ProtoEntity
{
	@ProtoMember(1)
	private String appWorkerId;

	@ProtoMember(2)
	private String serverName;

	@ProtoMember(3)
	private String serviceUrls;

	@ProtoMember(4)
	private Date lastTime;
	
	@ProtoMember(5)
	private List<Integer> appBeans;
	
	@ProtoMember(1001)
	private boolean isGlobal;

	public List<Integer> getAppBeans() {
		return appBeans;
	}

	public void setAppBeans(List<Integer> appBeans) {
		this.appBeans = appBeans;
	}

	public String getAppWorkerId()
	{
		return appWorkerId;
	}

	public void setAppWorkerId(String appWorkerId)
	{
		this.appWorkerId = appWorkerId;
	}

	public String getServerName()
	{
		return serverName;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	public String getServiceUrls()
	{
		return serviceUrls;
	}

	public void setServiceUrls(String serviceUrls)
	{
		this.serviceUrls = serviceUrls;
	}

	public Date getLastTime()
	{
		return lastTime;
	}

	public void setLastTime(Date lastTime)
	{
		this.lastTime = lastTime;
	}
	
	public boolean isGlobal() {
		return isGlobal;
	}

	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}

	public String getServiceUrl(String protocol)
	{
		for (String s : serviceUrls.split(";")) {
			s = s.trim();
			if (s.startsWith(protocol + "=")) {
				return s.substring(protocol.length() + 1);								
			}
		}
		return null;
	}

	public String show()
	{
		return "FAE_RunningWorker [appWorkerId=" + appWorkerId
				+ ", serverName=" + serverName + ", serviceUrls=" + serviceUrls
				+ ", lastTime=" + lastTime + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((appWorkerId == null) ? 0 : appWorkerId.hashCode());
		result = prime * result + ((lastTime == null) ? 0 : lastTime.hashCode());
		result = prime * result + ((serverName == null) ? 0 : serverName.hashCode());
		result = prime * result + ((serviceUrls == null) ? 0 : serviceUrls.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		
		if (this == obj)
			return true;
		
		if (getClass() != obj.getClass())
			return false;
		RunningWorkerEntity other = (RunningWorkerEntity) obj;
		if (appWorkerId == null) {
			if (other.appWorkerId != null)
				return false;
		} else if (!appWorkerId.equals(other.appWorkerId))
			return false;
		if (lastTime == null) {
			if (other.lastTime != null)
				return false;
		} else if (!lastTime.equals(other.lastTime))
			return false;
		if (serverName == null) {
			if (other.serverName != null)
				return false;
		} else if (!serverName.equals(other.serverName))
			return false;		
		if (serviceUrls == null) {
			if (other.serviceUrls != null)
				return false;
		} else if (!serviceUrls.equals(other.serviceUrls))
			return false;
		return true;
	}
}
