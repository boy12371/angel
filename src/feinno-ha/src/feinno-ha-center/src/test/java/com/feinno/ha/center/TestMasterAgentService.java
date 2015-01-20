/**
 * 
 */
package com.feinno.ha.center;

import java.util.ArrayList;
import java.util.Date;

import com.feinno.ha.interfaces.master.HAMasterAgentService;
import com.feinno.ha.interfaces.master.HAMasterHeartbeatArgs;
import com.feinno.ha.interfaces.master.HAMasterRegisterArgs;
import com.feinno.ha.interfaces.master.HAMasterWorkerStatus;
import com.feinno.ha.interfaces.monitor.HAMonitorValuePair;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.serialization.protobuf.types.ProtoString;
import com.feinno.threading.ExecutorFactory;

/**
 * <b>描述: 测试MasterProxyService透传</b>
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Zhou.yan
 * 
 */
public class TestMasterAgentService {

	public static void main(String[] arg) throws Exception {
		RpcDuplexClient client = new RpcDuplexClient(
				RpcTcpEndpoint.parse("tcp://127.0.0.1:11211"));
		client.setExecutor(ExecutorFactory.newFixedExecutor("SERVER", 10, 10240));
		client.registerCallbackService(new RpcSampleAgentCallbackService() {
			public ProtoString test(ProtoString args) {
				System.out.println(args.getValue());
				return args;
			}
		});
		client.connectSync();
		// RpcMethodStub stub = client.getMethodStub("MasterAgentService",
		// "Register");
		HAMasterRegisterArgs args = new HAMasterRegisterArgs();
		args.setMasterPid(10001);
		args.setMasterStartTime(new Date());
		args.setMasterVersion("1.0.0");
		args.setServerName("zhouyanxjs");
		HAMasterAgentService masterAgent = client
				.getService(HAMasterAgentService.class);
		masterAgent.register(args);
		System.out.println("call register done");

		while (true) {
			HAMasterHeartbeatArgs input = new HAMasterHeartbeatArgs();
			input.setMachineValues(new ArrayList<HAMonitorValuePair>());
			input.setMasterStatus("NORMAL");
			input.setMasterStatusEx("test");
			input.setWorkerStatus(new ArrayList<HAMasterWorkerStatus>());
			masterAgent.heartbeat(input);
			Thread.sleep(1000 * 60);
		}
	}
}
