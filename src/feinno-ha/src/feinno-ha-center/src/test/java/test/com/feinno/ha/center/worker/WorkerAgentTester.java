package test.com.feinno.ha.center.worker;

import java.util.Random;

import org.junit.Test;

import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.spi.HAConfigurator;
import com.feinno.configuration.spi.LocalConfigurator;
import com.feinno.ha.center.WorkerAgentService;
import com.feinno.ha.interfaces.worker.HAWorkerAgentService;
import com.feinno.ha.interfaces.worker.HAWorkerMonitorConfig;
import com.feinno.ha.interfaces.worker.HAWorkerRegisterArgs;
import com.feinno.rpc.RpcEndpointFactory;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.rpc.duplex.RpcDuplexServer;
import com.feinno.threading.ExecutorFactory;
import com.feinno.util.ServiceEnviornment;

public class WorkerAgentTester {
	public static HAWorkerAgentService service = null;
	static {
		try {
			RpcTcpServerChannel channel = new RpcTcpServerChannel(8123);
			RpcDuplexServer server = new RpcDuplexServer(channel);
			// WorkerAgent管理
			server.registerService(WorkerAgentService.INSTANCE);
			channel.start();
			server.setExecutor(ExecutorFactory.newFixedExecutor("SERVER", 10, 1024));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initLocalConfigurator() {
		ConfigurationManager.setConfigurator(new LocalConfigurator());
	}

	public void initHAConfigurator() {
		if (service == null) {
			RpcEndpoint ep = RpcEndpointFactory.parse("tcp://127.0.0.1:8123");
			RpcDuplexClient client = new RpcDuplexClient(ep);
			client.setExecutor(ExecutorFactory.newFixedExecutor("CLIENT", 10, 1024));
			try {
				client.connectSync();
				ConfigurationManager.setConfigurator(new HAConfigurator(client));
				service = client.getService(HAWorkerAgentService.class);
				HAWorkerRegisterArgs args = new HAWorkerRegisterArgs();
				args.setServerName(ServiceEnviornment.getComputerName());
				args.setServiceName(ServiceEnviornment.getServiceName());
				args.setServicePorts("");
				args.setWorkerPid(new Random().nextInt());
				service.connect(args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testMonitorConfig() throws Exception {
		initHAConfigurator();
		HAWorkerMonitorConfig monitorConfig = service.getMonitorConfig();
		System.out.println(monitorConfig);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new WorkerAgentTester().testMonitorConfig();
	}

}
