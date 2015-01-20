/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Aug 5, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.google.code.juds;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import test.com.feinno.serialization.protobuf.DataCreater;
import test.com.feinno.serialization.protobuf.bean.Table;
import test.com.feinno.serialization.protobuf.bean.User;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoManager;
import com.feinno.serialization.protobuf.ProtoMember;
import com.feinno.threading.ExecutorFactory;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class TestAll {
	public static void main(String[] args) throws Exception {
		// String[] a = new String[1];
		// a[0] = "someting";
		// try {
		// TestUnixDomainSocketServer.main(a);
		// TestUnixDomainSocket.main(a);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// @SuppressWarnings("deprecation")
		// java.util.Date utilDate = new java.util.Date(88, 4, 27);
		// byte[] buffer = Serializer.encode(utilDate);
		// System.out.println("原始日期: " + utilDate);
		// utilDate = Serializer.decode(java.util.Date.class, buffer);
		// System.out.println("序列日期: " + utilDate);
		// System.out.println("--------------------------");
		// java.sql.Date sqlDate = new java.sql.Date(88, 4, 27);
		// buffer = Serializer.encode(sqlDate);
		// System.out.println("原始日期: " + sqlDate);
		// sqlDate = Serializer.decode(java.sql.Date.class, buffer);
		// System.out.println("序列日期: " + sqlDate);
		//
		// // buffer = Serializer.encode(new java.util.Date[]{
		// // utilDate,utilDate,utilDate,utilDate });
		// // java.util.Date[] utilDateArray =
		// // Serializer.decode(java.util.Date[].class, buffer);
		// //
		// System.out.println("序列日期: "+utilDateArray[0]+"  "+utilDateArray[1]+"  "+utilDateArray[2]);
		//
		// buffer = Serializer.encode(new java.sql.Date[] { sqlDate, sqlDate,
		// sqlDate, sqlDate });
		// java.sql.Date[] sqlDateArray =
		// Serializer.decode(java.sql.Date[].class, buffer);
		// System.out.println("序列日期: " + sqlDateArray[0] + "  " +
		// sqlDateArray[1] + "  " + sqlDateArray[2]);
		//
		// Date javaDate = new Date(86, 4, 27);
		// System.out.println(TimeZone.getDefault().getOffset(javaDate.getTime()));
		// javaDate = new Date(86, 4, 3);
		// javaDate.setHours(16);
		// System.out.println(TimeZone.getDefault().getOffset(javaDate.getTime()));
		// javaDate = new Date(86, 4, 4);
		// javaDate.setHours(16);
		// System.out.println(TimeZone.getDefault().getOffset(javaDate.getTime()));

		// ByteArrayInputStream byteArrayInputStream = new
		// ByteArrayInputStream(buf);
		// Integer i = -1;
		// writeRawVarint32(-3);

		// byte[] buffer = Serializer.encode(-1);
		// System.out.println(toHexString(buffer));
		//
		// buffer = Serializer.encode(new IntObj(-1));
		// System.out.println(toHexString(buffer));
		// System.out.println(Serializer.decode(IntObj.class,
		// buffer).getValue());
		// System.out.println(toHexString(buffer));

		// buffer = [0x08];
		// System.out.println(Integer.MAX_VALUE);
		// System.out.println(Long.MAX_VALUE);

		// Class javaType = ProtoEntity.class;
		// Class test2 = Test2.class;
		// test2.getDeclaredFields();
		// System.out.println(javaType.isAssignableFrom(test2));
		//
		// Test1 test1 = new Test1();
		// test1.setTest1Name("test1");
		//
		// Test2 test2 = new Test2();
		// test2.setTest1Name("test1");
		// test2.setTest2Name("test2");
		//
		// Test3 test3 = new Test3();
		// test3.setTest1Name("test1");
		// test3.setTest2Name("test2");
		// test3.setTest3Name("test3");
		// byte[] buffer = Serializer.encode(test1);
		// Test1 t1 = Serializer.decode(Test1.class, buffer);
		// System.out.println(Arrays.toString(buffer));
		// System.out.println(t1.getTest1Name());
		// System.out.println(t1.getUnknownFields());
		// System.out.println("--------------------------------------");
		// buffer = Serializer.encode(test2);
		// System.out.println(Arrays.toString(buffer));
		// Test2 t2 = Serializer.decode(Test2.class, buffer);
		// System.out.println(t2.getTest1Name());
		// System.out.println(t2.getTest2Name());
		// System.out.println(t2.getUnknownFields());
		// System.out.println("--------------------------------------");
		// buffer = Serializer.encode(test3);
		// System.out.println(Arrays.toString(buffer));
		// Test3 t3 = Serializer.decode(Test3.class, buffer);
		// System.out.println(t3.getTest1Name());
		// System.out.println(t3.getTest2Name());
		// System.out.println(t3.getTest3Name());
		// System.out.println(t3.getUnknownFields());
		// System.out.println("--------------------------------------");

		// Table table = DataCreater.newTable(true);
		// table.toByteArray();
		//
		// int count = 1000000;
		// long startTime = System.nanoTime();
		// for (int i = 0; i < count; i++) {
		// table = new Table();
		// table.setId(1);
		// table.setName("我是Table");
		// table.setEmail("Table@163.com");
		// User user = new User();
		// user.setId(2);
		// user.setName("我是User");
		// user.setEmail("User@163.com");
		// table.setUser(user);
		//
		// table.toByteArray();
		// }
		// long endTime = System.nanoTime();
		// System.out.println("序列化耗时：" + TimeUnit.MILLISECONDS.convert(endTime -
		// startTime, TimeUnit.NANOSECONDS) + "毫秒");
		//
		// table = new Table();
		// table.setId(1);
		// table.setName("我是Table");
		// table.setEmail("Table@163.com");
		// User user = new User();
		// user.setId(2);
		// user.setName("我是User");
		// user.setEmail("User@163.com");
		// table.setUser(user);
		//
		// byte[] buffer = table.toByteArray();
		//
		// startTime = System.nanoTime();
		// for (int i = 0; i < count; i++) {
		// ProtoManager.parseFrom(new Table(), buffer);
		// }
		// endTime = System.nanoTime();
		// System.out.println("反序列化耗时：" + TimeUnit.MILLISECONDS.convert(endTime
		// - startTime, TimeUnit.NANOSECONDS) + "毫秒");
		// System.out.println(ProtoConfig.PROTO_BUILDER_TEMPLATE);

		// ByteArrayInputStream input = new ByteArrayInputStream(new
		// byte[]{1,1,1});
		// byte[] buffer = new byte[3];
		// System.out.println(input.read(buffer, 0, 3));
		// System.out.println(input.read(buffer));

		final Map<Thread, Integer> futureIndexThreadMap = Collections.synchronizedMap(new HashMap<Thread, Integer>());
		Executor executor = ExecutorFactory.newFixedExecutor("Test", 1, 10);
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.currentThread().setName("C");
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				futureIndexThreadMap.put(Thread.currentThread(), 0);
				System.out.println(futureIndexThreadMap);
			}
		});
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.currentThread().setName("A");
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				futureIndexThreadMap.put(Thread.currentThread(), 0);
				System.out.println(futureIndexThreadMap);
			}
		});
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.currentThread().setName("B");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				futureIndexThreadMap.put(Thread.currentThread(), 0);
				System.out.println(futureIndexThreadMap);
			}
		});
		
	}

	public static String toHexString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(toHexString(b[i]).toUpperCase());
			sb.append(" ");
		}
		return sb.toString();
	}

	public static String toHexString(byte b) {
		String hex = Integer.toHexString(b & 0xFF).toUpperCase();
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return hex;
	}

	public static void writeRawVarint32(int value) throws IOException {
		while (true) {
			if ((value & ~0x7F) == 0) {
				// writeRawByte(value);
				System.out.println(value);
				return;
			} else {
				System.out.println((value & 0x7F) | 0x80);
				value >>>= 7;
			}
		}
	}

	public static class Test1 extends ProtoEntity {
		@ProtoMember(1)
		private String test1Name;

		public String getTest1Name() {
			return test1Name;
		}

		public void setTest1Name(String test1Name) {
			this.test1Name = test1Name;
		}
	}

	public static class Test2 extends Test1 {
		@ProtoMember(2)
		private String test2Name;

		public final String getTest2Name() {
			return test2Name;
		}

		public final void setTest2Name(String test2Name) {
			this.test2Name = test2Name;
		}
	}

	public static class Test3 extends Test2 {
		@ProtoMember(3)
		private String test3Name;

		public String getTest3Name() {
			return test3Name;
		}

		public void setTest3Name(String test3Name) {
			this.test3Name = test3Name;
		}
	}
}
