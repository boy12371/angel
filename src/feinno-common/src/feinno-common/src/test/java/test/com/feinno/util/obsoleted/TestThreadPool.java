//package test.com.feinno.util.obsoleted;
//
//import java.util.ArrayList;
//
//import org.junit.Test;
//
//import com.feinno.util.obsoleted.ThreadPool;
//
//public class TestThreadPool {
//
//	@Test
//	public void test(){
//		ThreadPool.init();
//		ThreadPool.init(2);
//		ThreadPool.isStaticPoolShutDown();
//		
//		ArrayList<Runnable> runnableList = new ArrayList<Runnable>();
//		Runnable runnable = new Runnable() {
//			@Override
//			public void run() {
//			}
//		};
//		runnableList.add(runnable);
//		runnableList.add(runnable);
//		ThreadPool.submit(runnableList);
//		ThreadPool.submit(runnable);
//		ThreadPool.shutdownStaticPool();
//		ThreadPool threadPool = new ThreadPool();
//		threadPool.enqueue(runnable);
//		threadPool.enqueue(runnableList);
//		threadPool.getIdleThreadCount();
//		threadPool.getQueueCount();
//		threadPool.getThreadPoolExecutor();
//		threadPool.shutdown();
//		runnableList.clear();
//	}
//	
//}
