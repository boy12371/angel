/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-20
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.diagnostic;

import com.feinno.diagnostic.perfmon.PerformanceCounter;
import com.feinno.diagnostic.perfmon.PerformanceCounterCategory;
import com.feinno.diagnostic.perfmon.PerformanceCounterType;
import com.feinno.diagnostic.perfmon.SmartCounter;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@PerformanceCounterCategory("sample")
public class SampleCounter
{
	@PerformanceCounter(name="number", type=PerformanceCounterType.NUMBER)
	private SmartCounter number;
	
	@PerformanceCounter(name="ratio", type=PerformanceCounterType.RATIO)
	private SmartCounter ratio;
	
	@PerformanceCounter(name="throughput", type=PerformanceCounterType.THROUGHPUT)
	private SmartCounter throughput;
	
	@PerformanceCounter(name="transaction", type=PerformanceCounterType.TRANSACTION)
	private SmartCounter transaction;

	public SmartCounter getNumber()
	{
		return number;
	}

	public void setNumber(SmartCounter number)
	{
		this.number = number;
	}

	public SmartCounter getRatio()
	{
		return ratio;
	}

	public void setRatio(SmartCounter ratio)
	{
		this.ratio = ratio;
	}

	public SmartCounter getThroughput()
	{
		return throughput;
	}

	public void setThroughput(SmartCounter throughput)
	{
		this.throughput = throughput;
	}

	public SmartCounter getTransaction()
	{
		return transaction;
	}

	public void setTransaction(SmartCounter transaction)
	{
		this.transaction = transaction;
	}
	
}
