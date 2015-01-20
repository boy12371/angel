//package test.com.feinno.ha.component;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.feinno.ha.HostConnectArgs;
//import com.feinno.ha.ServiceComponent;
//import com.feinno.ha.ServiceSettings;
//import com.feinno.rpc.RpcServerContext;
//import com.feinno.rpc.RpcServiceBootstrap;
//import com.feinno.rpc.RpcServiceHandler;
//import com.feinno.rpc.RpcServiceSlot;
//import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
//import com.feinno.rpc.duplex.RpcDuplexClient;
//
//public class DummyComponent implements ServiceComponent{
//	
//	private static final Logger logger = LoggerFactory.getLogger(DummyComponent.class);
//
//	@Override
//	public void start(RpcDuplexClient _unused) throws Exception {
//		
//		int port = ServiceSettings.getServicePort("rpc_tcp");
//		logger.info(">>>>>>>>>>>>satrting up DummyComponent");
//		logger.info(">>>>>>>>>>>>rpc_tcp port is {}", ServiceSettings.getServicePort("rpc_tcp"));
//		logger.info(">>>>>>>>>>>>domain is {}", ServiceSettings.getDomain());
//		logger.info(">>>>>>>>>>>>site port is {}", ServiceSettings.getSite());
//		logger.info(">>>>>>>>>>>>runMode port is {}", ServiceSettings.getRunMode());
//		RpcServiceSlot slot = new RpcServiceSlot("calculate");
//		
//		slot.addMethod("add",
//			new RpcServiceHandler<HostConnectArgs, HostConnectArgs>() {
//				public void run(RpcServerContext ctx) {
//					
//					HostConnectArgs input = ctx.getArgs(HostConnectArgs.class);
//					ctx.end(new HostConnectArgs().setWorkerName("xixi"));
//				}
//			}
//		);		
//		
//		RpcServiceBootstrap.registerChannel(new RpcTcpServerChannel(port));
//		RpcServiceBootstrap.registerService(slot);
//		RpcServiceBootstrap.start();
//		logger.info("DummyComponent started");
//	}
//
//	@Override
//	public void stop() throws Exception {
//		logger.info("stoping DummyComponent");		
//	}
//
//}
