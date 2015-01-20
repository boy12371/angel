/*
 * FAE, Feinno App Engine
 *  
 * Create by 李会军 2011-2-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.duplex;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.rpc.duplex.RpcDuplexServer;
import com.feinno.serialization.protobuf.types.ProtoString;
import com.feinno.threading.ExecutorFactory;
import com.feinno.threading.Future;

/**
 * TestMockClient
 * 
 * @author 李会军
 */
public class RpcDuplexTest
{
	private static final int SERVER_PORT = 7037;
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcDuplexTest.class);
	
	@Before
	public void testBefore() throws Exception
	{
		// start service
		RpcTcpServerChannel channel = new RpcTcpServerChannel(SERVER_PORT);
		RpcDuplexServer server = new RpcDuplexServer(channel);
		server.setExecutor(ExecutorFactory.newFixedExecutor("Test", 10, 1024));
		server.registerService(RpcSampleAgentServiceImpl.INSTANCE);
		channel.start();	
	}

	@Test
	public void testRegisterAndCallback() throws Exception
	{
		RpcDuplexClient client = new RpcDuplexClient(RpcTcpEndpoint.parse("tcp://127.0.0.1:" + SERVER_PORT));
		client.setExecutor(ExecutorFactory.getExecutor("Test"));
		final Future<String> future = new Future<String>();
		client.registerCallbackService(new RpcSampleAgentCallbackService() {
			@Override
			public ProtoString test(ProtoString args)
			{
				future.complete(args.getValue());
				return args;
			}
		});
		RpcSampleAgentService service = client.getService(RpcSampleAgentService.class);
		
		LOGGER.info("connection sync");
		client.connectSync();
		LOGGER.info("connected");
		service.register(new ProtoString("CLIENT1"));
		service.testCallback();
		
		String result = future.getValue();
		Assert.assertEquals("CLIENT1", result);
	}	
}
