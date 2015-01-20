package test.com.feinno.appengine.testing;

import com.feinno.appengine.testing.AppBeanInjectorService;
import com.feinno.appengine.testing.AppBeanInjectorService.InjectArgs;
import com.feinno.appengine.testing.AppBeanInjectorServiceImps;
import com.feinno.appengine.testing.DebugProxyManager;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.spi.LocalConfigurator;
import com.feinno.rpc.RpcEndpointFactory;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.client.RpcMethodStub;
import com.feinno.rpc.client.RpcProxyFactory;
import com.feinno.rpc.duplex.RpcDuplexServer;

public class TestAppBeanInjectorService {

	private static final int port = 8888;

	public static void startServer() throws Exception {
		RpcTcpServerChannel channel = new RpcTcpServerChannel(port);
		RpcDuplexServer server = new RpcDuplexServer(channel);
		server.registerService(AppBeanInjectorServiceImps.INSTANCE);
		channel.start();
	}

	public static void startClient() throws Exception {
		RpcEndpoint endpoint = RpcEndpointFactory.parse("tcp://127.0.0.1:" + port);
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(endpoint, AppBeanInjectorService.SERVICE_NAME, "Inject");
		InjectArgs args = new InjectArgs();
		String annos = "{\"classInfo\":{\"type\":\"com.feinno.wats.NotifyContactListRemoteAppBean\",\"version\":\"4.3.1.0930\",\"baseClass\":{\"type\":\"com.feinno.appengine.rpc.RemoteAppBean\",\"genericParams\":[{\"key\":\"A\",\"value\":\"com.feinno.wats.struct.SyncContactListArgs\"},{\"key\":\"R\",\"value\":\"com.feinno.wats.struct.UserChangedNotifyResults\"},{\"key\":\"C\",\"value\":\"com.feinno.appengine.context.NullContext\"}]}},\"annotations\":[{\"type\":\"com.feinno.appengine.annotation.AppName\",\"fields\":[{\"key\":\"category\",\"value\":\"wats\"},{\"key\":\"name\",\"value\":\"WatsReceiveContactList\"}]},{\"type\":\"com.feinno.appengine.annotation.PeerSite\",\"fields\":[]}]}";
		args.setAnnos(annos);
		RpcFuture future = stub.invoke(args);
		future.syncGet(String.class); // 此处有可能抛出RpcException
	}

	public static void debug() throws Exception {
		ConfigurationManager.setConfigurator(new LocalConfigurator());
		System.out.println(DebugProxyManager.isCan("wats-WatsReceiveContactList", null));
	}

	public static void main(String args[]) throws Exception {
		startServer();
		startClient();
		debug();
	}
}
