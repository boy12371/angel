package com.feinno.ha.interfaces.worker;

import com.feinno.util.EnumInteger;

public enum HAWorkerStatus implements EnumInteger
{
	// 待机状态, 手动
	STANDBY(0),

	// 正在更新
	UPDATING(1),

	// 服务启动中
	STARTING(2),

	// 服务正在运行
	RUNNING(3),

	// 服务停止中
	STOPPING(4),

	// 服务宕机了
	CRASHED(5),

	// 服务离线中
	DISCONNECTED(6);

	private HAWorkerStatus(int n)
	{
		this.n = n;
	}

	public int intValue()
	{
		return n;
	}

	private int n;
}
