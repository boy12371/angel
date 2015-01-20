/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft 2011-2-1
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

import com.feinno.appengine.resource.ResourceType;
import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;

/**
 * 用于进行资源定位的表<br>
 * <table>
 * <tr>
 * 	<td>type</td>
	<td>name</td>
	<td>protocol</td>
	<td>locator</td>
	<td>params</td>
 * </tr>
 * <tr>
 * 	<td>database</td>
	<td>IICUPDB</td>
	<td>id</td>
	<td>IdPoolLocator</td>
	<td>CFG_LogicalPool</td>
 * </tr>
 * </table>
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class FAE_ResourcePolicy extends ConfigTableItem
{
	@ConfigTableField(value = "ResourceName",isKeyField=true)
	private String name;
	
	@ConfigTableField("ResourceType")
	private ResourceType type;
	
	@ConfigTableField("UriProtocol")
	private String protocol;
	
	@ConfigTableField("LocatorName")
	private String locator;
	
	@ConfigTableField("LocatorParams")
	private String locatorParams;
	
	@ConfigTableField("Sites")
	private String sites;

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

	/**
	 * @return the locator
	 */
	public String getLocator() {
		return locator;
	}

	/**
	 * @param locator the locator to set
	 */
	public void setLocator(String locator) {
		this.locator = locator;
	}

	/**
	 * @return the locatorParams
	 */
	public String getLocatorParams() {
		return locatorParams;
	}

	/**
	 * @param locatorParams the locatorParams to set
	 */
	public void setLocatorParams(String locatorParams) {
		this.locatorParams = locatorParams;
	}

	/**
	 * @return the sites
	 */
	public String getSites() {
		return sites;
	}

	/**
	 * @param sites the sites to set
	 */
	public void setSites(String sites) {
		this.sites = sites;
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
}

