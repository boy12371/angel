package test.com.feinno.serialization.protobuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.feinno.rpc.sample.SampleService;
import test.com.feinno.serialization.protobuf.bean.Person;
import test.com.feinno.serialization.protobuf.bean.Table;
import test.com.feinno.serialization.protobuf.bean.Unsupported;

import com.feinno.serialization.protobuf.ProtoManager;

public class TestProtoManager {

	private static final Logger logger = LoggerFactory.getLogger(TestProtoManager.class);

	@Test
	public void testToByteArray() {
		try {
			// 这个支持
			byte[] buffer = ProtoManager.toByteArray(123);
			logger.info("{}", ProtoManager.parseFrom(0, buffer));
			ProtoManager.toByteArray(new java.util.Date(System.currentTimeMillis()));
			// 这个不支持
			ProtoManager.toByteArray(new java.sql.Date(System.currentTimeMillis()));
		} catch (IOException e) {
			logger.error("Found error {}", e);
			Assert.assertEquals(1, 2);
		} catch (RuntimeException e) {
			logger.info("Test unsported type {}", e);
		}
	}

	@Test
	public void testWriteTo() {
		try {
			// 这个支持
			ProtoManager.writeTo(new java.util.Date(System.currentTimeMillis()), new ByteArrayOutputStream());
			// 这个不支持
			ProtoManager.writeTo(new java.sql.Date(System.currentTimeMillis()), new ByteArrayOutputStream());
		} catch (IOException e) {
			logger.error("Found error {}", e);
			Assert.assertEquals(1, 2);
		} catch (RuntimeException e) {
			logger.info("Test unsported type {}", e);
		}
	}

	@Test
	public void testListWriteTo() throws Exception {
		try {
			List<Table> list = new ArrayList<Table>();
			list.add(DataCreater.newTable(true));
			list.add(DataCreater.newTable(true));
			list.add(DataCreater.newTable(true));
			ProtoManager.writeTo(new ByteArrayOutputStream(), list, Table.class);

			List<java.sql.Date> sqlDateList = new ArrayList<java.sql.Date>();
			sqlDateList.add(new java.sql.Date(System.currentTimeMillis()));
			sqlDateList.add(new java.sql.Date(System.currentTimeMillis()));
			sqlDateList.add(new java.sql.Date(System.currentTimeMillis()));
			ProtoManager.writeTo(new ByteArrayOutputStream(), sqlDateList, java.sql.Date.class);

			byte[] buffer = ProtoManager.toByteArray(sqlDateList, java.sql.Date.class);
			List<java.sql.Date> sqlDateResultList = ProtoManager.parseFrom(buffer, new ArrayList<java.sql.Date>(),
					java.sql.Date.class);
			Assert.assertEquals(sqlDateList, sqlDateResultList);
			logger.info("sql date is : {}", sqlDateResultList);

			List<java.util.Date> utilDateList = new ArrayList<java.util.Date>();
			utilDateList.add(new java.util.Date(System.currentTimeMillis()));
			utilDateList.add(new java.util.Date(System.currentTimeMillis()));
			utilDateList.add(new java.util.Date(System.currentTimeMillis()));
			ProtoManager.writeTo(new ByteArrayOutputStream(), utilDateList, java.util.Date.class);
			buffer = ProtoManager.toByteArray(utilDateList, java.util.Date.class);
			logger.info("util date is : {}",
					ProtoManager.parseFrom(buffer, new ArrayList<java.util.Date>(), java.util.Date.class));

		} catch (IOException e) {
			logger.error("Found error {}", e);
			Assert.assertEquals(1, 2);
		} catch (RuntimeException e) {
			logger.info("Test unsported type {}", e);
		}
	}

	@Test
	public void testMapWriteTo() {
		try {
			Map<String, Person> map = new HashMap<String, Person>();
			map.put("A", DataCreater.newPerson(true));
			map.put("B", DataCreater.newPerson(true));
			map.put("C", DataCreater.newPerson(true));
			ProtoManager.writeTo(new ByteArrayOutputStream(), map, String.class, Person.class);
		} catch (IOException e) {
			logger.error("Found error {}", e);
			Assert.assertEquals(1, 2);
		} catch (RuntimeException e) {
			logger.info("Test unsported type {}", e);
		}
	}

	@Test
	public void testParseFrom() {
		try {
			// 这个支持
			byte[] buffer = ProtoManager.toByteArray(new java.util.Date(System.currentTimeMillis()));
			// 这个不支持
			ProtoManager.parseFrom(new java.sql.Date(System.currentTimeMillis()), buffer);
		} catch (IOException e) {
			logger.error("Found error {}", e);
			Assert.assertEquals(1, 2);
		} catch (RuntimeException e) {
			logger.info("Test unsported type {}", e);
		}
	}

	@Test
	public void testParseFormByStream() {

		try {
			// 这个支持
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ProtoManager.writeTo(new java.util.Date(System.currentTimeMillis()), output);
			// 这个支持
			ProtoManager.parseFrom(new java.util.Date(System.currentTimeMillis()),
					new ByteArrayInputStream(output.toByteArray()));
			// 这个不支持
			ProtoManager.parseFrom(new java.sql.Date(System.currentTimeMillis()),
					new ByteArrayInputStream(output.toByteArray()));

		} catch (IOException e) {
			logger.error("Found error {}", e);
			Assert.assertEquals(1, 2);
		} catch (RuntimeException e) {
			logger.info("Test unsported type {}", e);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testRequired() throws Exception {
		byte[] buffer = ProtoManager.toByteArray(DataCreater.newRequiredProtoEntity(true));
		ProtoManager.parseFrom(DataCreater.newRequiredProtoEntity(false), buffer);
	}

	@Test
	public void testRequiredError() throws Exception {
		try {
			byte[] buffer = ProtoManager.toByteArray(DataCreater.newRequiredProtoEntity(false));
			ProtoManager.parseFrom(DataCreater.newRequiredProtoEntity(false), buffer);
		} catch (Exception e) {
			org.junit.Assert.assertTrue(true);
			return;
		}
		org.junit.Assert.assertTrue(false);
	}

	@Test
	public void testError() {
		boolean isTrue = false;
		try {
			// 这就是个错误，目前不支持Set类型的序列化
			Set<String> set = new HashSet<String>();
			byte[] buffer = ProtoManager.toByteArray(set);
			ProtoManager.parseFrom(new HashSet(), buffer);
		} catch (Exception e) {
			isTrue = true;
		}
		org.junit.Assert.assertTrue(isTrue);

		try {
			// 已经可支持Set类型的序列化
			byte[] buffer = ProtoManager.toByteArray(DataCreater.newUnsupported(true));
			ProtoManager.parseFrom(new Unsupported(), buffer);
			org.junit.Assert.assertTrue(buffer.length > 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		isTrue = false;
		try {
			// 这就是个错误，目前不支持
			List<TestProtoManager> list = new ArrayList<TestProtoManager>();
			list.add(this);
			list.add(this);
			list.add(this);
			byte[] buffer = ProtoManager.toByteArray(list, TestProtoManager.class);
			ProtoManager.parseFrom(buffer, new ArrayList<TestProtoManager>(), TestProtoManager.class);
			org.junit.Assert.assertTrue(buffer.length == 0);
		} catch (Exception e) {
			e.printStackTrace();
			org.junit.Assert.assertTrue(false);
		}
	}

	@Test
	public void testInnerClass() throws Exception {
		SampleService.AddArgs bean = new SampleService.AddArgs();
		bean.setA(1);
		bean.setB(2);
		ProtoManager.parseFrom(new SampleService.AddArgs(), ProtoManager.toByteArray(bean));
	}
}
