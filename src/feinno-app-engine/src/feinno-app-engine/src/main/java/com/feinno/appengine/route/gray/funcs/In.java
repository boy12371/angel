package com.feinno.appengine.route.gray.funcs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.feinno.appengine.route.gray.OperandType;

class In extends FuncBase 
{
	private Set<String> set;
	
	public In(List<String> args, OperandType type)
	{
		super(args, type);
		set = new HashSet<String>();
		for (String a: args) {
			if (!set.contains(a))
				set.add(a);
		}
	}

	@Override
	public boolean apply(String fieldValue) 
	{
		//
		// 所有都用String判断,float的精度可能受到影响
		return set.contains(fieldValue);
	}
}
