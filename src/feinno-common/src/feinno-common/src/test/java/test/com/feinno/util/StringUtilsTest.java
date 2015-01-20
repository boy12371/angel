/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-13
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.util;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.util.Outter;
import com.feinno.util.StringUtils;


/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class StringUtilsTest
{
	@Test
	public void testFormatBuffer()
	{
		byte[] buffer = new byte[] { 0, 12, (byte) 0xaf, 0x7f, 8, 9};
		String s = StringUtils.formatBuffer(buffer);
		Assert.assertEquals("<6: 00 0C AF 7F 08 09>", s);
		Assert.assertEquals("<NULL>", StringUtils.formatBuffer(null));
	}
	
	@Test
	public void testSpilt()
	{
		Outter<String> p = new Outter<String>();
		Outter<String> left = new Outter<String>();
		StringUtils.splitWithFirst("tcp://192.168.1.1", "://", p, left);
		Assert.assertEquals("tcp", p.value());
		Assert.assertEquals("192.168.1.1", left.value());
	}
}
