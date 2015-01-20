package com.feinno.logging.counter;

import java.util.List;

import com.feinno.diagnostic.observation.ObserverReport;
import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.util.DateTime;

public class LoggerCounterReport extends ObserverReport {

	public LoggerCounterReport(String category, List<ObserverReportColumn> columns, DateTime time) {
		super(category, columns, time);
	}
}
