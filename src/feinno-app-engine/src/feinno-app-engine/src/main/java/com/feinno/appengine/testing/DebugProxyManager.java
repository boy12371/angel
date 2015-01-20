package com.feinno.appengine.testing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.AppTx;
import com.feinno.appengine.AppTxWithContext;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.rpc.channel.tcp.RpcTcpConnection;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.util.EventHandler;

/**
 * DebugProxy的管理类
 * 
 * @author lvmingwei
 * 
 */
public class DebugProxyManager {

	/**
	 * 用于存储DebugProxy信息的集合，当客户端IDE链接上来，会创建一个对应得DebugProxy到该集合中，当链接断开后，从集合中移除，
	 * 当某个应用有请求进来，则判断是否符合DebugProxy的条件,如果符合，则通过此DebugProxy链接到相应得客户端
	 */
	private static final List<DebugProxy<?>> DEBUG_PROXY_LIST = Collections
			.synchronizedList(new LinkedList<DebugProxy<?>>());

	/** 日志引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DebugProxyManager.class);

	/**
	 * 此方法与compareAndSet类似，如果isCan满足，则运行Debug，并返回true，否则不运行Debug，并返回false
	 * 
	 * @param categoryMinusName
	 * @param rpcServerContext
	 * @param appContext
	 * @param request
	 * @return
	 */
	public static boolean isCanAndRun(String categoryMinusName, RpcServerContext rpcServerContext, AppTx tx) {
		LOGGER.info("invoke DebugProxyManager.isCanAndRun().");
		try {
			// Step 1.如果在appengine.properties中没有开启debugmode或者没有IDE注入进来，则返回匹配失败
			if (!AppEngineManager.INSTANCE.getSettings().isDebugMode() || DEBUG_PROXY_LIST.size() == 0) {
				return false;
			}

			// Step 2.寻找合适得DebugProxy
			// TODO 后续需要考虑当多个IDE挂到统一个应用上，并且灰度相同得情况，这样需要给其他的DebugProxy发去已有人使用的应答
			@SuppressWarnings("unchecked")
			DebugProxy<?> debugProxy = getDebugProxy(categoryMinusName,
					((AppTxWithContext<AppContext>) tx).getContext());

			// Step 3.开始进行debug
			if (debugProxy != null) {
				try {
					debugProxy.debug(rpcServerContext, tx);
					return true;
				} catch (Exception e) {
					LOGGER.error("DebugProxy run failed.", e);
				}
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Invoke DebugProxyManager.isCanAndRun failed.categoryMinusName=%s",
					categoryMinusName), e);
		}

		return false;
	}

	/**
	 * 判断某一个应用是否可以调用Debug
	 * 
	 * @param categoryMinusName
	 * @param appContext
	 * @return
	 */
	public static boolean isCan(String categoryMinusName, AppContext appContext) {
		LOGGER.info("invoke DebugProxyManager.isCan().");
		try {
			// Step 1.如果在appengine.properties中没有开启debugmode或者没有IDE注入进来，则返回匹配失败
			if (!AppEngineManager.INSTANCE.getSettings().isDebugMode() || DEBUG_PROXY_LIST.size() == 0) {
				return false;
			}
			// 如果有DebugProxy，则返回true，否则返回false
			return getDebugProxy(categoryMinusName, appContext) != null ? true : false;
		} catch (Exception e) {
			LOGGER.error(
					String.format("Invoke DebugProxyManager.isCan failed.categoryMinusName=%s", categoryMinusName), e);
		}
		return false;
	}

	/**
	 * 获取指定应用的DebugProxy
	 * 
	 * @param categoryMinusName
	 * @param appContext
	 * @return
	 */
	private static DebugProxy<?> getDebugProxy(String categoryMinusName, AppContext appContext) {
		// Step 1.锁住DEBUG_PROXY_LIST对象，在其中寻找符合条件得DebugProxy，如果没有，则返回空
		synchronized (DEBUG_PROXY_LIST) {
			for (DebugProxy<?> debugProxy : DEBUG_PROXY_LIST) {
				if (debugProxy.isCan(categoryMinusName, appContext)) {
					return debugProxy;
				}
			}
		}
		return null;
	}

	/**
	 * 添加一个DebugProxy
	 * 
	 * @param debugProxy
	 * @return
	 */
	public static boolean addDebugProxy(DebugProxy<?> debugProxy) {
		if (debugProxy == null) {
			return false;
		}
		LOGGER.info("Add DebugProxy {}", debugProxy);
		// 当链接创建并且加入Debug集合后，有移除策略
		debugProxy.getConnection().setAttachment(debugProxy);
		debugProxy.getConnection().getDisconnected().addListener(new EventHandler<Throwable>() {
			public void run(Object sender, Throwable e) {
				RpcTcpConnection conn = (RpcTcpConnection) sender;
				DebugProxy<?> debugProxy = (DebugProxy<?>) conn.getAttachment();
				LOGGER.warn("DebugProxy Disconnected. categoryMinusName:{} Connection:{}.",
						debugProxy.getCategoryMinusName(), conn);
				DEBUG_PROXY_LIST.remove(debugProxy);
				// 最终销毁这个DebugProxy
				debugProxy.destroy();
			}

		});
		DEBUG_PROXY_LIST.add(debugProxy);
		return true;
	}

	/**
	 * 清理全部DebugProxy，可以用于从异常状态的恢复
	 */
	public static void clear() {
		DEBUG_PROXY_LIST.clear();
	}
}
