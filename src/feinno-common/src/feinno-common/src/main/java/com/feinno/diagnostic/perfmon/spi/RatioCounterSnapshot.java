/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.spi;

import com.feinno.diagnostic.observation.ObserverReportSnapshot;
import com.feinno.diagnostic.observation.ObserverReportUnit;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RatioCounterSnapshot extends ObserverReportSnapshot
{
	private long hitted;
	private long missed;

	public long getHitted()
	{
		return hitted;
	}

	public long getMissed()
	{
		return missed;
	}

	public RatioCounterSnapshot(long hitted, long missed)
	{
		this.hitted = hitted;
		this.missed = missed;
	}

	@Override
	public ObserverReportUnit computeReport(ObserverReportSnapshot obj)
	{
		RatioCounterSnapshot last = (RatioCounterSnapshot)obj;
		
		long hitted = this.hitted - last.hitted;
		long total = hitted + this.missed - last.missed;
		long nanos = this.getNanos() - last.getNanos();
		
		return new RatioCounterReportUnit(hitted, total, nanos);
	}
}
