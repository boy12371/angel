/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.logging.counter;

import java.util.List;


import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.diagnostic.observation.ObserverReportColumnType;
import com.feinno.diagnostic.observation.ObserverReportRow;
import com.feinno.diagnostic.observation.ObserverReportUnit;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class LoggerCounterReportUnit implements ObserverReportUnit {
	public static ObserverReportColumn[] getColumns() {
		return new ObserverReportColumn[] { new ObserverReportColumn("warn", ObserverReportColumnType.LONG),
				new ObserverReportColumn("error", ObserverReportColumnType.LONG),
				new ObserverReportColumn("lastException", ObserverReportColumnType.TEXT) };
	}

	private long warn;
	private long error;
	private String lastException;

	public LoggerCounterReportUnit(long warn, long error, String lastException) {
		this.warn = warn;
		this.error = error;
		this.lastException = lastException;
	}

	@Override
	public String toString() {
		return String.format("warn %d,error %d,lastException %s", warn, error, lastException);
	}

	@Override
	public void outputReport(ObserverReportRow row) {
		row.output(Long.toString(warn));
		row.output(Long.toString(error));
		row.output(lastException);
	}

	@Override
	public ObserverReportUnit summaryAll(List<ObserverReportUnit> units) {
		long warn = 0;
		long error = 0;
		String lastException = null;
		for (ObserverReportUnit a : units) {
			LoggerCounterReportUnit u = (LoggerCounterReportUnit) a;
			warn += u.warn;
			error += u.error;
			lastException = u.lastException;
		}
		return new LoggerCounterReportUnit(warn, error, lastException);
	}

}
