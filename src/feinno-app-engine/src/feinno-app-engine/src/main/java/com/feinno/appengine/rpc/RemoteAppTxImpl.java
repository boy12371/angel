/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.rpc;

import org.slf4j.MarkerFactory;

import com.feinno.appengine.AppContext;
import com.feinno.rpc.channel.RpcReturnCode;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.serialization.protobuf.types.ProtoString;

/**
 * RemoteAppBean的调用事务对象实体
 * TODO: 改为Rpc Extensions实现
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RemoteAppTxImpl<A, R, C extends AppContext> extends RemoteAppTx<A, R, C>
{
	public static final int EXTENSION_CONTEXT_URI = 1;
	public static final int EXTENSION_CONTEXT_DATA = 2;
	public static final int EXTENSION_APPBEAN_FROM = 3;
	public static final int EXTENSION_CONTEXT_TYPE = 4;
	public static final int EXTENSION_CONTEXT_DATA_BYFGR = 5;
	
	private RpcServerContext rpcCtx;
	private String fromBeanName;
	private String fromBeanVersion;
	
	@SuppressWarnings("unchecked")
	RemoteAppTxImpl(RemoteAppBean<A, R, C> bean, RpcServerContext ctx)
	{
		super();
		this.rpcCtx = ctx;
		
		try {
			fromBeanName = rpcCtx.getFromService();
			
			ProtoString pversion = rpcCtx.getExtension(EXTENSION_APPBEAN_FROM, ProtoString.class);
			if (pversion != null) {
				fromBeanVersion = pversion.getValue();
			} 
			
			ProtoString pstr = rpcCtx.getExtension(EXTENSION_CONTEXT_URI, ProtoString.class);
			if (pstr != null) {
				contextUri = pstr.getValue();
				if (contextUri != null) {
					setMarker(MarkerFactory.getMarker(contextUri));
				}
			}
			
			contextData = rpcCtx.getRawExtension(EXTENSION_CONTEXT_DATA);
			
			args = (A) ctx.getArgs(bean.getTypeA());
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}			
	}
	
	public String getFromBeanName()
	{
		return fromBeanName;
	}

	public String getFromBeanVersion()
	{
		return fromBeanVersion;
	}

	/**
	 * 
	 * 设置应答参数
	 */
	@Override
	public synchronized void end()
	{
		if(!rpcCtx.isDone())
			rpcCtx.end();
		terminate();
	}
	
	/**
	 * 
	 * 设置应答参数
	 * @param result
	 */
	@Override
	public synchronized void end(R result)
	{
		if(!rpcCtx.isDone())
			rpcCtx.end(result);
		terminate();
	}
	
	/**
	 * 
	 * 设置应答参数, 错误
	 * @param error
	 */
	@Override
	public synchronized void end(Exception error)
	{
		// set error 已经完成了， 不用 rpcctx.end了
//		rpcCtx.end(RpcReturnCode.SERVER_ERROR, error);
		setError(error);
		terminate();
	}
	
	/**
	 * 当有错误信息被set的时候，这个错误应该被返回到客户端
	 * 
	 * @param error
	 */
	@Override
	public synchronized void setError(Exception error) {
		super.setError(error);
		if (error != null && !rpcCtx.isDone()) {
			rpcCtx.end(RpcReturnCode.SERVER_ERROR, error);
		}
	}
}
