/*
 * FAE, Feinno App Engine
 * 
 * Create by gaolei 2012-2-15
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.sample;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * Rpc测试服务器端虚拟类
 * 
 * @author 高磊 gaolei@feinno.com
 */
@RpcService("RpcSampleService")
public interface RpcSampleService
{
	@RpcMethod("Hello")
	HelloResult hello(HelloArgs args);

	@RpcMethod("add")
	RpcSampleResults add(RpcSampleArgs args);

	public static class RpcSampleArgs extends ProtoEntity
	{
		@ProtoMember(1)
		private String message;

		@ProtoMember(2)
		private int x;

		@ProtoMember(3)
		private int y;

		public String getMessage()
		{
			return message;
		}

		public void setMessage(String message)
		{
			this.message = message;
		}

		public int getX()
		{
			return x;
		}

		public void setX(int x)
		{
			this.x = x;
		}

		public int getY()
		{
			return y;
		}

		public void setY(int y)
		{
			this.y = y;
		}
	}

	public static class RpcSampleResults extends ProtoEntity
	{
		@ProtoMember(1)
		private String message;

		@ProtoMember(2)
		private int r;

		public String getMessage()
		{
			return message;
		}

		public void setMessage(String message)
		{
			this.message = message;
		}

		public int getR()
		{
			return r;
		}

		public void setR(int r)
		{
			this.r = r;
		}
	}

	public static class HelloArgs extends ProtoEntity
	{
		@ProtoMember(1)
		private String str;

		@ProtoMember(2)
		private int begin;

		@ProtoMember(3)
		private int len;

		public String getStr()
		{
			return str;
		}

		public void setStr(String str)
		{
			this.str = str;
		}

		public int getBegin()
		{
			return begin;
		}

		public void setBegin(int begin)
		{
			this.begin = begin;
		}

		public int getLen()
		{
			return len;
		}

		public void setLen(int len)
		{
			this.len = len;
		}
	}

	public static class HelloResult extends ProtoEntity
	{
		@ProtoMember(1)
		private String str;

		public String getStr()
		{
			return str;
		}

		public void setStr(String str)
		{
			this.str = str;
		}
	}
}
