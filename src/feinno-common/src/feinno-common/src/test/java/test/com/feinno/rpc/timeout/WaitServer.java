package test.com.feinno.rpc.timeout;

import org.slf4j.impl.SimpleLoggerFactory;

import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.server.RpcServiceBootstrap;
import com.feinno.threading.ExecutorFactory;

public class WaitServer {

	public WaitServer() throws Exception {
		// 注册服务端通道
		RpcServiceBootstrap.registerChannel(new RpcTcpServerChannel(8002));
		RpcServiceBootstrap.registerService(new WaitService());
		RpcServiceBootstrap.setExecutor(ExecutorFactory.newFixedExecutor("mock", 32, 32 * 1000));

		SimpleLoggerFactory.INSTANCE.setInfoEnable(false);
		System.out.println("Test WaitServer started. [V39]");
		RpcServiceBootstrap.start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
