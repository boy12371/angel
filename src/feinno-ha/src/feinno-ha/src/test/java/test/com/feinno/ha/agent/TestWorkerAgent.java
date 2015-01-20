//package test.com.feinno.ha.agent;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Properties;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.feinno.database.DBConnectionPoolType;
//import com.feinno.database.Database;
//import com.feinno.database.DatabaseManager;
//import com.feinno.diagnostic.observation.ObserverReport;
//import com.feinno.diagnostic.observation.ObserverReportMode;
//import com.feinno.ha.agent.WorkerAgent;
//import com.feinno.ha.interfaces.monitor.HAMonitorConfig;
//import com.feinno.ha.interfaces.monitor.HAMonitorReportConfig;
//import com.feinno.ha.interfaces.worker.HAWorkerHeartbeatArgs;
//import com.feinno.ha.interfaces.worker.HAWorkerRegisterArgs;
//import com.feinno.logging.spi.LogManager;
//import com.feinno.rpc.RpcResults;
//import com.feinno.rpc.RpcServerContext;
//import com.feinno.rpc.channel.RpcConnection;
//import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
//import com.feinno.serialization.protobuf.util.FileUtil;
//import com.feinno.serialization.types.ProtoByteArray;
//import com.feinno.serialization.types.ProtoString;
//import com.feinno.util.Action;
//import com.feinno.util.PropertiesUtil;
//
//public class TestWorkerAgent {
//
//	@Before
//	public void registerService() {
//		EmulatorService workerHeartbeatService = new EmulatorService(
//				"HAWorkerHeartbeatService");
//		workerHeartbeatService.registerMethod("Register", new ServiceProcess() {
//
//			@Override
//			public void onProcess(RpcServerContext ctx) {
//				HAWorkerRegisterArgs args = ctx
//						.getArgs(HAWorkerRegisterArgs.class);
//				LOGGER.debug("context uri:{}", ctx.serverContext());
//				LOGGER.debug("ServerName:{}", args.getServerName());
//				LOGGER.debug("WorkerId:{}", args.getWorkerId());
//				conn = ctx.getConnection();
//				ctx.end();
//			}
//		});
//		workerHeartbeatService.registerMethod("Heartbeat",
//				new ServiceProcess() {
//
//					@Override
//					public void onProcess(RpcServerContext ctx)
//							throws Exception {
//						HAWorkerHeartbeatArgs args = ctx
//								.getArgs(HAWorkerHeartbeatArgs.class);
//						LOGGER.debug("context uri:{}", ctx.serverContext());
//						LOGGER.debug("ServerName:{}", args.getServerName());
//						LOGGER.debug("WorkerId:{}", args.getWorkerId());
//						if (flag) {
//							ctx.end(new Exception("conver try...catch..."));
//							flag = false;
//						} else {
//							ctx.end();
//						}
//
//					}
//
//				});
//
//		EmulatorService workerMonitorService = new EmulatorService(
//				"HAWorkerMonitorService");
//		workerMonitorService.registerMethod("GetMonitorConifig",
//				new ServiceProcess() {
//
//					@Override
//					public void onProcess(RpcServerContext ctx)
//							throws Exception {
//						ProtoString ps = ctx.getArgs(ProtoString.class);
//						LOGGER.debug("args:", ps.getValue());
//						HAMonitorConfig mconfig = new HAMonitorConfig();
//						HAMonitorReportConfig report1 = new HAMonitorReportConfig();
//						report1.setCategory("rpc-client");
//						report1.setRecodeMode(ObserverReportMode.ALL);
//						report1.setIntervalSeconds(15);
//						report1.setServer("zhouyanxjs");
//						report1.setUploadMode(ObserverReportMode.ALL);
//						report1.setUpload(true);
//						report1.setThresholds("(\"tx(concurrent.)\" > 0.232);(\"tx(cost ms.)\" >= 1.1); (\"tx(failed.)\" < 100);(\"tx(/sec.)\" <= 222);");
//						HAMonitorReportConfig report2 = new HAMonitorReportConfig();
//						report2.setCategory("rpc-server");
//						report2.setRecodeMode(ObserverReportMode.ALL);
//						report2.setIntervalSeconds(15);
//						report2.setServer("zhouyanxjs");
//						report2.setUploadMode(ObserverReportMode.ALL);
//						report2.setUpload(false);
//						report2.setThresholds("(\"tx(concurrent.)\" > 0.232);(\"tx(cost ms.)\" >= 1.1); (\"tx(failed.)\" < 100);(\"tx(/sec.)\" <= 222);");
//						mconfig.getReportConfigs().add(report1);
//						mconfig.getReportConfigs().add(report2);
//						ctx.end(mconfig);
//					}
//				});
//
//		ServiceProcess uploadReportProcessor = new ServiceProcess() {
//
//			@Override
//			public void onProcess(RpcServerContext ctx) throws Exception {
//				LOGGER.debug("context uri:{}", ctx.serverContext()
//						.getContextUri());
//				ProtoByteArray report = ctx.getArgs(ProtoByteArray.class);
//				ObserverReport observerReport;
//				observerReport = ObserverReport.decodeFromProtobuf(report
//						.getValue());
//				LOGGER.debug("--------------------------------"
//						+ observerReport.encodeToJson());
//				ctx.end();
//			}
//
//		};
//		workerMonitorService.registerMethod("UploadAlertReport",
//				new ServiceProcess() {
//
//					@Override
//					public void onProcess(RpcServerContext ctx)
//							throws Exception {
//						LOGGER.debug("context uri:{}", ctx.serverContext()
//								.getContextUri());
//						ProtoByteArray report = ctx
//								.getArgs(ProtoByteArray.class);
//						ObserverReport observerReport;
//						observerReport = ObserverReport
//								.decodeFromProtobuf(report.getValue());
//						LOGGER.debug("--------------------------------"
//								+ observerReport.encodeToJson());
//						if (flag2) {
//							ctx.end(new Exception("make an error"));
//							flag2 = false;
//						} else {
//							ctx.end();
//						}
//
//					}
//
//				});
//		workerMonitorService.registerMethod("UploadObserverReport",
//				uploadReportProcessor);
//
//		RpcServerEmulator server = RpcServerEmulator.startup(11211);
//		server.registerService(workerHeartbeatService);
//		server.registerService(workerMonitorService);
//
//	}
//
//	@Test
//	public void testWorker() throws FileNotFoundException, IOException,
//			InterruptedException {
//		Properties propts = PropertiesUtil.xmlToProperties(new File((System
//				.getProperty("user.dir") + File.separator + "logging.xml")));
//		LogManager.loadSettings(propts);
//		ByteArrayInputStream instr = new ByteArrayInputStream(
//				servicep.getBytes());
//		Properties service = new Properties();
//		service.load(instr);
//		// TODO 多客户端测试使用
//		service.put("workerName", "TestFAEWorker" + System.currentTimeMillis());
//		db = new Properties();
//		db.put("DriverClass", "com.mysql.jdbc.Driver");
//		db.put("JdbcUrl",
//				"jdbc:mysql://192.168.110.234/test?user=admin&password=admin");
//		db.put("Database", "test");
//
//		agent = new WorkerAgent();
//		RpcTcpEndpoint tcpEndpoint = RpcTcpEndpoint.parse(service
//				.getProperty("centerUrl"));
//		agent.connect(tcpEndpoint, service, db);
//		// this sleeping is for testing heartbeat.
//		Thread.sleep(16000);
//		if (conn != null) {
//			ProtoString ps = new ProtoString();
//			ps.setValue("UserTable");
//			conn.getProxy("DynamicTableUpdateService").invoke(
//					"UpdateTableNotify", ps, new Action<RpcResults>() {
//
//						@Override
//						public void run(RpcResults a) {
//							Assert.assertTrue(
//									"invoke client UpdateTableNotify",
//									a.error() == null);
//						}
//
//					});
//		}
//		// waitting callback finished.
//		Thread.sleep(1000);
//		RpcServerEmulator.getEmulator(11211).shutdown();
//		// this one is for the reconnection.
//		Thread.sleep(21000);
//		RpcServerEmulator.startup(11211);
//		registerService();
//		// reconnection will be success and report data from counters.
//		Thread.sleep(36000);
//	}
//
//	@After
//	public void setDown() throws SQLException {
//		agent.destroy();
//		DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//		String datetime = formatter.format(new Date());
//		Database database = DatabaseManager.getDatabase("test", db,
//				DBConnectionPoolType.C3p0);
//		String cmd = "drop table `OBR_rpc-server_" + datetime + "`";
//		database.executeNonQuery(cmd);
//	}
//
//	private String servicep = "runMode=LOCAL\n" +
//
//	"orkerName=TestFAEWorker\n" +
//
//	"serverName=zhouyanxjs\n" +
//
//	"dynamicTableName=ha_workerendpoint\n" +
//
//	"centerUrl=tcp://127.0.0.1:11211/\n" +
//
//	"workerComponent=com.feinno.appengine.AppEngineComponent\n" +
//
//	"servicePorts{rpc_tcp}=7777\n" + "servicePorts{rpc_http}=4000\n"
//			+ "servicePorts{http}=8080\n" + "servicePorts{monitor}=8080\n";
//	private WorkerAgent agent;
//	private Properties db;
//	private RpcConnection conn;
//	private static boolean flag = true;
//	private static boolean flag2 = true;
//	private static Logger LOGGER = LoggerFactory
//			.getLogger(TestWorkerAgent.class);
//}
