/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Sep 5, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.rpc;

import com.feinno.appengine.route.AppBeanParams;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RemoteAppBeanParams implements AppBeanParams
{
	private String categoryMinusName;
	
	public RemoteAppBeanParams(String s)
	{
		categoryMinusName = s;
	}

	public String getCategoryMinusName()
	{
		return categoryMinusName;
	}

	public void setCategoryMinusName(String categoryMinusName)
	{
		this.categoryMinusName = categoryMinusName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryMinusName == null) ? 0 : categoryMinusName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RemoteAppBeanParams other = (RemoteAppBeanParams) obj;
		if (categoryMinusName == null) {
			if (other.categoryMinusName != null)
				return false;
		} else if (!categoryMinusName.equals(other.categoryMinusName))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "RemoteAppBeanParams [categoryMinusName=" + categoryMinusName + "]";
	}
}
