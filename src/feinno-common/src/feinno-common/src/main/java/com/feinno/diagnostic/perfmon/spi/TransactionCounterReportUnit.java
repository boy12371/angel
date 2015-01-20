/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.spi;

import java.util.List;

import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.diagnostic.observation.ObserverReportColumnType;
import com.feinno.diagnostic.observation.ObserverReportRow;
import com.feinno.diagnostic.observation.ObserverReportUnit;
import com.feinno.util.StringUtils;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class TransactionCounterReportUnit implements ObserverReportUnit 
{
	private long total;
	private long failed;
	private long cost;
	private long concurrent;
	private long nanos;
	private String error;
	
	public long getTotal()
	{
		return total;
	}

	public long getFailed()
	{
		return failed;
	}

	public long getConcurrent()
	{
		return concurrent;
	}

	public double getAvgCostNanos()
	{
		return (double)cost / total;
	}

	public double getPerSecond()
	{
		return (double)total * 1E9 / nanos;
	}

	public String getLastError()
	{
		return error;
	}

	public TransactionCounterReportUnit(long total, long failed, long concurrent, long cost, long nanos, String error)
	{
		this.total = total;
		this.failed = failed;
		this.concurrent = concurrent;
		this.cost = cost;
		this.nanos = nanos;
		this.error = error;
	}

	@Override
	public String toString()
	{
		return String.format("%f/sec.concurrent=%d total=%d(%d error) elapse=%fms error=%s",
				getPerSecond(), concurrent, total, failed, getAvgCostNanos() / 1E6, error);
	}

	@Override
	public void outputReport(ObserverReportRow row)
	{
		row.output(Double.toString(getPerSecond()));
		row.output(Long.toString(concurrent));
		row.output(Long.toString(failed));
		row.output(Double.toString(getAvgCostNanos() / 1E6));
		row.output(error);
	}

	@Override
	public ObserverReportUnit summaryAll(List<ObserverReportUnit> items)
	{
		long stotal = 0;
		long sfailed = 0;
		long sconcurrent = 0;
		long scost = 0;
		
		String error = null;
		for (ObserverReportUnit a: items) {
			TransactionCounterReportUnit u = (TransactionCounterReportUnit)a;
			stotal += u.total;
			sfailed += u.failed;
			sconcurrent += u.concurrent;
			scost += u.cost;
			if (error == null && !StringUtils.isNullOrEmpty(u.error)) {
				error = u.error;
			}
		}
		
		return new TransactionCounterReportUnit(stotal, sfailed, sconcurrent, scost, nanos, error);
	}	
	
	public static ObserverReportColumn[] getColumns(String name)
	{
		return new ObserverReportColumn[] {
				new ObserverReportColumn(name + "(/sec.)", ObserverReportColumnType.DOUBLE),
				new ObserverReportColumn(name + "(concurrent.)", ObserverReportColumnType.LONG),
				new ObserverReportColumn(name + "(failed.)", ObserverReportColumnType.LONG),
				new ObserverReportColumn(name + "(cost ms.)", ObserverReportColumnType.DOUBLE),
				new ObserverReportColumn(name + "(error)", ObserverReportColumnType.TEXT),
		};	
	}
}
