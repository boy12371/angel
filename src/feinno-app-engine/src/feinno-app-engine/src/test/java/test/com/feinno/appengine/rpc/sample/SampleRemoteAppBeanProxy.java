/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.appengine.rpc.sample;

import test.com.feinno.appengine.rpc.sample.SampleRemoteAppBean.Args;
import test.com.feinno.appengine.rpc.sample.SampleRemoteAppBean.Results;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.context.NullContext;
import com.feinno.appengine.rpc.RemoteAppBeanProxy;

/**
 * 生成到SampleRemoteAppBeanProxy
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class SampleRemoteAppBeanProxy extends RemoteAppBeanProxy<Args, Results, NullContext>
{
	/**
	 * {在这里补充功能说明}
	 * @param categoryMinusName
	 * @throws Throwable 
	 */
	public SampleRemoteAppBeanProxy(AppBean parent) throws Exception
	{
		super(parent, "sample-Add");
	}
}
