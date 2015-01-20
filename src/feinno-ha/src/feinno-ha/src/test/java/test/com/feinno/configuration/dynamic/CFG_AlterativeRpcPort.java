/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-18
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.configuration.dynamic;

import java.util.Map;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;
import com.feinno.util.StringUtils;

/**
 * 当访问Rpc的表时, 获取到的Rpc地址使用其他端口代替 
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class CFG_AlterativeRpcPort extends ConfigTableItem
{
	@ConfigTableField(value = "Service", isKeyField = true)
	private String service;

	@ConfigTableField(value = "Protocol", isKeyField = true)
	private String protocol;

	@ConfigTableField("Port")
	private int port;
	
	@ConfigTableField("Address")
	private String address;
	
	private Map<String, String> replaces;
	
	public String getService()
	{
		return service;
	}

	public String getProtocol()
	{
		return protocol;
	}

	public int getPort()
	{
		return port;
	}

	public void setService(String service)
	{
		this.service = service;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public String getAlterativeAddress(String addr)
	{
		if (replaces != null) {
			return replaces.get(addr);
		} else {
			return null;
		}
	}
	
	@Override
	public void afterLoad() throws Exception
	{
		if (!StringUtils.isNullOrEmpty(address)) {
			replaces = StringUtils.splitValuePairs(address, ";", "=");
		}
	}
}
