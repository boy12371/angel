package test.com.feinno.rpc.duplex;
///*
// * FAE, Feinno App Engine
// *  
// * Create by 李会军 2011-1-20
// * 
// * Copyright (c) 2011 北京新媒传信科技有限公司
// */
//package test.com.feinno.rpc.duplex;
//
//import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
//import com.feinno.rpc.duplex.RpcDuplexServer;
//import com.feinno.rpc.duplex.RpcDuplexService;
//
//
///**
// * TestMain
// * 
// * @author gaolei
// */
//public class TestServer
//{
//	public static void main(String[] args) throws Exception
//	{
//		RpcDuplexContext agent = RpcDuplexContext.getCurrent();
//		agent.getConnection().getConnection();
//		
//		RpcTcpServerChannel channel = new RpcTcpServerChannel(8077);
//		RpcDuplexServer server = new RpcDuplexServer(channel);
//		
//		RpcDuplexService service = new RpcDuplexService(new )
//		server.registerService();
//		
//		server.registerDuplexService(HAServerAgentService());
//	}
//}
