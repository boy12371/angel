/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.resource;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.configuration.FAE_Resource;
import com.feinno.appengine.configuration.FAE_ResourceKey;
import com.feinno.appengine.configuration.FAE_ResourcePolicy;
import com.feinno.appengine.job.JobResourceLocator;
import com.feinno.appengine.resource.database.DatabaseProxy;
import com.feinno.appengine.resource.database.DatabaseResource;
import com.feinno.appengine.resource.redis.RedisProxy;
import com.feinno.appengine.resource.redis.RedisResource;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.initialization.Initializer;
import com.feinno.util.DictionaryList;

/**
 * 在AppBean中获取访问资源的代理 工厂类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ResourceFactory {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ResourceFactory.class);
	private static final String Xenon = "Xenon." ;

	private static Hashtable<String, ResourceGroup<?>> resources;
	private static Hashtable<String, ResourceProxy> proxys;
	public static ConfigTable<FAE_ResourceKey, FAE_Resource> FAE_RESOURCE_TABLE;

	@Initializer
	public static void initialize() throws Exception {
		ConfigurationManager
				.loadTable(
						FAE_ResourceKey.class,
						FAE_Resource.class,
						"FAE_Resource",
						new ConfigUpdateAction<ConfigTable<FAE_ResourceKey, FAE_Resource>>() {
							@Override
							public void run(
									ConfigTable<FAE_ResourceKey, FAE_Resource> table)
									throws Exception {
								FAE_RESOURCE_TABLE = table;
								DictionaryList<String, Resource> list = new DictionaryList<String, Resource>();
								for (FAE_Resource r : table.getValues()) {
									switch (ResourceType.valueOf(r.getType())) {
									case DATABASE:
										list.put(r.getName(),
												new DatabaseResource(r));
										break;
									case REDIS:
										list.put(r.getName(),
												new RedisResource(r));
										break;
									default:
										//忽略不支持的资源
										continue;
									}
									LOGGER.info("create resource for {} {}",
											r.getName(), r.getType());
								}
								Hashtable<String, ResourceGroup<?>> temp = new Hashtable<String, ResourceGroup<?>>();
								for (String k : list.keys()) {
									temp.put(k, new ResourceGroup<Resource>(
											list.get(k)));
								}
								resources = temp;
							}
						});

		ConfigurationManager
				.loadTable(
						String.class,
						FAE_ResourcePolicy.class,
						"FAE_ResourcePolicy",
						new ConfigUpdateAction<ConfigTable<String, FAE_ResourcePolicy>>() {
							@Override
							public void run(
									ConfigTable<String, FAE_ResourcePolicy> table)
									throws Exception {
								Hashtable<String, ResourceProxy> tempProxys = new Hashtable<String, ResourceProxy>();
								for (FAE_ResourcePolicy p : table.getValues()) {
									switch (p.getType()) {
									case DATABASE:
									case REDIS:
										break;
									default:
										//忽略不支持的资源
										continue;
									}
									LOGGER.info(
											"create resource policy for {} {}",
											p.getName(), p.getLocator());
									ResourceGroup<? extends Resource> group = resources
											.get(p.getName());
									ResourceLocator locator = AppEngineManager.INSTANCE
											.getLocator(p.getLocator());
									locator.setParams(group, p
											.getLocatorParams().split(","));
									ResourceProxy proxy = tempProxys.get(p
											.getName());
									if (proxy == null) {
										switch (p.getType()) {
										case DATABASE:
											proxy = new DatabaseProxy();
											break;
										case REDIS:
											proxy = new RedisProxy();
											break;
										}
										JobResourceLocator jl = new JobResourceLocator();
										jl.setParams(group, null);
										proxy.addLocator("job", jl);
										tempProxys.put(p.getName(), proxy);
									}
									proxy.addLocator(p.getProtocol(), locator);
								}
								proxys = tempProxys;
							}
						});
	}

	public static DatabaseProxy getDatabaseProxy(String name) {
		DatabaseProxy proxy = (DatabaseProxy) proxys.get(name);
		if (proxy == null)
			throw new IllegalArgumentException("Unknown database:" + name);
		return proxy;
	}

	public static RedisProxy getRedisProxy(String name) {
		RedisProxy proxy = (RedisProxy) proxys.get(name);
		if (proxy == null)
			throw new IllegalArgumentException("Unknown redis:" + name);
		return proxy;

	}

	public static ResourceGroup<?> getResourceGroup(ResourceType type,
			String name) {
		ResourceGroup<?> group = resources.get(name);
		if (group == null && ResourceType.NONE == type) {
			List<Resource> list = new ArrayList<Resource>();
			Resource res = new FakeResource();
			list.add(res);
			group = new ResourceGroup<Resource>(list,type);
		} else if (group == null)
			throw new IllegalArgumentException("Unknown resource:" + name);
		return group;
	}
}
