/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-20
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.util.Combo2;

/**
 * 计数器分类的模板类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class CounterCategorySchema
{
	private Class<?> clazz;
	private List<ObserverReportColumn> columns;
	private Map<String, Combo2<Field, CounterBuilder>> counters;
	
	public CounterCategorySchema(String name, Class<?> clazz)
	{
		this.clazz = clazz;
		counters = new HashMap<String, Combo2<Field, CounterBuilder>>();
		columns = new ArrayList<ObserverReportColumn>();
		Field[] fs =clazz.getDeclaredFields();
		AccessibleObject.setAccessible(fs, true);
		
		for (Field f: fs) {
			PerformanceCounter anno = f.getAnnotation(PerformanceCounter.class);
			if (anno == null)
				continue;
			
			if (counters.get(anno.name()) != null) {
				throw new IllegalArgumentException("duplicate counter name =" + anno.name());
			}

			CounterBuilder builder = PerformanceCounterFactory.getBuilder(anno.type());
			counters.put(anno.name(), new Combo2(f, builder));
			
			for (ObserverReportColumn column: builder.getColumns(anno.name())) {
				columns.add(column);
			}
		}
	}
	
	public CounterCategoryInstance createCounterInstance(String instance)
	{
		Object cc;
		try {
			cc = clazz.newInstance();
			CounterEntity[] entitys = new CounterEntity[counters.keySet().size()];
			int i = 0;
			Field[] fs =clazz.getDeclaredFields();
			for (Field fc: fs) {
				PerformanceCounter anno = fc.getAnnotation(PerformanceCounter.class);
				if (anno == null)
					continue;
				
				for (String key: counters.keySet()) {
					if(key != null && key.equals(anno.name())){
						Combo2<Field, CounterBuilder> item = counters.get(key);
						Field f = item.getV1();
						CounterBuilder builder = item.getV2();
						CounterEntity e = builder.createCounter();
						entitys[i++] = e;
						f.set(cc, e);
						break;
					}
					
				}
			}
			
			return new CounterCategoryInstance(entitys, cc, instance);
		} catch (Exception e) {
			throw new IllegalArgumentException("unable to create instance " + clazz.getName(), e);
		}	
	}

	public List<ObserverReportColumn> getObserverColumns()
	{
		return columns;
	}
}
