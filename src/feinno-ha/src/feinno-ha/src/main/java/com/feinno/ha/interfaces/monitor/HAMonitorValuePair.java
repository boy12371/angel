/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei May 14, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.monitor;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAMonitorValuePair extends ProtoEntity
{
	@ProtoMember(value = 1, required = true)
	private int key;
	
	@ProtoMember(value = 2, required = true)
	private float value;
	
	@ProtoMember(value = 3, required = false)
	private String alarm;

	public int getKey()
	{
		return key;
	}

	public void setKey(int key)
	{
		this.key = key;
	}

	public float getValue()
	{
		return value;
	}

	public void setValue(float value)
	{
		this.value = value;
	}

	public String getAlarm()
	{
		return alarm;
	}

	public void setAlarm(String alarm)
	{
		this.alarm = alarm;
	}
}
