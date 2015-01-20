/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon;

import com.feinno.diagnostic.observation.ObservableUnit;
import com.feinno.diagnostic.observation.ObserverReportUnit;

/**
 * Counter实现的实体类
 * 提供getSnapshot接口用于记录值
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class CounterEntity implements SmartCounter, ObservableUnit
{
	@Override
	public String getInstanceName()
	{
		throw new UnsupportedOperationException("Abstract");
	}
	
	public CounterEntity()
	{
	}

	@Override
	public abstract void reset();

	public abstract ObserverReportUnit getEmptyReport();
	
	@Override
	public void increase()
	{
		throw new UnsupportedOperationException("NotSupportted");
	}

	@Override
	public void decrease()
	{
		throw new UnsupportedOperationException("NotSupportted");
	}

	@Override
	public void increaseBy(long value)
	{
		throw new UnsupportedOperationException("NotSupportted");
	}

	@Override
	public void setRawValue(long value)
	{
		throw new UnsupportedOperationException("NotSupportted");
	}

	@Override
	public void increaseRatio(boolean hitted)
	{
		throw new UnsupportedOperationException("NotSupportted");
	}

	@Override
	public Stopwatch begin()
	{
		throw new UnsupportedOperationException("NotSupportted");
	}
}
