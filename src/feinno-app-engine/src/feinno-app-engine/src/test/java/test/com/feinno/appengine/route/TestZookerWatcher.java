package test.com.feinno.appengine.route;

import java.io.IOException;
import java.util.List;

import com.feinno.appengine.route.ZookeeperWatcher;
import com.feinno.appengine.runtime.configuration.ApplicationEntity;
import com.feinno.appengine.runtime.configuration.RunningWorkerEntity;
import com.feinno.util.Action;

public class TestZookerWatcher {
	public static void main(String[] args) throws IOException {
//		final ZKRouter r = new ZKRouter("127.0.0.1:2181", 2 * 1000);
		ZookeeperWatcher w = new ZookeeperWatcher("192.168.110.231:8998");
		w.setAppsUpdateCallback(new Action<List<ApplicationEntity>>(){
			@Override
			public void run(List<ApplicationEntity> apps) {
				for(ApplicationEntity app : apps) {
					System.out.println(app.getAppId());
				}
			}
		});
		
		w.setWorkersUpdaterCallback(new Action<List<RunningWorkerEntity>>(){
			@Override
			public void run(List<RunningWorkerEntity> workers) {
				for(RunningWorkerEntity worker : workers) {
					System.out.println(worker.getAppWorkerId());
				}
			}
		});
		w.init();
	}
}
