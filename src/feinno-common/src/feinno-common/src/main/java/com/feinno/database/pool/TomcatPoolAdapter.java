package com.feinno.database.pool;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TomcatPoolAdapter extends ConnectionPoolAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatPoolAdapter.class);

	TomcatPoolAdapter(Properties configs, DBConnectionPoolType type)
			throws Exception {
		super(configs, type);
		/*
		 * 在外界没有配置的情况下，预设的参数
		 */
		
		if(configs.containsKey("DriverClass") && !configs.containsKey("driverClassName"))
			configs.setProperty("driverClassName", configs.getProperty("DriverClass"));
		if(configs.containsKey("driverClass") && !configs.containsKey("driverClassName"))
			configs.setProperty("driverClassName", configs.getProperty("driverClass"));
		
		if(configs.containsKey("JdbcUrl") && !configs.containsKey("url"))
			configs.setProperty("url", configs.getProperty("JdbcUrl"));
		if(configs.containsKey("jdbcUrl") && !configs.containsKey("url"))
			configs.setProperty("url", configs.getProperty("jdbcUrl"));
				
		if(!configs.containsKey("initialSize"))
			configs.setProperty("initialSize", "10");
		if(!configs.containsKey("maxActive"))
			configs.setProperty("maxActive", "30");
		if(!configs.containsKey("minIdle"))
			configs.setProperty("minIdle", "10");
		if(!configs.containsKey("maxIdle"))
			configs.setProperty("maxIdle", "30");
		if(!configs.containsKey("maxWait"))
			configs.setProperty("maxWait", "30000");
		
		if(configs.containsKey("user") && !configs.containsKey("username"))
			configs.setProperty("username", configs.getProperty("user"));
			
		
		dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
		// 通过反射动态设定数据源属性
		Class clazz = org.apache.tomcat.jdbc.pool.DataSource.class;
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
