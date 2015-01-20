/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Apr 2, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.client;

import com.feinno.rpc.channel.RpcClientTransaction;
import com.feinno.rpc.channel.RpcConnectionGroup;
import com.feinno.rpc.channel.RpcConnectionReal;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.util.Func;
import com.feinno.util.LoopCounter;

/**
 * 
 * 用于负载均衡连接的RpcClientTransactionHandler, 针对一个IP地址创建3条连接, 
 * 并且当每条连接超时后(180秒)创建, 以避免引起负载均衡不均的问题
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcClientTransactionHandlerNLB implements RpcClientTransactionHandler
{
	private static final int NLB_CONNECTIONS = 3;
	private static final int NLB_CONNECTION_LIFE_SECOND = 180; 
	
	private LoopCounter counter;
	private RpcClientTransactionHandlerDirect[] handlers;
	private RpcConnectionGroup group;
	
	public RpcClientTransactionHandlerNLB(RpcEndpoint ep, String service, String method)
	{
		counter = new LoopCounter(NLB_CONNECTIONS);
		handlers = new RpcClientTransactionHandlerDirect[NLB_CONNECTIONS];
		group = ep.getClientChannel().getConnectionGroup(ep, NLB_CONNECTIONS);
		
		for (int i = 0; i < NLB_CONNECTIONS; i++) {
			handlers[i] = new RpcClientTransactionHandlerDirect(ep, service, method, createFunc(i));
			handlers[i].setMaxLife(NLB_CONNECTION_LIFE_SECOND);
		}
	}
	
	public RpcClientTransaction createTransaction()
	{
		int i = counter.next();
		RpcClientTransactionHandlerDirect handler = handlers[i];
		return handler.createTransaction();
	}
	
	private Func<Void, RpcConnectionReal> createFunc(final int i)
	{
		return new Func<Void, RpcConnectionReal>() {
			@Override
			public RpcConnectionReal exec(Void obj)
			{
				return group.getConnection(i);
			}
		};
	}
}
