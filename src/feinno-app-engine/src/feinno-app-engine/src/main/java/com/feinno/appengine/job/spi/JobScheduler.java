/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job.spi;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.job.JobAppBean;
import com.feinno.appengine.job.JobAppTx;
import com.feinno.appengine.job.JobResource;
import com.feinno.appengine.job.JobSchedule;
import com.feinno.appengine.resource.Resource;
import com.feinno.appengine.resource.ResourceFactory;
import com.feinno.appengine.resource.ResourceGroup;
import com.feinno.appengine.resource.ResourceType;
import com.feinno.configuration.ConfigurationException;
import com.feinno.util.ServiceEnviornment;
import com.feinno.util.ThreadPool;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class JobScheduler {
	private int index;
	private String jobName;
	private JobAppBean bean;
	private Date sleepBefore;
	private JobCoordinator coordinator;
	private JobScheduleDuring during;
	private boolean parallel;
	private JobAppTx jtx;

	private int timeout;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JobScheduler.class);

	/**
	 * 运行定时任务
	 * 
	 * @param now
	 */
	public void schedule(Date now) {

		/**
		 * 如果当前时间超过上次运行时间或者当前时间复合表达式运行时间，则执行任务。 理论上第一个条件压根就不存在false
		 **/
		if (now.after(sleepBefore) && during.within(now)) {
			jtx = new JobAppTx(index, this);
			int second = jtx.aquireJobLock();
			LOGGER.info(String.format("job schedule second = " + second));
			if (second != 0) {
				Future<?> future = ThreadPool.submit(new Runnable() {
					public void run() {
						runJob();
					}
				});
				if (!parallel) {
					// 串行
					while (!future.isDone()) {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							LOGGER.error("error:", e);
						}
					}
				}
			}
			sleepBefore = now;
		}
	}

	private void runJob() {
		
		try {
			bean.processJob(jtx);
		} catch (Exception e) {
			LOGGER.error(String
					.format("Job process error. jobName = %s, index =  %s, workerName =  %s, serverName =  %s, error info = %s",
							jobName, index,
							ServiceEnviornment.getServiceName(),
							ServiceEnviornment.getComputerName(),
							e.getMessage()));

		} finally {
			jtx.unlock();
		}
	}

	public static List<JobScheduler> createSchedulers(AppBean bean,
			AppBeanAnnotations annos) throws ParseException, IOException,
			ConfigurationException {
		JobResource jobRes = bean.getClass().getAnnotation(JobResource.class);
		JobSchedule jobSch = bean.getClass().getAnnotation(JobSchedule.class);

		ResourceGroup<?> group = ResourceFactory.getResourceGroup(
				jobRes.type(), jobRes.resource());
		List<JobScheduler> schList = new ArrayList<JobScheduler>();
		for (Resource res : group.resources()) {
			JobScheduler sch = new JobScheduler();
			sch.jobName = bean.getCategoryMinusName();
			sch.index = jobRes.type() == ResourceType.NONE ? -1 : res.index();
			sch.timeout = jobSch.timeout();
			sch.during = new JobScheduleDuring(jobSch.cron());
			sch.coordinator = new JobDatabaseCoordinator();
			sch.bean = (JobAppBean) bean;
			sch.sleepBefore = new Date();
			sch.parallel = jobRes.parallel();
			schList.add(sch);
		}
		return schList;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public JobCoordinator getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(JobCoordinator coordinator) {
		this.coordinator = coordinator;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
