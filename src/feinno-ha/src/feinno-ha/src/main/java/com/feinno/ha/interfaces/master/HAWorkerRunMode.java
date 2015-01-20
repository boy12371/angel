package com.feinno.ha.interfaces.master;

import com.feinno.util.EnumInteger;

public enum HAWorkerRunMode implements EnumInteger
{
	STATIC(0),
	DYNAMIC(1);

	private HAWorkerRunMode(int n)
	{
		this.n = n;
	}

	public int intValue()
	{
		return n;
	}

	private int n;
}
