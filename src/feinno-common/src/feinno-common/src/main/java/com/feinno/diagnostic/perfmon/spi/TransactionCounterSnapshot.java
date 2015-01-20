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
public class TransactionCounterSnapshot extends ObserverReportSnapshot
{
	private long fired;
	private long total;
	private long failed;
	private long totalCost;
	private String lastError;
	private long lastErrorNanos;
	
	public long getFired()
	{
		return fired;
	}
	
	public long getTotal()
	{
		return total;
	}

	public long getFailed()
	{
		return failed;
	}

	public long getTotalCost()
	{
		return totalCost;
	}

	public String getLastError()
	{
		return lastError;
	}
	
	public long getLastErrorNanos()
	{
		return lastErrorNanos;
	}

	public TransactionCounterSnapshot(long fired, long total, long failed, long totalCost, String lastError, long lastErrorNanos)
	{
		super();
		this.fired = fired;
		this.total = total;
		this.failed = failed;
		this.totalCost = totalCost;
		this.lastError = lastError;
		this.lastErrorNanos = lastErrorNanos;
	}

	@Override
	public ObserverReportUnit computeReport(ObserverReportSnapshot obj)
	{
		TransactionCounterSnapshot last = (TransactionCounterSnapshot)obj;
		long cu = this.fired - this.total;
		long t = this.total - last.total;
		long f = this.failed - last.failed;
		long cost = this.totalCost - last.totalCost;
		long nanos = this.getNanos() - last.getNanos();
		
		String lerror = null;
		if (this.getLastErrorNanos() != last.getLastErrorNanos()) {
			lerror = this.lastError;
		}
		
		return new TransactionCounterReportUnit(t, f, cu, cost, nanos, lerror);
	}
}
