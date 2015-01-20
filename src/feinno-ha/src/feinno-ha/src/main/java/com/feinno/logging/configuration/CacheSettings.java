/*
 * FAE, Feinno App Engine
 *  
 * Create by zhangyali 2011-3-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.logging.configuration;

import com.feinno.util.ConfigBean;

/**
 * 日志缓存设置
 * 
 * @author zhangyali@feinno.com
 */
public class CacheSettings extends ConfigBean {

	/**
	 * 缓存日志队列缓存的名称
	 */
	private String cacheName;

	/**
	 * 每次记录日志间隔毫秒数
	 */
	private int lazyMs;

	/**
	 * 每次打印日志的数量
	 */
	private int batchCount;

	/**
	 * 日志缓存可用标志: true-可用 false-不可用
	 */
	private boolean enabled;

	public String getCacheName() {
		return cacheName;
	}

	public int getLazyMs() {
		return lazyMs;
	}

	public int getBatchCount() {
		return batchCount;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
