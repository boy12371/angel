/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-8-20
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.deployment;

import java.util.List;

/**
 * 
 * 服务器组
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAServerGroup {

	private String name;
	private List<HAServer> servers;

	public HAServerGroup() {

	}

	public HAServerGroup(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<HAServer> getServers() {
		return servers;
	}

	public void setServers(List<HAServer> servers) {
		this.servers = servers;
	}

	public int getServerCount()
	{
		return servers.size();
	}
}
