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

/**
 * 用于记录命中率的计数器
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RatioCounter extends CounterEntity implements SmartCounter
{
	private AtomicLong hitted;
	private AtomicLong missed;
	
	public RatioCounter()
	{
		missed = new AtomicLong();
		hitted = new AtomicLong();
	}

	@Override
	public void reset()
	{
		missed.set(0);
		hitted.set(0);
	}

	@Override
	public void increase()
	{
		this.hitted.incrementAndGet();
	}

	@Override
	public void increaseBy(long value)
	{
		this.hitted.addAndGet(value);
	}

	@Override
	public void increaseRatio(boolean hitted)
	{
		if (hitted) {
			this.hitted.incrementAndGet();
		} else {
			this.missed.incrementAndGet();
		}
	}

	@Override
	public ObserverReportSnapshot getObserverSnapshot()
	{
		return new RatioCounterSnapshot(hitted.longValue(), missed.longValue());
	}

	@Override
	public ObserverReportUnit getEmptyReport()
	{
		return new RatioCounterReportUnit(0, 0, 1);
	}	
}
