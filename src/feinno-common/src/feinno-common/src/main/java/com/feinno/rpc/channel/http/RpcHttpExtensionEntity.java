package com.feinno.rpc.channel.http;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class RpcHttpExtensionEntity extends ProtoEntity
{
	@ProtoMember(1)
	private int id;
	
	@ProtoMember(2)
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
