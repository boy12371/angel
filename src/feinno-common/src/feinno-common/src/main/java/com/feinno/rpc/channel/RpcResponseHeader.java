/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-15
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.rpc.channel;

import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * Rpc应答protobuf实体类
 * <hr>
 *
<code>
 message RpcResponseHeader {<br>
  required int32 Sequence = 1;       // 事务序号, 客户端生成, 活动事务唯一，可复用<br>
  required int32 ResponseCode = 2;   // 返回码<br>
  required int32 BodyLength = 3;     // 包体长度: 0代表传空，1代表全默认，len-1为实际长度<br>
  optional int32 Option = 4;         // 消息可选项<br>
  optional int32 ToId = 5;           // 用于优化消息长度的交换后id, 针对服务器端为单服务器上有效<br>
  optional int32 FromId = 6;         // 用于优化消息长度的交换后id, 针对服务器端为单服务器上有效<br>
}<br>
</code>
 *
 * @author 高磊 gaolei@feinno.com
 */
public class RpcResponseHeader extends ProtoEntity
{
	@ProtoMember(value = 1, required = true)
	private int sequence;
	
	@ProtoMember(value = 2, required = true)
	private int responseCode;
	
	@ProtoMember(value = 3, required = true)
	private int bodyLength;
	
	@ProtoMember(value = 4, required = false)
	private int option;
	
	@ProtoMember(value = 5, required = false)
	private int toId;
	
	@ProtoMember(value = 6, required = false)
	private int fromId;
	
	@ProtoMember(value = 7, required = false)
	private List<RpcBodyExtension> extensions;

	public int getSequence()
	{
		return sequence;
	}

	public void setSequence(int sequence)
	{
		this.sequence = sequence;
	}

	public int getResponseCode()
	{
		return responseCode;
	}

	public void setResponseCode(int responceCode)
	{
		this.responseCode = responceCode;
	}

	public int getBodyLength()
	{
		return bodyLength;
	}

	public void setBodyLength(int bodyLength)
	{
		this.bodyLength = bodyLength;
	}

	public int getOption()
	{
		return option;
	}

	public void setOption(int option)
	{
		this.option = option;
	}

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

	public void setExtensions(List<RpcBodyExtension> extensions)
	{
		this.extensions = extensions;
	}

	public List<RpcBodyExtension> getExtensions()
	{
		return extensions;
	}
}
