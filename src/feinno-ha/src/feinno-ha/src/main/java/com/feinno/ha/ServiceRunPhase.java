/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Aug 18, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.ha;

/**
 * 
 * <b>描述: </b>服务运行状态<br>
 * 一般情况下，服务初始化为ADHOC状态
 * <p>
 * <b>功能: </b>用于描述服务运行状态的枚举类型
 * <p>
 * <b>用法: </b>正常枚举的使用
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 * 
 */
public enum ServiceRunPhase
{
	/** 未知情况，一律按照本地测试进行 */
	CHAOS,
	
	/** 启动中，一般服务初始化 */
	LOADING,
	
	/** 设置状态，一般用于检测依赖领域 */		
	SETUP,
	
	/** 正常运行时 */
	RUNNING,
}
