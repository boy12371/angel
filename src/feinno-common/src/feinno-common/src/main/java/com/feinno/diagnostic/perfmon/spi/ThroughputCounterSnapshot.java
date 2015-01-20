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
public class ThroughputCounterSnapshot extends ObserverReportSnapshot
{
	private long throughput;
	private long times;
	
	public ThroughputCounterSnapshot(long throughput, long times)
	{
		super();
		this.throughput = throughput;
		this.times = times;
	}
	
	public long getThroughput()
	{
		return throughput;
	}

	public long getTimes()
	{
		return times;
	}

	@Override
	public ObserverReportUnit computeReport(ObserverReportSnapshot obj)
	{
		ThroughputCounterSnapshot last = (ThroughputCounterSnapshot)obj;
		long nanos = this.getNanos() - last.getNanos();
		long tr = throughput - last.throughput;
		long ti = times - last.times;
		
		return new ThroughputCounterReportUnit(tr, ti, nanos);
	}
}
