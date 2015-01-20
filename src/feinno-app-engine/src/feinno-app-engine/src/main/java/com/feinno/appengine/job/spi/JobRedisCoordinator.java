/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job.spi;

/**
 * 留个扩展，但是在1.0版本中不起作用
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class JobRedisCoordinator implements JobCoordinator
{

	@Override
	public int aquireJobLock(String jobName, int index, int lockTimeout)
	{
		throw new UnsupportedOperationException("没实现呢");
	}

	@Override
	public void releaseJobLock(String jobName, int index)
	{
		throw new UnsupportedOperationException("没实现呢");
	}

}
