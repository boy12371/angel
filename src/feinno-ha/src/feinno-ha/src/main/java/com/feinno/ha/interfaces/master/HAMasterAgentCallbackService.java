/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei May 15, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.master;

import com.feinno.rpc.server.RpcService;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@RpcService("HAMasterAgentCallbackService")
public interface HAMasterAgentCallbackService {

	String NAME = "HAMasterAgentCallbackService";

	int EXT_SERVER_NAME = 1001;

	void startWorker();

	void stopWorker();

	void updateWorker();

	void killWorker();

	void refreshWorkers();

	void getStatus();
}
