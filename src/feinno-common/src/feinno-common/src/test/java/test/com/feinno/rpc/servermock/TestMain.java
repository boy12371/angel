/*
 * FAE, Feinno App Engine
 *  
 * Create by 李会军 2011-1-20
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.servermock;

import java.net.InetSocketAddress;

import org.slf4j.impl.SimpleLoggerFactory;

import com.feinno.diagnostic.perfmon.Stopwatch;
import com.feinno.diagnostic.perfmon.monitor.MonitorNHttpServer;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.client.RpcProxyFactory;
import com.feinno.rpc.server.RpcServiceBootstrap;
import com.feinno.threading.ExecutorFactory;

/**
 * TestMain
 * 
 * @author 李会军
 */
public class TestMain
{
	public static final int N = 1000;
	public static void main(String[] args) throws Exception
	{
		// 注册服务端通道
		RpcServiceBootstrap.registerChannel(new RpcTcpServerChannel(8001));
		RpcServiceBootstrap.registerService(new RpcTestServiceMock());
		RpcServiceBootstrap.setExecutor(ExecutorFactory.newFixedExecutor("mock", 32, 32 * 1000));

		SimpleLoggerFactory.INSTANCE.setInfoEnable(false);
		System.out.println("Test server started. [V39]");
		RpcServiceBootstrap.start();
		
		RpcTcpEndpoint tcpEndpoint = new RpcTcpEndpoint(new InetSocketAddress("127.0.0.1", 8001));
		IRpcTestServiceMock service = RpcProxyFactory.getService(tcpEndpoint, IRpcTestServiceMock.class);
		
		new MonitorNHttpServer(8088);
		
//		for (int j = 0; j < 20; j++) {
//			startTestThread(j, service);			
//		}
//		Thread.sleep(180 * 6000);
		while (true) {
			Thread.sleep(1000);
		}
	}

	private static void startTestThread(final int j, final IRpcTestServiceMock service)
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				int n = 0;
				Stopwatch stop = new Stopwatch();
				while (true) {
					for (int i = 0; i < N; i++) {
						AddInputArgs add = new AddInputArgs().setX(j).setY(i);
						AddOutputArgs out = service.testAdd(add);
						if (out.getZ() != j + i) {
							System.err.print("error");
						}
					}
					n++;
					System.out.printf("[%d] %d tested cost %fms\n", j, n * N, stop.getMillseconds());
				}
			}
		}).start();
	}
}
