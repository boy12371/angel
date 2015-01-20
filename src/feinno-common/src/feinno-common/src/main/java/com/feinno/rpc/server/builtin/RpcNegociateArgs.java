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
public class RpcNegociateArgs extends ProtoEntity
{
	@ProtoMember(1)
	private String toService;
	
	@ProtoMember(2)
	private String toMethod;
	
	@ProtoMember(3)
	private String fromService;
	
	@ProtoMember(4)
	private String fromComputer;

	public String getToService()
	{
		return toService;
	}

	public void setToService(String toService)
	{
		this.toService = toService;
	}

	public String getToMethod()
	{
		return toMethod;
	}

	public void setToMethod(String toMethod)
	{
		this.toMethod = toMethod;
	}

	public String getFromService()
	{
		return fromService;
	}

	public void setFromService(String fromService)
	{
		this.fromService = fromService;
	}

	public String getFromComputer()
	{
		return fromComputer;
	}

	public void setFromComputer(String fromComputer)
	{
		this.fromComputer = fromComputer;
	}
}
