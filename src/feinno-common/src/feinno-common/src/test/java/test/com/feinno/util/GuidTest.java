/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-25
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.util;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.util.Guid;
/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class GuidTest
{
	@Test
	public void test()
	{	
		UUID i = UUID.randomUUID();
		String s = i.toString();
		System.out.println(i.toString());
		long a = i.getMostSignificantBits();
		long b = i.getLeastSignificantBits();
		//byte[] buffer = getBuffer(a, b);
		
		//String uuidStr = getString(buffer);
		//byte[] buffer = getBuffer();
		String s2 = String.format("%08x-%04x-%04x-%04x-%012x", a >>> 32, (a & 0x00000000ffff0000L) >>> 16, a & 0x0000ffff, 
				b >>> 48, b & 0x0000ffffffffffffL);
		
		System.out.println(s2);
		//UUID.
		UUID i2 = UUID.fromString(s2);
		//"6d435f13-5f23-487e-b0fa-d077ac4157cb");
		System.out.println(i2.toString());
		System.out.println(i.equals(i2));
		
		System.out.print(Guid.randomGuid().toStr());
		
		
	}
	

	private static final Logger LOGGER = LoggerFactory.getLogger(GuidTest.class);
}
