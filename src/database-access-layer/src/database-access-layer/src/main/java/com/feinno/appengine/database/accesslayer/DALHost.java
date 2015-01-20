package com.feinno.appengine.database.accesslayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.rpc.RemoteAppHost;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.ha.ServiceComponent;
import com.feinno.ha.ServiceSettings;
import com.feinno.imps.configuration.ConfigHelper;
import com.feinno.imps.configuration.helper.PoolHelper;
import com.feinno.initialization.InitialUtil;
import com.feinno.initialization.Initializer;
import com.feinno.rpc.channel.tcp.RpcTcpServerChannel;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.rpc.server.RpcServiceBootstrap;
//import com.feinno.util.ThreadPool;

public class DALHost implements ServiceComponent{

	public static final String SERVICE_NAME = RemoteAppHost.SERVICE_NAME;
	public static final Logger logger = LoggerFactory.getLogger(DALHost.class);

	private int port;
	private RpcTcpServerChannel channel;
	
	
	public DALHost() throws Exception
	{
		this.init();
	}
	
	@Initializer
	private void init() throws Exception
	{
		AppEngineManager.INSTANCE.initialize();
		InitialUtil.init(PoolHelper.class);		
		//2012-8-13 rpc_tcp 在1.5版本改为rpc
//		if (RpcServiceBootstrap.getServerChannel("tcp", null) == null) {
//			port = ServiceSettings.INSTANCE.getServicePort("rpc");
//			channel = new RpcTcpServerChannel(port);
//			RpcServiceBootstrap.registerChannel(channel);
//		}
		ConfigHelper.Initiliaze(DALConfiguration.EXECUTER_SIZE, DALConfiguration.EXECUTER_LIMIT);
		RpcServiceBootstrap.registerService(new DatabaseRpcServer());
		//TODO 需要容量环境的验证，配置方式
		int processors = Runtime.getRuntime().availableProcessors()*50;
		logger.warn("DAL Thread process max:"+processors);
	//	ThreadPool.init(processors);
	}
	
	/**
	 * start
	 */
	public void start() throws Exception
	{
		logger.error("DAL start():{},version:{}",getServiceUrl(),"2014-01-04");
		RpcServiceBootstrap.start();
	}

	
	public void start(RpcDuplexClient rpcDuplexClient) throws Exception {
		start();
		
	}
	public void stop() throws Exception {
		logger.info("DAL stop():{}",getServiceUrl());
		RpcServiceBootstrap.stop();
		
	}
	
	public String getServiceUrl()
	{
		return String.format("rpc=tcp://%s:%d", ServiceSettings.INSTANCE.getServerAddress(), port);
	}
}
