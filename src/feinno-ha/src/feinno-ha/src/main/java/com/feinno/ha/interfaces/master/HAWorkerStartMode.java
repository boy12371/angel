package com.feinno.ha.interfaces.master;

import com.feinno.util.EnumInteger;

public enum HAWorkerStartMode implements EnumInteger
{
	AUTO(0),
	MANUAL(1);

	private HAWorkerStartMode(int n)
	{
		this.n = n;
	}

	public int intValue()
	{
		// TODO Auto-generated method stub
		return n;
	}

	private int n;
}
