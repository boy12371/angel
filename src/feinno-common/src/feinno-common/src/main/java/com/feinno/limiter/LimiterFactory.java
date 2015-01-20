package com.feinno.limiter;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.mysql.jdbc.StringUtils;

public class LimiterFactory {
		
		private static LimiterFactory instance = new LimiterFactory();	;
		
		private static final ConcurrentHashMap<String,Limiter> limiters = new  ConcurrentHashMap<String,Limiter>();
		private static final ConcurrentHashMap<String,String> prop = new ConcurrentHashMap<String,String>();
		
		private static Timer timer = new Timer();
		
		
		private int maxCount = 4096;
		private int maxCountPerSecond = 512;
		private long period = 60*1000; //1分钟
		private boolean isInit = false;
		
				
		public static LimiterFactory getInstance()
		{
			return instance;
		}
		/**
		 * Properties示例如下：
		 * 
		 * maxCount=2048
		 * maxCountPerSecond=256
		 * 
		 * core-RegisterSipcApp.maxCount=4096
		 * core-RegisterSipcApp.maxCountPerSecond=512
		 * 
		 * @param configs
		 */
		public synchronized void init(Properties configs)
		{
			if(!isInit)
			{
				String key = null;
				String val = null;
				Enumeration eunm = configs.propertyNames();
				while (eunm.hasMoreElements()) {
					key = (String) eunm.nextElement();
					val = configs.getProperty(key);
					prop.put(key, val);
				}
				if(prop.containsKey("maxCount"))
					maxCount = Integer.parseInt(prop.get("maxCount"));
				if(prop.containsKey("maxCountPerSecond"))
					maxCountPerSecond = Integer.parseInt(prop.get("maxCountPerSecond"));
				isInit = true;
				
				timer.scheduleAtFixedRate(new TimerTask(){
					@Override
					public void run() {
						for(Limiter limiter:limiters.values())
							limiter.resetCounter();					
					}				
				}, period, period);
				
				timer.scheduleAtFixedRate(new TimerTask(){
					@Override
					public void run() {
						for(Limiter limiter:limiters.values())
							limiter.resetCounterPerSecond();					
					}				
				}, 1000, 1000);
			}
		}
		
		/**
		 * 限制 maxCount,maxCountPerSecond
		 * 可以按照信令获取，没有配置的话，用通用配置
		 * @param key
		 * @return
		 */
		public Limiter getLimiter(String key)
		{
			int mc = maxCount;
			int mcp = maxCountPerSecond;
			String keyMc = prop.get(key+".maxCount");
			String keyMcp = prop.get(key+".maxCountPerSecond");
			
			if(!StringUtils.isNullOrEmpty(keyMc))
				mc = Integer.parseInt(keyMc);
			if(!StringUtils.isNullOrEmpty(keyMcp))
				mcp = Integer.parseInt(keyMcp);
			
			return getLimiter(key,mc,mcp);
		}
				
		/**
		 * 限制 maxCount,maxCountPerSecond
		 * @param key
		 * @return
		 */
		public Limiter getLimiter(String key,int maxCount, int maxCountPerSecond)
		{
			if(!limiters.containsKey(key))
			{
				synchronized(limiters)
				{
					if(!limiters.containsKey(key))
					{
						final Limiter limiter = new Limiter(maxCount,maxCountPerSecond);
				
						limiters.put(key, limiter);
					}
				}
			}
			return limiters.get(key);
		}


}
