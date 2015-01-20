/**
 * 
 */
package com.feinno.ha.center;

import com.feinno.rpc.channel.RpcClientTransaction;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.client.RpcMethodStub;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.serialization.protobuf.types.ProtoString;
import com.feinno.threading.ExecutorFactory;
import com.feinno.util.EventHandler;

/**
 * <b>描述: 测试master透传</b>
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Zhou.yan
 * 
 */
public class TestMasterProxyService {

	public static void main(String[] arg) throws Exception {
		RpcDuplexClient client = new RpcDuplexClient(
				RpcTcpEndpoint.parse("tcp://127.0.0.1:11211"));
		client.setExecutor(ExecutorFactory.getExecutor("Test"));
		client.connectSync();
		RpcMethodStub stub = client.getMethodStub(
				"RpcSampleAgentCallbackService", "Test");
		RpcClientTransaction tx = stub.createTransaction();
		ProtoString args = new ProtoString();
		args.setValue("Test Proxy");
		tx.setArgs(args);
		tx.putExtension(1001, "zhouyanxjs");
		final RpcFuture future = tx.begin();
		future.addListener(new EventHandler<RpcResults>() {

			public void run(Object sender, RpcResults e) {
				future.complete(e);
			}

		});
		future.await();
		RpcResults result = future.getValue();

		if (result.getError() != null)
			result.getError().printStackTrace();

		args = result.getValue(ProtoString.class);

		if (args != null)
			System.out.println(args.getValue());
		else
			System.out.println("returned value is null");

		System.out.println("call proxy done");
	}
}
