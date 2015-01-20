/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.logging.counter;

import com.feinno.diagnostic.observation.ObserverReportSnapshot;

import com.feinno.diagnostic.observation.ObserverReportUnit;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class LoggerCounterSnapshot extends ObserverReportSnapshot {

	private long warn;

	private long error;

	private String lastExecption;

	public LoggerCounterSnapshot(long warn, long error, String lastExecption) {
		this.warn = warn;
		this.error = error;
		this.lastExecption = lastExecption;
	}

	@Override
	public ObserverReportUnit computeReport(ObserverReportSnapshot last) {
		LoggerCounterSnapshot loggerCounterSnapshot = (LoggerCounterSnapshot) last;
		LoggerCounterReportUnit loggerCounterReportUnit = new LoggerCounterReportUnit(
				loggerCounterSnapshot.warn - warn, loggerCounterSnapshot.error - error, loggerCounterSnapshot.error
						- error > 0 ? loggerCounterSnapshot.lastExecption : lastExecption);
		return loggerCounterReportUnit;
	}
}
