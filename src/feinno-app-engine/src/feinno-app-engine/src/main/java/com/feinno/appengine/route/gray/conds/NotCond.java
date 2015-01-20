package com.feinno.appengine.route.gray.conds;

import com.feinno.appengine.AppContext;

public class NotCond implements Cond
{
	private Cond c;

	public NotCond(Cond c)
	{
		this.c = c;
	}

	@Override
	public boolean apply(AppContext ctx)
	{

		return !c.apply(ctx);
	}

}
