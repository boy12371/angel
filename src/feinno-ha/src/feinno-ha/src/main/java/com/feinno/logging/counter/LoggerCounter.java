/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-20
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.logging.counter;

import java.util.concurrent.atomic.AtomicLong;

import com.feinno.diagnostic.observation.ObserverReportSnapshot;
import com.feinno.diagnostic.observation.ObserverReportUnit;
import com.feinno.diagnostic.perfmon.CounterEntity;
import com.feinno.diagnostic.perfmon.Stopwatch;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class LoggerCounter extends CounterEntity
{
	private String name;
	private AtomicLong warn;
	private AtomicLong error;
	private String lastException;

	public LoggerCounter(String name)
	{
		super();
		this.name = name;
		warn = new AtomicLong();
		error = new AtomicLong();
		lastException = null;
	}

	@Override
	public void reset()
	{
		warn.set(0);
		error.set(0);
		lastException = null;
	}

	public void increaseWarn()
	{
		warn.incrementAndGet();
	}

	public void decreaseWarn()
	{
		warn.decrementAndGet();
	}

	public void increaseWarnBy(long value)
	{
		warn.addAndGet(value);
	}

	public void setRawWarnValue(long value)
	{
		warn.set(value);
	}

	public void increaseError()
	{
		error.incrementAndGet();
	}

	public void decreaseError()
	{
		error.decrementAndGet();
	}

	public void increaseErrorBy(long value)
	{
		error.addAndGet(value);
	}

	public void setRawErrorValue(long value)
	{
		error.set(value);
	}

	public void setLastException(String value)
	{
		this.lastException = value;
	}

	@Override
	public void increaseRatio(boolean hitted)
	{
		throw new UnsupportedOperationException("Invailed CounterType!");
	}

	@Override
	public Stopwatch begin()
	{
		throw new UnsupportedOperationException("Invailed CounterType!");
	}

	@Override
	public ObserverReportSnapshot getObserverSnapshot()
	{
		return new LoggerCounterSnapshot(warn.longValue(), error.longValue(), lastException);
	}

	@Override
	public ObserverReportUnit getEmptyReport()
	{
		return new LoggerCounterReportUnit(0, 0, null);
	}

	@Override
	public String getInstanceName()
	{
		return name; 
	}
}
