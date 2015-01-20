/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-10-8
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.diagnostic;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ObserverTest
{
	@Test
	public void test()
	{
		Date date = new Date();
	     long lDateTime = new Date().getTime();
	     System.out.println("Date() - Time in milliseconds: " + lDateTime);
	     // System.out.println("Date() - Time in milliseconds: " + date.);
	 
	      Calendar lCDateTime = Calendar.getInstance();
	      System.out.println("Calender - Time in milliseconds :" + lCDateTime.getTimeInMillis());			
	}
}
