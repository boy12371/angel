//package test.com.feinno.ha.agent;
//
//import java.util.Hashtable;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.feinno.rpc.RpcServerContext;
//import com.feinno.rpc.RpcServiceBase;
//
//public class EmulatorService {
//
//	public EmulatorService(String serviceName) {
//		this.serviceName = serviceName;
//		service = new RpcServiceBase(serviceName) {
//
//			@Override
//			public void process(final RpcServerContext ctx) {
//				LOGGER.debug("++++++++++++++++"
//						+ EmulatorService.this.serviceName + ":call "
//						+ ctx.getToService() + "." + ctx.getToMethod()
//						+ " ++++++++++++++");
//				try {
//					ServiceProcess centerProcess = map.get(ctx.getToMethod());
//					centerProcess.onProcess(ctx);
//				} catch (Exception e) {
//					LOGGER.warn(
//							"When process EmulatorService,occurred an error : {} ",
//							e.getCause());
//				}
//				LOGGER.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//			}
//
//		};
//	}
//
//	public void registerMethod(String method, ServiceProcess process) {
//		map.put(method, process);
//	}
//
//	public RpcServiceBase getService() {
//		return service;
//	}
//
//	private RpcServiceBase service;
//	private Logger LOGGER = LoggerFactory.getLogger(EmulatorService.class);
//	private String serviceName;
//	private Hashtable<String, ServiceProcess> map = new Hashtable<String, ServiceProcess>();
//}
