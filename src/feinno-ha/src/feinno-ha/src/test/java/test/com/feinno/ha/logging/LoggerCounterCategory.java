package test.com.feinno.ha.logging;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObservableUnit;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.diagnostic.observation.ObserverReportColumnType;

public class LoggerCounterCategory implements Observable {
	private static List<ObserverReportColumn> columns;
	private static LoggerCounterCategory instance;
	
	static {
		columns = new ArrayList<ObserverReportColumn>();
		columns.add(new ObserverReportColumn("warning", ObserverReportColumnType.LONG));
		columns.add(new ObserverReportColumn("error", ObserverReportColumnType.LONG));
		// columns.add(new ObserverReportColumn("last_exception", ObserverReportColumnType.TEXT));
		
		instance = new LoggerCounterCategory();
		ObserverManager.register(instance);
	}

	private Map<String, LoggerCounter> counters;
	
	private LoggerCounterCategory() {
		counters = new Hashtable<String, LoggerCounter>();
	}
	@Override
	public String getObserverName() {
		return "logger";
	}

	@Override
	public List<ObserverReportColumn> getObserverColumns() {
		return columns;
	}

	@Override
	public List<ObservableUnit> getObserverUnits() {
 
		return null;
	}
	
	public static LoggerCounter getLoggerCounter(String name)
	{
		LoggerCounter ret = instance.counters.get(name);
		if (ret == null) {
			ret = new LoggerCounter(name);
			instance.counters.put(name, ret);
		}
		return ret;
	}
}
