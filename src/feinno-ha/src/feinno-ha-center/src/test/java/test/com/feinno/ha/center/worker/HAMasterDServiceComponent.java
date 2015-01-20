package test.com.feinno.ha.center.worker;

import java.util.Scanner;

import com.feinno.ha.interfaces.controller.HAWorkerControlleeService;
import com.feinno.ha.interfaces.controller.HAWorkerControllerRegisterArgs;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.duplex.RpcDuplexClientAgent;
import com.feinno.rpc.duplex.RpcDuplexServer;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcServiceBase;
import com.feinno.threading.ExecutorFactory;

/**
 * 
 * <b>描述: </b>模拟HAMasterD通过控制台进行start、stop操作
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 */
public class HAMasterDServiceComponent {

	private RpcDuplexServer server;
	private int port;

	public HAMasterDServiceComponent(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		RpcTcpServerChannel channel = new RpcTcpServerChannel(port);
		server = new RpcDuplexServer(channel);
		server.registerService(WorkerControllerService.INSTANCE);
		server.setExecutor(ExecutorFactory.newFixedExecutor("SERVER", 10, 10240));
		channel.start();
	}

	public static class WorkerControllerService extends RpcServiceBase {

		private RpcDuplexClientAgent client;
		public static final WorkerControllerService INSTANCE = new WorkerControllerService();

		/**
		 * 私有的构造方法，满足单例模式
		 */
		private WorkerControllerService() {
			super("HAWorkerControllerService");
		}

		@RpcMethod("Register")
		public void register(RpcServerContext ctx) {
			HAWorkerControllerRegisterArgs args = ctx.getArgs(HAWorkerControllerRegisterArgs.class);
			System.out.println("HAWorkerControllerService Pid : " + args.getPid());
			client = new MasterDAgent(ctx);
			consoleMonitor();
			ctx.end();
//			try {
//				HAWorkerControlleeService controllee = client
//						.getService(HAWorkerControlleeService.class);
//				controllee.start();
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
		}

		public void consoleMonitor() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						System.out.print("Please input HAMaster Option");
						System.out.print(">");
						Scanner scanner = new Scanner(System.in);
						String line = scanner.next();
						if (line.equals("start")) {
							System.out.println("begin to start service");
							try {
								HAWorkerControlleeService controllee = client
										.getService(HAWorkerControlleeService.class);
								controllee.start();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else if (line.equals("stop")) {
							System.out.println("begin to stop service");
							try {
								HAWorkerControlleeService controllee = client
										.getService(HAWorkerControlleeService.class);
								controllee.stop();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else if (line.equals("ping")) {
							System.out.println("begin to ping service");
							try {
								HAWorkerControlleeService controllee = client
										.getService(HAWorkerControlleeService.class);
								controllee.stop();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}

				}
			}).start();
		}
	}

	public static class MasterDAgent extends RpcDuplexClientAgent {

		public MasterDAgent(RpcServerContext ctx) {
			super(ctx);
		}

	}

	public static void main(String args[]) throws Exception {
		new HAMasterDServiceComponent(8888).start();
	}
}
