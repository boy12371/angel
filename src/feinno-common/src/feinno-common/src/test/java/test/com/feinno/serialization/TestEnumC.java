/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-4-6
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.feinno.serialization.entity.ConfigTypeEnum;
import test.com.feinno.serialization.entity.TestEntityC;

import com.feinno.serialization.Serializer;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther duyu
 */
public class TestEnumC {

	private static final Logger logger = LoggerFactory.getLogger(TestEnumC.class);

	@Test
	public void testC() {
		TestEntityC t = new TestEntityC();
		t.setPath("a");
		t.setType(ConfigTypeEnum.TEXT);

		List<Integer> i1 = new ArrayList<Integer>();
		List<Integer> i2 = new ArrayList<Integer>();
		Integer[] i3 = new Integer[10];
		Integer[] i4 = new Integer[10];
		for (int i = 1; i < 11; i++) {
			i1.add(i);
			i2.add(100 + i);
		}

		for (int i = 0; i < 10; i++) {
			i3[i] = i + 1;
			i4[i] = 101 + i;
		}
		t.setI1(i1);
		t.setI2(i2);
		t.setI3(i3);
		t.setI4(i4);
		byte[] by;
		try {
			by = Serializer.encode(t);
			System.out.println("Entity = " + by);

			TestEntityC ee = Serializer.decode(TestEntityC.class, by);
			System.out.println(ee.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRequired() {
		TestEntityC t = new TestEntityC();
		t.setPath(null);
		t.setType(ConfigTypeEnum.TEXT);

		List<Integer> i1 = new ArrayList<Integer>();
		List<Integer> i2 = new ArrayList<Integer>();
		Integer[] i3 = new Integer[10];
		Integer[] i4 = new Integer[10];
		for (int i = 1; i < 11; i++) {
			i1.add(i);
			i2.add(100 + i);
		}

		for (int i = 0; i < 10; i++) {
			i3[i] = i + 1;
			i4[i] = 101 + i;
		}
		t.setI1(i1);
		t.setI2(i2);
		t.setI3(i3);
		t.setI4(i4);
		byte[] by;
		try {
			by = Serializer.encode(t);
			System.out.println("Entity = " + by);

			TestEntityC ee = Serializer.decode(TestEntityC.class, by);
			System.out.println(ee.getType());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (RuntimeException e) {
			logger.info("正常出错现象，必输字段不能为空", e);
		}
	}
}
