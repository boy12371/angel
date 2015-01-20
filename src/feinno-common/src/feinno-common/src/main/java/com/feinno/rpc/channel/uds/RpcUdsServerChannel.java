/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-7-30
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.uds;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etsy.net.JUDS;
import com.etsy.net.UnixDomainSocket;
import com.etsy.net.UnixDomainSocketServer;
import com.feinno.rpc.channel.RpcConnection;
import com.feinno.rpc.channel.RpcServerChannel;
import com.feinno.rpc.channel.RpcServerTransaction;
import com.feinno.threading.ExecutorFactory;
import com.feinno.util.EventHandler;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcUdsServerChannel extends RpcServerChannel
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcUdsServerChannel.class);
	private static final int MAX_CONCURRENT = 32;
	
	private UnixDomainSocketServer server;
	private Executor executor;
	private Thread acceptThread;

	public RpcUdsServerChannel(String path) throws IOException
	{
		super(RpcUdsClientChannel.SETTINGS, new RpcUdsEndpoint(path));
		
		LOGGER.info("Start listen:" + path);
		File cleanOnBegin = new File(path);
		cleanOnBegin.delete();
		
		server = new UnixDomainSocketServer(path, JUDS.SOCK_STREAM, MAX_CONCURRENT);
		File cleanOnEnd = new File(path);
		cleanOnEnd.deleteOnExit();
		
		this.getConnectionCreated().addListener(new EventHandler<RpcConnection>() {
			@Override
			public void run(Object sender, RpcConnection args)
			{
				args.getTransactionCreated().addListener(new EventHandler<RpcServerTransaction>() {
					@Override
					public void run(Object sender, RpcServerTransaction args)
					{
						RpcUdsServerChannel.this.getTransactionCreated().fireEvent(args);						
					}
				});
			}
		});
		executor = ExecutorFactory.newFixedExecutor("uds-receive", 32, 32 * 1000);
		
		LOGGER.info("Listen in:" + path);
		acceptThread = new Thread(new Runnable() {
			@Override
			public void run()
			{
				processAccept();
			}
		});
		acceptThread.setName("RpcUds-acceptThread");
	}

	@Override
	protected void doStart() throws Exception
	{
		acceptThread.start();
	}

	@Override
	protected void doStop() throws Exception
	{
		acceptThread.interrupt();
		server.close();
		server.unlink();
		RpcUdsEndpoint ep = (RpcUdsEndpoint)this.getServerEndpoint();
		File f = new File(ep.getPath());
		f.delete();
	}

	private void processAccept()
	{
		while (true) {
			UnixDomainSocket socket = null;
			try {
				socket = server.accept();
				final RpcUdsConnectionServer conn = new RpcUdsConnectionServer(this, socket);
				this.getConnectionCreated().fireEvent(conn);
				
				executor.execute(new Runnable() {
					@Override
					public void run()
					{
						try {
							conn.receive();
						} catch (Exception ex) {
							LOGGER.error("receive() failed, {}", ex);
						}
					}
				});
			} catch (IOException e) {
				LOGGER.error("processAccept Error {}", e);
				RpcUdsCloseHelper.safeClose(socket);
			} catch (Exception ex) {
				LOGGER.error("processAccept got unexpective Error {}", ex);
				RpcUdsCloseHelper.safeClose(socket);
			}
		}
	}	
}
