/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.resource.redis;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import com.feinno.appengine.configuration.FAE_Resource;
import com.feinno.appengine.resource.Resource;
import com.feinno.appengine.resource.ResourceType;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RedisResource implements Resource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisResource.class);
	private FAE_Resource resource;
	private JedisPool pool;

	public RedisResource(FAE_Resource r) throws Exception
	{
	    resource = r;
		Properties properties = new Properties();
		properties.load(new StringReader(r.getConfig()));
		String ip = (String) properties.get("IP");
		int port = Integer.parseInt(properties.get("port").toString());
		int timeout = Protocol.DEFAULT_TIMEOUT;

		if (properties.get("timeout") != null)
			timeout = Integer.parseInt((String) properties.get("timeout"));

		Config poolConfig = getPoolConfig(properties);
		pool = new JedisPool(poolConfig, ip, port, timeout);
		
	}

	@Override
	public ResourceType type()
	{
		return ResourceType.REDIS;
	}

	@Override
	public String name()
	{
		return resource.getName();
	}

	@Override
	public int index()
	{
		return resource.getIndex();
	}
	
	Jedis getClient()
	{
		return pool.getResource();
	}
    void returnClient(Jedis jedis)
    {
        pool.returnResource(jedis);
    }
    void returnBrokenClient(Jedis jedis)
    {
        pool.returnBrokenResource(jedis);
    }

	// 通过反射动态设定数据源属性
	private Config getPoolConfig(Properties configs) throws Exception
	{
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		Class<JedisPoolConfig> clazz = JedisPoolConfig.class;
		Method[] methods = null;
		String key = null;
		String val = null;
		String methodName = null;
		Enumeration<?> eunm = configs.propertyNames();
		while (eunm.hasMoreElements()) {
			key = (String) eunm.nextElement();
			val = configs.getProperty(key);
			if (key.equalsIgnoreCase("ip") || key.equalsIgnoreCase("port") || key.equalsIgnoreCase("timeout"))
				continue;
			methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
			methods = clazz.getMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					try {
						if (method.getParameterTypes()[0].getName() == "int") {
							method.invoke(poolConfig, Integer.parseInt(val));
						} else if (method.getParameterTypes()[0].getName() == "long") {
							method.invoke(poolConfig, Long.parseLong(val));
						} else if (method.getParameterTypes()[0].getName() == "float") {
							method.invoke(poolConfig, Float.parseFloat(val));
						} else if (method.getParameterTypes()[0].getName() == "double") {
							method.invoke(poolConfig, Double.parseDouble(val));
						} else if (method.getParameterTypes()[0].getName() == "java.lang.String") {
							method.invoke(poolConfig, val);
						} else {
							method.invoke(poolConfig, val);
						}
					} catch (Exception e) {
						LOGGER.error("init redis server error{}", e);
						throw e;
					}
					break;
				}
			}
		}
		return poolConfig;
	}
}
