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
import com.feinno.serialization.protobuf.types.ProtoInteger;
import com.feinno.serialization.protobuf.types.ProtoString;

/**
 * Rpc测试服务器端虚拟类
 * 
 * @author 高磊 gaolei@feinno.com
 */
@RpcService("Sample")
public interface SampleService
{
	@RpcMethod("add")
	ProtoInteger add(AddArgs args);
	
	@RpcMethod("echo")
	ProtoString echo(ProtoString args);
	
	public static class AddArgs extends ProtoEntity 
	{
		@ProtoMember(1)
		private int a;
		
		@ProtoMember(2)
		private int b;
		
		public int getA()
		{
			return a;
		}
		public void setA(int a)
		{
			this.a = a;
		}
		public int getB()
		{
			return b;
		}
		public void setB(int b)
		{
			this.b = b;
		}
	}	
}
