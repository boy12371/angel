/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-20
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.configuration;

import java.util.Date;
import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * 配置文本
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAConfigTextBuffer extends ProtoEntity
{
	@ProtoMember(1)
	private String text;

	@ProtoMember(2)
	private List<String> configParams;

	@ProtoMember(3)
	private Date version;

	public HAConfigTextBuffer()
	{
	}

	public HAConfigTextBuffer(String text)
	{
		this.text = text;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public void setConfigParams(List<String> configParams)
	{
		this.configParams = configParams;
	}

	public List<String> getConfigParams()
	{
		return configParams;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
	
}
