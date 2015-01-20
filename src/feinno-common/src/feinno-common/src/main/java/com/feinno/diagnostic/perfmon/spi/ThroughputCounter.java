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
 * 用于记录吞吐量的计数器
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ThroughputCounter extends CounterEntity implements SmartCounter
{
	private AtomicLong throughtput;
	private AtomicLong times;
	
	public ThroughputCounter()
	{
		throughtput = new AtomicLong();
		times = new AtomicLong();
	}

	@Override
	public void increase()
	{
		throughtput.incrementAndGet();
		times.incrementAndGet();
	}

	@Override
	public void increaseBy(long value)
	{
		throughtput.addAndGet(value);
		times.incrementAndGet();
	}

	@Override
	public void reset()
	{
		throw new UnsupportedOperationException("没实现呢");
	}

	@Override
	public void decrease()
	{
		throw new UnsupportedOperationException("没实现呢");
	}

	@Override
	public void setRawValue(long value)
	{
		throw new UnsupportedOperationException("没实现呢");
	}

	@Override
	public void increaseRatio(boolean hitted)
	{
		throw new UnsupportedOperationException("没实现呢");
	}

	@Override
	public Stopwatch begin()
	{
		throw new UnsupportedOperationException("没实现呢");
	}

	@Override
	public ObserverReportSnapshot getObserverSnapshot()
	{
		return new ThroughputCounterSnapshot(throughtput.longValue(), times.longValue());
	}
	

	@Override
	public ObserverReportUnit getEmptyReport()
	{
		return new ThroughputCounterReportUnit(0, 0, 1);
	}	
}
