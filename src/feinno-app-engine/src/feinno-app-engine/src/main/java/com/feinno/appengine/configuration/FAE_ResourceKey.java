/*
 * FAE, Feinno App Engine
 *  
 * Create by wanglihui 2011-3-22
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableKey;

/**
 * FAE_Resource表对应的主键
 * 
 * @auther wanglihui
 */
public class FAE_ResourceKey extends ConfigTableKey
{
	@ConfigTableField(value = "ResourceName", isKeyField = true)
	private String resourceName;

	@ConfigTableField(value = "ResourceIndex", isKeyField = true)
	private Integer resourceIndex;

	@Override
	public int hashCode()
	{
		return resourceName.hashCode() ^ resourceIndex.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		FAE_ResourceKey key = (FAE_ResourceKey) obj;
		return resourceName.equals(key.resourceName) && resourceIndex.equals(key.resourceIndex);
	}

	/**
	 * @return the resourceName
	 */
	public String getResourceName()
	{
		return resourceName;
	}

	/**
	 * @param resourceName
	 *            the resourceName to set
	 */
	public void setResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}

	/**
	 * @return the resourceIndex
	 */
	public Integer getResourceIndex()
	{
		return resourceIndex;
	}

	/**
	 * @param resourceIndex
	 *            the resourceIndex to set
	 */
	public void setResourceIndex(Integer resourceIndex)
	{
		this.resourceIndex = resourceIndex;
	}
}
