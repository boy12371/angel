/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei May 15, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.monitor;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAObserverDeployment extends ProtoEntity
{
	@ProtoMember(1)
	private String observerName;
	
	@ProtoMember(2)
	private String reportMode;
	
	@ProtoMember(3)
	private int interval;
	
	@ProtoMember(4)
	private boolean upload;
	
	@ProtoMember(5)
	private String thresholdExpr;

	public String getObserverName()
	{
		return observerName;
	}

	public void setObserverName(String observerName)
	{
		this.observerName = observerName;
	}

	public String getReportMode()
	{
		return reportMode;
	}

	public void setReportMode(String reportMode)
	{
		this.reportMode = reportMode;
	}

	public int getInterval()
	{
		return interval;
	}

	public void setInterval(int interval)
	{
		this.interval = interval;
	}

	public boolean isUpload()
	{
		return upload;
	}

	public void setUpload(boolean upload)
	{
		this.upload = upload;
	}

	public String getThresholdExpr()
	{
		return thresholdExpr;
	}

	public void setThresholdExpr(String thresholdExpr)
	{
		this.thresholdExpr = thresholdExpr;
	}
}
