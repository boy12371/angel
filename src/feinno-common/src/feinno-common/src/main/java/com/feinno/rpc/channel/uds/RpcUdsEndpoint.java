/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-7-30
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel.uds;

import com.feinno.rpc.channel.RpcClientChannel;
import com.feinno.rpc.channel.RpcEndpoint;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 刘丹 liudan@feinno.com
 */
public class RpcUdsEndpoint extends RpcEndpoint
{
	public static final RpcEndpoint EMPTY = null;
	
	private String path;

	public RpcUdsEndpoint(String path)
	{
		this.path = path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.feinno.rpc.RpcEndpoint#toString()
	 */
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return "uds://" + path;
	}
	
	public String getPath()
	{
		return path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.feinno.rpc.RpcEndpoint#parseWithInner(java.lang.String)
	 */
	@Override
	protected void parseAddress(String strWithoutProtocol)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.feinno.rpc.RpcEndpoint#getProtocol()
	 */
	@Override
	public String getProtocol()
	{
		// TODO Auto-generated method stub
		return "uds";
	}

	public String getAddress()
	{
		return path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.feinno.rpc.RpcEndpoint#getClientChannel()
	 */
	@Override
	public RpcClientChannel getClientChannel()
	{
		// TODO Auto-generated method stub
		return RpcUdsClientChannel.INSTANCE;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RpcUdsEndpoint other = (RpcUdsEndpoint) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
}
