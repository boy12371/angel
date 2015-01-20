package com.feinno.appengine.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.AppTx;
import com.feinno.appengine.AppTxWithContext;
import com.feinno.rpc.channel.RpcBody;
import com.feinno.rpc.channel.RpcClientTransaction;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcRequest;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.rpc.client.RpcMethodStub;
import com.feinno.rpc.server.RpcServerContext;

/**
 * 用于SipcAppBean的Debug代理类
 * 
 * @author lvmingwei
 * 
 */
public class SipcDebugProxy extends DebugProxy<RpcResults> {

	/** 远程方法的桩 */
	private RpcMethodStub method;

	/** 日志引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SipcDebugProxy.class);

	/**
	 * 构造方法
	 * 
	 * @param categoryMinusName
	 * @param grayFactors
	 * @param client
	 */
	public SipcDebugProxy(String categoryMinusName, String grayFactors, RpcServerContext ctx) {
		super(categoryMinusName, grayFactors, ctx);
		method = this.getMethodStub("ISipcOverRpcInterface", "requestReceived");
	}

	@Override
	public RpcFuture invoke(RpcServerContext rpcServerContext, AppTx tx) throws Exception {
		LOGGER.info("Start SipcDebugProxy");
		final RpcClientTransaction transaction = method.createTransaction();
		// 设置消息体
		RpcRequest request = transaction.getRequest();
		request.setBody(new RpcBody(rpcServerContext.getRawBody()));
		request.getHeader().setFromService(rpcServerContext.getFromService());

		if (LOGGER.isInfoEnabled()) {
			@SuppressWarnings("unchecked")
			AppTxWithContext<AppContext> sipAppTx = (AppTxWithContext<AppContext>) tx;
			LOGGER.info("RemoteAppBeanProxy args: {} ctx: {}" + request, categoryMinusName, sipAppTx.getContext());
		}

		RpcFuture future = transaction.begin();
		return future;
	}
}
