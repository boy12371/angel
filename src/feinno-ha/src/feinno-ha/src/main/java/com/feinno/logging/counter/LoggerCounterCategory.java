package com.feinno.logging.counter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObservableUnit;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.diagnostic.observation.ObserverReportColumnType;

public class LoggerCounterCategory implements Observable
{
	public static LoggerCounterCategory INSTANCE;
	
	private Map<String, LoggerCounter> counters;
	private List<ObserverReportColumn> columns;

	static {
		INSTANCE = new LoggerCounterCategory();
		
		List<ObserverReportColumn> columns = new ArrayList<ObserverReportColumn>();
		columns.add(new ObserverReportColumn("warning", ObserverReportColumnType.LONG));
		columns.add(new ObserverReportColumn("error", ObserverReportColumnType.LONG));
		columns.add(new ObserverReportColumn("last_exception", ObserverReportColumnType.TEXT));
		INSTANCE.columns = columns;
		
		ObserverManager.register(INSTANCE);
	}

	private LoggerCounterCategory()
	{
		counters = Collections.synchronizedMap(new HashMap<String, LoggerCounter>());
	}

	@Override
	public List<ObserverReportColumn> getObserverColumns()
	{
		return columns;
	}

	@Override
	public String getObserverName()
	{
		return "logging";
	}	
	
	@Override
	public List<ObservableUnit> getObserverUnits()
	{
		List<ObservableUnit> ret = new ArrayList<ObservableUnit>();
		synchronized (ret) {
			for (LoggerCounter i : INSTANCE.counters.values()) {
				ret.add(i);
			}
		}
		return ret;
	}

	public static LoggerCounter getLoggerCounter(String name)
	{
		LoggerCounter ret = INSTANCE.counters.get(name);
		if (ret == null) {
			ret = new LoggerCounter(name);
			INSTANCE.counters.put(name, ret);
		}
		return ret;
	}
}
