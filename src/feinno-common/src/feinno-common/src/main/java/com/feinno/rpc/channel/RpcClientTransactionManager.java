/*
 * FAE, Feinno App Engine
 *  
 * Create by 李会军 2011-1-19
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel;

import java.util.Map.Entry;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.threading.ExecutorFactory;
import com.feinno.util.Func;
import com.feinno.util.container.SessionPool;

/**
 * RPC客户端事务管理器,<br> 
 * <li>管理客户端事务, 通过SessionPool</li><br>
 * <li>管理客户端访问缓存RpcClientMethodCache</li><br>
 * 
 * @see RpcClientTransaction
 * @see RpcClientMethodCache
 * @see RpcClientMethodCache
 * @author gaolei@feinno.com
 */
public final class RpcClientTransactionManager
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientTransactionManager.class);
	public static final RpcClientTransactionManager INSTANCE = new RpcClientTransactionManager();
	public static final Executor CALLBACK_EXECUTOR = ExecutorFactory.newFixedExecutor("callback", 300, 6400);
	
	private SessionPool<RpcClientTransaction> txs;
	private Thread thread;
	
	
	private RpcClientTransactionManager()
	{
		txs = new SessionPool<RpcClientTransaction>();

		thread = new Thread(new Runnable() {
			@Override
			public void run()
			{
				checkTransactions();
			}
		});
		thread.setDaemon(true);
		thread.start();
	}
	
	public int addTransaction(RpcClientTransaction tx)
	{
		return txs.add(tx);
	}
	
	public RpcClientTransaction getTransaction(int seq)
	{
		return txs.get(seq);
	}
	
	public RpcClientTransaction removeTransaction(int seq)
	{
		return txs.remove(seq);
	}
	
	public void checkTransactions()
	{
		while (true) {
			try {
				Thread.sleep(500);
				Func<RpcClientTransaction, Boolean> funcConnectionBroken = new Func<RpcClientTransaction, Boolean>() {
					@Override
					public Boolean exec(RpcClientTransaction tx)
					{
						if (tx.getConnection().isClosed()) {
							return true;
						} else {
							return false;
						}
					}
				};
				
				Func<RpcClientTransaction, Boolean> funcTimeout = new Func<RpcClientTransaction, Boolean>() {
					@Override
					public Boolean exec(RpcClientTransaction tx)
					{
						return tx.isTimeout();
					}
				};				
				
				for (Entry<Integer, RpcClientTransaction> k: txs.getAllItems(funcConnectionBroken)) {
					RpcResponse response = RpcResponse.createError(RpcReturnCode.CONNECTION_BROKEN, null);
					txs.remove(k.getKey());
					try {
						k.getValue().setResponse(response);
					} catch (Exception e) {
						LOGGER.error("setResponse failed{}", e);
					}
				}
				
				for (Entry<Integer, RpcClientTransaction> k: txs.getAllItems(funcTimeout)) {
					RpcResponse response = RpcResponse.createError(RpcReturnCode.TRANSACTION_TIMEOUT, null);
					txs.remove(k.getKey());
					try {
						k.getValue().setResponse(response);
					} catch (Exception e) {
						LOGGER.error("setResponse failed{}", e);
					}
				}
			} catch (InterruptedException e) {
				// nothing
			} catch (Exception e) {
				LOGGER.error("check transction failed:", e);
			}
		}
	};
}