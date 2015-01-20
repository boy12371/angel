/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-15
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job;

import com.feinno.appengine.AppTxWithContext;
import com.feinno.appengine.job.spi.JobScheduler;

/**
 * Job执行的AppTx
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class JobAppTx extends AppTxWithContext<JobContext> {
	private JobContext context;
	private JobScheduler scheduler;
	private boolean isUnlock;
	private static Object syncRoot = new Object();

	public JobAppTx(int index, JobScheduler scheduler) {
		super();
		context = new JobContext(index);
		this.scheduler = scheduler;
	}

	public JobContext getContext() {
		return context;
	}

	public int aquireJobLock() {
		synchronized (syncRoot) {
			int second = 0;
			if (!isUnlock) {
				second = scheduler.getCoordinator().aquireJobLock(
						scheduler.getJobName(), scheduler.getIndex(),
						scheduler.getTimeout());
				isUnlock = true;
			}
			return second;
		}
	}

	public void unlock() {
		synchronized (syncRoot) {
			if (isUnlock) {
				scheduler.getCoordinator().releaseJobLock(
						scheduler.getJobName(), scheduler.getIndex());
				isUnlock = false;
			}
		}
	}

	@Override
	protected String extractContextUri() {
		throw new UnsupportedOperationException("NotSupported");
	}

	@Override
	protected byte[] extractContextData() {
		throw new UnsupportedOperationException("NotSupported");
	}

	public boolean isUnlock() {
		return isUnlock;
	}
}
