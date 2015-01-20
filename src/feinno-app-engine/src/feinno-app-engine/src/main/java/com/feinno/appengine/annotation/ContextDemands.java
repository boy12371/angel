/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-7
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.annotation;

/**
 * 
 * 标记一个AppBean对AppContext的数据需求
 * 
 * 某些AppBean对Context有默认的需求, 这种情况下不用可以描述
 * 
 * 当标记了ContextDemands参数, Proxy或Client(Rpc)在调用此Bean时
 * 需要将ContextData填到ContextData区域 
 *   
 * 当PROXY需要处理不同类型或不同方式的Context时,
 * 需要使用way和params来标记ContextData的填充方式
 *  
 * @author 高磊 gaolei@feinno.com
 */
public @interface ContextDemands
{
	/**
	 * 
	 * 在Proxy上进行何种操作
	 * @return
	 */
	int value() default 0;
}
