/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.http;

import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.context.NullContext;

@AppName(category="sample", name="HttpFoo")
@HttpPrefix("/foo.do")
public class SampleHttpAppBean extends HttpAppBean<NullContext>
{
	/*
	 * @see com.feinno.appengine.AppBean#setup()
	 */
	/**
	 * {在这里补充功能说明}
	 * @throws Exception
	 */
	@Override
	public void setup() throws Exception
	{
		// 执行自安装过程
	}

	/*
	 * @see com.feinno.appengine.AppBean#load()
	 */
	/**
	 * {在这里补充功能说明}
	 * @throws Exception
	 */
	@Override
	public void load() throws Exception
	{
		// 执行配置加载
	}

	/*
	 * @see com.feinno.appengine.AppBean#unload()
	 */
	/**
	 * {在这里补充功能说明}
	 * @throws Throwable
	 */
	@Override
	public void unload() throws Exception
	{
		// 执行应用停止前的操作
	}	
	/*
	 * @see com.feinno.appengine.http.HttpAppBean#process(com.feinno.appengine.http.HttpAppTx)
	 */
	/**
	 * {在这里补充功能说明}
	 * @param tx
	 * @throws Throwable
	 */
	@Override
	public void process(HttpAppTx<NullContext> tx) throws Exception
	{
		/**
		 * 在这里添加业务逻辑
		 */
		
		/**
 		 * 处理结束后必须执行tx.end()语句
		 */
		tx.end();
	}
}
