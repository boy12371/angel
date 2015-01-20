/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Apr 2, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.client;

import com.feinno.rpc.channel.RpcClientMethodCache;
import com.feinno.rpc.channel.RpcClientMethodManager;
import com.feinno.rpc.channel.RpcClientTransaction;
import com.feinno.rpc.channel.RpcConnection;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.RpcRequestHeader;
import com.feinno.util.ServiceEnviornment;

/**
 * 
 * 用于短连接的RpcClientTransactionHandler
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcClientTransactionHandlerShort implements RpcClientTransactionHandler
{
	private RpcEndpoint ep;
	private String service;
	private String method;
	
	public RpcClientTransactionHandlerShort(RpcEndpoint ep, String service, String method)
	{
		this.ep = ep;
		this.service = service;
		this.method = method;
	}
	
	public RpcClientTransaction createTransaction()
	{
		RpcConnection conn = ep.getClientChannel().createConnection(ep);
		RpcClientMethodCache cache = RpcClientMethodManager.INSTANCE.getMethodCache(ep, service, method);
		
		RpcClientTransaction tx = conn.createTransaction();
		tx.setMethodCache(cache);
		RpcRequestHeader header = tx.getRequest().getHeader();
		header.setToService(service);
		header.setToMethod(method);
		header.setFromComputer(ServiceEnviornment.getComputerName());
		header.setFromService(ServiceEnviornment.getServiceName());
		return tx;
	}
}
