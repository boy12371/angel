/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine;

import com.feinno.util.Action;

/**
 * 所有能够处理AppTx的对象, 都会继承AppTxHandler, 
 * 一连串的AppTxHandler能够被放在一个响应链模式, 并链在一起处理
 * 
 * @author 高磊 gaolei@feinno.com
 */
interface AppTxHandler
{
	void processTx(AppTx tx, Action<Exception> callback);
}
