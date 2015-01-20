//package test.com.feinno.ha.component;
//
//import com.feinno.rpc.RpcException;
//import com.feinno.rpc.channel.RpcConnection;
//import com.feinno.rpc.channel.RpcConnectionMode;
//import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
//import com.feinno.rpc.channel.tcp.RpcTcpTransactionManager;
//import com.feinno.rpc.duplex.RpcDuplexServer;
//
//
//public class MockHost {
//	RpcTcpServerChannel serverChannel;
//	
//	public MockHost(int port) {
//	
//		RpcTcpTransactionManager.init(3, 1500);
//		serverChannel = new RpcTcpServerChannel(port, RpcConnectionMode.DUPLEX);
//		
//		RpcDuplexServer server = new RpcDuplexServer(serverChannel){
//
//			
//			@Override
//			public void connectionCreated(RpcConnection conn) {
//				System.err.println(">> conn is " + conn);
////				HAWorkerService service = conn.getProxy(HAWorkerService.class);
////				proxyMap.put(conn.getRemoteEndpoint(), service);
//			}
//
//			@Override
//			public void connectionDestoryed(RpcConnection conn) {
////				proxyMap.remove(conn);
//				
//			}
//			
//		};  
//		server.registerService(new MockHostServiceImpl());
//		
//	}
//	
//	void start() throws RpcException{
//		serverChannel.start();
//	}
//
//	public static void main(String[] args) throws RpcException{
////		// 注册TCP通道
////		RpcServiceBootstrap.registerChannel(new RpcTcpServerChannel(8890));
////		
////		// 注册服务
////		RpcServiceBootstrap.registerService(new MockHostServiceImpl());
////		
////		// 启动服务
////		RpcServiceBootstrap.start();
//		
//		MockHost host = new MockHost(8890);
//		host.start();
//		
//		
//	}
//}
