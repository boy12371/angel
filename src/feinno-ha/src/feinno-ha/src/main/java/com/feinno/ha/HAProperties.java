/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-8-9
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha;

import java.util.HashMap;
import java.util.Map;

import com.feinno.util.ConfigBean;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAProperties extends ConfigBean
{
	private String siteName;
	private String serviceName;
	private String configDatabase;
	private Map<String, String> servicePorts = new HashMap<String, String> (); 
	
	public String getSiteName()
	{
		return siteName;
	}
	
	public void setSiteName(String site)
	{
		this.siteName = site;
	}
	
	public String getServiceName()
	{
		return serviceName;
	}
	
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}
	
	public String getConfigDatabase()
	{
		return configDatabase;
	}
	
	public void setConfigDatabase(String configDatabase)
	{
		this.configDatabase = configDatabase;
	}

	public Map<String, String> getServicePorts()
	{
		return servicePorts;
	}

	public void setServicePorts(Map<String, String> servicePorts)
	{
		this.servicePorts = servicePorts;
	} 
}
