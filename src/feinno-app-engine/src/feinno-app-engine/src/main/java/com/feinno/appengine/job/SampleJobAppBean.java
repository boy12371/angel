/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job;

import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.configuration.AppBeanAnnotationsLoader;
import com.feinno.appengine.resource.ResourceFactory;
import com.feinno.appengine.resource.ResourceType;
import com.feinno.initialization.InitialUtil;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@AppName(category = "sample", name = "Job1")
@JobResource(type = ResourceType.DATABASE, resource = "IICUPDB", parallel = false)
@JobSchedule(cron = "0/5 * * * * ?")
public class SampleJobAppBean extends JobAppBean
{
	private JobAppHost jobHost = null;
	private int num = 1;

	public void setup() throws Exception
	{

	}

	public void load() throws Exception
	{
		//ConfigurationManager.setLoader(new LocalConfigurationLoader());
		InitialUtil.init(ResourceFactory.class);
		jobHost = new JobAppHost();
		jobHost.start();
		jobHost.register(this, AppBeanAnnotationsLoader.getAppBeanAnnotaions(SampleJobAppBean.class));
	}

	public void unload() throws Exception
	{
		jobHost.stop();
	}

	public void process(JobAppTx tx) throws Exception
	{
		System.out.println(num);
		num++;
		Thread.sleep(5000);
		// updb.executeNonQuery(tx.context(), "delete top 1 * from LOG");
	}
}
