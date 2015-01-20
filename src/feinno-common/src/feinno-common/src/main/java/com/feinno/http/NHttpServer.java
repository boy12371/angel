package com.feinno.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.KeyStore;
import java.util.Properties;
import java.util.Map.Entry;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.nio.DefaultHttpServerIODispatch;
import org.apache.http.impl.nio.DefaultNHttpServerConnection;
import org.apache.http.impl.nio.DefaultNHttpServerConnectionFactory;
import org.apache.http.impl.nio.SSLNHttpServerConnectionFactory;
import org.apache.http.impl.nio.reactor.DefaultListeningIOReactor;
import org.apache.http.nio.NHttpConnectionFactory;
import org.apache.http.nio.NHttpServerConnection;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.nio.protocol.HttpAsyncRequestHandlerRegistry;
import org.apache.http.nio.protocol.HttpAsyncService;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.ListeningIOReactor;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.rpc.channel.RpcServerChannel;
import com.feinno.threading.ExecutorFactory;

public class NHttpServer
{
	private int port;

	private String ip;

	private ListeningIOReactor ioReactor;

	private RpcServerChannel serverchannel;
	
	private static Properties props = new Properties();

	private static Logger LOGGER = LoggerFactory.getLogger(NHttpServer.class);

	private HttpAsyncRequestHandlerRegistry reqistry = new HttpAsyncRequestHandlerRegistry();
	
	static
	{
		ClassLoader cl = NHttpServer.class.getClassLoader();
		URL resource = cl.getResource("NHttpParams.properties");
		try
		{
			props.load(resource.openStream());
		}
		catch (IOException e)
		{
			LOGGER.error("NHttpParams加载失败", e);
		}
	}

	public NHttpServer(int port)
	{
		this("0.0.0.0", port);
	}

	public NHttpServer(String ip, int port)
	{
		this.port = port;
		this.ip = ip;
	}

	/**
	 * 添加url匹配处理
	 * 
	 * @param pattern
	 * @param handler
	 */
	public void add(String pattern, HttpAsyncRequestHandler handler)
	{
		reqistry.register(pattern, handler);
	}

	public RpcServerChannel getServerchannel()
	{
		return serverchannel;
	}

	public void setServerchannel(RpcServerChannel serverchannel)
	{
		this.serverchannel = serverchannel;
	}

	public void start()
	{
		final IOEventDispatch ioEventDispatch = new DefaultHttpServerIODispatch(this.createHttpAsyncService(), this.createNHttpConnectionFactory());
		
		try
		{
			ioReactor = new DefaultListeningIOReactor();
		}
		catch (IOReactorException e)
		{
			LOGGER.error("DefaultListeningIOReactor 初始化error", e);
		}
		
		ioReactor.listen(new InetSocketAddress(ip, port));
	
		ExecutorFactory.newFixedExecutor("HttpNio", 1, 1).execute(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					ioReactor.execute(ioEventDispatch);
				}
				catch (IOException e)
				{
					LOGGER.error("I/O error: " + e.getMessage(), e);
				}
			}
		});
	}

	public void stop()
	{
		try
		{
			ioReactor.shutdown();
		}
		catch (IOException e)
		{
			LOGGER.error("服务停止异常", e);
		}
	}

	public static HttpParams createHttpParams()
	{
		HttpParams params = new SyncBasicHttpParams();
		for (Entry<Object, Object> entry : props.entrySet())
		{
			if (entry.getKey() != null && entry.getValue() != null)
			{
				String value = entry.getValue().toString().trim();
				if (value.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$"))
				{
					params.setIntParameter(entry.getKey().toString().trim(), Integer.valueOf(value));
				}
				else
				{
					params.setParameter(entry.getKey().toString().trim(), entry.getValue().toString().trim());
				}
			}
		}
		return params;
	}

	private HttpProcessor createHttpProcessor()
	{
		return new ImmutableHttpProcessor(new HttpResponseInterceptor[] { new ResponseDate(), new ResponseServer(), new ResponseContent(), new ResponseConnControl() });
	}

	private HttpAsyncService createHttpAsyncService()
	{
		HttpAsyncService protocolHandler = new HttpAsyncService(this.createHttpProcessor(), new DefaultConnectionReuseStrategy(), reqistry, createHttpParams())
		{
			@Override
			public void connected(final NHttpServerConnection conn)
			{
				LOGGER.info(conn + ": connection open");
				super.connected(conn);
			}

			@Override
			public void closed(final NHttpServerConnection conn)
			{
				LOGGER.info(conn + ": connection closed");
			}

		};
		return protocolHandler;

	}

	private NHttpConnectionFactory<DefaultNHttpServerConnection> createNHttpConnectionFactory()
	{
		if ("true".equals(props.get("ssl_isenable").toString().trim().toLowerCase()))
		{
			FileInputStream in = null;
			try
			{
				in = new FileInputStream(System.getProperty("java.home") + File.separatorChar + "lib" + File.separatorChar + "security" + File.separatorChar + "cacerts");
			}
			catch (FileNotFoundException e1)
			{
				LOGGER.error("文件没有找到", e1);
				return null;
			}
			try
			{
				KeyStore keystore = KeyStore.getInstance("jks");
				keystore.load(in, "changeit".toCharArray());
				KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				kmfactory.init(keystore, "changeit".toCharArray());
				KeyManager[] keymanagers = kmfactory.getKeyManagers();
				SSLContext sslcontext = SSLContext.getInstance("TLS");
				sslcontext.init(keymanagers, null, null);
				return new SSLNHttpServerConnectionFactory(sslcontext, null, createHttpParams());
			}
			catch (Exception e)
			{
				LOGGER.error("密钥获取失败", e);
			}
			return null;
		}
		else
		{
			return new DefaultNHttpServerConnectionFactory(createHttpParams());
		}
	}

	public static void main(String[] args) throws InterruptedException
	{
		NHttpServer server = new NHttpServer(8443);
		server.add("*", new DefaultHandler());
		server.start();

	}
}
