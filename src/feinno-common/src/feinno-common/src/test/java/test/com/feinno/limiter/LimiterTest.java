package test.com.feinno.limiter;

import java.util.Properties;

import junit.framework.TestCase;

import com.feinno.limiter.Limiter;
import com.feinno.limiter.LimiterFactory;
import com.feinno.util.Assert;

public class LimiterTest extends  TestCase{
	/**
	 * maxCountPerSecond  每秒中重置
	 * maxCount 每分钟重置
	 */
	public void testLimiterReset()
	{
		Properties prop = new Properties();
		prop.setProperty("maxCount", "100");
		prop.setProperty("maxCountPerSecond", "200");
		
		LimiterFactory.getInstance().init(prop);
		
		for(int i=0;i<100;i++)
		{
		
			Limiter limiter = LimiterFactory.getInstance().getLimiter("test"+i);
			limiter.increment();
			limiter.decrement();
			limiter.increment();
		}
		
		for(int i=0;i<100;i++)
		{
			Limiter limiter2 = LimiterFactory.getInstance().getLimiter("test"+i);
			assertEquals(2, limiter2.getCountPerSecond());
			assertEquals(1,limiter2.getCount());
			System.out.println(String.format("limiter:%s count:%d countPersecend:%d","test"+i,limiter2.getCount(),limiter2.getCountPerSecond()));
		}	
			try{
				Thread.sleep(1*1000);
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
			for(int i=0;i<100;i++){
				Limiter limiter2 = LimiterFactory.getInstance().getLimiter("test"+i);
				assertEquals(0, limiter2.getCountPerSecond());
				assertEquals(1,limiter2.getCount());
				System.out.println(String.format("after 1 secends,limiter:%s count:%d countPersecend:%d","test"+i,limiter2.getCount(),limiter2.getCountPerSecond()));
			}
			try{
				Thread.sleep(61*1000);
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
			for(int i=0;i<100;i++){
				Limiter limiter2 = LimiterFactory.getInstance().getLimiter("test"+i);
				assertEquals(0, limiter2.getCountPerSecond());
				assertEquals(0,limiter2.getCount());
				System.out.println(String.format("after 61 secends,limiter:%s count:%d countPersecend:%d","test"+i,limiter2.getCount(),limiter2.getCountPerSecond()));
			}
		
	}
	
	public void testLimiter()
	{
		Properties prop = new Properties();
		prop.setProperty("maxCount", "10");
		prop.setProperty("maxCountPerSecond", "5");
		
		LimiterFactory.getInstance().init(prop);
		
		Limiter limiter = LimiterFactory.getInstance().getLimiter("test");
		for(int i=0;i<11;i++)
		{
			limiter.increment();
			
			System.out.println(String.format("i:%d count:%d countPersecend:%d", i,limiter.getCount(),limiter.getCountPerSecond()));
			
			if(i >= 4)
				assertEquals(true,limiter.isOverFlowPerSecond());
			else
				assertEquals(false,limiter.isOverFlowPerSecond());
			
			
			if(i>=9)
				assertEquals(true,limiter.isOverFlow());
			else
				assertEquals(false,limiter.isOverFlow());
		}
		
	}

}
