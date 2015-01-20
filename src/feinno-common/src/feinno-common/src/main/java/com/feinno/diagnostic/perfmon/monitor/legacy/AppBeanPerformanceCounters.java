/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-1-4
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor.legacy;

import com.feinno.diagnostic.perfmon.PerformanceCounter;
import com.feinno.diagnostic.perfmon.PerformanceCounterCategory;
import com.feinno.diagnostic.perfmon.PerformanceCounterType;
import com.feinno.diagnostic.perfmon.SmartCounter;

/**
 * 临时实现
 * TODO: 用正式方法代替
 * 
 * @author 高磊 gaolei@feinno.com
 */
@PerformanceCounterCategory("apps")
public class AppBeanPerformanceCounters
{
	@PerformanceCounter(name = "tx", type = PerformanceCounterType.TRANSACTION)
	private SmartCounter tx;

	public void setTx(SmartCounter tx)
	{
		this.tx = tx;
	}

	public SmartCounter getTx()
	{
		return tx;
	}
}
