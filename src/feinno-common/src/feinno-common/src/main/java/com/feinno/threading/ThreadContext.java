/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-21
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.threading;

import java.util.Hashtable;
import java.util.Map;

/**
 * 
 * <b>描述: </b>这是一个用于存储线程上下文信息的类，该类中的{@linkSessionContext}存储了当前会话状态的信息
 * <p>
 * <b>功能: </b>用于存储线程上下文信息的类
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 * @see SessionTask
 * @see ThreadContext
 */
public class ThreadContext
{
	private static int MAX_NAMED_CONTEXTS = 8;
	private static Map<Long, ThreadContext> threads;
	
	static {
		//
		// TODO try lock-free
		threads = new Hashtable<Long, ThreadContext>();
	}
	
	public static long getId()
	{
		return Thread.currentThread().getId();
	}
	
	public static synchronized ThreadContext getCurrent()
	{
		long id = getId();

		if(threads.get(id) == null)
			threads.put(id, new ThreadContext());
		
		return threads.get(id);
	}
	
	public static void releaseCurrent()
	{
		long id = getId();
		threads.remove(id);
	}
	
	private Object[] namedContexts;
	private Map<Object, Object> contexts;
	
	public void putNamedContext(ThreadContextName name, Object value)
	{
		if (namedContexts == null) {
			namedContexts = new Object[MAX_NAMED_CONTEXTS];
		}
		namedContexts[name.intValue()] = value;
	}
	
	public Object getNamedContext(ThreadContextName name)
	{
		if (namedContexts == null) {
			return null;
		} else {
			return namedContexts[name.intValue()];
		}
	}
	
	public void putContext(Object key, Object value)
	{
		if (contexts == null) {
			contexts = new Hashtable<Object, Object>();
		}
		contexts.put(key, value);
	}
	
	public Object getContext(Object key)
	{
		if (contexts == null) {
			return null;
		} else {
			return contexts.get(key);
		}
	}
	
	public void clear()
	{
		contexts = null;
		namedContexts = null;
	}
}
