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
import com.feinno.diagnostic.perfmon.Stopwatch;

/**
 * 
 * 记录简单整数的计数器
 *  
 * @author 高磊 gaolei@feinno.com
 */
public class NumberCounter extends CounterEntity
{
	private AtomicLong count;
	
	public NumberCounter()
	{
		super();
		count = new AtomicLong();
	}

	@Override
	public void reset()
	{
		count.set(0);
	}

	@Override
	public void increase()
	{
		count.incrementAndGet();
	}

	@Override
	public void decrease()
	{
		count.decrementAndGet();
	}

	@Override
	public void increaseBy(long value)
	{
		count.addAndGet(value);
	}

	@Override
	public void setRawValue(long value)
	{
		count.set(value);
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
		return new NumberCounterSnapshot(count.longValue());
	}

	@Override
	public ObserverReportUnit getEmptyReport()
	{
		return new NumberCounterReportUnit(0);
	}
}
