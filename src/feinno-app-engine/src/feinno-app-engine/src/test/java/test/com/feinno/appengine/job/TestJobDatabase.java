/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-6-9
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.appengine.job;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import com.feinno.appengine.job.CronExpression;
import com.feinno.appengine.job.spi.JobDatabaseCoordinator;
import com.feinno.configuration.ConfigurationException;
import com.feinno.util.DateUtil;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 */
public class TestJobDatabase {

	public void testjobData() {
		try {

			JobDatabaseCoordinator data = new JobDatabaseCoordinator();
			Integer n = data.aquireJobLock("test3", 1, 10);
			System.out.println(n);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	
//	@Test
//	public void testCron() {
//		String cronStr = "0 0 10,11 * * ?" ;
//		
//		try {
//			CronExpression cron = new CronExpression(cronStr);
//			while(true){
//				Date a = new Date();
//				System.out.println(DateUtil.formatDate(a,
//						DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT) + "    " + cron.isSatisfiedBy(a));
//
//				Date dd = cron.getNextValidTimeAfter(a);
//				System.out.println(DateUtil.formatDate(dd,
//						DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT) + "    " + cron.isSatisfiedBy(dd));
//				System.out.println("");
//				Thread.sleep(60 * 1000);
//			}
//			
//			
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
}
