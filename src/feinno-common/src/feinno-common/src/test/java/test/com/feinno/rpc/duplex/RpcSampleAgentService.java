/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-5-29
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.duplex;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;
import com.feinno.serialization.protobuf.types.ProtoString;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@RpcService("RpcSampleAgentService")
public interface RpcSampleAgentService
{
	@RpcMethod("Register")
	void register(ProtoString string);
	
	@RpcMethod("TestCallback")
	void testCallback();
}
