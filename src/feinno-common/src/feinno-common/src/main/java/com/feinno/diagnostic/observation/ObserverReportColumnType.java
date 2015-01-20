/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-10-8
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.observation;

import com.feinno.util.EnumInteger;

/**
 * 输出对象类型
 * 
 * @author 高磊 gaolei@feinno.com
 */
public enum ObserverReportColumnType implements EnumInteger {
	LONG(0), DOUBLE(1), RATIO(2), TEXT(3);

	private ObserverReportColumnType(int value) {
		this.value = value;
	}

	@Override
	public int intValue() {
		return value;
	}

	private int value;
}
