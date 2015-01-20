package com.feinno.appengine.testing;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

import org.eclipse.jetty.servlets.ProxyServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.AppTx;
import com.feinno.appengine.http.HttpAppTx;
import com.feinno.appengine.http.server.HttpAppBeanHandler;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.threading.Future;

/**
 * 用于HttpAppBean的Debug代理类
 * 
 * @author lvmingwei
 * 
 */
public class HttpDebugProxy extends DebugProxy<ProxyServlet> {

	/** Http的Debug临时端口 **/
	public static final int HTTP_DEBUG_PORT = 7421;

	/** 日志引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpDebugProxy.class);

	/** 此集合类用于存储当前HttpDebugProxy中所用到得全部ProxyServlet，用于在当前HttpDebugProxy对象结束后统一销毁 */
	private static final List<ProxyServlet> PROXY_SERVLET_LIST = Collections
			.synchronizedList(new ArrayList<ProxyServlet>());

	/**
	 * 构造方法
	 * 
	 * @param categoryMinusName
	 * @param grayFactors
	 * @param client
	 */
	public HttpDebugProxy(String categoryMinusName, String grayFactors, RpcServerContext ctx) {
		super(categoryMinusName, grayFactors, ctx);
	}

	@Override
	public Future<ProxyServlet> invoke(RpcServerContext rpcServerContext, AppTx tx) throws Exception {
		if (!(tx instanceof HttpAppTx)) {
			LOGGER.error("AppTxWithContext is not instanceof HttpAppTx. tx is {}", tx);
		}
		LOGGER.info("Start HttpDebugProxy");
		@SuppressWarnings("unchecked")
		HttpAppTx<AppContext> httpAppTx = (HttpAppTx<AppContext>) tx;
		// 拼装地址
		RpcTcpEndpoint tcpEndpoint = (RpcTcpEndpoint) this.getConnection().getRemoteEndpoint();
		String address = tcpEndpoint.getAddress().getAddress().getHostAddress();
		String httpAddress = "http://" + address + ":" + HTTP_DEBUG_PORT;
		// 进行消息转发
		ProxyServlet servlet = new ProxyServlet.Transparent();
		String target = (String) httpAppTx.getRequest().getAttribute(HttpAppBeanHandler.KEY_HD_HTTPPREFIX);
		servlet.init(new HttpDebugProxy.JettyProxyServletConfig(target, httpAddress + target));
		LOGGER.info("Invoke to {}", httpAddress + target);
		servlet.service(httpAppTx.getRequest(), httpAppTx.getResponse());
		Future<ProxyServlet> future = new Future<ProxyServlet>();
		future.complete(servlet);
		return future;
	}

	/**
	 * 处理debug结束后的返回值的操作
	 * 
	 * @param ctx
	 * @param future
	 * @return
	 * @throws Exception
	 */
	public boolean complete(RpcServerContext ctx, Future<ProxyServlet> future) throws Exception {
		LOGGER.info("HttpDebugProxy complete,destroy ProxyServlet.");
		ProxyServlet servlet = future.getValue();
		// 此处添加到代销毁区域，因为异步的原因，此时servlet中的httpClient还没有执行结束，因此无法在此处销毁这个servlet
		PROXY_SERVLET_LIST.add(servlet);
		return true;
	}

	/**
	 * 销毁当前的HttpDebugProxy
	 */
	public void destroy() {
		LOGGER.info("Destroy HttpDebugProxy ProxyServlet.The number is {}", PROXY_SERVLET_LIST.size());
		List<ProxyServlet> tmpList = new ArrayList<ProxyServlet>();
		synchronized (PROXY_SERVLET_LIST) {
			for (ProxyServlet servlet : PROXY_SERVLET_LIST) {
				if (servlet != null) {
					tmpList.add(servlet);
				}
			}
		}
		PROXY_SERVLET_LIST.clear();
		for (ProxyServlet servlet : tmpList) {
			try {
				servlet.destroy();
			} catch (Exception e) {
				LOGGER.error("Destroy [ProxyServlet] failed.", e);
			}
		}
	}

	public static class JettyProxyServletConfig implements ServletConfig {

		private String contextPath = null;

		private ServletContext servletContext = null;

		private Map<String, String> init_parameter_map = new HashMap<String, String>();

		private static final String SERVLET_NAME_STRING = "JettyProxyServlet";

		public JettyProxyServletConfig(String contextPath, String ProxyTo) {
			// 我们的jetty没有servletContext...
			// this.servletContext = servletContext;
			this.contextPath = contextPath;
			init_parameter_map.put("Prefix", "");
			init_parameter_map.put("ProxyTo", ProxyTo);
			// init_parameter_map.put("maxThreads", "1");
			init_parameter_map.put("HostHeader", "127.0.0.1");
		}

		@Override
		public String getInitParameter(String key) {

			return init_parameter_map.get(key);
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			return Collections.enumeration(init_parameter_map.keySet());
		}

		@Override
		public ServletContext getServletContext() {
			servletContext = servletContext != null ? servletContext : new JettyProxyServletContext(contextPath);
			return servletContext;
		}

		@Override
		public String getServletName() {
			return SERVLET_NAME_STRING;
		}

	}

	@SuppressWarnings("deprecation")
	public static class JettyProxyServletContext implements ServletContext {

		private String contextPath = null;

		public JettyProxyServletContext(String contextPath) {
			this.contextPath = contextPath;
		}

		@Override
		public Dynamic addFilter(String s, String s1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Dynamic addFilter(String s, Filter filter) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Dynamic addFilter(String s, Class<? extends Filter> class1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void addListener(String s) {
			// TODO Auto-generated method stub

		}

		@Override
		public <T extends EventListener> void addListener(T arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addListener(Class<? extends EventListener> class1) {
			// TODO Auto-generated method stub

		}

		@Override
		public javax.servlet.ServletRegistration.Dynamic addServlet(String s, String s1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public javax.servlet.ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public javax.servlet.ServletRegistration.Dynamic addServlet(String s, Class<? extends Servlet> class1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T extends Filter> T createFilter(Class<T> class1) throws ServletException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T extends EventListener> T createListener(Class<T> class1) throws ServletException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T extends Servlet> T createServlet(Class<T> class1) throws ServletException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void declareRoles(String... as) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object getAttribute(String s) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Enumeration<String> getAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ClassLoader getClassLoader() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ServletContext getContext(String s) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getContextPath() {
			return contextPath;
		}

		@Override
		public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getEffectiveMajorVersion() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getEffectiveMinorVersion() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public FilterRegistration getFilterRegistration(String s) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getInitParameter(String s) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JspConfigDescriptor getJspConfigDescriptor() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getMajorVersion() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getMimeType(String s) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getMinorVersion() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public RequestDispatcher getNamedDispatcher(String s) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getRealPath(String s) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public RequestDispatcher getRequestDispatcher(String s) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public URL getResource(String s) throws MalformedURLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public InputStream getResourceAsStream(String s) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Set<String> getResourcePaths(String s) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getServerInfo() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Servlet getServlet(String s) throws ServletException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getServletContextName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Enumeration<String> getServletNames() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ServletRegistration getServletRegistration(String s) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<String, ? extends ServletRegistration> getServletRegistrations() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Enumeration<Servlet> getServlets() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SessionCookieConfig getSessionCookieConfig() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void log(String s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void log(Exception exception, String s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void log(String s, Throwable throwable) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeAttribute(String s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setAttribute(String s, Object obj) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean setInitParameter(String s, String s1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setSessionTrackingModes(Set<SessionTrackingMode> set) {
			// TODO Auto-generated method stub

		}

	}
}
