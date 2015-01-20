//package test.com.feinno.ha.agent;
//
//import java.util.concurrent.Executors;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.feinno.ha.agent.Reconnector;
//import com.feinno.rpc.RpcServerContext;
//import com.feinno.rpc.channel.RpcConnection;
//import com.feinno.rpc.channel.RpcConnectionMode;
//import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
//import com.feinno.rpc.duplex.RpcDuplexClient;
//import com.feinno.serialization.types.ProtoString;
//
//public class TestReconnector {
//
//	@Before
//	public void setUp() {
//		EmulatorService testReconnector = new EmulatorService("TestReconnector");
//		testReconnector.registerMethod("TestRecall", new ServiceProcess() {
//
//			@Override
//			public void onProcess(RpcServerContext ctx) throws Exception {
//				ctx.end();
//
//			}
//		});
//
//		RpcServerEmulator rpcserver = RpcServerEmulator.startup(8486);
//		rpcserver.registerService(testReconnector);
//	}
//
//	@Test
//	public void testRecall() {
//		Reconnector.recall(tcpEndpoint, "TestReconnector", "TestRecall", null,
//				null, listener);
//
//		RpcConnection conn = tcpEndpoint
//				.getConnection(RpcConnectionMode.SIMPLEX);
//		Reconnector.recall(conn, "TestReconnector", "TestRecall", null, null);
//		conn = tcpEndpoint.getConnection(RpcConnectionMode.DUPLEX);
//		Reconnector.recall(conn, "TestReconnector", "TestRecall", null, null);
//		Reconnector.reconnect(tcpEndpoint, listener);
//		RpcDuplexClient client = Reconnector.reconnect(tcpEndpoint);
//		WAIT = false;
//		Reconnector.recall(client, "TestReconnector", "TestRecall", null, null,
//				listener);
//		// cover try..catch code in method reconnect
//		RpcServerEmulator.getEmulator(8486).shutdown();
//		Executors.newSingleThreadExecutor().submit(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(1500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				setUp();
//			}
//
//		});
//		client = Reconnector.reconnect(tcpEndpoint);
//		Assert.assertTrue(client != null);
//		// test recall
//		Executors.newSingleThreadExecutor().submit(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(1500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				EmulatorService service = new EmulatorService(
//						"TestReconnector2");
//				service.registerMethod("TestRecall2", new ServiceProcess() {
//
//					@Override
//					public void onProcess(RpcServerContext ctx)
//							throws Exception {
//						ProtoString ps = ctx.getArgs(ProtoString.class);
//						Assert.assertEquals("test", ps.getValue());
//						Assert.assertEquals(ctx.getContextUri(), "a@b");
//						ctx.end();
//
//					}
//				});
//				RpcServerEmulator.getEmulator(8486).registerService(service);
//			}
//
//		});
//		ProtoString ps = new ProtoString();
//		ps.setValue("test");
//		Reconnector
//				.recall(client, "TestReconnector2", "TestRecall2", ps, "a@b");
//
//		RpcServerEmulator.getEmulator(8486).shutdown();
//		Executors.newSingleThreadExecutor().submit(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(1500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				setUp();
//			}
//
//		});
//		Reconnector.recall(client, "TestReconnector", "TestRecall", null, null);
//	}
//
//	private Reconnector.ReconnectorListener listener = new Reconnector.ReconnectorListener() {
//
//		@Override
//		public void onSuccess() {
//			Assert.assertTrue(true);
//		}
//
//		@Override
//		public void onFailed(Exception e) {
//			LOGGER.warn("Testing method testGetProxy: {}", e.getMessage());
//
//		}
//
//		@Override
//		public boolean isWait() {
//			// TODO Auto-generated method stub
//			return WAIT;
//		}
//
//		@Override
//		public boolean isContinue() {
//			// TODO Auto-generated method stub
//			return CONTINUE;
//		}
//	};
//	private RpcTcpEndpoint tcpEndpoint = RpcTcpEndpoint
//			.parse("tcp://127.0.0.1:8486");
//	private boolean WAIT = true;
//	private boolean CONTINUE = true;
//	private static Logger LOGGER = LoggerFactory
//			.getLogger(TestReconnector.class);
//}
