package com.feinno.appengine.server;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;

@RpcService("AppEngineCallbackService")
public interface AppEngineCallbackService {
	
	@RpcMethod("refresh")
	public void refresh();

}
