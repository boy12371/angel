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
public class NumberCounterSnapshot extends ObserverReportSnapshot
{
	private long count;

	public long getCount()
	{
		return count;
	}
	
	public NumberCounterSnapshot(long count)
	{
		this.count = count;
	}

	@Override
	public ObserverReportUnit computeReport(ObserverReportSnapshot last)
	{
		return new NumberCounterReportUnit(count);
	}
}
