/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.observation;

import java.util.List;

import com.feinno.diagnostic.perfmon.CounterCategory;

/**
 * 
 * <b>描述: </b>用于告知监控窗口当前监控下正在运行的监控条目<br>
 * 当一个类实现该接口后，外部监控可以据此知道该类型监控所监控的具体子条目
 * <p>
 * <b>功能: </b>为某一类监控数据的具体监控子条目的名称及定义提供统一接口
 * <p>
 * <b>用法: </b>用于继承使用，请参照{@link CounterCategory}<br>
 * 完整用法请参见{@link ObserverManager}的用法部分
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 * 
 */
public interface Observable
{
	/**
	 * 
	 * 获取观察者名称
	 * @return
	 */
	String getObserverName();
	
	/**
	 * 
	 * 获取报表的列类型
	 * @return
	 */
	List<ObserverReportColumn> getObserverColumns();
	
	/**
	 * 
	 * 获取报表单元列表
	 * @return
	 */
	List<ObservableUnit> getObserverUnits(); 
}
