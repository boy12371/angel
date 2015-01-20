/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-10-8
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.spi;

import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.diagnostic.perfmon.CounterBuilder;
import com.feinno.diagnostic.perfmon.CounterEntity;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ThroughputCounterBuilder extends CounterBuilder
{
	public static final ThroughputCounterBuilder INSTANCE = new ThroughputCounterBuilder();
	
	private ThroughputCounterBuilder()
	{
	}
	
	@Override
	public CounterEntity createCounter()
	{
		return new ThroughputCounter();
	}

	@Override
	public ObserverReportColumn[] getColumns(String name)
	{
		return ThroughputCounterReportUnit.getColumns(name);
	}
}
