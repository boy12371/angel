/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Nov 15, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.feinno.diagnostic.observation.ObservableUnit;
import com.feinno.diagnostic.observation.ObserverReportSnapshot;
import com.feinno.diagnostic.perfmon.Stopwatch;
import com.mysql.jdbc.StringUtils;

/**
 * 
 * 用于记录SessionExecutor运行状况的计数器
 * <p>
 * </p>
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class SessionExecutorCounter implements ObservableUnit, Stopwatch.Watchable
{
	private String instance;
	private AtomicInteger concurrent;
	private AtomicLong fired;
	private AtomicLong successed;
	private AtomicLong failed;
	private AtomicLong elapseNanos;
	private AtomicLong costNanos;
	private String lastError;
	
	public SessionExecutorCounter(String instance)
	{
		this.instance = instance;
		concurrent = new AtomicInteger();
		fired = new AtomicLong();
		successed = new AtomicLong();
		failed = new AtomicLong();
		elapseNanos = new AtomicLong();
		costNanos = new AtomicLong();
		lastError = null;
	}
	
	public Stopwatch beginSession()
	{
		return new Stopwatch(this);
	}
	
	public Stopwatch beginThread()
	{
		concurrent.incrementAndGet();
		return new Stopwatch(new Stopwatch.Watchable() {
			@Override
			public void fail(long nanos, Throwable error)
			{
				concurrent.decrementAndGet();
				costNanos.addAndGet(nanos);
			}
			
			@Override
			public void fail(long nanos, String message)
			{
				concurrent.decrementAndGet();
				costNanos.addAndGet(nanos);
			}
			
			@Override
			public void end(long nanos)
			{
				concurrent.decrementAndGet();
				costNanos.addAndGet(nanos);
			}
		});	
	}
	
	
	@Override
	public ObserverReportSnapshot getObserverSnapshot()
	{
		return new SessionExecutorCounterSnapshot(
				instance,
				concurrent.intValue(),
				fired.longValue(),				
				successed.longValue(),
				failed.longValue(),
				elapseNanos.longValue(),
				costNanos.longValue(),
				lastError
				);
	}
	
	@Override
	public String getInstanceName()
	{
		return instance;
	}

	@Override
	public void end(long nanos)
	{
		elapseNanos.addAndGet(nanos);
		successed.incrementAndGet();
	}

	@Override
	public void fail(long nanos, String message)
	{
		elapseNanos.addAndGet(nanos);
		failed.incrementAndGet();
		if (!StringUtils.isNullOrEmpty(message)) {
			lastError = message;
		}
	}

	@Override
	public void fail(long nanos, Throwable error)
	{
		elapseNanos.addAndGet(nanos);
		failed.incrementAndGet();
		if (error != null) {
			lastError = error.getMessage();
		}
	}
}
