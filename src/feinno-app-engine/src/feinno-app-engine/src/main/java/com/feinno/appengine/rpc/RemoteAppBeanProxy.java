/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.rpc;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.AppContext;
import com.feinno.appengine.ContextUri;
import com.feinno.appengine.annotation.ContextDemands;
import com.feinno.appengine.annotation.PeerSite;
import com.feinno.appengine.configuration.AppBeanAnnotation;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.configuration.AppBeanAnnotationsLoader;
import com.feinno.appengine.route.AppEngineRouteClient;
import com.feinno.appengine.runtime.AppEngineErrorCode;
import com.feinno.appengine.runtime.AppEngineException;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.ha.ServiceSettings;
import com.feinno.rpc.channel.RpcBody;
import com.feinno.rpc.channel.RpcClientTransaction;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.RpcException;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcRequest;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.rpc.channel.inproc.RpcInprocEndpoint;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.client.RpcMethodStub;
import com.feinno.rpc.client.RpcProxy;
import com.feinno.rpc.client.RpcProxyFactory;
import com.feinno.serialization.protobuf.types.ProtoString;
import com.feinno.util.Action;
import com.feinno.util.EventHandler;
import com.feinno.util.GenericsUtils;
import com.feinno.util.ObjectHelper;
import com.feinno.util.StringUtils;

/**
 * 调用对方RemoteAppBean的代理类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RemoteAppBeanProxy<A, R, C extends AppContext>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteAppBeanProxy.class);

	private String categoryMinusName;
	private String parentVersion;
	private String parentName;

	private Class<?> resultsClazz;
	private int contextDemands;

	private RemoteAppBean<A, R, C> inprocBean;
	private RemoteAppBeanRouteManager routeManager;
	private RpcEndpoint gateWayEp = null;
	//TODO 缓存，临时方案
	private Hashtable<String,RpcEndpoint> cachedEndpoint = new Hashtable<String,RpcEndpoint>();
	private List<RpcEndpoint> cachedEndpointList = new ArrayList<RpcEndpoint>();
	private AtomicInteger getRouterCount = new AtomicInteger(0);
	private static final int REFRESHCOUNT = 50;
	private static final int REFRESHINTEVAL = 1000;

	/**
	 * 
	 * 创建一个访问AppBean的客户端代理类，用这个构造函数创建出的RemoteAppBeanProxy具有如下特性<br>
	 * 1. 会将依赖的RemoteAppBean加载到本地进程<br>
	 * 2. 当访问的RemoteAppBean路由到本Site内时，会转为进程内调用<br>
	 * 3. 如果本Site的Zookeeper中存在其他RemoteAppBean的部署，永远不会生效<br>
	 * 4. 当访问其他Site的RemoteAppBean时，转为远程调用<br>
	 * 
	 * @param parent
	 * @param beanClazz
	 * @throws Exception
	 */
	public RemoteAppBeanProxy(AppBean parent, Class<? extends RemoteAppBean<A, R, C>> beanClazz) throws Exception
	{
		this(parent);

		//
		// 从目标RemoteAppBean的类型中提取返回参数类型
		resultsClazz = GenericsUtils.getSuperClassGenricType(beanClazz, 1);

		 //TODO 热加载，不走进程内调用，先注销掉 inprocBean
		if (beanClazz.getAnnotation(PeerSite.class) != null) {
			inprocBean = (RemoteAppBean<A, R, C>) AppEngineManager.INSTANCE.loadAppBean(beanClazz);
		}

		AppBeanAnnotations annos = AppBeanAnnotationsLoader.getAppBeanAnnotaions(beanClazz);
		categoryMinusName = annos.getCategoryMinusName();

		//
		// 获取@ContextDemands
		// TODO: 没有明确规范指明如何使用ContextDemands
		AppBeanAnnotation contextDemandsAnno = annos.getAppBeanAnnotation(ContextDemands.class);
		if (contextDemandsAnno != null) {
			contextDemands = Integer.parseInt(contextDemandsAnno.getFieldValue("value"));
		} else {
			contextDemands = 0;
		}
	}

	/**
	 * 
	 * 仅通过名字创建，一般用于非本工程依赖的情况下的调用
	 * 
	 * @param parent
	 * @param categoryMinusName
	 * @throws Exception
	 */
	public RemoteAppBeanProxy(AppBean parent, String categoryMinusName) throws Exception
	{
		this(parent);

		// 从父类的RemoteAppBeanProxy中提取返回参数类型
		resultsClazz = GenericsUtils.getSuperClassGenricType(this.getClass(), 1);

		//
		// TODO: 标注在本地，或者放到远端?
		this.categoryMinusName = categoryMinusName;
		contextDemands = 0;
	}
	
	public RemoteAppBeanProxy(AppBean parent, String categoryMinusName,Class<?> resultsClazz) throws Exception
	{	
		this(parent);

		this.resultsClazz = resultsClazz;
		
		//
		// TODO: 标注在本地，或者放到远端
		this.categoryMinusName = categoryMinusName;
		contextDemands = 0; 
	}


	/**
	 * 
	 * 创建一个会通过AEG进行跨Site调用的RemoteAppBean
	 * 
	 * @param parent
	 * @param categoryMinusName
	 * @param gateWayEp
	 * @throws Exception
	 */
	public RemoteAppBeanProxy(AppBean parent, String categoryMinusName, RpcEndpoint gateWayEp) throws Exception
	{
		this(parent);

		// 从父类的RemoteAppBeanProxy中提取返回参数类型
		resultsClazz = GenericsUtils.getSuperClassGenricType(this.getClass(), 1);

		//
		// TODO: 标注在本地，或者放到远端?
		this.categoryMinusName = categoryMinusName;
		contextDemands = 0;

		if (gateWayEp == null)
			throw new Exception("gateWayEp is null");
		this.gateWayEp = gateWayEp;
	}

	/**
	 * 
	 * 代码复用的内部调用
	 * @param parent
	 * @throws Exception
	 */
	private RemoteAppBeanProxy(AppBean parent) throws Exception
	{
		if (parent == null) {
			parentVersion = "0.0.0.Unknown";
			parentName = "Unknown-Unknown";

		} else {
			parentVersion = parent.getVersion();
			parentName = parent.getCategoryMinusName();
		}
		String zkHosts = AppEngineManager.INSTANCE.getSettings().getZkHosts();
//		AppEngineRouteClient.initialize(RemoteAppBean.class,zkHosts);
		// 远程调用是否强制走本Site
		boolean isGlobal = AppEngineManager.INSTANCE.getSettings().isGlobal();
		if (isGlobal) {
			AppEngineRouteClient.initialize(RemoteAppBean.class,zkHosts, isGlobal);
		} else {
			AppEngineRouteClient.initialize(RemoteAppBean.class,zkHosts);
		}
		// 远程调用是否强制走本Site
		//no zk 方式，routeManager不再可用
		if(!ServiceSettings.INSTANCE.isNoZK())
			routeManager = (RemoteAppBeanRouteManager) AppEngineRouteClient.getInstance().getRouteManager(
				RemoteAppBean.class);
	}

	/**
	 * @return the categoryMinusName
	 */
	public String getCategoryMinusName()
	{
		return this.categoryMinusName;
	}

	/**
	 * 
	 * 同步调用, 使用默认的超时时间 
	 * 
	 * @param ctx
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public R syncInvoke(C ctx, A args) throws Exception
	{
		return syncInvoke(ctx, args, 90 * 1000);
	}

	/**
	 * 
	 * 同步调用
	 * 
	 * @param ctx
	 * @param args
	 * @param timeoutMs
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public R syncInvoke(C ctx, A args, int timeoutMs) throws Exception
	{
		RpcFuture future = invoke(ctx, args);
		future.await(timeoutMs);
		return (R)future.getResult(resultsClazz);
	}

	/**
	 * 
	 * 异步调用，兼容不易更改的老代码
	 * 
	 * @param ctx
	 * @param args
	 * @param callback
	 * @throws Exception
	 */
	public void invoke(C ctx, A args, final Action<AsyncResults<R>> callback) throws Exception
	{
		RpcFuture future = invoke(ctx, args);
		future.addListener(new EventHandler<RpcResults>() {
			@SuppressWarnings("unchecked")
			@Override
			public void run(Object sender, RpcResults r)
			{
				RpcException error = r.getError();
				AsyncResults<R> results;
				if (error != null) {
					results = new AsyncResults<R>(error, null);
				} else {
					results = new AsyncResults<R>(error, (R)r.getValue(resultsClazz));
				}
				callback.run(results);
			}
		});
	}
	
	/**
	 * 
	 * 异步调用，兼容不易更改的老代码
	 * 
	 * @param ctx
	 * @param args
	 * @param callback
	 * @throws Exception
	 */
	public void invoke(C ctx, A args, final Action<AsyncResults<R>> callback,int timeout) throws Exception
	{
		RpcFuture future = invoke(ctx, args);
		future.setTimeout(timeout);
		future.addListener(new EventHandler<RpcResults>() {
			@SuppressWarnings("unchecked")
			@Override
			public void run(Object sender, RpcResults r)
			{
				RpcException error = r.getError();
				AsyncResults<R> results;
				if (error != null) {
					results = new AsyncResults<R>(error, null);
				} else {
					results = new AsyncResults<R>(error, (R)r.getValue(resultsClazz));
				}
				callback.run(results);
			}
		});
	}
	
	public RpcFuture invoke(C ctx, A args) throws Exception
	{
		RpcEndpoint ep = this.gateWayEp;

		//
		// 如果存在本Site内的对象，使用Site内路由的方式转化为本机调用
		if (inprocBean != null) {
			String targetSite = ctx.getSiteName();
			String currentSite = AppEngineManager.INSTANCE.currentSite();
			if (StringUtils.strEquals(targetSite, currentSite)) {
				ep = RpcInprocEndpoint.INSTANCE;
			}
		}

		if (ep == null) {
			ep = routeManager.routeRemoteAppBean(categoryMinusName, ctx, parentVersion);
			if (ep == null) {
				throw new AppEngineException(AppEngineErrorCode.APPBEAN_NOT_FOUND, categoryMinusName);
			}
		}
		
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, RemoteAppHost.SERVICE_NAME, categoryMinusName);
		final RpcClientTransaction tx = stub.createTransaction(args);
		RpcRequest request = tx.getRequest();
		request.getHeader().setFromService(parentName);

		//
		// 设置发起方的Bean版本
		RpcBody rbVersion = new RpcBody(new ProtoString(parentVersion), false);
		request.putExtension(RemoteAppTxImpl.EXTENSION_APPBEAN_FROM, rbVersion);

		if (ctx != null) {
			ContextUri uri = ctx.getContextUri();
			if (uri != null) {
				ProtoString pb = new ProtoString(uri.toString());
				RpcBody rbUri = new RpcBody(pb, false);
				request.putExtension(RemoteAppTxImpl.EXTENSION_CONTEXT_URI, rbUri);
			}

			byte[] data = ctx.encode(contextDemands);
			if (data != null) {
				RpcBody rbData = new RpcBody(data, false);
				request.putExtension(RemoteAppTxImpl.EXTENSION_CONTEXT_DATA, rbData);
			}
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("RemoteAppBeanProxy args: {} ctx: {}" + ObjectHelper.dumpObject(args), categoryMinusName, ctx);
		}

		RpcFuture future = tx.begin();
		return future;
	}
	
	/**
	 * 
	 * 异步调用
	 * @param routeDirct true:直接通过AEG获取地址发送；false:发送AEG并通过AEG中转
	 * @param ctx
	 * @param args
	 * @param callback
	 * @throws Exception
	 */
	public void invoke(boolean routeDirect,final C ctx, final A args, final Action<AsyncResults<R>> callback) throws Exception
	{
		RpcEndpoint ep = this.gateWayEp;
		//
		// 如果存在本Site内的对象，使用Site内路由的方式转化为本机调用
		if (inprocBean != null) {
			String targetSite = ctx.getSiteName();
			String currentSite = AppEngineManager.INSTANCE.currentSite();
			if (StringUtils.strEquals(targetSite, currentSite)) {
				ep = RpcInprocEndpoint.INSTANCE;
				innerInvoke(ep,ctx, args, callback);
				return;
			}
			
		}
				
		
		//如果不走指定的AEG:
		if (ep == null) {
			ep = routeManager.routeRemoteAppBean(categoryMinusName, ctx, parentVersion);
			if (ep == null) {
				callback.run(new AsyncResults<R>(new Exception("No route found" + categoryMinusName), null));
				return;
			}
			innerInvoke(ep,ctx, args, callback);
			return;
		}
		//如果走指定的AEG：
		else if(routeDirect)
		{
			LOGGER.info("routeDirect categoryMinusName:{}",categoryMinusName);
			
			int i = getRouterCount.incrementAndGet() - 1;			
			
			if(i>REFRESHINTEVAL)
				getRouterCount.set(0);
			RpcProxy proxy = RpcProxyFactory.getProxy(ep, RemoteAppHost.ROUTER_SERVICE_NAME);
			GetRouterArgs getRouterArgs = new GetRouterArgs();
			getRouterArgs.setCategoryMinusName(categoryMinusName);
			getRouterArgs.setFromVersion(parentVersion);
			if(ctx!=null)
				getRouterArgs.setContextUri(ctx.getContextUri().toString());
			if(i<REFRESHCOUNT || cachedEndpoint.size() == 0)
			{
					if(i == 0)
						cachedEndpoint.clear();
					
					proxy.invoke("getRouter", getRouterArgs, new Action<RpcResults>(){

						@Override
						public void run(RpcResults a) {
						if(a.getError()!=null)
						{
							callback.run(new AsyncResults<R>(a.getError(), null));
							return;
						}
						else
						{
							try
							{
								ProtoString str = (ProtoString) a.getValue(ProtoString.class);
								RpcEndpoint remoteAppBeanEp = RpcTcpEndpoint.parse(str.getValue());
								cachedEndpoint.put(str.getValue(), remoteAppBeanEp);
								
								if (remoteAppBeanEp == null) {
									callback.run(new AsyncResults<R>(new Exception("routeDirect:No route found" + categoryMinusName), null));
									return;
								}
								innerInvoke(remoteAppBeanEp, ctx,  args, callback);
							}catch(IllegalArgumentException e)
							{
								LOGGER.error("routeDirect:remoteAppBeanEp parse failed {}", e);
								callback.run(new AsyncResults<R>(e, null));
							}
							catch(Exception e2)
							{
								callback.run(new AsyncResults<R>(e2, null));
							}
						}
					
					}
				
				});
			}
			else
			{
				if(i == REFRESHCOUNT)
				{
					List<RpcEndpoint> newList = new ArrayList<RpcEndpoint>(cachedEndpoint.values());
					cachedEndpointList = newList;			
				}
					
				int rand = i%cachedEndpoint.size();	
				RpcEndpoint remoteAppBeanEp2 = null;
				if(cachedEndpointList.size() != 0)
					remoteAppBeanEp2 = cachedEndpointList.get(rand);
				else
					remoteAppBeanEp2 = cachedEndpoint.values().iterator().next();
				
				innerInvoke(remoteAppBeanEp2,ctx, args, callback);
				
			}
		}
		else
			 innerInvoke(ep,ctx, args, callback);
		
	}
	
	private void innerInvoke(RpcEndpoint ep,C ctx, A args, final Action<AsyncResults<R>> callback) throws Exception
	{
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, RemoteAppHost.SERVICE_NAME, categoryMinusName);
		
		final RpcClientTransaction tx = stub.createTransaction(args);
		RpcRequest request = tx.getRequest();
		request.getHeader().setFromService(parentName);

		//
		// 设置发起方的Bean版本
		RpcBody rbVersion = new RpcBody(new ProtoString(parentVersion), false);
		request.putExtension(RemoteAppTxImpl.EXTENSION_APPBEAN_FROM, rbVersion);

		if (ctx != null) {
			ContextUri uri = ctx.getContextUri();
			if (uri != null) {
				ProtoString pb = new ProtoString(uri.toString());
				RpcBody rbUri = new RpcBody(pb, false);
				request.putExtension(RemoteAppTxImpl.EXTENSION_CONTEXT_URI, rbUri);
			}
			
			byte[] data = ctx.encode(contextDemands);
			if (data != null) {
				RpcBody rbData = new RpcBody(data, false);
				request.putExtension(RemoteAppTxImpl.EXTENSION_CONTEXT_DATA, rbData);
			}
		} 
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("RemoteAppBeanProxy args: {} ctx: {}" + args, categoryMinusName, ctx);
		}
		
		RpcFuture future = tx.begin();
		future.addListener(new EventHandler<RpcResults>() {
			@SuppressWarnings("unchecked")
			@Override
			public void run(Object sender, RpcResults r)
			{
				RpcException error = r.getError();
				AsyncResults<R> results;
				if (error != null) {
					results = new AsyncResults<R>(error, null);
				} else {
					results = new AsyncResults<R>(error, (R)r.getValue(resultsClazz));
				}
				callback.run(results);
			}
		});
	}
}
