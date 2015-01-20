package com.feinno.appengine.testing;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.feinno.appengine.runtime.AppEngineManager;

import com.feinno.appengine.testing.AppBeanInjectorService.InjectArgs;
import com.feinno.serialization.Serializer;

public class Injector {

	static String InjectorPath = "/FAE/Injectors";

	public static String Inject(String targetSvc, InjectArgs args)
			throws IOException, KeeperException, InterruptedException {
		String zkstr = AppEngineManager.INSTANCE.getSettings().getZkHosts();
		ZooKeeper zk = new ZooKeeper(zkstr, 5000, null); // watcher为null时，日志很难看
		
		Stat s = zk.exists(InjectorPath, false);
		if(s == null) {
			zk.create(InjectorPath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		
		byte[] data = Serializer.encode(args);
		return zk.create(InjectorPath + "/" +  targetSvc, data, Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL_SEQUENTIAL);
	}
}
