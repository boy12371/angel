package test.com.feinno.appengine.testing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.feinno.appengine.runtime.configuration.RunningWorkerEntity;
import com.feinno.serialization.protobuf.ProtoManager;

public class TestZK {
	public final static String FAE_WORKER_PATH = "/FAE/RunningWorkers";
	
	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		String zkHosts = "192.168.110.231:8998";
		int sessionTimeout = 30000;
		Watcher watcher = null;
		ZooKeeper zk = new ZooKeeper(zkHosts, sessionTimeout, watcher);
		
		List<String> children = zk.getChildren(FAE_WORKER_PATH, false, null);
		List<byte[]> dataList = new ArrayList<byte[]>();

		for (String child : children) {
			try {
				byte[] data = zk.getData(FAE_WORKER_PATH + "/" + child, false, null);
				dataList.add(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (byte[] data : dataList) {
			RunningWorkerEntity worker = new RunningWorkerEntity();
			try {
				ProtoManager.parseFrom(worker, data);// worker.getServerName();worker.getServiceUrls();
				System.out.println(worker.getAppWorkerId()+"---"+worker.getServiceUrl("rpc"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
