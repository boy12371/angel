/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Nov 16, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObservableUnit;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReportColumn;

/**
 * 用于生成SessionExecutorCOunter并提供监控页面
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class SessionExecutorCounterFactory implements Observable
{
	public static final SessionExecutorCounterFactory INSTANCE = new SessionExecutorCounterFactory();
	
	private Map<String, SessionExecutorCounter> counters;
	
	private SessionExecutorCounterFactory()
	{
		counters = new Hashtable<String, SessionExecutorCounter>();
		ObserverManager.register(this);
	}

	public SessionExecutorCounter getCounter(String instance)
	{
		SessionExecutorCounter executor = counters.get(instance);
		if (executor == null) {
			executor = new SessionExecutorCounter(instance);
			counters.put(instance, executor);
		}
		return executor;
	}
	
	@Override
	public String getObserverName()
	{
		return "sexec";
	}

	@Override
	public List<ObserverReportColumn> getObserverColumns()
	{
		return SessionExecutorCounterReportUnit.getColumns();
	}

	@Override
	public List<ObservableUnit> getObserverUnits()
	{
		throw new UnsupportedOperationException("没实现呢");
	}

}
