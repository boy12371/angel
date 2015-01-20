/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine;

/**
 * ContextUri是FAE中的定位标准 一般的形式如下: id:1231232;p=5002
 * 
 * 分为 id: protocol 1231232 value ; parameters
 * 
 * 三个部分 *
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class ContextUri
{
	public abstract String getProtocol();

	public abstract String getValue();

	protected abstract void setValue(String value);

	public abstract String getParameter(String p);

	protected abstract void setParameter(String p, String value);

	public abstract String toString();

	public abstract int getRouteHash();

	/**
	 * 
	 * 解析属于ContextUri格式的ContextUri数据
	 * 
	 * @param <E>
	 * @param clazz
	 * @param uri
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <E extends ContextUri> E parse(Class<E> clazz, String uri)
	{
		// 解析id:xxxx;p=123;a=234;d=aab
		int first = uri.indexOf(':');
		if (first < 0) {
			throw new IllegalArgumentException("except ':' but not found:" + uri);
		}

		String protocol = uri.substring(0, first);
		E u;

		try {
			u = (E) clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Failed to initialize uri class: " + clazz, e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Illegal uri class: " + clazz, e);
		}

		if (!protocol.equals(u.getProtocol())) {
			throw new IllegalArgumentException("Uri type not match " + u.getProtocol() + " with " + uri);
		}

		int next = uri.indexOf(';', first);
		if (next < 0) {
			String value = uri.substring(first);
			u.setValue(value);
		} else {
			String value = uri.substring(first + 1, next);
			u.setValue(value);
			String parameters = uri.substring(next);
			for (String s : parameters.split(";")) {
				int f = s.indexOf('=');
				if (f > 0) {
					String p = s.substring(0, f);
					String v = s.substring(f + 1);
					u.setParameter(p, v);
				}
			}
		}
		return u;
	}
}
