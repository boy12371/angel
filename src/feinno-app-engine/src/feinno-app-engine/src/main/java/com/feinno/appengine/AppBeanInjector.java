/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine;

import java.util.concurrent.TimeoutException;

import com.feinno.util.Action;

/**
 * 维护一个AppBeanHandler的调用链
 * 
 * @author 高磊 gaolei@feinno.com
 */
public final class AppBeanInjector implements AppTxHandler {
	private static final class Node {
		private AppTxHandler handler;
		private AppBeanHandlerMode mode;
		private Node next;

		private Node(AppTxHandler handler, AppBeanHandlerMode mode, Node next) {
			this.handler = handler;
			this.mode = mode;
			this.next = next;
		}
	}

	private Node head;
	private AppBean bean;

	public AppBeanInjector(AppBean bean) {
		this.bean = bean;
		this.head = new Node(bean, AppBeanHandlerMode.RUN_UNTIL_TERMINATED, null);
	}

	public void addBeforeHandler(Class<? extends AppBeanHandler> clazz, String... params) throws Exception {
		AppBeanHandler handler = (AppBeanHandler) clazz.newInstance();
		AppBeanHandlerMode mode = handler.getMode();
		handler.setParams(params);

		if (!mode.canUsedWithBefore())
			throw new IllegalArgumentException("mode can't used with BeforeHandler:" + mode);
		//
		// 放置在链表前
		Node n = new Node(handler, mode, head);
		head = n;
	}

	public void addAfterHandler(Class<? extends AppBeanHandler> clazz, String... params) throws Exception {
		AppBeanHandler handler = (AppBeanHandler) clazz.newInstance();
		AppBeanHandlerMode mode = handler.getMode();
		handler.setParams(params);

		if (!mode.canUsedWithAfter())
			throw new IllegalArgumentException("mode can't used with AfterHandler:" + mode);

		Node p = head;
		while (p.next != null) {
			p = p.next;
		}
		p.next = new Node(handler, mode, null);
	}

	/*
	 * @see
	 * com.feinno.appengine.AppTxHandler#processTx(com.feinno.appengine.AppTx,
	 * com.feinno.util.Action)
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @param tx
	 * @param callback
	 */
	@Override
	public void processTx(AppTx tx, Action<Exception> callback) {
		processInner(head, tx, callback);
	}

	private void processInner(final Node n, final AppTx tx, final Action<Exception> callback) {
		if (n == null) {
			callback.run(tx.error());
			return;
		}
		try {
			bean.LOGGER.info("begin to run handler {}", n.handler);
			n.handler.processTx(tx, new Action<Exception>() {
				@Override
				public void run(Exception e) {
					if (e != null) {
						// TODO logging
						bean.LOGGER.error("run failed handler : " + n.handler + " error:" + e.getMessage(), e);
					}

					bean.LOGGER.info("bean status {} {}", tx.terminated(), tx.error());

					//
					// 增加服务器端的超时判断机制, tx超时的处理转移到AppTxManager里面完成
					if (tx.isServerTimeout()) {
						String msg = "ServerTimeout:" + tx.getElapseMs() + "ms";
						bean.LOGGER.error(msg);
						try {
							tx.setError(new TimeoutException(msg));
						} finally {
							tx.terminate();
							callback.run(tx.error());
						}
						// callback.run(new TimeoutException("ServerTimeout:" +
						// tx.getElapseMs() + "ms"));
						return;
					}

					if (n.mode.canRun(tx.terminated(), tx.error() != null)) {
						processInner(n.next, tx, callback);
					} else {
						bean.LOGGER.info("tx process over");
						callback.run(tx.error());
					}
				}
			});
		} catch (Exception a) {
			bean.LOGGER.error("run failed handler : " + n.handler + " error:" + a.getMessage(), a);
			if (n.mode.canRun(tx.terminated(), tx.error() != null)) {
				processInner(n.next, tx, callback);
			} else {
				bean.LOGGER.info("tx process over");
				callback.run(tx.error());
			}
		}
	}
}
