package com.feinno.appengine.http.server;

import java.io.ByteArrayInputStream;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.xml.XmlConfiguration;

import com.feinno.appengine.http.HttpAppBean;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.webapp.AppScanner;

public class JettyServer
{
	private final static Logger log = Log.getLogger(JettyServer.class);
	private String host;
	private int port;
	private Server server;
	private AppScanner scanner;
	private Boolean isEnableRequestLog = false;
	private int TIEMOUT = 180 * 1000;
	private boolean debugMode = true;
	private static Map<String, HttpAppBean<?>> beanMap = new Hashtable<String, HttpAppBean<?>>();

	public JettyServer()
	{
		this("127.0.0.1", 8080, false);
	}

	public JettyServer(String host, int port, Boolean isEnableRequestLog)
	{
		this.host = host;
		this.port = port;
		this.isEnableRequestLog = isEnableRequestLog;

		server = buildServer();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(this.port);
		connector.setHost(this.host);
		connector.setAcceptors(2);
		connector.setAcceptQueueSize(2500);
		connector.setThreadPool(new QueuedThreadPool(30));
		connector.setName("JettyHttpServer");
		connector.setLowResourcesConnections(10240);
		server.setConnectors(new Connector[] { connector });
		scanner = new AppScanner(server);
	}

	public void registerBean(String id, HttpAppBean<?> bean)
	{
		beanMap.put(id, bean);
	}

	public void registerWar(String warPath)
	{
		scanner.addWar(warPath);
	}

	public void registerWar(String contextName, String warPath)
	{
		scanner.addWar(contextName, warPath);
	}

	public void unregisterBean(String id)
	{
		beanMap.remove(id);
	}

	public void unregisterWar(String warPath)
	{
		scanner.removeWar(warPath);
	}

	public void start()
	{
		if (server != null)
		{
			try
			{
				server.start();
				scanner.start();
			}
			catch (Exception e)
			{
				log.warn("server 起动失败", e);
			}
		}
		else
		{
			log.warn("server 配置错误", "");
		}
	}

	public void stop()
	{
		if (server != null)
		{
			try
			{
				server.stop();
				scanner.stop();
			}
			catch (Exception e)
			{
				log.warn("server 关闭异常", e);
			}
		}
	}

	private Server buildServer()
	{
		server = new Server();
		try
		{
			ConfigurationManager.loadText("jetty-server.xml", null, new ConfigUpdateAction<String>()
			{
				@Override
				public void run(String e) throws Exception
				{
					XmlConfiguration configuration = new XmlConfiguration(new ByteArrayInputStream(e.getBytes()));

					configuration.configure(server);
				}
			});
		}
		catch (ConfigurationException e)
		{
			log.warn("配置jetty-server.xml出错", e);
		}
		if (server.getThreadPool() == null)
		{
			QueuedThreadPool threadPool = new QueuedThreadPool();
			threadPool.setMaxThreads(50);
			threadPool.setMinThreads(2);
			server.setThreadPool(threadPool);
		}

		// Set default handlers if not set with the configuration file
		HandlerCollection contexts = (HandlerCollection) server.getChildHandlerByClass(ContextHandlerCollection.class);
		if (contexts == null)
		{
			contexts = (HandlerCollection) server.getChildHandlerByClass(HandlerCollection.class);
		}
		if (contexts == null)
		{
			HandlerCollection handlers = new HandlerCollection();
			contexts = new ContextHandlerCollection();

			// Logger
			if (this.isEnableRequestLog)
			{
				RequestLogHandler requestLogHandler = new RequestLogHandler();
				NCSARequestLog requestLog = new NCSARequestLog();
				requestLog.setExtended(true);
				requestLogHandler.setRequestLog(requestLog);

				handlers.setHandlers(new Handler[] { contexts, new DefaultHandler(), requestLogHandler, new HttpAppBeanHandler(beanMap, TIEMOUT, debugMode) });
			}
			else
			{// ,new DefaultHandler()
				handlers.setHandlers(new Handler[] { new HttpAppBeanHandler(beanMap, TIEMOUT, debugMode), contexts });
			}

			server.setHandler(handlers);
		}

		// Set a connector if none has been given in the config file
		if (server.getConnectors() == null || server.getConnectors().length == 0)
		{
			SelectChannelConnector connector = new SelectChannelConnector();
			connector.setPort(port);
			connector.setHost(host);
			server.setConnectors(new Connector[] { connector });
		}

		// Create the web app modules:
		/*
		 * WebAppContext webapp = new WebAppContext("./", "/");
		 * contexts.addHandler(webapp);
		 */
		return server;
	}

	public String getHost()
	{
		return host;
	}

	public int getPort()
	{
		return port;
	}

}
