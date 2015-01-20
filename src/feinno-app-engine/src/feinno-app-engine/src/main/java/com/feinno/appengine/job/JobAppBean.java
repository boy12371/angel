/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-15
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.AppTx;
import com.feinno.appengine.annotation.AppBeanBaseType;
import com.feinno.appengine.job.spi.JobDatabaseCoordinator;
import com.feinno.appengine.job.spi.JobScheduler;
import com.feinno.appengine.resource.Resource;
import com.feinno.appengine.resource.ResourceFactory;
import com.feinno.appengine.resource.ResourceGroup;
import com.feinno.util.Action;

/**
 * 定时任务的AppBean
 * 
 * @author 高磊 gaolei@feinno.com
 */
@AppBeanBaseType
public abstract class JobAppBean extends AppBean {
	@Override
	public void processTx(AppTx tx, Action<Exception> callback) {
		JobAppTx jtx = (JobAppTx) tx;
		try {
			process(jtx);
			callback.run(null);
		} catch (Exception e) {
			jtx.setError(e);
			callback.run(e);
		}
	}

	public void processJob(JobAppTx jtx) {
		this.processHandlerChain(jtx, null);
	}

	public void processTest() {
		JobAppTx jtx = null;
		JobResource jobRes = this.getClass().getAnnotation(JobResource.class);

		ResourceGroup<?> group = ResourceFactory.getResourceGroup(
				jobRes.type(), jobRes.resource());
		try {
			for (Resource res : group.resources()) {
				JobScheduler sch = new JobScheduler();
				sch.setCoordinator(new JobDatabaseCoordinator());
				jtx = new JobAppTx(res.index(), sch);
				process(jtx);
			}
		} catch (Exception e) {
			jtx.setError(e);
		}
	}

	public abstract void process(JobAppTx ctx) throws Exception;
}
