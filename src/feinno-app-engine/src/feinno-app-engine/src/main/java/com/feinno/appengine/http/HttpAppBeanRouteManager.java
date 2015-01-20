/*
 * FAE, Feinno App Engine
 * 
 * Create by windcraft Sep 5, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.configuration.AppBeanAnnotation;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.route.AppBeanParams;
import com.feinno.appengine.route.AppBeanRouteManager;
import com.feinno.appengine.route.router.AppBeanRouter;
import com.feinno.appengine.runtime.configuration.ApplicationEntity;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.util.ObjectHelper;
import com.feinno.util.PrefixDictionary;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HttpAppBeanRouteManager extends AppBeanRouteManager
{
	private PrefixDictionary<HttpAppBeanParams> paramsByPrefix;
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HttpAppBeanRouteManager.class);

	public HttpAppBeanRouteManager()
	{
		super();
		paramsByPrefix = new PrefixDictionary<HttpAppBeanParams>();
	}

	@Override
	public void onUpdateRouter(AppBeanParams param, AppBeanRouter router)
	{
		HttpAppBeanParams p = (HttpAppBeanParams) param;
		if (router != null)
		{
			p.setRouter(router);
			paramsByPrefix.put(p.getUrlPrefix(), p);
		}
		else
		{
			paramsByPrefix.remove(p.getUrlPrefix());
		}
	}

	/**
	 * 
	 * 通过访问的Url获取对方的地址
	 * 
	 * @param url
	 * @param ctx
	 * @return
	 */
	public RpcEndpoint routeHttpAppBean(String url, AppContext ctx, Map<String, Object> requestArgs)
	{
		HttpAppBeanParams params = paramsByPrefix.get(url);
		AppBeanRouter router = params.getRouter();
		if (router == null)
		{
			LOGGER.info(String.format("url中的参数有:%s", requestArgs.toString()));
			LOGGER.error(String.format("router for %s is null,routers have %s", url, paramsByPrefix.toString()));
			return null;
		}
		else
		{
			LOGGER.info(String.format("HttpAppBeanParams:%s", params.toString()));
			if (params.getStateParamField() == null || params.getStateParamField() == HttpStateParamField.NONE)
			{
				LOGGER.info(String.format("%s 所在没有HttpState注解", url));
				return router.route(ctx, null);
			}
			else
			{
				// TODO: 根据不同的HttpStateParamField进行路由
				// NONE(0),
				int hash = 0;
				switch (params.getStateParamField())
				{
					case CONTEXT:
					{
						hash = ctx.getContextUri().hashCode();
						LOGGER.info(String.format("走Context %s", String.valueOf(hash)));
						break;
					}
					case QUERY_STRING:
					{					
						hash = requestArgs.get(params.getStateValue()).hashCode();
						LOGGER.info(String.format("走QUERY_STRING %s", String.valueOf(hash)));
						break;
					}
					case URL:
						break;
					case COOKIE:
						break;
					case HEADER:
						break;
				}
				RpcEndpoint routeByHash = router.routeByHash(ctx, null, hash);
				if(LOGGER.isInfoEnabled())
				{
					String newline=System.getProperty("line.separator");
					LOGGER.info("url:"+url+" 参数值为："+requestArgs.toString()+newline+"返回结果应用服务器是:"+routeByHash+newline+" Router="+router.toString());
				}
				return routeByHash;
			}
		}
	}
	public AppBeanRouter getRouter(String url)
	{
		HttpAppBeanParams router = paramsByPrefix.get(url);
		if (router == null)
		{
			LOGGER.error("router for " + url + "is null" + paramsByPrefix.toString());
			return null;
		}
		else
		{
			return router.getRouter();
		}
	}

	@Override
	public List<AppBeanParams> getRequestParams(ApplicationEntity app)
	{
		List<AppBeanParams> ret = new ArrayList<AppBeanParams>();
		AppBeanAnnotations annos = app.getAppBeanAnnotations();

		AppBeanAnnotation stateAnno = annos.getAppBeanAnnotation(HttpState.class);
		// @HttpPrefix
		AppBeanAnnotation an1 = annos.getAppBeanAnnotation(HttpPrefix.class);
		if (an1 != null)
		{
			ret.add(HttpAppBeanParams.fromAnnotation(an1, stateAnno));
		}

		// @HttpPrefixes
		List<AppBeanAnnotation> ans2 = annos.getChildAnnotations(HttpPrefixes.class, HttpPrefix.class);
		for (AppBeanAnnotation a : ans2)
		{
			ret.add(HttpAppBeanParams.fromAnnotation(a, stateAnno));
		}
		return ret;
	}

	@Override
	public String toString()
	{
		return "HttpAppBeanRouteManager [paramsByPrefix=" + paramsByPrefix + "]";
	}
}
