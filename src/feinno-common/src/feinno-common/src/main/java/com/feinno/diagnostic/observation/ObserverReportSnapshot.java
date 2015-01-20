/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.observation;


/**
 * 计数器快照，保存某个时间点的计数器信息，
 * 可以通过不同时间的计数器快照计算出此段时间中的信息
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class ObserverReportSnapshot
{
	private long nanos;
	
	public ObserverReportSnapshot()
	{
		nanos = System.nanoTime();
	}

	public long getNanos()
	{
		return nanos;
	}

	/**
	 * 
	 * 从两次快照的结果中得到ReportUnit，其中this为本次快照，last为上次快照
	 * @param last
	 * @return
	 */
	public abstract ObserverReportUnit computeReport(ObserverReportSnapshot last);
}
