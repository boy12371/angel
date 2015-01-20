/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Apr 29, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel;

import java.util.HashMap;
import java.util.Map;

import com.feinno.diagnostic.perfmon.SmartCounter;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcClientMethodManager
{
	public static final RpcClientMethodManager INSTANCE = new RpcClientMethodManager();

	private Object syncCache;	
	private Map<RpcClientMethodKey, RpcClientMethodCache> methodCaches;
	
	private RpcClientMethodManager()
	{
		syncCache = new Object();
		methodCaches = new HashMap<RpcClientMethodKey, RpcClientMethodCache>();
	}

	public RpcClientMethodCache getMethodCache(RpcEndpoint ep, String service, String method)
	{
		RpcClientMethodKey key = new RpcClientMethodKey(ep, service, method);
		return getMethodCache(key);
	}
	
	public RpcClientMethodCache getMethodCache(RpcClientMethodKey key)
	{
		synchronized (syncCache) {
			RpcClientMethodCache cache = methodCaches.get(key);
			if (cache == null) {
				cache = new RpcClientMethodCache(key);
				SmartCounter counter = RpcPerformanceCounters.getClientTxCounter(key.getServiceUrl());
				cache.setCounter(counter);
				methodCaches.put(key, cache);
			}
			return cache;
		}
	}
}
