/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Aug 4, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.server.builtin;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcNegociateResults extends ProtoEntity
{
	@ProtoMember(1)
	private int toId;
	
	@ProtoMember(2)
	private int fromId;

	public int getToId()
	{
		return toId;
	}

	public void setToId(int toId)
	{
		this.toId = toId;
	}

	public int getFromId()
	{
		return fromId;
	}

	public void setFromId(int fromId)
	{
		this.fromId = fromId;
	}
}
