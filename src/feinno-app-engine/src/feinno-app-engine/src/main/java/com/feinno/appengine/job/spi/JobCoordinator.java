/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job.spi;


/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public interface JobCoordinator
{
	int aquireJobLock(String jobName, int index, int lockTimeout);
	
	void releaseJobLock(String jobName, int index);
}
