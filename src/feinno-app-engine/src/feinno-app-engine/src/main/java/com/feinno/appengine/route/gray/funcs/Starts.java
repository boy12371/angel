package com.feinno.appengine.route.gray.funcs;

import java.util.List;

import com.feinno.appengine.route.gray.OperandType;

class Starts extends FuncBase
{
	private String c;
	
	public Starts(List<String> args, OperandType type)
	{
		super(args, type);
		c = args.get(0);
	}
	
	@Override
	public boolean apply(String fieldValue)
	{
		return fieldValue.startsWith(c);
	}

}