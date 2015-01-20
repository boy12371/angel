/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-15
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.appengine.rpc;

import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import com.feinno.appengine.AppBeanWithContext;
import com.feinno.appengine.AppContext;
import com.feinno.appengine.AppContextData;
import com.feinno.appengine.AppTx;
import com.feinno.appengine.annotation.AppBeanBaseType;
import com.feinno.appengine.configuration.AppBeanAnnotationsLoader;
import com.feinno.appengine.testing.DebugProxyManager;
import com.feinno.rpc.channel.RpcReturnCode;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.util.Action;
import com.feinno.util.SyncInvoker;

/**
 * 支持强类型远程调用服务的AppBean
 * 
 * TODO 1. 支持跨IDC的操作 2. 支持原始接口的兼容，双向兼容 3. 支持级联的灰度发布
 * 
 * @author 高磊 gaolei@feinno.com
 */
@AppBeanBaseType
public abstract class RemoteAppBean<A, R, C extends AppContext> extends AppBeanWithContext<C>
{
	public static final int EXTENSION_CONTEXT_URI = 1;
	public static final int EXTENSION_CONTEXT_DATA = 2;
	
//	List<RpcServerContext>
	private Class<?> typeA;
	private Class<?> typeR;
	private Class<?> typeC;

	
	protected RemoteAppBean()
	{
		super(2);
//		Class<?> subClass = this.getClass();
//        while(subClass.getSuperclass().getAnnotation(AppBeanBaseType.class) == null) {
//            subClass = subClass.getSuperclass();
//        }
////        Class<?>[] aa = GenericsUtils.getSuperClassGenricTypes(this.getClass());
//		Class<?>[] a = GenericsUtils.getSuperClassGenricTypes(subClass);
		  Class<?>[] a = AppBeanAnnotationsLoader.getAppBeanActualTypes(this.getClass());
		typeA = a[0];
		typeR = a[1];
		typeC = a[2];
		
	}

	Class<?> getTypeA()
	{
		return typeA;
	}

	Class<?> getTypeR()
	{
		return typeR;
	}

	Class<?> getTypeC()
	{
		return typeC;
	}
	
	void processRequest(RpcServerContext ctx)
	{	
		RemoteAppTx<A, R, C> tx = null;
		try {
			LOGGER.info("RemoteAppBean.processRequest...");
			tx = new RemoteAppTxImpl<A, R, C>(this, ctx);
			loadContext(tx);
		} catch (Exception ex) {
			LOGGER.error("RemoteAppBean.processRequest,SERVER_ERROR ex:{}",ex);
			ctx.end(RpcReturnCode.SERVER_ERROR, ex);
		}

		if (tx != null) {
			// 如果有Debug，则走Debug流程，否则走正常流程
			if (DebugProxyManager.isCan(ctx.getToMethod(), tx.getContext())) {
				DebugProxyManager.isCanAndRun(ctx.getToMethod(), ctx, tx);
			}else {
				processHandlerChain(tx, null);
			}
		}
	}

	@Override
	public void processTx(AppTx tx, Action<Exception> callback)
	{
		@SuppressWarnings("unchecked")
		RemoteAppTx<A, R, C> rtx = (RemoteAppTx<A, R, C>) tx;
		rtx.setTerminateCallback(callback);
		try {
			LOGGER.info("entering app process with tx:{}", tx);
			process(rtx);
			LOGGER.info("exit app process with tx:{}", tx);
		} catch (Exception e) {
			LOGGER.error("app process failed {}", e);
			rtx.end(e);
		}
	}

	public R processTest(A args, AppContextData data)
	{
		return processTest(args, data);
	}

	public R processTest(A args, String contextUri, byte[] contextData) throws InterruptedException
	{
		LOGGER.info("begin test");
		RemoteAppTxMock<A, R, C> tx = new RemoteAppTxMock<A, R, C>(args, contextUri, contextData);
		loadContext(tx);

		SyncInvoker<AppTx> invoker = new SyncInvoker<AppTx>();
		processHandlerChain(tx, invoker.getCallback());
		invoker.waitFor(5000);
		return tx.results();
	}

	public abstract void process(RemoteAppTx<A, R, C> tx) throws Exception;
}
