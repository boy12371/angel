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
 * 日志记录到数据库时所需要的参数类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class DatabaseAppenderSetting extends ConfigBean {
	/**
	 * 日志输出到控制台是否可用标志: true-可用 false-不可用
	 */
	private boolean enabled;

	/**
	 * 数据库url
	 */
	private String url;
	/**
	 * 数据库名称
	 */
	private String database;
	/**
	 * 数据库驱动
	 */
	private String driver;

	/**
	 * 数据库用户名
	 */
	private String user;

	/**
	 * 数据库密码
	 */
	private String password;

	/**
	 * 数据库插入日志sql语句
	 */
	private String table;

	public String getUrl() {
		return url;
	}

	public String getDriver() {
		return driver;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getTable() {
		return table;
	}

	public final String getDatabase() {
		return database;
	}

	/**
	 * 获取控制台是否可用
	 * 
	 * @return 返回布尔类型
	 */
	public boolean isEnabled() {
		return enabled;
	}
}
