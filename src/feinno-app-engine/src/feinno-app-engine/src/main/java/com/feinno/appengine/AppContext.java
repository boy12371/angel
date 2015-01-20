/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft 2011-2-1
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine;

import java.io.IOException;

import com.feinno.appengine.context.NullContext;

/**
 * 所有FAE中的Context基类,AppContext包含了应用定位
 * AppContext有统计功能
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class AppContext
{

	private AppTxWithContext<?> tx;
	
	/**
	 * 
	 * 获取ContextUri
	 * @return
	 */
	public abstract ContextUri getContextUri();
	
	/**
	 * 
	 * 从数据中解码ContextUri及附加数据
	 * @param uri
	 * @param datas
	 * @throws IOException 
	 */
	public abstract void decode(String uri, byte[] datas) throws Exception;
	
	/**
	 * 
	 * 按需编码
	 * @param demand
	 * @return
	 */
	public abstract byte[] encode(int demands) throws IOException;

	
	/**
	 * 
	 * 获取命名的值
	 * 用于灰度发布的场景
	 * 
	 * @param id
	 * @return
	 */
	public abstract  Object getNamedValue(String id);
	
	/**
	 * 
	 * 设置命名的值
	 */
	public abstract void putNamedValue(String id, Object value);

	/**
	 * 
	 * 获取当前AppContext所属的Site
	 * 返回空表示默认机房
	 */
	public abstract String getSiteName();
	
	/**
	 * 
	 * 创建一个特定类型的AppBean
	 * @param clazz
	 * @param uri
	 * @param datas
	 * @return
	 * @throws IOException 
	 */
	public static AppContext create(Class<? extends AppContext> clazz, AppTxWithContext<?> tx)
	{
		if (clazz.equals(NullContext.class)) {
			return NullContext.INSTANCE;
		} else {
			AppContext ctx;
			try {
				ctx = clazz.newInstance();
				ctx.tx = tx;
			} catch (InstantiationException e) {
				throw new IllegalArgumentException("Failed to initialize AppContext: " + clazz, e);
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException("bad AppContext Type" + clazz.toString(), e);
			}
			return ctx;
		}
	}
	
	public AppTxWithContext<?> getTx()
	{
		return tx;
	}
}
