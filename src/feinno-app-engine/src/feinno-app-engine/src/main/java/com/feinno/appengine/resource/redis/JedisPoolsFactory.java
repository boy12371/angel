package com.feinno.appengine.resource.redis;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.feinno.appengine.configuration.FAE_Resource;
import com.feinno.appengine.configuration.FAE_ResourceKey;
import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;

public class JedisPoolsFactory {
	
	private static final JedisPoolsFactory INSTANCE = new JedisPoolsFactory();
	private final Integer VIRTUAL_NODE_COUNT = 160;		//虚节点数
	private KetamaNodeLocator<JedisPool> locator;
	private static final Logger LOGGER = LoggerFactory.getLogger(JedisPoolsFactory.class);
	
	private JedisPoolsFactory(){
		init();
	}
	
	private void init(){
		List<JedisPool> nodes = new ArrayList<JedisPool>();
		List<String> configs;
		try {
			configs = getConfig();
		} catch (ConfigurationException e) {
			LOGGER.error("init redispools error {}",e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			LOGGER.error("init redispools error {}",e);
			throw new RuntimeException(e);
		}
		Iterator<String> iterator = configs.iterator();
		while(iterator.hasNext()){
			String config = (String)iterator.next();
			Properties property = new Properties();
			try {
				property.load(new StringReader(config));
			} catch (IOException e) {
				LOGGER.error("load config{} in table FAE_Resource error{} ", config, e);
				throw new RuntimeException(e);
			}
			String ip = (String)property.get("IP");
			if(ip == null){
				LOGGER.error("the redis config column in table FAE_Resource set error, no ip address");
				throw new RuntimeException("the redis config column in table FAE_Resource set error, no ip address");
			}
			String port = (String)property.get("port");
			if(port == null){
				LOGGER.error("the redis config column in table FAE_Resource set error, no ip address");
				throw new RuntimeException("the redis config column in table FAE_Resource set error, no ip address");
			}
			
			String wait = (String)property.get("wait");
			
			JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();			
			if(wait != null)
				jedisPoolConfig.setMaxWait(Long.parseLong(wait));
			jedisPoolConfig.setTestOnBorrow(true);
			JedisPool pool = new JedisPool(jedisPoolConfig, ip, Integer.parseInt(port));		
			nodes.add(pool);
		}
		locator = new KetamaNodeLocator<JedisPool>(nodes, VIRTUAL_NODE_COUNT);
	}
	
	public static JedisPool getJedisPool(String uid){
		JedisPool pool = INSTANCE.locator.getPrimary(uid);
		return pool;
	}
	
	//数据库取数据
	private List<String> getConfig() throws ConfigurationException, IOException{
		List<String> list = new ArrayList<String>();
		ConfigTable<FAE_ResourceKey, FAE_Resource> resources;
		resources = ConfigurationManager.loadTable(FAE_ResourceKey.class, FAE_Resource.class, "FAE_Resource", null);		
		for(FAE_Resource rs : resources.getValues()){
			if(rs.getType().equalsIgnoreCase("redis")){
				String config = rs.getConfig();
				list.add(config);
			}
		}
		return list;
	}
}
