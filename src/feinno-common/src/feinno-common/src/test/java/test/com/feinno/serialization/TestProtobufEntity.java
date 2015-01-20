/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-26
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization;

import java.io.IOException;

import org.junit.Test;

import com.feinno.serialization.Serializer;

import test.com.feinno.serialization.entity.BaseArrayTest;
import test.com.feinno.serialization.entity.GooglePropertyEntity;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class TestProtobufEntity {
	@Test
	public void testEntity() {
		BaseArrayTest ba = new BaseArrayTest();
		Integer id = 12;
		ba.setId(id);

		try {
			byte[] by = Serializer.encode(ba);
			System.out.println(by);

			BaseArrayTest b = Serializer.decode(BaseArrayTest.class, by);
			System.out.println(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
