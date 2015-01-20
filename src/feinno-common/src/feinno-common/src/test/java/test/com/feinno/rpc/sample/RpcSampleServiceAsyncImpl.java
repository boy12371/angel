/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-6-4
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.sample;

import test.com.feinno.rpc.sample.RpcSampleService.HelloArgs;
import test.com.feinno.rpc.sample.RpcSampleService.HelloResult;

import com.feinno.diagnostic.perfmon.monitor.MonitorNHttpServer;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcServiceBase;
import com.feinno.rpc.server.RpcServiceBootstrap;
import com.feinno.threading.ExecutorFactory;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcSampleServiceAsyncImpl extends RpcServiceBase
{
	protected RpcSampleServiceAsyncImpl()
	{
		super("RpcSampleService", true);
	}
	
	@RpcMethod("Hello")
	public void hello(RpcServerContext ctx)
	{
		HelloArgs a = ctx.getArgs(HelloArgs.class);
		System.out.println(a.getStr());
		HelloResult r = new HelloResult();
		r.setStr(a.getStr().substring(a.getBegin(), a.getLen() + a.getBegin()));
		ctx.end(r);
	}
		
	public static void main(String[] args) throws Exception
	{
		new MonitorNHttpServer(8088);
		
		RpcServiceBootstrap.registerChannel(new RpcTcpServerChannel(7008));
		RpcServiceBootstrap.registerService(new RpcSampleServiceAsyncImpl());
		RpcServiceBootstrap.setExecutor(ExecutorFactory.newFixedExecutor("Test", 10, 10 * 1024));
		RpcServiceBootstrap.start();
		
		while (true) {
			Thread.sleep(1);
		}
	}
}


