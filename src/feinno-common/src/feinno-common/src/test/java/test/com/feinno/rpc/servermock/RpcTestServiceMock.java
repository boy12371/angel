/*
 * FAE, Feinno App Engine
 *  
 * Create by 李会军 2011-1-20
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.servermock;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.rpc.channel.RpcReturnCode;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.client.RpcMethodStub;
import com.feinno.rpc.client.RpcProxyFactory;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcServiceBase;
import com.feinno.util.EventHandler;

/**
 * RpcTestServiceMock
 * 
 * @author 李会军
 */
public class RpcTestServiceMock implements IRpcTestServiceMock
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcTestServiceMock.class);

	@Override
	public AddOutputArgs testAdd(AddInputArgs inputArgs)
	{
		AddOutputArgs output = new AddOutputArgs().setZ(inputArgs.getX() + inputArgs.getY());
		return output;
	}

	@Override
	public DelayOutputArgs testDelay(DelayInputArgs inputArgs)
	{
		try {
			Thread.sleep(inputArgs.getDelay());
		} catch (InterruptedException e) {
			return null;
		}

		DelayOutputArgs output = new DelayOutputArgs().setDelay(inputArgs.getDelay());
		return output;
	}

	@Override
	public LargeDataOutputArgs testLargeData(LargeDataInputArgs inputArgs)
	{
		LOGGER.debug("calling TestLargeData");
		List<String> ret = new ArrayList<String>();

		for (int i = 0; i < inputArgs.getCount(); i++) {
			int mod = inputArgs.getData().size();
			if (mod != 0) {
				int j = i % mod;
				ret.add(inputArgs.getData().get(j));
			} else {
				ret.add("NULL");
			}
		}

		LargeDataOutputArgs output = new LargeDataOutputArgs().setData(ret);
		LOGGER.debug("TestLargeData returning");
		return output;
	}

	@Override
	public ProxyOutputArgs testProxy(ProxyInputArgs inputArgs)
	{
		throw new UnsupportedOperationException("instead by testProxy(RpcServerContext)");
	}
	
	@RpcMethod
	public void testProxy(final RpcServerContext ctx)
	{
		LOGGER.debug("calling TestProxy");

		ProxyInputArgs inputArgs = ctx.getArgs(ProxyInputArgs.class);
		RpcTcpEndpoint tcpEndpoint = RpcTcpEndpoint.parse(inputArgs.getUri());
		
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(tcpEndpoint, "RpcTestServiceMock", "TestLargeData");
		
		RpcFuture future = stub.invoke(new LargeDataInputArgs().setCount(inputArgs.getCount()).setData(inputArgs.getData()));
		future.addListener(new EventHandler<RpcResults>() {

			@Override
			public void run(Object sender, RpcResults e)
			{
				if (e.getError() != null) {
					ctx.end(RpcReturnCode.SERVER_TRANSFER_ERROR, e.getError());
				} else {
					LOGGER.debug("TestProxy, ending context");
					ctx.end(new ProxyOutputArgs().setData(((LargeDataOutputArgs) e.getValue(LargeDataOutputArgs.class)).getData()));
				}
			}
		});
		LOGGER.debug("returning TestProxy");
	}

	@Override
	public ProxyOutputArgs testProxyDelay(ProxyDelayInputArgs inputArgs)
	{
		throw new UnsupportedOperationException("没实现呢");
	}
	
	@RpcMethod
	public void testProxyDelay(final RpcServerContext ctx)
	{
		ProxyDelayInputArgs inputArgs = ctx.getArgs(ProxyDelayInputArgs.class);
		RpcTcpEndpoint tcpEndpoint = RpcTcpEndpoint.parse(inputArgs.getUri());
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(tcpEndpoint, "RpcTestServiceMock", "TestDelay");

		RpcFuture future = stub.invoke(new DelayInputArgs().setDelay(inputArgs.getDelay()));
		future.addListener(new EventHandler<RpcResults>() {
			@Override
			public void run(Object sender, RpcResults e)
			{
				if (e.getError() != null) {
					ctx.end(RpcReturnCode.SERVER_TRANSFER_ERROR, e.getError());
				} else {
					ctx.end();
				}
			}
		});
	}
}
