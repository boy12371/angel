/*
 * FAE, Feinno App Engine
 *  
 * Create by Huangxianglong 2011-7-28
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.util;

import java.sql.Date;
import java.text.ParseException;

import org.junit.Test;

import com.feinno.util.DateUtil;
import com.feinno.util.TimeSpan;

import junit.framework.Assert;

/**
 * @author huangxianglong
 * 
 */
public class TestTimeSpan {
	@Test
	public void TestGetDays() throws ParseException {
		long millis = DateUtil.getDefaultDate("2011-1-1 5:6:7",
				DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime()
				- DateUtil.getDefaultDate("2010-1-1 0:0:0",
						DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime();
		TimeSpan ts = new TimeSpan(millis);
		Assert.assertEquals(365, ts.getDays());
	}

	@Test
	public void TestGetHours() throws ParseException {
		long millis = DateUtil.getDefaultDate("2011-1-1 5:6:7",
				DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime()
				- DateUtil.getDefaultDate("2010-1-1 0:0:0",
						DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime();
		TimeSpan ts = new TimeSpan(millis);
		Assert.assertEquals(5, ts.getHours());
	}

	@Test
	public void TestGetMinutes() throws ParseException {
		long millis = DateUtil.getDefaultDate("2011-1-1 5:6:7",
				DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime()
				- DateUtil.getDefaultDate("2010-1-1 0:0:0",
						DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime();
		TimeSpan ts = new TimeSpan(millis);
		Assert.assertEquals(6, ts.getMinutes());
	}

	@Test
	public void TestGetSeconds() throws ParseException {
		long millis = DateUtil.getDefaultDate("2011-1-1 5:6:7",
				DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime()
				- DateUtil.getDefaultDate("2010-1-1 0:0:0",
						DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime();
		TimeSpan ts = new TimeSpan(millis);
		Assert.assertEquals(7, ts.getSeconds());
	}

	@Test
	public void TestGetTotalHours() throws ParseException {
		long millis = DateUtil.getDefaultDate("2011-1-1 5:6:7",
				DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime()
				- DateUtil.getDefaultDate("2010-1-1 0:0:0",
						DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime();
		TimeSpan ts = new TimeSpan(millis);
		Assert.assertEquals(365 * 24 + 5, ts.getTotalHours());
	}

	@Test
	public void TestGetTotalMinutes() throws ParseException {
		long millis = DateUtil.getDefaultDate("2011-1-1 5:6:7",
				DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime()
				- DateUtil.getDefaultDate("2010-1-1 0:0:0",
						DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime();
		TimeSpan ts = new TimeSpan(millis);
		Assert.assertEquals((365 * 24 + 5) * 60 + 6, ts.getTotalMinutes());
	}

	@Test
	public void TestGetTotalSeconds() throws ParseException {
		long millis = DateUtil.getDefaultDate("2011-1-1 5:6:7",
				DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime()
				- DateUtil.getDefaultDate("2010-1-1 0:0:0",
						DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT).getTime();
		TimeSpan ts = new TimeSpan(millis);
		Assert.assertEquals(((365 * 24 + 5) * 60 + 6) * 60 + 7,
				ts.getTotalSeconds());
	}

	@Test
	public void TestTimeSpan()  {
		Date begin = new Date(1000);
		Date end = new Date(5000);
		TimeSpan ts = new TimeSpan(begin, end);
		Assert.assertEquals(4, ts.getTotalSeconds());

	}
	@Test
	public void TestGetTotalMillseconds() {
		Date begin = new Date(1000);
		Date end = new Date(5000);
		TimeSpan ts = new TimeSpan(begin, end);
		Assert.assertEquals(4000, ts.getTotalMillseconds());

	}
}
