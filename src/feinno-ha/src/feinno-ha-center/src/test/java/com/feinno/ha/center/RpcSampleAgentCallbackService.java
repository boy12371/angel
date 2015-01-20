/**
 * 
 */
package com.feinno.ha.center;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;
import com.feinno.serialization.protobuf.types.ProtoString;

/**
 * <b>描述: </b>
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 *
 * @author Zhou.yan
 *
 */
@RpcService("RpcSampleAgentCallbackService")
public interface RpcSampleAgentCallbackService
{
	@RpcMethod("Test")
	ProtoString test(ProtoString args);
}

