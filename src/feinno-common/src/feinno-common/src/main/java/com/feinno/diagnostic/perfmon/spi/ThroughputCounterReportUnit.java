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

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ThroughputCounterReportUnit implements ObserverReportUnit
{
	private long throughput;
	private long times;
	private long nanos;
	
	public ThroughputCounterReportUnit(long throughput, long times, long nanos)
	{
		super();
		this.throughput = throughput;
		this.times = times;
		this.nanos = nanos;
	}

	@Override
	public String toString()
	{
		double tr = (double) throughput * 1E9 / nanos;
		double ti = (double) times  * 1E9 / nanos;
		return String.format("%f %f", tr, ti);
	}

	@Override
	public void outputReport(ObserverReportRow row)
	{
		double tr = (double) throughput * 1E9 / nanos;
		double ti = (double) times  * 1E9 / nanos;		
		row.output(Double.toString(tr));
		row.output(Double.toString(ti));
	}

	@Override
	public ObserverReportUnit summaryAll(List<ObserverReportUnit> items)
	{
		long stp = 0;
		long sti = 0;
		
		for (ObserverReportUnit a: items) {
			ThroughputCounterReportUnit u = (ThroughputCounterReportUnit)a;
			stp += u.throughput;
			sti += u.times;
		}
		
		return new RatioCounterReportUnit(stp, sti, nanos);
	}
	
	public static ObserverReportColumn[] getColumns(String name)
	{
		return new ObserverReportColumn[] {
				new ObserverReportColumn(name + "(bytes/sec)", ObserverReportColumnType.DOUBLE),
				new ObserverReportColumn(name + "(/sec)", ObserverReportColumnType.DOUBLE),
		};	
	}
}
