/*
 * FAE, Feinno App Engine
 *  
 * Create by 李会军 2011-2-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.servermock;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.client.RpcProxyFactory;

/**
 * TestMockClient
 * 
 * @author 李会军
 */
public class TestMockClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestMockClient.class);
	public static void main(String[] args) throws Exception
	{
		RpcTcpEndpoint tcpEndpoint = new RpcTcpEndpoint(new InetSocketAddress("127.0.0.1", 8001));
		IRpcTestServiceMock service = RpcProxyFactory.getService(tcpEndpoint, IRpcTestServiceMock.class);
		
		AddInputArgs add = new AddInputArgs().setX(100).setY(200);
		service.testAdd(add);

		DelayInputArgs delay = new DelayInputArgs().setDelay(10);
		service.testDelay(delay);
		
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			data.add(Integer.toString(i));
		}
		
		LargeDataInputArgs large = new LargeDataInputArgs()
			.setCount(100)
			.setData(data);
		
		
		service.testLargeData(large);
	}
//	
//	public static void invokeTestProxy(RpcProxy proxy) {
//		List<String> data = new ArrayList<String>();
//		for (int i = 0; i < 100; i++) {
//			data.add(Integer.toString(i));
//		}
//		
//		ProxyInputArgs input = new ProxyInputArgs()
//			.setCount(100)
//			.setData(data)
////			.setUri("tcp://10.10.70.76:8000");
////		.setUri("tcp://127.0.0.1:8001");
//		.setUri("tcp://10.10.40.184:7777");
//		
//		
//		proxy.invoke("TestProxy", input, 
//				new Action<RpcResults>(){
//					@Override
//					public void run(RpcResults a){
//						if(a.error() != null){
//							logger.debug("TestProxy callback with error.", a.error());
//						}
//						else {
//							logger.debug("TestProxy got callback");
////								LargeDataOutputArgs output = (LargeDataOutputArgs)a.getValue(LargeDataOutputArgs.class);
////								System.out.println("data size");
////								System.out.println(output.getData().size());
////								System.out.println("data size2");
//						}
//					}
//				}
//		);
//	}
}
