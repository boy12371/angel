/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-16
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.threading;

import java.util.Hashtable;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.feinno.diagnostic.perfmon.Stopwatch;
import com.feinno.threading.ExecutorFactory;
import com.feinno.threading.ThreadContext;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ThreadContextTest
{
	@Test
	public void test()
	{
		ThreadContext tc = ThreadContext.getCurrent();
		Thread t = Thread.currentThread();
		
	}
	
	
	public static void main(String[] args) throws InterruptedException
	{
		ExecutorFactory.newFixedExecutor("test", 10, 100);
		long n = 100*1000;
		Stopwatch watch;
		watch = new Stopwatch();
		long a = 0;
		for (int i = 0; i < n; i++) {
			a = Thread.currentThread().getId();
		}
		System.out.println("Thread.currentThread().getId(): " + a + " cost: "+ watch.getMillseconds());

		watch = new Stopwatch();
		for (int i = 0; i < n; i++) {
			ThreadLocal thred = new ThreadLocal();
			thred.set(i);
		}
		System.out.println("ThreadLocal.set(): " + watch.getMillseconds());
		
		watch = new Stopwatch();
		for (int i = 0; i < n; i++) {
			n += i;
		}
		System.out.println("+= i: " + watch.getMillseconds());
		
		
		watch = new Stopwatch();
		AtomicLong l2 = new AtomicLong();
		for (int i = 0; i < n; i++) {
			l2.incrementAndGet();
		}
		System.out.println(".incrementAndGet(): " + watch.getMillseconds());

		watch = new Stopwatch();
		Hashtable hash = new Hashtable<Integer, Integer>();
		for (int i = 0; i < n; i++) {
			if (hash.get(i % 200) != null) {
				hash.put(i % 200, i);
			}
		}
		System.out.println("hash.put(i % 10, i);" + watch.getMillseconds());
		
		
		for (int j = 0; j < 1000; j++) {
			Executor exec = ExecutorFactory.newFixedExecutor("test" + j % 10, 10, 100);
			for (int i = 0; i < 100; i++) {
				exec.execute(
						new Runnable() {
							@Override
							public void run()
							{
								System.out.print(" " + Thread.currentThread().getId());
							}
						}
				);
			}
			System.out.println("----------------");
			Thread.sleep(10);
		}
	}
}
