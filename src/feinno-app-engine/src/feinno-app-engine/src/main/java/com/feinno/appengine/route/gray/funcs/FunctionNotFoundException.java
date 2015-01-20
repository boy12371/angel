package com.feinno.appengine.route.gray.funcs;

public class FunctionNotFoundException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3737000941291257694L;

	public FunctionNotFoundException()
	{
		super();
	}

	public FunctionNotFoundException(String message)
	{
		super(message);
	}
}
