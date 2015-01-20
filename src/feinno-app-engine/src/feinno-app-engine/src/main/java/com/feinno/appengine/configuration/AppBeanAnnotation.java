/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Sep 5, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

import java.util.List;

import com.feinno.util.KeyValuePair;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppBeanAnnotation
{
	protected String type;

	private List<KeyValuePair<String, String>> fields;

	private AppBeanAnnotation[] childAnnotations;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public List<KeyValuePair<String, String>> getFields()
	{
		return fields;
	}

	public void setFields(List<KeyValuePair<String, String>> fields)
	{
		this.fields = fields;
	}

	public String getFieldValue(String field)
	{
		for (KeyValuePair<String, String> kv : fields) {
			if (field.equals(kv.getKey()))
				return kv.getValue();
		}
		return null;
	}

	public AppBeanAnnotation[] getChildAnnotations()
	{
		return childAnnotations;
	}

	public void setChildAnnotations(AppBeanAnnotation[] childAnnotations)
	{
		this.childAnnotations = childAnnotations;
	}
}