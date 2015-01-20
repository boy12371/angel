/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 23, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.center;

import com.feinno.rpc.duplex.RpcDuplexClientAgent;
import com.feinno.rpc.server.RpcServerContext;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class WorkerAgent extends RpcDuplexClientAgent {

	private String serverName;

	private String seviceName;

	private int workerPid;

	public WorkerAgent(RpcServerContext ctx) {
		super(ctx);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getSeviceName() {
		return seviceName;
	}

	public void setSeviceName(String seviceName) {
		this.seviceName = seviceName;
	}

	public int getWorkerPid() {
		return workerPid;
	}

	public void setWorkerPid(int workerPid) {
		this.workerPid = workerPid;
	}
}
