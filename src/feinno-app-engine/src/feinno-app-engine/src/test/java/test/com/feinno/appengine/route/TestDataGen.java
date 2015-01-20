//package test.com.feinno.appengine.route;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.List;
//
//import org.apache.zookeeper.CreateMode;
//import org.apache.zookeeper.ZooDefs.Ids;
//import org.apache.zookeeper.ZooKeeper;
//import org.apache.zookeeper.data.Stat;
//
//import com.feinno.appengine.route.ZKRouter;
//import com.feinno.appengine.runtime.configuration.Application;
//import com.feinno.appengine.runtime.configuration.RunningWorker;
//import com.google.gson.Gson;
//
//public class TestDataGen
//{
//
//	public static final String testLocalSiteName = "testSite";
//
//	public Application buildApp(int id) throws Exception
//	{
//		Application app = new Application();
//
//		app.setAppId(id);
//		app.setCategory("cat");
//		app.setName("name");
//		// app.setType("type");
//		app.setAppWorkerId("wokerName1");
//		app.setLocalSites(testLocalSiteName);
//		InputStream is = TestDataGen.class.getResourceAsStream("/test/com/feinno/appengine/route/AppAnno.js");
//
//		String line;
//		StringBuilder sb = new StringBuilder();
//		BufferedReader r = new BufferedReader(new InputStreamReader(is));
//		while ((line = r.readLine()) != null) {
//			sb.append(line).append("\n"); // hehe
//		}
//
//		app.setAnnotations(sb.toString());
//
//		return app;
//	}
//
//	public List<RunningWorker> buildWorkers() throws Exception
//	{
//		// RunningWorker worker = new RunningWorker();
//
//		InputStream is = TestDataGen.class.getResourceAsStream("/test/com/feinno/appengine/route/worker.js");
//
//		BufferedReader r = new BufferedReader(new InputStreamReader(is));
//
//		List<RunningWorker> testData;
//		Gson gson = new Gson();
//		testData = gson.fromJson(r, new com.google.gson.reflect.TypeToken<List<RunningWorker>>() {
//		}.getType());
//		System.out.println(testData.size());
//		for (RunningWorker worker : testData) {
//			Thread.sleep(1);// avoid same timestamp
//			worker.setLastTime(new java.util.Date(System.currentTimeMillis()));
//			System.out.println(worker.show());
//		}
//		return testData;
//	}
//
//	public static void main(String[] args) throws Exception
//	{
//		TestDataGen gen = new TestDataGen();
//		Application app = gen.buildApp(4);
//
//		ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 10 * 1000, null);
//
//		// zk.create("/FAE/Applications/" + app.getAppId(), app.toByteArray(),
//		// Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//
//		List<RunningWorker> workers = gen.buildWorkers();
//
//		for (RunningWorker worker : workers) {
//			zk.create(ZKRouter.NODE_WORKER + "/" + worker.getAppWorkerId(), worker.toByteArray(), Ids.OPEN_ACL_UNSAFE,
//					CreateMode.EPHEMERAL_SEQUENTIAL);
//		}
//		// zk.setData(path, data, version)
//
//		// ten lines for ten days
//		Stat s = new Stat();
//		zk.getData("/FAE/Applications/4", false, s);
//		System.out.println("got app");
//		Thread.sleep(20 * 1000);
//		System.out.println("updating..., version is " + s.getVersion());
//		Stat n = zk.setData("/FAE/Applications/4", app.toByteArray(), s.getVersion());
//		System.out.println("new version is " + n.getVersion());
//
//		System.in.read();
//	}
//}
