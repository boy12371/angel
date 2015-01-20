/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine;

/**
 * AppBeanHandler的运行模式
 * 
 * @author 高磊 gaolei@feinno.com
 */
public enum AppBeanHandlerMode
{
	/**
	 * 永远都会被运行, 不管有没有结束
	 * 不适用于AfterHandler
	 */
	RUN_ALWAYS(true, false, true, true, true),
	
	/**
	 * 只要tx没有terminated就继续运行, 
	 * 适用于Bean或一般的BeforeHandler, 
	 * 不适用于AfterHandler 
	 * 由Handler决定是否继续运行
	 */
	RUN_UNTIL_TERMINATED(true, false, false, false, false),
	
	/**
	 * 当tx被终止后运行, 不论对错, 适用于一般的AfterHandler
	 * 不适用于BeforeHandler 
	 */
	RUN_AFTER_TERMINATED(false, true, true, true, true),
	
	/**
	 * 当tx被错误终止后, 运行
	 * 只适用于AfterHandler
	 */
	RUN_ON_FAILED(false, true, true, true, false),
	
	/**
	 * 当tx被正确终止后, 运行
	 * 只适用于AfterHandler
	 */
	RUN_ON_SUCCESSED(false, true, true, false, true),
	;
	
	private boolean usedWithBefore;
	private boolean usedWithAfter;
	private boolean runWhenTerminated;
	private boolean runWithFailed;
	private boolean runWithSuccess;
	
	AppBeanHandlerMode(boolean usedWithBefore, boolean usedWithAfter, boolean runWhenTerminated, boolean runWithFailed, boolean runWithSuccess)
	{
		this.usedWithBefore = usedWithBefore;
		this.usedWithAfter = usedWithAfter;
		this.runWhenTerminated = runWhenTerminated;
		this.runWithFailed = runWithFailed;
		this.runWithSuccess = runWithSuccess;
	}
	
	public boolean canUsedWithBefore()
	{
		return usedWithBefore;
	}
	
	public boolean canUsedWithAfter()
	{
		return usedWithAfter;
	}
	
	public boolean canRun(boolean isTerminated, boolean isFailed)
	{
		if (!isTerminated) {
			return true;
		} else {
			if (runWhenTerminated) {
				return (runWithFailed && isFailed) || (runWithSuccess && !isFailed);
			} else {
				return false;
			}
		}
	}
}
