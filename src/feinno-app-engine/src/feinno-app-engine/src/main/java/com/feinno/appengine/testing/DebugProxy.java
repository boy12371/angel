package com.feinno.appengine.testing;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.AppTx;
import com.feinno.appengine.route.gray.CondBuilder;
import com.feinno.appengine.route.gray.ParserException;
import com.feinno.appengine.route.gray.conds.Cond;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcResponse;
import com.feinno.rpc.duplex.RpcDuplexClientAgent;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.threading.Future;

/**
 * 用于Debug的代理
 * 
 * @author lvmingwei
 * 
 */
public abstract class DebugProxy<R> extends RpcDuplexClientAgent {

	/**
	 * Debug应用名称
	 */
	protected String categoryMinusName;

	/**
	 * Debug应用的灰度字符串
	 */
	protected String grayFactors;

	/**
	 * Debug应用的灰度对象
	 */
	protected Cond cond;

	/**
	 * 构造方法，传入应用名称、灰度及客户端链接
	 * 
	 * @param categoryMinusName
	 * @param grayFactors
	 * @param client
	 */
	protected DebugProxy(String categoryMinusName, String grayFactors, RpcServerContext ctx) {
		super(ctx);
		this.categoryMinusName = categoryMinusName;
		try {
			this.grayFactors = grayFactors;
			this.cond = grayFactors != null && grayFactors.length() > 0 ? CondBuilder.parse(grayFactors) : null;
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否是能够执行此DebugProxy
	 * 
	 * @param categoryMinusName
	 * @param ctx
	 * @return
	 */
	public boolean isCan(String categoryMinusName, AppContext ctx) {
		// 如果应用名称不一样，那么返回false
		if (!this.categoryMinusName.equals(categoryMinusName)) {
			return false;
		}
		// 如果设置了灰度，但是灰度不匹配，那么返回false
		if (cond != null && ctx != null && !cond.apply(ctx)) {
			return false;
		}
		// 以上条件满足，则返回true
		return true;
	}

	/**
	 * 获得该DebugProxy对应得应用名称
	 * 
	 * @return
	 */
	public String getCategoryMinusName() {
		return categoryMinusName;
	}

	/**
	 * 获得灰度信息
	 * 
	 * @return
	 */
	public String getGrayFactors() {
		return grayFactors;
	}

	/**
	 * 进行debug
	 * 
	 * @param rpcServerContext
	 * @param appContext
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public boolean debug(RpcServerContext rpcServerContext, AppTx tx) throws Exception {
		Future<R> future = invoke(rpcServerContext, tx);
		return complete(rpcServerContext, future);
	}

	/**
	 * 处理debug开始得操作
	 * 
	 * @param ctx
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected abstract Future<R> invoke(RpcServerContext rpcServerContext, AppTx tx) throws Exception;

	/**
	 * 处理debug结束后的返回值的操作
	 * 
	 * @param ctx
	 * @param future
	 * @return
	 * @throws Exception
	 */
	protected boolean complete(RpcServerContext ctx, Future<R> future) throws Exception {
		if (!(future instanceof RpcFuture)) {
			return false;
		}
		RpcResponse response = ((RpcFuture) future).getValue().getResponse();
		ctx.endWithResponse(response);
		return true;
	}

	/**
	 * 销毁当前的DebugProxy
	 */
	public void destroy() {
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{CategoryMinusName:").append(categoryMinusName).append(",");
		sb.append("GrayFactors:").append(grayFactors).append(",");
		sb.append("Connection:").append(this.getConnection()).append("}");
		return sb.toString();
	}
}
