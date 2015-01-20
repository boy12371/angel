///*
// * FAE, Feinno App Engine
// *  
// * Create by gaolei 2012-2-21
// * 
// * Copyright (c) 2012 北京新媒传信科技有限公司
// */
//package com.feinno.threading;
//
//import java.util.concurrent.Executor;
//
///**
// * 固定大小的会话线程池
// * 
// * @author 高磊 gaolei@feinno.com
// */
//public class FixedSessionExecutor extends ObservableExecutor
//{
//	private static class WorkItem implements Runnable 
//	{
//		private Runnable command;
//		private SessionContext ctx;
//		
//		public WorkItem(Runnable command, SessionContext ctx)
//		{
//			this.command = command;			
//			this.ctx = ctx;
//		}
//
//		@Override
//		public void run()
//		{
//			if (ctx == null) {
//				ctx = SessionContext.newSession();
//			}
//			ThreadContext.getCurrent().setSession(ctx);
//			command.run();
//		}
//	}
//	
//	private int limit;
//	
//	public FixedSessionExecutor(String name, Executor executor, int size, int limit)
//	{
//		super(name, executor);
//		this.limit = limit;
//	}	
//	
//	/**
//	 * 创建Session并运行, 这里存在线程数的限制
//	 */
//	@Override
//	public void execute(Runnable command)
//	{
//		int c = getWorkerCounter().getConcurrent();
//		if (c > limit) {
//			throw new ExecutorBusyException("ExecutorBusy", this.getName());
//		}
//		
//		WorkItem work = new WorkItem(command, null);
//		super.execute(work);
//	}
//	
//	/**
//	 * 
//	 * Callback Session的运行，无论如何保证能加入线程池队列
//	 * @param command
//	 */
//	public void executeCallback(Runnable command)
//	{
//		SessionContext ctx = ThreadContext.getCurrent().getSession();
//		if (ctx == null) {
//			throw new IllegalArgumentException("Not in a session");
//		}
//		
//		WorkItem work = new WorkItem(command, ctx);
//		super.execute(work);
//	}
//}
