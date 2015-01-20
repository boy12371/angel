/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-9-6
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.threading.continuation;

import com.feinno.util.Func;

public class MyRunnable implements Runnable
{
	Func<Integer, String> func;
	Func<Integer, Integer> func3;
	
	public MyRunnable() throws Exception
	{
		Class<?> c = ContiuationTest.magicClassLoader.forceLoadClass("test.com.feinno.threading.continuation.MyRunnable2");
		func = (Func<Integer, String>)c.newInstance();
		
		// func = new MyRunnable2();
		Class<?> c2 = ContiuationTest.magicClassLoader.forceLoadClass("test.com.feinno.threading.continuation.MyRunnable3");
		func3 = (Func<Integer, Integer>)c2.newInstance();
		
		func3 = new MyRunnable3();
	}
	
	public void run()
	{
		System.out.println("started!");
		for (int i = 0; i < 20; i++) {
			System.out.printf("%d-%s\n", Thread.currentThread().getId(), Thread.currentThread().getName());
			String ret = func.exec(i);
			System.out.printf("expect:%d result:%d\n", 6 + i, func3.exec(i));
		}
	}
}
