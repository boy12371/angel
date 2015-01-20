package com.feinno.appengine.http.server;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.http.HttpAppBean;
import com.feinno.appengine.http.HttpAppTx;
import com.feinno.appengine.testing.DebugProxyManager;

public class HttpAppBeanHandler extends AbstractHandler
{
	private static final Logger logger = LoggerFactory.getLogger(HttpAppBeanHandler.class);

	private static final String KEY_INVOCATION_FUTURN = "_FAE.http.bean.invocation.futurn";
	private static final String KEY_HD_APP_NAME = "AppName";
	public static final String KEY_HD_HTTPPREFIX = "HttpPrefix";
	private Map<String, HttpAppBean<?>> beanMap;
	private boolean debugMode;
	// TODO thread pool strategy
	private static final ExecutorService es = Executors.newCachedThreadPool();
	private int timeout;

	public HttpAppBeanHandler(Map<String, HttpAppBean<?>> beanMap, int timeout, boolean debugMode)
	{
		this.beanMap = beanMap;
		this.timeout = timeout;
		this.debugMode = debugMode;
	}

	@Override
	public void handle(String target, Request baseRequest, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException
	{

		logger.debug("got request [Handler]");

		String method = request.getMethod();
		// DEBUG模式下，允许GET方式访问
		if (debugMode || method.equals("POST")) {

			final HttpAppBean<?> bean = getBeanFromRequest(request);
			request.setAttribute(KEY_HD_HTTPPREFIX, target);
			if (bean != null) {
				if (DebugProxyManager.isCan(bean.getCategoryMinusName(), null)) {
					//如果开启了Http的debug，则通过HttpDebugProxy转发到制定得机器上
					HttpAppTx<AppContext> tx = new HttpAppTx<AppContext>(request, response, null);
					DebugProxyManager.isCanAndRun(bean.getCategoryMinusName(), null, tx);
					return ;
				}
				final Continuation continuation = ContinuationSupport.getContinuation(request);
				if (continuation.isExpired()) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "http bean invocation timeout.");

					Future<?> f = (Future<?>) request.getAttribute(KEY_INVOCATION_FUTURN);

					// "试图"cancel已经超时的bean
					if (f != null) {
						f.cancel(true); // TODO interrupt?
					}
				} else {
					continuation.setTimeout(timeout);
					continuation.suspend(response);
					Future<?> f = es.submit(new Runnable() {
						@Override
						public void run()
						{
							try {
								 invokeBean(request, response, bean, continuation);
								// continuation.complete();
								// 目前要求httpBean通过context.end()显式结束调用
							} catch (IOException t) {
								// invokeBean中已经尽力向client端发送错误信息了，此处只在server端记log
								logger.error("bean invocation failed", t);
							}
						}
					});  
					 request.setAttribute(KEY_INVOCATION_FUTURN, f);
				}
			} else {
				if (!debugMode) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					logger.warn("service not exists");
				} else {
					//showServiceList(response);
					baseRequest.setHandled(false);
					return ;
				}
			}
		} else {
			// 生产模式下，只处理POST请求
			logger.error("invalid method");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

		 baseRequest.setHandled(true);

	}

	private void invokeBean(HttpServletRequest request, HttpServletResponse response, HttpAppBean<?> bean, Continuation c) throws IOException
	{

		try {
			bean.processRequest(request, response, c);
		} catch (Exception t) {
			logger.error("error processing bean", t);

			if (!response.isCommitted())
				response.reset();

			if (debugMode) { // TODO copy stack trace to string buffer
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, t.getMessage());
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "");
			}
		}
	}

	/*
	 * spec: http://10.10.41.235/trac/fae/wiki/FAE_AppBeanOverHttp
	 * AppName:category-name;
	 * 
	 * 先从header中取，如果没有且在debug状态下，再从request parameters中尝试一次
	 */

	private String getAppBeanNameFromRequest(HttpServletRequest request)
	{
		String appName = request.getHeader(KEY_HD_APP_NAME);

		if (debugMode && appName == null) {
			appName = request.getParameter(KEY_HD_APP_NAME);
		}

		if (appName != null && !appName.equals("")) {
			return appName;
		}
		return null;
	}

	private HttpAppBean<?> getBeanFromRequest(HttpServletRequest request)
	{
		String beanName = getAppBeanNameFromRequest(request);
		if (beanName != null) {
			return beanMap.get(beanName);
		}
		logger.error("beanName [{}] not found", beanName);
		return null;
	}

	private void showServiceList(final HttpServletResponse response) throws IOException
	{
		// DEBUG模式下，显示可用服务列表以方便调试
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>" + "<h2>FAE</h2>" + "<h3> HttpAppBean Host (Debug mode)</h3>" + "<hr>"
				+ "<ul>Available services are:");
		for (String key : beanMap.keySet()) {
			String url = "/?AppName=" + key; // do we need URL encode?
			sb.append("<li><a href='").append(url).append("'>").append(key).append("</a></li>");
		}
		sb.append("</ul></body></html>");
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().println(sb.toString());
	}
}
