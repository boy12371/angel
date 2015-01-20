/*
 * FAE, Feinno App Engine
 *  
 * Create by 李会军 2011-1-20
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.servermock;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;

/**
 * IRpcTestServiceMock
 * 
 * @author 李会军
 */
@RpcService("RpcTestServiceMock")
public interface IRpcTestServiceMock
{
	@RpcMethod("TestAdd")
	AddOutputArgs testAdd(AddInputArgs inputArgs);

	@RpcMethod("TestDelay")
	DelayOutputArgs testDelay(DelayInputArgs inputArgs);

	@RpcMethod("TestLargeData")
	LargeDataOutputArgs testLargeData(LargeDataInputArgs inputArgs);

	@RpcMethod("TestProxy")
	// 10.10.40.184:7777
	ProxyOutputArgs testProxy(ProxyInputArgs inputArgs);

	@RpcMethod("TestProxyDelay")
	ProxyOutputArgs testProxyDelay(ProxyDelayInputArgs inputArgs);
}
