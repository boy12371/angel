/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Nov 15, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.diagnostic.observation.ObserverReportColumnType;
import com.feinno.diagnostic.observation.ObserverReportSnapshot;
import com.feinno.diagnostic.observation.ObserverReportUnit;

/**
 * 
 * qps:	请求数/每秒<br>
 * concurrent:	并发<br>
 * failed: 失败次数<br>
 * elapsesMs: 平均延时<br>
 * maxElapseMs: 最大延时<br>
 * costMs: 真实线程消耗<br> 
 * maxCostMs: 真实线程消耗最大延时<br> 
 * lastException: 上次错误<br>
 * @author 高磊 gaolei@feinno.com
 */
public class SessionExecutorCounterSnapshot extends ObserverReportSnapshot
{
	private String instance;
	private int concurrent;
	private int sessions;
	private long total;
	private long failed;
	private long elapseNanos;
	private long costNanos;
	private String lastError;
	
	public SessionExecutorCounterSnapshot(String instance, int concurrent, long fired, long successed, long failed,
			long elapseNanos, long costNanos, String lastError)
	{
		this.instance = instance;
		this.concurrent = concurrent;
		this.sessions = (int)(fired - successed - failed);
		this.total = successed + failed;
		this.failed = failed;
		this.elapseNanos = elapseNanos;
		this.costNanos = costNanos;
		this.lastError = lastError;
	}

	@Override
	public ObserverReportUnit computeReport(ObserverReportSnapshot last)
	{
		// return new SessionExecutorCounterSnapshot();
		return null;
	}
}
