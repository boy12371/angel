/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 23, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.duplex;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;
import com.feinno.serialization.protobuf.types.ProtoString;

/**
 * 
 *    
 * 
 * @author 高磊 gaolei@feinno.com
 */
@RpcService("RpcSampleAgentCallbackService")
public interface RpcSampleAgentCallbackService
{
	@RpcMethod("Test")
	ProtoString test(ProtoString args);
}
