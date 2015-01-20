/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine;

import com.feinno.appengine.configuration.AppBeanAnnotationsLoader;
import com.feinno.util.StringUtils;

/**
 * 带有AppContext的AppBean,AppContext会包含在整条调用链上
 * tx的生命周期
 * 1. 通过bean的receiveRequest创建
 * 2. 通过loadContext方法加载context,
 * 3.  
 * 
 * @author 高磊 gaolei@feinno.com
 */
public abstract class AppBeanWithContext<C extends AppContext> extends AppBean
{
	private Class<C> contextClazz;
	
	@SuppressWarnings("unchecked")
	protected AppBeanWithContext(int genericParamOrder)
	{
		//
		// 考虑多重集成性，在某些情况下，需要在应用基础类型（RemoteAppBean,SipcAppBean)与最终的应用之间加入一级或多级抽象类；
		// 形成如下继承链：TestGroupRemoteApp extends TestRemoteApp extends RemoteAppBean<argu,result,context>。在这种情况下
		// 直接调用GenericsUtils.getSuperClassGenricTypes(this.class)，找到的泛型就不是应用基础类型的泛型参数。
		// 在此处调用另一个方法来查找继承链上所有的形参来解决该问题。
		Class<?>[] aa = AppBeanAnnotationsLoader.getAppBeanActualTypes(this.getClass());
		contextClazz = (Class<C>)aa[genericParamOrder];
	}
	
	@SuppressWarnings("unchecked")
	protected void loadContext(AppTxWithContext<C> tx)
	{
		String contextUri = tx.extractContextUri();
		byte[] contextData = tx.extractContextData();
		if (LOGGER.isInfoEnabled())
			LOGGER.info("bean context with {}/{}", contextUri, StringUtils.formatBuffer(contextData));
		
		if (!StringUtils.isNullOrEmpty(contextUri)) {
			try {
				C c = (C)AppContext.create(contextClazz, tx);
				c.decode(contextUri, contextData);
				tx.setContext(c);
			} catch (Exception e) {
				LOGGER.error("decode context failed", e);
				tx.setError(new AppEngineException(AppEngineException.FORMAT_FAILED, "decode context failed", e));
				tx.terminate();
			}
		}
	}
}
