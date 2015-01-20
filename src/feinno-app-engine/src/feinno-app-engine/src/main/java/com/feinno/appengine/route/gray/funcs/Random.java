package com.feinno.appengine.route.gray.funcs;

import java.util.List;

import com.feinno.appengine.route.gray.OperandType;

class Random extends FuncBase 
{
	private double threshold;
	
	public Random(List<String> args, OperandType type)
	{
		super(args, type);
		threshold = Double.parseDouble(args.get(0));
	}

	@Override
	public boolean apply(String fieldValue) 
	{
		return Math.random() < threshold;
	}
}
