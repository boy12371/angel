/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon;

/**
 * 计数器通用接口类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public interface SmartCounter
{
	void reset();
	void increase();
	void decrease();
	void increaseBy(long value);
	void setRawValue(long value);	
	void increaseRatio(boolean hitted);
	Stopwatch begin();
}
