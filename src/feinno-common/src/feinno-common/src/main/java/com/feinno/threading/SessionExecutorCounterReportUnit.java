/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Nov 15, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import java.util.ArrayList;
import java.util.List;

import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.diagnostic.observation.ObserverReportColumnType;
import com.feinno.diagnostic.observation.ObserverReportRow;
import com.feinno.diagnostic.observation.ObserverReportUnit;

/**
 * 
 * qps:	请求数/每秒<br>
 * concurrent:	并发<br>
 * failed: 失败次数<br>
 * elapsesMs: 平均延时<br>
 * maxElapseMs: 最大延时(TODO)<br>
 * costMs: 真实线程消耗<br> 
 * maxCostMs: 真实线程消耗最大延时<br> 
 * lastException: 上次错误<br>
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class SessionExecutorCounterReportUnit implements ObserverReportUnit
{
	private int sessions;
	private int concurrent;
	private long total;
	private long failed;
	private double avgElapseMs;
	private double avgCostMs;
	private String lastError;

	private SessionExecutorCounterReportUnit(int sessions, int concurrent, long total, long failed, double avgElapseMs, double avgCostMs, String lastError)
	{
		this.sessions = sessions;
		this.concurrent = concurrent;
		this.total = total;
		this.failed = failed;
		this.avgElapseMs = avgElapseMs;
		this.avgCostMs = avgCostMs;
		this.lastError = lastError;
	}
	
	@Override
	public void outputReport(ObserverReportRow row)
	{
		row.output(Integer.toString(sessions));
		row.output(Integer.toString(concurrent));
		row.output(Long.toString(total));
		row.output(Long.toString(failed));
		row.output(Double.toString(avgElapseMs));	// TODO double format
		row.output(Double.toString(avgCostMs));		// TODO double format
		row.output(lastError);
	}

	@Override
	public ObserverReportUnit summaryAll(List<ObserverReportUnit> items)
	{		
		for (ObserverReportUnit i: items) {
			SessionExecutorCounterReportUnit u = (SessionExecutorCounterReportUnit)i;
						
		}
		SessionExecutorCounterReportUnit sum = null; // new SessionExecutorCounterReportUnit();
		return sum;
	}
	
	public static List<ObserverReportColumn> getColumns()
	{
		List<ObserverReportColumn> cols = new ArrayList<ObserverReportColumn>();
		cols.add(new ObserverReportColumn("concurrent.", ObserverReportColumnType.DOUBLE));
		cols.add(new ObserverReportColumn("(concurrent.)", ObserverReportColumnType.LONG));
		cols.add(new ObserverReportColumn("(failed.)", ObserverReportColumnType.LONG));
		cols.add(new ObserverReportColumn("(cost ms.)", ObserverReportColumnType.DOUBLE));
		cols.add(new ObserverReportColumn("(error)", ObserverReportColumnType.TEXT));
		return cols;
	}
}
