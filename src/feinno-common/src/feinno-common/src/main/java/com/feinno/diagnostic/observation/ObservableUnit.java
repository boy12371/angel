/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-10-6
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.observation;

import com.feinno.diagnostic.perfmon.PerformanceCounter;
import com.feinno.diagnostic.perfmon.spi.NumberCounter;

/**
 * 
 * <b>描述: </b>可观察对象单元，该单元一般是指一个{@link PerformanceCounter}
 * 所标识出来的一个字段，它本身承担了这个字段代表的监控条目在运行时对数据的变化进行记录，再通过
 * {@link ObservableUnit#getObserverSnapshot()}方法获取当前时刻该单元的快照，示例可参见
 * {@link NumberCounter}
 * <p>
 * <b>功能: </b>可观察对象单元，该单元一般是指一个{@link PerformanceCounter}所标识出来的一个字段信息
 * <p>
 * <b>用法: </b>用于继承使用，可参见{@link NumberCounter}
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 * 
 */
public interface ObservableUnit {
	/**
	 * 
	 * 生成快照项
	 * 
	 * @return
	 */
	ObserverReportSnapshot getObserverSnapshot();

	/**
	 * 
	 * 获取实例名
	 * 
	 * @return
	 */
	String getInstanceName();
}
