package com.feinno.logging.appender;

import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

import com.feinno.logging.LogEvent;
import com.feinno.logging.common.FireEventQueue;
import com.feinno.util.Action;

/**
 * 此类是一个用于管理Appender集合的类<br>
 * 其中的逻辑是创建保存日志信息的队列，并在需要的时候调用appender进行处理
 * 
 * @author Lv.Mingwei
 * 
 */
public class AppenderAttachable {

	/**
	 * 保存日志信息的队列
	 */
	private FireEventQueue<LogEvent> queue;

	/**
	 * 队列的锁,为了保证同时只有一个人对队列进行初始化或进行关闭
	 */
	private Object synQueueObject = new Object();

	/**
	 * appender的锁,为了保证同时只有一个人对Appender进行初始化或进行关闭
	 */
	private Object synAppenderObject = new Object();

	/**
	 * 一个用于保存日志配置文件中要求的Appender的集合,因为会涉及到配置文件的变化，所以这里使用了线程安全的读写分离的集合
	 */
	final private CopyOnWriteArrayList<Appender> appenderList = new CopyOnWriteArrayList<Appender>();

	/**
	 * 添加一个appender进来
	 * 
	 * @param newAppender
	 */
	public void addAppender(Appender newAppender) {
		if (newAppender == null) {
			throw new IllegalArgumentException("Null argument disallowed");
		}
		synchronized (synAppenderObject) {
			appenderList.addIfAbsent(newAppender);
		}
	}

	/**
	 * 清理所有的appender
	 * 
	 * @param newAppender
	 */
	public void removeAllAppender() {
		// 在清理Appender时需要获得Appender的锁，防止另一个线程在执行Appender中的doAppender方法，而此处却要对Appender中的资源进行释放
		synchronized (synAppenderObject) {
			if (appenderList != null) {
				for (Appender appender : appenderList) {
					if (appender != null) {
						appender.destroy();
					}
				}
				appenderList.clear();
			}
		}
	}
	
	public CopyOnWriteArrayList<Appender> getAppenders(){
		return appenderList;
	}
	
	/**
	 * 初始化一个具有缓存的队列，此队列将在缓存满时或指定时间后激活Appender进行处理
	 * 
	 * @param cacheSize
	 * @param intervalTime
	 */
	public void initCacheQueue(int cacheSize, long intervalTime) {
		synchronized (synQueueObject) {
			// 一个具有缓存功能的Q
			queue = FireEventQueue.newCacheFireEventQueue(cacheSize, intervalTime, new Action<Queue<LogEvent>>() {
				@Override
				public void run(Queue<LogEvent> event) {
					synchronized (synAppenderObject) {
						for (Appender appender : appenderList) {
							try {
								appender.doAppend(event);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
		}
	}

	/**
	 * 初始化一个可以快速响应入列事件，并及时调用Appender进行处理的队列，<br>
	 * 这个队列的缓存目的是为了防止内存溢出
	 * 
	 * @param cacheSize
	 */
	public void initSyncQueue() {
		synchronized (synQueueObject) {
			// 一个普通的Q，超过Q的限制值的内容直接抛弃
			queue = FireEventQueue.newSyncFireEventQueue(new Action<LogEvent>() {
				@Override
				public void run(LogEvent event) {

					synchronized (synAppenderObject) {
						for (Appender appender : appenderList) {
							try {
								appender.doAppend(event);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
		}

	}

	/**
	 * 清理队列信息，使队列做完手里的工作就停掉
	 */
	public void closeQueue() {
		// 这里清理队列和初始化队列不能同时进行，否则会出错
		synchronized (synQueueObject) {
			if (queue != null) {
				queue.close();
			}
		}
	}

	/**
	 * 添加一个日志信息到缓存队列
	 */
	public void appendLoopOnAppenders(LogEvent event) {
		queue.add(event);
	}

}
