/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-3-2
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel;

//import com.feinno.imps.api.ImpsResultCode;
import com.feinno.serialization.protobuf.types.ProtoString;
import com.feinno.threading.Future;
import com.feinno.util.EventHandler;
import com.feinno.util.TraceContext;

/**
 * Rpc调用结果返回，可用作异步处理
 * 
 * @see com.feinno.threading.Future
 * @author 高磊 gaolei@feinno.com
 */
public class RpcFuture extends Future<RpcResults>
{
	RpcFuture()
	{
	}
	

	/*
	@Override
	public void addListener(final EventHandler<RpcResults> handler)
	{
		super.addListener(new EventHandler<RpcResults>() {
			
			@Override
			public void run(Object sender, RpcResults result) {
				if (result.getResponse() != null)
				{
					TraceContext context = TraceContext.GetContext();
					ProtoString traceToken = result.getResponse().getExtension(TraceContext.EXTENSION_CONTEXT_TRACECTOKEN, ProtoString.class);
					if (traceToken != null)
					{
						TraceContext.Apply(traceToken.getValue());
					}
				}
				handler.run(sender, result);
			}
		});
	}
*/

	/**
	 * 
	 * 获取返回码
	 * @return
	 */
	public RpcReturnCode getReturnCode()
	{
		if (isDone()) {
			return getValue().getReturnCode();
		} else {
			throw new IllegalStateException("future not done!");
		}
	}

	/**
	 * 
	 * 获取错误
	 * @return
	 */
	public RpcException getError()
	{
		if (isDone()) {
			return getValue().getError();
		} else {
			throw new IllegalStateException("future not done!");
		}
	}

	/**
	 * 
	 * 获取返回结果, 需要先检查getError, 如果未完成会抛出IllegalStateException,  
	 * @param clazz 反序列化的结果类型
	 * @return
	 * @throws IllegalStateException
	 */
	public <E> E getResult(Class<E> clazz) throws RpcException
	{
		if (isDone()) {
			RpcResults r = this.getValue();
			if (r == null) {
				String msg = "RpcResults is empty. Maybe 'Future' waiting for rpc invoke timeout.'Future' default wait time is %s ms.";
				throw new RpcException(String.format(msg, Future.TIMEOUT));
			}
			if (r.getError() != null) {
				throw r.getError();
			} else {
				return r.getValue(clazz);
			}
		} else {
			throw new IllegalStateException("future not done!");
		}
	}

	/**
	 * 
	 * 同步获取结果, 超时时间设置为
	 * @param clazz
	 * @return
	 * @throws RpcException
	 */
	public <E> E syncGet(Class<E> clazz) throws RpcException
	{
		if (!isDone()) {
			await();
		}
		RpcResults r = this.getValue();
		if (r == null) {
			String msg = "RpcResults is empty. Maybe 'Future' waiting for rpc invoke timeout.'Future' default wait time is %s ms.";
			throw new RpcException(String.format(msg, Future.TIMEOUT));
		}
		if (r.getError() != null) {
			throw r.getError();
		} else {
			return r.getValue(clazz);
		}
	}
}