package com.feinno.diagnostic.observation;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class ObserverReportRowBean extends ProtoEntity
{
	@ProtoMember(1)
	private String instance;
	@ProtoMember(2)
	private String[] data;

	public String getInstance()
	{
		return instance;
	}

	public void setInstance(String instance)
	{
		this.instance = instance;
	}

	public String[] getData()
	{
		return data;
	}

	public void setData(String[] data)
	{
		this.data = data;
	}
}
