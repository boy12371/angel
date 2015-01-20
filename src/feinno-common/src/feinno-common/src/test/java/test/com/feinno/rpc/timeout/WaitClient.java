package test.com.feinno.rpc.timeout;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;

import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.client.RpcMethodStub;
import com.feinno.rpc.client.RpcProxyFactory;
import com.feinno.util.DateUtil;
import com.feinno.util.EventHandler;

public class WaitClient {

	private static RpcTcpEndpoint tcpEndpoint;
	private static RpcMethodStub wait;

	private static AtomicInteger counter = new AtomicInteger();

	@Before
	public void setUp() {
		try {
			if (tcpEndpoint == null) {
				new WaitServer();
				tcpEndpoint = new RpcTcpEndpoint(new InetSocketAddress("127.0.0.1", 8002));
				wait = RpcProxyFactory.getMethodStub(tcpEndpoint, "WaitService", "wait");
				testSync(0, 20000, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean testSync(int waitTime, int timeout, String contextUri) {
		int seq = counter.incrementAndGet();
		System.out.println(seq + ". Send "
				+ DateUtil.formatDate(new Date(), DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT_LONG));
		RpcFuture future = contextUri != null ? wait.invoke(waitTime, contextUri, timeout) : wait.invoke(waitTime,
				timeout);
		boolean value = future.getValue().getValue(Boolean.class);
		System.out.println(seq + ". End  "
				+ DateUtil.formatDate(new Date(), DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT_LONG));
		return value;
	}

	public void testAsync(int waitTime, int timeout, String contextUri) {
		int seq = counter.incrementAndGet();
		System.out.println(seq + ". Send "
				+ DateUtil.formatDate(new Date(), DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT_LONG));
		RpcFuture future = contextUri != null ? wait.invoke(waitTime, contextUri, timeout) : wait.invoke(waitTime,
				timeout);
		future.addListener(newListener(seq));
	}

	/**
	 * 为这个Test提供的一个快捷创建Listener的方法
	 * 
	 * @return
	 */
	public static EventHandler<RpcResults> newListener(final int seq) {
		return new EventHandler<RpcResults>() {

			@Override
			public void run(Object sender, RpcResults e) {
				System.out.println(seq + ". End  "
						+ DateUtil.formatDate(new Date(), DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT_LONG));
				if (e.getError() != null) {
					Assert.assertTrue("RPC exception" + e.getError(), false);
				} else {
					Assert.assertTrue("return false", e.getValue(Boolean.class));
				}
			}
		};
	}

	public static void main(String args[]) {
		WaitClient client = new WaitClient();
		client.setUp();
		client.testAsync(2000, 5000, null);
		client.testAsync(2000, 5000, "hello");
		client.testAsync(5000, 2000, null);
		client.testAsync(5000, 2000, "hello");
	}
}
