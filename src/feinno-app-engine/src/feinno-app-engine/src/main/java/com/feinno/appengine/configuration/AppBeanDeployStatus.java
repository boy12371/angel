/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-8-28
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

import com.feinno.util.EnumInteger;

/**
 * AppBean的部署及运行状态
 * 
 * @author 高磊 gaolei@feinno.com
 */
public enum AppBeanDeployStatus implements EnumInteger
{
	UNDEPLOYED(0, "未部署"),
	RUNNING(1, "运行中"),
	MODIFIED(2, "已修改"),
	DEPLOYING(3, "部署中"),
	DISABLED(4, "已禁用"),
	;
	private int value;
	private String zhText;
	private AppBeanDeployStatus(int value, String zhText)
	{
		this.zhText = zhText;
	}
	
	public String getText()
	{
		return zhText;
	}
	
	public int intValue()
	{
		return value;
	}
}
