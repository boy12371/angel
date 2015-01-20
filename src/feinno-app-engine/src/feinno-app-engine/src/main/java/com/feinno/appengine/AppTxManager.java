/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Dec 4, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.util.EventHandler;
import com.feinno.util.Func;
import com.feinno.util.container.SessionPool;

/**
 * 管理所有的AppTx
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppTxManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppTxManager.class);

	public static final AppTxManager INSTANCE = new AppTxManager();

	public static final int MAX_CAPACITY = 1024 * 128;
	public static final int TIMEOUT = 90 * 1000;

	private SessionPool<AppTx> txs;
	private Thread thread;

	private AppTxManager() {
		txs = new SessionPool<AppTx>(MAX_CAPACITY);
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				monitorProc();
			}
		});

		thread.setDaemon(true);
		thread.setName("AppTxManager.monitorProc");
		thread.start();
	}

	public void addTx(final AppTx tx) {
		int id = txs.add(tx);
		if (id < 0) {
			throw new IllegalStateException("TooManyTx");
		}
		tx.setId(id);
		tx.getTerminatedEvent().addListener(new EventHandler<Throwable>() {
			@Override
			public void run(Object sender, Throwable e) {
				if (txs.remove(tx.getId()) != null) {
					tx.getCurrentApp().releaseTx(tx);
				}
			}
		});

	}

	public boolean removeTx(AppTx tx) {
		return txs.remove(tx.getId()) != null;
	}

	private void monitorProc() {
		while (true) {
			try {
				Thread.sleep(1000);
				List<Entry<Integer, AppTx>> timeoutTxs = txs.getAllItems(new Func<AppTx, Boolean>() {
					@Override
					public Boolean exec(AppTx obj) {
						return obj.getElapseMs() > TIMEOUT;
					}
				});
// 在这里把serverTransaction超时处理完,应该没有遗漏了, 
				for (Entry<Integer, AppTx> e : timeoutTxs) {
//					txs.remove(e.getKey()); tx terminate 事件里完成
					final AppTx tx = e.getValue();
					Runnable termTx = new Runnable() {
						@Override
						public void run() {
							tx.getCurrentApp().getSessionCounters().getSessionTimeoutCount().increase();
							String msg = "ServerTimeout:" + tx.getElapseMs() + "ms";
							tx.getCurrentApp().LOGGER.error(msg);
							try {
								tx.setError(new TimeoutException(msg));
							}finally {
								tx.terminate();//放在最前保证 tx释放能被确定执行。
							}

						}
					};
					try {
						tx.getCurrentApp().getExecutor().execute(termTx);
					} catch (RejectedExecutionException re) {
						termTx.run();
					}

				}
			} catch (Exception ex) {
				LOGGER.error("monitorProc failed {}", ex);
			}
		}
	}
}
