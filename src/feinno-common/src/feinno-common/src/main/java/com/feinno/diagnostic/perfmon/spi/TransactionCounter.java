/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.spi;

import java.util.concurrent.atomic.AtomicLong;

import com.feinno.diagnostic.observation.ObserverReportSnapshot;
import com.feinno.diagnostic.observation.ObserverReportUnit;
import com.feinno.diagnostic.perfmon.CounterEntity;
import com.feinno.diagnostic.perfmon.SmartCounter;
import com.feinno.diagnostic.perfmon.Stopwatch;

/**
 * 
 * 一个用于追踪Transaction的计数器
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class TransactionCounter extends CounterEntity implements SmartCounter, Stopwatch.Watchable
{
	private AtomicLong fired;
	private AtomicLong successed;
	private AtomicLong failed;
	private AtomicLong totalCost;
	private String lastError;
	private long lastErrorNanos;
	
	public TransactionCounter()
	{
		fired = new AtomicLong();
		successed = new AtomicLong();
		failed = new AtomicLong();
		totalCost = new AtomicLong();
		lastError = null;
	}
	
	public int getConcurrent()
	{
		return (int)(fired.get() - successed.get() - failed.get());
	}

	@Override
	public void reset()
	{
		fired.set(0);
		successed.set(0);
		failed.set(0);
		totalCost.set(0);
		lastError = null;
	}
	
	@Override
	public Stopwatch begin()
	{
		fired.incrementAndGet();
		return new Stopwatch(this);
	}

	@Override
	public void end(long nanos)
	{
		successed.incrementAndGet();
		totalCost.addAndGet(nanos);
	}

	@Override
	public void fail(long nanos, String message)
	{
		failed.incrementAndGet();
		totalCost.addAndGet(nanos);
		if (message != null) {
			lastError = message;
			lastErrorNanos = System.nanoTime();
		}
	}

	@Override
	public void fail(long nanos, Throwable error)
	{
		failed.incrementAndGet();
		totalCost.addAndGet(nanos);
		if (error != null) {
			lastError = error.getMessage();
			lastErrorNanos = System.nanoTime();
		}
	}

	@Override
	public ObserverReportSnapshot getObserverSnapshot()
	{
		long total = successed.longValue() + failed.longValue();
		return new TransactionCounterSnapshot(fired.longValue(), total, failed.longValue(), totalCost.longValue(), lastError, lastErrorNanos);
	}

	@Override
	public ObserverReportUnit getEmptyReport()
	{
		return new TransactionCounterReportUnit(0, 0, 0, 0, 0, null);
	}
	
	@Override
	public String toString() {
		return String.format("fired=%s,successed=%s,failed=%s,totalCost=%s,lastError=%s", fired != null ? fired.get()
				: "", successed != null ? successed.get() : "", failed != null ? failed.get() : "",
				totalCost != null ? totalCost.get() : "", lastError != null ? lastError : "");
	}
}