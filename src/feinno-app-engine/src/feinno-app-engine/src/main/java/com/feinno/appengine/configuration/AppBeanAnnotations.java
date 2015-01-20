/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.feinno.appengine.annotation.AppName;
import com.feinno.serialization.Serializer;
import com.feinno.serialization.json.JsonContract;

/**
 * 描述一个AppBean的类型信息及
 * 
 * @author 高磊 gaolei@feinno.com
 */
@JsonContract
public class AppBeanAnnotations
{
	private AppBeanClassInfo classInfo;

	private String appCategory;

	private String appName;

	private List<AppBeanAnnotation> annotations;

	public String getAppCategory() {
		return appCategory;
	}

	public void setAppCategory(String appCategory) {
		this.appCategory = appCategory;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public AppBeanClassInfo getClassInfo()
	{
		return classInfo;
	}

	public List<AppBeanAnnotation> getAnnotations()
	{
		return (List<AppBeanAnnotation>) annotations;
	}

	public void setClassInfo(AppBeanClassInfo classInfo)
	{
		this.classInfo = classInfo;
	}

	public void setAnnotations(List<AppBeanAnnotation> annotations)
	{
		this.annotations = new ArrayList<AppBeanAnnotation>();
		for (Object o : annotations) {
			if (o.getClass().equals(AppBeanAnnotation.class)) {
				this.annotations.add((AppBeanAnnotation) o);
			} else {
				throw new RuntimeException();
			}
		}
		// this.annotations = annotations;
	}

	public AppBeanAnnotation getAppBeanAnnotation(Class<?> clazz)
	{
		AppBeanAnnotation result = null;
		for (AppBeanAnnotation a : annotations) {
			if (clazz.getName().equals(a.getType())) {
				result = a;
				break;
			}
		}
		return result;
	}

	public List<AppBeanAnnotation> getChildAnnotations(Class<?> annosClazz, Class<?> childClazz)
	{
		List<AppBeanAnnotation> ret = new ArrayList<AppBeanAnnotation>();
		AppBeanAnnotation annos = getAppBeanAnnotation(annosClazz);
		if (annos == null) {
			return ret;
		}

		for (AppBeanAnnotation anno : annos.getChildAnnotations()) {
			ret.add(anno);
		}
		return ret;
	}

	public String getCategoryMinusName()
	{
		AppBeanAnnotation anno = getAppBeanAnnotation(AppName.class);
		if (anno == null)
			throw new IllegalArgumentException("AppBean must has annotation @AppName");

		String category = anno.getFieldValue("category");
		String name = anno.getFieldValue("name");
		return category + "-" + name;
	}

	public String toJsonString()
	{
		byte[] buffer;
		try {
			buffer = Serializer.encode(this);
			String str = new String(buffer);
			return str;
		} catch (IOException e) {
			throw new RuntimeException("toJson Failed", e);
		}
	}
}
