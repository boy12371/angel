/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-11-29
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.logging.appender;

import java.util.Queue;

import com.feinno.logging.LogEvent;
import com.feinno.logging.LogLevel;

/**
 * 日志输出的控制台类,提供将日志写入控制台的方法
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ConsoleAppender implements Appender {
	/**
	 * 此Appender是否有效标志,默认为有效
	 */
	private boolean valid = true;

	/*
	 * public boolean isValid() { return valid; }
	 */

	/**
	 * 设置Appender是否可用 true-可用 false-不可用
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * 输出日志到控制台
	 * 
	 * @param events
	 *            将要输出的日志信息集合
	 */
	@Override
	public void doAppend(Queue<LogEvent> events) {

		if (events.size() == 0)
			return;
		for (LogEvent event : events) {
			doAppend(event);
		}
	}

	/**
	 * 输出日志到控制台
	 * 
	 * @param events
	 *            将要输出的日志信息
	 */
	@Override
	public void doAppend(LogEvent event) {

		if (event == null)
			return;

		if (event.getLevel().intValue() >= LogLevel.WARN.intValue()) {
			System.err.println(event);
		} else {
			System.out.println(event);
		}

	}

	/**
	 * 设置Appender是否可用 true-可用 false-不可用
	 */
	/*
	 * public void setEnabled(boolean enabled) { this.setValid(enabled); }
	 */

	/**
	 * 获取Appender是否可用
	 */
	@Override
	public boolean isEnabled() {
		return valid;
	}

	public void destroy() {

	}
}
