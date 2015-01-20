/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Aug 23, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.deployment;

import com.feinno.rpc.channel.RpcEndpoint;

/**
 * 服务器
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAServer {
	private String serverName;
	private RpcEndpoint masterCenterEp; // HA_MasterEndpoint

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public RpcEndpoint getMasterCenterEp() {
		return masterCenterEp;
	}

	public void setMasterCenterEp(RpcEndpoint masterCenterEp) {
		this.masterCenterEp = masterCenterEp;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof HAServer)) {
			return false;
		}
		HAServer server = (HAServer) obj;
		if (serverName == server.getServerName() && masterCenterEp == server.getMasterCenterEp()) {
			return true;
		}
		if (serverName != null) {
			if (server.getServerName() == null || !serverName.equals(server.getServerName())) {
				return false;
			}
		} else {
			if (server.getServerName() != null) {
				return false;
			}
		}
		if (masterCenterEp != null) {
			if (server.getMasterCenterEp() == null || !masterCenterEp.equals(server.getMasterCenterEp())) {
				return false;
			}
		} else {
			if (server.getMasterCenterEp() != null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result += (serverName != null ? serverName.hashCode() * 17 : 0);
		result += (masterCenterEp != null ? masterCenterEp.hashCode() * 17 : 0);
		return result;
	}

}
