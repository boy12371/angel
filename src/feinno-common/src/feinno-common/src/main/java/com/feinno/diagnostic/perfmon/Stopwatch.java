/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 自动开始的计时秒表, 精度到纳秒1E-9<br>
 *  
 * @author 高磊 gaolei@feinno.com
 */
public class Stopwatch
{
	public interface Watchable
	{
		void end(long nanos);
		void fail(long nanos, String message);
		void fail(long nanos, Throwable error);
	}
	
	private long begin;
	private AtomicBoolean isDone = new AtomicBoolean(false);
	private Watchable watchee;
	
	public Stopwatch(Watchable watchee)
	{
		this.watchee = watchee;
		begin = System.nanoTime();
	}
	
	public Stopwatch()
	{
		begin = System.nanoTime();
	}

	public void update() 
	{
		begin = System.nanoTime();
	}
	
	public long getBeginNanos()
	{
		return begin;
	}
	
	public long getNanos()
	{
		return System.nanoTime() - begin;
	}
	
	public double getSeconds()
	{
		return (System.nanoTime() - begin) / 1E9;
	}
	
	public double getMillseconds()
	{
		return (double)(System.nanoTime() - begin) / 1E6;
	}
	
	public void end()
	{
		long nanos = System.nanoTime() - begin;
		if (isDone.compareAndSet(false, true) && watchee != null) {
			watchee.end(nanos);
		}
	}
	
	public void fail(String message)
	{
		long nanos = System.nanoTime() - begin;
		if (isDone.compareAndSet(false, true) && watchee != null) {
			watchee.fail(nanos, message);
		}
	}
	
	public void fail(Throwable error)
	{
		long nanos = System.nanoTime() - begin;
		if (isDone.compareAndSet(false, true) && watchee != null) {
			watchee.fail(nanos, error);
		}
	}
}
