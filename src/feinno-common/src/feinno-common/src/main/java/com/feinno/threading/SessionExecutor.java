/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-21
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.util.container.SessionPool;

/**
 * 一个适用异步应用的线程池，适应回调的场景，并提供Session的超时与检测
 * 
 * 能够被添加到此线程池中的应用应当实现SessionRunnable接口
 * <code>
 * SessionRunable
 * (run): 执行
 * (continueWith): 接续
 * 
 * </code> 
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class SessionExecutor implements Executor
{	
	private int maxConcurrent;
	private int maxSessions;
	
	private String name;
	
	private Executor innerExecutor;
	
	private AtomicInteger concurrent;
	private SessionPool<SessionTask> sessions;
	
	private SessionExecutorCounter counter;
	private Thread thread;
	private final Logger LOGGER;
	
	/**
	 * 创建一个能够按照Session进行线程管理的线程池
	 * 
	 * @param name
	 * @param size
	 * @param concurrent
	 * @param maxSessions
	 */
	SessionExecutor(String name, int size, int maxConcurrent, int maxSessions)
	{
		this.name = name;
		this.innerExecutor = Executors.newFixedThreadPool(size, ThreadFactorys.forApp(name));

		counter = SessionExecutorCounterFactory.INSTANCE.getCounter(name); 
				
		this.name = name;
		this.maxConcurrent = maxConcurrent;
		this.maxSessions = maxSessions;
		
		concurrent = new AtomicInteger();
		sessions = new SessionPool<SessionTask>(maxSessions);
		
		counter = SessionExecutorCounterFactory.INSTANCE.getCounter(name);
		LOGGER = LoggerFactory.getLogger("com.feinno.threading.SessionExecutor." + name);
		
		thread = new Thread(new Runnable() {
			@Override
			public void run()
			{
				monitorProc();
			}
		});
		
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * 执行一个任务, 接口接受两种结构<br>
	 * 1. SessionTask: 起始任务<br>
	 * 2. FutureCallback: 接续任务<br>
	 */
	public void execute(final Runnable r)
	{
		if (r instanceof SessionTask) {
			executeSessionTask((SessionTask)r);
		} else if (r instanceof FutureCallback) {
			executeFutureCallback((FutureCallback)r);
		} else {
			//
			// 不支持其他类型的Runnable
			LOGGER.error("Task not supported", r.toString());
			throw new IllegalArgumentException("SessionExecutor only support SessionContext and FutureCallback");
		}		
	}

	/**
	 * 
	 * 新启一个任务
	 * @param task
	 */
	private void executeSessionTask(final SessionTask task)
	{
		//
		// 释放超过了线程池的最大等待数量
		if (concurrent.incrementAndGet() > maxConcurrent) {
			concurrent.decrementAndGet();
			LOGGER.error("OVER_MAX_CONCURRENT");
			throw new ExecutorException(ExecutorException.OVER_MAX_CONCURRENT, this);				
		}

		final int id = sessions.add(task);

		//
		// 是否超过了最大的Session
		if (id < 0) {
			LOGGER.error("OVER_MAX_SESSION");
			throw new ExecutorException(ExecutorException.OVER_MAX_SESSION, this);
		}
				
		innerExecutor.execute(new Runnable() {
			@Override
			public void run()
			{
				ThreadContext currentThread = ThreadContext.getCurrent();
				try {
					// Stopwatch watch = counter.begin();
					currentThread.putNamedContext(ThreadContextName.SESSION_TASK, task);
					currentThread.putNamedContext(ThreadContextName.EXECUTOR, SessionExecutor.this);
					task.run();
					
				} catch (Exception ex) {
				} finally {
					if (task.isTerminated()) {
						sessions.remove(id);
					}
					currentThread.clear();
					concurrent.decrementAndGet();
				}
			}
		});
	}
	
	/**
	 * 
	 * 接续一个任务, 通过Future
	 * @param callback
	 */
	public void executeFutureCallback(final FutureCallback callback)
	{
		//
		// 当处理一个回调任务的时候, 保证此回调任务总能完成
		final SessionTask task = callback.getFuture().getSessionTask();
		if (task == null) {
			LOGGER.error("SessionLost", callback.getFuture().toString());
			throw new IllegalStateException("SessionLost" + callback.getFuture().toString());
		}

		concurrent.incrementAndGet();
		innerExecutor.execute(new Runnable() {
			@Override
			public void run()
			{
				ThreadContext currentThread = ThreadContext.getCurrent();
				try {
					currentThread.putNamedContext(ThreadContextName.SESSION_TASK, task);
					currentThread.putNamedContext(ThreadContextName.EXECUTOR, SessionExecutor.this);
					callback.run();					
				} catch (Exception ex) {
					if (task.isTerminated()) {
						//sessions.decrementAndGet();
					}					
				} finally {
					if (task.isTerminated()) {
						//sessions.decrementAndGet();
					}
					currentThread.clear();
					concurrent.decrementAndGet();
				}
			}			
		});
	}
	
	private void monitorProc()
	{
		while (true) {
			try {
				Thread.sleep(500);
				clearTerminatedSessions();
				// TODO: clearTimeoutSessions();
			} catch (InterruptedException e) {
				// NOTHING TO DO
			} catch (Exception ex) {
				LOGGER.error("monitorProc failed: {}", ex);
			} 
		}
	}
	
	private void clearTerminatedSessions()
	{
		
	}
}
