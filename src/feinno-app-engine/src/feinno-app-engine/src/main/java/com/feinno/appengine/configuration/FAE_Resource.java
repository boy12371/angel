/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft 2011-2-9
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class FAE_Resource extends ConfigTableItem
{
	@ConfigTableField("ResourceName")
	private String name;

	@ConfigTableField("ResourceType")
	private String type;

	@ConfigTableField("ResourceIndex")
	private Integer index;
	
	@ConfigTableField("Config")
	private String config;
	
	@ConfigTableField("Site")
	private String site;

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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * @return the config
	 */
	public String getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(String config) {
		this.config = config;
	}

	/**
	 * @return the site
	 */
	public String getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}
}
