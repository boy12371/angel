/*
 * FAE, Feinno App Engine
 * 
 * Create by windcraft Sep 5, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.route.router;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.route.AppEngineRouteClient;
import com.feinno.appengine.runtime.configuration.ApplicationEntity;
import com.feinno.appengine.runtime.configuration.RunningWorkerEntity;
import com.feinno.rpc.RpcEndpointFactory;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.util.RandomPicker;

/**
 * 一种随机分配的AppBeanRouter
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppBeanRouterRandom extends AppBeanRouter
{
	private RandomPicker<RpcEndpoint> picker;
	private RpcEndpoint[] endpoints;

	public AppBeanRouterRandom(AppEngineRouteClient client, ApplicationEntity app, String contextType, String serviceType)
	{
		super(app.getCategoryMinusName(), contextType, serviceType);
		List<RunningWorkerEntity> workers = client.loadWorkers(this, app.getAppWorkerId());
		updateWorkers(workers);
	}

	@Override
	public RpcEndpoint route(AppContext ctx, String version)
	{
		return picker.pickOne();
	}

	@Override
	public RpcEndpoint routeByHash(AppContext ctx, String version, int hash)
	{
		hash = hash >= 0 ? hash : -hash;
		return endpoints[hash % endpoints.length];
	}

	@Override
	public void updateWorkers(List<RunningWorkerEntity> workers)
	{
		List<RpcEndpoint> eps = new ArrayList<RpcEndpoint>();
		if (workers != null)
		{
			for (RunningWorkerEntity w : workers)
			{
				String serviceUrl = w.getServiceUrl(this.getSerivceType());
				RpcEndpoint ep = RpcEndpointFactory.parse(serviceUrl);
				eps.add(ep);
			}
		}
		Collections.sort(eps, new Comparator<RpcEndpoint>()
		{

			@Override
			public int compare(RpcEndpoint o1, RpcEndpoint o2)
			{
				return o1.toString().compareTo(o2.toString());
			}

		});
		// TODO 先简单实现，再改为一致性HASH
		picker = new RandomPicker<RpcEndpoint>(eps);
		endpoints = new RpcEndpoint[eps.size()];
		for (int i = 0; i < eps.size(); i++)
		{
			endpoints[i] = eps.get(i);
		}
	}

	@Override
	public String toString()
	{
		String newline = System.getProperty("line.separator");
		StringBuffer sb = new StringBuffer();
		sb.append("appName=");
		sb.append(appName());
		sb.append(newline);
		for (RpcEndpoint ep : endpoints)
		{
			sb.append(ep.toString()).append(newline);
		}
		return sb.toString();
	}
}
