package test.com.feinno.rpc.future;

import org.slf4j.impl.SimpleLoggerFactory;

import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.server.RpcServiceBootstrap;
import com.feinno.threading.ExecutorFactory;

public class FutureServer {

	public FutureServer() throws Exception {
		// 注册服务端通道
		RpcServiceBootstrap.registerChannel(new RpcTcpServerChannel(8001));
		RpcServiceBootstrap.registerService(new FutureService());
		RpcServiceBootstrap.setExecutor(ExecutorFactory.newFixedExecutor("mock", 32, 32 * 1000));

		SimpleLoggerFactory.INSTANCE.setInfoEnable(false);
		System.out.println("Test server started. [V39]");
		RpcServiceBootstrap.start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
