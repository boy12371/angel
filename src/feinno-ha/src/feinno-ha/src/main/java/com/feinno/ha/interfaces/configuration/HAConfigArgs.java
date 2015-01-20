package com.feinno.ha.interfaces.configuration;

import java.util.Date;

import com.feinno.configuration.ConfigType;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * 
 * 用于所有配置接口的HAConfigArgs 1.
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAConfigArgs extends ProtoEntity
{
	@ProtoMember(1)
	private ConfigType type;

	@ProtoMember(2)
	private String path;

	@ProtoMember(3)
	private String params;

	@ProtoMember(4)
	private Date version;

	public ConfigType getType()
	{
		return type;
	}

	public void setType(ConfigType type)
	{
		this.type = type;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public void setParams(String params)
	{
		this.params = params;
	}

	public String getParams()
	{
		return this.params;
	}

	public Date getVersion()
	{
		return version;
	}

	public void setVersion(Date version)
	{
		this.version = version;
	}
}
