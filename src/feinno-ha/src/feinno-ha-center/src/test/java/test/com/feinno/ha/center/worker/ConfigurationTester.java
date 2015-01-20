package test.com.feinno.ha.center.worker;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.feinno.configuration.ConfigParams;
import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigType;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.spi.HAConfigurator;
import com.feinno.configuration.spi.LocalConfigurator;
import com.feinno.ha.ServiceSettings;
import com.feinno.ha.center.WorkerAgentService;
import com.feinno.ha.interfaces.worker.HAWorkerAgentService;
import com.feinno.ha.interfaces.worker.HAWorkerRegisterArgs;
import com.feinno.rpc.RpcEndpointFactory;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.rpc.duplex.RpcDuplexServer;
import com.feinno.threading.ExecutorFactory;
import com.feinno.util.ServiceEnviornment;

/**
 * 
 * <b>描述: </b>配置信息相关的测试代码
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class ConfigurationTester {

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
			int port = ServiceSettings.INSTANCE.getServicePort("rpc_duplex");
			RpcEndpoint ep = RpcEndpointFactory.parse("tcp://127.0.0.1:" + port);
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
				service.register(args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testLocalConfigText() throws ConfigurationException {
		initLocalConfigurator();
		ConfigurationManager.loadText("logging.xml", null, new ConfigUpdateAction<String>() {
			public void run(String e) throws Exception {
				System.out.println("local config text : \r\n" + e);
			}

		});
	}

	public void testLocalConfigTable() throws ConfigurationException {
		initLocalConfigurator();
		ConfigTable<String, CFGSiteConfigTableItem> ct = ConfigurationManager.loadTable(String.class,
				CFGSiteConfigTableItem.class, "CFG_Site",
				new ConfigUpdateAction<ConfigTable<String, CFGSiteConfigTableItem>>() {
					public void run(ConfigTable<String, CFGSiteConfigTableItem> value) throws Exception {
						Assert.assertTrue(value != null && value.getCount() > 0);
					}

				});
		Assert.assertTrue(ct.getCount() > 0);
	}

	public void testHaConfigText() throws ConfigurationException {
		initHAConfigurator();
		ConfigurationManager.loadText("logging.properties", null, new ConfigUpdateAction<String>() {
			public void run(String e) throws Exception {
				System.out.println("ha config text : \r\n" + e);
			}
		});
		ConfigurationManager.subscribeConfigUpdate(ConfigType.TEXT, "logging.properties", null);
	}

	public void testHaConfigTable() throws ConfigurationException {
		initHAConfigurator();
		ConfigTable<String, CFGSiteConfigTableItem> ct = ConfigurationManager.loadTable(String.class,
				CFGSiteConfigTableItem.class, "CFG_Site",
				new ConfigUpdateAction<ConfigTable<String, CFGSiteConfigTableItem>>() {
					public void run(ConfigTable<String, CFGSiteConfigTableItem> value) throws Exception {
						System.out.println("ha config text [CFG_Site] count:" + value.getCount());
						Assert.assertTrue(value != null && value.getCount() > 0);
					}

				});
		Assert.assertTrue(ct.getCount() > 0);
		ConfigurationManager.subscribeConfigUpdate(ConfigType.TABLE, "CFG_Site", null);
	}

	public void testHaConfigParamsText() throws ConfigurationException {
		initHAConfigurator();
		ConfigParams params = new ConfigParams();
		params.put("service", "test");
		ConfigurationManager.loadText("serviceSettings.properties", params, new ConfigUpdateAction<String>() {
			public void run(String e) throws Exception {
				System.out.println("ha config text : \r\n" + e);
			}
		});
		ConfigurationManager.subscribeConfigUpdate(ConfigType.TEXT, "serviceSettings.properties", params);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		new ConfigurationTester().testLocalConfigText();
		new ConfigurationTester().testLocalConfigTable();
		new ConfigurationTester().testHaConfigText();
		new ConfigurationTester().testHaConfigTable();
		new ConfigurationTester().testHaConfigParamsText();
	}
}
