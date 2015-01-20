/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-10-8
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon;

import com.feinno.diagnostic.observation.ObserverReportColumn;

/**
 * 抽象构造器
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class CounterBuilder
{
	public abstract CounterEntity createCounter();
	
	public abstract ObserverReportColumn[] getColumns(String name);
}
