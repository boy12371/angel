//package test.com.feinno.ha.component;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.feinno.ha.interfaces.controller.HAWorkerControlleeService;
//import com.feinno.ha.interfaces.controller.HAWorkerControlleeService.StartArgs;
//import com.feinno.ha.HostConnectArgs;
//import com.feinno.rpc.RpcClientContext;
//import com.feinno.rpc.RpcContext;
//import com.feinno.rpc.RpcNull;
//import com.feinno.rpc.RpcResults;
//import com.feinno.rpc.RpcReturnCode;
//import com.feinno.rpc.channel.RpcConnection;
//
//public class MockHostServiceImpl implements MockHostService {
//
//	private static final Logger logger = LoggerFactory.getLogger(MockHostServiceImpl.class);
//	public MockHostServiceImpl() {}
//
//	@Override
//	public void connect(HostConnectArgs data, RpcContext ctx) {
//		logger.info("got workerName > " + data.getWorkerName());
//		RpcConnection dConn = ctx.serverContext().getConnection();
//		HAWorkerControlleeService service = dConn.getProxy(HAWorkerControlleeService.class);
//		StartArgs args = new StartArgs();
//		args.setProperties("workerComponent=test.com.feinno.ha.component.DummyComponent");
//		service.start(args, new RpcClientContext(RpcNull.class) {
//			@Override
//			public void callback(RpcResults results) {
//				if (results.returnCode() == RpcReturnCode.OK) {
//					logger.info("got start callback");
//				} else {
//					logger.error("worker startup failed.");
//				}
//				
//			}
//		});
//	}
//
//}
