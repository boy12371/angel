/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-7-19
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.annotation.AppBeanBaseType;
import com.feinno.appengine.annotation.AppName;
import com.feinno.util.KeyValuePair;

/**
 * 该类提供了一个静态方法用来分析一个FAE应用，并返回AppBeanAnnotations对象。
 * 
 * @author 陈春松
 */
public final class AppBeanAnnotationsLoader
{
	private AppBeanAnnotationsLoader()
	{
	}

	public static AppBeanAnnotations getAppBeanAnnotaions(Class<?> clazz) throws Exception
	{
		if (!AppBean.class.isAssignableFrom(clazz)) {
			throw new Exception("应用（" + clazz.getCanonicalName() + "）没有实现应用接口（" + AppBean.class.getCanonicalName()
					+ "）！");
		}
		// clazz实现了AppBean接口
		if (Modifier.isAbstract(clazz.getModifiers())) {
			// clazz是一个抽象类
			throw new Exception("应用（" + clazz.getCanonicalName() + "）是一个抽象类，而不是一个实现好的应用类！");
		}
		AppName appName = clazz.getAnnotation(AppName.class);
		if (appName == null) {
			// 不是一个AppBean
			throw new Exception("应用（" + clazz.getCanonicalName() + "）的定义中找不到AppName的注解信息！");
		}
		// 查找BaseClass：第一个具有注解AppBeanBaseType的抽象类。
		Class<?> beanTypeClazz = null;
		beanTypeClazz = clazz;
		do {
			beanTypeClazz = beanTypeClazz.getSuperclass();
		} while ((beanTypeClazz != null) && (beanTypeClazz.getAnnotation(AppBeanBaseType.class) == null));
		if (beanTypeClazz == null) {
			throw new Exception("应用（" + clazz.getName() + "）的继承链中找不到任何一个类定义了AppBeanBaseType注解！");
		}
		if (!Modifier.isAbstract(beanTypeClazz.getModifiers())) {
			throw new Exception("应用（" + clazz.getName() + "）的继承链中定义了AppBeanBaseType的注解的父类（" + beanTypeClazz.getName()
					+ "）必须是一个抽象类！");
		}
		if (!AppBean.class.isAssignableFrom(beanTypeClazz)) {
			throw new Exception("应用（" + clazz.getName() + "）的继承链中定义了AppBeanBaseType的注解的父类（" + beanTypeClazz.getName()
					+ "）必须实现接口" + AppBean.class.getName() + "！");
		}

		// 构造一个AppBean的注解对象。
		AppBeanAnnotations appBeanAnno = new AppBeanAnnotations();
		// 构造AppBean的信息
		AppBeanClassInfo appBeanClassInfo = new AppBeanClassInfo();
		appBeanAnno.setClassInfo(appBeanClassInfo);
		appBeanClassInfo.setType(clazz.getName());
		appBeanClassInfo.setVersion(AppBean.loadVersion(clazz)); // 增加获取版本号

		AppBeanBaseClassInfo appBeanBaseClassInfo = new AppBeanBaseClassInfo();
		appBeanClassInfo.setBaseClass(appBeanBaseClassInfo);

		appBeanBaseClassInfo.setType(beanTypeClazz.getName());
		// 获取Generic class的类型参数信息
		TypeVariable<?>[] typeParams = beanTypeClazz.getTypeParameters();
		if ((typeParams == null) || (typeParams.length == 0)) {
			List<KeyValuePair<String, String>> formalTypeParams = new ArrayList<KeyValuePair<String, String>>(1);
			appBeanBaseClassInfo.setGenericParams(formalTypeParams);
		} else {
			List<Map<String, String>> resolvedTypes = resolveAllTypeParameters(clazz, beanTypeClazz);
			Map<String, String> appBeanTypeMap = resolvedTypes.get(resolvedTypes.size() - 1);

			List<KeyValuePair<String, String>> formalTypeParams = new ArrayList<KeyValuePair<String, String>>(
					typeParams.length);
			String typeName = null;
			String typeValue = null;
			for (TypeVariable<?> typeVar : typeParams) {
				typeName = typeVar.getName();
				typeValue = null;
				typeValue = appBeanTypeMap.get(typeName);
				if (typeValue != null) {
					formalTypeParams.add(new KeyValuePair<String, String>(typeName, typeValue));
				} else {
					throw new Exception("应用（" + clazz.getName() + "）没有为类型参数（" + typeName + "）提供具体的类型！");
				}
			}
			appBeanBaseClassInfo.setGenericParams(formalTypeParams);
		}

		// 开始分析注解
		List<AppBeanAnnotation> annoList = new ArrayList<AppBeanAnnotation>();
		java.lang.annotation.Annotation[] javaAnnotations = null;
		javaAnnotations = clazz.getAnnotations(); // 此类所有注解
		for (java.lang.annotation.Annotation javaAnno : javaAnnotations) {
			annoList.add(createAnnotation(javaAnno, appBeanAnno));
		}
		appBeanAnno.setAnnotations(annoList);

		return appBeanAnno;
	}

	/**
	 * 
	 * @param app
	 * @return 按照定义的顺序，返回appBean的实际参数类型数组。
	 */
	public static Class<?>[] getAppBeanActualTypes(Class<? extends AppBean> appClazz) {
		// 获取应用基本类型。
		Class<?> beanTypeClazz = null;
		beanTypeClazz = appClazz;
		do {
			beanTypeClazz = beanTypeClazz.getSuperclass();
		} while ((beanTypeClazz != null) && (beanTypeClazz.getAnnotation(AppBeanBaseType.class) == null));

		// 获取应用基本类的形参列表。
		TypeVariable<?>[] typeParams = beanTypeClazz.getTypeParameters();
		if ((typeParams == null) || (typeParams.length == 0)) {
			return new Class<?>[0];
		}

		// 获取所有的形参和实参的绑定信息。
		List<Map<String, Class<?>>> resolvedTypes = resolveAllTypeParameters2(appClazz, beanTypeClazz);

		Map<String, Class<?>> appBeanTypeMap = resolvedTypes.get(resolvedTypes.size() - 1);

		Class<?>[] actualTypes = new Class<?>[typeParams.length];

		String typeName = null;
		Class<?> typeValue = null;
		for (int index = 0; index < typeParams.length; index++) {
			typeName = typeParams[index].getName();
			typeValue = null;
			typeValue = appBeanTypeMap.get(typeName);
			actualTypes[index] = typeValue;
		}
		return actualTypes;
	}

	private static AppBeanAnnotation createAnnotation(Annotation javaAnno, AppBeanAnnotations appBeanAnno) throws Exception
	{
		Method[] javaAnnoMethods = null;
		javaAnnoMethods = javaAnno.annotationType().getDeclaredMethods();
		if (javaAnnoMethods.length == 1) {
			if (Annotation[].class.isAssignableFrom(javaAnnoMethods[0].getReturnType())) {
				return createAnnotationArray(javaAnno, appBeanAnno);
			} else {
				return createBasicAnnotation(javaAnno, appBeanAnno);
			}
		} else {
			return createBasicAnnotation(javaAnno, appBeanAnno);
		}
	}

	/**
	 * 分析并创建一个简单的注解信息类
	 * 
	 * @param javaAnno
	 * @return 返回新创建的简单的注解信息类
	 * @throws Exception
	 */
	private static AppBeanAnnotation createBasicAnnotation(java.lang.annotation.Annotation javaAnno, AppBeanAnnotations appBeanAnno) throws Exception
	{
		List<KeyValuePair<String, String>> annoProperties = null;
		KeyValuePair<String, String> annoProperty;
		Method[] javaAnnoMethods = null;
		String annoPropName = null;
		String annoPropValue = null;
		Object tmpValue = null;

		AppBeanAnnotation anno = new AppBeanAnnotation();
		anno.setType(javaAnno.annotationType().getName());

		annoProperties = new ArrayList<KeyValuePair<String, String>>();
		javaAnnoMethods = javaAnno.annotationType().getDeclaredMethods();
		for (Method javaAnnoMethod : javaAnnoMethods) {
			annoPropName = javaAnnoMethod.getName();
			tmpValue = javaAnnoMethod.invoke(javaAnno);
			if (tmpValue == null) {
				annoPropValue = "";
			} else {
				annoPropValue = tmpValue.toString();
			}
			annoProperty = new KeyValuePair<String, String>(annoPropName, annoPropValue);
			annoProperties.add(annoProperty);
		}
		// 将category与name放入AppBeanAnnotations
		if (anno.getType().equals(AppName.class.getName())) {
			for (KeyValuePair<String, String> ap : annoProperties) {
				if (ap.getKey().equals("category")) {
					appBeanAnno.setAppCategory(ap.getValue());
					continue;
				}

				if (ap.getKey().equals("name")) {
					appBeanAnno.setAppName(ap.getValue());
					continue;
				}
			}
		}
		anno.setFields(annoProperties);
		return anno;
	}

	/**
	 * 分析并创建一个数组类型的注解信息类
	 * 
	 * @param javaAnno
	 * @return 返回新创建的数组类型的注解信息类
	 * @throws Exception
	 */
	private static AppBeanAnnotation createAnnotationArray(java.lang.annotation.Annotation javaAnno, AppBeanAnnotations appBeanAnno) throws Exception
	{
		AppBeanAnnotation[] subAnnoArray = null;
		Method javaAnnoMethod = null;
		java.lang.annotation.Annotation subJavaAnnoArray[] = null;

		AppBeanAnnotation anno = new AppBeanAnnotation();
		anno.setType(javaAnno.annotationType().getName());

		javaAnnoMethod = javaAnno.annotationType().getDeclaredMethods()[0];
		subJavaAnnoArray = (java.lang.annotation.Annotation[]) javaAnnoMethod.invoke(javaAnno);
		subAnnoArray = new AppBeanAnnotation[subJavaAnnoArray.length];
		for (int index = 0; index < subJavaAnnoArray.length; index++) {
			subAnnoArray[index] = createAnnotation(subJavaAnnoArray[index], appBeanAnno);
		}
		anno.setChildAnnotations(subAnnoArray);
		return anno;
	}

	/**
	 * 对一个找出所有参数类中每个类型参数对应的类型实参。 该方法假设所有的参数都绑定到了具体的java类。
	 * 
	 * @param parameterizedClass 参数化类；从genericClass开始定义所有类型参数都将被找出来。
	 * @param baseGenericClass 模板类；只有从该类开始定义的类型参数才会被找出类。
	 * @return 返回一个列表对象，该列表对象中的每一个元素都是一个map，每一个map包含两类数据 1、提供类型参数的参数化类，它的key是"$ParameterizedClass" 2、该参数化类提供的所有类型实参。每个实参的key是类型的形参；值就是类型的实参。
	 */
	private static List<Map<String, String>> resolveAllTypeParameters(Class<?> parameterizedClass, Class<?> baseGenericClass)
	{
		Class<?> parentClazz = parameterizedClass;
		Type genericSuperType = null;
		TypeVariable<?>[] typeParams = null;
		Type[] typeArguments = null;
		List<Map<String, String>> resolvedTypes = new ArrayList<Map<String, String>>(5);
		Map<String, String> tmpMap = null;
		String typeArgument = null;
		Class<?> curClazz = null;
		Map<String, String> lastResolvedTypeMap = null;
		while ((parentClazz != baseGenericClass) && (parentClazz != null)) {
			curClazz = parentClazz;
			genericSuperType = curClazz.getGenericSuperclass();
			parentClazz = curClazz.getSuperclass();
			if (parentClazz != null) {
				typeParams = parentClazz.getTypeParameters();
				if (genericSuperType instanceof ParameterizedType) {
					typeArguments = ((ParameterizedType) genericSuperType).getActualTypeArguments();
					if ((typeParams == null) || (typeParams.length == 0)) {
						continue;
					}
					tmpMap = new HashMap<String, String>(5);
					for (int index = 0; index < typeParams.length; index++) {
						typeArgument = resolveTypeParameter(typeArguments[index], lastResolvedTypeMap, tmpMap);
						if (typeArgument != null) {
							tmpMap.put(typeParams[index].getName(), typeArgument);
						}
					}
					tmpMap.put("$ParameterizedClass", curClazz.getName());
					lastResolvedTypeMap = tmpMap;
					resolvedTypes.add(tmpMap);
				}
			}
		}
		return resolvedTypes;

	}

	/**
	 * 解析一个类型实参
	 * 
	 * @param type 类型实参
	 * @param curResolvedTypeMap 类型形参的上下文；当在一个模板类中，后面的形参用到了前面的形参时，该上下文将会被用到。
	 * @return 返回类型实参。
	 */
	private static String resolveTypeParameter(Type type, Map<String, String> lastResolvedTypeMap, Map<String, String> curResolvedTypeMap)
	{
		if (type instanceof Class<?>) {
			return ((Class<?>) type).getCanonicalName();
		} else if (type instanceof ParameterizedType) {
			StringBuffer buff = new StringBuffer(
					((Class<?>) ((ParameterizedType) type).getRawType()).getCanonicalName());
			buff.append("<");
			Type[] childTypes = ((ParameterizedType) type).getActualTypeArguments();
			for (int index = 0; index < childTypes.length; index++) {
				if (index > 0) {
					buff.append(",");
				}
				buff.append(resolveTypeParameter(childTypes[index], lastResolvedTypeMap, curResolvedTypeMap));
			}
			buff.append(">");
			return buff.toString();
		} else if (type instanceof TypeVariable<?>) {
			String typeName = curResolvedTypeMap.get(((TypeVariable<?>) type).getName());
			if (typeName == null) {
				return lastResolvedTypeMap.get(((TypeVariable<?>) type).getName());
			} else {
				return typeName;
			}
		} else {
			throw new IllegalArgumentException(type.getClass().getCanonicalName() + " is not supported!");
		}
	}

	/**
	 * 对一个找出所有参数类中每个类型参数对应的类型实参。 该方法假设所有的参数都绑定到了具体的java类。
	 * 
	 * @param parameterizedClass 参数化类；从genericClass开始定义所有类型参数都将被找出来。
	 * @param baseGenericClass 模板类；只有从该类开始定义的类型参数才会被找出类。
	 * @return 返回一个列表对象，该列表对象中的每一个元素都是一个map，每一个map包含两类数据 1、提供类型参数的参数化类，它的key是"$ParameterizedClass" 2、该参数化类提供的所有类型实参。每个实参的key是类型的形参；值就是类型的实参。
	 */
	private static List<Map<String, Class<?>>> resolveAllTypeParameters2(Class<?> parameterizedClass, Class<?> baseGenericClass)
	{
		Class<?> parentClazz = parameterizedClass;
		Type genericSuperType = null;
		TypeVariable<?>[] typeParams = null;
		Type[] typeArguments = null;
		List<Map<String, Class<?>>> resolvedTypes = new ArrayList<Map<String, Class<?>>>(5);
		Map<String, Class<?>> tmpMap = null;
		Class<?> typeArgument = null;
		Class<?> curClazz = null;
		Map<String, Class<?>> lastResolvedTypeMap = null;
		while ((parentClazz != baseGenericClass) && (parentClazz != null)) {
			curClazz = parentClazz;
			genericSuperType = curClazz.getGenericSuperclass();
			parentClazz = curClazz.getSuperclass();
			if (parentClazz != null) {
				typeParams = parentClazz.getTypeParameters();
				if (genericSuperType instanceof ParameterizedType) {
					typeArguments = ((ParameterizedType) genericSuperType).getActualTypeArguments();
					if ((typeParams == null) || (typeParams.length == 0)) {
						continue;
					}
					tmpMap = new HashMap<String, Class<?>>(5);
					for (int index = 0; index < typeParams.length; index++) {
						typeArgument = resolveTypeParameter2(typeArguments[index], lastResolvedTypeMap, tmpMap);
						if (typeArgument != null) {
							tmpMap.put(typeParams[index].getName(), typeArgument);
						}
					}
					tmpMap.put("$ParameterizedClass", curClazz);
					lastResolvedTypeMap = tmpMap;
					resolvedTypes.add(tmpMap);
				}
			}
		}
		return resolvedTypes;

	}

	/**
	 * 解析一个类型实参
	 * 
	 * @param type 类型实参
	 * @param curResolvedTypeMap 类型形参的上下文；当在一个模板类中，后面的形参用到了前面的形参时，该上下文将会被用到。
	 * @return 返回类型实参。
	 */
	private static Class<?> resolveTypeParameter2(Type type, Map<String, Class<?>> lastResolvedTypeMap, Map<String, Class<?>> curResolvedTypeMap)
	{
		if (type instanceof Class<?>) {
			return ((Class<?>) type);
		} else if (type instanceof ParameterizedType) {
			return (Class<?>) ((ParameterizedType) type).getRawType();
		} else if (type instanceof TypeVariable<?>) {
			Class<?> clazz = curResolvedTypeMap.get(((TypeVariable<?>) type).getName());
			if (clazz == null) {
				return lastResolvedTypeMap.get(((TypeVariable<?>) type).getName());
			} else {
				return clazz;
			}
		} else {
			throw new IllegalArgumentException(type.getClass().getCanonicalName() + " is not supported!");
		}
	}

}
