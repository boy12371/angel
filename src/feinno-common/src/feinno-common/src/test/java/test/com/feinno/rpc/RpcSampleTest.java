/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-15
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc;


import java.util.Random;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import test.com.feinno.rpc.sample.SampleService;
import test.com.feinno.rpc.sample.SampleServiceImpl;

import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.RpcServerChannel;
import com.feinno.rpc.channel.inproc.RpcInprocEndpoint;
import com.feinno.rpc.channel.inproc.RpcInprocServerChannel;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.channel.uds.RpcUdsEndpoint;
import com.feinno.rpc.channel.uds.RpcUdsServerChannel;
import com.feinno.rpc.client.RpcMethodStub;
import com.feinno.rpc.client.RpcProxyFactory;
import com.feinno.rpc.server.RpcServiceBootstrap;
import com.feinno.serialization.protobuf.types.ProtoInteger;
import com.feinno.serialization.protobuf.types.ProtoString;
import com.feinno.threading.ExecutorFactory;

/**
 * Rpc基本功能单元测试
 * 
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcSampleTest
{
	private static final String udsPath = "/tmp/test_uds_channel_" + System.currentTimeMillis();
	
	private boolean isLinux;
	private int port = 9215;
	private RpcEndpoint testEp;
	private RpcEndpoint testTcpEp;
	private RpcEndpoint testUdsEp;
	private RpcEndpoint testInprocEp;
	private Random rand = new Random(System.currentTimeMillis());
	
	@Before
	public void setUp() throws Exception
	{	
		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			isLinux = true;
		} else {
			isLinux = false;
		}
						
		// RpcServiceBootstrap.setExecutor(ExecutorFactory.newFixedExecutor("test", 10, 5));
		RpcServerChannel channel = RpcServiceBootstrap.getServerChannel("tcp", Integer.toString(port));
		if (channel == null){
			channel = new RpcTcpServerChannel(port);	// 如何保证端口不重复是个问题
			RpcServiceBootstrap.registerChannel(channel);
			RpcServiceBootstrap.registerService(new SampleServiceImpl());
		}
		
		if (RpcServiceBootstrap.getServerChannel("inproc", "") == null) {
			RpcServiceBootstrap.registerChannel(RpcInprocServerChannel.INSTANCE);
		}
		
		if (isLinux) {
			RpcServerChannel channel2 = RpcServiceBootstrap.getServerChannel("uds", "test_uds_channel");
			if (channel2 == null){
				channel2 = new RpcUdsServerChannel(udsPath);	// 如何保证端口不重复是个问题
				RpcServiceBootstrap.registerChannel(channel2);
			}	
		}		
		
		if (!channel.isStarted()) {
			RpcServiceBootstrap.start();
		}
		

		testTcpEp = RpcTcpEndpoint.parse("tcp://127.0.0.1:" + port);
		testUdsEp = new RpcUdsEndpoint(udsPath);
		testInprocEp = RpcInprocEndpoint.INSTANCE;
		
		testEp = testTcpEp;
	}
		
	@Test
	public void testAddTransparent() throws Exception
	{
		SampleService.AddArgs args = generateAddArgs();
		ProtoInteger r = getService().add(args);
		String msg = String.format("%d + %d = %d", args.getA(), args.getB(), r.getValue());
		Assert.assertEquals(msg, args.getA() + args.getB(), r.getValue());
	}
	
	@Test
	public void testAddInvokeSync() throws Exception
	{
		SampleService.AddArgs args = generateAddArgs();
		ProtoInteger r = getMethodStub("add").invoke(args).syncGet(ProtoInteger.class);
		String msg = String.format("%d + %d = %d", args.getA(), args.getB(), r.getValue());
		Assert.assertEquals(msg, args.getA() + args.getB(), r.getValue());
	}
	
	@Test
	public void testEchoTransparent() throws Exception
	{
		SampleService service = getService();
		ProtoString default1 = new ProtoString();
		ProtoString foo = new ProtoString("foo");
		Assert.assertEquals("echo null > null", null, service.echo(null));
		Assert.assertTrue("echo default > default (not null)", service.echo(default1) != null);
		Assert.assertEquals("echo default > default (.value == null)", null, service.echo(default1).getValue());
		Assert.assertEquals("echo foo > foo", foo.getValue(), service.echo(foo).getValue());
	}	

	@Test
	public void testAllMethods() throws Exception
	{
		testAddTransparent();
		testAddInvokeSync();
		testEchoTransparent();		
	}
	
	@Test 
	public void testTcpChannel() throws Exception
	{
		testEp = testTcpEp;
		testAllMethods();
	}
	
	@Test
	public void testInprocChannel() throws Exception
	{
		testEp = testInprocEp;
		testAllMethods();
	}
	
	@Test
	public void testUdsChannel() throws Exception
	{
		if (!isLinux)
			return;
		
		testEp = testUdsEp;
		testAllMethods();
	}
	
	private SampleService.AddArgs generateAddArgs()
	{
		SampleService.AddArgs args = new SampleService.AddArgs();
		args.setA(rand.nextInt(1 << 30));
		args.setB(rand.nextInt(1 << 30));
		return args;
	}

	private SampleService getService()
	{
		SampleService service = RpcProxyFactory.getService(testEp, SampleService.class);
		return service;
	}
	
	private RpcMethodStub getMethodStub(String method)
	{
		return RpcProxyFactory.getMethodStub(testEp, "Sample", method);
	}
}
