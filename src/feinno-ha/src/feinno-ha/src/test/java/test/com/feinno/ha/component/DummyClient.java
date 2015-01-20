//package test.com.feinno.ha.component;
//
//import java.net.InetSocketAddress;
//
//import com.feinno.ha.HostConnectArgs;
//import com.feinno.rpc.RpcProxy;
//import com.feinno.rpc.RpcProxyFactory;
//import com.feinno.rpc.RpcResults;
//import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
//import com.feinno.util.Action;
//
//public class DummyClient {
//
//	public static void main(String[] args) {
//		String address = "127.0.0.1";
//		if (args.length > 0) {
//			address = args[0];
//		}
//		
//		System.out.println("using server address : " + address);
//		
//		RpcTcpEndpoint tcpEndpoint = new RpcTcpEndpoint(new InetSocketAddress(address, 9999));
//		
//		RpcProxy proxy = RpcProxyFactory.getProxy(tcpEndpoint, "calculate");
//		
//		HostConnectArgs arg = new HostConnectArgs();
//		arg.setWorkerName("hehe");
//		
//		for(;;) {
//			proxy.invoke("add", arg, new Action<RpcResults>() {
//				@Override
//				public void run(RpcResults a) {
//					if (a.error() != null) {
//						System.out.println(a.error());
//					} else {
//						HostConnectArgs r = (HostConnectArgs)a.getValue(HostConnectArgs.class);
//						System.out.println(r.getWorkerName());
//					}
//				}
//			});
//			try {
//				Thread.sleep(1 * 1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//}
