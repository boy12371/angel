//package test.com.feinno.ha;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.Properties;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import test.com.feinno.configuration.TestHaConfigurator;
//import test.com.feinno.ha.agent.EmulatorService;
//import test.com.feinno.ha.agent.RpcServerEmulator;
//import test.com.feinno.ha.agent.ServiceProcess;
//
//import com.feinno.configuration.ConfigType;
//import com.feinno.configuration.spi.LocalConfigurationLoader;
//import com.feinno.ha.Genesis;
//import com.feinno.ha.interfaces.controller.HAWorkerControlleeService.StartArgs;
//import com.feinno.ha.interfaces.controller.HAWorkerControlleeService.UpdateConfigArgs;
//import com.feinno.ha.HostConnectArgs;
//import com.feinno.rpc.RpcNull;
//import com.feinno.rpc.RpcProxy;
//import com.feinno.rpc.RpcResults;
//import com.feinno.rpc.RpcServerContext;
//import com.feinno.rpc.channel.RpcConnection;
//import com.feinno.util.Action;
//
//public class TestGenesis {
//
//	@Before
//	public void registerService() {
//		EmulatorService hostWorker = new EmulatorService("HAHostWorker");
//		hostWorker.registerMethod("Connect", new ServiceProcess() {
//
//			@Override
//			public void onProcess(RpcServerContext ctx) throws Exception {
//				conn = ctx.getConnection();
//				if (!flag) {
//					HostConnectArgs args = ctx.getArgs(HostConnectArgs.class);
//					LOGGER.info("workername:{}", args.getWorkerName());
//					ctx.end();
//				} else {
//					ctx.end(new Exception("This is a faulty master service"));
//				}
//			}
//		});
//
//		RpcServerEmulator server = RpcServerEmulator.startup(8890);
//		server.registerService(hostWorker);
//
//		TestHaConfigurator tha = new TestHaConfigurator();
//		tha.registerService();
//
//	}
//
//	@Test
//	public void testStartup() throws InterruptedException {
//		testArguments();
//		try {
//			testInitHa();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		testNomMode();
//		testLocalMode();
//		try {
//			faultyHttpPort();
//			nomHttpPort();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		Thread.sleep(1000);
//		RpcProxy proxy = conn.getProxy("HAWorkerService");
//		Action<RpcResults> action = new Action<RpcResults>() {
//
//			@Override
//			public void run(RpcResults a) {
//				Assert.assertTrue(a.error() == null);
//			}
//
//		};
//		proxy.invoke("Ping", RpcNull.NULL, action);
//		Properties proper = new Properties();
//		try {
//			proper.load(new FileInputStream(
//					LocalConfigurationLoader.CONFIG_ROOT
//							+ "settings.properties"));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		StartArgs sa = new StartArgs();
//		sa.setProperties(proper.toString());
//		proxy.invoke("Start", sa, action);
//		UpdateConfigArgs ca = new UpdateConfigArgs();
//		ca.setPath("CFG_Site");
//		ca.setConfigType(ConfigType.TABLE);
//		proxy.invoke("UpdateConfig", ca, action);
//		proxy.invoke("Stop", RpcNull.NULL, action);
//		Thread.sleep(5000);
//		// testNoMaster();
//		// Genesis.INSTANCE.stopComponent();
//		// LOGGER.info("-------------------------done--------------------------");
//	}
//
//	private void testLocalMode() {
//		// String[] failed = new String[] { "-local:8890s" };
//		// Genesis.main(failed);
//		String[] failed2 = new String[] { "-local:settings.properties",
//				"-ha:22" };
//		Genesis.main(failed2);
//		String[] nom = new String[] { "-local:settings.properties" };
//		Genesis.main(nom);
//	}
//
//	private void testInitHa() throws Exception {
//		Properties prope = new Properties();
//		prope.load(new FileInputStream(LocalConfigurationLoader.CONFIG_ROOT
//				+ "settings.properties"));
//		Genesis.INSTANCE.startup(prope, false);
//	}
//
//	private void faultyHttpPort() throws Exception {
//		Properties prope = new Properties();
//		prope.load(new FileInputStream(LocalConfigurationLoader.CONFIG_ROOT
//				+ "settings.properties"));
//		prope.setProperty("servicePorts{http}", "kk");
//		Genesis.INSTANCE.startup(prope, true);
//	}
//
//	private void nomHttpPort() throws Exception {
//		Properties prope = new Properties();
//		prope.load(new FileInputStream(LocalConfigurationLoader.CONFIG_ROOT
//				+ "settings.properties"));
//		prope.setProperty("servicePorts{http}", "8080");
//		Genesis.INSTANCE.startup(prope, true);
//	}
//
//	private void testArguments() {
//		String[] min = new String[0];
//		Genesis.main(min);
//		String[] max = new String[3];
//		Genesis.main(max);
//		LOGGER.info("--------------------------------------------");
//		String[] failed = new String[] { "-ha:8890s", "-workername:testworker" };
//		try {
//			Genesis.main(failed);
//		} catch (Exception e) {
//			Assert.assertTrue(
//					"caught an excetpion NumberFormatException",
//					e.getClass()
//							.getSimpleName()
//							.equals(NumberFormatException.class.getSimpleName()));
//		}
//
//	}
//
//	private void testNoMaster() {
//		String[] nom = new String[] { "-ha:8890", "-workername:testworker" };
//		flag = true;
//		Genesis.main(nom);
//	}
//
//	private void testNomMode() throws InterruptedException {
//		String[] nom1 = new String[] { "-ha:8890" };
//		Genesis.main(nom1);
//		String[] nom = new String[] { "-ha:8890", "-workername:testworker" };
//		Genesis.main(nom);
//	}
//
//	private RpcConnection conn;
//	private static Logger LOGGER = LoggerFactory.getLogger(TestGenesis.class);
//	private boolean flag = false;
//}
