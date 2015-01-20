/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-6-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.util;

import org.junit.Test;

import com.feinno.util.container.SortedDictionary;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class SortedDictionaryTest
{
	@Test
	public void test()
	{
		SortedDictionary<Integer, String> a = new SortedDictionary<Integer, String>();
		a.add(10, 19, "10-19");
		a.add(5, 9, "5-9");
		a.add(0, 4, "0-4");
		a.add(20, 29, "20-29");
		
		System.out.println("search:" + a.search(11));
		System.out.println("search:" + a.search(4));
		System.out.println("search:" + a.search(29));
	}
}
