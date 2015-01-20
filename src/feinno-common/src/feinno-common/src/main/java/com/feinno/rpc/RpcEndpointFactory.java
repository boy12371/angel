/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-31
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc;

import java.util.HashMap;
import java.util.Map;

import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.http.RpcHttpEndpoint;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.channel.uds.RpcUdsEndpoint;
import com.feinno.util.Outter;
import com.feinno.util.StringUtils;

/**
 * 
 * <b>描述: </b>这是一个{@link RpcEndpoint}的静态工厂类，它可以通过字符串，动态选定{@link RpcEndpoint}
 * 的准确类型，例如tcp、http，关于{@link RpcEndpoint}的细节请参考{@link RpcEndpoint},关于
 * {@link RpcEndpoint}的使用请参考{@link RpcProxyFactory}
 * <p>
 * <b>功能: </b>{@link RpcEndpoint}的静态工厂类，它可以通过字符串，动态选定{@link RpcEndpoint}
 * 的准确类型，例如tcp、http
 * <p>
 * <b>用法: </b>关于{@link RpcEndpoint}的使用请参考{@link RpcProxyFactory}的示例部分
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 * 
 */
public class RpcEndpointFactory
{
	private static Map<String, Class<? extends RpcEndpoint>> epTypes;

	static
	{
		epTypes = new HashMap<String, Class<? extends RpcEndpoint>>();
		
		//
		// 初始化默认的Endpoint Types
		epTypes.put("tcp", RpcTcpEndpoint.class);
		epTypes.put("inproc", RpcTcpEndpoint.class);
		epTypes.put("uds", RpcUdsEndpoint.class);
		epTypes.put("http", RpcHttpEndpoint.class);
		epTypes.put("https", RpcHttpEndpoint.class);
	}
	
	public static void registerEpType(String protocol, Class<? extends RpcEndpoint> epClazz)
	{
		epTypes.put(protocol, epClazz);		
	}
	
	public static RpcEndpoint parse(String str)
	{
		Outter<String> protocol = new Outter<String>();
		Outter<String> left = new Outter<String>();
		if (StringUtils.splitWithFirst(str, "://", protocol, left)) {
			Class<? extends RpcEndpoint> epClazz = epTypes.get(protocol.value());
			if (epClazz == null) {
				throw new IllegalArgumentException("Unknown uri protocol:" + str);
			}
			RpcEndpoint ep;
			try {
				ep = (RpcEndpoint)epClazz.newInstance();
			} catch (Exception e) {
				throw new IllegalArgumentException("Bad Uri Type:" + epClazz.toString());	
			}		
			ep.parseWith(left.value());
			return ep;
		} else {
			throw new IllegalArgumentException("Badformat Uri:" + str);
		}
	}
}
