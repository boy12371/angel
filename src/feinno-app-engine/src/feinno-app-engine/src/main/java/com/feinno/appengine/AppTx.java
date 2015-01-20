/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-15
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.appengine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import com.feinno.diagnostic.perfmon.Stopwatch;
import com.feinno.threading.ThreadContext;
import com.feinno.threading.ThreadContextName;
import com.feinno.util.Action;
import com.feinno.util.Event;

/**
 * AppBean关联的事务对象, 一个事务对象在一次请求中会经过AppBean及所有的Handler的处理
 * 
 * 其中AppBean及BeforeHandler都能够对AppTx进行terminate处理 terminate处理后, 相应的bean不会继续
 * 
 * TODO: 如果异常停止, 如何继续进行下一步的
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class AppTx {
	
	public static final int SERVER_TIMEOUT = 60 * 1000;
	
	private long lastTicks;
	private AtomicBoolean terminated = new AtomicBoolean(false);
	private Exception error;
	private Action<Exception> terminateCallback = null;
	private Map<String, Object> sessions = null;
	private AppBean currentApp;
	private Marker marker;
	private Stopwatch watch;
	private Event<Throwable> terminatedEvent;
	private int id;
	private AppLogger appLogger = new AppLogger(this);
	private static final Logger LOGGER = LoggerFactory.getLogger(AppTx.class);
	
	protected AppTx() {
		watch = new Stopwatch();
		setTerminatedEvent(new Event<Throwable>(this));
		//不能在这里就进入manager的管理了, 太早
//		AppTxManager.INSTANCE.addTx(this);
	}
	

	public long getCreatedTicks() {
		return watch.getBeginNanos();
	}

	public long getLastTicks() {
		return lastTicks;
	}

	public void refreshLastTicks() {
		lastTicks = System.nanoTime();
	}
	
	public int getElapseMs()
	{
		return (int)watch.getMillseconds();
	}
	
	public boolean isServerTimeout()
	{
		return watch.getMillseconds() > SERVER_TIMEOUT;
	}

	//
	// 设置当前正在运行的AppBean
	public void setCurrentApp(AppBean app) {
		this.currentApp = app;
		appLogger.setLoggerClass(app.getClass());
	}

	public AppBean getCurrentApp() {
		return currentApp;
	}

	/**
	 * 
	 * 设置Transaction结束时的流程
	 * 
	 * @param callback
	 */
	public void setTerminateCallback(Action<Exception> callback) {
		terminateCallback = callback;
	}

	public boolean terminated() {
		return terminated.get();
	}

	public void setError(Exception error) {
		this.error = error;
	}

	public Exception error() {
		return error;
	}

	/**
	 * 
	 * 终止此tx
	 * 
	 * @param message
	 */
	public void terminate() {
		if (terminated.compareAndSet(false, true)) {
			terminatedEvent.fireEvent(error());
			if (error() == null)
				watch.end();
			else
				watch.fail(error());
			ThreadContext.releaseCurrent();
			if (terminateCallback != null) {
				terminateCallback.run(error());
			}
		}
	}

	/**
	 * 
	 * 当终止此tx时, 如果存在错误, 则返回, 本方法提供
	 */
	protected void onTransactionError() {
	}

	/**
	 * 
	 * 在Tx中保存一个上下文变量, 可以再Handler当中进行处理
	 * 
	 * @param key
	 * @param value
	 */
	public void putSession(String key, Object value) {
		synchronized (this) {
			if (sessions == null) {
				sessions = new HashMap<String, Object>();
			}
			sessions.put(key, value);
		}
	}

	/**
	 * 
	 * 获取在tx中保存的上下文变量, 只在
	 * 
	 * @param key
	 * @param value
	 */
	public Object getSession(String key) {
		if (sessions == null) {
			return null;
		} else {
			synchronized (this) {
				return sessions.get(key);
			}
		}
	}

	/**
	 * 获取当前TX的Marker,一般用于Context
	 * 
	 * @return
	 */
	public Marker getMarker() {
		return marker;
	}

	protected void setMarker(Marker marker) {
		this.marker = marker;
		try {
			ThreadContext.getCurrent().putNamedContext(ThreadContextName.LOGGING_MARKER, marker);
		} catch (Exception ex) {
			LOGGER.error("setMarker failed {}", ex);
		}	
	}


	public int getId()
	{
		return id;
	}


	public void setId(int id)
	{
		this.id = id;
	}


	public Event<Throwable> getTerminatedEvent()
	{
		return terminatedEvent;
	}


	public void setTerminatedEvent(Event<Throwable> terminatedEvent)
	{
		this.terminatedEvent = terminatedEvent;
	}
	
	/**
	 * 获取一个AppLogger，该Logger可以帮助记录app的context.uri
	 * @return
	 */
	public Logger getAppLogger(){
		return appLogger;
	}
}
