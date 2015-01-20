/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-21
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.util;

import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.util.Func;
import com.feinno.util.container.SessionPool;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class SessionPoolTest
{
	private static final int TEST_SIZE = 1000;
	@Test
	public void testSimple()
	{
		SessionPool<String> sessions = new SessionPool(TEST_SIZE);
		for (int i = 0; i < TEST_SIZE + 100; i++) {
			int n = sessions.add("sample" + i);
			if (n < 0) {
				Assert.assertEquals("insert fail n < 0, i=" + i, true, i >= TEST_SIZE);
			} else {
				Assert.assertEquals("insert succ n > 0, i=" + i, true, i < TEST_SIZE);
				String msg = sessions.get(n);
				Assert.assertEquals("session=" + i, "sample" + i, msg);
			}
			Assert.assertEquals(i + 1 > TEST_SIZE ? TEST_SIZE: i + 1, sessions.concurrent());			
		}
		int n = TEST_SIZE;

		for (Entry<Integer, String> a: sessions.getAllItems(null)) {
			Assert.assertEquals(sessions.concurrent(), n);
			String msg = sessions.remove(a.getKey());
			Assert.assertEquals("session=" + a.getKey(), a.getValue(), msg);
			n--;
		}
	}
	
	@Test
	public void testSimple10K()
	{
		for (int i = 0; i < 3 * 1024; i++) {
			testSimple();
		}
	}
	
	@Test
	public void testConcurrent() throws InterruptedException
	{
		final SessionPool<String> sessions = new SessionPool(TEST_SIZE);
		Executor executor = Executors.newFixedThreadPool(8);
		final AtomicInteger aint = new AtomicInteger();
		for (int i = 0; i < TEST_SIZE * 100; i++) {
			final String msg = "foo" + i;
			executor.execute(new Runnable() {
				@Override
				public void run()
				{
					int n = sessions.add(msg);
					Assert.assertEquals("session:" + msg, true, n > 0);
					String msg2 = sessions.remove(n);
					Assert.assertEquals("session:" + msg, msg, msg2);
					aint.incrementAndGet();
				}
			});
		}
		Thread.sleep(1000);
		Assert.assertEquals(TEST_SIZE * 100, aint.get());
	}
	
	@Test
	public void testCoverage()
	{
		Exception ex = null;
		try {
			new SessionPool<String>();
			new SessionPool<String>(1024 * 1024);
		} catch (IllegalArgumentException e) {
			ex = e;
		}
		Assert.assertTrue("Exception", ex != null);
		Assert.assertTrue("Exception" + ex.getMessage(), ex instanceof IllegalArgumentException);
	}
	
	@Test
	public void testFilter()
	{
		SessionPool<String> sessions = new SessionPool(TEST_SIZE);
		for (int i = 0; i < TEST_SIZE + 100; i++) {
			int n = sessions.add("sample" + i);
			if (n < 0) {
				Assert.assertEquals("insert fail n < 0, i=" + i, true, i >= TEST_SIZE);
			} else {
				Assert.assertEquals("insert succ n > 0, i=" + i, true, i < TEST_SIZE);
				String msg = sessions.get(n);
				Assert.assertEquals("session=" + i, "sample" + i, msg);
			}
			Assert.assertEquals(i + 1 > TEST_SIZE ? TEST_SIZE: i + 1, sessions.concurrent());			
		}
		int n = TEST_SIZE;
		Func<String, Boolean> filter = new Func<String, Boolean>() {
			@Override
			public Boolean exec(String obj)
			{
				return obj.endsWith("0");
			}
		};
		for (Entry<Integer, String> a: sessions.getAllItems(filter)) {
			Assert.assertEquals(sessions.concurrent(), n);
			String msg = sessions.remove(a.getKey());
			Assert.assertEquals("session=" + a.getKey(), a.getValue(), msg);
			n--;
		}
		Assert.assertEquals(sessions.concurrent(), TEST_SIZE - 100);
	}
}
