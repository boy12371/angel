package com.feinno.appengine.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.AppTx;
import com.feinno.appengine.ContextUri;
import com.feinno.appengine.rpc.RemoteAppTx;
import com.feinno.appengine.rpc.RemoteAppTxImpl;
import com.feinno.rpc.channel.RpcBody;
import com.feinno.rpc.channel.RpcClientTransaction;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcRequest;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.rpc.client.RpcMethodStub;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.serialization.protobuf.types.ProtoString;

/**
 * 用于RemoteApp的Debug代理类
 * 
 * @author lvmingwei
 * 
 */
public class RemoteDebugProxy extends DebugProxy<RpcResults> {

	/** 远程方法的桩 */
	private RpcMethodStub method;

	/** 默认为0 */
	private int contextDemands = 0;

	/** 应用版本号 */
	private String parentVersion = "";

	/** 日志引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteDebugProxy.class);

	/**
	 * 构造方法
	 * 
	 * @param categoryMinusName
	 * @param grayFactors
	 * @param client
	 */
	public RemoteDebugProxy(String categoryMinusName, String grayFactors, RpcServerContext ctx) {
		super(categoryMinusName, grayFactors, ctx);
		method = this.getMethodStub("FAE", categoryMinusName);
	}

	@Override
	public RpcFuture invoke(RpcServerContext rpcServerContext, AppTx tx) throws Exception {
		LOGGER.info("Start RemoteDebugProxy");
		@SuppressWarnings("unchecked")
		RemoteAppTx<?, ?, AppContext> remoteAppTx = (RemoteAppTx<?, ?, AppContext>) tx;
		AppContext appContext = remoteAppTx.getContext();
		final RpcClientTransaction transaction = method.createTransaction();
		// 设置消息体
		RpcRequest request = transaction.getRequest();
		request.setBody(new RpcBody(rpcServerContext.getRawBody()));
		request.getHeader().setFromService(rpcServerContext.getFromService());

		// 设置发起方的Bean版本
		RpcBody rbVersion = new RpcBody(new ProtoString(parentVersion), false);
		request.putExtension(RemoteAppTxImpl.EXTENSION_APPBEAN_FROM, rbVersion);

		if (appContext != null) {
			ContextUri uri = appContext.getContextUri();
			if (uri != null) {
				ProtoString pb = new ProtoString(uri.toString());
				RpcBody rbUri = new RpcBody(pb, false);
				request.putExtension(RemoteAppTxImpl.EXTENSION_CONTEXT_URI, rbUri);
			}

			byte[] data = appContext.encode(contextDemands);
			if (data != null) {
				RpcBody rbData = new RpcBody(data, false);
				request.putExtension(RemoteAppTxImpl.EXTENSION_CONTEXT_DATA, rbData);
			}
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("RemoteAppBeanProxy args: {} ctx: {}" + request, categoryMinusName, appContext);
		}

		RpcFuture future = transaction.begin();
		return future;
	}
}
