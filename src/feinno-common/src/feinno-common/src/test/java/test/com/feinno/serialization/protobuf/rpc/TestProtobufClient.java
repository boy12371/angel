///*
// * FAE, Feinno App Engine
// *  
// * Create by 李会军 2011-2-17
// * 
// * Copyright (c) 2011 北京新媒传信科技有限公司
// */
//package test.com.feinno.serialization.protobuf.rpc;
//
//import java.net.InetSocketAddress;
//import java.util.Arrays;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import test.com.feinno.serialization.protobuf.DataCreater;
//import test.com.feinno.serialization.protobuf.bean.FullElementsBean;
//import test.com.feinno.serialization.protobuf.bean.Person;
//
//import com.feinno.rpc.RpcProxy;
//import com.feinno.rpc.RpcProxyFactory;
//import com.feinno.rpc.RpcResults;
//import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
//import com.feinno.util.Action;
//
///**
// * TestMockClient
// * 
// * @author 李会军
// */
//public class TestProtobufClient {
//	private static final Logger logger = LoggerFactory.getLogger(TestProtobufClient.class);
//
//	@Before
//	public void testBefore()throws Exception{
//		TestProtobufServer.main(null);
//	}
//	
//	@Test
//	public void test() {
//		RpcTcpEndpoint tcpEndpoint = new RpcTcpEndpoint(new InetSocketAddress("127.0.0.1", 8002));
//		RpcProxy proxy = RpcProxyFactory.getProxy(tcpEndpoint, "ProtobufRpcService");
//		logger.debug("calling TestFullElements");
//		invokeTestFullElements(proxy);
//		logger.debug("calling TestNativeString");
//		invokeTestNativeString(proxy);
//		logger.debug("calling invokeTestNativeBoolean");
//		invokeTestNativeBoolean(proxy);
//		logger.debug("calling invokeTestNativeStringArray");
//		invokeTestNativeStringArray(proxy);
//		logger.debug("calling invokeTestNativeIntegerArray");
//		invokeTestNativeIntegerArray(proxy);
//		logger.debug("calling invokeTestNativeLongArray");
//		invokeTestNativeLongArray(proxy);
//		logger.debug("calling invokeTestNativeBigLongArray");
//		invokeTestNativeBigLongArray(proxy);
//		logger.debug("calling invokeTestFullElementsArray");
//		invokeTestFullElementsArray(proxy);
//
//	}
//
//	public static void invokeTestFullElements(RpcProxy proxy) {
//
//		proxy.invoke("TestFullElements", DataCreater.newFullElementsBean(true), new Action<RpcResults>() {
//			@Override
//			public void run(RpcResults a) {
//				if (a.error() != null) {
//					logger.debug("Test invokeTestFullElements callback with error.", a.error());
//				} else {
//					Person output = (Person) a.getValue(Person.class);
//					logger.debug("TestFullElements is " + output);
//				}
//			}
//		});
//	}
//
//	public static void invokeTestNativeString(RpcProxy proxy) {
//		proxy.invoke("TestNativeString", "Lv.Mingwei calling", new Action<RpcResults>() {
//			@Override
//			public void run(RpcResults a) {
//				if (a.error() != null) {
//					logger.debug("invokeTestNativeString callback with error.", a.error());
//				} else {
//					System.out.println(a.getValue(String.class));
//				}
//			}
//		});
//	}
//
//	public static void invokeTestNativeBoolean(RpcProxy proxy) {
//		proxy.invoke("TestNativeBoolean", true, new Action<RpcResults>() {
//			@Override
//			public void run(RpcResults a) {
//				if (a.error() != null) {
//					logger.debug("invokeTestNativeBoolean callback with error.", a.error());
//				} else {
//					System.out.println(a.getValue(boolean.class));
//				}
//			}
//		});
//	}
//
//	public static void invokeTestNativeStringArray(RpcProxy proxy) {
//		proxy.invoke("TestNativeStringArray", new String[] { "L", "V", "M", "I", "N", "G", "W", "E", "I" },
//				new Action<RpcResults>() {
//					@Override
//					public void run(RpcResults a) {
//						if (a.error() != null) {
//							logger.debug("invokeTestNativeStringArray callback with error.", a.error());
//						} else {
//							System.out.println(Arrays.toString((String[]) a.getValue(String[].class)));
//						}
//					}
//				});
//	}
//
//	public static void invokeTestNativeIntegerArray(RpcProxy proxy) {
//		proxy.invoke("TestNativeIntegerArray", new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, new Action<RpcResults>() {
//			@Override
//			public void run(RpcResults a) {
//				if (a.error() != null) {
//					logger.debug("invokeTestNativeIntegerArray callback with error.", a.error());
//				} else {
//					System.out.println(Arrays.toString((Integer[]) a.getValue(Integer[].class)));
//				}
//			}
//		});
//	}
//
//	public static void invokeTestNativeLongArray(RpcProxy proxy) {
//		proxy.invoke("TestNativeLongArray", new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L },
//				new Action<RpcResults>() {
//					@Override
//					public void run(RpcResults a) {
//						if (a.error() != null) {
//							logger.debug("invokeTestNativeLongArray callback with error.", a.error());
//						} else {
//							System.out.println(Arrays.toString((long[]) a.getValue(long[].class)));
//						}
//					}
//				});
//	}
//
//	public static void invokeTestNativeBigLongArray(RpcProxy proxy) {
//		proxy.invoke("TestNativeBigLongArray", new Long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L },
//				new Action<RpcResults>() {
//					@Override
//					public void run(RpcResults a) {
//						if (a.error() != null) {
//							logger.debug("TestNativeBigLongArray callback with error.", a.error());
//						} else {
//							System.out.println(Arrays.toString((Long[]) a.getValue(Long[].class)));
//						}
//					}
//				});
//	}
//	
//	public static void invokeTestFullElementsArray(RpcProxy proxy) {
//		proxy.invoke("TestFullElementsArray",
//				new FullElementsBean[] { DataCreater.newFullElementsBean(true), DataCreater.newFullElementsBean(true),
//						DataCreater.newFullElementsBean(true), DataCreater.newFullElementsBean(true) },
//				new Action<RpcResults>() {
//					@Override
//					public void run(RpcResults a) {
//						if (a.error() != null) {
//							logger.debug("invokeTestNativeLongArray callback with error.", a.error());
//						} else {
//							System.out.println(Arrays.toString((FullElementsBean[]) a
//									.getValue(FullElementsBean[].class)));
//						}
//					}
//				});
//	}
//}
