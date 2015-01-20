/*
 * FAE, Feinno App Engine
 * 
 * Create by gaolei 2011-1-19
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.http;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.feinno.rpc.channel.RpcClientChannel;
import com.feinno.rpc.channel.RpcEndpoint;

/**
 * RpcOverHttp的地址抽象类
 * 
 * TODO
 * 
 * @author 黄湘龙 huangxianglong@feinno.com
 */

public class RpcHttpEndpoint extends RpcEndpoint
{
	private InetSocketAddress address;

	// private String url;

	/**
	 * 
	 * 通过反射创建, 必须提供默认构造函数
	 */
	public RpcHttpEndpoint()
	{
	}

	/**
	 * {在这里补充功能说明}
	 * 
	 * @param address
	 */
	public RpcHttpEndpoint(InetSocketAddress address)
	{
		this.address = address;
	}

	/**
	 * {在这里补充功能说明}
	 * 
	 * @return
	 */
	public InetSocketAddress getInetSocketAddress()
	{
		return address;
	}

	/**
	 * {在这里补充功能说明}
	 * 
	 * @param uri
	 * @return
	 */
	public static RpcHttpEndpoint parse(String uri)
	{
		if (uri.startsWith("http://"))
			return parse(uri, "http://".length());
		else if (uri.startsWith("http:"))
			return parse(uri, "http:".length());
		else if (uri.startsWith("https://"))
			return parse(uri, "https://".length());
		else if (uri.startsWith("https:"))
			return parse(uri, "https:".length());
		else
			throw new IllegalArgumentException("Unreconized http uri:" + uri);
	}

	/**
	 * {在这里补充功能说明}
	 * 
	 * @param uri
	 * @param start
	 * @return
	 */
	public static RpcHttpEndpoint parse(String uri, int start)
	{
		int p = uri.indexOf(':', start);
		int end = uri.indexOf('/', start);
		if (end < 0)
			end = uri.length();

		String ip = uri.substring(start, p);
		String portstr = uri.substring(p + 1, end);

		int port = Integer.parseInt(portstr);
		return new RpcHttpEndpoint(new InetSocketAddress(ip, port));
	}

	/*
	 * @see com.feinno.rpc.RpcEndpoint#toString()
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @return
	 */
	@Override
	public String toString()
	{
		return  "http://" + address + "/";
	}

	/*
	 * @see com.feinno.rpc.RpcEndpoint#equals(java.lang.Object)
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RpcHttpEndpoint other = (RpcHttpEndpoint) obj;
		if (address == null)
		{
			if (other.address != null)
				return false;
		}
		else if (!address.equals(other.address))
			return false;
		return true;
	}

	/*
	 * @see com.feinno.rpc.RpcEndpoint#hashCode()
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @return
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	/*
	 * @see com.feinno.rpc.RpcEndpoint#getProtocol()
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @return
	 */
	@Override
	public String getProtocol()
	{
		return "http";
	}

	/*
	 * @see com.feinno.rpc.RpcEndpoint#getClientChannel()
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @return
	 */
	@Override
	public RpcClientChannel getClientChannel()
	{
		return RpcHttpClientChannel.INSTANCE;
	}

	public int getPort()
	{
		return address.getPort();
	}

	public InetAddress getAddress()
	{
		return address.getAddress();
	}

	/*
	 * @see com.feinno.rpc.RpcEndpoint#parseWithInner(java.lang.String)
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @param strWithoutProtocol
	 */
	@Override
	protected void parseAddress(String strWithoutProtocol)
	{
		int p = strWithoutProtocol.indexOf(':');
		int end = strWithoutProtocol.indexOf('/');

		if (end < 0)
			end = strWithoutProtocol.length();

		String ip = strWithoutProtocol.substring(0, p);
		String portstr = strWithoutProtocol.substring(p + 1, end);

		int port = Integer.parseInt(portstr);
		address = new InetSocketAddress(ip, port);
	}
}
