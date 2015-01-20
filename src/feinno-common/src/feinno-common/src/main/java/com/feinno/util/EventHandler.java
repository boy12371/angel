/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Apr 7, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.util;

/**
 * 事件处理器 
 * 
 * @author 高磊 gaolei@feinno.com
 */
public interface EventHandler<E>
{
	void run(Object sender, E e);
}
