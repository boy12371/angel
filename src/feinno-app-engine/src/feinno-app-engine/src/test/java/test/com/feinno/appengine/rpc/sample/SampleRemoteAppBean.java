/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-28
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
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@AppName(category="sample", name="Add")
public class SampleRemoteAppBean extends RemoteAppBean<Args, Results, NullContext>
{
	/*
	 * @see com.feinno.appengine.AppBean#setup()
	 */
	/**
	 * {在这里补充功能说明}
	 * @throws Throwable
	 */
	@Override
	public void setup() throws Exception
	{
		// 处理Bean的自安装
	}

	/*
	 * @see com.feinno.appengine.AppBean#load()
	 */
	/**
	 * {在这里补充功能说明}
	 * @throws Throwable
	 */
	@Override
	public void load() throws Exception
	{
		// 处理Bean的初始化
	}

	/*
	 * @see com.feinno.appengine.AppBean#unload()
	 */
	/**
	 * {在这里补充功能说明}
	 * @throws Throwable
	 */
	@Override
	public void unload() throws Exception
	{
		// 处理Bean的卸载
	}
	
	public static class Args extends ProtoEntity
	{
		public int getA()
		{
			return a;
		}

		public int getB()
		{
			return b;
		}

		public void setA(int a)
		{
			this.a = a;
		}

		public void setB(int b)
		{
			this.b = b;
		}

		@ProtoMember(1)
		private int a;
		
		@ProtoMember(2)
		private int b;
	}
	
	public static class Results extends ProtoEntity
	{
		public int getR()
		{
			return r;
		}

		public void setR(int r)
		{
			this.r = r;
		}

		@ProtoMember(1)
		private int r;
	}

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
		LOGGER.debug("begin Process"); 
		Args a = tx.args();
		Results r = new Results();
		r.setR(a.getA() + a.getB());
		tx.end(r);
	}
}
