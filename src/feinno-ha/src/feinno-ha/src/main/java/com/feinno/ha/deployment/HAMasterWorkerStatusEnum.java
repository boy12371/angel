package com.feinno.ha.deployment;

import com.feinno.util.EnumInteger;

/**
 * 
 * <b>描述: </b>Master传递过来的Worker状态的枚举信息
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 *
 * @author Lv.Mingwei
 *
 */
public enum HAMasterWorkerStatusEnum implements EnumInteger {

	INITIAL(1), 
	STANDBY(2), 
	RUNNING(3), 
	STOPPED(4), 
	DELAY_STOPPED(5), 
	CORRUPTED(6), 
	CRUSHED(7), 
	DISCONNECTED(8);

	private int value = 0;

	HAMasterWorkerStatusEnum(int value) {
		this.value = value;
	}

	@Override
	public int intValue() {
		return value;
	}
}
