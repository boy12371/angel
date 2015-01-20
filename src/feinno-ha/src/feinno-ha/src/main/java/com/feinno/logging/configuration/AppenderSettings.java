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
 * 日志输出平台所需的参数类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppenderSettings extends ConfigBean {
	/**
	 * 日志记录文件模板
	 */
	private TextAppenderSetting text = new TextAppenderSetting();

	/**
	 * 日志记录数据库模板
	 */
	private DatabaseAppenderSetting database = new DatabaseAppenderSetting();

	/**
	 * 日志打印输出到控制台
	 */
	private ConsoleAppenderSetting console = new ConsoleAppenderSetting();
	/**
	 * 日志抓取记录模版
	 */
	private CaptureAppenderSetting capture = new CaptureAppenderSetting();

	public TextAppenderSetting getText() {
		return text;
	}
	
	public CaptureAppenderSetting getCapture() {
		return capture;
	}

	public DatabaseAppenderSetting getDatabase() {
		return database;
	}

	public ConsoleAppenderSetting getConsole() {
		return console;
	}

}
