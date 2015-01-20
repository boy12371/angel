/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Sep 3, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.route.gray.conds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.route.gray.funcs.FuncBase;
import com.feinno.util.StringUtils;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class FuncCond implements Cond
{
	private static Logger LOGGER = LoggerFactory.getLogger(FuncCond.class);
	
	private FuncBase func;
	private String fieldName;

	public FuncCond(String fieldName, FuncBase func)
	{
		this.func = func;
		this.fieldName = fieldName;
	}
	
	@Override
	public boolean apply(AppContext ctx)
	{
		String fieldValue = null;
		
		//
		// fieldName == null 说明此灰度条件与fieldValue无关，应该继续计算，例如random
		// 否则当获取不到fieldValue时，直接返回false
		if (fieldName != null) {
			Object obj = ctx.getNamedValue(fieldName);
			fieldValue = obj == null ? null : obj.toString();
			LOGGER.debug("func field {} value = {}", fieldName, fieldValue);
			if (StringUtils.isNullOrEmpty(fieldValue))
				return false;	// 未命中
		}

		try {
			return func.apply(fieldValue);
		} catch (Exception ex) {
			LOGGER.error("when apply gray func meet exception.", ex);
			return false;
		}
	}
}
