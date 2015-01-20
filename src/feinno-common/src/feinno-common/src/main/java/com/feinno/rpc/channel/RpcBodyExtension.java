/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-15
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * Rpc包体扩展字段
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcBodyExtension extends ProtoEntity
{
	/**
	 * 扩展字段序号
	 */
	@ProtoMember(value = 1, required = true)
	private int id;
	
	/**
	 * 扩展字段长度，
	 */
	@ProtoMember(value = 2, required = true)
	private int length;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}
}
