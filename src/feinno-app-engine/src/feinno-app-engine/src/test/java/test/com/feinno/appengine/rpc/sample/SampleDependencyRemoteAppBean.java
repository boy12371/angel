/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.appengine.rpc.sample;

import test.com.feinno.appengine.rpc.sample.SampleRemoteAppBean.Args;
import test.com.feinno.appengine.rpc.sample.SampleRemoteAppBean.Results;

import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.context.NullContext;
import com.feinno.appengine.rpc.RemoteAppBean;
import com.feinno.appengine.rpc.RemoteAppTx;
import com.feinno.rpc.channel.RpcFuture;
/**
 * {在这里补充类的功能说明}
 * 
 * 
 * 
 * @author 高磊 gaolei@feinno.com
 */
@AppName(category="sample", name="DependencySample")
public class SampleDependencyRemoteAppBean extends RemoteAppBean<Args, Results, NullContext>
{
	private SampleRemoteAppBeanProxy proxy;

	/*
	 * @see com.feinno.appengine.rpc.RemoteAppBean#process(com.feinno.appengine.rpc.RemoteAppTx)
	 */
	/**
	 * {在这里补充功能说明}
	 * @param tx
	 * @throws Throwable
	 */
	@Override
	public void process(RemoteAppTx<Args, Results, NullContext> tx) throws Exception
	{
		SampleRemoteAppBean.Args args = new SampleRemoteAppBean.Args();
		args.setA(100);
		args.setB(200);
		RpcFuture future = proxy.invoke(NullContext.INSTANCE, args);
		SampleRemoteAppBean.Results results = future.getResult(SampleRemoteAppBean.Results.class); 
		results.getR();
	}

	/*
	 * @see com.feinno.appengine.AppBean#setup()
	 */
	/**
	 * {在这里补充功能说明}
	 * @throws Exception
	 */
	@Override
	public void setup() throws Exception
	{
	}

	/*
	 * @see com.feinno.appengine.AppBean#load()
	 */
	/**
	 * {在这里补充功能说明}
	 * @throws Exception
	 */
	@Override
	public void load() throws Exception
	{
		proxy = new SampleRemoteAppBeanProxy(this);
	}

	/*
	 * @see com.feinno.appengine.AppBean#unload()
	 */
	/**
	 * {在这里补充功能说明}
	 * @throws Exception
	 */
	@Override
	public void unload() throws Exception
	{
	}
}
