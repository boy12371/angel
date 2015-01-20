/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-1-26
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization.property;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.feinno.serialization.entity.JavaPropertyEntity;

import com.feinno.serialization.protobuf.ProtoBuilder;
import com.feinno.serialization.protobuf.ProtoManager;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther duyu
 */
public class TestJavaProperty {

//	private static final Logger LOGGER = LoggerFactory.getLogger(TestJavaProperty.class);
//
//	public static void main(String args[]) throws Exception {
//
//		TestJavaProperty test = new TestJavaProperty();
//		test.test();
//	}
//
//	@Test
//	public void test() throws IOException {
//
//		// 第一遍涉及到动态编译，因此不算
//		int count = 1000000;
//		new JavaPropertyEntity().toByteArray();
//
//		// Google的速度统计
//		long startTime = System.currentTimeMillis();
//		TestGoogleProtos.GoogleEntity.Builder e = TestGoogleProtos.GoogleEntity.newBuilder();
//		e.setId(1);
//		e.setName("name");
//		e.setPwd("pwd");
//		TestGoogleProtos.GoogleEntity google = e.build();
//		for (int i = 0; i < count; i++) {
//			google.toByteArray();
//		}
//		long endTime = System.currentTimeMillis() - startTime;
//		LOGGER.info("GooglePB used time:" + endTime);
//
//		// 我们自己的序列化速度统计
//		startTime = System.currentTimeMillis();
//		JavaPropertyEntity e2 = new JavaPropertyEntity();
//		e2.setId(1);
//		e2.setName("name");
//		e2.setPwd("pwd");
//		ProtoBuilder protoBuilder= ProtoManager.getProtoBuilder(e2);
//		for (int i = 0; i < count; i++) {
//			protoBuilder.toByteArray();
//		}
//		endTime = System.currentTimeMillis() - startTime;
//		LOGGER.info("FeinnoPB used time:" + endTime);
//
//		// JAVA 原生的序列化方式
//		startTime = System.currentTimeMillis();
//		JavaPropertyEntity e3 = new JavaPropertyEntity();
//		e3.setId(1);
//		e3.setName("name");
//		e3.setPwd("pwd");
//		for (int i = 0; i < count; i++) {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(baos);
//			oos.writeObject(e3);
//			baos.toByteArray();
//		}
//		endTime = System.currentTimeMillis() - startTime;
//		LOGGER.info("JAVA Serializable used time:" + endTime);
//	}

}
