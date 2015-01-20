/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Apr 7, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel;

import java.util.HashMap;
import java.util.Map;

import com.feinno.diagnostic.perfmon.SmartCounter;
import com.feinno.util.Combo2;
import com.feinno.util.container.IntegerDictionary;

/**
 * 
 * 缓存所有服务器端方法
 *
 * @author 高磊 gaolei@feinno.com
 */
public class RpcServerMethodManager
{
	// private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerMethodManager.class); 
	public static final RpcServerMethodManager INSTANCE = new RpcServerMethodManager();

	private Object sync;
	private IntegerDictionary<Combo2<String, String>> toCaches;
	private IntegerDictionary<Combo2<String, String>> fromCaches;
	private Map<RpcServerMethodKey, RpcServerMethodCache> keyCaches;
	
	public RpcServerMethodManager()
	{
		sync = new Object();
		//serverMethods = new IntegerDictionary<RpcServerMethodHandler>();
		toCaches = new IntegerDictionary<Combo2<String, String>>();
		fromCaches = new IntegerDictionary<Combo2<String, String>>();
		
		keyCaches = new HashMap<RpcServerMethodKey, RpcServerMethodCache>();
	}

	public Combo2<String, String> getFrom(int id)
	{
		return fromCaches.get(id);
	}
	
	public int getOrAddFrom(String computer, String service)
	{
		Combo2<String, String> from = new Combo2(computer, service);
		return fromCaches.addOrGet(from);		
	}
	
	public RpcServerMethodCache getMethodCache(String fromComputer, String fromService, String service, String method)
	{
		// LOGGER.info("getMethodCache:" + fromComputer + "@" + fromService + "/" + service + "." + method);
		RpcServerMethodKey key = new RpcServerMethodKey(fromComputer, fromService, service, method);
		RpcServerMethodCache cache;
		
		synchronized(sync) {
			cache = keyCaches.get(key);
			if (cache == null) {
				cache = new RpcServerMethodCache(key);
				Combo2<String, String> from = new Combo2(fromComputer, fromService);
				int fromId = fromCaches.addOrGet(from);
				cache.setFromId(fromId);
				
				Combo2<String, String> to = new Combo2(service, method);
				int toId = toCaches.addOrGet(to);
				cache.setToId(toId);
				
				String perfKey = service + "." + method + "|" + fromService;
				SmartCounter counter = RpcPerformanceCounters.getServerTxCounter(perfKey);
				cache.setCounter(counter);
				keyCaches.put(key,  cache);
			}
		}
		return cache;
	}
}
