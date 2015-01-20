/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-6-9
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

import com.feinno.appengine.resource.ResourceType;
import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableKey;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class FAE_ResourcePolicyKey extends ConfigTableKey
{
	@ConfigTableField(value = "ResourceName",isKeyField=true)
	private String name;
	
	@ConfigTableField("ResourceType")
	private ResourceType type;
	
	@ConfigTableField("UriProtocol")
	private String protocol;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}


	public ResourceType getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ResourceType type) {
		this.type = type;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FAE_ResourcePolicyKey other = (FAE_ResourcePolicyKey) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (protocol == null) {
			if (other.protocol != null)
				return false;
		} else if (!protocol.equals(other.protocol))
			return false;
		if (type != other.type)
			return false;
		return true;
	}}