/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Aug 4, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.server.builtin;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcPingResults extends ProtoEntity {
	@ProtoMember(1)
	private String serverName;

	@ProtoMember(2)
	public static String serviceName;

	@ProtoMember(3)
	private String[] services;

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		RpcPingResults.serviceName = serviceName;
	}

	public String[] getServices() {
		return services;
	}

	public void setServices(String[] services) {
		this.services = services;
	}
}
