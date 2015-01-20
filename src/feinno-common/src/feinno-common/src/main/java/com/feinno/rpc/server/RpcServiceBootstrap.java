/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-11-25
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.rpc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.RpcException;
import com.feinno.rpc.channel.RpcServerChannel;
import com.feinno.rpc.channel.RpcServerTransaction;
import com.feinno.rpc.server.builtin.RpcBuiltinService;
import com.feinno.util.EventHandler;
import com.feinno.util.StringUtils;

/**
 * Rpc服务注册及引导启动类
 * <pre>
 * <code>
 * 	
 * </code>
 * </pre>
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcServiceBootstrap
{
	private static Object syncRoot = new Object();
	private static List<RpcServerChannel> channels = new ArrayList<RpcServerChannel>();
	private static RpcServiceDispather dispather = new RpcServiceDispather();
	private static Logger LOGGER = LoggerFactory.getLogger(RpcServiceBootstrap.class);
	
	static{
		//默认提供获取此服务的内部信息
		dispather.addService(new RpcBuiltinService());
	}
	
	/**
	 * 
	 * 注册服务器端Channel
	 * @param channel
	 */
	public static void registerChannel(RpcServerChannel channel)
	{
		synchronized (syncRoot) {
			channels.add(channel);	
		}
		channel.getTransactionCreated().addListener(new EventHandler<RpcServerTransaction>() {
			@Override
			public void run(Object sender, RpcServerTransaction args)
			{
				dispather.processTransaction(args);
			}
		});
	}
	
	/**
	 * 
	 * 设置线程池
	 * @param channel
	 */
	public static void setExecutor(Executor executor)
	{
		dispather.setExecutor(executor);
	}
	
	/**
	 * 
	 * 注册Rpc服务
	 * @param service
	 */
	public static void registerService(Object service)
	{
		dispather.addService(service);
	}

	/**
	 * 
	 * 获取一个已经注册了的Rpc服务
	 * @param service
	 * @return
	 */
	public static RpcServiceBase getService(String service)
	{
		return dispather.getService(service);
	}
	
	/**
	 * 获取运行时的全部服务名称
	 * 
	 * @return
	 */
	public static String[] getRunningService() {
		return dispather.getRunningService();
	}
	
	/**
	 * 
	 * 启动所有Channel
	 * @throws RpcException
	 */
	public static void start() throws Exception
	{
		for (RpcServerChannel channel: channels) {
			try {
				channel.start();
				LOGGER.error("start channel ok {}", channel.getServerEndpoint());
			} catch (Exception ex) {
				LOGGER.error("start channel failed {} ", ex);
			}
		}
	}
	
	/**
	 * 
	 * 停止所有Channel
	 * @throws RpcException
	 */	
	public static void stop() throws Exception
	{
		for (RpcServerChannel channel: channels) {
			channel.stop();
		}
	}
	
	/**
	 *  
	 * 获取一个已经注册了的RpcServerChannel
	 * 
	 * @param protocol
	 * @param urlMark
	 * @return
	 */
	public static RpcServerChannel getServerChannel(String protocol, String urlMark)
	{
		synchronized (syncRoot) {
			for (RpcServerChannel channel: channels) {
				RpcEndpoint ep = channel.getServerEndpoint();
				if (ep.getProtocol().equals(protocol)) {
					if (!StringUtils.isNullOrEmpty(urlMark) && ep.toString().contains(urlMark)) {
						return channel;
					}
				}
			}	
		}
		return null;
	}
}
