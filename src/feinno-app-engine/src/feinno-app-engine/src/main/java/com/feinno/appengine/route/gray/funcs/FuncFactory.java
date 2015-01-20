package com.feinno.appengine.route.gray.funcs;

import java.util.List;

import com.feinno.appengine.route.gray.OperandType;
import com.feinno.configuration.ConfigurationException;

public class FuncFactory 
{
	public static FuncBase createFunc(String funcName, List<String> args, OperandType opType) throws ConfigurationException
	{
		String n = funcName.toLowerCase();
		if (n.equals("between")) {
			return new Between(args, opType);
		} else if (n.equals("contains")) {
			return new Contains(args, opType);
		} else if (n.equals("ends")) {
			return new Ends(args, opType);
		} else if (n.equals("equals")) {
			return new Equals(args, opType);
		} else if (n.equals("in")) {
			return new In(args, opType);
		} else if (n.equals("random")) {
			return new Random(args, opType);
		} else if (n.equals("starts")) {
			return new Starts(args, opType);
		} else if (n.equals("package")) {
			return new Package(args, opType);
		} else {
			throw new FunctionNotFoundException(funcName);
		}
	}		
}
