/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Sep 5, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.http;

import com.feinno.appengine.configuration.AppBeanAnnotation;
import com.feinno.appengine.route.AppBeanParams;
import com.feinno.appengine.route.router.AppBeanRouter;

/**
 * 
 * 用于HttpAppBeanParams路由的参数类，
 * 其中仅有urlPrefix参与hash与equals计算
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HttpAppBeanParams implements AppBeanParams
{
	private String urlPrefix;
	
	//
	// 这两个字段不参与GetHashCode与其他计算
	private HttpStateParamField stateParamField;
	private String stateValue;
	private AppBeanRouter router;

	public String getUrlPrefix()
	{	
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix)
	{
		this.urlPrefix = urlPrefix;
	}
	
	public HttpStateParamField getStateParamField()
	{
		return stateParamField;
	}

	public void setStateParamField(HttpStateParamField stateParamField)
	{
		this.stateParamField = stateParamField;
	}
	

	public String getStateValue()
	{
		return stateValue;
	}

	public void setStateValue(String stateValue)
	{
		this.stateValue = stateValue;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((urlPrefix == null) ? 0 : urlPrefix.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpAppBeanParams other = (HttpAppBeanParams) obj;
		if (urlPrefix == null) {
			if (other.urlPrefix != null)
				return false;
		} else if (!urlPrefix.equals(other.urlPrefix))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "HttpAppBeanParams [urlPrefix=" + urlPrefix + ", stateParamField=" + stateParamField + ", stateValue=" + stateValue + "]";
	}

	public static AppBeanParams fromAnnotation(AppBeanAnnotation prefixAnno, AppBeanAnnotation stateAnno)
	{
		HttpAppBeanParams p = new HttpAppBeanParams();
		p.setUrlPrefix(prefixAnno.getFieldValue(HttpPrefix.VALUE));
		 
		if (stateAnno != null)
		{
			String stateParamField = stateAnno.getFieldValue(HttpState.PARAM_FIELD);
			p.setStateParamField(HttpStateParamField.valueOf(stateParamField));
			p.setStateValue(stateAnno.getFieldValue(HttpState.VALUE));
		}
		return p;
	}

	public AppBeanRouter getRouter()
	{
		return router;
	}

	public void setRouter(AppBeanRouter router)
	{
		this.router = router;
	}
}
