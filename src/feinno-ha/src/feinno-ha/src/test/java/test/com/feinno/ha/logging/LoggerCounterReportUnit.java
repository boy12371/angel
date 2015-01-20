package test.com.feinno.ha.logging;

import java.util.List;

import com.feinno.diagnostic.observation.ObserverReportRow;
import com.feinno.diagnostic.observation.ObserverReportUnit;

public class LoggerCounterReportUnit implements ObserverReportUnit {
	private long warn;
	private long error;
	@Override
	public void outputReport(ObserverReportRow row) {
		// TODO Auto-generated method stub
		row.output(Long.toString(warn));
		row.output(Long.toString(error));
	}

	public LoggerCounterReportUnit(long warn, long error) {
		super();
		this.warn = warn;
		this.error = error;
	}

	public long getWarn() {
		return warn;
	}

	public void setWarn(long warn) {
		this.warn = warn;
	}

	public long getError() {
		return error;
	}

	public void setError(long error) {
		this.error = error;
	}

	@Override
	public ObserverReportUnit summaryAll(List<ObserverReportUnit> items) {
		// TODO Auto-generated method stub
		return null;
	}
}
