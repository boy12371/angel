package test.com.feinno.ha.center.worker;

import com.feinno.ha.Genesis;
import com.feinno.ha.ServiceSettings;

public class GenesisTester {

	// @Test
	public void testLocalCenter() throws Exception {
		String[] args = new String[] { "-ports", "rpc_duplex=8111;rpc=8112" };
		Genesis.main(args);
	}

	// @Test
	public void testHA() throws Exception {
		int centerPort = 8111;
		testHACenter();
		String[] args = new String[] { "-agent", "tcp://127.0.0.1:" + centerPort, "-ports",
				"rpc=8080;http=8005;monitor=8007;rpc_duplex=8111" };
		Genesis.main(args);
	}

	public void testWorker() throws Exception {
		int centerPort = 8111;
		String[] args = new String[] { "-agent", "tcp://127.0.0.1:" + centerPort, "-ports",
				"rpc=8080;http=8005;monitor=8007;rpc_duplex=8111" };
		// String[] args = new String[] { "-agent", "tcp://192.168.110.174:" +
		// 8888, "-ports",
		// "rpc=8080;http=8005;monitor=8007;rpc_duplex=8111" };
		Genesis.main(args);
	}

	// @Test
	public void testHAMasterd() throws Exception {
		int masterPort = 8091;
		// int centerPort = 8111;
		// String centerURL = "tcp://127.0.0.1:" + centerPort;
		new HAMasterDServiceComponent(masterPort).start();
		testHACenter();
		// String[] args = new String[] { "-ha", String.valueOf(masterPort),
		// "-agent", centerURL, "-ports",
		// "rpc=8080;http=8005;monitor=8007;rpc_duplex=8111;" };
		String[] args = new String[] { "-ha", "-agent", "-ports", "" };
		Genesis.main(args);
	}

	public void testHACenter() throws Exception {
		String[] args = new String[] { "-ports", "rpc_duplex=8111;rpc=8112;monitor=8113" };
		ServiceSettings.INSTANCE.loadFromXml();
		ServiceSettings.INSTANCE.assignOpts(args);
		ServiceSettings.INSTANCE.verifyConfig();
		new com.feinno.ha.center.CenterServiceComponent().start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args1) throws Exception {
		// ProtoManager.toByteArray("");
		// 本地测试
		// new GenesisTester().testLocalCenter();
		// Thread.sleep(5 * 1000);

		// HA测试，不受HAMaster控制
		// new GenesisTester().testHA();
		// HAMasterd测试，受HAMaster控制
		// new GenesisTester().testHAMasterd();
		// Genesis.main(null);

		// 联调 1.HACenter测试
		// new GenesisTester().testHACenter();
		// new GenesisTester().testWorker();

		// System.out.println(System.getProperty("sun.java.command"));
		// System.out.println(ServiceEnviornment.getServiceName());
		// System.out.println(ServiceEnviornment.getComputerName());
		// System.out.println(ServiceEnviornment.getPid());
		// args = new String[] { "-agent", "tcp://127.0.0.1:" + 8111, "-ports",
		// "rpc=8080;http=8005;monitor=8007" };
		// Genesis.main(args);

		// 10.10.40.132

		// ServiceSettings.init();
//		new GenesisTester().testLocalCenter();
		new GenesisTester().testWorker();
	}
}
