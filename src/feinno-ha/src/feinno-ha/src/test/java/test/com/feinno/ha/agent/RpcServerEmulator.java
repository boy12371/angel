//package test.com.feinno.ha.agent;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.Properties;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.feinno.logging.spi.LogManager;
//import com.feinno.rpc.RpcException;
//import com.feinno.rpc.channel.RpcConnection;
//import com.feinno.rpc.channel.RpcConnectionMode;
//import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
//import com.feinno.rpc.channel.tcp.RpcTcpTransactionManager;
//import com.feinno.rpc.duplex.RpcDuplexServer;
//import com.feinno.util.PropertiesUtil;
//
//public class RpcServerEmulator {
//
//	private RpcServerEmulator(int port) {
//		try {
//			if (running) {
//				throw new RuntimeException(
//						"Emulator has to stop and then restart.");
//			}
//			RpcTcpTransactionManager.init(8000, 180000);
//			running = true;
//			Properties propts = PropertiesUtil
//					.xmlToProperties(new File((System.getProperty("user.dir")
//							+ File.separator + "logging.xml")));
//			LogManager.loadSettings(propts);
//			serverChannel = new RpcTcpServerChannel(port,
//					RpcConnectionMode.DUPLEX);
//			server = new RpcDuplexServer(serverChannel) {
//
//				@Override
//				public void connectionCreated(RpcConnection conn) {
//				}
//
//				@Override
//				public void connectionDestoryed(RpcConnection conn) {
//				}
//
//			};
//			serverChannel.start();
//			LOGGER.info("===============================server has started on port "
//					+ port + ".=================================");
//		} catch (RpcException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	public void shutdown() {
//		if (serverChannel != null) {
//			serverChannel.stop();
//			emulators.remove(this.port);
//			running = false;
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			LOGGER.info("===========================Server has Stoped on port "
//					+ this.port + "=========================");
//			this.port = 0;
//		}
//	}
//
//	public static RpcServerEmulator startup(int port) {
//		if (emulators.get(port) == null) {
//			RpcServerEmulator emulator = new RpcServerEmulator(port);
//			emulators.put(port, emulator);
//			emulator.port = port;
//			return emulator;
//		} else {
//			return emulators.get(port);
//		}
//	}
//
//	public static RpcServerEmulator getEmulator(int port) {
//		return emulators.get(port);
//	}
//
//	public boolean isStop() {
//		return running;
//	}
//
//	public void registerService(EmulatorService service) {
//		if (server == null) {
//			throw new RuntimeException("Must start this emulator at first.");
//		}
//		server.registerService(service.getService());
//	}
//
//	private Logger LOGGER = LoggerFactory.getLogger(RpcServerEmulator.class);
//	private RpcDuplexServer server;
//	private RpcTcpServerChannel serverChannel;
//	private boolean running = false;
//	private int port = 0;
//	private static Hashtable<Integer, RpcServerEmulator> emulators = new Hashtable<Integer, RpcServerEmulator>();
//}
