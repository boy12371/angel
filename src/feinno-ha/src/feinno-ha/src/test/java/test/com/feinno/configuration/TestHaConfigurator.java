//package test.com.feinno.configuration;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.Date;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.feinno.configuration.ConfigTable;
//import com.feinno.configuration.ConfigUpdateAction;
//import com.feinno.configuration.ConfigurationException;
//import com.feinno.configuration.spi.Configurator;
//import com.feinno.configuration.spi.HAConfigurationLoader;
//import com.feinno.ha.interfaces.configuration.HAConfigArgs;
//import com.feinno.ha.interfaces.configuration.HAConfigTableBuffer;
//import com.feinno.ha.interfaces.configuration.HAConfigTableRow;
//import com.feinno.ha.interfaces.configuration.HAConfigTextBuffer;
//import com.feinno.ha.interfaces.configuration.HADynamicTableVersionRegisterArgs;
//import com.feinno.rpc.server.RpcServerContext;
//
//public class TestHaConfigurator {
//
//	@Test
//	public void testHa() throws Exception {
//		loadTable(loadTableAction);
//		loadText(loadTextAction);
//		loadTable(new ConfigUpdateAction<ConfigTable<String, TestSiteTableItem>>() {
//			@Override
//			public void run(ConfigTable<String, TestSiteTableItem> a)
//					throws Exception {
//				throw new Exception("test exception");
//			}
//
//		});
//		loadText(new ConfigUpdateAction<String>() {
//
//			@Override
//			public void run(String e) throws Exception {
//				throw new Exception("test exception");
//			}
//		});
////		registerDynamicTable();
//	}
//
//////	private void registerDynamicTable() {
//////		loader.registerDynamicTableVersion("TestTable", new Date().getTime());
////	}
//
//	private void loadTable(
//			ConfigUpdateAction<ConfigTable<String, TestSiteTableItem>> action) {
//		Configurator configurator = new Configurator();
//		try {
//			configurator.loadTable(String.class, TestSiteTableItem.class,
//					loader, "Test_Site", action);
//		} catch (ConfigurationException e) {
//			LOGGER.info("catch an exception {} .", e.getCause());
//		}
//	}
//
//	private void loadText(ConfigUpdateAction<String> action) {
//		Configurator configurator = new Configurator();
//		try {
//			configurator.loadText(loader, "FAEDB.properties", null, action);
//		} catch (Exception e) {
//			LOGGER.info("catch an exception {} .", e.getCause());
//		}
//	}
//
//	@Before
//	public void registerService() {
//		EmulatorService centerConfigService = new EmulatorService(
//				"HACenterConfigService");
//		centerConfigService.registerMethod("LoadConfigTable",
//				new ServiceProcess() {
//
//					@Override
//					public void onProcess(RpcServerContext ctx)
//							throws Exception {
//						HAConfigArgs args = ctx.getArgs(HAConfigArgs.class);
//						// 增加覆盖率
//						args.setParams("params");
//
//						LOGGER.debug(">>>>>" + args.getPath());
//						LOGGER.debug(">>>>>" + args.getServiceName());
//						LOGGER.debug(">>>>>" + args.getComputerName());
//						HAConfigTableBuffer buffer = new HAConfigTableBuffer();
//						buffer.setTableName(args.getPath());
//						buffer.setVersion(new Date());
//						ArrayList<String> colum = new ArrayList<String>();
//						colum.add("id");
//						buffer.setColumns(colum);
//						ArrayList<HAConfigTableRow> rowList = new ArrayList<HAConfigTableRow>();
//						buffer.setRows(rowList);
//						ArrayList<String> row = new ArrayList<String>();
//						row.add("22");
//						HAConfigTableRow ctr = new HAConfigTableRow();
//						ctr.setValues(row);
//						rowList.add(ctr);
//						// test HAConfigTableBuffer 2
//						HAConfigTableBuffer buf = new HAConfigTableBuffer();
//						buf.setDynamicTableVersion(11111l);
//						buf.setTableName("CFGSite");
//						buf.setVersion(new Date());
//						ArrayList<String> col = new ArrayList<String>();
//						col.add("SiteName");
//						col.add("SiteType");
//						col.add("Gateway");
//						buf.setColumns(col);
//						ArrayList<HAConfigTableRow> rs = new ArrayList<HAConfigTableRow>();
//						buf.setRows(rs);
//						ArrayList<String> row2 = new ArrayList<String>();
//						row2.add("11");
//						row2.add("22");
//						row2.add("33");
//						HAConfigTableRow trow2 = new HAConfigTableRow();
//						trow2.setValues(row2);
//						rs.add(trow2);
//						buf.rowCount();
//						buf.getRow(0);
//						ConfigTable<CFGSiteConfigTableKey, CFGSiteConfigTableItem> ct = buf
//								.toTable(CFGSiteConfigTableKey.class,
//										CFGSiteConfigTableItem.class);
//						ct.setTableName("CFGSite");
//						ct.setVersion(new Date());
//						// ct.tryGet(new CFGSiteConfigTableKey());
//						new ConfigTable("test", new Date());
//						junit.framework.Assert.assertTrue(ct.getCount() == 1);
//						ctx.end(buffer);
//					}
//				});
//		centerConfigService.registerMethod("LoadConfigText",
//				new ServiceProcess() {
//
//					@Override
//					public void onProcess(RpcServerContext ctx)
//							throws Exception {
//						HAConfigTextBuffer buffer = new HAConfigTextBuffer();
//						StringBuffer sb = new StringBuffer();
//						HAConfigArgs args = ctx.getArgs(HAConfigArgs.class);
//						if (args.getPath().equals("logging.xml")) {
//							ByteArrayOutputStream outs = new ByteArrayOutputStream();
//							FileInputStream in = new FileInputStream(new File(
//									(System.getProperty("user.dir")
//											+ File.separator + "logging.xml")));
//							byte[] buffers = new byte[1024 * 3];
//							int len = in.read(buffers);
//							outs.write(buffers, 0, len);
//							outs.close();
//							in.close();
//							sb.append(outs.toString());
//						} else if (args.getPath().equals(
//								"serviceSettings.properties")) {
//							sb.append("site=SiteC\n");
//							sb.append("domain=femoo.amigo.bjmcc.net\n");
//							// sb.append("workerComponent=com.feinno.appengine.gateway.GateWayHost\n");
//							sb.append("debug=true\n");
//						} else {
//							LOGGER.debug(">>>>>" + args.getPath());
//							sb.append("Test=2Test");
//						}
//						// 增加覆盖率
//						buffer.setConfigParams(new ArrayList<String>());
//						buffer.setText(sb.toString());
//						ctx.end(buffer);
//					}
//				});
//
//		centerConfigService.registerMethod("RegisterDynamicTableVersion",
//				new ServiceProcess() {
//
//					@Override
//					public void onProcess(RpcServerContext ctx)
//							throws Exception {
//						HADynamicTableVersionRegisterArgs args = ctx
//								.getArgs(HADynamicTableVersionRegisterArgs.class);
//						Assert.assertTrue(args.getServerName() != null);
//						Assert.assertTrue(args.getTableName() != null);
//						Assert.assertTrue(args.getWorkerId() != null);
//						Assert.assertTrue(args.getUnknownFields() != null);
//						Assert.assertTrue(args.getVersion() != 0);
//					}
//
//				});
//
//		centerConfigService.registerMethod("LoadDynamicTable",
//				new ServiceProcess() {
//
//					@Override
//					public void onProcess(RpcServerContext ctx)
//							throws Exception {
//						HAConfigArgs args = ctx.getArgs(HAConfigArgs.class);
//						Assert.assertTrue(!args.getPath().equals(""));
//						HAConfigTableBuffer buf = new HAConfigTableBuffer();
//						buf.setDynamicTableVersion(11111l);
//						buf.setTableName(args.getPath());
//						buf.setVersion(new Date());
//						ArrayList<String> col = new ArrayList<String>();
//						col.add("WorkerId");
//						col.add("ServerName");
//						buf.setColumns(col);
//						ArrayList<HAConfigTableRow> rs = new ArrayList<HAConfigTableRow>();
//						buf.setRows(rs);
//						ArrayList<String> row2 = new ArrayList<String>();
//						row2.add("11");
//						row2.add("22");
//						HAConfigTableRow trow2 = new HAConfigTableRow();
//						trow2.setValues(row2);
//						rs.add(trow2);
//						ctx.end(buf);
//					}
//
//				});
//
//		centerConfigService.registerMethod("RegisterDynamicTableVersion",
//				new ServiceProcess() {
//
//					@Override
//					public void onProcess(RpcServerContext ctx)
//							throws Exception {
//						HADynamicTableVersionRegisterArgs args = ctx
//								.getArgs(HADynamicTableVersionRegisterArgs.class);
//						Assert.assertTrue(args.getVersion() != 0);
//						ctx.end();
//					}
//
//				});
//
//		RpcServerEmulator server = RpcServerEmulator.startup(11211);
//		server.registerService(centerConfigService);
//	}
//
//	private ConfigUpdateAction<ConfigTable<String, TestSiteTableItem>> loadTableAction = new ConfigUpdateAction<ConfigTable<String, TestSiteTableItem>>() {
//		@Override
//		public void run(ConfigTable<String, TestSiteTableItem> a)
//				throws Exception {
//			LOGGER.info(a.getCount() + "");
//			LOGGER.info("Table$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//		}
//
//	};
//	private ConfigUpdateAction<String> loadTextAction = new ConfigUpdateAction<String>() {
//
//		@Override
//		public void run(String e) throws Exception {
//			LOGGER.info(e);
//			LOGGER.info("Text$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//		}
//	};
//	private HAConfigurationLoader loader = new HAConfigurationLoader(
//			"worker01", "test", "tcp://127.0.0.1:11211");
//	private static Logger LOGGER = LoggerFactory
//			.getLogger(TestHaConfigurator.class);
//
//}
