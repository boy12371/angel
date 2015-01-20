/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-26
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.ha;


/**
 * 所有通过HA方式启动的服务, 需要实现ServiceComponent接口
 * 
 * @author 高磊 gaolei@feinno.com
 */
public interface ServiceComponent
{
	/**
	 * 
	 * 启动服务, 
	 * 初始化资源, 打开监听端口
	 */
	void start() throws Exception;
	
	/**
	 * 
	 * 停止服务, 
	 * 释放资源, 清理数据, 关闭监听端口
	 */
	void stop() throws Exception;
}
