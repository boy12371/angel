package com.feinno.appengine.route.gray.conds;

import com.feinno.appengine.AppContext;

public class OrCond implements Cond
{
	private Cond l, r;

	public OrCond(Cond l, Cond r)
	{
		this.l = l;
		this.r = r;
	}

	@Override
	public boolean apply(AppContext ctx)
	{
		return l.apply(ctx) || r.apply(ctx);
	}
}
