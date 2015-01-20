package test.com.feinno.appengine.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.appengine.route.ZookeeperWatcherHelper;
import com.feinno.appengine.runtime.configuration.RunningWorkerEntity;

public class ZookeeperWatcherTest
{

//	@Test
//	public final void testParseServiceUrls()
//	{
//		String url = "rpc=tcp://192.168.110.88:2323;http=http://192.168.110.88:8800;";
//
//		java.util.Map<String, String> res = RouterHelper.parseServiceUrls(url);
//
//		assertTrue("has tow elements", res.size() == 2);
//		String rpc = res.get("RPC");
//		Assert.assertTrue("hasRpc", rpc != null);
//		Assert.assertEquals("tcp://192.168.110.88:2323", rpc);
//		String http = res.get("HTTP");
//		Assert.assertTrue("hasHTTP", http != null);
//		Assert.assertEquals("http://192.168.110.88:8800", http);
//
//		url = "";
//
//		res = RouterHelper.parseServiceUrls(url);
//		assertTrue("empty content", res.size() == 0);
//
//		url = ";;;;";
//
//		res = RouterHelper.parseServiceUrls(url);
//		assertTrue("empty items", res.size() == 0);
//
//		url = "tcp://192.168.110.88:2323;http=http://192.168.110.88:8800";
//
//		res = RouterHelper.parseServiceUrls(url);
//		assertTrue("invalid config", res.size() == 0);
//
//		url = "http=http://192.168.110.88:8800";
//
//		res = RouterHelper.parseServiceUrls(url);
//		assertTrue("got one", res.size() == 1);
//
//		url = "rpc=tcp://192.168.110.88:2323;http=http://192.168.110.88:8800";
//		res = RouterHelper.parseServiceUrls(url);
//		assertTrue("got one", res.size() == 1);
//	}
//
	private RunningWorkerEntity buildWorker(String workerId, String serverName, String serviceUrls)
	{
		RunningWorkerEntity val = new RunningWorkerEntity();
		Date time = new Date(System.currentTimeMillis());
		val.setAppWorkerId(workerId);
		val.setServerName(serverName);
		val.setServiceUrls(serviceUrls);
		val.setLastTime(time);
		try {
			Thread.sleep(2); // lasttime能分开
		} catch (Exception e) {
			// swallow
		}
		return val;
	}

	private void dumpList(List<RunningWorkerEntity> workers)
	{
		for (RunningWorkerEntity worker : workers) {
			System.out.println(worker.getAppWorkerId() + " " + worker.getServerName() + " " + worker.getServiceUrls()
					+ " " + worker.getLastTime().getTime());
		}
	}

	@Test
	public final void testSortAndDropZombieRunningWorkers()
	{

		List<RunningWorkerEntity> workers = new ArrayList<RunningWorkerEntity>();

		workers.add(buildWorker("worker1", "server1", "tcp://192.168.110.88:2323;http=http://192.168.110.88:8800"));
		workers.add(buildWorker("worker1", "server2", "tcp://192.168.110.88:2323;http=http://192.168.110.88:8800"));
		workers.add(buildWorker("worker1", "server3", "tcp://192.168.110.88:2323;http=http://192.168.110.88:8800"));
		workers.add(buildWorker("worker1", "server1", "tcp://192.168.110.88:2323;http=http://192.168.110.88:8800"));
		workers.add(buildWorker("worker1", "server2", "tcp://192.168.110.88:2323;http=http://192.168.110.88:8800"));
		workers.add(buildWorker("worker1", "server3", "tcp://192.168.110.88:2323;http=http://192.168.110.88:8800"));
		workers.add(buildWorker("worker2", "server1", "tcp://192.168.110.88:2323;http=http://192.168.110.88:8800"));
		workers.add(buildWorker("worker2", "server2", "tcp://192.168.110.88:2323;http=http://192.168.110.88:8800"));
		workers.add(buildWorker("worker2", "server3", "tcp://192.168.110.88:2323;http=http://192.168.110.88:8800"));
		workers.add(buildWorker("worker3", "server4", "tcp://192.168.110.88:2323;http=http://192.168.110.88:8800"));

		Collections.shuffle(workers);
		dumpList(workers);

		System.out.println("========AFTER SORT=============");

		ZookeeperWatcherHelper.sortRunningWorkers(workers);
		dumpList(workers);

		List<RunningWorkerEntity> newVal = ZookeeperWatcherHelper.dropZombies(workers);
		System.out.println("========AFTER DROP ZOMBIES=============");
		dumpList(newVal);
		Assert.assertEquals(7, newVal.size());
	}

//	@Test
//	public final void testParseHostInfo()
//	{
//		String test = "http://192.168.110.88:8800";
//		HostInfo host = ZookeeperWatcherHelper.parseHostInfo("http://", test);
//
//		Assert.assertEquals("192.168.110.88", host.host);
//		Assert.assertEquals("8800", host.port);
//	}

//	@Test
//	public final void testToWrapperList()
//	{
//		fail("Not yet implemented"); // TODO
//	}
		

}
