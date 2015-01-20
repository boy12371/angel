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
public class NumberCounterReportUnit implements ObserverReportUnit 
{
	public static ObserverReportColumn[] getColumns(String name)
	{
		return new ObserverReportColumn[] {
				new ObserverReportColumn(name, ObserverReportColumnType.LONG)
		};
	}	
	

	private long count;
	
	public long getCount()
	{
		return count;
	}
	
	public NumberCounterReportUnit(long count)
	{
		this.count = count;
	}

	@Override
	public String toString()
	{
		return String.format("%d", count);
	}
	
	@Override
	public void outputReport(ObserverReportRow row)
	{
		row.output(Long.toString(count));	
	}

	@Override
	public ObserverReportUnit summaryAll(List<ObserverReportUnit> units)
	{
		long sum = 0;
		for (ObserverReportUnit a: units) {
			NumberCounterReportUnit u = (NumberCounterReportUnit)a;
			sum += u.count;
		}
		return new NumberCounterReportUnit(sum);
	}

}
