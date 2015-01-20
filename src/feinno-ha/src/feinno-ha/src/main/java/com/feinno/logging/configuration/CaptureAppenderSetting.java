package com.feinno.logging.configuration;

import com.feinno.util.ConfigBean;

public class CaptureAppenderSetting extends ConfigBean {
	/**
	 * 日志输出到控制台是否可用标志: true-可用 false-不可用
	 */
	private boolean enabled;

	/**
	 * redisCache名称
	 */
	private String path;
	/**
	 * 实现类路径
	 */
	private String classPath;


	public boolean isEnabled() {
		return enabled;
	}
	
	public String getPath(){
		return path;
	}
	
	public String getClassPath(){
		return classPath;
	}
}
