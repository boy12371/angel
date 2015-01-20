/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.appengine.rpc;

import org.junit.Test;

import test.com.feinno.appengine.rpc.sample.SampleRemoteAppBean;


/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class TestSampleRpcAppBean
{
	@Test
	public void testBean()
	{
//		SampleRemoteAppBeanProxy proxy = new SampleRemoteAppBeanProxy();
//		SampleRemoteAppBeanProxy.Args a = new SampleRemoteAppBeanProxy.Args();
//		a.setA(100);
//		a.setB(100);
//		SampleRemoteAppBeanProxy.Results r = proxy.invoke(null, a);
//		r.getR();
	}
	
	@Test
	public void testProxy()
	{
	}
	
	public static void main(String[] args) 
	{
		SampleRemoteAppBean bean = new SampleRemoteAppBean();
		System.out.println(bean.getVersion());
	}
}