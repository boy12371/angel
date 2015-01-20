/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Nov 9, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.database;

import com.feinno.diagnostic.perfmon.PerformanceCounter;
import com.feinno.diagnostic.perfmon.PerformanceCounterCategory;
import com.feinno.diagnostic.perfmon.PerformanceCounterFactory;
import com.feinno.diagnostic.perfmon.PerformanceCounterType;
import com.feinno.diagnostic.perfmon.SmartCounter;

/**
 * <b>描述：</b>数据库性能计数器
 * <p>
 * <b>功能：</b>对数据库访问的频率、次数、进行计数。参考{@link PerformanceCounterFactory}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class DatabasePerfmon
{
	@PerformanceCounterCategory("database-sp")
	public static class Counter
	{
		@PerformanceCounter(name = "tx", type = PerformanceCounterType.TRANSACTION)
		private SmartCounter tx;

		public SmartCounter getTx()
		{
			return tx;
		}

		public void setTx(SmartCounter tx)
		{
			this.tx = tx;
		}
	}

	public static void initialize()
	{
		PerformanceCounterFactory.getCounters(Counter.class, "");
	}
	
	public static SmartCounter getCounter(String name)
	{
		Counter c = PerformanceCounterFactory.getCounters(Counter.class, name);
		return c.getTx();
	}
}
