/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-14
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.diagnostic.perfmon.PerformanceCounterFactory;
import com.feinno.diagnostic.perfmon.Stopwatch;
import com.feinno.diagnostic.perfmon.spi.NumberCounter;
import com.feinno.diagnostic.perfmon.spi.TransactionCounter;
import com.feinno.util.TraceContext;

/**
 * 
 * <b>描述: </b>这是一个{@link Executor}的包装类，使用装饰者模式将{@link Executor}装饰起来，为其增加计数器功能
 * <p>
 * <b>功能: </b>使用装饰者模式将{@link Executor}装饰起来，为其增加计数器功能
 * <p>
 * <b>用法: </b>该类用于继承使用
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ObservableExecutor implements Executor
{
	private String name;
	private Executor innerExecutor;
	private NumberCounter sizeCounter;
	private TransactionCounter workerCounter;
	private Logger logger;
	
	public String getName()
	{
		return name;
	}
	
	protected TransactionCounter getWorkerCounter()
	{
		return workerCounter;
	}
	
	protected NumberCounter getSizeCounter()
	{
		return sizeCounter;
	}
	
	protected Logger getLogger()
	{
		return logger;
	}
	
	public ObservableExecutor(String name, Executor executor)
	{
		this.name = name;
		this.innerExecutor = executor;
		ExecutorCounterCategory c = PerformanceCounterFactory.getCounters(
				ExecutorCounterCategory.class, name);
		
		this.sizeCounter = (NumberCounter)c.getSizeCounter();
		this.workerCounter = (TransactionCounter)c.getWorkerCounter();
		this.logger = LoggerFactory.getLogger("com.feinno.threading.ThreadPool." + name);
	}

	@Override
	public void execute(final Runnable command)
	{
		final String currentTraceToken;
		if (TraceContext.GetContext() != null)
		{
			currentTraceToken = TraceContext.GetContext().toString();
		}
		else
		{
			currentTraceToken = null;
		}
		final Stopwatch watch = workerCounter.begin();
		innerExecutor.execute(new Runnable() {
			@Override
			public void run()
			{
				try {
					TraceContext.Clear();
					if (traceToken != null)
					{
						TraceContext.Apply(traceToken);
					}
					command.run();
					watch.end();
				} catch (Exception ex) {
					watch.fail(ex);
					logger.error("ThreadPool Error", ex);
				} catch (Error ex) {
					watch.fail(ex);
					logger.error("ThreadPool Exception", ex);
				}
			}
			
			private String traceToken = currentTraceToken;
		});
	}
}
