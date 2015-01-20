/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-14
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import java.util.concurrent.Executor;

/**
 * 
 * <b>描述: </b>该类继承自{@link ObservableExecutor}且间接实现了{@link Executor}接口，且通过父类
 * {@link ObservableExecutor}扩展，使其拥有了仅允许同时执行固定任务量的功能
 * <p>
 * <b>功能: </b>一个具有同时执行固定任务量功能的{@link Executor}接口
 * <p>
 * <b>用法: </b>导出最终为Java标准的{@link Executor}接口，因此参照{@link Executor}接口使用
 * <p>
 * 
 * @author gaolei@feinno.com
 * @see Executor
 * @see ObservableExecutor
 */
public class FixedObservableExecutor extends ObservableExecutor
{
	private int limit;
	
	public FixedObservableExecutor(String name, Executor executor, int limit, int size)
	{
		super(name, executor);
		this.limit = limit;
		this.getSizeCounter().setRawValue(size);
	}
	
	@Override
	public void execute(Runnable command)
	{
		int concurrent = getWorkerCounter().getConcurrent();
		if (concurrent >= limit) {
			throw new ExecutorBusyException(ExecutorException.OVER_MAX_CONCURRENT+" limit:"+limit, this.getName());
		} else {
			super.execute(command);
		}
	}

	@Override
	public String toString() {
		return String.format("name=%s,limit=%s,counter={%s}", getName(), limit, getWorkerCounter());
	}
}
