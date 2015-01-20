package com.feinno.ha.interfaces.configuration;

import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class HAConfigTableRow extends ProtoEntity
{
	@ProtoMember(1)
	private List<String> values;

	public List<String> getValues()
	{
		return values;
	}

	public void setValues(List<String> values)
	{
		this.values = values;
	}

	public String getValue(int i)
	{
		return values.get(i);
	}
}
