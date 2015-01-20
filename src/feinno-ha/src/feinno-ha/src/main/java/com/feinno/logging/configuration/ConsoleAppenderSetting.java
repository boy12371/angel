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
 * 日志打印到控制台所需要的参数类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ConsoleAppenderSetting extends ConfigBean {
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

}
