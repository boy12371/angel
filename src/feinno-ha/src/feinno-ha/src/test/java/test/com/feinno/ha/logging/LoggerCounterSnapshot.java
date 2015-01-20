package test.com.feinno.ha.logging;

import java.util.concurrent.atomic.AtomicLong;

import com.feinno.diagnostic.observation.ObserverReportSnapshot;
import com.feinno.diagnostic.observation.ObserverReportUnit;

public class LoggerCounterSnapshot extends ObserverReportSnapshot {
	private long warn;
	private long error;
	@Override
	public ObserverReportUnit computeReport(ObserverReportSnapshot last) {
		// TODO Auto-generated method stub
		LoggerCounterSnapshot rval = (LoggerCounterSnapshot)last;
		return new LoggerCounterReportUnit(this.warn - rval.warn, this.error - rval.error);
	}
	
	public LoggerCounterSnapshot(long warn, long error) {
		super();
		this.warn = warn;
		this.error = error;
	}
	public long getWarn() {
		return warn;
	}
	public long getError() {
		return error;
	}

}
