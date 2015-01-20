/*
 * FAE, Feinno App Engine
 * 
 * Create by gaolei 2012-2-15
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.sample;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.impl.SimpleLoggerFactory;

import com.feinno.rpc.RpcEndpointFactory;
import com.feinno.rpc.channel.http.RpcHttpServerChannel;
import com.feinno.rpc.client.RpcProxyFactory;
import com.feinno.rpc.server.RpcServiceBootstrap;
import com.feinno.threading.ExecutorFactory;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcSampleServiceImpl implements RpcSampleService
{
	@Override
	public HelloResult hello(HelloArgs args)
	{
		String s = args.getStr().substring(args.getBegin(), args.getBegin() + args.getLen());
		HelloResult r = new HelloResult();
		r.setStr(s);
		return r;
	}

	@Override
	public RpcSampleResults add(RpcSampleArgs args)
	{
		RpcSampleResults result = new RpcSampleResults();
		result.setMessage(args.getMessage() + "_http");
		result.setR(args.getX() + args.getY());
		return result;
	}
	public static Executor newScalableExecutor = ExecutorFactory.newScalableExecutor("rpc_http2");

	public static void main(String[] args) throws Exception
	{
		//System.out.println(String.format("1213%s,ssic is %s", "test","gfasf"));
		// RpcServiceBootstrap.registerChannel(new RpcTcpServerChannel(8889));
		SimpleLoggerFactory.INSTANCE.setInfoEnable(false);
		RpcServiceBootstrap.registerChannel(new RpcHttpServerChannel(8443));
		RpcServiceBootstrap.registerService(new RpcSampleServiceImpl());
		RpcServiceBootstrap.setExecutor(ExecutorFactory.newFixedExecutor("Test", 10, 10 * 1024));
		RpcServiceBootstrap.start();
		boolean flag = false;
		while (flag)
		{

			final CountDownLatch cdl = new CountDownLatch(10000);
			Long start = System.currentTimeMillis();
			final AtomicInteger success = new AtomicInteger(0);
			for (int i = 0; i < 1; i++)
			{
				newScalableExecutor.execute(new Runnable()
				{
					@Override
					public void run()
					{
						for (int i = 0; i < 100; i++)
						{
							newScalableExecutor.execute(new Runnable()
							{

								@Override
								public void run()
								{
									for (int i = 0; i < 100; i++)
									{
										RpcSampleService service = RpcProxyFactory.getService(RpcEndpointFactory.parse("https://127.0.0.1:8443"), RpcSampleService.class);

										RpcSampleArgs arg = new RpcSampleArgs();
										arg.setMessage("test");
										arg.setX(1);
										arg.setY(2);
										RpcSampleResults result = service.add(arg);
										if (result.getR() == 3)
										{
											success.getAndAdd(1);
										}
										else
										{
											System.err.println("test");

										}
										cdl.countDown();
									}

								}
							});

						}

					}
				});

			}
			cdl.await();
			System.out.println("finish: "+(System.currentTimeMillis()-start)); 
		}
	}
}
