/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 16, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.controller;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAWorkerControllerRegisterArgs extends ProtoEntity
{
	@ProtoMember(1)
	private int pid;
	
	@ProtoMember(2)
	private String workerId;

	public int getPid()
	{
		return pid;
	}

	public void setPid(int pid)
	{
		this.pid = pid;
	}

	public String getWorkerId()
	{
		return workerId;
	}

	public void setWorkerId(String workerId)
	{
		this.workerId = workerId;
	}
	
	
}
