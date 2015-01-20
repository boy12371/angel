package test.com.feinno.ha.center.worker;

import java.util.Hashtable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigParams;
import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.ha.ServiceComponent;
import com.feinno.ha.WorkerAgentHA.HALoggingMarkerTableItem;
import com.feinno.logging.LogEvent;

public class ClientServiceComponent implements ServiceComponent {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceComponent.class);

	public void start() throws Exception {
		// RpcDuplexClient client =
		// ServiceSettings.INSTANCE.getWorkerDuplexClient();
		// HACenterDeploymentService service =
		// client.getService(HACenterDeploymentService.class);
		// String[] groups =
		// service.getServerGroups(ServiceEnviornment.getComputerName());
		ConfigurationManager.loadTable(String.class, CFG_Site.class, "CFG_Site",
				new ConfigUpdateAction<ConfigTable<String, CFG_Site>>() {
					@Override
					public void run(ConfigTable<String, CFG_Site> a) throws Exception
					{
						Hashtable<String, CFG_Site> tbl = new Hashtable<String, CFG_Site>();
						for (CFG_Site s : a.getValues()) {
							System.out.println(s.getName()+"  "+ s.getAppEngineGateway());
							tbl.put(s.getName(), s);
						}
						System.out.println(tbl);
					}
				});
		ConfigurationManager.loadProperties("testBaseCenter.properties", new ConfigParams("name=lvmingwei@163.com"),
				new ConfigUpdateAction<Properties>() {
					@Override
					public void run(Properties props) throws Exception {
						if (props != null) {
							LOGGER.error("testBaseCenter.properties is {}", props);
						} else {
							LOGGER.info("testBaseCenter.properties is empty.");
						}
					}
				});

		ConfigurationManager.loadTable(String.class, HALoggingMarkerTableItem.class, "HA_LoggingMarker_Base_Center",
				new ConfigUpdateAction<ConfigTable<String, HALoggingMarkerTableItem>>() {
					public void run(ConfigTable<String, HALoggingMarkerTableItem> value) throws Exception {
						LOGGER.info("Load Marker from HA-Center.");
						if (value != null && value.getValues() != null && value.getValues().size() > 0) {
							for (HALoggingMarkerTableItem loggingMarker : value.getValues()) {
								LOGGER.info("HA_LoggingMarker_Base_Center 有数据 {}", loggingMarker.getMarker());
							}
						} else {
							LogEvent.isEnableDefaultMarker = false;
						}
					}
				});
		// try {

		// ConfigTable<Integer, FloatTest> table =
		// ConfigurationManager.loadTable(Integer.class, FloatTest.class,
		// "Float_Test", null);
		// if (table != null) {
		// System.out.println(table.getCount());
		// for (Entry<Integer, FloatTest> entity : table.getSet()) {
		// System.out.println("------------------------------------");
		// System.out.println("F1: " + entity.getValue().getF1());
		// System.out.println("F2: " + entity.getValue().getF2());
		// System.out.println("F3: " + entity.getValue().getF3());
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// ConfigurationManager.loadText("logging.xml", null, new
		// ConfigUpdateAction<String>() {
		// public void run(String e) throws Exception {
		// System.out.println("config text : \r\n" + e);
		// }
		// });
		// ConfigurationManager.subscribeConfigUpdate(ConfigType.TEXT,
		// "logging.xml", null);
		// ConfigurationManager.loadText("TestBaseHADB", null, new
		// ConfigUpdateAction<String>() {
		// public void run(String e) throws Exception {
		// System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
		// System.out.println("TestBaseHADB : \r\n" + e);
		// System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
		// }
		// });

		// System.out.println("-------------------------------------------------------");
		// System.out.println("PackageId : " +
		// ServiceSettings.INSTANCE.getPackageId());
		// System.out.println("PackageInfo.PackageId ： " +
		// ServiceSettings.INSTANCE.getPackageInfo().getPackageId());
		// System.out.println("PackageInfo.PackageUrl ： " +
		// ServiceSettings.INSTANCE.getPackageInfo().getPackageUrl());
		// System.out.println("PackageInfo.ServiceVersion ： "
		// + ServiceSettings.INSTANCE.getPackageInfo().getServiceVersion());
		// System.out.println("ServerGroups : " +
		// Arrays.toString(ServiceSettings.INSTANCE.getServerGroups()));
		// System.out.println("-------------------------------------------------------");
		// Thread.sleep(1000000000);
		// ConfigurationManager.loadProperties("serviceSettings.properties", new
		// ConfigParams("service=RA"),
		// new ConfigUpdateAction<Properties>() {
		// @Override
		// public void run(Properties props) throws Exception {
		// ConfigBean configBean = ConfigBean.valueOf(props, ConfigBean.class);
		// if (configBean.getChild("servicePorts") != null) {
		// String rpc_uds =
		// configBean.getChild("servicePorts").getFieldValue("rpc_uds");
		// if (rpc_uds == null) {
		// LOGGER.error("配置文件中servicePorts这个属性没有配置rpc_uds，例如servicePorts{rpc_uds}",
		// "");
		// return;
		// } else {
		// LOGGER.info("ok-----------------------------");
		// // RpcServiceBootstrap.registerChannel(new
		// // RpcUdsServerChannel(rpc_uds));
		// // RpcServiceBootstrap.registerService(new
		// // NginxProxyServiceImpl());
		// // SSIApplicationHelper.intialize(true);
		// // RpcServiceBootstrap.start();
		// }
		// } else {
		// LOGGER.error("配置文件中没有找到servicePorts这个配置属性", "");
		// }
		// }
		// });

	}

	public void stop() throws Exception {
		Thread.sleep(1 * 1000);
	}

}
