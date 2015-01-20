package com.feinno.appengine.route.gray.conds;

import com.feinno.appengine.AppContext;

public interface Cond
{
	boolean apply(AppContext ctx);
}
