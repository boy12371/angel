/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-9-6
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.threading.continuation;

import org.apache.commons.javaflow.Continuation;

import com.feinno.util.Func;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class MyRunnable2 implements Func<Integer, String>
{
	@Override
	public String exec(Integer a)
	{
		System.out.println("Runnable2.test:" + a);
		Continuation.suspend();
		return "test:" + a;
	}
}
