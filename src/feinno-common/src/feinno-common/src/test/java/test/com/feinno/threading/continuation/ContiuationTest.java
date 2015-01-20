/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-9-6
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.threading.continuation;

import java.io.File;
import java.net.URL;
import java.util.concurrent.Executor;

import org.apache.commons.javaflow.Continuation;
import org.apache.commons.javaflow.ContinuationClassLoader;

import com.feinno.threading.ExecutorFactory;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ContiuationTest
{
	private static Continuation cont;
	public static ContinuationClassLoader magicClassLoader;
	
	static {
		URL[] locations = null;
		
		try{
			String cp = System.getProperty("java.class.path");
			String[] ps = cp.split(File.pathSeparator);
			locations = new URL[ps.length];
			for(int i = 0; i < ps.length; i++){
				try{
					locations[i] = new File(ps[i]).toURI().toURL();
				}catch(Exception e){
					// just ignore.
				}
			}
		}catch(Exception ex){
			// just ignore.
		}
		if(locations == null){
			try {
				locations = new URL[]{Thread.currentThread().getContextClassLoader().getResource("").toURI().toURL()};
			} catch (Exception e) {
				// never happens.
			}
		}
		magicClassLoader = new ContinuationClassLoader(locations, Thread.currentThread().getContextClassLoader());
//		magicClassLoader.addLoaderPackageRoot("com.feinno.rpc.client");
//		magicClassLoader.loadClass(name);
//		magicClassLoader.forceLoadClass(classname);
//		magicClassLoader.addLoaderPackageRoot("com.feinno.http.OauthClient");
//		magicClassLoader.addLoaderPackageRoot("com.feinno.app.fetch.service");
//		magicClassLoader.addLoaderPackageRoot("com.feinno.app.sync.utils");
	}
	
//	private String rpc(String a)
//	{
//		RpcMethodStub stub;
//		Future<RpcResults> r = stub.invoke(a);
//		Continuation c = null;
//		r.addListener(new EventHandler<RpcResults>() {
//			@Override
//			public void run(Object sender, RpcResults e)
//			{
//				if (c != null) {
//					c = Continuation.continueWith(a);
//				}
//			}
//		});
//		c = Continuation.suspend();
//	}
	
	public static void main(String[] args) throws Exception
	{
		Class<?> r = magicClassLoader.forceLoadClass("test.com.feinno.threading.continuation.MyRunnable");
		Runnable run = (Runnable)r.newInstance();
		System.out.println(run.toString());
				
		cont = Continuation.startWith(run);
		Executor executor = ExecutorFactory.newFixedExecutor("test", 10, 1000);
		
		System.out.println("returned a continuation");
		for (int i = 0; i < 10; i++) {
			test2(executor, i);
		}
		
		System.out.println("----------------- main  --------------");
		Thread.sleep(10000);
	}

	private static void test2(Executor executor, final int i)
	{
		executor.execute(new Runnable() {
			@Override
			public void run()
			{
				try {
					Thread.sleep(i * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (cont != null)
					cont = Continuation.continueWith(cont);	
			}
		});
	}
}
