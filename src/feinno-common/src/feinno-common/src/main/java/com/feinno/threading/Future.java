package com.feinno.threading;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.util.Event;
import com.feinno.util.EventHandler;

/**
 * 
 * 用以帮助异步调用转为同步调用的工具类<br>
 * 调用此类的await方法后，它会阻塞当前线程，等待处理线程执行完毕，由处理线程将结果返回到当前线程，来解除阻塞.
 * 
 * Future支持Session的Relay机制
 * @author Lv.Mingwei *
 * @param <V>
 */
public class Future<V>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Future.class);
	
	/** 默认超时时间为90秒 */
	public static final int TIMEOUT = 90 * 1000;
	
	/** 是否已经完成 */
	private boolean done;
	
	/** Future的执行结果 */
	private V value;
	
	/** 用户可根据需要创建事件完成后监听 */
	private Event<V> listener;

	/** 内部同步控制器 */
	private final Sync sync;

	/** 是否正在等待 */
	private AtomicBoolean waiting;
	
	/** 执行回调时的执行器 */
	private Executor executor;
	
	/** 默认的超时时间: 90秒 */
	private int timeoutMs = TIMEOUT;
	
	/** 当用于SessionExecutor接续任务, 通过RelaySession执行接续功能 */ 
	private SessionTask sessionTask;

	public Future()
	{
		value = null;
		done = false;
		waiting = new AtomicBoolean(false);
		sync = new Sync();
		listener = new Event<V>(this);
	}

	/**
	 * 
	 * 获取得到的执行器
	 * @return
	 */
	public Executor getExecutor()
	{
		return executor;
	}

	/**
	 * 
	 * 设置一个执行器, 当设置了一个不为空的执行器后, 会使用这个执行器进行回调操作 
	 * @param executor
	 */
	public void setExecutor(Executor executor)
	{
		this.executor = executor;
	}
	
	public SessionTask getSessionTask()
	{
		return sessionTask;
	}

	/**
	 * 
	 * 是否完成
	 * 
	 * @return
	 */
	public boolean isDone()
	{
		return done;
	}

	/**
	 * 
	 * 是否处于等待中
	 * 
	 * @return
	 */
	public boolean isWaiting()
	{
		return waiting.get();
	}

	/**
	 * 
	 * 获得执行结果，请注意，当结果未获取之前，此方法会导致当前线程阻塞
	 * 
	 * @return
	 * @throws TimeoutException 
	 */
	public V getValue()
	{
		if (done) {
			return value;
		} 
		
		if (await()) {
			return value;
		} else {
			throw new RuntimeException("Timeout"); // 临时
		}
	}
	
	/**
	 * 
	 * 设置超时的毫秒数
	 * @param milliseconds
	 */
	public void setTimeout(int milliseconds)
	{
		timeoutMs = milliseconds;		
	}
	
	/**
	 * 
	 * 返回超时时间(毫秒)
	 * @return
	 */
	public int getTimeout()
	{
		return timeoutMs;
	}

	/**
	 * 
	 * 阻塞当前线程直到拿到结果, 使用默认超时时间
	 */
	public boolean await()
	{
		return await(timeoutMs);
	}

	/**
	 * 
	 * 阻塞当前线程，等待milliseconds毫秒，如果在时间内完成
	 * 
	 * @param milliseconds 超时毫秒数, -1表示无限等待
	 * @return 在时间内拿到结果返回true, 否则返回false
	 */
	public boolean await(long milliseconds)
	{
		if (done) {
			return true;
		}
		//
		// 如果已经等待一次了, 则仅等待一次
		if (waiting.compareAndSet(false, true)) {
			if (milliseconds == -1) {
				try {
					sync.acquireSharedInterruptibly(1);
					return true;
				} catch (InterruptedException e) {
					throw new RuntimeException(e); // TODO: need isCancel?
				} finally {
					waiting.set(false);
				}
			} else {
				try {
					return sync.tryAcquireSharedNanos(1, TimeUnit.MILLISECONDS.toNanos(milliseconds));
				} catch (InterruptedException e) {
					throw new RuntimeException(e); // TODO: need isCancel?
				} finally {
					waiting.set(false);
				}
			}
		} else {
			throw new IllegalStateException("Already waiting...");
		}
	}

	/**
	 * 
	 * 添加完成的listener，如果此Future已经完成了运行，则直接执行此listener
	 * 
	 * @param listener
	 */
	public void addListener(EventHandler<V> handler)
	{
		boolean runNow = false;
		synchronized (this) {
			if (done) {
				//
				// 因为handler只会执行一次, 所以如果已经做完了, 就直接触发handler
				runNow = true;
			} else {
				listener.addListener(handler);
			}
		}
		
		if (runNow) {
			handler.run(this, value);
		}
	}
	
	/**
	 * 
	 * 当回调的时候尝试在当前线程池(SessionExecutor)上续接此操作 
	 * @return
	 */
	public boolean relaySessionTask()
	{
		ThreadContext currentThread = ThreadContext.getCurrent();
		SessionTask task = (SessionTask)currentThread.getNamedContext(ThreadContextName.SESSION_TASK);
		if (task != null) {
			this.sessionTask = task;
			this.executor = (Executor)currentThread.getNamedContext(ThreadContextName.EXECUTOR);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 设置结果, 如果有线程处于wait状态, 则使用当前线程解锁wait并执行回调<br>
	 * 当设置了回调线程池时, 使用回调线程池进行回调<br>
	 * 回调的FutureCallback扩展自Runnable, 能够允许在executor中执行更自由的操作<br>
	 * 
	 * @param v
	 */
	public void complete(final V result)
 {
		//
		// 标记为完成, 多次调用的话, 只有第一次有效,
		final EventHandler<V>[] handlers;
		synchronized (this) {
			if (!done) {
				done = true;
				value = result;
				handlers = listener.getAllHandlers();

				//
				// 没有需要执行的直接在锁中释放等待, 避免并发问题
				if (handlers == null || handlers.length == 0) {
					sync.releaseShared(1);
					return;
				}
			} else {
				throw new IllegalStateException("Already complete");
			}
		}

		//
		// 不在等待中,
		sync.releaseShared(1); // TODO: 释放貌似没意义, 能否不用waiting, 直接从sync中判断等待状态

		if (executor == null) {
			//
			// 没有设置线程池, 使用当前线程池执行回调
			// TODO, 在很多经验中, 直接使用IO线程处理回调会有问题, 建议增加默认应用线程池
			runHandlers(handlers);
		} else {
			//
			// 使用设置好的线程池, FutureCallback接口会将Future额外暴露给Executor便于实现一些定制的逻辑
			executor.execute(new FutureCallback() {
				@Override
				public void run() {
					runHandlers(handlers);
				}

				@Override
				public Future<?> getFuture() {
					return Future.this;
				}
			});
		}
	}


	/**
	 * 
	 * 执行Handlers, 
	 * TODO: 现在会吞掉异常, 这种模式是否合理? 
	 * @param handlers
	 */
	private void runHandlers(EventHandler<V>[] handlers)
	{
		for (EventHandler<V> handler: handlers) {
			try {
				handler.run(this, value);
			} catch (Exception ex) {
				LOGGER.error("catch error ", ex);
			} catch (Error ex) {
				LOGGER.error("catch serius error", ex);
				throw ex;
			}
		}		
	}

	/**
	 * 同步控制器，目的为实现阻塞，继承自AQS
	 * 
	 * @author Lv.Mingwei
	 */
	private final class Sync extends AbstractQueuedSynchronizer
	{
		private static final long serialVersionUID = -990419035911612884L;

		/** 停止状态 */
		private static final int STOP = 0;

		/** 正在运行中 */
		private static final int RUNNING = 1;

		/**
		 * 初始化为未运行状态
		 */
		Sync()
		{
			setState(RUNNING);
		}

		/**
		 * AQS的惯用法，此处标识当AQS尝试获取共享锁时，是否获取成功，如果当前不处于运行状态，则可以获取
		 */
		@Override
		public int tryAcquireShared(int acquires)
		{
			return getState() != RUNNING ? 1 : -1;
		}

		/**
		 * AQS的惯用法，此处标识为释放
		 */
		@Override
		public boolean tryReleaseShared(int releases)
		{
			for (;;) {
				int c = getState();
				if (c != RUNNING)
					return false;
				if (compareAndSetState(c, STOP)) {
					return true;
				}
			}
		}
	}
}
