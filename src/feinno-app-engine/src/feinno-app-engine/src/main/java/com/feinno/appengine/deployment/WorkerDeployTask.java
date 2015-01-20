///*
// * FAE, Feinno App Engine
// *  
// * Create by gaolei 2012-9-4
// * 
// * Copyright (c) 2012 北京新媒传信科技有限公司
// */
//package com.feinno.appengine.deployment;
//
//import com.feinno.ha.deployment.HATask;
//import com.feinno.threading.Future;
//
///**
// * 
// * 针对一个Worker的部署任务
// * 
// * 部署的过程
// * 1. beans - 客户端选中的内容
// * 2. 判断beans中的每个bean的状态
// * 	(正常): 运行正常
// * 	(更新Worker部署): 更新Worker部署
// *  (包变更更新): 更新Worker部署
// * 3. 
// * for (bean in beans)
// *      bean.saveDeloyment()
// * 3.
// *  
// * @author 高磊 gaolei@feinno.com
// */
//public class WorkerDeployTask
//{
//	private String serviceName;
//	private String serverName;
//	
//	private HATask task;
//	private Exception ex;
//	private List<AppBeanDeployment> beans;
//	private List<HADeploymentOne> deployments;
//	
//	public WorkerDeployTask(String serviceName, String serverName)
//	{
//		
//	}
//	
//	public String getIdentity()
//	{
//		return serverName + serviceName;
//	}
//	
//	/**
//	 * 是否已经完成了部署(Worker已经连接上了Zookeeper)
//	 */
//	public boolean isDone()
//	{
//		return false;
//	}
//	
//	/**
//	 * 
//	 * 由监控线程调用,用于判断部署是否完成,是否完成有两个条件
//	 * 1.HATask完成
//	 * 2.Zookeeper中节点已经添加完毕
//	 * 
//	 * 当任务确定完成后, 调用自身的complete方法, 完成部署
//	 */
//	public void check()
//	{
//		throw new UnsupportedOperationException("没实现呢");
//	}
//}
