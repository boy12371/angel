package com.feinno.appengine.route.gray.funcs;

import java.util.List;

import com.feinno.appengine.route.gray.OperandType;

class Between extends FuncBase
{
	private static float DELTA = 0.000000001f;
	String l;
	String h;

	public Between(List<String> args, OperandType type)
	{
		super(args, type);
		l = args.get(0);
		h = args.get(1);
	}

	@Override
	public boolean apply(String fieldValue)
	{
		switch (getOpType()) {
		case STRING:
			return fieldValue.compareTo(l) >= 0 && fieldValue.compareTo(h) <= 0;
		case INT:
			long il = Long.parseLong(l);
			long ih = Long.parseLong(h);
			long f = Long.parseLong(fieldValue);
			return f >= il && f <= ih;
		case FLOAT:
		default:
			float fl = Float.parseFloat(l) - DELTA;
			float fu = Float.parseFloat(h) + DELTA;
			float ff = Float.parseFloat(fieldValue);
			return ff > fl && ff < fu;
		}
	}
}
