/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-14
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.threading;


import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.feinno.threading.ExecutorBusyException;
import com.feinno.threading.ExecutorFactory;
import com.feinno.util.SyncInvoker;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ExecutorFactoryTest
{
	@Before
	public void setUp() throws Exception
	{	
		ExecutorFactory.newFixedExecutor("test", 10, 50);
	}
	
	@Test
	public void testBasic()
	{
		final SyncInvoker invoker = new SyncInvoker();
		
		ExecutorFactory.getExecutor("test").execute(
				new Runnable() {
					@Override
					public void run()
					{
						invoker.getCallback().run("gotcha");
					}
				}
		);
		Assert.assertEquals(true, invoker.waitFor(100));
		Assert.assertEquals("gotcha", invoker.getResult());
	}
	

	/**
	 * 
	 * 在并发10,上限50的线程池中加入100个任务
	 * 
	 * excepted: 头50个任务能够成功完成，后50个任务catch到ExcutorBusyException
	 * 
	 */
	@Test
	public void testLimit()
	{
		final int sleepMs = 10;
		final SyncInvoker invoker = new SyncInvoker();
		final AtomicInteger marker = new AtomicInteger();
		
		for (int i = 0; i < 100; i++) {
			Exception error = null;
			try {
				ExecutorFactory.getExecutor("test").execute(
						new Runnable() {
							@Override
							public void run()
							{
								try {
									Thread.sleep(sleepMs);
									int m = marker.incrementAndGet();
									System.out.println("task run m=" + m);
									if (m == 50) {
										invoker.getCallback().run(m);
									}									
								} catch (InterruptedException e) {
									e.printStackTrace();
									throw new RuntimeException("never happen");
								}
							}
						}
				);
			} catch (Exception ex) {
				error = ex;
			}
			if (error == null) {
				Assert.assertTrue("i = " + i, i < 50);
			} else {
				Assert.assertTrue("i = " + i, i >= 50);
				Assert.assertTrue(error instanceof ExecutorBusyException);
				String msg = "Executor["+ "test" + "] concurrent=" + 50 + ":" + "Fixed";
				Assert.assertEquals(msg, error.getMessage());
			}
		}
		
		//
		// 在本用例里因为线程池固定为10个，每个任务Sleep，T毫秒，500个任务完全执行完成需要至少5*T毫秒
		// 额外加上500ms是降低机器太弱的情况下的失败几率
		Assert.assertEquals(true, invoker.waitFor(sleepMs * 50 + 2000));
		Assert.assertEquals(50, invoker.getResult());
	}

	/**
	 * 
	 * 在TestLimit基础上测试两遍，
	 * 
	 * excepted: 软件能从第一次的极限数据中恢复
	 */
	@Test
	public void testLimit2()
	{
		testLimit();
		testLimit();
	}
	
	/**
	 * 
	 * 测试性能计数器在testLimit()用例下是否正常
	 * 
	 * excepted: 
	 * 
	 * 
	 */
	@Test
	public void testPerfmon()
	{
		//TODO
	}
}
