/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-8
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.appengine;

import test.com.feinno.appengine.RemoteAppBeanSample.Entity;

import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.context.SessionContext;
import com.feinno.appengine.rpc.RemoteAppBean;
import com.feinno.appengine.rpc.RemoteAppTx;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */

@AppName(category="sample",name="sample")
//HttpPrefix("/Sample.do")
public class RemoteAppBeanSample extends RemoteAppBean<Entity, Entity, SessionContext>
{
	/*
	 * @see com.feinno.appengine.rpc.RemoteAppBean#run(com.feinno.appengine.rpc.RemoteAppTx)
	 */
	/**
	 * {在这里补充功能说明}
	 * @param tx
	 */
	@Override
	public void process(RemoteAppTx<Entity, Entity, SessionContext> tx)
	{
		Entity e = tx.args();
		e.a = e.a + e.b;
		
		tx.end(e);
	}

	/*
	 * @see com.feinno.appengine.AppBean#setup()
	 */
	/**
	 * {在这里补充功能说明}
	 */
	@Override
	public void setup()
	{
	}

	/*
	 * @see com.feinno.appengine.AppBean#load()
	 */
	/**
	 * {在这里补充功能说明}
	 */
	@Override
	public void load() throws Exception
	{
		getInjector().addBeforeHandler(AppBeanHandlerSample.class);
	}

	/*
	 * @see com.feinno.appengine.AppBean#unload()
	 */
	/**
	 * {在这里补充功能说明}
	 */
	@Override
	public void unload()
	{
		
	}
	
	public static class Entity extends ProtoEntity
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
}
