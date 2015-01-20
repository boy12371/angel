package com.feinno.appengine.route.gray.funcs;

import java.util.List;

import com.feinno.appengine.route.gray.OperandType;

class Equals extends FuncBase
{
	private static float DELTA = 0.000000001f;

	private String e;

	public Equals(List<String> args, OperandType type)
	{
		super(args, type);
		e = args.get(0);
	}

	@Override
	public boolean apply(String fieldValue)
	{
		switch (getOpType()) {
		case INT:
			long el = Long.parseLong(e);
			long fl = Long.parseLong(fieldValue);
			return el == fl;
		case FLOAT:
			float ef = Float.parseFloat(e);
			float ff = Float.parseFloat(fieldValue);
			float d = ef - ff;
			return (d > -DELTA) && (d < DELTA);
		case STRING:
		default:
			return fieldValue.equals(e);
		}
	}
}
