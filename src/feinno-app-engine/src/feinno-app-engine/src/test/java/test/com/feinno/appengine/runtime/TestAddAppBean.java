/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-23
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.appengine.runtime;

import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.context.NullContext;
import com.feinno.appengine.rpc.RemoteAppBean;
import com.feinno.appengine.rpc.RemoteAppTx;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
@AppName(category="test",name="TestAdd")
public class TestAddAppBean extends RemoteAppBean<TestAddAppBean.Args, TestAddAppBean.Results, NullContext>
{
	
	public static class Args extends ProtoEntity
	{
		@ProtoMember(1)
		private int a;
		
		@ProtoMember(2)
		private int b;
		
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
	}
	
	public static class Results extends ProtoEntity
	{
		@ProtoMember(1)
		private int r;
				
		public int getR()
		{
			return r;
		}
		
		public void setR(int r)
		{
			this.r = r;
		}
	}
	/*
	 * @see com.feinno.appengine.AppBean#settings()
	 */

	/*
	 * @see com.feinno.appengine.AppBean#setup()
	 */
	/**
	 * {在这里补充功能说明}
	 */
	@Override
	public void setup()
	{
		throw new UnsupportedOperationException("没实现呢");
	}
	/*
	 * @see com.feinno.appengine.AppBean#prepare()
	 */

	/**
	 * @see com.feinno.appengine.rpc.RemoteAppBean#run(com.feinno.appengine.rpc.RemoteAppTx)
	 */
	/**
	 * {在这里补充功能说明}
	 * @param tx
	 */
	@Override
	public void process(RemoteAppTx<Args, Results, NullContext> tx)
	{
		int r = tx.args().getA() + tx.args().getB();
		Results res = new Results();
		res.setR(r);
		tx.end(res);
	}
	/*
	 * @see com.feinno.appengine.AppBean#load()
	 */
	/**
	 * {在这里补充功能说明}
	 */
	@Override
	public void load()
	{
		throw new UnsupportedOperationException("没实现呢");
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
		throw new UnsupportedOperationException("没实现呢");
	}
}
