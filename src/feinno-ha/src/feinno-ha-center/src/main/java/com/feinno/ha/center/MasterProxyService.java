/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 24, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.center;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.rpc.channel.RpcClientTransaction;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcResponse;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.rpc.channel.RpcReturnCode;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcServiceBase;
import com.feinno.serialization.protobuf.types.ProtoString;
import com.feinno.util.EventHandler;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class MasterProxyService extends RpcServiceBase {

	private static final int RPC_TIME_OUT = 5 * 60 * 1000;

	public static final MasterProxyService INSTANCE = new MasterProxyService("HAMasterAgentCallbackService");

	public MasterProxyService(String name) {
		super(name, false);
	}

	@Override
	public void process(final RpcServerContext ctx) {
		try {
			ProtoString s = ctx.getExtension(1001, ProtoString.class); // serverName
			LOGGER.debug("request serverName" + s.getValue());
			MasterAgent agent = MasterAgentService.INSTANCE.getMasterAgent(s.getValue());
			if (agent == null) {
				ctx.end(RpcReturnCode.SESSION_NOT_FOUND, null);
				return;
			}
			RpcClientTransaction tx = agent.getConnection().createTransaction();
			tx.setTimeout(RPC_TIME_OUT);
			final int seq = ctx.getTx().getRequest().getHeader().getSequence();
			tx.copyContext(ctx.getTx());
			RpcFuture future = tx.begin();
			future.addListener(new EventHandler<RpcResults>() {
				public void run(Object sender, RpcResults e) {
					RpcResponse response = e.getResponse();
					ctx.getTx().getRequest().getHeader().setSequence(seq);
					ctx.endWithResponse(response);
				}
			});
		} catch (Exception e) {
			LOGGER.error("MasterProxyService.process invoke error.", e);
			ctx.endWithError(e);
		}

	}

	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
}
