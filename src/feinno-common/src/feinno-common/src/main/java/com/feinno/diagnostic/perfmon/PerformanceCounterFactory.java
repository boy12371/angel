/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.perfmon.spi.NumberCounterBuilder;
import com.feinno.diagnostic.perfmon.spi.RatioCounterBuilder;
import com.feinno.diagnostic.perfmon.spi.ThroughputCounterBuilder;
import com.feinno.diagnostic.perfmon.spi.TransactionCounterBuilder;

/**
 * 
 * 创建计数器的通用类型
 * 
 * TODO: 如何处理自己扩展类型的计数器比如Database
 * 
 * @author 高磊 gaolei@feinno.com
 */
public final class PerformanceCounterFactory
{
	private static Object syncRoot;
	private static Map<String, CounterCategory> categorys;
	private static Map<PerformanceCounterType, CounterBuilder> builders;
	// private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceCounterFactory.class);
	
	static 
	{
		syncRoot = new Object();
		categorys = new HashMap<String, CounterCategory>();
		
		builders = new HashMap<PerformanceCounterType, CounterBuilder>();
		builders.put(PerformanceCounterType.NUMBER, NumberCounterBuilder.INSTANCE);
		builders.put(PerformanceCounterType.RATIO, RatioCounterBuilder.INSTANCE);
		builders.put(PerformanceCounterType.THROUGHPUT, ThroughputCounterBuilder.INSTANCE);
		builders.put(PerformanceCounterType.TRANSACTION, TransactionCounterBuilder.INSTANCE);
	}

	/**
	 * 
	 * 通过@PerformanceCounterCategory和@PerformanceCounter标注，获取或创建一个性能计数器
	 * @param <E>
	 * @param categoryClazz
	 * @param instance
	 * @return
	 */
	public static <E> E getCounters(Class<E> categoryClazz, String instance)
	{
		PerformanceCounterCategory anno = categoryClazz.getAnnotation(PerformanceCounterCategory.class); 
		if (anno == null) {
			throw new IllegalArgumentException("only support class annotated with @PerformanceCounterCategory");
		}
		
		String catName = anno.value();
		CounterCategory category;
		synchronized (syncRoot) {
			 category = categorys.get(catName);
			 if (category == null) {
				 category = new CounterCategory(catName, categoryClazz);
				 categorys.put(catName, category);
				 ObserverManager.register(category);
			 }
		}
		
		// TODO assert categoryClazz == category.counterClazz
		return (E) category.getInstanceRefer(instance);
	}
	
	public static CounterCategory getCategory(String name)
	{
		synchronized (syncRoot) {
			return categorys.get(name);
		}		
	}
	
	public static List<CounterCategory> getAllCategorys()
	{
		List<CounterCategory> ret = new ArrayList<CounterCategory>();
		synchronized (syncRoot) {
			for (CounterCategory c: categorys.values()) {
				ret.add(c);
			}
		}		
		return ret;
	}
	
	public static CounterBuilder getBuilder(PerformanceCounterType type)
	{
		return builders.get(type);
	}
	
//	
//	public static Thread registerCounterLogger(SmartCounter counter, final int second)
//	{
//		final CounterEntity e = (CounterEntity)counter;
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run()
//			{
//				while (true) {
//					try {
//						ObserverReportSnapshot last = e.getObserverSnapshot();
//						Thread.sleep(second * 1000);
//						CounterOutput output = e.getSnapshot().computeOutput(last);
//						String name = String.format("%s:%s:%s", e.getCategory(), e.getName(), e.getInstance());
//						LOGGER.warn("{} {}", name, output);
//					} catch (Exception ex) {
//						LOGGER.error("failed {}", ex);
//					}
//				}
//			} 
//		});
//		t.start();
//		return t;
//	}

}
