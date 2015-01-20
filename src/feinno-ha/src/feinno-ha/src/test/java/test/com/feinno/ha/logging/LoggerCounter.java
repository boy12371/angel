package test.com.feinno.ha.logging;

import java.util.concurrent.atomic.AtomicLong;

import com.feinno.diagnostic.observation.ObservableUnit;
import com.feinno.diagnostic.observation.ObserverReportSnapshot;

public class LoggerCounter implements ObservableUnit {
	private String name;
	private AtomicLong warn;
	private AtomicLong error;

	LoggerCounter(String name)
	{
		this.name = name;
	}
	
	@Override
	public ObserverReportSnapshot getObserverSnapshot() {
		return new LoggerCounterSnapshot(warn.longValue(), error.longValue());
	}
	
	public void increaseWarn()
	{
		warn.incrementAndGet();
	}
	
	public void increaseError()
	{
		error.incrementAndGet();
	}

	@Override
	public String getInstanceName() {
		return name;
	}
}
