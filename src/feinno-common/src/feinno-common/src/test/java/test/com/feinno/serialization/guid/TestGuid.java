/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-4-19
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization.guid;

import java.io.IOException;

import org.junit.Test;

import com.feinno.serialization.Serializer;


/**
 * {在这里补充类的功能说明}
 * 
 * @auther duyu
 */
public class TestGuid {

	@Test
	public void testGuid(){
		String isOnline = "isOnline";
		if(isOnline.indexOf("is") == 0){
			isOnline = isOnline.substring(2, isOnline.length());
		}
		System.out.println(isOnline);
		
	}
}
