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
import com.feinno.rpc.channel.RpcConnectionReal;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.RpcRequestHeader;
import com.feinno.util.DateTime;
import com.feinno.util.Func;
import com.feinno.util.ServiceEnviornment;

/**
 * 
 * 用于直连的RpcClientTransactionHandler, 一般用于处理单点到单点的请求
 * 允许通过协商进一步提高性能
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcClientTransactionHandlerDirect implements RpcClientTransactionHandler
{
	private int maxLifeSeconds;
	private RpcEndpoint ep;
	private String service;
	private String method;
	private RpcConnectionReal connection;
	private Func<Void, RpcConnectionReal> getConnectionFunc;
	private RpcClientMethodCache cache;
	
	public RpcEndpoint getEndpoint()
	{
		return ep;
	}
	
	public RpcClientTransactionHandlerDirect(final RpcEndpoint ep, String service, String method, Func<Void, RpcConnectionReal> func)
	{
		this.ep = ep;
		this.service = service;
		this.method = method;
		this.maxLifeSeconds = Integer.MAX_VALUE;
		
		connection = (RpcConnectionReal)ep.getClientChannel().getConnection(ep);
		cache = RpcClientMethodManager.INSTANCE.getMethodCache(ep, service, method);
		
		if (func == null) {
			getConnectionFunc = new Func<Void, RpcConnectionReal>() {
				@Override
				public RpcConnectionReal exec(Void obj)
				{
					return (RpcConnectionReal) ep.getClientChannel().getConnection(ep);
				}
			};
		} else {
			getConnectionFunc = func;
		}
	}

	public void setMaxLife(int seconds)
	{
		maxLifeSeconds = seconds;
	}

	public RpcConnectionReal getConnection()
	{
		if (!connection.isUseable()) {
			synchronized (connection) {
				connection = getConnectionFunc.exec(null);
			}		
		}
//		if (DateTime.now().substract(connection.getConnectedTime()).getTotalSeconds() > maxLifeSeconds) {
//			connection.setToIdle();
//		}
		connection = getConnectionFunc.exec(null);
		return connection;
	}
	
	public RpcClientTransaction createTransaction()
	{
		RpcConnectionReal conn = getConnection();
		RpcClientTransaction tx = conn.createTransaction();
		
		RpcRequestHeader header = tx.getRequest().getHeader();
		tx.setMethodCache(cache);
		header.setToService(service);
		header.setToMethod(method);
		header.setFromService(ServiceEnviornment.getServiceName());
		header.setFromComputer(ServiceEnviornment.getComputerName());
		return tx;
	}
}

//private int toId;
//private int fromId;
//private boolean negociated;
//
//private RpcConnectionReal connection;
//private RpcClientMethodCache cache;
//
///**
// * 
// * simplex模式
// * @param ep
// * @param service
// * @param method
// */
//public RpcClientMethodSink(RpcEndpoint ep, String service, String method)
//{
//	cache = RpcClientMethodManager.INSTANCE.getMethodCache(ep, service, method);
//	connection = (RpcConnectionReal)ep.getClientChannel().createConnection(ep);
//	connection.autoConnect();
//	negociated = false;
//}
//
///**
// * 
// * duplex模式
// * @param connection
// * @param service
// * @param method
// */
//public RpcClientMethodSink(RpcConnectionReal connection, String service, String method)
//{
//	RpcEndpoint remoteEp = connection.getRemoteEndpoint();
//	cache = RpcClientMethodManager.INSTANCE.getMethodCache(remoteEp, service, method);
//	this.connection = connection;
//}
//
///**
// * 
// * 创建一个可用的RpcClientTransaction
// * @return
// */
//public RpcClientTransaction createTransaction()
//{
//	RpcClientTransaction tx = connection.createTransaction();
//	tx.setMethodCache(cache); 
//	
//	RpcRequestHeader header = tx.getRequest().getHeader();
//	
//	//
//	// 协商后可优化
//	if (toId > 0) {
//		header.setToId(toId);
//		header.setFromId(fromId);
//	} else {
//		header.setToService(cache.getKey().getService());
//		header.setToMethod(cache.getKey().getMethod());
//		header.setFromService(ServiceEnviornment.getServiceName());
//		header.setFromComputer(ServiceEnviornment.getComputerName());
//	}
//	
//	if (!negociated) {
//		tx.getTransactionEnded().addListener(new EventHandler<RpcResponse>() {
//			@Override
//			public void run(Object sender, RpcResponse args)
//			{
//				if (args.getHeader().getToId() > 0) {
//					toId = args.getHeader().getToId();
//					fromId = args.getHeader().getFromId();
//				}
//				negociated = true;
//			}				
//		});
//	}
//	return tx;
//}
//
//public RpcConnectionReal getConnection()
//{
//	return connection;
//}







