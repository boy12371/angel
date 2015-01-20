/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.job.spi;

import java.text.ParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.job.CronExpression;

/**
 * 能够解析job时间区间的应用类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class JobScheduleDuring {

	private CronExpression cron;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JobScheduleDuring.class);

	public JobScheduleDuring(String during) {
		try {
			if (CronExpression.isValidExpression(during)) {
				cron = new CronExpression(during);
			} else {
				throw new IllegalArgumentException(
						"cronExpression isValidExpression error. during = "
								+ during);
			}
		} catch (ParseException e) {
			LOGGER.error("new CronExpression error.{}", e.getMessage());
		}
	}

	/**
	 * 比较当前时间是满足运行时间
	 * 
	 * @param now
	 *            当前时间
	 * @return boolean
	 */
	public boolean within(Date now) {
		return cron.isSatisfiedBy(now);
	}

	/**
	 * 获得下次运行时的时间。如果运行周期内已经执行过，则不再执行
	 * 
	 * @param now
	 * @return
	 */
	public Date getNextTimeAfter(Date now) {
		return cron.getNextValidTimeAfter(now);
	}
}
