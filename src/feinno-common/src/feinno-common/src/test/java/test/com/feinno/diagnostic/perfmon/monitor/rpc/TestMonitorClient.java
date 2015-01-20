package test.com.feinno.diagnostic.perfmon.monitor.rpc;

import com.feinno.diagnostic.perfmon.monitor.rpc.MonitorCategoryEntity;
import com.feinno.diagnostic.perfmon.monitor.rpc.MonitorService;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.client.RpcProxyFactory;

public class TestMonitorClient {

	public static void main(String[] args) {

		RpcTcpEndpoint testTcpEp = RpcTcpEndpoint.parse("tcp://127.0.0.1:8081");
		MonitorService mon = RpcProxyFactory.getService(testTcpEp,
				MonitorService.class);
		MonitorCategoryEntity[] mCategoryEntityList = mon.getCategoryList();
		System.out.println(mCategoryEntityList.length);
		for(MonitorCategoryEntity mce :mCategoryEntityList){
			System.out.println(mce.getName()+":"+mce.getInstance());
		}
	}
}
