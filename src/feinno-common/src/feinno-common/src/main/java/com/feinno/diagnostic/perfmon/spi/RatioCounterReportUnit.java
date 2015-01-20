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
public class RatioCounterReportUnit implements ObserverReportUnit
{
	private long hitted;
	private long total;
	private long nanos;
	
	public long getHitted()
	{
		return hitted;
	}

	public long getTotal()
	{
		return total;
	}

	public double getPerSecond()
	{
		if (nanos == 0) {
			return Double.NaN;
		} else {
			return (double)total * 1E9 / nanos;
		}
	}

	public double getRatio()
	{
		if (total == 0) {
			return 0.0f;
		} else {
			return (double)hitted / total;
		}
	}
	
	public RatioCounterReportUnit(long hitted, long total, long nanos)
	{
		super();
		this.hitted = hitted;
		this.total = total;
		this.nanos = nanos;
	}	

	@Override
	public void outputReport(ObserverReportRow row)
	{
		row.output(Long.toString(total));
		row.output(Double.toString(getRatio()));
	}

	@Override
	public ObserverReportUnit summaryAll(List<ObserverReportUnit> items)
	{
		long stotal = 0;
		long shitted = 0;
		
		for (ObserverReportUnit a: items) {
			RatioCounterReportUnit u = (RatioCounterReportUnit)a;
			stotal += u.total;
			shitted += u.hitted;
		}
		
		return new RatioCounterReportUnit(stotal, shitted, nanos);		
	}
	
	public static ObserverReportColumn[] getColumns(String name)
	{
		return new ObserverReportColumn[] {
				new ObserverReportColumn(name + "(total)", ObserverReportColumnType.LONG),
				new ObserverReportColumn(name + "(ratio)", ObserverReportColumnType.RATIO),
		};
	}
	
	public String toString()
	{
		return String.format("total %d ratio %f", total, getRatio());
	}
}
