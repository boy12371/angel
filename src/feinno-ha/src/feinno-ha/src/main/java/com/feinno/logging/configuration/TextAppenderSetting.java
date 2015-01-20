/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-9
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.logging.configuration;

import com.feinno.util.ConfigBean;

/**
 * 日志信息写入文件时,所需的参数类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class TextAppenderSetting extends ConfigBean {
	/**
	 * 记录日志文件的具体路径
	 */
	private String path;
	/**
	 * 日志输出到控制台是否可用标志: true-可用 false-不可用
	 */
	private boolean enabled;

	/**
	 * 获取控制台是否可用
	 * 
	 * @return 返回布尔类型
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 获取日志文件路径
	 * 
	 * @return 返回String类型
	 */
	public String getPath() {
		return path;
	}
}
