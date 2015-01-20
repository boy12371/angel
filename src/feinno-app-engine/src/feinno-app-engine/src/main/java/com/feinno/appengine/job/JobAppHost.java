/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-15
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.job.spi.JobScheduler;
import com.feinno.appengine.runtime.AppHost;
import com.feinno.configuration.ConfigurationException;
import com.feinno.rpc.duplex.RpcDuplexClient;

/**
 * JobAppHost
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class JobAppHost extends AppHost
{
	private static final Logger LOGGER = LoggerFactory.getLogger(JobAppHost.class);
	private boolean running;
	private Thread thread;
	//private List<JobScheduler> jobs;
	//key:jobname+index,防止加载多次，热加载一个JobAppBean可能会被加载多次，后面加载的替换掉前面加载的
	private Hashtable<String,JobScheduler> jobs;

	public JobAppHost() throws IOException, ConfigurationException
	{
		jobs = new Hashtable<String,JobScheduler>();
		thread = new Thread(new Runnable() {
			public void run()
			{
				threadProc();
			}
		});
	}

	/**
	 * 执行队列内所有的job
	 */
	private void threadProc()
	{
		while (running) {
			try {
				Date now = new Date();
				for (JobScheduler job : jobs.values()) {
					job.schedule(now);
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				LOGGER.error("JobAppHost threadProc interupted.{}", e);
			} catch (Exception e) {
				LOGGER.error("JobAppHost threadProc failed.{}", e);
			}
		}
	}

	/**
	 * 执行任务
	 * 
	 * @throws Exception
	 */
	protected void start() throws Exception
	{
		running = true;
		thread.start();
	}

	/**
	 * 停止任务
	 * 
	 * @throws Exception
	 */
	protected void stop() throws Exception
	{
		running = false;
		thread.join();
	}

	/**
	 * 注册job，添加到队列内
	 * 
	 * @param bean
	 * @param app
	 * @param serviceName
	 */
	protected void register(AppBean bean, AppBeanAnnotations annos)
	{
		List<JobScheduler> schedulers = new ArrayList<JobScheduler>();
		try {
			schedulers = JobScheduler.createSchedulers(bean, annos);
		} catch (ParseException e) {
			LOGGER.error("new CronExpression error.{}", e.getMessage());
		} catch (IOException e) {
			LOGGER.error("new JobDatabaseCoordinator IOException error.{}", e.getMessage());
		} catch (ConfigurationException e) {
			LOGGER.error("new JobDatabaseCoordinator ConfigurationException error.{}", e.getMessage());
		}
		for (JobScheduler s : schedulers) {
			jobs.put(s.getJobName()+s.getIndex(),s);
		}
	}

	@Override
	protected String getServiceUrl()
	{
		return null;
	}

	@Override
	public void registerInjectorService(RpcDuplexClient client)
	{
		throw new UnsupportedOperationException("no JobAppBean Injector support");
	}
}
