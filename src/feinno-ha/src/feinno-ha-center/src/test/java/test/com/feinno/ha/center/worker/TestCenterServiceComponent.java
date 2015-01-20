package test.com.feinno.ha.center.worker;

import com.feinno.ha.ServiceComponent;
import com.feinno.ha.center.MasterAgentService;
import com.feinno.ha.center.MasterProxyService;
import com.feinno.ha.center.WorkerAgentService;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.duplex.RpcDuplexServer;
import com.feinno.threading.ExecutorFactory;

public class TestCenterServiceComponent implements ServiceComponent {
	private RpcDuplexServer server;
	int port = 8111;

	public TestCenterServiceComponent(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		// Duplex Rpc服务
		// int port = ServiceSettings.INSTANCE.getServicePort("rpc");

		RpcTcpServerChannel channel = new RpcTcpServerChannel(port);
		server = new RpcDuplexServer(channel);
		// WorkerAgent管理
		server.registerService(WorkerAgentService.INSTANCE);
		// MasterAgent管理
		server.registerService(MasterAgentService.INSTANCE);
		// 透传
		server.registerService(MasterProxyService.INSTANCE);

		server.setExecutor(ExecutorFactory.newFixedExecutor("SERVER", 10, 10240));
		channel.start();

		// int port2 = ServiceSettings.INSTANCE.getServicePort("rpc");
		// RpcTcpServerChannel channel2 = new RpcTcpServerChannel(port2);
		// RpcServiceBootstrap.registerChannel(channel2);
		// RpcServiceBootstrap.registerService(new ConfigurationService());
		// // RpcServiceBootstrap.registerService(new CenterConsoleService());
		//
		// RpcServiceBootstrap.setExecutor(ExecutorFactory.getExecutor("SERVER"));
		// RpcServiceBootstrap.start();
	}

	public void stop() throws Exception {
		System.exit(1);
	}
}
