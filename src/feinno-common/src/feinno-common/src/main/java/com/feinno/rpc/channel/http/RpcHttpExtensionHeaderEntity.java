package com.feinno.rpc.channel.http;

import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class RpcHttpExtensionHeaderEntity extends ProtoEntity
{
	@ProtoMember(1)
	private int bodyLength;
	
	@ProtoMember(2)
	private List<RpcHttpExtensionEntity> extensions; 

	public int getBodyLength()
    {
    	return bodyLength;
    } 

	public void setBodyLength(int bodyLength)
    {
    	this.bodyLength = bodyLength;
    } 

	public List<RpcHttpExtensionEntity> getExtensions()
    {
    	return extensions;
    } 

	public void setExtensions(List<RpcHttpExtensionEntity> extensions)
    {
    	this.extensions = extensions;
    }
	 
}
