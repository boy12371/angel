package com.feinno.appengine.route.gray.funcs;

import java.util.List;

import com.feinno.appengine.route.gray.OperandType;

public abstract class FuncBase
{
	private List<String> args;
	private OperandType opType;
	
	protected List<String> getArgs()
	{
		return args;
	}
	
	protected OperandType getOpType()
	{
		return opType;
	}
	
	protected FuncBase(List<String> args, OperandType opType)
	{
		this.args = args;
		this.opType = opType;
	}
	
	public abstract boolean apply(String fieldValue);
}
