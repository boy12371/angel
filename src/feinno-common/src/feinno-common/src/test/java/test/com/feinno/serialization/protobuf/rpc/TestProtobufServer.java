///*
// * FAE, Feinno App Engine
// *  
// * Create by 李会军 2011-1-20
// * 
// * Copyright (c) 2011 北京新媒传信科技有限公司
// */
//package test.com.feinno.serialization.protobuf.rpc;
//
//import com.feinno.rpc.RpcServiceBootstrap;
//import com.feinno.rpc.channel.RpcChannelSettings;
//import com.feinno.rpc.channel.RpcChannelSupportFlags;
//import com.feinno.rpc.channel.RpcConnectionMode;
//import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
//import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
//import com.feinno.util.Flags;
//
///**
// * TestMain
// * 
// * @author 李会军
// */
//public class TestProtobufServer {
//
//	public static void main(String[] args) throws Exception {
//		// 注册服务端通道
////		RpcServiceBootstrap.registerChannel(new RpcTcpServerChannel(8001));
//		
//		RpcChannelSettings setting = new RpcChannelSettings(Flags.of(RpcChannelSupportFlags.SUPPORT_CONNECTION), 
//				5 * 1024 * 1024, 15* 1000); 
//		RpcServiceBootstrap.registerChannel(new RpcTcpServerChannel(RpcTcpEndpoint.parse("tcp://0.0.0.0:8002"), setting, RpcConnectionMode.SIMPLEX));
//		RpcServiceBootstrap.registerService(new ProtobufService());
//		
//		System.out.println("Test server started. [V39]");
//		RpcServiceBootstrap.start();
//	}
//}
