package com.feinno.appengine.route.gray.conds;

import com.feinno.appengine.AppContext;

public class AndCond implements Cond
{
	private Cond l, r;

	public AndCond(Cond l, Cond r)
	{
		this.l = l;
		this.r = r;
	}

	@Override
	public boolean apply(AppContext ctx)
	{
		System.out.println("invoking AND.");
		return l.apply(ctx) && r.apply(ctx);
	}
}
