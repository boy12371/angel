package com.feinno.http;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.nio.DefaultHttpClientIODispatch;
import org.apache.http.impl.nio.pool.BasicNIOConnPool;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutor;
import org.apache.http.nio.protocol.HttpAsyncRequester;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.threading.ExecutorFactory;

/**
 * @author zhouyang
 */
public class NHttpClient
{
	private static Logger LOGGER = LoggerFactory.getLogger(NHttpClient.class);
	private static HttpAsyncRequester requester;
	private static ConnectingIOReactor ioReactor;
	private static BasicNIOConnPool pool;
	private static Properties props = new Properties();
	public static NHttpClient Instance = new NHttpClient();
	private static String protocol = "http";
	static
	{
		ClassLoader cl = NHttpServer.class.getClassLoader();
		URL resource = cl.getResource("NHttpParams.properties");
		try
		{
			props.load(resource.openStream());
			if ("true".equals(props.get("ssl_isenable").toString().trim().toLowerCase()))
			{
				protocol = "https";
			}
		}
		catch (IOException e)
		{
			LOGGER.error("NHttpParams加载失败", e);
		}
	}

	private NHttpClient()
	{
		init();
	}

	private static void init()
	{
		HttpParams params = NHttpServer.createHttpParams();

		HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpRequestInterceptor[] { new RequestContent(), new RequestTargetHost(), new RequestConnControl(), new RequestUserAgent(), new RequestExpectContinue() });

		requester = new HttpAsyncRequester(httpproc, new DefaultConnectionReuseStrategy(), params);

		HttpAsyncRequestExecutor protocolHandler = new HttpAsyncRequestExecutor();

		final IOEventDispatch ioEventDispatch = new DefaultHttpClientIODispatch(protocolHandler, params);

		try
		{
			ioReactor = new DefaultConnectingIOReactor();
		}
		catch (IOReactorException e1)
		{
			LOGGER.error("ioReactor 初始化error", e1);
		}

		pool = new BasicNIOConnPool(ioReactor, params);
		pool.setDefaultMaxPerRoute(props.get("default_max_perRoute") != null ? Integer.valueOf(props.get("default_max_perRoute").toString()) : 50);
		pool.setMaxTotal(props.get("max_total") != null ? Integer.valueOf(props.get("max_total").toString()) : 2000);

		ExecutorFactory.newFixedExecutor("http-nio-client", 1, 1).execute(new Runnable()
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
					LOGGER.error("", e);
				}
			}
		});

	}

	private static Executor newScalableExecutor = ExecutorFactory.newScalableExecutor("http-client3");

	public void send(String hostname, int port, final HttpRequest request, final FutureCallback callback)
	{
		final HttpHost target = new HttpHost(hostname, port, protocol);
		newScalableExecutor.execute(new Runnable()
		{

			@Override
			public void run()
			{
				requester.execute(new BasicAsyncRequestProducer(target, request), new BasicAsyncResponseConsumer(), pool, new BasicHttpContext(), callback);
			}
		});
	}

	public void stop() throws IOException
	{
		ioReactor.shutdown();
	}
}
