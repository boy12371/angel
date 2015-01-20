/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-9-6
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.threading.continuation;

import com.feinno.util.Func;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class MyRunnable3 implements Func<Integer, Integer>
{
	@Override
	public Integer exec(Integer obj)
	{
		String hello = "system" + obj;
		System.out.println("MyRunnable3:" + obj);
		return hello.length() + obj;
	}
}
