package test.com.feinno.appengine.runtime;

import org.apache.zookeeper.ZooKeeper;

import com.feinno.appengine.runtime.configuration.RunningWorkerEntity;

public class ZKRunningWorkersReader {

	public static void main(String[] args) throws Exception {
		
		String connStr = null;
		
		if (args.length == 0) { 
			connStr = "192.168.110.231:8998";
		} else {
			connStr = args[0];
		}
//		connStr = "192.168.110.231:8998";
		ZooKeeper zk = new ZooKeeper(connStr, 10 * 1000, null); // TODO sessionTimeout Settings
		String path = "/FAE/RunningWorkers/imps-app_2011-4-26.tar.gzTest-0000001196";
		byte[] data = zk.getData(path, false, null);
		System.out.println("data len is : " + data.length);
		RunningWorkerEntity obj = new RunningWorkerEntity();
		obj.parseFrom(data);
		RunningWorkerEntity worker = obj;
	
		System.out.println("worker informatiopn : ");
		System.out.println("appWorkerId : " + worker.getAppWorkerId());
		System.out.println("serverName : " + worker.getServerName());
		System.out.println("serviceUrls : " + worker.getServiceUrls());
		System.out.println("lastTime : " + worker.getLastTime());
	}
}
