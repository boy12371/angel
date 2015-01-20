package com.feinno.database.pool;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCPDataSource;

public class BoneCPPoolAdapter extends ConnectionPoolAdapter{

	private static final Logger LOGGER = LoggerFactory.getLogger(BoneCPPoolAdapter.class);
	
	BoneCPPoolAdapter(Properties configs, DBConnectionPoolType type)
			throws Exception {
		super(configs, type);
		/*
		 * 在外界没有配置的情况下，预设一些BoneCP的参数
		 */
		if(!configs.containsKey("acquireIncrement"))
			configs.setProperty("acquireIncrement", "5");
		if(!configs.containsKey("acquireRetryDelayInMs"))
			configs.setProperty("acquireRetryDelayInMs", "500");
		if(!configs.containsKey("connectionTimeoutInMs"))
			configs.setProperty("connectionTimeoutInMs", "20000");
		if(!configs.containsKey("idleConnectionTestPeriodInSeconds"))
			configs.setProperty("idleConnectionTestPeriodInSeconds", "60");
	
		if(!configs.containsKey("partitionCount"))
			configs.setProperty("partitionCount", "3");
		
		if(!configs.containsKey("minConnectionsPerPartition"))
			configs.setProperty("minConnectionsPerPartition", "3");
		
		if(!configs.containsKey("maxConnectionsPerPartition"))
			configs.setProperty("maxConnectionsPerPartition", "10");
		
		
		if(!configs.containsKey("idleMaxAgeInSeconds"))
			configs.setProperty("idleMaxAgeInSeconds", "30");
		
		if(configs.containsKey("user") && !configs.containsKey("username"))
			configs.setProperty("username", configs.getProperty("user"));
			
			
		    dataSource = new BoneCPDataSource(); 
			// 通过反射动态设定数据源属性
			Class clazz = BoneCPDataSource.class;
			Method[] methods = null;
			String key = null;
			String val = null;
			String methodName = null;
			Enumeration eunm = configs.propertyNames();
			while (eunm.hasMoreElements()) {
				key = (String) eunm.nextElement();
				val = configs.getProperty(key);
				methodName = "set" + key.substring(0, 1).toUpperCase()
						+ key.substring(1);
				methods = clazz.getMethods();
				for (Method method : methods) {
					if (method.getName().equals(methodName)) {
						try {
							if (method.getParameterTypes()[0].getName() == "int") {
								method.invoke(dataSource, Integer.parseInt(val));
							} else if (method.getParameterTypes()[0].getName() == "long") {
								method.invoke(dataSource, Long.parseLong(val));
							} else if (method.getParameterTypes()[0].getName() == "float") {
								method.invoke(dataSource, Float.parseFloat(val));
							} else if (method.getParameterTypes()[0].getName() == "double") {
								method.invoke(dataSource, Double.parseDouble(val));
							} else if (method.getParameterTypes()[0].getName() == "java.lang.String") {
								method.invoke(dataSource, val);
							} else {
								method.invoke(dataSource, val);
							}
						} catch (Exception e) {
							LOGGER.error("init database error{}", e);
							throw e;
						}
						break;
					}
				}

			}
	}

}
